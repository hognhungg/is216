package com.thuexe.gui;

import com.thuexe.bus.KhachHangBUS;
import com.thuexe.dto.KhachHangDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmKhachHang extends JFrame {
    private JTextField txtMa, txtTen, txtCCCD, txtSDT, txtDiaChi;
    private JTable tblKhachHang;
    private DefaultTableModel model;
    private KhachHangBUS khBus = new KhachHangBUS();

    public FrmKhachHang() {
        setTitle("Quản Lý Khách Hàng");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Panel Nhập liệu (North) ---
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        
        pnlInput.add(new JLabel("Mã KH:"));
        txtMa = new JTextField(); txtMa.setEditable(false);
        pnlInput.add(txtMa);

        pnlInput.add(new JLabel("Họ tên:"));
        txtTen = new JTextField();
        pnlInput.add(txtTen);

        pnlInput.add(new JLabel("CCCD:"));
        txtCCCD = new JTextField();
        pnlInput.add(txtCCCD);

        pnlInput.add(new JLabel("Số ĐT:"));
        txtSDT = new JTextField();
        pnlInput.add(txtSDT);

        add(pnlInput, BorderLayout.NORTH);

        // --- Panel Bảng (Center) ---
        String[] headers = {"Mã KH", "Họ Tên", "CCCD", "SĐT", "Địa Chỉ"};
        model = new DefaultTableModel(headers, 0);
        tblKhachHang = new JTable(model);
        add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        // --- Panel Nút bấm (South) ---
        JPanel pnlBtns = new JPanel();
        JButton btnSua = new JButton("Cập nhật (Sửa)");
        JButton btnLamMoi = new JButton("Làm mới danh sách");
        
        pnlBtns.add(btnSua);
        pnlBtns.add(btnLamMoi);
        add(pnlBtns, BorderLayout.SOUTH);

        // --- Sự kiện ---
        btnLamMoi.addActionListener(e -> loadData());
        
        btnSua.addActionListener(e -> {
            KhachHangDTO kh = new KhachHangDTO();
            kh.setMaKhachHang(Integer.parseInt(txtMa.getText()));
            kh.setHoTen(txtTen.getText());
            kh.setCccd(txtCCCD.getText());
            kh.setSdt(txtSDT.getText());
            
            String res = khBus.updateKhachHang(kh);
            if(res.equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, res);
            }
        });

        // Click vào bảng hiện lên textfield
        tblKhachHang.getSelectionModel().addListSelectionListener(e -> {
            int row = tblKhachHang.getSelectedRow();
            if (row >= 0) {
                txtMa.setText(model.getValueAt(row, 0).toString());
                txtTen.setText(model.getValueAt(row, 1).toString());
                txtCCCD.setText(model.getValueAt(row, 2).toString());
                txtSDT.setText(model.getValueAt(row, 3).toString());
            }
        });

        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        List<KhachHangDTO> list = khBus.getAllKhachHang();
        for (KhachHangDTO kh : list) {
            model.addRow(new Object[]{kh.getMaKhachHang(), kh.getHoTen(), kh.getCccd(), kh.getSdt(), kh.getDiaChi()});
        }
    }
}