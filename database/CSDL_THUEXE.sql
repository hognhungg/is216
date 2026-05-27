CREATE TABLE TAIKHOAN (
    MaChuThe NUMBER PRIMARY KEY,
    TenDangNhap VARCHAR2(50) UNIQUE NOT NULL,
    MatKhau VARCHAR2(255) NOT NULL,
    NgayTao DATE DEFAULT SYSDATE,
    VaiTro VARCHAR2(50),
    TrangThai VARCHAR2(20)
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
    SDT VARCHAR2(10),
    Email VARCHAR2(100),
    DiaChi VARCHAR2(255),
    MaChuThe NUMBER,
    CONSTRAINT FK_DN_TAIKHOAN FOREIGN KEY (MaChuThe) REFERENCES TAIKHOAN(MaChuThe)
);

CREATE TABLE NHANVIEN (
    MaNhanVien NUMBER PRIMARY KEY,
    HoTenNV VARCHAR2(100),
    CCCD VARCHAR2(12) UNIQUE,
    SDT VARCHAR2(10),
    Email VARCHAR2(100),
    MaChuThe NUMBER,
    MaDoanhNghiep NUMBER,
    CONSTRAINT FK_NV_TAIKHOAN FOREIGN KEY (MaChuThe) REFERENCES TAIKHOAN(MaChuThe),
    CONSTRAINT FK_NV_DOANHNGHIEP FOREIGN KEY (MaDoanhNghiep) REFERENCES DOANHNGHIEPQUANLY(MaDoanhNghiep)
);

CREATE TABLE LOAIXE (
    MaLoaiXe NUMBER PRIMARY KEY,
    TenLoai VARCHAR2(50),
    LoaiNhienLieu VARCHAR2(20),
    GiaThueNgay NUMBER CHECK (GiaThueNgay > 0), 
    GiaThueGio NUMBER CHECK (GiaThueGio >= 0)
);

CREATE TABLE CHUXE (
    MaChuXe NUMBER PRIMARY KEY,
    HoTen VARCHAR2(100),
    SDT VARCHAR2(10),
    Email VARCHAR2(100),
    DiaChi VARCHAR2(255),
    CCCD VARCHAR2(12) UNIQUE,
    MaSoThue VARCHAR2(20),
    MaChuThe NUMBER,
    CONSTRAINT FK_CX_TAIKHOAN FOREIGN KEY (MaChuThe) REFERENCES TAIKHOAN(MaChuThe)
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
    CONSTRAINT CHK_TG_NHAN CHECK (ThoiGianNhanXe >= ThoiGianLap),
    CONSTRAINT CHK_TG_TRA CHECK (ThoiGianTraXe >= ThoiGianNhanXe)
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

CREATE TABLE HOADON (
    MaHD NUMBER PRIMARY KEY,
    NgHD DATE DEFAULT SYSDATE,
    TienThueGoc NUMBER DEFAULT 0,
    PhiPhatSinh NUMBER DEFAULT 0,
    SoTienGiam NUMBER DEFAULT 0,
    TrangThaiHD VARCHAR2(20),
    TongTien NUMBER DEFAULT 0,
    MaBB NUMBER,
    MaKM NUMBER,
    CONSTRAINT FK_HDON_BB FOREIGN KEY (MaBB) REFERENCES BIENBAN(MaBienBan),
    CONSTRAINT FK_HDON_KM FOREIGN KEY (MaKM) REFERENCES KHUYENMAI(MaKM)
);

CREATE TABLE THANHTOAN (
    MaTT NUMBER PRIMARY KEY,
    NgayTT DATE DEFAULT SYSDATE,
    SoTien NUMBER CHECK (SoTien > 0),
    PhuongThuc VARCHAR2(50),
    NoiDung VARCHAR2(255),
    TrangThai VARCHAR2(20),
    MaHD NUMBER,
    CONSTRAINT FK_TT_HOADON FOREIGN KEY (MaHD) REFERENCES HOADON(MaHD)
);
CREATE SEQUENCE SEQ_HOADON START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE DANHGIA (
    MaDG NUMBER PRIMARY KEY,
    MaPhieuThue NUMBER NOT NULL,
    DiemSo NUMBER CHECK (DiemSo BETWEEN 1 AND 5),
    NoiDung VARCHAR2(1000),
    CONSTRAINT FK_DG_PHIEUTHUE FOREIGN KEY (MaPhieuThue) REFERENCES HOPDONG(MaHopDong)
);


CREATE OR REPLACE TRIGGER TRG_KIEMTRA_TRUNG_LICH_XE
BEFORE INSERT OR UPDATE ON CT_HOPDONG_XE
FOR EACH ROW
DECLARE
    v_count NUMBER;
    v_ngay_nhan  HOPDONG.ThoiGianNhanXe%TYPE;
    v_ngay_tra   HOPDONG.ThoiGianTraXe%TYPE;
BEGIN

    SELECT ThoiGianNhanXe, ThoiGianTraXe
    INTO v_ngay_nhan, v_ngay_tra
    FROM HOPDONG
    WHERE MaHopDong = :NEW.MaHopDong;

    SELECT COUNT(*)
    INTO v_count
    FROM CT_HOPDONG_XE ct
    JOIN HOPDONG hd
        ON ct.MaHopDong = hd.MaHopDong
    WHERE ct.BienSoXe = :NEW.BienSoXe
        AND ct.MaHopDong <> :NEW.MaHopDong
        AND hd.TrangThaiHopDong NOT IN ('Da_Huy', 'Hoan_Thanh')
        AND (
            v_ngay_nhan < hd.ThoiGianTraXe
            AND v_ngay_tra > hd.ThoiGianNhanXe
        );

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(
            -20011,
            'Xe co bien so ' || :NEW.BienSoXe ||
            ' da duoc thue trong khoang thoi gian nay'
        );
    END IF;

END;
/

--Xe phải ở trạng thái “Available” mới được thêm vào hợp đồng
CREATE OR REPLACE TRIGGER TRG_CHECK_XE_SANSANG
BEFORE INSERT ON CT_HOPDONG_XE
FOR EACH ROW
DECLARE
    v_trang_thai XE.TrangThai%TYPE;
BEGIN

    SELECT TrangThai
    INTO v_trang_thai
    FROM XE
    WHERE BienSoXe = :NEW.BienSoXe;

    IF v_trang_thai <> 'Available' THEN
        RAISE_APPLICATION_ERROR(
            -20012,
            'Xe khong o trang thai Available de them vao hop dong'
        );
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(
            -20013,
            'Bien so xe khong ton tai'
        );
END;
/

--Khi lập biên bản trả xe, cập nhật trạng thái xe về “Available”
CREATE OR REPLACE TRIGGER TRG_UPDATE_TRANGTHAI_XE_TRA
AFTER INSERT ON BIENBAN
FOR EACH ROW
BEGIN
    UPDATE XE
    SET TrangThai = 'Available'
    WHERE BienSoXe IN (
        SELECT BienSoXe
        FROM CT_HOPDONG_XE
        WHERE MaHopDong = :NEW.MaHopDong
    );
END;
/

--Ngày ký biên bản bàn giao phải trùng với ngày nhận xe trong hợp đồng
CREATE OR REPLACE TRIGGER TRG_BB_CHECK_NGAY_KY_NHAN
BEFORE INSERT OR UPDATE OF NgayKyNhan, MaHopDong
ON BIENBAN
FOR EACH ROW
DECLARE
    v_tg_nhan_hd HOPDONG.ThoiGianNhanXe%TYPE;BEGIN

    SELECT ThoiGianNhanXe
    INTO v_tg_nhan_hd
    FROM HOPDONG
    WHERE MaHopDong = :NEW.MaHopDong;

    IF TRUNC(:NEW.NgayKyNhan) <> TRUNC(v_tg_nhan_hd) THEN
        RAISE_APPLICATION_ERROR(
            -20005,
            'Ngay ky bien ban phai trung voi ngay nhan xe trong hop dong'
        );
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(
            -20006,
            'Ma hop dong ' || :NEW.MaHopDong || ' khong ton tai'
        );
END;
/

--Khách hàng phải từ 18 tuổi trở lên mới được thuê xe
CREATE OR REPLACE TRIGGER TRG_CHECK_TUOI_KHACHHANG
BEFORE INSERT OR UPDATE OF NgaySinh ON KHACHHANG
FOR EACH ROW
DECLARE
    v_tuoi NUMBER;
BEGIN

    IF :NEW.NgaySinh IS NOT NULL THEN

        v_tuoi := TRUNC(MONTHS_BETWEEN(SYSDATE, :NEW.NgaySinh) / 12);

        IF v_tuoi < 18 THEN
            RAISE_APPLICATION_ERROR(
                -20008,
                'Khach hang hien tai moi ' || v_tuoi ||
                ' tuoi. Yeu cau phai du 18 tuoi tro len moi duoc thue xe'
            );
        END IF;

    END IF;

END;
/



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

--Số tiền thanh toán thực tế không được vượt quá tổng tiền hóa đơn
CREATE OR REPLACE TRIGGER TRG_TT_CHECK_SOTIEN
BEFORE INSERT OR UPDATE OF SoTien, MaHD ON THANHTOAN
FOR EACH ROW
DECLARE
    v_tong_tien HOADON.TongTien%TYPE;
BEGIN
    SELECT TongTien INTO v_tong_tien
    FROM HOADON
    WHERE MaHD = :NEW.MaHD;

    IF :NEW.SoTien > v_tong_tien THEN
        RAISE_APPLICATION_ERROR(-20009, 'So tien thanh toan khong duoc vuot qua tong tien hoa don');
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20010, 'Ma hoa don khong ton tai');
END;
/

