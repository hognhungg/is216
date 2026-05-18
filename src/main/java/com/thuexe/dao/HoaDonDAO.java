package com.thuexe.dao;

import com.thuexe.dto.HoaDonDTO;
import com.thuexe.util.DBConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class HoaDonDAO {

    public boolean thanhToan(HoaDonDTO hd) {
        if (hd == null || hd.getMaPhieuThue() == null || hd.getNgayThanhToan() == null) {
            return false;
        }

        String sql = "{call PROC_TRA_XE(?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, hd.getMaPhieuThue());
            stmt.setDate(2, new Date(hd.getNgayThanhToan().getTime()));
            stmt.setDouble(3, hd.getPhiPhatSinh());

            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
