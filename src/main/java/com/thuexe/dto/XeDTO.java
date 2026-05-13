package com.thuexe.dto;

public class XeDTO {
    // 1. Các thuộc tính ánh xạ từ bảng XE và bổ sung TenLoai từ bảng LOAIXE
    private String bienSoXe;
    private String thuongHieu;
    private String tenXe;
    private int soCho;
    private String hinhAnh;
    private String trangThai;
    private int maChuXe;
    private int maLoaiXe;
    private int maDoanhNghiep;
    private String tenLoai; // Dùng để hiển thị tên loại xe (xe du lịch, xe tải...)

    // 2. Constructor không đối số (Mặc định)
    public XeDTO() {}

    // 3. Constructor đầy đủ đối số (Đã cập nhật thêm tenLoai)
    public XeDTO(String bienSoXe, String thuongHieu, String tenXe, int soCho, 
                 String hinhAnh, String trangThai, int maChuXe, int maLoaiXe, 
                 int maDoanhNghiep, String tenLoai) {
        this.bienSoXe = bienSoXe;
        this.thuongHieu = thuongHieu;
        this.tenXe = tenXe;
        this.soCho = soCho;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
        this.maChuXe = maChuXe;
        this.maLoaiXe = maLoaiXe;
        this.maDoanhNghiep = maDoanhNghiep;
        this.tenLoai = tenLoai;
    }

    // 4. Các phương thức Getter và Setter
    public String getBienSoXe() { return bienSoXe; }
    public void setBienSoXe(String bienSoXe) { this.bienSoXe = bienSoXe; }

    public String getThuongHieu() { return thuongHieu; }
    public void setThuongHieu(String thuongHieu) { this.thuongHieu = thuongHieu; }

    public String getTenXe() { return tenXe; }
    public void setTenXe(String tenXe) { this.tenXe = tenXe; }

    public int getSoCho() { return soCho; }
    public void setSoCho(int soCho) { this.soCho = soCho; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public int getMaChuXe() { return maChuXe; }
    public void setMaChuXe(int maChuXe) { this.maChuXe = maChuXe; }

    public int getMaLoaiXe() { return maLoaiXe; }
    public void setMaLoaiXe(int maLoaiXe) { this.maLoaiXe = maLoaiXe; }

    public int getMaDoanhNghiep() { return maDoanhNghiep; }
    public void setMaDoanhNghiep(int maDoanhNghiep) { this.maDoanhNghiep = maDoanhNghiep; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
}