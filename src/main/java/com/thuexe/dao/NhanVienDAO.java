package com.thuexe.dao;

import com.thuexe.util.DBConnection;
import java.sql.*;

public class NhanVienDAO {
    
    // Lấy thông tin nhân viên bằng MaChuThe của tài khoản đang đăng nhập
    public Object[] getProfileByMaChuThe(int maChuThe) {
        String sql = "SELECT nv.MaNhanVien, nv.HoTenNV, nv.CCCD, nv.SDT, nv.Email, dn.TenDoanhNghiep " +
                     "FROM NHANVIEN nv " +
                     "LEFT JOIN DOANHNGHIEPQUANLY dn ON nv.MaDoanhNghiep = dn.MaDoanhNghiep " +
                     "WHERE nv.MaChuThe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maChuThe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[] {
                        rs.getInt("MaNhanVien"),
                        rs.getString("HoTenNV"),
                        rs.getString("CCCD"),
                        rs.getString("SDT"), 
                        rs.getString("Email"),
                        rs.getString("TenDoanhNghiep") // Đóng vai trò Chi nhánh làm việc
                    };
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Nhân viên chỉ được sửa Số điện thoại và Email công việc
    public boolean updateContact(int maNhanVien, String sdt, String email) {
        String sql = "UPDATE NHANVIEN SET SDT = ?, Email = ? WHERE MaNhanVien = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sdt);
            ps.setString(2, email);
            ps.setInt(3, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}