--Kiểm tra tính hợp lệ của khuyến mãi: Ngày hóa đơn phải nằm trong thời gian áp dụng của chương trình khuyến mãi
CREATE OR REPLACE TRIGGER TRG_KHUYENMAI
BEFORE INSERT OR UPDATE OF MaKM, NgHD
ON HOADON
FOR EACH ROW
DECLARE
    v_ngay_bd KHUYENMAI.NgayBatDau%TYPE;
    v_ngay_kt KHUYENMAI.NgayKetThuc%TYPE;
BEGIN
    IF :NEW.MaKM IS NULL THEN
        RETURN;
    END IF;

    SELECT NgayBatDau, NgayKetThuc
    INTO v_ngay_bd, v_ngay_kt
    FROM KHUYENMAI
    WHERE MaKM = :NEW.MaKM;

    IF TRUNC(:NEW.NgHD) < TRUNC(v_ngay_bd)
    OR TRUNC(:NEW.NgHD) > TRUNC(v_ngay_kt) THEN

        RAISE_APPLICATION_ERROR(-20025, 'Ngày hóa đơn không nằm trong thời gian áp dụng khuyến mãi');
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(
            -20026,'Mã khuyến mãi không tồn tại');
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

--Cập nhật thông tin khách hàng 
CREATE OR REPLACE PROCEDURE UPDATE_KHACHHANG (
    PAR_MAKH   IN KHACHHANG.MaKhachHang%TYPE,
    PAR_HOTEN  IN KHACHHANG.HoTen%TYPE,
    PAR_SDT    IN KHACHHANG.SDT%TYPE
) AS
BEGIN

    -- [1] Validate dữ liệu đầu vào
    IF PAR_HOTEN IS NULL OR TRIM(PAR_HOTEN) = '' THEN
        RAISE_APPLICATION_ERROR(-20001, 'Ho ten khong duoc rong');
    END IF;

    IF PAR_SDT IS NULL OR LENGTH(TRIM(PAR_SDT)) < 9 THEN
        RAISE_APPLICATION_ERROR(-20002, 'So dien thoai khong hop le');
    END IF;

    -- [2] Cập nhật
    UPDATE KHACHHANG
    SET HoTen = PAR_HOTEN,
        SDT   = PAR_SDT
    WHERE MaKhachHang = PAR_MAKH;

    -- [3] Kiểm tra có update không
    IF SQL%ROWCOUNT = 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 'Khong tim thay khach hang: ' || PAR_MAKH);
    END IF;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20004, 'Loi cap nhat khach hang: ' || SQLERRM);
