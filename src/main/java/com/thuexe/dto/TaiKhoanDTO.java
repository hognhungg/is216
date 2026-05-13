package com.thuexe.dto; 

import java.util.Date;

public class TaiKhoanDTO {
    // Thuộc tính private để đảm bảo tính đóng gói 
    private int maChuThe;
    private String tenDangNhap;
    private String matKhau;
    private Date ngayTao;
    private String vaiTro;
    private String trangThai;

    // Hàm khởi tạo không đối số (Constructor mặc định) 
    public TaiKhoanDTO() {}

    // Hàm khởi tạo đầy đủ đối số 
    public TaiKhoanDTO(int maChuThe, String tenDangNhap, String matKhau, Date ngayTao, String vaiTro, String trangThai) {
        this.maChuThe = maChuThe;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.ngayTao = ngayTao;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    // Các phương thức Getter và Setter để truy xuất dữ liệu từ ngoài gói 
    public int getMaChuThe() { return maChuThe; }
    public void setMaChuThe(int maChuThe) { this.maChuThe = maChuThe; }

    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public Date getNgayTao() { return ngayTao; }
    public void setNgayTao(Date ngayTao) { this.ngayTao = ngayTao; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}