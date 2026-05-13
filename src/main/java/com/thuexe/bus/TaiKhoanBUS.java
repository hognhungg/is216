package com.thuexe.bus;

import com.thuexe.dao.TaiKhoanDAO;
import com.thuexe.dto.TaiKhoanDTO;
import java.util.ArrayList;
import java.util.Base64;

public class TaiKhoanBUS {
    // SỬA: Khớp chính xác với giá trị trong Database script [4]
    public static final String TRANG_THAI_HOAT_DONG = "Hoat dong";

    private String simpleEncrypt(String password) {
        if (password == null) return "";
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    public String login(String user, String pass, ArrayList<TaiKhoanDTO> dsTK) {
        // 1. Kiểm tra null/rỗng và TRIM TRƯỚC khi vào vòng lặp để tối ưu
        if (user == null || pass == null) return "Dữ liệu không được để trống!";
        
        String trimmedUser = user.trim();
        String trimmedPass = pass.trim();
        
        if (trimmedUser.isEmpty() || trimmedPass.isEmpty()) {
            return "Tên đăng nhập và mật khẩu không được để trống!";
        }
        
        if (dsTK == null) return "Lỗi hệ thống: Dữ liệu không khả dụng!";

        String encryptedPass = simpleEncrypt(trimmedPass);

        for (TaiKhoanDTO tk : dsTK) {
            if (tk == null) continue;

            String dbUser = tk.getTenDangNhap();
            // 2. So sánh Username an toàn
            if (dbUser != null && dbUser.trim().equalsIgnoreCase(trimmedUser)) {
                
                String dbPass = tk.getMatKhau();
                // 3. SỬA LỖI: So sánh mật khẩu an toàn tuyệt đối (Null-safe)
                if (encryptedPass.equals(dbPass)) { 
                    
                    // 4. SỬA LỖI: So sánh trạng thái đúng với giá trị "Hoat dong" trong DB
                    if (TRANG_THAI_HOAT_DONG.equalsIgnoreCase(tk.getTrangThai())) {
                        return "SUCCESS";
                    } else {
                        return "Tài khoản hiện đang bị khóa!";
                    }
                } else {
                    return "Mật khẩu không chính xác!";
                }
            }
        }
        return "Tài khoản không tồn tại!";
    }
    
    // Bổ sung vào class TaiKhoanBUS hiện tại của ông
public String changePassword(String user, String currentPass, String newPass, String confirmPass, String dbPassHash) {
    if (newPass.isEmpty()) return "Mật khẩu mới không được để trống!";
    if (!newPass.equals(confirmPass)) return "Xác nhận mật khẩu không khớp!";
    
    // Kiểm tra mật khẩu cũ (phải mã hóa rồi mới so sánh với pass trong DB)
    String encryptedOld = simpleEncrypt(currentPass.trim());
    if (!encryptedOld.equals(dbPassHash)) return "Mật khẩu cũ không chính xác!";
    
    // Thực hiện đổi
    String encryptedNew = simpleEncrypt(newPass.trim());
    TaiKhoanDAO dao = new TaiKhoanDAO();
    return dao.updatePassword(user, encryptedNew) ? "SUCCESS" : "Lỗi hệ thống!";
}
    
}