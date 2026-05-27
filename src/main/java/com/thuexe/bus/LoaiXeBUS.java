package com.thuexe.bus;

import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.dao.LoaiXeDAO;
import java.util.List;

public class LoaiXeBUS {
    private LoaiXeDAO dao = new LoaiXeDAO();

    // Lấy toàn bộ danh sách loại xe từ DAO
    public List<LoaiXeDTO> getAll() {
        return dao.getAllLoaiXe();
    }

    // Cập nhật thông tin / Giá thuê loại xe
    public String updateGiaThue(LoaiXeDTO loaiXe) {
        if (loaiXe == null) return "Dữ liệu trống!";
        
        if (loaiXe.getGiaThueNgay() <= 0 || loaiXe.getGiaThueGio() <= 0) {
            return "Giá thuê ngày và thuê giờ phải lớn hơn 0!";
        }
        
        if (loaiXe.getTenLoai() == null || loaiXe.getTenLoai().trim().isEmpty()) {
            return "Tên loại xe không được để trống!";
        }

        return dao.updateLoaiXe(loaiXe) ? "SUCCESS" : "Cập nhật thất bại!";
    }

    // 🛠️ BỔ SUNG HÀM THÊM MỚI LOẠI XE
    public String insert(LoaiXeDTO loaiXe) {
        if (loaiXe == null) return "Dữ liệu trống!";
        
        if (loaiXe.getTenLoai() == null || loaiXe.getTenLoai().trim().isEmpty()) {
            return "Tên loại xe không được để trống!";
        }
        if (loaiXe.getGiaThueNgay() <= 0 || loaiXe.getGiaThueGio() <= 0) {
            return "Giá thuê phải lớn hơn 0!";
        }

        // Gọi xuống hàm thêm của DAO (ông check xem bên LoaiXeDAO đặt tên hàm thêm là gì nhé, ví dụ: insertLoaiXe)
        return dao.insertLoaiXe(loaiXe) ? "SUCCESS" : "Thêm mới loại xe thất bại!";
    }

    // 🛠️ BỔ SUNG HÀM XÓA LOẠI XE
    public String delete(int maLoaiXe) {
        if (maLoaiXe <= 0) return "Mã loại xe không hợp lệ!";

        // Gọi xuống hàm xóa của DAO (ông check xem bên LoaiXeDAO đặt tên hàm xóa là gì nhé, ví dụ: deleteLoaiXe)
        return dao.deleteLoaiXe(maLoaiXe) ? "SUCCESS" : "Xóa loại xe thất bại (Có thể do ràng buộc dữ liệu)!";
    }
}