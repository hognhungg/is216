package com.thuexe.dao;

import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiXeDAO {
    
    // 1. Lấy danh sách loại xe (Đổ lên bảng và ComboBox)
    public List<LoaiXeDTO> getAllLoaiXe() {
        List<LoaiXeDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM LOAIXE";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new LoaiXeDTO(
                    rs.getInt("MaLoaiXe"), 
                    rs.getString("TenLoai"), 
                    rs.getString("LoaiNhienLieu"), 
                    rs.getDouble("GiaThueNgay"), 
                    rs.getDouble("GiaThueGio")
                ));
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // 2. Hàm Thêm loại xe
    public boolean insertLoaiXe(LoaiXeDTO loaiXe) {
        // MẸO: Nếu Database Oracle của ông dùng Sequence để tăng mã, hãy sửa đoạn đầu thành:
        // "INSERT INTO LOAIXE (MaLoaiXe, TenLoai,..." và VALUES (SEQ_LOAIXE.NEXTVAL, ?, ?, ?, ?)
        String sql = "INSERT INTO LOAIXE (TenLoai, LoaiNhienLieu, GiaThueNgay, GiaThueGio) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, loaiXe.getTenLoai());
            pst.setString(2, loaiXe.getLoaiNhienLieu());
            pst.setDouble(3, loaiXe.getGiaThueNgay());
            pst.setDouble(4, loaiXe.getGiaThueGio());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 3. Hàm Cập nhật giá thuê và thông tin loại xe
    public boolean updateLoaiXe(LoaiXeDTO loaiXe) {
        String sql = "UPDATE LOAIXE SET TenLoai=?, LoaiNhienLieu=?, GiaThueNgay=?, GiaThueGio=? WHERE MaLoaiXe=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, loaiXe.getTenLoai());
            pst.setString(2, loaiXe.getLoaiNhienLieu());
            pst.setDouble(3, loaiXe.getGiaThueNgay());
            pst.setDouble(4, loaiXe.getGiaThueGio());
            pst.setInt(5, loaiXe.getMaLoaiXe());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    // 4. 🛠️ BỔ SUNG HÀM XÓA LOẠI XE (Giải quyết lỗi gạch đỏ bên BUS)
    public boolean deleteLoaiXe(int maLoaiXe) {
        String sql = "DELETE FROM LOAIXE WHERE MaLoaiXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, maLoaiXe);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { 
            // Nếu dính lỗi khóa ngoại (do loại xe này đang có xe được lưu ở bảng XE), log sẽ thông báo cụ thể
            e.printStackTrace(); 
            return false; 
        }
    }
}