END;
/

--Xóa khách hàng 
CREATE OR REPLACE PROCEDURE DELETE_KHACHHANG (
    PAR_MAKH IN KHACHHANG.MaKhachHang%TYPE
) IS
    v_ma_chu_the KHACHHANG.MaChuThe%TYPE;
    v_count_hd   NUMBER;
BEGIN

    -- [1] Kiểm tra khách hàng có tồn tại và lấy MaChuThe
    SELECT MaChuThe
    INTO v_ma_chu_the
    FROM KHACHHANG
    WHERE MaKhachHang = PAR_MAKH;

    -- [2] Kiểm tra lịch sử hợp đồng
    SELECT COUNT(*)
    INTO v_count_hd
    FROM HOPDONG
    WHERE MaKhachHang = PAR_MAKH;

    IF v_count_hd > 0 THEN
        RAISE_APPLICATION_ERROR(
            -20015,
            'Khong the xoa: Khach hang da co ' || v_count_hd || ' hop dong trong he thong'
        );
    END IF;

    -- [3] Xóa khách hàng
    DELETE FROM KHACHHANG
    WHERE MaKhachHang = PAR_MAKH;

    -- [4] Xóa tài khoản liên quan
    DELETE FROM TAIKHOAN
    WHERE MaChuThe = v_ma_chu_the;

    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(
            -20016,
            'Khach hang khong ton tai: ' || PAR_MAKH
        );

    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(
            -20017,
            'Loi he thong khi xoa khach hang: ' || SQLERRM
        );
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

--Cập nhật hợp đồng
CREATE OR REPLACE PROCEDURE UPDATE_HOPDONG (
    PAR_MAHD      IN HOPDONG.MaHopDong%TYPE,
    PAR_TRANGTHAI IN HOPDONG.TrangThaiHopDong%TYPE
) AS
BEGIN
    -- [1] Kiểm tra trạng thái hợp lệ
    IF PAR_TRANGTHAI IS NULL THEN
        RAISE_APPLICATION_ERROR(-20025, 'Trạng thái hợp đồng không hợp lệ');
    END IF;

    -- [2] Cập nhật hợp đồng
    UPDATE HOPDONG
    SET TrangThaiHopDong = PAR_TRANGTHAI
    WHERE MaHopDong = PAR_MAHD;

    IF SQL%ROWCOUNT = 0 THEN
        RAISE_APPLICATION_ERROR(-20023, 'Không tìm thấy hợp đồng: ' || PAR_MAHD);
    END IF;

    -- [3] Nếu hủy hợp đồng → trả xe về trạng thái sẵn sàng
    IF PAR_TRANGTHAI = 'Da huy' THEN
        UPDATE XE
        SET TrangThai = 'San sang'
        WHERE BienSoXe IN (
            SELECT BienSoXe
            FROM CT_HOPDONG_XE
            WHERE MaHopDong = PAR_MAHD
        );
    END IF;
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20024, 'Lỗi cập nhật hợp đồng: ' || SQLERRM);
END;
/

--Xóa hợp đồng
CREATE OR REPLACE PROCEDURE DELETE_HOPDONG (
    PAR_MAHD IN HOPDONG.MaHopDong%TYPE
) AS
    v_trang_thai_hd HOPDONG.TrangThaiHopDong%TYPE;
    v_count_hd      NUMBER;
    v_count_bb      NUMBER;
BEGIN

    -- [1] Kiểm tra tồn tại hợp đồng
    SELECT COUNT(*)
    INTO v_count_hd
    FROM HOPDONG
    WHERE MaHopDong = PAR_MAHD;

    IF v_count_hd = 0 THEN
        RAISE_APPLICATION_ERROR(-20025, 'Hợp đồng không tồn tại: ' || PAR_MAHD);
    END IF;

    -- Lấy trạng thái hợp đồng
    SELECT TrangThaiHopDong
    INTO v_trang_thai_hd
    FROM HOPDONG
    WHERE MaHopDong = PAR_MAHD;

    -- [2] Kiểm tra biên bản / giao dịch
    SELECT COUNT(*)
    INTO v_count_bb
    FROM BIENBAN
    WHERE MaHopDong = PAR_MAHD;

    IF v_count_bb > 0 THEN
        RAISE_APPLICATION_ERROR(
            -20026,
            'Không thể xóa hợp đồng vì đã phát sinh biên bản/giao dịch'
        );
    END IF;

    -- [3] Không cho xóa hợp đồng đã hoàn thành
    IF v_trang_thai_hd = 'Da hoan thanh' THEN
        RAISE_APPLICATION_ERROR(
            -20027,
            'Không thể xóa hợp đồng đã hoàn thành'
        );
    END IF;

    -- [4] Giải phóng xe
    UPDATE XE
    SET TrangThai = 'San sang'
    WHERE BienSoXe IN (
        SELECT BienSoXe
        FROM CT_HOPDONG_XE
        WHERE MaHopDong = PAR_MAHD
    );

    -- [5] Xóa chi tiết hợp đồng
    DELETE FROM CT_HOPDONG_XE
    WHERE MaHopDong = PAR_MAHD;

    -- [6] Xóa hợp đồng
    DELETE FROM HOPDONG
    WHERE MaHopDong = PAR_MAHD;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20029, 'Lỗi hệ thống: ' || SQLERRM);
