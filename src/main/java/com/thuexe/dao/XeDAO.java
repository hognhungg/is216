package com.thuexe.dao;

import com.thuexe.dto.XeDTO;
import com.thuexe.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XeDAO {

    // 1. Lấy danh sách (Dùng SQL JOIN)
    public List<XeDTO> getAllXe() {
        List<XeDTO> list = new ArrayList<>();
        String sql = "SELECT x.*, l.TenLoai FROM XE x JOIN LOAIXE l ON x.MaLoaiXe = l.MaLoaiXe";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. Thêm xe (Đã sửa khớp với PROC_INSERT_XE 7 tham số)
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

    // 3. Xóa xe (Khớp với PROC_DELETE_XE)
    public boolean xoaXe(String bienSoXe) {
        String sql = "{call PROC_DELETE_XE(?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, bienSoXe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // 4. Tìm xe trống (Khớp với PROC_TIM_XE_TRONG)
    public List<XeDTO> timXeTrong(Timestamp ngayNhan, Timestamp ngayTra) {
        List<XeDTO> dsXe = new ArrayList<>();
        String sql = "{call PROC_TIM_XE_TRONG(?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setTimestamp(1, ngayNhan);
            stmt.setTimestamp(2, ngayTra);
            stmt.registerOutParameter(3, -10); // Oracle Cursor
            stmt.execute();
            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                while (rs.next()) {
                    dsXe.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return dsXe;
    }

    private XeDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        XeDTO x = new XeDTO();
        x.setBienSoXe(rs.getString("BienSoXe"));
        x.setThuongHieu(rs.getString("ThuongHieu"));
        x.setTenXe(rs.getString("TenXe"));
        x.setSoCho(rs.getInt("SoCho"));
        x.setTrangThai(rs.getString("TrangThai"));
        x.setMaLoaiXe(rs.getInt("MaLoaiXe"));
        try {
            x.setTenLoai(rs.getString("TenLoai"));
        } catch (SQLException e) { /* Bỏ qua nếu ko join */ }
        return x;
    }
}