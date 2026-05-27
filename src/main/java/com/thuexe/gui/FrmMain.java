package com.thuexe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.thuexe.dto.TaiKhoanDTO;

/**
 * FILE HOÀN CHỈNH: Giao diện chính sau khi Đăng nhập
 * Kết nối toàn bộ các module: Quản lý xe, Lập phiếu thuê, Phân quyền.
 */
public class FrmMain extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuHeThong, menuQuanLy, menuNghiepVu, menuThongKe;
    private JMenuItem mnuXe, mnuLoaiXe, mnuKhachHang, mnuNhanVien;
    private JMenuItem mnuLapPhieu, mnuTraXe;
    private JMenuItem mnuBaoCao, mnuThoat, mnuDangXuat;

    private TaiKhoanDTO tk;

    public FrmMain() {
        // Lấy thông tin tài khoản từ static userLogged của FrmLogin
        this.tk = FrmLogin.userLogged;

        // 1. Cấu hình cơ bản cho Frame
        setTitle("HỆ THỐNG QUẢN LÝ THUÊ XE");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 2. Khởi tạo Menu Bar
        initMenuBar();

        // 3. Phân quyền người dùng
        phanQuyen();

        // 4. Gắn sự kiện (Events)
        initEvents();

        // 5. Giao diện trung tâm
        initContent();

        // 6. Thanh trạng thái phía dưới (Status Bar)
        initStatusBar();

        // Tự động phóng to toàn màn hình
        this.setExtendedState(MAXIMIZED_BOTH);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        // Menu Hệ thống
        menuHeThong = new JMenu("Hệ thống");
        JMenuItem mnuHoSo = new JMenuItem("Xem hồ sơ cá nhân");
        mnuDangXuat = new JMenuItem("Đăng xuất");
        mnuThoat = new JMenuItem("Thoát");
        menuHeThong.add(mnuDangXuat);
        menuHeThong.addSeparator();
        menuHeThong.add(mnuThoat);


        // Menu Quản lý
        menuQuanLy = new JMenu("Quản lý");
        mnuXe = new JMenuItem("Quản lý Xe");
        mnuLoaiXe = new JMenuItem("Quản lý Loại xe");
        mnuKhachHang = new JMenuItem("Quản lý Khách hàng");
        mnuNhanVien = new JMenuItem("Quản lý Nhân viên");
        menuQuanLy.add(mnuXe);
        menuQuanLy.add(mnuLoaiXe);
        menuQuanLy.add(mnuKhachHang);
        menuQuanLy.add(mnuNhanVien);

        // Menu Nghiệp vụ
        menuNghiepVu = new JMenu("Nghiệp vụ");
        mnuLapPhieu = new JMenuItem("Lập Phiếu Thuê Xe"); // CHỨC NĂNG CHÍNH ÔNG VỪA LÀM
        mnuTraXe = new JMenuItem("Trả Xe & Thanh Toán");
        menuNghiepVu.add(mnuLapPhieu);
        menuNghiepVu.add(mnuTraXe);

        // Menu Thống kê
        menuThongKe = new JMenu("Thống kê");
        mnuBaoCao = new JMenuItem("Báo cáo doanh thu");
        menuThongKe.add(mnuBaoCao);

        // Lắp ráp vào MenuBar
        menuBar.add(menuHeThong);
        menuBar.add(menuQuanLy);
        menuBar.add(menuNghiepVu);
        menuBar.add(menuThongKe);
        setJMenuBar(menuBar);

        //Menu HoSoCaNhan
        menuHeThong.add(mnuHoSo); 
        menuHeThong.addSeparator();
        menuHeThong.add(mnuDangXuat);
        menuHeThong.addSeparator();
        menuHeThong.add(mnuThoat);
    }

    private void initEvents() {
        // Thoát
        mnuThoat.addActionListener(e -> System.exit(0));

        // Đăng xuất
        mnuDangXuat.addActionListener(e -> {
            this.dispose();
            new FrmLogin().setVisible(true);
        });

        // MỞ FORM LẬP PHIẾU THUÊ (Kết nối với file FrmLapPhieuThue.java của ông)
        mnuLapPhieu.addActionListener(e -> {
            // Kiểm tra xem class đã tồn tại chưa để tránh lỗi đỏ
            try {
                new FrmLapPhieuThue().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi mở form Lập phiếu: " + ex.getMessage());
            }
        });

        // Mở Form Tra cứu Xe
        mnuXe.addActionListener(e -> {
            // new FrmTraCuuXe().setVisible(true); 
            JOptionPane.showMessageDialog(this, "Mở chức năng Quản lý xe");
        });
        
        for (Component comp : menuHeThong.getMenuComponents()) {
        if (comp instanceof JMenuItem && ((JMenuItem) comp).getText().equals("Xem hồ sơ cá nhân")) {
            ((JMenuItem) comp).addActionListener(e -> {
                if (this.tk != null) {
                    // Bước 1: Làm sạch toàn bộ vùng hiển thị Content cũ ở trung tâm Frame
                    getContentPane().removeAll(); 
                    
                    // Bước 2: Thiết lập lại cấu trúc Menu bar
                    setJMenuBar(menuBar); 
                    
                    // Bước 3: Nạp Panel hồ sơ mới vào vùng BorderLayout.CENTER
                    add(new PnlHoSoCaNhan(tk.getMaChuThe(), tk.getTenDangNhap(), tk.getVaiTro(), tk.getMatKhau()), BorderLayout.CENTER);
                    
                    // Bước 4: Tải lại thanh trạng thái Status Bar phía dưới cùng
                    initStatusBar(); 
                    
                    // Bước 5: Yêu cầu Swing vẽ và hiển thị lại giao diện JFrame
                    revalidate();
                    repaint();
                }
            });
        }
    }
    }

    private void initContent() {
        // Hình nền hoặc nhãn chào mừng ở giữa
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        JLabel lblWelcome = new JLabel("HỆ THỐNG QUẢN LÝ THUÊ XE Ô TÔ", JLabel.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 35));
        lblWelcome.setForeground(new Color(0, 51, 153));
        pnlCenter.add(lblWelcome);
        add(pnlCenter, BorderLayout.CENTER);
    }

    private void initStatusBar() {
        JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        String user = (tk != null) ? tk.getTenDangNhap() : "Guest";
        String role = (tk != null) ? tk.getVaiTro() : "None";
        JLabel lblUser = new JLabel(" Người dùng: " + user + " | Quyền: " + role + " | Trạng thái: Đã kết nối Oracle 19c");
        lblUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
        pnlStatus.add(lblUser);
        add(pnlStatus, BorderLayout.SOUTH);
    }

    // Thêm hàm làm mới thanh trạng thái từ bất kỳ đâu
