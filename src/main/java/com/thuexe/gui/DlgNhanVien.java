package com.thuexe.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DlgNhanVien extends JDialog {
    private JTextField txtMaNV, txtTenNV, txtCCCD, txtSDT, txtEmail, txtMaChuThe, txtMaDoanhNghiep;
    private JButton btnLuu, btnHuy;
    private boolean isConfirmed = false;
    private String actionType; // "THEM", "SUA", "XOA"

    public DlgNhanVien(Window owner, String title, String actionType) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.actionType = actionType;
        initComponents();
        configureMode();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setSize(450, 480);
        setLayout(new BorderLayout());
        setResizable(false);

        // --- Panel Nội Dung Nhập Liệu ---
        JPanel pnlContent = new JPanel(new GridLayout(7, 2, 10, 15));
        pnlContent.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlContent.setBackground(Color.WHITE);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        pnlContent.add(createStyledLabel("Mã nhân viên:", fontLabel));
        txtMaNV = new JTextField(); txtMaNV.setFont(fontText);
        txtMaNV.setEditable(false);
        txtMaNV.setBackground(new Color(240, 242, 245));
        pnlContent.add(txtMaNV);

        pnlContent.add(createStyledLabel("Họ tên NV:", fontLabel));
        txtTenNV = new JTextField(); txtTenNV.setFont(fontText);
        pnlContent.add(txtTenNV);

        pnlContent.add(createStyledLabel("Số CCCD:", fontLabel));
        txtCCCD = new JTextField(); txtCCCD.setFont(fontText);
        pnlContent.add(txtCCCD);

        pnlContent.add(createStyledLabel("Số điện thoại:", fontLabel));
        txtSDT = new JTextField(); txtSDT.setFont(fontText);
        pnlContent.add(txtSDT);

        pnlContent.add(createStyledLabel("Email liên hệ:", fontLabel));
        txtEmail = new JTextField(); txtEmail.setFont(fontText);
        pnlContent.add(txtEmail);

        pnlContent.add(createStyledLabel("Mã chủ thể:", fontLabel));
        txtMaChuThe = new JTextField(); txtMaChuThe.setFont(fontText);
        pnlContent.add(txtMaChuThe);

        pnlContent.add(createStyledLabel("Mã doanh nghiệp:", fontLabel));
        txtMaDoanhNghiep = new JTextField(); txtMaDoanhNghiep.setFont(fontText);
        pnlContent.add(txtMaDoanhNghiep);

        add(pnlContent, BorderLayout.CENTER);

        // --- Panel Nút Bấm Xử Lý ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        pnlButtons.setBackground(new Color(245, 246, 248));
        pnlButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 223, 230)));

        btnLuu = createStyledButton("Xác nhận", new Color(74, 137, 95));
        btnHuy = createStyledButton("Hủy bỏ", new Color(140, 145, 155));

        pnlButtons.add(btnHuy);
        pnlButtons.add(btnLuu);
        add(pnlButtons, BorderLayout.SOUTH);

        // --- Sự Kiện Nút Bấm ---
        btnHuy.addActionListener(e -> dispose());

        btnLuu.addActionListener(e -> {
            if (!actionType.equals("XOA") && (txtTenNV.getText().trim().isEmpty() || txtCCCD.getText().trim().isEmpty())) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ các thông tin bắt buộc!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            isConfirmed = true;
            dispose();
        });
    }

    private void configureMode() {
        if (actionType.equals("THEM")) {
            txtMaNV.setText("(Tự động tăng)");
        } else if (actionType.equals("XOA")) {
            // Chế độ xóa: Khóa toàn bộ ô nhập liệu để người dùng chỉ xem và xác nhận xóa
            txtTenNV.setEditable(false);
            txtCCCD.setEditable(false);
            txtSDT.setEditable(false);
            txtEmail.setEditable(false);
            txtMaChuThe.setEditable(false);
            txtMaDoanhNghiep.setEditable(false);
            btnLuu.setBackground(new Color(190, 50, 50));
        }
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(new Color(40, 44, 52));
        return lbl;
    }

    public void setFormValues(Object[] data) {
        if (data == null || data.length < 7) return;
        txtMaNV.setText(data[0] != null ? data[0].toString() : "");
        txtTenNV.setText(data[1] != null ? data[1].toString() : "");
        txtCCCD.setText(data[2] != null ? data[2].toString() : "");
        txtSDT.setText(data[3] != null ? data[3].toString() : "");
        txtEmail.setText(data[4] != null ? data[4].toString() : "");
        txtMaChuThe.setText(data[5] != null ? data[5].toString() : "");
        txtMaDoanhNghiep.setText(data[6] != null ? data[6].toString() : "");
    }

    public Object[] getFormValues() {
        return new Object[] {
            txtMaNV.getText(),
            txtTenNV.getText(),
            txtCCCD.getText(),
            txtSDT.getText(),
            txtEmail.getText(),
            txtMaChuThe.getText(),
            txtMaDoanhNghiep.getText()
        };
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 4, 4);
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