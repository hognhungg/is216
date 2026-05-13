package com.thuexe.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmNhanVien extends JFrame {
    private JTextField txtMaNV, txtTenNV, txtCCCD, txtSDT, txtEmail;
    private JTable tblNhanVien;
    private DefaultTableModel model;

    public FrmNhanVien() {
        setTitle("Quản Lý Nhân Viên Vận Hành");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel Nhập liệu ---
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Chi tiết nhân viên"));

        pnlInput.add(new JLabel("Mã NV:"));
        txtMaNV = new JTextField(); txtMaNV.setEditable(false);
        pnlInput.add(txtMaNV);

        pnlInput.add(new JLabel("Họ tên NV:"));
        txtTenNV = new JTextField();
        pnlInput.add(txtTenNV);

        pnlInput.add(new JLabel("CCCD:"));
        txtCCCD = new JTextField();
        pnlInput.add(txtCCCD);

        pnlInput.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        pnlInput.add(txtEmail);

        add(pnlInput, BorderLayout.NORTH);

        // --- Panel Bảng ---
        String[] headers = {"Mã NV", "Họ Tên", "CCCD", "SĐT", "Email", "Mã ChuThe"};
        model = new DefaultTableModel(headers, 0);
        tblNhanVien = new JTable(model);
        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        // --- Panel Nút ---
        JPanel pnlBtns = new JPanel();
        JButton btnThem = new JButton("Thêm NV mới");
        JButton btnXoa = new JButton("Nghỉ việc (Xóa)");
        
        pnlBtns.add(btnThem);
        pnlBtns.add(btnXoa);
        add(pnlBtns, BorderLayout.SOUTH);
        
        // Ghi chú: Phần logic xử lý nút bấm tương tự như FrmKhachHang 
        // nhưng ông cần gọi NhanVienBUS.
    }
}