END;
/

--Thêm xe vào hợp đồng 
CREATE OR REPLACE PROCEDURE INSERT_CT_HOPDONG (
    PAR_MAHD   IN CT_HOPDONG_XE.MaHopDong%TYPE,
    PAR_BIENSO IN CT_HOPDONG_XE.BienSoXe%TYPE
) IS
    v_count_hd   NUMBER;
    v_trang_thai XE.TrangThai%TYPE;
BEGIN

    -- [1] Kiểm tra hợp đồng tồn tại
    SELECT COUNT(*)
    INTO v_count_hd
    FROM HOPDONG
    WHERE MaHopDong = PAR_MAHD;

    IF v_count_hd = 0 THEN
        RAISE_APPLICATION_ERROR(-20030, 'Hợp đồng không tồn tại: ' || PAR_MAHD);
    END IF;

    -- [2] Lấy trạng thái xe (đồng thời kiểm tra tồn tại)
    BEGIN
        SELECT TrangThai
        INTO v_trang_thai
        FROM XE
        WHERE BienSoXe = PAR_BIENSO;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20031,
                'Xe không tồn tại: ' || PAR_BIENSO);
    END;

    -- [3] Kiểm tra xe có sẵn sàng không
    IF v_trang_thai <> 'San sang' THEN
        RAISE_APPLICATION_ERROR(-20032, 'Xe hiện không sẵn sàng để thuê: ' || PAR_BIENSO);
    END IF;

    -- [4] Kiểm tra xe đã có trong hợp đồng chưa
    SELECT COUNT(*)
    INTO v_count_hd
    FROM CT_HOPDONG_XE
    WHERE MaHopDong = PAR_MAHD
      AND BienSoXe = PAR_BIENSO;

    IF v_count_hd > 0 THEN
        RAISE_APPLICATION_ERROR(-20033, 'Xe đã tồn tại trong hợp đồng');
    END IF;

    -- [5] Thêm xe vào hợp đồng
    INSERT INTO CT_HOPDONG_XE (MaHopDong, BienSoXe)
    VALUES (PAR_MAHD, PAR_BIENSO);

    -- [6] Cập nhật trạng thái xe
    UPDATE XE
    SET TrangThai = 'Dang thue'
    WHERE BienSoXe = PAR_BIENSO;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20034, 'Lỗi hệ thống khi thêm xe vào hợp đồng: ' || SQLERRM);
END;
/

--Lập biên bản 
CREATE OR REPLACE PROCEDURE INSERT_BIENBAN (
    PAR_MAHD   IN BIENBAN.MAHOPDONG%TYPE,
    PAR_MANV   IN BIENBAN.MANHANVIEN%TYPE,
    MABB_OUT   OUT BIENBAN.MABIENBAN%TYPE
) IS
    v_trang_thai_hd HOPDONG.TrangThaiHopDong%TYPE;
    v_count NUMBER;
BEGIN

    -- [1] Lấy trạng thái hợp đồng
    BEGIN
        SELECT TrangThaiHopDong
        INTO v_trang_thai_hd
        FROM HOPDONG
        WHERE MaHopDong = PAR_MAHD;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20040,
                'Hợp đồng không tồn tại: ' || PAR_MAHD);
    END;

    -- [2] Chặn hợp đồng không hợp lệ
    IF v_trang_thai_hd IN ('Da huy', 'Da hoan thanh') THEN
        RAISE_APPLICATION_ERROR(-20043,
            'Hợp đồng không hợp lệ để lập biên bản');
    END IF;

    -- [3] Kiểm tra nhân viên
    SELECT COUNT(*)
    INTO v_count
    FROM NHANVIEN
    WHERE MaNhanVien = PAR_MANV;

    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20041,
            'Nhân viên không tồn tại: ' || PAR_MANV);
    END IF;

    -- [4] Insert biên bản (ĐÚNG CỘT CỦA BẠN)
    INSERT INTO BIENBAN (
        NGAYKYNHAN,
        TINHTRANGBANGIAO,
        MAHOPDONG,
        MANHANVIEN
    )
    VALUES (
        SYSDATE,
        'Da ban giao',
        PAR_MAHD,
        PAR_MANV
    )
    RETURNING MABIENBAN INTO MABB_OUT;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20042,
            'Lỗi hệ thống khi tạo biên bản: ' || SQLERRM);
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
    INSERT INTO HOADON (NgHD, TienThueGoc, PhiPhatSinh, SoTienGiam, TrangThaiHD, TongTien, MaBB, MaKM)
    VALUES (SYSDATE, v_tien_thue_goc, 0, v_so_tien_giam, 'Chua thanh toan', v_tong_tien, i_ma_bb, i_ma_km)
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

--Thanh toán 
CREATE OR REPLACE PROCEDURE INSERT_THANHTOAN (
    PAR_MAHD    IN HOADON.MaHD%TYPE,
    PAR_SOTIEN  IN THANHTOAN.SoTien%TYPE,
    MATT_OUT    OUT THANHTOAN.MaTT%TYPE
) AS
    v_tong_tien   HOADON.TongTien%TYPE;
    v_trang_thai  HOADON.TrangThaiHD%TYPE;
    v_da_tt       NUMBER;
