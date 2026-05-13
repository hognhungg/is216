package com.thuexe.dao;

import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiXeDAO {
    
    // HÀM BỊ THIẾU GÂY RA LỖI: Lấy danh sách loại xe (Để đổ lên ComboBox)
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

    // Hàm Thêm loại xe
    public boolean insertLoaiXe(LoaiXeDTO loaiXe) {
        String sql = "INSERT INTO LOAIXE (TenLoai, LoaiNhienLieu, GiaThueNgay, GiaThueGio) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, loaiXe.getTenLoai());
            pst.setString(2, loaiXe.getLoaiNhienLieu());
            pst.setDouble(3, loaiXe.getGiaThueNgay());
            pst.setDouble(4, loaiXe.getGiaThueGio());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // Hàm Cập nhật giá thuê
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
        } catch (SQLException e) { return false; }
    }
}