package com.thuexe.dao;

import com.thuexe.dto.TaiKhoanDTO; 
import com.thuexe.util.DBConnection;
import java.sql.*;

public class TaiKhoanDAO {

    /**
     * Sử dụng Stored Procedure để kiểm tra đăng nhập
     * Trả về đối tượng TaiKhoanDTO nếu thành công, null nếu thất bại
     */
    public TaiKhoanDTO login(String tenDangNhap, String matKhau) {
        // Gọi Procedure trong Oracle: tham số 1 & 2 là IN, tham số 3 là OUT (Cursor)
        String sql = "{call SP_DANG_NHAP(?, ?, ?)}";
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {
            
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            
            // Đăng ký tham số đầu ra là Cursor (mã -10 đại diện cho Oracle Cursor)
            stmt.registerOutParameter(3, -10);
            
            stmt.execute();
            
            // Lấy ResultSet từ tham số OUT thứ 3
            try (ResultSet rs = (ResultSet) stmt.getObject(3)) {
                if (rs.next()) {
                    // Tạo đối tượng DTO và đổ dữ liệu vào
                    TaiKhoanDTO tk = new TaiKhoanDTO();
                    tk.setMaChuThe(rs.getInt("MaChuThe"));
                    tk.setTenDangNhap(rs.getString("TenDangNhap"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setNgayTao(rs.getDate("NgayTao"));
                    tk.setVaiTro(rs.getString("VaiTro"));
                    tk.setTrangThai(rs.getString("TrangThai"));
                    return tk;
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi đăng nhập: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    
public boolean updatePassword(String username, String encryptedNewPass) {
    String sql = "UPDATE TAIKHOAN SET MatKhau = ? WHERE TenDangNhap = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, encryptedNewPass);
        ps.setString(2, username);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) { return false; }
}
}
