package com.thuexe.dao;

import com.thuexe.dto.HoaDonDTO;
import com.thuexe.util.DBConnection;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class HoaDonDAO {

    // 1. HÀM THANH TOÁN (Giữ lại từ nhánh HEAD)
    public boolean thanhToan(HoaDonDTO hd) {
        if (hd == null || hd.getMaPhieuThue() == null || hd.getNgayThanhToan() == null) {
            return false;
        }

        String sql = "{call PROC_TRA_XE(?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, hd.getMaPhieuThue());
            stmt.setDate(2, new java.sql.Date(hd.getNgayThanhToan().getTime()));
            stmt.setDouble(3, hd.getPhiPhatSinh());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. HÀM THỐNG KÊ (Giữ lại từ nhánh gộp để vẽ biểu đồ)
    public Vector<Vector<Object>> getThongKe(Date tuNgay, Date denNgay) {
        Vector<Vector<Object>> data = new Vector<>();

        String sql = """
            SELECT MaHD, NgHD, MaBB, TongTien
            FROM HOADON
            WHERE NgHD >= ? AND NgHD <= ?
        """;

        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setDate(1, new java.sql.Date(tuNgay.getTime()));
            ps.setDate(2, new java.sql.Date(denNgay.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("MaHD"));
                    row.add(rs.getDate("NgHD"));
                    row.add(rs.getInt("MaBB"));
                    row.add(rs.getDouble("TongTien"));
                    data.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }
}