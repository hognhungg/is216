package com.thuexe.dto;

public class NhanVienDTO {
    private int maNhanVien;
    private String hoTenNV;
    private String cccd;
    private String sdt;
    private String email;
    private int maChuThe;
    private int maDoanhNghiep;

    // Constructor mặc định
    public NhanVienDTO() {}

    // Constructor đầy đủ tham số
    public NhanVienDTO(int maNhanVien, String hoTenNV, String cccd, String sdt, String email, int maChuThe, int maDoanhNghiep) {
        this.maNhanVien = maNhanVien;
        this.hoTenNV = hoTenNV;
        this.cccd = cccd;
        this.sdt = sdt;
        this.email = email;
        this.maChuThe = maChuThe;
        this.maDoanhNghiep = maDoanhNghiep;
    }

    // Getter và Setter
    public int getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(int maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTenNV() { return hoTenNV; }
    public void setHoTenNV(String hoTenNV) { this.hoTenNV = hoTenNV; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getMaChuThe() { return maChuThe; }
    public void setMaChuThe(int maChuThe) { this.maChuThe = maChuThe; }

    public int getMaDoanhNghiep() { return maDoanhNghiep; }
    public void setMaDoanhNghiep(int maDoanhNghiep) { this.maDoanhNghiep = maDoanhNghiep; }
}