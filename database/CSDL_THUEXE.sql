CREATE TABLE TAIKHOAN (
    MaChuThe NUMBER PRIMARY KEY,
    TenDangNhap VARCHAR2(50) UNIQUE NOT NULL,
    MatKhau VARCHAR2(255) NOT NULL,
    NgayTao DATE,
    VaiTro VARCHAR2(50),
    TrangThai VARCHAR2(20)
);

CREATE TABLE LOAIXE (
    MaLoaiXe NUMBER PRIMARY KEY,
    TenLoai VARCHAR2(50),
    LoaiNhienLieu VARCHAR2(20),
    GiaThueNgay NUMBER CHECK (GiaThueNgay > 0), 
    GiaThueGio NUMBER
);

CREATE TABLE CHUXE (
    MaChuXe NUMBER PRIMARY KEY,
    HoTen VARCHAR2(100),
    SDT NUMBER,
    Email VARCHAR2(100),
    DiaChi VARCHAR2(255),
    CCCD VARCHAR2(12) UNIQUE,
    MaSoThue VARCHAR2(20)
);

CREATE TABLE KHUYENMAI (
    MaKM NUMBER PRIMARY KEY,
    TenChuongTrinh VARCHAR2(255),
    PhanTramGiam NUMBER,
    GiamToiDa NUMBER,
    NgayBatDau DATE,
    NgayKetThuc DATE,
    DieuKien VARCHAR2(500),
    CONSTRAINT CHK_NGAY_KM CHECK (NgayKetThuc >= NgayBatDau) 
);

CREATE TABLE KHACHHANG (
    MaKhachHang NUMBER PRIMARY KEY,
    HoTen VARCHAR2(100),
    CCCD VARCHAR2(12) UNIQUE,
    SoBangLai VARCHAR2(20),
    SDT VARCHAR2(10),
    GioiTinh VARCHAR2(10),
    NgaySinh DATE,
    DiaChi VARCHAR2(255),
    MaChuThe NUMBER,
    CONSTRAINT FK_KH_TAIKHOAN FOREIGN KEY (MaChuThe) REFERENCES TAIKHOAN(MaChuThe)
);

CREATE TABLE DOANHNGHIEPQUANLY (
    MaDoanhNghiep NUMBER PRIMARY KEY,
    TenDoanhNghiep VARCHAR2(255),
    HoTenNDD VARCHAR2(100),
    ChucVu VARCHAR2(50),
    SDT NUMBER,
    Email VARCHAR2(100),
    DiaChi VARCHAR2(255),
    MaChuThe NUMBER,
    CONSTRAINT FK_DN_TAIKHOAN FOREIGN KEY (MaChuThe) REFERENCES TAIKHOAN(MaChuThe)
);

CREATE TABLE NHANVIEN (
    MaNhanVien NUMBER PRIMARY KEY,
    HoTenNV VARCHAR2(100),
    CCCD VARCHAR2(12) UNIQUE,
    SDT NUMBER,
    Email VARCHAR2(100),
    MaChuThe NUMBER,
    MaDoanhNghiep NUMBER,
    CONSTRAINT FK_NV_TAIKHOAN FOREIGN KEY (MaChuThe) REFERENCES TAIKHOAN(MaChuThe),
    CONSTRAINT FK_NV_DOANHNGHIEP FOREIGN KEY (MaDoanhNghiep) REFERENCES DOANHNGHIEPQUANLY(MaDoanhNghiep)
);

CREATE TABLE XE (
    BienSoXe VARCHAR2(15) PRIMARY KEY,
    ThuongHieu VARCHAR2(50),
    TenXe VARCHAR2(100),
    SoCho NUMBER,
    HinhAnh VARCHAR2(255),
    TrangThai VARCHAR2(20),
    MaChuXe NUMBER,
    MaLoaiXe NUMBER,
    MaDoanhNghiep NUMBER,
    CONSTRAINT FK_XE_CHUXE FOREIGN KEY (MaChuXe) REFERENCES CHUXE(MaChuXe),
    CONSTRAINT FK_XE_LOAIXE FOREIGN KEY (MaLoaiXe) REFERENCES LOAIXE(MaLoaiXe),
    CONSTRAINT FK_XE_DN FOREIGN KEY (MaDoanhNghiep) REFERENCES DOANHNGHIEPQUANLY(MaDoanhNghiep)
);

