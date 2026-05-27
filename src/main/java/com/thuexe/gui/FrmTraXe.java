package com.thuexe.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class FrmTraXe extends JPanel {

    private JComboBox<String> cboMaPhieu;
    private JTextField txtBienSo, txtTenXe, txtNgayNhan, txtNgayTraThucTe;
    private JTextField txtTienThueGoc, txtPhiPhatSinh, txtTongThanhToan;
    private JButton btnXacNhanTra;
    private JButton btnXacNhanTra;
    private JButton btnThanhToanQR;

    private JLabel lblQRCode;
    private JLabel lblHoanTatValue, lblChuaHoanTatValue, lblDoanhThuValue;

    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);
    
    // Đồng bộ bảng màu Flat UI
    private static final Color CARD_COLOR_1 = new Color(46, 204, 113);  // Xanh lá
    private static final Color CARD_COLOR_2 = new Color(231, 76, 60);   // Đỏ
    private static final Color CARD_COLOR_3 = new Color(52, 152, 219);  // Xanh dương

    // Định nghĩa các Font chữ lớn, rõ nét hơn theo yêu cầu
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 15);
    private static final Font FONT_TITLE_ZONE = new Font("Segoe UI", Font.BOLD, 16);

    public FrmTraXe() {
        initComponents();
        initEvents();
        loadThongKeDemo();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // =========================================================================
        // PHẦN BẮC (NORTH): TIÊU ĐỀ VÀ THẺ THỐNG KÊ
        // =========================================================================
        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        // 1. Tiêu đề phân hệ
        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTitle = new JLabel("Quy Trình Trả Xe & Thanh Toán Hóa Đơn");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTitle, BorderLayout.WEST);
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15));

        // 2. Ba thẻ thống kê
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(COLOR_BG);
        pnlCards.setPreferredSize(new Dimension(0, 110)); 

        JPanel cardHoanTat = createFlatCard("SỐ XE ĐÃ HOÀN TẤT", "0", CARD_COLOR_1);
        lblHoanTatValue = (JLabel) cardHoanTat.getClientProperty("valueLabel");

        JPanel cardChuaHoanTat = createFlatCard("SỐ XE CHƯA HOÀN TẤT", "0", CARD_COLOR_2);
        lblChuaHoanTatValue = (JLabel) cardChuaHoanTat.getClientProperty("valueLabel");

        JPanel cardDoanhThu = createFlatCard("TỔNG DOANH THU TRẢ XE", "0 đ", CARD_COLOR_3);
        lblDoanhThuValue = (JLabel) cardDoanhThu.getClientProperty("valueLabel");

        pnlCards.add(cardHoanTat);
        pnlCards.add(cardChuaHoanTat);
        pnlCards.add(cardDoanhThu);
        pnlNorthContainer.add(pnlCards);
        
        add(pnlNorthContainer, BorderLayout.NORTH);

        // =========================================================================
        // PHẦN TRUNG TÂM (CENTER): BỐ TRÍ CUỘN & PHÓNG TO CHỮ VÙNG DỮ LIỆU
        // =========================================================================
        JPanel pnlMainContent = new JPanel();
        pnlMainContent.setLayout(new BoxLayout(pnlMainContent, BoxLayout.Y_AXIS));
        pnlMainContent.setBackground(COLOR_BG);

        // --- VÙNG 1: THÔNG TIN ĐỐI SOÁT TRẢ XE (CHO PHÉP NHẬP) ---
        JPanel pnlDoiSoat = new JPanel(new GridBagLayout());
        pnlDoiSoat.setBackground(Color.WHITE);
        pnlDoiSoat.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true), 
                " Đối soát dữ liệu xe nhận thực tế ", 
                0, 0, FONT_TITLE_ZONE, COLOR_TEXT_DARK)); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 0: Chọn phiếu thuê
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        JLabel lblMaPhieu = new JLabel("Mã phiếu thuê (Đang thuê):");
        lblMaPhieu.setFont(FONT_LABEL);
        pnlDoiSoat.add(lblMaPhieu, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        cboMaPhieu = new JComboBox<>(new String[]{"-- Chọn mã phiếu đối soát --", "PT001", "PT002"});
        cboMaPhieu.setFont(FONT_INPUT);
        cboMaPhieu.setPreferredSize(new Dimension(0, 38)); 
        pnlDoiSoat.add(cboMaPhieu, gbc);

        // Dòng 1: Biển số xe
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        JLabel lblBienSo = new JLabel("Biển số xe:");
        lblBienSo.setFont(FONT_LABEL);
        pnlDoiSoat.add(lblBienSo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtBienSo = createEditableTextField(); // <--- Được phép nhập tự do
        pnlDoiSoat.add(txtBienSo, gbc);

        // Dòng 2: Tên phương tiện
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lblTenXe = new JLabel("Tên phương tiện:");
        lblTenXe.setFont(FONT_LABEL);
        pnlDoiSoat.add(lblTenXe, gbc);
        
        gbc.gridx = 1;
        txtTenXe = createEditableTextField(); // <--- Được phép nhập tự do
        pnlDoiSoat.add(txtTenXe, gbc);

        // Dòng 3: Ngày nhận
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lblNgayNhan = new JLabel("Ngày hệ thống bàn giao:");
        lblNgayNhan.setFont(FONT_LABEL);
        pnlDoiSoat.add(lblNgayNhan, gbc);
        
        gbc.gridx = 1;
        txtNgayNhan = createEditableTextField(); // <--- Được phép nhập tự do
        pnlDoiSoat.add(txtNgayNhan, gbc);

        // Dòng 4: Ngày trả thực tế (Nơi nhân viên nhập ngày hoặc số tiền tạm tính để đồng bộ xuống dưới)
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel lblNgayTra = new JLabel("Ngày trả xe thực tế / Nhập số tiền gốc:");
        lblNgayTra.setFont(FONT_LABEL);
        pnlDoiSoat.add(lblNgayTra, gbc);
        
        gbc.gridx = 1;
        txtNgayTraThucTe = createEditableTextField(); // <--- Được phép nhập tự do
        pnlDoiSoat.add(txtNgayTraThucTe, gbc);

        pnlMainContent.add(pnlDoiSoat);
        pnlMainContent.add(Box.createVerticalStrut(20)); 

        // --- VÙNG 2: HÓA ĐƠN & TÍNH TOÁN THANH TOÁN (CẤM NHẬP HỒNG/XÁM) ---
        JPanel pnlThanhToan = new JPanel(new GridBagLayout());
        pnlThanhToan.setBackground(Color.WHITE);
        pnlThanhToan.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true), 
                " Chi tiết hóa đơn tài chính ", 
                0, 0, FONT_TITLE_ZONE, COLOR_TEXT_DARK));

        GridBagConstraints gbcPay = new GridBagConstraints();
        gbcPay.insets = new Insets(12, 20, 12, 20);
        gbcPay.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 0: Tiền thuê gốc (Khóa - lấy tự động dữ liệu từ trên truyền xuống)
        gbcPay.gridx = 0; gbcPay.gridy = 0; gbcPay.weightx = 0.3;
        JLabel lblGoc = new JLabel("Tổng tiền thuê xe gốc:");
        lblGoc.setFont(FONT_LABEL);
        pnlThanhToan.add(lblGoc, gbcPay);
        
        gbcPay.gridx = 1; gbcPay.weightx = 0.7;
        txtTienThueGoc = createReadOnlyTextField(); // <--- CẤM NHẬP
        pnlThanhToan.add(txtTienThueGoc, gbcPay);

        // Dòng 1: Phí phát sinh (Khóa - không cho sửa bừa bãi ở phần hóa đơn)
        gbcPay.gridx = 0; gbcPay.gridy = 1;
        JLabel lblPhatSinh = new JLabel("Chi phí phát sinh thêm (nếu có):");
        lblPhatSinh.setFont(FONT_LABEL);
        pnlThanhToan.add(lblPhatSinh, gbcPay);
        
        gbcPay.gridx = 1;
        txtPhiPhatSinh = createReadOnlyTextField(); // <--- CẤM NHẬP
        txtPhiPhatSinh.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        txtPhiPhatSinh.setForeground(new Color(192, 57, 43)); 
        pnlThanhToan.add(txtPhiPhatSinh, gbcPay);

        // Dòng 2: Tổng số tiền cuối cùng phải trả (Khóa hệ thống)
        gbcPay.gridx = 0; gbcPay.gridy = 2;
        JLabel lblTong = new JLabel("TỔNG TIỀN CẦN THANH TOÁN:");
        lblTong.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        pnlThanhToan.add(lblTong, gbcPay);
        
        gbcPay.gridx = 1;
        txtTongThanhToan = createReadOnlyTextField(); // <--- CẤM NHẬP
        txtTongThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 20)); 
        txtTongThanhToan.setForeground(new Color(41, 128, 185)); 
        pnlThanhToan.add(txtTongThanhToan, gbcPay);

        pnlMainContent.add(pnlThanhToan);
        // ==========================================================
