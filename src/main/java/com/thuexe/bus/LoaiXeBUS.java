package com.thuexe.bus;

import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.dao.LoaiXeDAO;

public class LoaiXeBUS {
    private LoaiXeDAO dao = new LoaiXeDAO();

    public String updateGiaThue(LoaiXeDTO loaiXe) {
        if (loaiXe == null) return "Dữ liệu trống!";
        
        // Ràng buộc nghiệp vụ quan trọng
        if (loaiXe.getGiaThueNgay() <= 0 || loaiXe.getGiaThueGio() <= 0) {
            return "Giá thuê ngày và thuê giờ phải lớn hơn 0!";
        }
        
        if (loaiXe.getTenLoai() == null || loaiXe.getTenLoai().trim().isEmpty()) {
            return "Tên loại xe không được để trống!";
        }

        return dao.updateLoaiXe(loaiXe) ? "SUCCESS" : "Cập nhật thất bại!";
    }
    
    // Tương tự, bạn có thể gọi hàm insert và delete từ DAO ở đây
}