CREATE TABLE HOPDONG (
    MaHopDong NUMBER PRIMARY KEY,
    ThoiGianNhanXe DATE,
    ThoiGianTraXe DATE,
    DiaDiemNhanXe VARCHAR2(255),
    DiaDiemTraXe VARCHAR2(255),
    ThoiGianLap DATE DEFAULT SYSDATE,
    TrangThaiHopDong VARCHAR2(50),
    MaKhachHang NUMBER,
    CONSTRAINT FK_HD_KH FOREIGN KEY (MaKhachHang) REFERENCES KHACHHANG(MaKhachHang),
    CONSTRAINT CHK_TG_NHAN CHECK (ThoiGianNhanXe >= ThoiGianLap) 
);

CREATE TABLE CT_HOPDONG_XE (
    MaHopDong NUMBER,
    BienSoXe VARCHAR2(15),
    TinhTrangNhan VARCHAR2(500),
    TinhTrangTra VARCHAR2(500),
    HinhAnhMinhChung VARCHAR2(255),
    PhuTungKemTheo VARCHAR2(255),
    PRIMARY KEY (MaHopDong, BienSoXe), 
    CONSTRAINT FK_CTHD_HD FOREIGN KEY (MaHopDong) REFERENCES HOPDONG(MaHopDong),
    CONSTRAINT FK_CTHD_XE FOREIGN KEY (BienSoXe) REFERENCES XE(BienSoXe)
);

CREATE TABLE BIENBAN (
    MaBienBan NUMBER PRIMARY KEY,
    NgayKyNhan DATE,
    NgayKyTra DATE,
    TinhTrangBanGiao VARCHAR2(500),
    MaHopDong NUMBER,
    MaNhanVien NUMBER,
    CONSTRAINT FK_BB_HD FOREIGN KEY (MaHopDong) REFERENCES HOPDONG(MaHopDong),
    CONSTRAINT FK_BB_NV FOREIGN KEY (MaNhanVien) REFERENCES NHANVIEN(MaNhanVien)
);

CREATE TABLE HOADON (
    MaHD NUMBER PRIMARY KEY,
    NgHD DATE,
    TienThueGoc NUMBER,
    PhiPhatSinh NUMBER,
    SoTienGiam NUMBER,
    TrangThaiHD VARCHAR2(20),
    TongTien NUMBER,
    MaBB NUMBER,
    MaKM NUMBER,
    CONSTRAINT FK_HDON_HD FOREIGN KEY (MaPhieuThue) REFERENCES HOPDONG(MaHopDong),
    CONSTRAINT FK_HDON_BB FOREIGN KEY (MaBB) REFERENCES BIENBAN(MaBienBan),
    CONSTRAINT FK_HDON_KM FOREIGN KEY (MaKM) REFERENCES KHUYENMAI(MaKM)
);

CREATE SEQUENCE SEQ_HOADON START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE DANHGIA (
    MaDG NUMBER PRIMARY KEY,
    MaPhieuThue NUMBER NOT NULL,
    DiemSo NUMBER CHECK (DiemSo BETWEEN 1 AND 5),
    NoiDung VARCHAR2(1000),
    CONSTRAINT FK_DG_PHIEUTHUE FOREIGN KEY (MaPhieuThue) REFERENCES HOPDONG(MaHopDong)
);

CREATE TABLE THANHTOAN (
    MaTT NUMBER PRIMARY KEY,
    NgayTT DATE,
    SoTien NUMBER,
    PhuongThuc VARCHAR2(50),
    NoiDung VARCHAR2(255),
    TrangThai VARCHAR2(20),
    MaHD NUMBER,
    CONSTRAINT FK_TT_HOADON FOREIGN KEY (MaHD) REFERENCES HOADON(MaHD)
);

-- 1. Trigger tự động tính Tổng Tiền Hóa Đơn khi chèn hoặc cập nhật
CREATE OR REPLACE TRIGGER TRG_TINH_TONG_TIEN
BEFORE INSERT OR UPDATE ON HOADON
FOR EACH ROW
BEGIN
    IF :new.TienThueGoc < 0 OR :new.PhiPhatSinh < 0 OR :new.SoTienGiam < 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Loi: Cac gia tri thanh phan khong duoc am.');
    END IF;

    :new.TongTien := NVL(:new.TienThueGoc, 0) + NVL(:new.PhiPhatSinh, 0) - NVL(:new.SoTienGiam, 0);

    IF :new.TongTien < 0 THEN
        :new.TongTien := 0;
    END IF;
