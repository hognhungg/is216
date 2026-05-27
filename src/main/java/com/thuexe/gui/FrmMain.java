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

public class FrmMain extends JFrame {

    private TaiKhoanDTO tk;

    // Các thành phần nút trên thanh Sidebar bên trái
    public JButton btnDashboard; 
    private JButton btnXe, btnTraCuuXe, btnKhachHang, btnNhanVien, btnHoSoNhanVien;
    private JButton btnLapPhieu, btnTraXe, btnThongKe, btnDangXuat;
    
    // Các nút chức năng dành riêng cho Role KhachHang
    private JButton btnKhachDatXe;      // Trang chủ & Đặt xe
    private JButton btnChuyenDiCuaToi;  // Chuyến đi của tôi
    private JButton btnViVoucher;       // Ví Voucher & Ưu đãi
    
    // --- NÚT ĐÁNH GIÁ DỊCH VỤ ---
    private JButton btnDanhGiaMenu;     // Đóng góp ý kiến

    // Các đường gạch ngang phân tách
    private JSeparator sepAdminNhanVien; 
    private JSeparator sepKhachNew;
    
    // Các khoảng đệm để điều khiển ẩn hiện linh hoạt theo Role, tránh dồn khoảng trống
    private Component strutAdmin;
    private Component strutNghiepVu;
    private Component strutKhach;

    // Panel chứa nội dung động bên phải
    private JPanel pnlContentBody;