BEGIN

    -- [1] Kiểm tra tồn tại hóa đơn + lấy thông tin
    BEGIN
        SELECT TongTien, TrangThaiHD
        INTO v_tong_tien, v_trang_thai
        FROM HOADON
        WHERE MaHD = PAR_MAHD;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            RAISE_APPLICATION_ERROR(-20050, 'Hóa đơn không tồn tại: ' || PAR_MAHD);
    END;

    -- [2] Kiểm tra trạng thái hóa đơn
    IF v_trang_thai = 'Da thanh toan' THEN
        RAISE_APPLICATION_ERROR(-20051, 'Hóa đơn đã được thanh toán');
    ELSIF v_trang_thai = 'Da huy' THEN
        RAISE_APPLICATION_ERROR(-20052, 'Hóa đơn đã bị hủy');
    END IF;

    -- [3] Kiểm tra số tiền hợp lệ
    IF PAR_SOTIEN IS NULL OR PAR_SOTIEN <= 0 THEN
        RAISE_APPLICATION_ERROR(-20053, 'Số tiền thanh toán không hợp lệ');
    END IF;

    -- [4] Kiểm tra đã có giao dịch chưa
    SELECT COUNT(*)
    INTO v_da_tt
    FROM THANHTOAN
    WHERE MaHD = PAR_MAHD;

    IF v_da_tt > 0 THEN
        RAISE_APPLICATION_ERROR(-20054, 'Hóa đơn đã có giao dịch thanh toán');
    END IF;

    -- [5] Kiểm tra đủ tiền
    IF PAR_SOTIEN < v_tong_tien THEN
        RAISE_APPLICATION_ERROR(-20055, 'Không đủ tiền thanh toán');
    END IF;

    -- [6] Thêm thanh toán
    INSERT INTO THANHTOAN (MaHD, SoTien, NgayTT)
    VALUES (PAR_MAHD, PAR_SOTIEN, SYSDATE)
    RETURNING MaTT INTO MATT_OUT;

    -- [7] Cập nhật hóa đơn
    UPDATE HOADON
    SET TrangThaiHD = 'Da thanh toan'
    WHERE MaHD = PAR_MAHD;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20056, 'Lỗi thanh toán: ' || SQLERRM);
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
CREATE OR REPLACE PROCEDURE DELETE_XE (
    PAR_BIENSO IN XE.BienSoXe%TYPE
) AS
    v_count_xe  NUMBER;
    v_count_cthd NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count_xe
    FROM XE
    WHERE BienSoXe = PAR_BIENSO;

    IF v_count_xe = 0 THEN
        RAISE_APPLICATION_ERROR(-20020, 'Lỗi thao tác: Không tìm thấy chiếc xe có biển số ' || PAR_BIENSO || ' trong hệ thống!');
    END IF;

    SELECT COUNT(*) INTO v_count_cthd
    FROM CT_HOPDONG_XE
    WHERE BienSoXe = PAR_BIENSO;

    IF v_count_cthd > 0 THEN
        RAISE_APPLICATION_ERROR(-20021, 
            'Lỗi nghiệp vụ: Không thể xóa xe này vì biển số ' || PAR_BIENSO || 
            ' đã nằm trong lịch sử của ' || v_count_cthd || ' hợp đồng thuê xe!');
    END IF;

    DELETE FROM XE 
    WHERE BienSoXe = PAR_BIENSO;
    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20022, 'Lỗi hệ thống khi xóa xe: ' || SQLERRM);
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

--Thêm khuyến mãi 
CREATE OR REPLACE PROCEDURE INSERT_KHUYENMAI (
    PAR_TENKM   IN KHUYENMAI.TENCHUONGTRINH%TYPE,
    PAR_GIAM    IN KHUYENMAI.PhanTramGiam%TYPE,
    MAKM_OUT    OUT KHUYENMAI.MaKM%TYPE
) AS
    v_count NUMBER;
BEGIN

    -- [1] Validate dữ liệu
    IF PAR_TENKM IS NULL OR TRIM(PAR_TENKM) = '' THEN
        RAISE_APPLICATION_ERROR(-20001, 'Ten khuyen mai khong duoc rong');
    END IF;

    IF PAR_GIAM IS NULL OR PAR_GIAM <= 0 OR PAR_GIAM > 100 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Phan tram giam khong hop le (1-100)');
    END IF;

    -- [2] Kiểm tra trùng tên khuyến mãi
    SELECT COUNT(*)
    INTO v_count
    FROM KHUYENMAI
    WHERE UPPER(TENCHUONGTRINH) = UPPER(PAR_TENKM);

    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20003,
            'Khuyen mai da ton tai: ' || PAR_TENKM);
    END IF;

    -- [3] Thêm khuyến mãi
    INSERT INTO KHUYENMAI (
        TENCHUONGTRINH,
        PhanTramGiam
    )
    VALUES (
        PAR_TENKM,
        PAR_GIAM
    )
    RETURNING MaKM INTO MAKM_OUT;

    COMMIT;

EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20004, 'Trung khoa chinh khuyen mai');

    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20005,
            'Loi he thong khi them khuyen mai: ' || SQLERRM);
END;
/

--Cập nhật khuyến mãi 
CREATE OR REPLACE PROCEDURE UPDATE_KHUYENMAI (
    PAR_MAKM   IN KHUYENMAI.MaKM%TYPE,
    PAR_GIAM   IN KHUYENMAI.PhanTramGiam%TYPE
) AS
BEGIN

    -- [1] Validate dữ liệu đầu vào
    IF PAR_GIAM IS NULL OR PAR_GIAM <= 0 OR PAR_GIAM > 100 THEN
        RAISE_APPLICATION_ERROR(-20001, 'Phan tram giam khong hop le (1-100)');
    END IF;

    -- [2] Cập nhật khuyến mãi
    UPDATE KHUYENMAI
    SET PhanTramGiam = PAR_GIAM
    WHERE MaKM = PAR_MAKM;

    -- [3] Kiểm tra có update không
    IF SQL%ROWCOUNT = 0 THEN
        RAISE_APPLICATION_ERROR(-20002, 'Khong tim thay khuyen mai: ' || PAR_MAKM);
    END IF;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20003, 'Loi cap nhat khuyen mai: ' || SQLERRM);
END;
/

--Xóa khuyến mãi
CREATE OR REPLACE PROCEDURE DELETE_KHUYENMAI (
    PAR_MAKM IN KHUYENMAI.MaKM%TYPE
) AS
    v_count_hoadon NUMBER;
