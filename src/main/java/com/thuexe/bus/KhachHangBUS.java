package com.thuexe.bus;

import com.thuexe.dao.KhachHangDAO;
import com.thuexe.dto.KhachHangDTO;
import java.util.List;

public class KhachHangBUS {
    private KhachHangDAO khDAO = new KhachHangDAO();

    public List<KhachHangDTO> getAllKhachHang() { 
        return khDAO.getAll(); 
    }

    public String insertKhachHang(KhachHangDTO kh) {
        if (kh.getHoTen().trim().isEmpty()) return "Họ tên không được để trống!";
        if (kh.getCccd().trim().length() != 12) return "CCCD bắt buộc phải đủ 12 số!";
        return khDAO.insert(kh) ? "SUCCESS" : "Lỗi hệ thống không thể thêm mới!";
    }

    public String updateKhachHang(KhachHangDTO kh) {
        if (kh.getHoTen().trim().isEmpty()) return "Họ tên không được để trống!";
        return khDAO.update(kh) ? "SUCCESS" : "Lỗi hệ thống không thể cập nhật!";
    }

    public String deleteKhachHang(int maKhachHang) {
        if (maKhachHang <= 0) return "Mã khách hàng không hợp lệ!";
        return khDAO.delete(maKhachHang) ? "SUCCESS" : "Xóa thất bại (Dữ liệu khách hàng đang liên kết với hợp đồng thuê)!";
    }
}