// VÙNG 3: QR THANH TOÁN
// ==========================================================

JPanel pnlQR = new JPanel(new BorderLayout());
pnlQR.setBackground(Color.WHITE);

pnlQR.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(
                new Color(210, 215, 225),
                1,
                true),
        " Thanh toán QR động ",
        0,
        0,
        FONT_TITLE_ZONE,
        COLOR_TEXT_DARK));

pnlQR.setPreferredSize(new Dimension(300, 350));

lblQRCode = new JLabel(
        "QR THANH TOÁN",
        SwingConstants.CENTER);

lblQRCode.setFont(
        new Font("Segoe UI", Font.BOLD, 18));

lblQRCode.setForeground(Color.GRAY);

lblQRCode.setPreferredSize(
        new Dimension(280, 280));

btnThanhToanQR =
        createStyledButton(
                "Tạo mã QR thanh toán",
                new Color(52, 152, 219));

JPanel pnlQRBottom =
        new JPanel(new FlowLayout(
                FlowLayout.CENTER));

pnlQRBottom.setBackground(Color.WHITE);

pnlQRBottom.add(btnThanhToanQR);

pnlQR.add(lblQRCode, BorderLayout.CENTER);
pnlQR.add(pnlQRBottom, BorderLayout.SOUTH);

pnlMainContent.add(Box.createVerticalStrut(20));
pnlMainContent.add(pnlQR);

        // TÍNH NĂNG CUỘN
        JScrollPane scrMainScroll = new JScrollPane(pnlMainContent);
        scrMainScroll.setBorder(null); 
        scrMainScroll.setBackground(COLOR_BG);
        scrMainScroll.getViewport().setBackground(COLOR_BG);
        scrMainScroll.getVerticalScrollBar().setUnitIncrement(16); 
        
        add(scrMainScroll, BorderLayout.CENTER);

        // =========================================================================
        // PHẦN NAM (SOUTH): NÚT THAO TÁC LỚN
        // =========================================================================
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        pnlBottom.setBackground(COLOR_BG);

        btnXacNhanTra = createStyledButton("Xác nhận trả xe & Thanh toán hóa đơn", new Color(46, 204, 113));
        btnXacNhanTra.setFont(new Font("Segoe UI", Font.BOLD, 16)); 
        pnlBottom.add(btnXacNhanTra);
        
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void initEvents() {
        // LẮNG NGHE SỰ KIỆN: Khi người dùng gõ nội dung vào ô "Ngày trả xe thực tế / Số tiền gốc" ở vùng trên
        txtNgayTraThucTe.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { dongBoGiaoDienHoaDon(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { dongBoGiaoDienHoaDon(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { dongBoGiaoDienHoaDon(); }
        });

        // Sự kiện đổi mã phiếu Combobox (Demo)
        cboMaPhieu.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (cboMaPhieu.getSelectedIndex() > 0) {
                    txtBienSo.setText("51H-123.45");
                    txtTenXe.setText("Toyota Camry 2023");
                    txtNgayNhan.setText("20/05/2026 08:00");
                    txtNgayTraThucTe.setText("1600000"); // Gõ số vào đây để đồng bộ tính toán tự động
                } else {
                    xoaTrangForm();
                }
            }
        });// ==========================================================
