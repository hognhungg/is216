package com.thuexe.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DlgLoaiXe extends JDialog {
    private JTextField txtMaLoai, txtTenLoai, txtNhienLieu, txtGiaNgay, txtGiaGio;
    private JButton btnLuu, btnHuy;
    private boolean isConfirmed = false;
    private String actionType; // "THEM", "SUA", "XOA"

    public DlgLoaiXe(Window owner, String title, String actionType) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        this.actionType = actionType;
        initComponents();
        configureMode();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        setSize(450, 380);
        setLayout(new BorderLayout());
        setResizable(false);

        // --- Panel Nội Dung Nhập Liệu ---
        JPanel pnlContent = new JPanel(new GridLayout(5, 2, 10, 15));
        pnlContent.setBorder(new EmptyBorder(20, 25, 20, 25));
        pnlContent.setBackground(Color.WHITE);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        pnlContent.add(createStyledLabel("Mã loại xe:", fontLabel));
        txtMaLoai = new JTextField(); txtMaLoai.setFont(fontText);
        txtMaLoai.setEditable(false);
        txtMaLoai.setBackground(new Color(240, 242, 245));
        pnlContent.add(txtMaLoai);

        pnlContent.add(createStyledLabel("Tên loại xe:", fontLabel));
        txtTenLoai = new JTextField(); txtTenLoai.setFont(fontText);
        pnlContent.add(txtTenLoai);

        pnlContent.add(createStyledLabel("Nhiên liệu:", fontLabel));
        txtNhienLieu = new JTextField(); txtNhienLieu.setFont(fontText);
        pnlContent.add(txtNhienLieu);

        pnlContent.add(createStyledLabel("Giá thuê ngày:", fontLabel));
        txtGiaNgay = new JTextField(); txtGiaNgay.setFont(fontText);
        pnlContent.add(txtGiaNgay);

        pnlContent.add(createStyledLabel("Giá thuê giờ:", fontLabel));
        txtGiaGio = new JTextField(); txtGiaGio.setFont(fontText);
        pnlContent.add(txtGiaGio);

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
            // Kiểm tra rỗng dữ liệu khi Thêm hoặc Sửa
            if (!actionType.equals("XOA")) {
                if (txtTenLoai.getText().trim().isEmpty() || txtNhienLieu.getText().trim().isEmpty() ||
                    txtGiaNgay.getText().trim().isEmpty() || txtGiaGio.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ tất cả các thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // Kiểm tra định dạng số tiền
                try {
                    Double.parseDouble(txtGiaNgay.getText().trim());
                    Double.parseDouble(txtGiaGio.getText().trim());
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Giá thuê ngày và giá thuê giờ phải nhập vào số nguyên hợp lệ!", "Sai định dạng", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            isConfirmed = true;
            dispose();
        });
    }

    private void configureMode() {
        if (actionType.equals("THEM")) {
            txtMaLoai.setText("(Tự động tăng)");
        } else if (actionType.equals("XOA")) {
            // Chế độ xóa: Khóa dữ liệu hiển thị, chuyển nút Lưu thành màu đỏ nguy hiểm
            txtTenLoai.setEditable(false);
            txtNhienLieu.setEditable(false);
            txtGiaNgay.setEditable(false);
            txtGiaGio.setEditable(false);
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
        if (data == null || data.length < 5) return;
        txtMaLoai.setText(data[0] != null ? data[0].toString() : "");
        txtTenLoai.setText(data[1] != null ? data[1].toString() : "");
        txtNhienLieu.setText(data[2] != null ? data[2].toString() : "");
        txtGiaNgay.setText(data[3] != null ? data[3].toString() : "");
        txtGiaGio.setText(data[4] != null ? data[4].toString() : "");
    }

    public Object[] getFormValues() {
        return new Object[] {
            txtMaLoai.getText(),
            txtTenLoai.getText().trim(),
            txtNhienLieu.getText().trim(),
            txtGiaNgay.getText().trim(),
            txtGiaGio.getText().trim()
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