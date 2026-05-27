package com.thuexe.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thuexe.dto.PhieuThueDTO;
import com.thuexe.util.DBConnection;

public class PhieuThueDAO {

    // ===== 1. LẤY DANH SÁCH KHÁCH HÀNG =====
    public List<Object[]> layDanhSachKhachHang() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT MaKhachHang, HoTen FROM KHACHHANG";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    String.valueOf(rs.getInt("MaKhachHang")), 
                    rs.getString("HoTen")
                });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ===== 2. TÌM XE TRỐNG THEO NGÀY  =====
    public List<Object[]> layDanhSachXeTrong(Date ngayNhan, Date ngayTra) {
        List<Object[]> list = new ArrayList<>();
        String sql = "{call SP_TIM_XE_TRONG(?, ?, ?)}";  // Oracle: 3 tham số
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setDate(1, new java.sql.Date(ngayNhan.getTime()));
            stmt.setDate(2, new java.sql.Date(ngayTra.getTime()));
            stmt.registerOutParameter(3, -10);  // Oracle CURSOR = -10
            
            stmt.execute();
            
            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("BienSoXe"),
                        rs.getString("BienSoXe") + " - " + 
                        rs.getString("TenXe") + " (" + 
                        rs.getString("TenLoai") + ")"
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ===== 3. LẤY ĐƠN GIÁ =====
    public double layDonGiaNgay(String bienSoXe) {
        String sql = "SELECT l.GiaThueNgay FROM XE x JOIN LOAIXE l ON x.MaLoaiXe = l.MaLoaiXe WHERE x.BienSoXe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bienSoXe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("GiaThueNgay");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // ===== 4. THÊM PHIẾU THUÊ =====
    public String themPhieuThue(PhieuThueDTO phieu) {
        String sql = "{call Create_Rental_Contract(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, phieu.getBienSoXe());
            stmt.setInt(2, Integer.parseInt(phieu.getMaKhachHang()));
            stmt.setDate(3, new java.sql.Date(phieu.getThoiGianNhanXe().getTime()));
            stmt.setString(4, phieu.getDiaDiemNhanXe());
            stmt.setString(5, phieu.getDiaDiemTraXe());
            stmt.registerOutParameter(6, Types.INTEGER);
            stmt.execute();
            
            return String.valueOf(stmt.getInt(6));
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return null;
        }
    }

    // ===== 5. CẬP NHẬT/SỬA PHIẾU THUÊ (HÀM BỔ SUNG MỚI) =====
    public boolean capNhatPhieuThue(PhieuThueDTO p, String maHopDong) throws SQLException {
        String sql = "UPDATE HOPDONGTHUEXE SET MaKhachHang = ?, BienSoXe = ?, NgayNhan = ?, NgayTra = ?, " +
                     "DiaDiemNhan = ?, DiaDiemTra = ?, TongTienTamTinh = ? WHERE MaHopDong = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(p.getMaKhachHang()));
            ps.setString(2, p.getBienSoXe());
            ps.setDate(3, new java.sql.Date(p.getThoiGianNhanXe().getTime()));
            ps.setDate(4, new java.sql.Date(p.getThoiGianTraXe().getTime()));
            ps.setString(5, p.getDiaDiemNhanXe());
            ps.setString(6, p.getDiaDiemTraXe());
            ps.setDouble(7, p.getTienTamTinh());
            ps.setInt(8, Integer.parseInt(maHopDong)); // Ép kiểu về số nguyên để khớp với DB
            
            return ps.executeUpdate() > 0;
        }
    }

    // ===== 6. HỦY/XÓA PHIẾU THUÊ (HÀM BỔ SUNG MỚI) =====
    public boolean xoaPhieuThue(String maHopDong) throws SQLException {
        String sql = "DELETE FROM HOPDONGTHUEXE WHERE MaHopDong = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, Integer.parseInt(maHopDong));
            return ps.executeUpdate() > 0;
        }
    }
}