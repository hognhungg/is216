package com.thuexe.bus;

import com.thuexe.dao.NhanVienDAO;
import com.thuexe.dto.NhanVienDTO;
import com.thuexe.util.ValidationUtil;
import java.util.List;

public class NhanVienBUS {
    private NhanVienDAO nvDAO = new NhanVienDAO();

    // Lấy toàn bộ danh sách nhân viên
    public List<NhanVienDTO> getAllNhanVien() {
        return nvDAO.getAll();
    }

    // Thêm nhân viên mới
    public String insertNhanVien(NhanVienDTO nv) {
        if (nv.getHoTenNV().trim().isEmpty()) return "Họ tên nhân viên không được để trống!";
        if (!ValidationUtil.isCCCD(nv.getCccd())) return "Số CCCD phải đúng 12 chữ số!";
        if (!ValidationUtil.isPhoneNumber(nv.getSdt())) return "Số điện thoại không hợp lệ!";
        if (nv.getEmail().trim().isEmpty() || !nv.getEmail().contains("@")) return "Email không hợp lệ!";
        
        return nvDAO.insert(nv) ? "SUCCESS" : "Thêm nhân viên thất bại (Có thể bị trùng CCCD)!";
    }

    // Sửa thông tin nhân viên
    public String updateNhanVien(NhanVienDTO nv) {
        if (nv.getHoTenNV().trim().isEmpty()) return "Họ tên không được để trống!";
        if (!ValidationUtil.isCCCD(nv.getCccd())) return "Số CCCD phải đúng 12 chữ số!";
        if (!ValidationUtil.isPhoneNumber(nv.getSdt())) return "Số điện thoại không hợp lệ!";
        
        return nvDAO.update(nv) ? "SUCCESS" : "Cập nhật thông tin nhân viên thất bại!";
    }

    // Xóa nhân viên
    public String deleteNhanVien(int maNhanVien) {
        if (maNhanVien <= 0) return "Mã nhân viên không hợp lệ!";
        return nvDAO.delete(maNhanVien) ? "SUCCESS" : "Xóa nhân viên thất bại!";
    }
}