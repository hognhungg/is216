/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.thuexe.bus;

import com.thuexe.dao.PhieuThueDAO;
import com.thuexe.dto.PhieuThueDTO;
import java.util.Date;
import java.util.List;

public class PhieuThueBUS {
    
    private PhieuThueDAO dao = new PhieuThueDAO();

    public List<Object[]> layKhachHang() {
        return dao.layDanhSachKhachHang();
    }

    // ===== LẤY XE TRỐNG THEO NGÀY =====
    public List<Object[]> layXeTrong(Date ngayNhan, Date ngayTra) {
        return dao.layDanhSachXeTrong(ngayNhan, ngayTra);
    }

    public double layDonGia(String bienSo) {
        return dao.layDonGiaNgay(bienSo);
    }

    // ===== LẬP PHIẾU THUÊ MỚI =====
    public String lapPhieu(PhieuThueDTO p) throws Exception {
        // Ràng buộc dữ liệu đầu vào (Validation)
        if (p.getMaKhachHang() == null || p.getMaKhachHang().isEmpty())
            throw new Exception("Vui lòng chọn khách hàng!");
        if (p.getBienSoXe() == null || p.getBienSoXe().isEmpty())
            throw new Exception("Vui lòng chọn xe!");
        if (p.getThoiGianTraXe().before(p.getThoiGianNhanXe()))
            throw new Exception("Ngày trả phải sau ngày nhận!");
        if (p.getDiaDiemNhanXe() == null || p.getDiaDiemNhanXe().trim().isEmpty())
            throw new Exception("Vui lòng nhập địa điểm nhận!");

        return dao.themPhieuThue(p);
    }

    // ===== CẬP NHẬT/SỬA PHIẾU THUÊ =====
    public boolean suaPhieu(PhieuThueDTO p, String maHopDong) throws Exception {
        // Kiểm tra ràng buộc dữ liệu tương tự khi lập phiếu
        if (maHopDong == null || maHopDong.trim().isEmpty())
            throw new Exception("Không tìm thấy mã hợp đồng cần sửa!");
        if (p.getMaKhachHang() == null || p.getMaKhachHang().isEmpty())
            throw new Exception("Vui lòng chọn khách hàng!");
        if (p.getBienSoXe() == null || p.getBienSoXe().isEmpty())
            throw new Exception("Vui lòng chọn xe!");
        if (p.getThoiGianTraXe().before(p.getThoiGianNhanXe()))
            throw new Exception("Ngày trả phải sau ngày nhận!");
            
        // Gọi xuống tầng DAO để thực thi câu lệnh UPDATE SQL
        return dao.capNhatPhieuThue(p, maHopDong);
    }

    // ===== HỦY/XÓA PHIẾU THUÊ =====
    public boolean xoaPhieu(String maHopDong) throws Exception {
        if (maHopDong == null || maHopDong.trim().isEmpty())
            throw new Exception("Mã hợp đồng không hợp lệ để thực hiện xóa!");
            
        // Gọi xuống tầng DAO để thực thi câu lệnh DELETE SQL
        return dao.xoaPhieuThue(maHopDong);
    }
}