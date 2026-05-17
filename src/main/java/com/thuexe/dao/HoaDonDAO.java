package com.thuexe.dao;

import com.thuexe.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Date;
import java.util.Vector;

public class HoaDonDAO {

    public Vector<Vector<Object>> getThongKe(
            Date tuNgay,
            Date denNgay
    ) {

        Vector<Vector<Object>> data =
                new Vector<>();

        String sql = """
            SELECT MaHD, NgHD, MaBB, TongTien
            FROM HOADON
            WHERE NgHD >= ? AND NgHD <= ?
        """;

        try (
                Connection conn =
                        DBConnection.getConnection();

                PreparedStatement ps =
                        conn.prepareStatement(sql)
        ) {

            ps.setDate(
                    1,
                    new java.sql.Date(
                            tuNgay.getTime()
                    )
            );

            ps.setDate(
                    2,
                    new java.sql.Date(
                            denNgay.getTime()
                    )
            );

            try (
                    ResultSet rs =
                            ps.executeQuery()
            ) {

                while (rs.next()) {

                    Vector<Object> row =
                            new Vector<>();

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