END;
/

-- 2. Trigger tự động cập nhật trạng thái Xe thành 'Dang thue' khi có hợp đồng mới
CREATE OR REPLACE TRIGGER TRG_CAPNHAT_TRANGTHAI_XE
AFTER INSERT ON CT_HOPDONG_XE
FOR EACH ROW
BEGIN
    UPDATE XE
    SET TrangThai = 'Dang thue'
    WHERE BienSoXe = :new.BienSoXe;
END;
/

-- 3. Trigger kiểm tra ngày thuê xe phải trước ngày trả xe
CREATE OR REPLACE TRIGGER TRG_KIEMTRA_NGAY_HD
BEFORE INSERT OR UPDATE ON HOPDONG
FOR EACH ROW
BEGIN
    IF :new.ThoiGianTraXe <= :new.ThoiGianNhanXe THEN
        RAISE_APPLICATION_ERROR(-20010, 'Loi: Thoi gian tra xe phai sau thoi gian nhan xe');
    END IF;
END;
/

CREATE OR REPLACE TRIGGER TRG_CHECK_TUOI_VA_BANGLAI
BEFORE INSERT OR UPDATE ON HOPDONG
FOR EACH ROW
DECLARE
    v_NgaySinh DATE;
    v_SoBangLai VARCHAR2(50);
    v_Tuoi NUMBER;
BEGIN
    SELECT NgaySinh, SoBangLai INTO v_NgaySinh, v_SoBangLai
    FROM KHACHHANG
    WHERE MaKhachHang = :new.MaKhachHang;

    v_Tuoi := MONTHS_BETWEEN(SYSDATE, v_NgaySinh) / 12;

    IF v_Tuoi < 18 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Loi: Khach hang chua du 18 tuoi de thue xe.');
    END IF;

    IF v_SoBangLai IS NULL THEN
        RAISE_APPLICATION_ERROR(-20003, 'Loi: Khach hang phai co so bang lai de thue xe.');
    END IF;
END;
/

-- 1. Thủ tục thêm tài khoản mới
CREATE OR REPLACE PROCEDURE Register_New_Customer (
    i_ten_dang_nhap IN TAIKHOAN.TenDangNhap%TYPE,
    i_mat_khau      IN TAIKHOAN.MatKhau%TYPE,
    i_ho_ten        IN KHACHHANG.HoTen%TYPE,
    i_cccd          IN KHACHHANG.CCCD%TYPE,
    i_so_bang_lai   IN KHACHHANG.SoBangLai%TYPE,
    i_sdt           IN KHACHHANG.SDT%TYPE,
    i_gioi_tinh     IN KHACHHANG.GioiTinh%TYPE,
    i_ngay_sinh     IN KHACHHANG.NgaySinh%TYPE,
    i_dia_chi       IN KHACHHANG.DiaChi%TYPE,
    o_makh_out      OUT KHACHHANG.MaKhachHang%TYPE
) IS
    v_MaChuThe TAIKHOAN.MaChuThe%TYPE;
BEGIN
    IF MONTHS_BETWEEN(SYSDATE, i_ngay_sinh) / 12 < 18 THEN
        RAISE_APPLICATION_ERROR(-20004, 'Khach hang chua du 18 tuoi.');
    END IF;

    INSERT INTO TAIKHOAN (TenDangNhap, MatKhau, NgayTao, VaiTro, TrangThai)
    VALUES (i_ten_dang_nhap, i_mat_khau, SYSDATE, 'Khach hang', 'Hoat dong')
    RETURNING MaChuThe INTO v_MaChuThe; -- Đã sửa dấu ; ở trên

    INSERT INTO KHACHHANG (HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe)
    VALUES (i_ho_ten, i_cccd, i_so_bang_lai, i_sdt, i_gioi_tinh, i_ngay_sinh, i_dia_chi, v_MaChuThe)
    RETURNING MaKhachHang INTO o_makh_out;
    
    COMMIT;
EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20003, 'Loi dang ky: ' || SQLERRM);
END;
/

