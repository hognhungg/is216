package com.thuexe.dao;

import com.thuexe.dto.XeDTO;
import com.thuexe.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XeDAO {

    // 1. Lấy toàn bộ danh sách xe (Dùng SQL JOIN)
    public List<XeDTO> getAllXe() {
        List<XeDTO> list = new ArrayList<>();
        String sql = "SELECT x.*, l.TenLoai FROM XE x JOIN LOAIXE l ON x.MaLoaiXe = l.MaLoaiXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // 2. Thêm xe mới (Gọi PROC_INSERT_XE 7 tham số)
    public boolean themXe(XeDTO xe) {
        String sql = "{call PROC_INSERT_XE(?, ?, ?, ?, ?, ?, ?)}"; 
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, xe.getBienSoXe());
            stmt.setString(2, xe.getThuongHieu());
            stmt.setString(3, xe.getTenXe());
            stmt.setInt(4, xe.getSoCho());
            stmt.setInt(5, xe.getMaChuXe());
            stmt.setInt(6, xe.getMaLoaiXe());
            stmt.setInt(7, xe.getMaDoanhNghiep());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    // 3. Xóa xe theo Biển số (Gọi PROC_DELETE_XE)
    public boolean xoaXe(String bienSoXe) {
        String sql = "{call PROC_DELETE_XE(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, bienSoXe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    // 4. Tìm xe trống theo thời gian lịch trình (Gọi PROC_TIM_XE_TRONG)
    public List<XeDTO> timXeTrong(Timestamp ngayNhan, Timestamp ngayTra) {
        List<XeDTO> dsXe = new ArrayList<>();
        String sql = "{call PROC_TIM_XE_TRONG(?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, ngayNhan);
            stmt.setTimestamp(2, ngayTra);
            stmt.registerOutParameter(3, -10); // Hỗ trợ Oracle Cursor REFCURSOR (-10)
            stmt.execute();
            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                while (rs.next()) {
                    dsXe.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return dsXe;
    }

    // 5. Tìm kiếm xe nâng cao phối hợp nhiều bộ lọc động
    public List<XeDTO> timKiemXeNangCao(String keyword, int maLoaiXe, String trangThai) {
        List<XeDTO> list = new ArrayList<>();
        
        // Tạo chuỗi truy vấn động bằng StringBuilder
        StringBuilder sql = new StringBuilder(
            "SELECT x.*, l.TenLoai FROM XE x JOIN LOAIXE l ON x.MaLoaiXe = l.MaLoaiXe WHERE 1=1"
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(x.TenXe) LIKE ? OR LOWER(x.BienSoXe) LIKE ?)");
        }
        if (maLoaiXe > 0) {
            sql.append(" AND x.MaLoaiXe = ?");
        }
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            sql.append(" AND x.TrangThai = ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                String boundKey = "%" + keyword.toLowerCase() + "%";
                ps.setString(paramIndex++, boundKey);
                ps.setString(paramIndex++, boundKey);
            }
            if (maLoaiXe > 0) {
                ps.setInt(paramIndex++, maLoaiXe);
            }
            if (trangThai != null && !trangThai.trim().isEmpty()) {
                ps.setString(paramIndex++, trangThai);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🛠️ HÀM KHÉP KÍN: Ánh xạ dữ liệu từ ResultSet sang Đối tượng XeDTO chuẩn hóa
    private XeDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        XeDTO x = new XeDTO();
        x.setBienSoXe(rs.getString("BienSoXe"));
        x.setThuongHieu(rs.getString("ThuongHieu"));
        x.setTenXe(rs.getString("TenXe"));
        x.setSoCho(rs.getInt("SoCho"));
        x.setHinhAnh(rs.getString("HinhAnh"));       // Đã bổ sung gán thuộc tính hình ảnh
        x.setTrangThai(rs.getString("TrangThai"));
        x.setMaChuXe(rs.getInt("MaChuXe"));           // Đã bổ sung gán khóa ngoại Chủ xe
        x.setMaLoaiXe(rs.getInt("MaLoaiXe"));
        x.setMaDoanhNghiep(rs.getInt("MaDoanhNghiep")); // Đã bổ sung gán khóa ngoại Doanh nghiệp
        
        // Phòng ngừa trường hợp gọi hàm từ các câu lệnh SQL không kết nối bảng LOAIXE
        try {
            x.setTenLoai(rs.getString("TenLoai"));
        } catch (SQLException e) { 
            x.setTenLoai(""); 
        }
        return x;
    }
}