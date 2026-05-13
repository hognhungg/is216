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

    public String lapPhieu(PhieuThueDTO p) throws Exception {
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
}

    