-- 2. Thủ tục thêm xe mới
CREATE OR REPLACE PROCEDURE PROC_INSERT_XE(
    p_BienSo IN VARCHAR2,
    p_ThuongHieu IN VARCHAR2,
    p_TenXe IN VARCHAR2,
    p_SoCho IN NUMBER,
    p_MaChuXe IN NUMBER,
    p_MaLoaiXe IN NUMBER, -- Đã sửa
    p_MaDN IN NUMBER
) IS
BEGIN
    INSERT INTO XE (BienSoXe, ThuongHieu, TenXe, SoCho, TrangThai, MaChuXe, MaLoaiXe, MaDoanhNghiep)
    VALUES (p_BienSo, p_ThuongHieu, p_TenXe, p_SoCho, 'San sang', p_MaChuXe, p_MaLoaiXe, p_MaDN);
    COMMIT;
END;
/

-- 3. Thủ tục lập hợp đồng (Tự động lấy thời gian hiện tại làm ngày lập)
CREATE OR REPLACE PROCEDURE PROC_LAP_HOPDONG(
    p_MaHD IN NUMBER,
    p_NgayNhan IN DATE,
    p_NgayTra IN DATE,
    p_ĐiaiemNhan IN VARCHAR2,
    p_MaKH IN NUMBER
) IS
BEGIN
    INSERT INTO HOPDONG (MaHopDong, ThoiGianNhanXe, ThoiGianTraXe, DiaDiemNhanXe, ThoiGianLap, TrangThaiHopDong, MaKhachHang)
    VALUES (p_MaHD, p_NgayNhan, p_NgayTra, p_DiaDiemNhan, SYSDATE, 'Moi tao', p_MaKH);
    COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE Create_Rental_Contract (
    i_bien_so_xe       IN XE.BienSoXe%TYPE,
    i_ma_khach_hang    IN KHACHHANG.MaKhachHang%TYPE,
    i_tg_nhan_xe      IN HOPDONG.ThoiGianNhanXe%TYPE,
    i_dd_nhan_xe      IN HOPDONG.DiaDiemNhanXe%TYPE,
    i_dd_tra_xe       IN HOPDONG.DiaDiemTraXe%TYPE,
    o_ma_hd_out       OUT HOPDONG.MaHopDong%TYPE
) IS
    v_trang_thai_xe   XE.TrangThai%TYPE;
    v_MaHD            HOPDONG.MaHopDong%TYPE;
BEGIN
    -- Kiểm tra trạng thái xe trong bảng XE dựa trên biển số
    SELECT TrangThai INTO v_trang_thai_xe
    FROM XE
    WHERE BienSoXe = i_bien_so_xe;

    IF v_trang_thai_xe <> 'San sang' THEN
        RAISE_APPLICATION_ERROR(-20005, 'Xe hien khong san sang cho thue (Trang thai: ' || v_trang_thai_xe || ').');
    END IF;

    -- Thêm bản ghi mới vào bảng HOPDONG
    -- Trạng thái mặc định: “Chờ nhận xe”, Thời gian lập: SYSDATE
    INSERT INTO HOPDONG (ThoiGianNhanXe, DiaDiemNhanXe, DiaDiemTraXe, ThoiGianLap, TrangThaiHopDong, MaKhachHang)
    VALUES (i_tg_nhan_xe, i_dd_nhan_xe, i_dd_tra_xe, SYSDATE, 'Cho nhan xe', i_ma_khach_hang)
    RETURNING MaHopDong INTO v_MaHD;

    -- Thêm dữ liệu vào bảng CT_HOPDONG_XE (Chi tiết hợp đồng)
    -- Ghi chú: Dựa trên lược đồ CT_HOPDONG_XE(MaHopDong, BienSoXe, ...)
    INSERT INTO CT_HOPDONG_XE (MaHopDong, BienSoXe)
    VALUES (v_MaHD, i_bien_so_xe);

    -- Cập nhật trạng thái xe trong bảng XE thành “Đang thuê”
    UPDATE XE
    SET TrangThai = 'Dang thue'
    WHERE BienSoXe = i_bien_so_xe;

    -- Gán mã hợp đồng vừa tạo cho tham số đầu ra
    o_ma_hd_out := v_MaHD;

    -- Xác nhận thay đổi
    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20006, 'Khong tim thay thong tin xe voi bien so da cung cap.');
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20007, 'Loi lap hop dong: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE Create_Invoice_From_Report (
    i_ma_bb         IN BIENBAN.MaBienBan%TYPE,
    i_ma_km         IN KHUYENMAI.MaKM%TYPE DEFAULT NULL,
    o_ma_hd_out     OUT HOADON.MaHD%TYPE
) IS
    v_ma_hd_thue    HOPDONG.MaHopDong%TYPE;
    v_gia_thue_ngay LOAIXE.GiaThueNgay%TYPE;
    v_ngay_nhan     DATE;
    v_ngay_tra      DATE;
    v_so_ngay_thue  NUMBER;
    v_tien_thue_goc NUMBER;
    v_phan_tram_km  NUMBER := 0;
    v_giam_toi_da   NUMBER := 0;
    v_so_tien_giam  NUMBER := 0;
    v_tong_tien     NUMBER;
