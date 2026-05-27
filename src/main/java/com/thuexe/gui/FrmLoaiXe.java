package com.thuexe.gui;

import com.thuexe.bus.LoaiXeBUS;
import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.dto.TaiKhoanDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmLoaiXe extends JPanel {

    private JTable tblLoaiXe;
    private DefaultTableModel tableModel;
    
    // Khai báo các nút chức năng
    private JButton btnThem, btnSua, btnXoa;         
    private JButton btnLamMoi, btnThoat;             

    private JLabel lblTongXeValue, lblDangThueValue, lblSanSangValue;
    private LoaiXeBUS loaiXeBUS;
    
    // Đối tượng lưu thông tin tài khoản đang đăng nhập để thực hiện phân quyền nội bộ
    private TaiKhoanDTO tkLogged;

    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);
    
    private static final Color CARD_COLOR_1 = new Color(111, 96, 216);  
    private static final Color CARD_COLOR_2 = new Color(145, 145, 255); 
    private static final Color CARD_COLOR_3 = new Color(254, 202, 87);  

    public FrmLoaiXe() {
        // Lấy thông tin tài khoản vừa đăng nhập thành công từ FrmLogin
        this.tkLogged = FrmLogin.userLogged;
        
        loaiXeBUS = new LoaiXeBUS();
        initComponents();
        
        // GỌI HÀM PHÂN QUYỀN: Ẩn/Hiện chức năng dựa theo chức vụ Admin hay NhanVien
        phanQuyenChucNang();
        
        initEvents();
        loadDataToTable(); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25)); 

        // =========================================================================
        // PHẦN BẮC: CHỨA TIÊU ĐỀ, THỂ THỐNG KÊ VÀ THANH NÚT TÁC VỤ
        // =========================================================================
        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        // 1. Dòng tiêu đề lớn trên cùng
        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTableTitle = new JLabel("Danh Sách Phân Loại Xe Hệ Thống");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTableTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTableTitle, BorderLayout.WEST);
        
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15)); 

        // 2. Hàng chứa 3 thẻ thống kê bo góc
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(COLOR_BG);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        JPanel cardTong = createFlatCard("TỔNG SỐ LOẠI XE", "0", CARD_COLOR_1);
        lblTongXeValue = (JLabel) cardTong.getClientProperty("valueLabel");
        
        JPanel cardThue = createFlatCard("ĐANG ĐƯỢC THUÊ", "4 xe", CARD_COLOR_2);
        lblDangThueValue = (JLabel) cardThue.getClientProperty("valueLabel");

        JPanel cardSanSang = createFlatCard("SẴN SÀNG PHỤC VỤ", "Đạt chuẩn", CARD_COLOR_3);
        lblSanSangValue = (JLabel) cardSanSang.getClientProperty("valueLabel");

        pnlCards.add(cardTong);
        pnlCards.add(cardThue);
        pnlCards.add(cardSanSang);

        pnlNorthContainer.add(pnlCards);
        pnlNorthContainer.add(Box.createVerticalStrut(20)); 

        // 3. Thanh chứa nút Thêm, Sửa, Xóa (Trọng tâm phân quyền nằm ở đây)
        JPanel pnlMiddleButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlMiddleButtons.setBackground(COLOR_BG);

        btnThem = createStyledButton("Thêm xe", new Color(74, 137, 95));       
        btnSua = createStyledButton("Sửa cấu trúc & Giá", new Color(225, 140, 50));          
        btnXoa = createStyledButton("Xóa dòng xe", new Color(190, 50, 50));            

        pnlMiddleButtons.add(btnThem);
        pnlMiddleButtons.add(btnSua);
        pnlMiddleButtons.add(btnXoa);
        
        pnlNorthContainer.add(pnlMiddleButtons);
        add(pnlNorthContainer, BorderLayout.NORTH);

        // =========================================================================
        // CENTER: BẢNG HIỂN THỊ DỮ LIỆU JTABLE
        // =========================================================================
        String[] columnNames = {"Mã Loại", "Tên Phân Loại Xe", "Nhiên Liệu Sử Dụng", "Giá Thuê Ngày", "Giá Thuê Giờ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblLoaiXe = new JTable(tableModel);
        
        tblLoaiXe.setRowHeight(35);
        tblLoaiXe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLoaiXe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblLoaiXe.getTableHeader().setBackground(new Color(230, 235, 240));

        JScrollPane scrollPane = new JScrollPane(tblLoaiXe);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // SOUTH: THANH CHỨC NĂNG ĐIỀU HƯỚNG PHÍA DƯỚI
        // =========================================================================
        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomButtons.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới danh sách", new Color(110, 110, 110)); 
        btnThoat = createStyledButton("Quay về Dashboard", new Color(214, 48, 49)); 

        pnlBottomButtons.add(btnLamMoi);
        pnlBottomButtons.add(btnThoat);
        add(pnlBottomButtons, BorderLayout.SOUTH);
    }

    /**
     * THỰC HIỆN PHÂN QUYỀN NGHIÊM NGẶT TRÊN GIAO DIỆN QUẢN LÝ XE
     */
    private void phanQuyenChucNang() {
        if (tkLogged == null || tkLogged.getVaiTro() == null) return;

        String role = tkLogged.getVaiTro().trim();

        // Nếu là Nhân viên -> Chặn đứng hành vi thay đổi cấu trúc xe và giá cả
        if ("NhanVien".equalsIgnoreCase(role)) {
            btnThem.setVisible(false);
            btnSua.setVisible(false);
            btnXoa.setVisible(false);
        } else {
            // Ngược lại, nếu là Admin -> Mở toàn quyền hiển thị bộ nút bấm
            btnThem.setVisible(true);
            btnSua.setVisible(true);
            btnXoa.setVisible(true);
        }
    }

    private void initEvents() {
        // --- Sự kiện nút THÊM XE ---
        btnThem.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            DlgLoaiXe dlg = new DlgLoaiXe(parentWindow, "Thêm phân loại xe mới", "THEM");
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                Object[] values = dlg.getFormValues();
                try {
                    LoaiXeDTO newDto = new LoaiXeDTO(
                        0, 
                        values[1].toString(), 
                        values[2].toString(), 
                        Double.parseDouble(values[3].toString()), 
                        Double.parseDouble(values[4].toString())
                    );
                    
                    String result = loaiXeBUS.insert(newDto); 
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(this, "Thêm phân loại xe mới thành công!");
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thêm thất bại: " + result, "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi thực thi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Sự kiện nút SỬA THÔNG TIN XE ---
        btnSua.addActionListener(e -> {
            int selectedRow = tblLoaiXe.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng xe dưới bảng trước khi sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object[] rowData = new Object[] {
                tableModel.getValueAt(selectedRow, 0),
                tableModel.getValueAt(selectedRow, 1),
                tableModel.getValueAt(selectedRow, 2),
                tableModel.getValueAt(selectedRow, 3),
                tableModel.getValueAt(selectedRow, 4)
            };

            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            DlgLoaiXe dlg = new DlgLoaiXe(parentWindow, "Chỉnh sửa thông tin loại xe", "SUA");
            dlg.setFormValues(rowData);
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                Object[] values = dlg.getFormValues();
                try {
                    int ma = Integer.parseInt(values[0].toString());
                    double giaNgayVal = Double.parseDouble(values[3].toString());
                    double giaGioVal = Double.parseDouble(values[4].toString());

                    LoaiXeDTO updateDto = new LoaiXeDTO(ma, values[1].toString(), values[2].toString(), giaNgayVal, giaGioVal);
                    String result = loaiXeBUS.updateGiaThue(updateDto);
                    
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu thành công!");
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thất bại: " + result, "Lỗi cập nhật", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Thất bại", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Sự kiện nút XÓA XE ---
        btnXoa.addActionListener(e -> {
            int selectedRow = tblLoaiXe.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng xe cần xóa dưới bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object[] rowData = new Object[] {
                tableModel.getValueAt(selectedRow, 0),
                tableModel.getValueAt(selectedRow, 1),
                tableModel.getValueAt(selectedRow, 2),
                tableModel.getValueAt(selectedRow, 3),
                tableModel.getValueAt(selectedRow, 4)
            };

            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            DlgLoaiXe dlg = new DlgLoaiXe(parentWindow, "Xác nhận xóa phân loại xe", "XOA");
            dlg.setFormValues(rowData);
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                try {
                    int maLoai = Integer.parseInt(rowData[0].toString());
                    String result = loaiXeBUS.delete(maLoai); 
                    
                    if ("SUCCESS".equals(result)) {
                        JOptionPane.showMessageDialog(this, "Thực hiện xóa phân loại xe thành công!");
                        loadDataToTable();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xóa thất bại: " + result, "Lỗi ràng buộc dữ liệu", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Thất bại", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Sự kiện làm mới bảng ---
        btnLamMoi.addActionListener(e -> {
            loadDataToTable();
            tblLoaiXe.clearSelection();
            JOptionPane.showMessageDialog(this, "Dữ liệu hệ thống đã được đồng bộ mới nhất!");
        });

        // --- Sự kiện quay về màn hình Dashboard ---
        btnThoat.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent instanceof JPanel) {
                JPanel pnlContentBody = (JPanel) parent;
                if (pnlContentBody.getTopLevelAncestor() instanceof FrmMain) {
                    FrmMain mainFrame = (FrmMain) pnlContentBody.getTopLevelAncestor();
                    mainFrame.btnDashboard.doClick(); 
                }
            }
        });
    }

    private void loadDataToTable() {
        tableModel.setRowCount(0);
        try {
            tableModel.addRow(new Object[]{"1", "Xe 4 Chỗ (Sedan Hạng B)", "Xăng A95", "800000", "90000"});
            tableModel.addRow(new Object[]{"2", "Xe 7 Chỗ (SUV Gia Đình)", "Dầu Diesel", "1200000", "130000"});
            tableModel.addRow(new Object[]{"3", "Xe Bán Tải (Pickup)", "Dầu Diesel", "1000000", "110000"});
            tableModel.addRow(new Object[]{"4", "Xe Máy Côn Tay 150cc", "Xăng A95", "250000", "30000"});
            tableModel.addRow(new Object[]{"5", "Xe Điện 4 Chỗ Urban", "Điện sạc", "700000", "80000"});

            lblTongXeValue.setText("5");
            lblDangThueValue.setText("3 xe");
            lblSanSangValue.setText("Sẵn Sàng");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu giả lập: " + e.getMessage());
        }
    }

    private JPanel createFlatCard(String title, String defaultValue, Color colorBG) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setBackground(colorBG);
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(240, 240, 240));

        JLabel lblValue = new JLabel(defaultValue);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        card.putClientProperty("valueLabel", lblValue); 

        return card;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 6, 6);
                g2.dispose();
                super.paint(g, c);
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });

        return btn;
    }
}