    // Bảng màu Flat UI đồng bộ (Tone xanh lá cây chủ đạo)
    private static final Color COLOR_SIDEBAR = new Color(74, 137, 95);    
    private static final Color COLOR_SIDEBAR_HOVER = new Color(62, 117, 80); 
    private static final Color COLOR_SIDEBAR_ACTIVE = new Color(53, 102, 69); 
    private static final Color COLOR_BG_BODY = new Color(248, 249, 250);   
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);    
    private static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.BOLD, 14);

    public FrmMain() {
        this.tk = FrmLogin.userLogged;

        // 1. Cấu hình cơ bản cho Frame chính
        setTitle("HỆ THỐNG QUẢN LÝ THUÊ XE Ô TÔ");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 2. KHỞI TẠO THANH SIDEBAR BÊN TRÁI
        initSidebar();

        // 3. KHỞI TẠO VÙNG CHỨA NỘI DUNG CHÍNH BÊN PHẢI
        pnlContentBody = new JPanel(new CardLayout());
        pnlContentBody.setBackground(COLOR_BG_BODY);
        add(pnlContentBody, BorderLayout.CENTER);

        // Mặc định ban đầu sẽ nạp giao diện tương ứng với quyền đăng nhập
        if (tk != null && "KhachHang".equalsIgnoreCase(tk.getVaiTro().trim())) {
            showKhachDatXeContent();
            btnKhachDatXe.setBackground(COLOR_SIDEBAR_ACTIVE);
        } else {
            showDashboardContent();
            btnDashboard.setBackground(COLOR_SIDEBAR_ACTIVE);
        }

        // 4. KIỂM TRA PHÂN QUYỀN
        phanQuyen();

        // 5. GẮN CÁC SỰ KIỆN CHUYỂN MODULE
        initEvents();

        this.setExtendedState(MAXIMIZED_BOTH);
    }

    /**
     * THIẾT KẾ CỘT SIDEBAR BÊN TRÁI DỌC PHẲNG (FLAT)
     */
    private void initSidebar() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COLOR_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(240, this.getHeight()));
        pnlSidebar.setLayout(new BoxLayout(pnlSidebar, BoxLayout.Y_AXIS));
        pnlSidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Phần Logo / Tiêu đề Hệ thống nằm trên cùng Sidebar
        JLabel lblLogo = new JLabel("HỆ THỐNG THUÊ XE");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setBorder(new EmptyBorder(10, 0, 15, 0)); 
        pnlSidebar.add(lblLogo);

        // Khởi tạo các nút bấm hệ thống
        btnDashboard = createSidebarButton("Tổng quan hệ thống");
        btnXe = createSidebarButton("Quản lý Xe");
        btnTraCuuXe = createSidebarButton("Tra cứu xe"); 
        btnKhachHang = createSidebarButton("Quản lý Khách hàng");
        btnNhanVien = createSidebarButton("Quản lý Nhân viên");
        btnHoSoNhanVien = createSidebarButton("Hồ sơ cá nhân");
        btnDanhGiaMenu = createSidebarButton("Đóng góp ý kiến");
        btnLapPhieu = createSidebarButton("Lập Phiếu Thuê");
        btnTraXe = createSidebarButton("Trả Xe & Thanh Toán");
        btnThongKe = createSidebarButton("Báo cáo thống kê");
        btnDangXuat = createSidebarButton("Đăng xuất tài khoản");

        // Khởi tạo các nút dành riêng cho Khách hàng
        btnKhachDatXe = createSidebarButton("Trang chủ & Đặt xe");
        btnChuyenDiCuaToi = createSidebarButton("Chuyến đi của tôi");
        btnViVoucher = createSidebarButton("Ví Voucher & Ưu đãi");

        // --- 1. THÊM NHÓM NÚT QUẢN LÝ (ADMIN / NHÂN VIÊN) ---
        pnlSidebar.add(btnDashboard); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnXe); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnTraCuuXe); pnlSidebar.add(Box.createVerticalStrut(8)); 
        pnlSidebar.add(btnKhachHang); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnNhanVien);
        pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnHoSoNhanVien); 
        
        // Khoảng đệm dưới nhóm quản lý
        strutAdmin = Box.createVerticalStrut(15);
        pnlSidebar.add(strutAdmin);

        // Đường phân cách mỏng số 1
        sepAdminNhanVien = new JSeparator();
        sepAdminNhanVien.setMaximumSize(new Dimension(220, 1));
        pnlSidebar.add(sepAdminNhanVien); 
        
        strutNghiepVu = Box.createVerticalStrut(15);
        pnlSidebar.add(strutNghiepVu);

        // --- 2. THÊM NHÓM NÚT NGHIỆP VỤ VẬN HÀNH ---
        pnlSidebar.add(btnLapPhieu); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnTraXe); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnThongKe); 
        
        // --- 3. THÊM NHÓM NÚT CHỨC NĂNG CỦA KHÁCH HÀNG ---
        pnlSidebar.add(btnKhachDatXe); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnChuyenDiCuaToi); pnlSidebar.add(Box.createVerticalStrut(8));
        pnlSidebar.add(btnViVoucher); 
        
        // Khoảng đệm dưới nhóm nút khách hàng / nghiệp vụ trước khi kẻ vạch cuối cùng
        strutKhach = Box.createVerticalStrut(15);
        pnlSidebar.add(strutKhach);
        
        // Đường phân cách mỏng số 2 (Ngay phía trên nút Đóng góp ý kiến)
        sepKhachNew = new JSeparator();
        sepKhachNew.setMaximumSize(new Dimension(220, 1));
        pnlSidebar.add(sepKhachNew); pnlSidebar.add(Box.createVerticalStrut(15));
        
        // --- 4. THÊM NÚT ĐÓNG GÓP Ý KIẾN ---
        pnlSidebar.add(btnDanhGiaMenu);
        
        // Đẩy nút Đăng xuất xuống sát đáy thanh Sidebar
        pnlSidebar.add(Box.createVerticalGlue());
        pnlSidebar.add(btnDangXuat);

        add(pnlSidebar, BorderLayout.WEST);
    }

    /**
     * PHÂN QUYỀN TRUY CẬP ĐỐI VỚI TỪNG CHỦ THỂ TRONG HỆ THỐNG
     */
    private void phanQuyen() {
        if (tk == null || tk.getVaiTro() == null) return;
        
        String role = tk.getVaiTro().trim();

        if ("NhanVien".equalsIgnoreCase(role)) {
            // Hiển thị chức năng Nhân viên
            btnHoSoNhanVien.setVisible(true);
            btnDashboard.setVisible(true);
            btnXe.setVisible(true);
            btnTraCuuXe.setVisible(true);  
            btnKhachHang.setVisible(true);
            btnLapPhieu.setVisible(true);
            btnTraXe.setVisible(true);
            
            // Ẩn các nút không thuộc quyền
            btnNhanVien.setVisible(false);  
            btnThongKe.setVisible(false);   
            btnDanhGiaMenu.setVisible(false); 
            btnKhachDatXe.setVisible(false);
            btnChuyenDiCuaToi.setVisible(false);
            btnViVoucher.setVisible(false);
            
            // Cấu hình đường gạch và khoảng đệm
            strutAdmin.setVisible(true);
            sepAdminNhanVien.setVisible(true);
            strutNghiepVu.setVisible(true);
            strutKhach.setVisible(false);
            sepKhachNew.setVisible(false);

        } else if ("KhachHang".equalsIgnoreCase(role)) {
            // Ẩn toàn bộ chức năng nội bộ Admin/NhanVien
            btnHoSoNhanVien.setVisible(false);
            btnDashboard.setVisible(false); 
            btnXe.setVisible(false);        
            btnTraCuuXe.setVisible(false); 
            btnKhachHang.setVisible(false); 
            btnNhanVien.setVisible(false);  
            btnLapPhieu.setVisible(false);  
            btnTraXe.setVisible(false);     
            btnThongKe.setVisible(false);   

            // Ẩn đường gạch và khoảng đệm phía trên
            strutAdmin.setVisible(false);
            sepAdminNhanVien.setVisible(false);
            strutNghiepVu.setVisible(false);

            // Hiển thị cụm chức năng Khách hàng lên sát logo
            btnKhachDatXe.setVisible(true);
            btnChuyenDiCuaToi.setVisible(true);
            btnViVoucher.setVisible(true);
            
            // Hiện khoảng đệm và đường vạch phân cách ngay trên Đóng góp ý kiến
            strutKhach.setVisible(true);
            sepKhachNew.setVisible(true);
            btnDanhGiaMenu.setVisible(true);  

        } else if ("Admin".equalsIgnoreCase(role)) {
            // Hiển thị toàn bộ nút quản lý và nghiệp vụ hệ thống
            btnHoSoNhanVien.setVisible(true);
            btnDashboard.setVisible(true);
            btnXe.setVisible(true);
            btnTraCuuXe.setVisible(true);  
            btnKhachHang.setVisible(true);
            btnNhanVien.setVisible(true);
            btnLapPhieu.setVisible(true);
            btnTraXe.setVisible(true);
            btnThongKe.setVisible(true);
            
            // Hiện nút đóng góp ý kiến liền kề bên dưới Báo cáo thống kê
            btnDanhGiaMenu.setVisible(true);  
            
            // Ẩn hoàn toàn cụm nút Khách hàng
            btnKhachDatXe.setVisible(false);
            btnChuyenDiCuaToi.setVisible(false);
            btnViVoucher.setVisible(false);

            // --- XỬ LÝ ĐỘC QUYỀN CHO ADMIN: Giúp nút Đóng góp ý kiến sát lên trên ---
            strutAdmin.setVisible(true);
            sepAdminNhanVien.setVisible(true);
            strutNghiepVu.setVisible(true);
            
            // Ẩn bớt 1 khoảng đệm dư thừa và 1 đường kẻ phụ của Khách hàng
            strutKhach.setVisible(false); 
            sepKhachNew.setVisible(true); // Giữ lại đường vạch này để ngăn cách Đóng góp ý kiến với Báo cáo thống kê
        }
    }

    public void showDashboardContent() {
        pnlContentBody.removeAll();
        JPanel pnlDashboard = new JPanel(new BorderLayout());
        pnlDashboard.setBackground(COLOR_BG_BODY);
        pnlDashboard.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(COLOR_BG_BODY);
        pnlHeader.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        String user = (tk != null) ? tk.getTenDangNhap() : "Admin";
        JLabel lblWelcome = new JLabel("Xin chào, " + user + " !");
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblWelcome.setForeground(Color.GRAY);

        JLabel lblTitle = new JLabel("Dashboard", JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        
        JPanel pnlTitleGroup = new JPanel(new BorderLayout());
        pnlTitleGroup.setBackground(COLOR_BG_BODY);
        pnlTitleGroup.add(lblWelcome, BorderLayout.NORTH);
        pnlTitleGroup.add(lblTitle, BorderLayout.SOUTH);
        pnlHeader.add(pnlTitleGroup, BorderLayout.CENTER);
        pnlDashboard.add(pnlHeader, BorderLayout.NORTH);

        JPanel pnlGridCards = new JPanel(new GridLayout(3, 1, 0, 25)); 
        pnlGridCards.setBackground(COLOR_BG_BODY);
        pnlGridCards.add(createReportRow("Doanh thu thuê xe", "Ngày hôm nay", "0đ", "Tháng này", "0đ", "Năm 2026", "0đ"));
        pnlGridCards.add(createReportRow("Lượng đơn đặt thuê", "Đang thuê", "0 xe", "Đã trả hôm nay", "0 xe", "Yêu cầu mới", "0"));
        pnlGridCards.add(createReportRow("Hệ thống xe", "Xe sẵn sàng", "0", "Xe đang bảo trì", "0", "Tổng số lượng xe", "0"));
        pnlDashboard.add(pnlGridCards, BorderLayout.CENTER);

        String role = (tk != null) ? tk.getVaiTro() : "Quản trị viên";
        JLabel lblStatus = new JLabel("Tài khoản đang chạy: " + user + " | Quyền: " + role + " | Database: Oracle 19c Connected", JLabel.RIGHT);
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setBorder(new EmptyBorder(15, 0, 0, 0));
        pnlDashboard.add(lblStatus, BorderLayout.SOUTH);

        pnlContentBody.add(pnlDashboard);
        pnlContentBody.revalidate();
        pnlContentBody.repaint();
    }

    private void showKhachDatXeContent() {
        pnlContentBody.removeAll();
        JPanel pnlMock = new JPanel(new GridBagLayout());
        pnlMock.setBackground(COLOR_BG_BODY);
        JLabel lbl = new JLabel("Giao diện chính: Tìm kiếm, hiển thị danh sách xe và Đặt xe trực tuyến.");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(COLOR_TEXT_DARK);
        pnlMock.add(lbl);
        pnlContentBody.add(pnlMock);
        pnlContentBody.revalidate();
        pnlContentBody.repaint();
    }

    private JPanel createReportRow(String rowTitle, String t1, String v1, String t2, String v2, String t3, String v3) {
        JPanel pnlRow = new JPanel(new BorderLayout());
        pnlRow.setBackground(COLOR_BG_BODY);
        JLabel lblRowTitle = new JLabel(rowTitle);
        lblRowTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRowTitle.setForeground(COLOR_TEXT_DARK);
        lblRowTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        pnlRow.add(lblRowTitle, BorderLayout.NORTH);

        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(COLOR_BG_BODY);
        pnlCards.add(createSingleCard(t1, v1));
        pnlCards.add(createSingleCard(t2, v2));
        pnlCards.add(createSingleCard(t3, v3));
        pnlRow.add(pnlCards, BorderLayout.CENTER);
        return pnlRow;
    }

    private JPanel createSingleCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12); 
                g2.setColor(new Color(225, 228, 224)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel lblTitle = new JLabel(title, JLabel.LEFT);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitle.setForeground(Color.GRAY);

        JLabel lblValue = new JLabel(value, JLabel.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(COLOR_TEXT_DARK);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_SIDEBAR);
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setMaximumSize(new Dimension(220, 42));
        btn.setPreferredSize(new Dimension(220, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() != COLOR_SIDEBAR_ACTIVE) {
                    btn.setBackground(COLOR_SIDEBAR_HOVER);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() != COLOR_SIDEBAR_ACTIVE) {
                    btn.setBackground(COLOR_SIDEBAR);
                }
            }
        });
        return btn;
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
        btnDanhGiaMenu.addActionListener(e -> {
            setMenuButtonActive(btnDanhGiaMenu);
            try {
                pnlContentBody.removeAll();          
                FrmDanhGia pnlDanhGia = new FrmDanhGia(); 
                pnlContentBody.add(pnlDanhGia);      
                pnlContentBody.revalidate();
                pnlContentBody.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nạp màn hình Đánh giá dịch vụ: " + ex.getMessage());
            }
        });

        btnDashboard.addActionListener(e -> {
            setMenuButtonActive(btnDashboard);
            showDashboardContent();
        });

        btnXe.addActionListener(e -> {
            setMenuButtonActive(btnXe);
            try {
                pnlContentBody.removeAll();
                FrmLoaiXe pnlLoaiXe = new FrmLoaiXe();
                pnlContentBody.add(pnlLoaiXe);
                pnlContentBody.revalidate();
                pnlContentBody.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nạp màn hình Quản lý Xe: " + ex.getMessage());
            }
        });

        btnTraCuuXe.addActionListener(e -> {
            setMenuButtonActive(btnTraCuuXe);
            try {
                pnlContentBody.removeAll();
                FrmTraCuuXe pnlTraCuu = new FrmTraCuuXe(); 
                pnlContentBody.add(pnlTraCuu);            
                pnlContentBody.revalidate();
                pnlContentBody.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nạp màn hình Tra cứu xe: " + ex.getMessage());
            }
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

        btnKhachHang.addActionListener(e -> {
            setMenuButtonActive(btnKhachHang);
            try {
                pnlContentBody.removeAll();
                FrmKhachHang pnlKhachHang = new FrmKhachHang();
                pnlContentBody.add(pnlKhachHang);
                pnlContentBody.revalidate();
                pnlContentBody.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nạp màn hình Quản lý Khách hàng: " + ex.getMessage());
            }
        });

        btnNhanVien.addActionListener(e -> {
            setMenuButtonActive(btnNhanVien);
            try {
                pnlContentBody.removeAll();
                FrmNhanVien pnlNhanVien = new FrmNhanVien();
                pnlContentBody.add(pnlNhanVien);
                pnlContentBody.revalidate();
                pnlContentBody.repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nạp màn hình Quản lý Nhân viên: " + ex.getMessage());
            }
        });
       btnHoSoNhanVien.addActionListener(e -> {

    setMenuButtonActive(btnHoSoNhanVien);

    try {

        pnlContentBody.removeAll();

        FrmHoSoNhanVienPanel pnlHoSo =
                new FrmHoSoNhanVienPanel();

        pnlContentBody.add(pnlHoSo);

        pnlContentBody.revalidate();

        pnlContentBody.repaint();

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Lỗi khi nạp hồ sơ nhân viên: "
                        + ex.getMessage());
    }
});
        btnLapPhieu.addActionListener(e -> {
            setMenuButtonActive(btnLapPhieu);
            try {
                pnlContentBody.removeAll();                
                FrmLapPhieuThue pnlLapPhieu = new FrmLapPhieuThue(); 
                pnlContentBody.add(pnlLapPhieu);            
                pnlContentBody.revalidate();                
                pnlContentBody.repaint();                   
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi nạp màn hình Lập phiếu thuê: " + ex.getMessage());
            }        
        });

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
