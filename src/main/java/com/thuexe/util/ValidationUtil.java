package com.thuexe.util; // Khai báo gói tiện ích 
public class ValidationUtil {
    // Kiểm tra Số điện thoại: 10 chữ số, bắt đầu bằng 0
    public static boolean isPhoneNumber(String sdt) {
        return sdt != null && sdt.trim().matches("^0\\d{9}$");
    }

    // Kiểm tra CCCD: Đúng 12 chữ số theo quy định 
    public static boolean isCCCD(String cccd) {
        return cccd != null && cccd.trim().matches("^\\d{12}$");
    }

    // Kiểm tra Email: Định dạng chuẩn tên@domain.com
    public static boolean isEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.trim().matches(emailRegex);
    }

    // Kiểm tra Biển số xe: Chuẩn Việt Nam (VD: 59A1-12345 hoặc 51B-6789) 
    public static boolean isBienSoXe(String bienSo) {
        return bienSo != null && bienSo.trim().matches("^\\d{2}[A-Z]{1}[0-9A-Z]{1}-?\\d{4,5}$");
    }
}