BEGIN

    -- [0] Validate input
    IF PAR_MAKM IS NULL THEN
        RAISE_APPLICATION_ERROR(-20024, 'Ma khuyen mai khong duoc rong');
    END IF;

    -- [1] Kiểm tra đã dùng trong hóa đơn chưa
    SELECT COUNT(*)
    INTO v_count_hoadon
    FROM HOADON
    WHERE MaKM = PAR_MAKM;

    IF v_count_hoadon > 0 THEN
        RAISE_APPLICATION_ERROR(-20021, 'Khong the xoa: Khuyen mai da duoc su dung trong hoa don');
    END IF;

    -- [2] Xóa khuyến mãi
    DELETE FROM KHUYENMAI
    WHERE MaKM = PAR_MAKM;

    -- [3] Kiểm tra kết quả xóa
    IF SQL%ROWCOUNT = 0 THEN
        RAISE_APPLICATION_ERROR(-20020, 'Khong tim thay khuyen mai: ' || PAR_MAKM);
    END IF;

    COMMIT;

EXCEPTION
    WHEN OTHERS THEN
        ROLLBACK;
        RAISE_APPLICATION_ERROR(-20023, 'Loi he thong khi xoa khuyen mai: ' || SQLERRM);
END;
/

--Tra cứu hợp đồng 
CREATE OR REPLACE PROCEDURE TRA_CUU_HOPDONG (
    PAR_MAHD     IN HOPDONG.MaHopDong%TYPE,
    RES_CURSOR   OUT SYS_REFCURSOR
) AS
    v_count NUMBER;
BEGIN

    -- [1] Validate input
    IF PAR_MAHD IS NULL THEN
        RAISE_APPLICATION_ERROR(-20080, 'Ma hop dong khong duoc rong');
    END IF;

    -- [2] Kiểm tra hợp đồng tồn tại
    SELECT COUNT(*)
    INTO v_count
    FROM HOPDONG
    WHERE MaHopDong = PAR_MAHD;

    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20081, 'Khong tim thay hop dong: ' || PAR_MAHD);
    END IF;

    -- [3] Trả thông tin hợp đồng + khách hàng + xe
    OPEN RES_CURSOR FOR
        SELECT 
            hd.MaHopDong,
            hd.ThoiGianLap,
            hd.TrangThaiHopDong,
            kh.MaKhachHang,
            kh.HoTen,
            kh.SDT,
            x.BienSoXe,
            x.TenXe,
            lx.TenLoai
        FROM HOPDONG hd
        JOIN KHACHHANG kh ON hd.MaKhachHang = kh.MaKhachHang
        LEFT JOIN CT_HOPDONG_XE ct ON hd.MaHopDong = ct.MaHopDong
        LEFT JOIN XE x ON ct.BienSoXe = x.BienSoXe
        LEFT JOIN LOAIXE lx ON x.MaLoaiXe = lx.MaLoaiXe
        WHERE hd.MaHopDong = PAR_MAHD;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20082, 'Loi he thong khi tra cuu hop dong: ' || SQLERRM);
END;
/

--Tra cứu khách hàng 
CREATE OR REPLACE PROCEDURE TRA_CUU_KHACHHANG (
    PAR_CCCD     IN KHACHHANG.CCCD%TYPE,
    RES_CURSOR   OUT SYS_REFCURSOR
) AS
    v_count NUMBER;
BEGIN

    -- [1] Validate input
    IF PAR_CCCD IS NULL THEN
        RAISE_APPLICATION_ERROR(-20090,
            'CCCD khong duoc rong');
    END IF;

    -- [2] Kiểm tra khách hàng tồn tại
    SELECT COUNT(*)
    INTO v_count
    FROM KHACHHANG
    WHERE CCCD = PAR_CCCD;

    IF v_count = 0 THEN
        RAISE_APPLICATION_ERROR(-20091,
            'Khong tim thay khach hang: ' || PAR_CCCD);
    END IF;

    -- [3] Trả thông tin khách hàng + hợp đồng + xe
    OPEN RES_CURSOR FOR
        SELECT 
            kh.MaKhachHang,
            kh.HoTen,
            kh.SDT,
            kh.CCCD,

            hd.MaHopDong,
            hd.ThoiGianLap,
            hd.TrangThaiHopDong,

            x.BienSoXe,
            x.TenXe,
            lx.TenLoai

        FROM KHACHHANG kh
        LEFT JOIN HOPDONG hd ON kh.MaKhachHang = hd.MaKhachHang
        LEFT JOIN CT_HOPDONG_XE ct ON hd.MaHopDong = ct.MaHopDong
        LEFT JOIN XE x ON ct.BienSoXe = x.BienSoXe
        LEFT JOIN LOAIXE lx ON x.MaLoaiXe = lx.MaLoaiXe
        WHERE kh.CCCD = PAR_CCCD;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20092,
            'Loi he thong khi tra cuu khach hang: ' || SQLERRM);
END;
/

--Tính doanh thu 
CREATE OR REPLACE FUNCTION TINH_DOANH_THU (
    PAR_THANG IN NUMBER,
    PAR_NAM   IN NUMBER
)
RETURN NUMBER
AS
    v_doanh_thu   NUMBER := 0;
    v_ngay_dau_thang DATE;
    v_ngay_dau_thang_sau DATE;
