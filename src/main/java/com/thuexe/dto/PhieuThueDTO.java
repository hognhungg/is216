package com.thuexe.dto;

import java.util.Date;

public class PhieuThueDTO {
    // 1. Thêm thuộc tính mã hợp đồng vào đây
    private String maHopDong; 
    
    private String maKhachHang;
    private String hoTenKH;
    private String bienSoXe;
    private String thongTinXe;
    private Date thoiGianNhanXe;
    private Date thoiGianTraXe;
    private String diaDiemNhanXe;
    private String diaDiemTraXe;
    private double donGiaNgay;
    private int soNgayThue;
    private double tienTamTinh;

    public PhieuThueDTO() {}

    // 2. Thêm Getter và Setter cho mã hợp đồng
    public String getMaHopDong() { return maHopDong; }
    public void setMaHopDong(String maHopDong) { this.maHopDong = maHopDong; }

    // --- Các hàm Getter/Setter bên dưới giữ nguyên vẹn của bạn ---
    public String getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }
    public String getHoTenKH() { return hoTenKH; }
    public void setHoTenKH(String hoTenKH) { this.hoTenKH = hoTenKH; }
    public String getBienSoXe() { return bienSoXe; }
    public void setBienSoXe(String bienSoXe) { this.bienSoXe = bienSoXe; }
    public String getThongTinXe() { return thongTinXe; }
    public void setThongTinXe(String thongTinXe) { this.thongTinXe = thongTinXe; }
    public Date getThoiGianNhanXe() { return thoiGianNhanXe; }
    public void setThoiGianNhanXe(Date thoiGianNhanXe) { this.thoiGianNhanXe = thoiGianNhanXe; }
    public Date getThoiGianTraXe() { return thoiGianTraXe; }
    public void setThoiGianTraXe(Date thoiGianTraXe) { this.thoiGianTraXe = thoiGianTraXe; }
    public String getDiaDiemNhanXe() { return diaDiemNhanXe; }
    public void setDiaDiemNhanXe(String diaDiemNhanXe) { this.diaDiemNhanXe = diaDiemNhanXe; }
    public String getDiaDiemTraXe() { return diaDiemTraXe; }
    public void setDiaDiemTraXe(String diaDiemTraXe) { this.diaDiemTraXe = diaDiemTraXe; }
    public double getDonGiaNgay() { return donGiaNgay; }
    public void setDonGiaNgay(double donGiaNgay) { this.donGiaNgay = donGiaNgay; }
    public int getSoNgayThue() { return soNgayThue; }
    public void setSoNgayThue(int soNgayThue) { this.soNgayThue = soNgayThue; }
    public double getTienTamTinh() { return tienTamTinh; }
public void setTienTamTinh(double tienTamTinh) { this.tienTamTinh = tienTamTinh; }
    public void tinhTienTamTinh() {
        if (thoiGianNhanXe != null && thoiGianTraXe != null) {
            long diff = thoiGianTraXe.getTime() - thoiGianNhanXe.getTime();
            soNgayThue = (int) (diff / (1000 * 60 * 60 * 24));
            if (soNgayThue <= 0) soNgayThue = 1; 
            tienTamTinh = soNgayThue * donGiaNgay;
        } else {
            soNgayThue = 0;
            tienTamTinh = 0;
        }
    }
}