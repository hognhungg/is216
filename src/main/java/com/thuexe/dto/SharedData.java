package com.thuexe.dto;

/**
 * Class trung gian lưu trữ phiên làm việc (Session) của người dùng hiện tại.
 * Dữ liệu ở đây là static để mọi Form trong hệ thống (FrmLoaiXe, FrmKhachHang, FrmMain) 
 * đều có thể bốc ra kiểm tra quyền bất cứ lúc nào mà không cần truy vấn lại Database.
 */
public class SharedData {
    
    // Quy ước 3 chuỗi ký tự quyền hạn sẽ khớp với cột VaiTro dưới Database Oracle:
    // 1. "ADMIN"    -> Chủ cửa hàng (Toàn quyền)
    // 2. "STAFF"    -> Nhân viên (Xử lý nghiệp vụ, không được sửa cấu hình giá xe)
    // 3. "CUSTOMER" -> Khách hàng đặt xe (Chỉ được xem lịch sử, tự đặt xe của mình)
    public static String currentRole = "";      
    
    // Lưu tên tài khoản đang đăng nhập để hiển thị lên màn hình chào hoặc ghi nhận người lập hợp đồng
    public static String currentUsername = "";  
    
    // Lưu mã chủ thể (MaChuThe) để biết chính xác Khách hàng nào đang đăng nhập để lọc đúng xe họ đặt
    public static int maChuThe = 0;             
}