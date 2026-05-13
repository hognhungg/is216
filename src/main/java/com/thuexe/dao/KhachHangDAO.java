package com.thuexe.dao;

import com.thuexe.dto.KhachHangDTO;
import com.thuexe.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    // 1. Lấy toàn bộ danh sách khách hàng
    public List<KhachHangDTO> getAll() {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KHACHHANG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new KhachHangDTO(
                    rs.getInt("MaKhachHang"), rs.getString("HoTen"),
                    rs.getString("CCCD"), rs.getString("SoBangLai"),
                    rs.getString("SDT"), rs.getString("GioiTinh"),
                    rs.getDate("NgaySinh"), rs.getString("DiaChi"),
                    rs.getInt("MaChuThe")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 2. Cập nhật thông tin khách hàng
    public boolean update(KhachHangDTO kh) {
        String sql = "UPDATE KHACHHANG SET HoTen=?, CCCD=?, SoBangLai=?, SDT=?, GioiTinh=?, NgaySinh=?, DiaChi=? WHERE MaKhachHang=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getCccd());
            ps.setString(3, kh.getSoBangLai());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getGioiTinh());
            ps.setDate(6, new java.sql.Date(kh.getNgaySinh().getTime()));
            ps.setString(7, kh.getDiaChi());
            ps.setInt(8, kh.getMaKhachHang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}