BEGIN
    -- Truy xuất thông tin thời gian thuê và đơn giá từ BIENBAN, HOPDONG và LOAIXE
    SELECT bb.MaHopDong, bb.NgayKyNhan, bb.NgayKyTra, lx.GiaThueNgay
    INTO v_ma_hd_thue, v_ngay_nhan, v_ngay_tra, v_gia_thue_ngay
    FROM BIENBAN bb
    JOIN HOPDONG hd ON bb.MaHopDong = hd.MaHopDong
    JOIN CT_HOPDONG_XE ct ON hd.MaHopDong = ct.MaHopDong
    JOIN XE x ON ct.BienSoXe = x.BienSoXe
    JOIN LOAIXE lx ON x.MaLoaiXe = lx.MaLoaiXe
    WHERE bb.MaBienBan = i_ma_bb;
    -- Tính tiền thuê gốc dựa trên số ngày thuê thực tế
    -- Sử dụng CEIL để làm tròn lên nếu thuê lẻ ngày
    v_so_ngay_thue := CEIL(v_ngay_tra - v_ngay_nhan);
    IF v_so_ngay_thue = 0 THEN v_so_ngay_thue := 1; END IF; -- Tính ít nhất 1 ngày
    
    v_tien_thue_goc := v_so_ngay_thue * v_gia_thue_ngay;

    -- Kiểm tra mã khuyến mãi (nếu có)
    IF i_ma_km IS NOT NULL THEN
        BEGIN
            SELECT PhanTramGiam, GiamToiDa 
            INTO v_phan_tram_km, v_giam_toi_da
            FROM KHUYENMAI
            WHERE MaKM = i_ma_km 
              AND TRUNC(SYSDATE) BETWEEN NgayBatDau AND NgayKetThuc;
            
            v_so_tien_giam := v_tien_thue_goc * (v_phan_tram_km / 100);
            -- Khống chế mức giảm tối đa
            IF v_so_tien_giam > v_giam_toi_da THEN
                v_so_tien_giam := v_giam_toi_da;
            END IF;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                v_so_tien_giam := 0; -- Mã không tồn tại hoặc hết hạn
        END;
    END IF;

    -- Tính tổng tiền cuối cùng
    v_tong_tien := v_tien_thue_goc - v_so_tien_giam;

    -- Thêm bản ghi mới vào bảng HOADON
    INSERT INTO HOADON (MaHD, MaPhieuThue, NgayThanhToan, TienThueGoc, PhiPhatSinh, SoTienGiam, TrangThaiHD, MaBB, MaKM)
    VALUES (SEQ_HOADON.NEXTVAL, v_ma_hd_thue, SYSDATE, v_tien_thue_goc, 0, v_so_tien_giam, 'Chua thanh toan', i_ma_bb, i_ma_km)
    RETURNING MaHD INTO o_ma_hd_out;

    -- Cập nhật trạng thái xe thành “Sẵn sàng” sau khi đã hoàn tất biên bản trả và lập hóa đơn
    UPDATE XE
    SET TrangThai = 'San sang'
    WHERE BienSoXe = (SELECT BienSoXe FROM CT_HOPDONG_XE WHERE MaHopDong = v_ma_hd_thue);

    -- Kết thúc thủ tục
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20008, 'Loi lap hoa don: ' || SQLERRM);
END;
/

CREATE OR REPLACE PROCEDURE PROC_TRA_XE (
    p_MaPhieu      IN HOPDONG.MaHopDong%TYPE,
    p_NgayTra      IN DATE,
    p_PhiPhatSinh  IN NUMBER
) IS
    v_bien_so_xe     XE.BienSoXe%TYPE;
    v_ngay_nhan      DATE;
    v_gia_thue_ngay  LOAIXE.GiaThueNgay%TYPE;
    v_so_ngay_thue   NUMBER;
    v_tien_thue_goc  NUMBER;
    v_tong_tien      NUMBER;
    v_ma_hd          HOADON.MaHD%TYPE;
