package com.thuexe.dao;

import com.thuexe.dto.XeDTO;
import com.thuexe.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhieuThueDAO_New {

    public List<XeDTO> layXeLoc(String loai, double giaMax) {
        List<XeDTO> list = new ArrayList<>();
        String sql = "SELECT x.*, l.TenLoai " +
                     "FROM XE x " +
                     "JOIN LOAIXE l ON x.MaLoaiXe = l.MaLoaiXe " +
                     "WHERE LOWER(l.TenLoai) LIKE ? " +
                     "AND NVL(l.GiaThueNgay, 0) <= ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String searchLoai = loai == null ? "" : loai.trim().toLowerCase();
            ps.setString(1, "%" + searchLoai + "%");
            ps.setDouble(2, giaMax);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToDTO(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private XeDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        XeDTO x = new XeDTO();
        x.setBienSoXe(rs.getString("BienSoXe"));
        x.setThuongHieu(rs.getString("ThuongHieu"));
        x.setTenXe(rs.getString("TenXe"));
        x.setSoCho(rs.getInt("SoCho"));
        x.setHinhAnh(rs.getString("HinhAnh"));
        x.setTrangThai(rs.getString("TrangThai"));
        x.setMaChuXe(rs.getInt("MaChuXe"));
        x.setMaLoaiXe(rs.getInt("MaLoaiXe"));
        x.setMaDoanhNghiep(rs.getInt("MaDoanhNghiep"));
        x.setTenLoai(rs.getString("TenLoai"));
        return x;
    }
}
