package com.thuexe.gui;

import com.thuexe.bus.KhachHangBUS;
import com.thuexe.dto.KhachHangDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;

public class FrmKhachHang extends JPanel {
    private JTable tblKhachHang;
    private DefaultTableModel model;
    private KhachHangBUS khBus = new KhachHangBUS();
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnDashboard;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private JLabel lblTotalCount, lblActiveRentCount, lblStatusText;

    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);

    public FrmKhachHang() {
        initComponents();
        initEvents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // =========================================================================
        // PHẦN BẮC: CHỨA TIÊU ĐỀ, THẺ THỐNG KÊ VÀ THANH NÚT TÁC VỤ (XẾP DỌC)
        // =========================================================================
        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        // 1. Dòng tiêu đề lớn trên cùng
        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTitle = new JLabel("Quản Lý Thông Tin Khách Hàng");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTitle, BorderLayout.WEST);
        
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15)); // Khoảng cách 15px

        // 2. Hàng chứa 3 thẻ thống kê bo góc
        JPanel pnlDashboardCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlDashboardCards.setBackground(COLOR_BG);
        pnlDashboardCards.setPreferredSize(new Dimension(0, 100));

        JPanel cardTotal = createStatCard("TỔNG SỐ KHÁCH HÀNG", "0", new Color(111, 96, 216));
        lblTotalCount = (JLabel) cardTotal.getClientProperty("valueLabel");
        
        JPanel cardRent = createStatCard("ĐANG THUÊ XE", "0 người", new Color(145, 145, 255));
        lblActiveRentCount = (JLabel) cardRent.getClientProperty("valueLabel");

        JPanel cardStatus = createStatCard("HẠNG THÀNH VIÊN", "Ổn Định", new Color(254, 202, 87));
        lblStatusText = (JLabel) cardStatus.getClientProperty("valueLabel");

        pnlDashboardCards.add(cardTotal);
        pnlDashboardCards.add(cardRent);
        pnlDashboardCards.add(cardStatus);

        pnlNorthContainer.add(pnlDashboardCards);
        pnlNorthContainer.add(Box.createVerticalStrut(20)); // Khoảng cách giữa Thẻ và Bộ nút

        // 3. 🛠️ ĐÃ DI CHUYỂN: Thanh chứa nút Thêm, Sửa, Xóa nằm dưới Thẻ và trên Bảng
        JPanel pnlActionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActionRow.setBackground(COLOR_BG);

        btnThem = createStyledButton("Thêm khách hàng", new Color(74, 137, 95));
        btnSua = createStyledButton("Sửa thông tin", new Color(225, 140, 50));
        btnXoa = createStyledButton("Xóa khách hàng", new Color(190, 50, 50));

        pnlActionRow.add(btnThem);
        pnlActionRow.add(btnSua);
        pnlActionRow.add(btnXoa);

        pnlNorthContainer.add(pnlActionRow);
        add(pnlNorthContainer, BorderLayout.NORTH);

        // =========================================================================
        // CENTER: BẢNG HIỂN THỊ DỮ LIỆU KHÁCH HÀNG
        // =========================================================================
        String[] headers = {"Mã KH", "Họ và Tên", "Số CCCD", "Số Bằng Lái", "Số Điện Thoại", "Giới Tính", "Ngày Sinh", "Địa Chỉ"};
        model = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(35);
        tblKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhachHang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblKhachHang.getTableHeader().setBackground(new Color(230, 235, 240));

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // SOUTH: THANH CHỨC NĂNG PHỤ
        // =========================================================================
        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomButtons.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới danh sách", new Color(110, 110, 110));
        btnDashboard = createStyledButton("Quay về Dashboard", new Color(214, 48, 49));

        pnlBottomButtons.add(btnLamMoi);
        pnlBottomButtons.add(btnDashboard);
        add(pnlBottomButtons, BorderLayout.SOUTH);
    }

    private void initEvents() {
        // HÀNH ĐỘNG THÊM MỚI
        btnThem.addActionListener(e -> {
            Window parent = SwingUtilities.getWindowAncestor(this);
            DlgKhachHang dlg = new DlgKhachHang(parent, "Đăng ký khách hàng mới", "THEM");
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                Object[] val = dlg.getFormValues();
                try {
                    KhachHangDTO kh = new KhachHangDTO(0, val[1].toString(), val[2].toString(), val[3].toString(), val[4].toString(), val[5].toString(), sdf.parse(val[6].toString()), val[7].toString(), 0);
                    String res = khBus.insertKhachHang(kh);
                    if ("SUCCESS".equalsIgnoreCase(res)) {
                        JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                        loadData();
                    } else JOptionPane.showMessageDialog(this, "Thất bại: " + res, "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        // HÀNH ĐỘNG SỬA
        btnSua.addActionListener(e -> {
            int row = tblKhachHang.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng trên bảng!"); return; }

            Object[] rowData = new Object[]{ model.getValueAt(row,0), model.getValueAt(row,1), model.getValueAt(row,2), model.getValueAt(row,3), model.getValueAt(row,4), model.getValueAt(row,5), model.getValueAt(row,6), model.getValueAt(row,7) };
            
            Window parent = SwingUtilities.getWindowAncestor(this);
            DlgKhachHang dlg = new DlgKhachHang(parent, "Chỉnh sửa thông tin khách hàng", "SUA");
            dlg.setFormValues(rowData);
            dlg.setVisible(true);

            if (dlg.isConfirmed()) {
                Object[] val = dlg.getFormValues();
                try {
                    KhachHangDTO kh = new KhachHangDTO(Integer.parseInt(val[0].toString()), val[1].toString(), val[2].toString(), val[3].toString(), val[4].toString(), val[5].toString(), sdf.parse(val[6].toString()), val[7].toString(), 0);
                    String res = khBus.updateKhachHang(kh);
                    if ("SUCCESS".equalsIgnoreCase(res)) {
                        JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu thành công!");
                        loadData();
                    } else JOptionPane.showMessageDialog(this, "Thất bại: " + res, "Lỗi", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        // HÀNH ĐỘNG XÓA
        btnXoa.addActionListener(e -> {
            int row = tblKhachHang.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!"); return; }

            int maKh = Integer.parseInt(model.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa khách hàng mã số " + maKh + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String res = khBus.deleteKhachHang(maKh);
                if ("SUCCESS".equalsIgnoreCase(res)) {
                    JOptionPane.showMessageDialog(this, "Xóa tài khoản khách hàng thành công!");
                    loadData();
                } else JOptionPane.showMessageDialog(this, "Lỗi ràng buộc: " + res, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnLamMoi.addActionListener(e -> loadData());
        btnDashboard.addActionListener(e -> {
            Container p = this.getParent();
            if (p instanceof JPanel && ((JPanel) p).getTopLevelAncestor() instanceof FrmMain) {
                ((FrmMain) ((JPanel) p).getTopLevelAncestor()).btnDashboard.doClick();
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            List<KhachHangDTO> list = khBus.getAllKhachHang();
            int totalKhach = list.size();
            int đangThuê = 0; 

            for (KhachHangDTO kh : list) {
                model.addRow(new Object[]{
                    kh.getMaKhachHang(), kh.getHoTen(), kh.getCccd(), kh.getSoBangLai(),
                    kh.getSdt(), kh.getGioiTinh(), kh.getNgaySinh() != null ? sdf.format(kh.getNgaySinh()) : "", kh.getDiaChi()
                });
                if (kh.getMaChuThe() > 0) { 
                    đangThuê++;
                }
            }

            lblTotalCount.setText(String.valueOf(totalKhach));
            lblActiveRentCount.setText(đangThuê + " KH");
            lblStatusText.setText(totalKhach > 0 ? "Hoạt Động" : "Trống");

        } catch (Exception ex) { 
            ex.printStackTrace(); 
        }
    }

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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); btn.setForeground(Color.WHITE); btn.setBackground(bg);
        btn.setFocusPainted(false); btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground()); g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 6, 6);
                g2.dispose(); super.paint(g, c);
            }
        });
        return btn;
    }
}