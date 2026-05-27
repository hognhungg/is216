package com.thuexe.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DlgKhachHang extends JDialog {
    private JTextField txtMa, txtTen, txtCCCD, txtSoBangLai, txtSDT, txtNgaySinh, txtDiaChi, txtMaChuThe;
    private JComboBox<String> cbGioiTinh;
    private JButton btnXacNhan, btnHuy;
    private boolean isConfirmed = false;
    private String mode; // "THEM", "SUA", "XOA"
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public DlgKhachHang(Window parent, String title, String mode) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        this.mode = mode;
        initComponents();
        adjustUIByMode();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel pnlMain = new JPanel(new BorderLayout(0, 20));
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(new EmptyBorder(20, 22, 20, 22));

        // Grid 8 hàng 2 cột chứa các trường thông tin
        JPanel pnlForm = new JPanel(new GridLayout(8, 2, 10, 15));
        pnlForm.setBackground(Color.WHITE);

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 13);
        Font fontText = new Font("Segoe UI", Font.PLAIN, 14);

        pnlForm.add(createStyledLabel("Mã khách hàng:", fontLabel));
        txtMa = createStyledTextField(fontText, false);
        txtMa.setBackground(new Color(240, 242, 245));
        pnlForm.add(txtMa);

        pnlForm.add(createStyledLabel("Họ và tên *:", fontLabel));
        txtTen = createStyledTextField(fontText, true);
        pnlForm.add(txtTen);

        pnlForm.add(createStyledLabel("Số CCCD (12 số) *:", fontLabel));
        txtCCCD = createStyledTextField(fontText, true);
        pnlForm.add(txtCCCD);

        pnlForm.add(createStyledLabel("Số bằng lái:", fontLabel));
        txtSoBangLai = createStyledTextField(fontText, true);
        pnlForm.add(txtSoBangLai);

        pnlForm.add(createStyledLabel("Số điện thoại *:", fontLabel));
        txtSDT = createStyledTextField(fontText, true);
        pnlForm.add(txtSDT);

        pnlForm.add(createStyledLabel("Giới tính:", fontLabel));
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cbGioiTinh.setFont(fontText);
        pnlForm.add(cbGioiTinh);

        pnlForm.add(createStyledLabel("Ngày sinh (dd/mm/yyyy):", fontLabel));
        txtNgaySinh = createStyledTextField(fontText, true);
        txtNgaySinh.setText(sdf.format(new Date())); // Mặc định ngày hôm nay
        pnlForm.add(txtNgaySinh);

        pnlForm.add(createStyledLabel("Địa chỉ cư trú:", fontLabel));
        txtDiaChi = createStyledTextField(fontText, true);
        pnlForm.add(txtDiaChi);

        pnlMain.add(pnlForm, BorderLayout.CENTER);

        // Thanh công cụ nút bấm phía dưới
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        pnlButtons.setBackground(Color.WHITE);

        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setBackground(mode.equals("XOA") ? new Color(190, 50, 50) : new Color(74, 137, 95));
        
        btnHuy = new JButton("Hủy bỏ");
        btnHuy.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        pnlButtons.add(btnXacNhan);
        pnlButtons.add(btnHuy);
        pnlMain.add(pnlButtons, BorderLayout.SOUTH);
        add(pnlMain);

        // Events
        btnXacNhan.addActionListener(e -> {
            if (validateForm()) {
                isConfirmed = true;
                dispose();
            }
        });
        btnHuy.addActionListener(e -> dispose());
    }

    private void adjustUIByMode() {
        if ("XOA".equals(mode)) {
            txtTen.setEditable(false);
            txtCCCD.setEditable(false);
            txtSoBangLai.setEditable(false);
            txtSDT.setEditable(false);
            cbGioiTinh.setEnabled(false);
            txtNgaySinh.setEditable(false);
            txtDiaChi.setEditable(false);
            btnXacNhan.setText("Xác nhận Xóa");
        }
    }

    private boolean validateForm() {
        if ("XOA".equals(mode)) return true;
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!"); return false;
        }
        if (txtCCCD.getText().trim().length() != 12) {
            JOptionPane.showMessageDialog(this, "CCCD phải nhập đúng 12 chữ số!"); return false;
        }
        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!"); return false;
        }
        try {
            sdf.parse(txtNgaySinh.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không đúng định dạng dd/MM/yyyy!"); return false;
        }
        return true;
    }

    public void setFormValues(Object[] values) {
        txtMa.setText(values[0].toString());
        txtTen.setText(values[1].toString());
        txtCCCD.setText(values[2].toString());
        txtSoBangLai.setText(values[3] != null ? values[3].toString() : "");
        txtSDT.setText(values[4].toString());
        cbGioiTinh.setSelectedItem(values[5] != null ? values[5].toString() : "Nam");
        txtNgaySinh.setText(values[6].toString());
        txtDiaChi.setText(values[7] != null ? values[7].toString() : "");
    }

    public Object[] getFormValues() {
        return new Object[] {
            txtMa.getText().trim(),
            txtTen.getText().trim(),
            txtCCCD.getText().trim(),
            txtSoBangLai.getText().trim(),
            txtSDT.getText().trim(),
            cbGioiTinh.getSelectedItem().toString(),
            txtNgaySinh.getText().trim(),
            txtDiaChi.getText().trim()
        };
    }

    public boolean isConfirmed() { return isConfirmed; }
    private JLabel createStyledLabel(String t, Font f) { JLabel l = new JLabel(t); l.setFont(f); return l; }
    private JTextField createStyledTextField(Font f, boolean ed) { JTextField t = new JTextField(); t.setFont(f); t.setEditable(ed); return t; }
}