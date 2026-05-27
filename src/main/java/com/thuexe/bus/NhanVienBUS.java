package com.thuexe.bus;

import com.thuexe.dao.NhanVienDAO;
import com.thuexe.util.ValidationUtil;

public class NhanVienBUS {
    private NhanVienDAO nvDAO = new NhanVienDAO();

    public Object[] getProfile(int maChuThe) {
        return nvDAO.getProfileByMaChuThe(maChuThe);
    }

    public String updateProfile(int maNhanVien, String sdt, String email) {
        if (!ValidationUtil.isPhoneNumber(sdt)) {
            return "Số điện thoại không hợp lệ (Phải gồm 10 số và bắt đầu bằng 0)!";
        }
        if (!ValidationUtil.isEmail(email)) {
            return "Định dạng Email không hợp lệ!";
        }
        
        return nvDAO.updateContact(maNhanVien, sdt, email) ? "SUCCESS" : "Cập nhật thông tin thất bại!";
    }
}