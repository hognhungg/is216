package com.thuexe.dto;

import java.util.Date;

public class KhachHangDTO {
    private int maKhachHang;
    private String hoTen;
    private String cccd;
    private String soBangLai;
    private String sdt;
    private String gioiTinh;
    private Date ngaySinh;
    private String diaChi;
    private int maChuThe;

    public KhachHangDTO() {}

    public KhachHangDTO(int maKhachHang, String hoTen, String cccd, String soBangLai, 
                        String sdt, String gioiTinh, Date ngaySinh, String diaChi, int maChuThe) {
        this.maKhachHang = maKhachHang;
        this.hoTen = hoTen;
        this.cccd = cccd;
        this.soBangLai = soBangLai;
        this.sdt = sdt;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.maChuThe = maChuThe;
    }

    // Getter và Setter
    public int getMaKhachHang() { return maKhachHang; }
    public void setMaKhachHang(int maKhachHang) { this.maKhachHang = maKhachHang; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public String getSoBangLai() { return soBangLai; }
    public void setSoBangLai(String soBangLai) { this.soBangLai = soBangLai; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public int getMaChuThe() { return maChuThe; }
    public void setMaChuThe(int maChuThe) { this.maChuThe = maChuThe; }
}