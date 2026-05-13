package com.thuexe.dto;

public class LoaiXeDTO {
    private int maLoaiXe;
    private String tenLoai;
    private String loaiNhienLieu;
    private double giaThueNgay;
    private double giaThueGio;

    public LoaiXeDTO() {}

    public LoaiXeDTO(int maLoaiXe, String tenLoai, String loaiNhienLieu, double giaThueNgay, double giaThueGio) {
        this.maLoaiXe = maLoaiXe;
        this.tenLoai = tenLoai;
        this.loaiNhienLieu = loaiNhienLieu;
        this.giaThueNgay = giaThueNgay;
        this.giaThueGio = giaThueGio;
    }

    // Getter và Setter
    public int getMaLoaiXe() { return maLoaiXe; }
    public void setMaLoaiXe(int maLoaiXe) { this.maLoaiXe = maLoaiXe; }
    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }
    public String getLoaiNhienLieu() { return loaiNhienLieu; }
    public void setLoaiNhienLieu(String loaiNhienLieu) { this.loaiNhienLieu = loaiNhienLieu; }
    public double getGiaThueNgay() { return giaThueNgay; }
    public void setGiaThueNgay(double giaThueNgay) { this.giaThueNgay = giaThueNgay; }
    public double getGiaThueGio() { return giaThueGio; }
    public void setGiaThueGio(double giaThueGio) { this.giaThueGio = giaThueGio; }
}