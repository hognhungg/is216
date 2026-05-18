package com.thuexe.dto;

import java.util.Date;

public class DanhGiaDTO {
    
    private int maDG;
    private int maPhieuThue;
    private int diemSo;
    private String noiDung;
    private Date ngayDG;
    
    // Constructor mặc định
    public DanhGiaDTO() {}
    
    // Constructor đầy đủ
    public DanhGiaDTO(int maDG, int maPhieuThue, int diemSo, String noiDung, Date ngayDG) {
        this.maDG = maDG;
        this.maPhieuThue = maPhieuThue;
        this.diemSo = diemSo;
        this.noiDung = noiDung;
        this.ngayDG = ngayDG;
    }
    
    // Constructor không cần mã (tự sinh)
    public DanhGiaDTO(int maPhieuThue, int diemSo, String noiDung) {
        this.maPhieuThue = maPhieuThue;
        this.diemSo = diemSo;
        this.noiDung = noiDung;
    }
    
    // Getters và Setters
    public int getMaDG() {
        return maDG;
    }
    
    public void setMaDG(int maDG) {
        this.maDG = maDG;
    }
    
    public int getMaPhieuThue() {
        return maPhieuThue;
    }
    
    public void setMaPhieuThue(int maPhieuThue) {
        this.maPhieuThue = maPhieuThue;
    }
    
    public int getDiemSo() {
        return diemSo;
    }
    
    public void setDiemSo(int diemSo) {
        this.diemSo = diemSo;
    }
    
    public String getNoiDung() {
        return noiDung;
    }
    
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
    
    public Date getNgayDG() {
        return ngayDG;
    }
    
    public void setNgayDG(Date ngayDG) {
        this.ngayDG = ngayDG;
    }
    
    @Override
    public String toString() {
        return "DanhGiaDTO{" +
                "maDG=" + maDG +
                ", maPhieuThue=" + maPhieuThue +
                ", diemSo=" + diemSo +
                ", noiDung='" + noiDung + '\'' +
                ", ngayDG=" + ngayDG +
                '}';
    }
}