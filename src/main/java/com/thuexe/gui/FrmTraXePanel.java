package com.thuexe.gui;

import javax.swing.*;
import java.awt.*;

public class FrmTraXePanel extends JPanel {

    public FrmTraXePanel() {

        setLayout(new BorderLayout());

        JLabel lblTitle =
                new JLabel("TRẢ XE & THANH TOÁN");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 28));

        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(20,20,20,20));

        add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel();

        form.setLayout(
                new GridLayout(5,2,15,15));

        form.setBorder(
                BorderFactory.createEmptyBorder(30,30,30,30));

        JTextField txtTien =
                new JTextField();

        JTextField txtPhi =
                new JTextField();

        JTextField txtVoucher =
                new JTextField();

        JTextField txtTong =
                new JTextField();

        JButton btnTinh =
                new JButton("Tính tiền");

        JButton btnQR =
                new JButton("Thanh toán QR");

        form.add(new JLabel("Tiền gốc"));
        form.add(txtTien);

        form.add(new JLabel("Phí phát sinh"));
        form.add(txtPhi);

        form.add(new JLabel("Voucher"));
        form.add(txtVoucher);

        form.add(new JLabel("Tổng tiền"));
        form.add(txtTong);

        form.add(btnTinh);
        form.add(btnQR);

        add(form, BorderLayout.CENTER);

        btnTinh.addActionListener(e -> {

            double tong =
                    Double.parseDouble(txtTien.getText())
                    + Double.parseDouble(txtPhi.getText())
                    - Double.parseDouble(txtVoucher.getText());

            txtTong.setText(
                    String.valueOf(tong));
        });

        btnQR.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Đã tạo mã QR thanh toán!");
        });
    }
}
