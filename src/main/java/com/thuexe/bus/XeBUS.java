package com.thuexe.bus;

import com.thuexe.dao.XeDAO;
import com.thuexe.dto.XeDTO;
import java.sql.Timestamp;
import java.util.List;

public class XeBUS {
    private XeDAO xeDAO;

    public XeBUS() {
        xeDAO = new XeDAO();
    }

    // 1. Lấy toàn bộ danh sách xe phục vụ nạp vào JTable
    public List<XeDTO> getAllXe() {
        return xeDAO.getAllXe();
    }

    // 2. Tìm kiếm xe nâng cao phối hợp nhiều bộ lọc động (Mã loại, từ khóa, trạng thái)
    public List<XeDTO> timKiemXeNangCao(String keyword, int maLoaiXe, String trangThai) {
        // Chuẩn hóa từ khóa tìm kiếm (bỏ khoảng trắng thừa)
        if (keyword != null) {
            keyword = keyword.trim();
        }
        
        // Nếu chọn "-- Tất cả trạng thái --" thì chuyển về null để tầng DAO không kích hoạt điều kiện lọc WHERE x.TrangThai
        if ("-- Tất cả trạng thái --".equalsIgnoreCase(trangThai) || "".equals(trangThai)) {
            trangThai = null;
        }
        
        return xeDAO.timKiemXeNangCao(keyword, maLoaiXe, trangThai);
    }

    // 3. Gọi Procedure tìm xe trống theo lịch trình khách đặt
    public List<XeDTO> timXeTrong(Timestamp ngayNhan, Timestamp ngayTra) {
        if (ngayNhan == null || ngayTra == null) {
            return null;
        }
        // Logic kiểm tra: Ngày trả không thể trước ngày nhận xe
        if (ngayTra.before(ngayNhan)) {
            return null; 
        }
        return xeDAO.timXeTrong(ngayNhan, ngayTra);
    }

    // 4. Thêm xe mới (Kiểm tra nghiệp vụ cơ bản trước khi thêm)
    public String themXe(XeDTO xe) {
        // Kiểm tra validation dữ liệu bỏ trống
        if (xe.getBienSoXe() == null || xe.getBienSoXe().trim().isEmpty()) {
            return "Biển số xe không được để trống!";
        }
        if (xe.getTenXe() == null || xe.getTenXe().trim().isEmpty()) {
            return "Tên xe không được để trống!";
        }
        if (xe.getSoCho() <= 0) {
            return "Số chỗ ngồi phải lớn hơn 0!";
        }
        if (xe.getMaLoaiXe() <= 0) {
            return "Vui lòng chọn phân loại xe hợp lệ!";
        }

        // Chuẩn hóa chữ in hoa cho biển số xe trước khi lưu xuống DB
        xe.setBienSoXe(xe.getBienSoXe().trim().toUpperCase());

        boolean isSuccess = xeDAO.themXe(xe);
        return isSuccess ? "SUCCESS" : "Thêm xe thất bại! Biển số xe có thể đã tồn tại hoặc lỗi ràng buộc dữ liệu.";
    }

    // 5. Xóa xe dựa theo biển số
    public String xoaXe(String bienSoXe) {
        if (bienSoXe == null || bienSoXe.trim().isEmpty()) {
            return "Biển số xe không hợp lệ để thực hiện xóa!";
        }
        
        boolean isSuccess = xeDAO.xoaXe(bienSoXe.trim());
        // Lỗi thường gặp: Xe đang nằm trong một hợp đồng thuê chưa thanh lý (Dính khóa ngoại bảng ChiTietPhieuThue)
        return isSuccess ? "SUCCESS" : "Xóa thất bại! Xe này đang có lịch trình thuê hoặc liên quan đến dữ liệu hóa đơn.";
    }
}