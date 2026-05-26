package com.thuexe.dao;

import com.thuexe.dto.NhanVienDTO;
import com.thuexe.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types; // Thêm dòng này nếu chưa có

public class NhanVienDAO {

    // 1. Lấy toàn bộ danh sách nhân viên
    public List<NhanVienDTO> getAll() {
        List<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN ORDER BY MaNhanVien DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new NhanVienDTO(
                    rs.getInt("MaNhanVien"),
                    rs.getString("HoTenNV"),
                    rs.getString("CCCD"),
                    rs.getString("SDT"),
                    rs.getString("Email"),
                    rs.getInt("MaChuThe"),
                    rs.getInt("MaDoanhNghiep")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới nhân viên (Sử dụng SEQ_NHANVIEN như lệnh SQL lúc nãy)
    public boolean insert(NhanVienDTO nv) {
        String sql = "INSERT INTO NHANVIEN (MaNhanVien, HoTenNV, CCCD, SDT, Email, MaChuThe, MaDoanhNghiep) "
                   + "VALUES (SEQ_NHANVIEN.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTenNV());
            ps.setString(2, nv.getCccd());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getEmail());
            
            // Xử lý khóa ngoại nếu bằng 0 thì gán NULL trong database
            if (nv.getMaChuThe() > 0) ps.setInt(5, nv.getMaChuThe());
            else ps.setNull(5, Types.NUMERIC);
            
            if (nv.getMaDoanhNghiep() > 0) ps.setInt(6, nv.getMaDoanhNghiep());
            else ps.setNull(6, Types.NUMERIC);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cập nhật thông tin nhân viên
    public boolean update(NhanVienDTO nv) {
        String sql = "UPDATE NHANVIEN SET HoTenNV=?, CCCD=?, SDT=?, Email=?, MaChuThe=?, MaDoanhNghiep=? WHERE MaNhanVien=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTenNV());
            ps.setString(2, nv.getCccd());
            ps.setString(3, nv.getSdt());
            ps.setString(4, nv.getEmail());
            
            if (nv.getMaChuThe() > 0) ps.setInt(5, nv.getMaChuThe());
            else ps.setNull(5, Types.NUMERIC);
            
            if (nv.getMaDoanhNghiep() > 0) ps.setInt(6, nv.getMaDoanhNghiep());
            else ps.setNull(6, Types.NUMERIC);
            
            ps.setInt(7, nv.getMaNhanVien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Xóa nhân viên theo mã số
    public boolean delete(int maNhanVien) {
        String sql = "DELETE FROM NHANVIEN WHERE MaNhanVien = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
public NhanVienDTO getProfileByMaChuThe(int maChuThe) {

    String sql =
            "SELECT * FROM NHANVIEN WHERE MaChuThe = ?";

    try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
    ) {

        ps.setInt(1, maChuThe);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            return new NhanVienDTO(
                    rs.getInt("MaNhanVien"),
                    rs.getString("HoTenNV"),
                    rs.getString("CCCD"),
                    rs.getString("SDT"),
                    rs.getString("Email"),
                    rs.getInt("MaChuThe"),
                    rs.getInt("MaDoanhNghiep")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
