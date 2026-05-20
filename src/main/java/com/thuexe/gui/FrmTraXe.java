package com.thuexe.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class FrmTraXe extends JFrame {
    private JComboBox<String> cboMaPhieu;
    private JTextField txtBienSo, txtTenXe, txtNgayNhan;
    private JButton btnXacNhanTra;

    public FrmTraXe() {
        setTitle("Quy trình trả xe - Đối soát dữ liệu");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Header ---
        JLabel lblHeader = new JLabel("HOÀN TẤT TRẢ XE", JLabel.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblHeader.setForeground(new Color(0, 102, 0));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblHeader, BorderLayout.NORTH);

        // --- Center Panel (Input) ---
        JPanel pnlCenter = new JPanel(new GridBagLayout());
        pnlCenter.setBorder(BorderFactory.createTitledBorder("Thông tin đối soát"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã phiếu thuê
        gbc.gridx = 0; gbc.gridy = 0;
        pnlCenter.add(new JLabel("Mã phiếu thuê (Đang thuê):"), gbc);
        gbc.gridx = 1;
        cboMaPhieu = new JComboBox<>(new String[]{"-- Chọn mã phiếu --", "PT001", "PT002"}); // Demo data
        pnlCenter.add(cboMaPhieu, gbc);

        // Thông tin hiển thị (Read-only)
        gbc.gridx = 0; gbc.gridy = 1;
        pnlCenter.add(new JLabel("Biển số xe:"), gbc);
        gbc.gridx = 1;
        txtBienSo = new JTextField(); txtBienSo.setEditable(false);
        pnlCenter.add(txtBienSo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        pnlCenter.add(new JLabel("Tên xe:"), gbc);
        gbc.gridx = 1;
        txtTenXe = new JTextField(); txtTenXe.setEditable(false);
        pnlCenter.add(txtTenXe, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        pnlCenter.add(new JLabel("Ngày nhận xe:"), gbc);
        gbc.gridx = 1;
        txtNgayNhan = new JTextField(); txtNgayNhan.setEditable(false);
        pnlCenter.add(txtNgayNhan, gbc);

        add(pnlCenter, BorderLayout.CENTER);

        // --- Bottom Panel (Action) ---
        JPanel pnlBottom = new JPanel();
        btnXacNhanTra = new JButton("Xác nhận trả xe");
        btnXacNhanTra.setFont(new Font("Arial", Font.BOLD, 14));
        btnXacNhanTra.setPreferredSize(new Dimension(160, 40));
        pnlBottom.add(btnXacNhanTra);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- Events ---
        // Khi chọn mã phiếu, hiển thị thông tin tương ứng (Demo logic)
        cboMaPhieu.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if(cboMaPhieu.getSelectedIndex() > 0) {
                    txtBienSo.setText("51H-123.45");
                    txtTenXe.setText("Toyota Camry 2023");
                    txtNgayNhan.setText("12/10/2023");
                }
            }
        });

        // Xác nhận trả xe và mở Form đánh giá
        btnXacNhanTra.addActionListener(e -> {
            if (cboMaPhieu.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn mã phiếu thuê!");
                return;
            }
            
            // Bước 1: Thông báo thành công trả xe
            JOptionPane.showMessageDialog(this, "Hệ thống đã ghi nhận trả xe thành công!");
            
            // Bước 2: Hiện JDialog đánh giá
            FrmDanhGia dialog = new FrmDanhGia(this);
            dialog.setVisible(true);
        });
    }
}