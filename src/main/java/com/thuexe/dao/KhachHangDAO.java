package com.thuexe.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thuexe.dto.KhachHangDTO;
import com.thuexe.util.DBConnection;

public class KhachHangDAO {
    
    public List<KhachHangDTO> getAll() {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe FROM KHACHHANG ORDER BY MaKhachHang DESC";
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

    public boolean insert(KhachHangDTO kh) {
        // Dùng Sequence sinh mã tự tăng cho Oracle DB
        String sql = "INSERT INTO KHACHHANG (MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe) "
                   + "VALUES (SEQ_KHACHHANG.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, NULL)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getCccd());
            ps.setString(3, kh.getSoBangLai());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getGioiTinh());
            ps.setDate(6, kh.getNgaySinh() != null ? new java.sql.Date(kh.getNgaySinh().getTime()) : null);
            ps.setString(7, kh.getDiaChi());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean update(KhachHangDTO kh) {
        String sql = "UPDATE KHACHHANG SET HoTen=?, CCCD=?, SoBangLai=?, SDT=?, GioiTinh=?, NgaySinh=?, DiaChi=? WHERE MaKhachHang=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getCccd());
            ps.setString(3, kh.getSoBangLai());
            ps.setString(4, kh.getSdt());
            ps.setString(5, kh.getGioiTinh());
            ps.setDate(6, kh.getNgaySinh() != null ? new java.sql.Date(kh.getNgaySinh().getTime()) : null);
            ps.setString(7, kh.getDiaChi());
            ps.setInt(8, kh.getMaKhachHang());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }

        
    }


    // Tìm khách hàng theo mã chủ thể tài khoản
public KhachHangDTO getByMaChuThe(int maChuThe) {
    String sql = "SELECT * FROM KHACHHANG WHERE MaChuThe = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, maChuThe);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new KhachHangDTO(
                    rs.getInt("MaKhachHang"), rs.getString("HoTen"),
                    rs.getString("CCCD"), rs.getString("SoBangLai"),
                    rs.getString("SDT"), rs.getString("GioiTinh"),
                    rs.getDate("NgaySinh"), rs.getString("DiaChi"),
                    rs.getInt("MaChuThe")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}