BEGIN
    SELECT hd.ThoiGianNhanXe, x.BienSoXe, lx.GiaThueNgay
    INTO v_ngay_nhan, v_bien_so_xe, v_gia_thue_ngay
    FROM HOPDONG hd
    JOIN CT_HOPDONG_XE ct ON hd.MaHopDong = ct.MaHopDong
    JOIN XE x ON ct.BienSoXe = x.BienSoXe
    JOIN LOAIXE lx ON x.MaLoaiXe = lx.MaLoaiXe
    WHERE hd.MaHopDong = p_MaPhieu;

    IF p_NgayTra <= v_ngay_nhan THEN
        RAISE_APPLICATION_ERROR(-20015, 'Ngay tra phai sau ngay nhan xe.');
    END IF;

    v_so_ngay_thue := CEIL(p_NgayTra - v_ngay_nhan);
    IF v_so_ngay_thue < 1 THEN
        v_so_ngay_thue := 1;
    END IF;

    v_tien_thue_goc := v_so_ngay_thue * v_gia_thue_ngay;
    v_tong_tien := v_tien_thue_goc + NVL(p_PhiPhatSinh, 0);

    UPDATE XE
    SET TrangThai = 'Trống'
    WHERE BienSoXe = v_bien_so_xe;

    UPDATE HOPDONG
    SET ThoiGianTraXe = p_NgayTra,
        TrangThaiHopDong = 'Da tra'
    WHERE MaHopDong = p_MaPhieu;

    INSERT INTO HOADON (MaHD, MaPhieuThue, NgayThanhToan, TienThueGoc, PhiPhatSinh, SoTienGiam, TrangThaiHD, MaBB, MaKM)
    VALUES (SEQ_HOADON.NEXTVAL, p_MaPhieu, SYSDATE, v_tien_thue_goc, NVL(p_PhiPhatSinh, 0), 0, 'Chua thanh toan', NULL, NULL)
    RETURNING MaHD INTO v_ma_hd;

    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20016, 'Khong tim thay phieu thue: ' || p_MaPhieu);
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20017, 'Loi PROC_TRA_XE: ' || SQLERRM);
END;
/

-- Sửa 'Active' thành 'Hoat dong' và '123' thành 'MTIz' (là 123 đã mã hóa)
INSERT INTO TAIKHOAN (MaChuThe, TenDangNhap, MatKhau, VaiTro, TrangThai) 
VALUES (1, 'admin', 'MTIz', 'Admin', 'Hoat dong');

INSERT INTO TAIKHOAN (MaChuThe, TenDangNhap, MatKhau, VaiTro, TrangThai) 
VALUES (2, 'nv01', 'MTIz', 'NhanVien', 'Hoat dong');

SELECT * FROM TAIKHOAN WHERE TenDangNhap = 'admin';

CREATE OR REPLACE PROCEDURE SP_DANG_NHAP (
    p_username IN VARCHAR2,
    p_password IN VARCHAR2,
    p_cursor   OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
    SELECT * FROM TAIKHOAN 
    WHERE TenDangNhap = p_username 
      AND MatKhau = p_password;
END;
/

-- 1. Procedure Xóa xe
CREATE OR REPLACE PROCEDURE PROC_DELETE_XE(p_BienSo IN VARCHAR2) IS
BEGIN
    DELETE FROM XE WHERE BienSoXe = p_BienSo;
    COMMIT;
END;
/

-- 2. Procedure Tìm xe trống (Dùng Cursor)
CREATE OR REPLACE PROCEDURE PROC_TIM_XE_TRONG (
    p_ngay_nhan IN DATE,
    p_ngay_tra  IN DATE,
    p_cursor    OUT SYS_REFCURSOR
) AS
BEGIN
    OPEN p_cursor FOR
    SELECT x.*, l.TenLoai 
    FROM XE x 
    JOIN LOAIXE l ON x.MaLoaiXe = l.MaLoaiXe
    WHERE x.BienSoXe NOT IN (
        SELECT BienSoXe FROM CT_HOPDONG_XE ct
        JOIN HOPDONG h ON ct.MaHopDong = h.MaHopDong
        WHERE (p_ngay_nhan BETWEEN h.ThoiGianNhanXe AND h.ThoiGianTraXe)
           OR (p_ngay_tra BETWEEN h.ThoiGianNhanXe AND h.ThoiGianTraXe)
    );
END;
/