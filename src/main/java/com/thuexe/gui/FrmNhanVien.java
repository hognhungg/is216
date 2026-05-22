package com.thuexe.gui;

import com.thuexe.bus.NhanVienBUS;
import com.thuexe.dto.NhanVienDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmNhanVien extends JPanel {
    private JTable tblNhanVien;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnDashboard;
    
    private NhanVienBUS nvBus = new NhanVienBUS();

    // 🛠️ ĐÃ THÊM: Các thẻ Label hiển thị số liệu thống kê động
    private JLabel lblTotalStaff, lblEnterpriseStaff, lblSystemStatus;

    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);
    private static final Color COLOR_PRIMARY = new Color(74, 137, 95); 

    public FrmNhanVien() {
        initComponents();
        initEvents();
        loadData(); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // =========================================================================
        // PHẦN BẮC: CHỨA TIÊU ĐỀ, THÈ THỐNG KÊ VÀ THANH NÚT TÁC VỤ (XẾP DỌC)
        // =========================================================================
        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        // 1. Dòng tiêu đề chính trên cùng
        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTitle = new JLabel("Quản Lý Danh Sách Nhân Viên");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTitle, BorderLayout.WEST);
        
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15)); // Khoảng cách 15px

        // 2. 🛠️ ĐÃ THÊM: Hàng chứa 3 thẻ thống kê bo góc (Đồng bộ thiết kế)
        JPanel pnlDashboardCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlDashboardCards.setBackground(COLOR_BG);
        pnlDashboardCards.setPreferredSize(new Dimension(0, 100));

        // Thẻ 1: Tổng số nhân viên (Màu tím)
        JPanel cardTotal = createStatCard("TỔNG SỐ NHÂN VIÊN", "0", new Color(111, 96, 216));
        lblTotalStaff = (JLabel) cardTotal.getClientProperty("valueLabel");
        
        // Thẻ 2: Nhân viên thuộc doanh nghiệp đối tác (Màu xanh dương nhẹ)
        JPanel cardEnterprise = createStatCard("THUỘC DOANH NGHIỆP", "0 người", new Color(145, 145, 255));
        lblEnterpriseStaff = (JLabel) cardEnterprise.getClientProperty("valueLabel");

        // Thẻ 3: Trạng thái hệ thống (Màu vàng cam)
        JPanel cardStatus = createStatCard("TRẠNG THÁI HỆ THỐNG", "Ổn Định", new Color(254, 202, 87));
        lblSystemStatus = (JLabel) cardStatus.getClientProperty("valueLabel");

        pnlDashboardCards.add(cardTotal);
        pnlDashboardCards.add(cardEnterprise);
        pnlDashboardCards.add(cardStatus);

        pnlNorthContainer.add(pnlDashboardCards);
        pnlNorthContainer.add(Box.createVerticalStrut(20)); // Khoảng cách 20px xuống cụm nút

        // 3. 🛠️ ĐÃ DI CHUYỂN: Thanh chứa nút Thêm, Sửa, Xóa nằm dưới Thẻ thống kê
        JPanel pnlActionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActionRow.setBackground(COLOR_BG);

        btnThem = createStyledButton("Thêm nhân viên", COLOR_PRIMARY);
        btnSua = createStyledButton("Sửa thông tin", new Color(225, 140, 50));
        btnXoa = createStyledButton("Nghỉ việc (Xóa)", new Color(190, 50, 50));

        pnlActionRow.add(btnThem);
        pnlActionRow.add(btnSua);
        pnlActionRow.add(btnXoa);

        pnlNorthContainer.add(pnlActionRow);
        add(pnlNorthContainer, BorderLayout.NORTH);

        // =========================================================================
        // CENTER: BẢNG DỮ LIỆU NHÂN VIÊN
        // =========================================================================
        String[] headers = {"Mã NV", "Họ Tên", "Số CCCD", "Số Điện thoại", "Email liên hệ", "Mã Chủ Thể", "Mã Doanh Nghiệp"};
        model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblNhanVien = new JTable(model);
        
        tblNhanVien.setRowHeight(35);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblNhanVien.getTableHeader().setBackground(new Color(230, 235, 240));
        tblNhanVien.setSelectionBackground(new Color(210, 230, 215));
        tblNhanVien.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 1));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // SOUTH: THANH ĐIỀU HƯỚNG PHÍA DƯỚI DÀNH CHO FORM
        // =========================================================================
        JPanel pnlBottomGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomGroup.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới danh sách", new Color(110, 110, 110)); 
        btnDashboard = createStyledButton("Quay về Dashboard", new Color(190, 50, 50)); 

        pnlBottomGroup.add(btnLamMoi);
        pnlBottomGroup.add(btnDashboard);
        add(pnlBottomGroup, BorderLayout.SOUTH);
    }

    // 🛠️ ĐÃ CẬP NHẬT: Hàm loadData tự động tính toán số lượng đẩy lên các thẻ màu
    private void loadData() {
        model.setRowCount(0);
        try {
            List<NhanVienDTO> list = nvBus.getAllNhanVien();
            int totalNhanVien = list.size();
            int thuocDoanhNghiep = 0;

            for (NhanVienDTO nv : list) {
                model.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getHoTenNV(),
                    nv.getCccd(),
                    nv.getSdt(),
                    nv.getEmail(),
                    nv.getMaChuThe() == 0 ? "Chưa cấp" : nv.getMaChuThe(),
                    nv.getMaDoanhNghiep() == 0 ? "Chưa gán" : nv.getMaDoanhNghiep()
                });
                
                // Đếm số nhân viên trực thuộc doanh nghiệp đối tác (khóa ngoại > 0)
                if (nv.getMaDoanhNghiep() > 0) {
                    thuocDoanhNghiep++;
                }
            }

            // Đổ con số thực tế từ DB lên các thẻ UI phía trên
            lblTotalStaff.setText(String.valueOf(totalNhanVien));
            lblEnterpriseStaff.setText(thuocDoanhNghiep + " người");
            lblSystemStatus.setText(totalNhanVien > 0 ? "Hoạt Động" : "Trống");

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối khi tải danh sách nhân viên!", "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initEvents() {
        // --- Sự kiện kích hoạt Form khi THÊM MỚI ---
        btnThem.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            DlgNhanVien dlg = new DlgNhanVien(parentWindow, "Thêm Nhân Viên Mới", "THEM");
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                Object[] rowData = dlg.getFormValues();
                try {
                    int maChuThe = (rowData[5] == null || rowData[5].toString().isEmpty()) ? 0 : Integer.parseInt(rowData[5].toString());
                    int maDoanhNghiep = (rowData[6] == null || rowData[6].toString().isEmpty()) ? 0 : Integer.parseInt(rowData[6].toString());
                    
                    NhanVienDTO nv = new NhanVienDTO(0, rowData[1].toString(), rowData[2].toString(), rowData[3].toString(), rowData[4].toString(), maChuThe, maDoanhNghiep);
                    
                    String result = nvBus.insertNhanVien(nv);
                    if ("SUCCESS".equalsIgnoreCase(result)) {
                        JOptionPane.showMessageDialog(this, "Thêm nhân viên mới thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        loadData(); 
                    } else {
                        JOptionPane.showMessageDialog(this, "Thất bại: " + result, "Lỗi thực thi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Sự kiện kích hoạt Form khi CẬP NHẬT (SỬA) ---
        btnSua.addActionListener(e -> {
            int selectedRow = tblNhanVien.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên trên bảng cần sửa đổi thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Object[] currentData = new Object[model.getColumnCount()];
            for (int i = 0; i < model.getColumnCount(); i++) {
                currentData[i] = model.getValueAt(selectedRow, i);
            }

            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            DlgNhanVien dlg = new DlgNhanVien(parentWindow, "Cập Nhật Thông Tin Nhân Viên", "SUA");
            dlg.setFormValues(currentData);
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                Object[] updatedData = dlg.getFormValues();
                try {
                    int maNhanVien = Integer.parseInt(updatedData[0].toString());
                    int maChuThe = (updatedData[5] == null || updatedData[5].toString().isEmpty() || "Chưa cấp".equals(updatedData[5].toString())) ? 0 : Integer.parseInt(updatedData[5].toString());
                    int maDoanhNghiep = (updatedData[6] == null || updatedData[6].toString().isEmpty() || "Chưa gán".equals(updatedData[6].toString())) ? 0 : Integer.parseInt(updatedData[6].toString());
                    
                    NhanVienDTO nv = new NhanVienDTO(maNhanVien, updatedData[1].toString(), updatedData[2].toString(), updatedData[3].toString(), updatedData[4].toString(), maChuThe, maDoanhNghiep);
                    
                    String result = nvBus.updateNhanVien(nv);
                    if ("SUCCESS".equalsIgnoreCase(result)) {
                        JOptionPane.showMessageDialog(this, "Thông tin dữ liệu đã được cập nhật!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Thất bại: " + result, "Lỗi thực thi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Định dạng mã số khóa ngoại không hợp lệ!", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Sự kiện kích hoạt Form khi XÓA ---
        btnXoa.addActionListener(e -> {
            int selectedRow = tblNhanVien.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên muốn xóa dữ liệu khỏi hệ thống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int maNhanVien = Integer.parseInt(model.getValueAt(selectedRow, 0).toString());
            String tenNhanVien = model.getValueAt(selectedRow, 1).toString();
            
            int choice = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn xóa nhân viên [" + tenNhanVien + "] khỏi hệ thống?", 
                    "Xác nhận xóa tài khoản", JOptionPane.YES_NO_OPTION);
                    
            if (choice == JOptionPane.YES_OPTION) {
                String result = nvBus.deleteNhanVien(maNhanVien);
                if ("SUCCESS".equalsIgnoreCase(result)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa nhân viên khỏi cơ sở dữ liệu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + result, "Lỗi ràng buộc hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnLamMoi.addActionListener(e -> {
            loadData();
            tblNhanVien.clearSelection();
            JOptionPane.showMessageDialog(this, "Danh sách dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnDashboard.addActionListener(e -> {
            Component topLevel = SwingUtilities.getWindowAncestor(this);
            if (topLevel instanceof FrmMain) {
                ((FrmMain) topLevel).btnDashboard.doClick();
            }
        });
    }

    // 🛠️ ĐÃ THÊM: Hàm vẽ cấu trúc Thẻ Thống Kê bo tròn góc
    private JPanel createStatCard(String title, String defaultValue, Color bg) {
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
        card.setBackground(bg);
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
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
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