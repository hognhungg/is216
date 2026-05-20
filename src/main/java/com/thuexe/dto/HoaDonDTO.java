package com.thuexe.dto;

import java.util.Date;

public class HoaDonDTO {
    private Integer maHD;
    private Integer maPhieuThue;
    private Date ngayThanhToan;
    private double phiPhatSinh;
    private double tongTien;

    public HoaDonDTO() {
    }

    public Integer getMaHD() {
        return maHD;
    }

    public void setMaHD(Integer maHD) {
        this.maHD = maHD;
    }

    public Integer getMaPhieuThue() {
        return maPhieuThue;
    }

    public void setMaPhieuThue(Integer maPhieuThue) {
        this.maPhieuThue = maPhieuThue;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public double getPhiPhatSinh() {
        return phiPhatSinh;
    }

    public void setPhiPhatSinh(double phiPhatSinh) {
        this.phiPhatSinh = phiPhatSinh;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