BEGIN

    -- [1] Validate dữ liệu đầu vào (Nhất quán dùng RAISE_APPLICATION_ERROR như các bài trước)
    IF PAR_THANG IS NULL OR PAR_THANG < 1 OR PAR_THANG > 12 THEN
        RAISE_APPLICATION_ERROR(-20100, 'Lỗi dữ liệu: Tháng không hợp lệ (Phải từ 1-12)');
    END IF;

    IF PAR_NAM IS NULL OR PAR_NAM < 2000 THEN
        RAISE_APPLICATION_ERROR(-20101, 'Lỗi dữ liệu: Năm không hợp lệ (Phải từ năm 2000 trở đi)');
    END IF;

    -- [2] Kỹ thuật tối ưu hóa: Tính toán biên ngày tháng trước
    v_ngay_dau_thang := TO_DATE('01-' || PAR_THANG || '-' || PAR_NAM, 'DD-MM-YYYY');
        v_ngay_dau_thang_sau := ADD_MONTHS(v_ngay_dau_thang, 1);

    -- [3] Tính doanh thu bằng khoảng giá trị S_DATE <= NgayLap < E_DATE
    SELECT NVL(SUM(TongTien), 0)
    INTO v_doanh_thu
    FROM HOADON
    WHERE TrangThaiHD = 'Da thanh toan'
      AND NgHD >= v_ngay_dau_thang
      AND NgHD < v_ngay_dau_thang_sau; 

    -- [4] Trả kết quả
    RETURN v_doanh_thu;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20102, 'Lỗi hệ thống khi tính doanh thu: ' || SQLERRM);
END;
/

--Tính lợi nhuận 
CREATE OR REPLACE FUNCTION TINH_LOI_NHUAN (
    PAR_THANG IN NUMBER,
    PAR_NAM   IN NUMBER
)
RETURN NUMBER
AS
    v_tong_doanh_thu     NUMBER := 0;
    v_fee       NUMBER := 0;

    v_ngay_dau_thang     DATE;
    v_ngay_dau_thang_sau DATE;

    c_TY_LE_HOA_HONG CONSTANT NUMBER := 0.30;
BEGIN

    -- [1] Validate
    IF PAR_THANG IS NULL OR PAR_THANG < 1 OR PAR_THANG > 12 THEN
        RAISE_APPLICATION_ERROR(-20110, 'Tháng không hợp lệ (1-12)');
    END IF;

    IF PAR_NAM IS NULL OR PAR_NAM < 2000 THEN
        RAISE_APPLICATION_ERROR(-20111, 'Năm không hợp lệ');
    END IF;

    -- [2] Biên thời gian
    v_ngay_dau_thang := TO_DATE('01-' || PAR_THANG || '-' || PAR_NAM, 'DD-MM-YYYY');
    v_ngay_dau_thang_sau := ADD_MONTHS(v_ngay_dau_thang, 1);

    -- [3] Tổng doanh thu
    SELECT NVL(SUM(TongTien), 0)
    INTO v_tong_doanh_thu
    FROM HOADON
    WHERE TrangThaiHD = 'Da thanh toan'
      AND NgHD >= v_ngay_dau_thang
      AND NgHD < v_ngay_dau_thang_sau;

    -- [4] Hoa hồng sàn
    v_fee := v_tong_doanh_thu * c_TY_LE_HOA_HONG;

    RETURN v_fee;

EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20112, 'Lỗi hệ thống khi tính lợi nhuận: ' || SQLERRM);
END;
/

--Tính số ngày thuê 
CREATE OR REPLACE FUNCTION TINH_SO_NGAY_THUE (
    ngay_nhan IN DATE,
    ngay_tra  IN DATE
)
RETURN NUMBER
AS
    v_so_ngay NUMBER;
BEGIN
    IF ngay_tra < ngay_nhan THEN
        DBMS_OUTPUT.PUT_LINE('Thời gian không hợp lệ');
        RETURN 0;
    END IF;

    v_so_ngay := ngay_tra - ngay_nhan;

    IF v_so_ngay = 0 THEN
        v_so_ngay := 1;
    END IF;

    RETURN v_so_ngay;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Lỗi: ' || SQLERRM);
        RETURN 0;
END;
/

--Tính tiền thuê
CREATE OR REPLACE FUNCTION TINH_TIEN_THUE (
    ngay_nhan      IN DATE,
    ngay_tra       IN DATE,
    gia_thue_ngay  IN NUMBER
)
RETURN NUMBER
AS
    v_so_ngay   NUMBER;
    v_tong_tien NUMBER;
BEGIN
    v_so_ngay := TINH_SO_NGAY_THUE(ngay_nhan, ngay_tra);

    IF gia_thue_ngay IS NULL OR gia_thue_ngay <= 0 THEN
        DBMS_OUTPUT.PUT_LINE('Đơn giá thuê không hợp lệ');
        RETURN 0;
    END IF;

    v_tong_tien := v_so_ngay * gia_thue_ngay;

    RETURN v_tong_tien;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Lỗi: ' || SQLERRM);
        RETURN 0;
END;
/

--Kiểm tra trạng thái xe
CREATE OR REPLACE FUNCTION KIEM_TRA_XE_TRONG (
    PAR_BIENSO IN VARCHAR2
) 
RETURN VARCHAR2 
AS
    v_trang_thai XE.TrangThai%TYPE;
BEGIN

    SELECT TrangThai
    INTO v_trang_thai
    FROM XE
    WHERE BienSoXe = PAR_BIENSO;

    IF v_trang_thai = 'San sang' THEN
        RETURN 'TRONG';
    ELSIF v_trang_thai = 'Dang thue' THEN
        RETURN 'DANG_THUE';
    ELSE
        RETURN 'KHAC';
    END IF;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'KHONG_TON_TAI';
    WHEN OTHERS THEN
        RETURN 'LOI';
END;
/

COMMIT;

UPDATE TAIKHOAN
SET MatKhau = '123'
WHERE TenDangNhap = 'admin';

UPDATE TAIKHOAN
SET MatKhau = '123'
WHERE TenDangNhap = 'nv01';

COMMIT;

CREATE SEQUENCE SEQ_KHACHHANG START WITH 1 INCREMENT BY 1;

-- 1. Xóa toàn bộ dữ liệu đang có trong bảng khách hàng
DELETE FROM KHACHHANG;

