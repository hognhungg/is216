package com.thuexe.bus;

import com.thuexe.dao.KhachHangDAO;
import com.thuexe.dto.KhachHangDTO;
import com.thuexe.util.ValidationUtil;
import java.util.List;

public class KhachHangBUS {
    private KhachHangDAO khDAO = new KhachHangDAO();

    public List<KhachHangDTO> getAllKhachHang() { return khDAO.getAll(); }

    public String updateKhachHang(KhachHangDTO kh) {
        if (!ValidationUtil.isCCCD(kh.getCccd())) return "CCCD phải đúng 12 chữ số!";
        if (!ValidationUtil.isPhoneNumber(kh.getSdt())) return "SĐT không hợp lệ (10 số, bắt đầu bằng 0)!";
        if (kh.getHoTen().trim().isEmpty()) return "Họ tên không được để trống!";
        
        return khDAO.update(kh) ? "SUCCESS" : "Cập nhật thất bại!";
    }
}