// SỰ KIỆN TẠO QR THANH TOÁN
// ==========================================================

btnThanhToanQR.addActionListener(e -> {

    try {

        String tongTienText =
                txtTongThanhToan
                        .getText()
                        .replaceAll("[^\\d]", "");

        if (tongTienText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vui lòng tính tiền trước!");

            return;
        }

        double tongTien =
                Double.parseDouble(tongTienText);

        String maPhieu =
                cboMaPhieu.getSelectedItem()
                        .toString();

        // ==================================================
        // THÔNG TIN VIETQR
        // ==================================================

        String bankID = "970415";

        String accountNumber = "123456789";

        String qrUrl =
                "https://img.vietqr.io/image/"
                        + bankID
                        + "-"
                        + accountNumber
                        + "-compact2.jpg?amount="
                        + (int) tongTien
                        + "&addInfo=ThanhToan_"
                        + maPhieu;

        ImageIcon qrIcon =
                new ImageIcon(new URL(qrUrl));

        Image img =
                qrIcon.getImage()
                        .getScaledInstance(
                                260,
                                260,
                                Image.SCALE_SMOOTH);

        lblQRCode.setText("");

        lblQRCode.setIcon(
                new ImageIcon(img));

        JOptionPane.showMessageDialog(
                this,
                "Đã tạo mã QR thanh toán!");

    } catch (Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Không thể tạo QR!");
    }
});

        // Nút Xác nhận thanh toán
        btnXacNhanTra.addActionListener(e -> {
            if (cboMaPhieu.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một mã phiếu đang thuê để tiến hành đối soát!", "Cảnh báo nghiệp vụ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "Hệ thống đã nhận đủ " + txtTongThanhToan.getText() + ".\nĐã đóng hóa đơn và cập nhật trạng thái xe thành Sẵn Sàng!");
            xoaTrangForm();
            cboMaPhieu.setSelectedIndex(0);
        });
    }

    // Hàm đồng bộ dữ liệu tự động từ vùng Đối soát nhảy xuống vùng Hóa Đơn
    private void dongBoGiaoDienHoaDon() {
        SwingUtilities.invokeLater(() -> {
            try {
                String textVungTren = txtNgayTraThucTe.getText().trim();
                
                // Lọc sạch chỉ lấy các chữ số nguyên
                String chiSo = textVungTren.replaceAll("[^\\d]", "");
                
                if (!chiSo.isEmpty()) {
                    double tienGoc = Double.parseDouble(chiSo);
                    double phatSinh = 200000; // Đặt sẵn một khoản phí phạt cố định làm demo hệ thống tự sinh
                    double tongTien = tienGoc + phatSinh;

                    // Tự động gán chữ dữ liệu hiển thị trực quan xuống dưới phần Hóa đơn
                    txtTienThueGoc.setText(String.format("%,.0f đ", tienGoc));
                    txtPhiPhatSinh.setText(String.format("%,.0f đ", phatSinh));
                    txtTongThanhToan.setText(String.format("%,.0f đ", tongTien));
                } else {
                    txtTienThueGoc.setText("0 đ");
                    txtPhiPhatSinh.setText("0 đ");
                    txtTongThanhToan.setText("0 đ");
                }
            } catch (Exception ex) {
                // Tránh ném lỗi khi người dùng đang trong tiến trình gõ phím nửa chừng
            }
        });
    }

    private void loadThongKeDemo() {
        lblHoanTatValue.setText("12 xe");
        lblChuaHoanTatValue.setText("5 xe");
        lblDoanhThuValue.setText("16.800.000 đ");
    }

    private void xoaTrangForm() {
        txtBienSo.setText("");
        txtTenXe.setText("");
        txtNgayNhan.setText("");
        txtNgayTraThucTe.setText("");
        txtTienThueGoc.setText("");
        txtPhiPhatSinh.setText("");
        txtTongThanhToan.setText("");
        lblQRCode.setIcon(null);

lblQRCode.setText("QR THANH TOÁN");
    }

    // Ô CHỈ ĐỌC (Dùng cho vùng Hóa đơn - Cấm sửa đổi hoàn toàn)
    private JTextField createReadOnlyTextField() {
        JTextField txt = new JTextField();
        txt.setFont(FONT_INPUT);
        txt.setEditable(false); // <--- KHÓA CẤM NHẬP KHÔNG CHO ĐÈ CHỮ
        txt.setBackground(new Color(242, 244, 245)); // Nền màu xám nhạt báo hiệu ô bị khóa hệ thống
        txt.setPreferredSize(new Dimension(0, 38));
        return txt;
    }

    // Ô CHO PHÉP NHẬP (Dùng cho vùng Đối soát dữ liệu)
    private JTextField createEditableTextField() {
        JTextField txt = new JTextField();
        txt.setFont(FONT_INPUT);
        txt.setEditable(true); // <--- MỞ KHÓA CHO PHÉP GÕ CHỮ
        txt.setBackground(Color.WHITE); // Nền màu trắng tinh để nhập liệu
        txt.setPreferredSize(new Dimension(0, 38));
        return txt;
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

        JLabel lblCardTitle = new JLabel(title);
        lblCardTitle.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblCardTitle.setForeground(new Color(240, 240, 240));

        JLabel lblValue = new JLabel(defaultValue);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
        lblValue.setForeground(Color.WHITE);

        card.add(lblCardTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        card.putClientProperty("valueLabel", lblValue);

        return card;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
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