-- 2. Reset số tự tăng của Sequence về lại từ đầu (bắt đầu từ 1) để mã KH đẹp từ số 1
ALTER SEQUENCE SEQ_KHACHHANG RESTART START WITH 1;

-- 3. Lưu thay đổi dọn dẹp
COMMIT;

INSERT INTO KHACHHANG (MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe)
VALUES (SEQ_KHACHHANG.NEXTVAL, 'Nguyễn Văn Anh', '012345678901', 'GMC123456', '0901234567', 'Nam', TO_DATE('15/05/1990', 'DD/MM/YYYY'), '123 Nguyễn Trãi, Quận 5, TP.Hồ Chí Minh', NULL);

INSERT INTO KHACHHANG (MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe)
VALUES (SEQ_KHACHHANG.NEXTVAL, 'Trần Thị Bích', '023456789012', 'GMC789012', '0912345678', 'Nữ', TO_DATE('20/10/1995', 'DD/MM/YYYY'), '456 Lê Lợi, Quận 1, TP.Hồ Chí Minh', NULL);

INSERT INTO KHACHHANG (MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe)
VALUES (SEQ_KHACHHANG.NEXTVAL, 'Phạm Minh Cường', '034567890123', 'GMC345678', '0987654321', 'Nam', TO_DATE('02/09/1988', 'DD/MM/YYYY'), '789 Điện Biên Phủ, Quận Bình Thạnh, TP.Hồ Chí Minh', NULL);

INSERT INTO KHACHHANG (MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe)
VALUES (SEQ_KHACHHANG.NEXTVAL, 'Lê Hoàng Diệu', '045678901234', 'GMC901234', '0933445566', 'Nữ', TO_DATE('12/12/1992', 'DD/MM/YYYY'), '23 Nguyễn Huệ, Quận 1, TP.Hồ Chí Minh', NULL);

INSERT INTO KHACHHANG (MaKhachHang, HoTen, CCCD, SoBangLai, SDT, GioiTinh, NgaySinh, DiaChi, MaChuThe)
VALUES (SEQ_KHACHHANG.NEXTVAL, 'Hoàng Xuân Vinh', '056789012345', 'GMC567890', '0944556677', 'Nam', TO_DATE('30/04/1985', 'DD/MM/YYYY'), '88 Cách Mạng Tháng 8, Quận 3, TP.Hồ Chí Minh', NULL);

COMMIT;

SELECT * FROM KHACHHANG;

CREATE SEQUENCE SEQ_NHANVIEN START WITH 1 INCREMENT BY 1;
-- Nhân viên 1: Quản lý chi nhánh
INSERT INTO NHANVIEN (MaNhanVien, HoTenNV, CCCD, SDT, Email, MaChuThe, MaDoanhNghiep)
VALUES (
    SEQ_NHANVIEN.NEXTVAL, 
    'Lê Minh Hoàng', 
    '012203004567', 
    '0909123456', 
    'hoang.le@thuexe.com', 
    NULL, 
    NULL
);

-- Nhân viên 2: Kế toán trưởng
INSERT INTO NHANVIEN (MaNhanVien, HoTenNV, CCCD, SDT, Email, MaChuThe, MaDoanhNghiep)
VALUES (
    SEQ_NHANVIEN.NEXTVAL, 
    'Phạm Thị Tuyết Mai', 
    '024205001234', 
    '0918234567', 
    'mai.pham@thuexe.com', 
    NULL, 
    NULL
);

-- Nhân viên 3: Nhân viên điều hành xe
INSERT INTO NHANVIEN (MaNhanVien, HoTenNV, CCCD, SDT, Email, MaChuThe, MaDoanhNghiep)
VALUES (
    SEQ_NHANVIEN.NEXTVAL, 
    'Trần Vũ Lâm', 
    '036201009876', 
    '0982345678', 
    'lam.tran@thuexe.com', 
    NULL, 
    NULL
);

-- Nhân viên 4: Nhân viên chăm sóc khách hàng
INSERT INTO NHANVIEN (MaNhanVien, HoTenNV, CCCD, SDT, Email, MaChuThe, MaDoanhNghiep)
VALUES (
    SEQ_NHANVIEN.NEXTVAL, 
    'Nguyễn Hoàng Yến', 
    '048204005555', 
    '0933456789', 
    'yen.nguyen@thuexe.com', 
    NULL, 
    NULL
);

-- Nhân viên 5: Nhân viên kỹ thuật/bảo dưỡng xe
INSERT INTO NHANVIEN (MaNhanVien, HoTenNV, CCCD, SDT, Email, MaChuThe, MaDoanhNghiep)
VALUES (
    SEQ_NHANVIEN.NEXTVAL, 
    'Đỗ Cao Cường', 
    '060202008888', 
    '0944567890', 
    'cuong.do@thuexe.com', 
    NULL, 
    NULL
);

SELECT * FROM NHANVIEN;

-- Xác nhận lưu dữ liệu vĩnh viễn vào hệ thống
COMMIT;

-- 1. Tạo tài khoản đăng nhập cho khách hàng Nguyễn Văn Anh
-- Cấp MaChuThe tiếp theo là số 3 (vì số 1 và 2 đã cấp cho admin và nv01)
INSERT INTO TAIKHOAN (MaChuThe, TenDangNhap, MatKhau, VaiTro, TrangThai) 
VALUES (3, 'khachhang01', '123', 'KhachHang', 'Hoat dong');

-- 2. Cập nhật (Nối) tài khoản số 3 này vào hồ sơ của Nguyễn Văn Anh 
UPDATE KHACHHANG 
SET MaChuThe = 3 
WHERE CCCD = '012345678901';

-- 3. Lưu lại thay đổi
COMMIT;

SELECT MaHD, NgHD, MaBB, TongTien FROM HOADON;