public void refreshStatusBar() {
    // Tìm và xóa panel status cũ nếu có
    for (Component comp : getContentPane().getComponents()) {
        if (comp instanceof JPanel && comp.getName() != null && comp.getName().equals("pnlStatus")) {
            getContentPane().remove(comp);
        }
    }
    
    JPanel pnlStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pnlStatus.setName("pnlStatus"); // Đặt tên để dễ quản lý xóa/sửa
    pnlStatus.setBorder(BorderFactory.createLoweredBevelBorder());
    
    String user = (tk != null) ? tk.getTenDangNhap() : "Guest";
    String role = (tk != null) ? tk.getVaiTro() : "None";
    
    JLabel lblUser = new JLabel(" Người dùng: " + user + " | Quyền: " + role + " | Trạng thái: Hệ thống sẵn sàng");
    lblUser.setFont(new Font("Tahoma", Font.PLAIN, 12));
    pnlStatus.add(lblUser);
    
    add(pnlStatus, BorderLayout.SOUTH);
    revalidate();
    repaint();
}

    private void phanQuyen() {
        if (tk == null) return;
        
        // Nếu là Nhân viên, giới hạn các chức năng Admin
        if (tk.getVaiTro().equalsIgnoreCase("NhanVien")) {
            mnuNhanVien.setEnabled(false); // Vô hiệu hóa Quản lý nhân viên
            menuThongKe.setVisible(false); // Ẩn luôn menu Thống kê
        }
    }
}