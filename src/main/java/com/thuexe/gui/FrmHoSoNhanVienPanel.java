package com.thuexe.gui;

import com.thuexe.dao.NhanVienDAO;
import com.thuexe.dto.NhanVienDTO;

import javax.swing.*;
import java.awt.*;

public class FrmHoSoNhanVienPanel extends JPanel {

    private JTextField txtMa;
    private JTextField txtTen;
    private JTextField txtCCCD;
    private JTextField txtSDT;
    private JTextField txtEmail;

    private JButton btnEdit;

    private boolean editing = false;

    public FrmHoSoNhanVienPanel() {

        setLayout(new BorderLayout());

        JLabel lblTitle =
                new JLabel("HỒ SƠ NHÂN VIÊN");

        lblTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 28));

        lblTitle.setBorder(
                BorderFactory.createEmptyBorder(20,20,20,20));

        add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel();

        form.setLayout(
                new GridLayout(6,2,15,15));

        form.setBorder(
                BorderFactory.createEmptyBorder(30,30,30,30));

        txtMa = new JTextField();
        txtTen = new JTextField();
        txtCCCD = new JTextField();
        txtSDT = new JTextField();
        txtEmail = new JTextField();

        txtMa.setEditable(false);
        txtTen.setEditable(false);
        txtCCCD.setEditable(false);

        txtSDT.setEditable(false);
        txtEmail.setEditable(false);

        form.add(new JLabel("Mã nhân viên"));
        form.add(txtMa);

        form.add(new JLabel("Họ tên"));
        form.add(txtTen);

        form.add(new JLabel("CCCD"));
        form.add(txtCCCD);

        form.add(new JLabel("SĐT"));
        form.add(txtSDT);

        form.add(new JLabel("Email"));
        form.add(txtEmail);

        btnEdit =
                new JButton("Chỉnh sửa hồ sơ");

        form.add(btnEdit);

        add(form, BorderLayout.CENTER);

        loadProfile();

        event();
    }

    private void loadProfile() {

        NhanVienDAO dao =
                new NhanVienDAO();

        // test tạm
        NhanVienDTO nv =
                dao.getProfileByMaChuThe(1);

        if (nv != null) {

            txtMa.setText(
                    String.valueOf(
                            nv.getMaNhanVien()));

            txtTen.setText(
                    nv.getHoTenNV());

            txtCCCD.setText(
                    nv.getCccd());

            txtSDT.setText(
                    nv.getSdt());

            txtEmail.setText(
                    nv.getEmail());
        }
    }

    private void event() {

        btnEdit.addActionListener(e -> {

            if (!editing) {

                txtSDT.setEditable(true);
                txtEmail.setEditable(true);

                btnEdit.setText("Lưu thay đổi");

            } else {

                NhanVienDTO nv =
                        new NhanVienDTO();

                nv.setMaNhanVien(
                        Integer.parseInt(
                                txtMa.getText()));

                nv.setHoTenNV(
                        txtTen.getText());

                nv.setCccd(
                        txtCCCD.getText());

                nv.setSdt(
                        txtSDT.getText());

                nv.setEmail(
                        txtEmail.getText());

                NhanVienDAO dao =
                        new NhanVienDAO();

                boolean result =
                        dao.update(nv);

                if (result) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Cập nhật thành công!");
                }

                txtSDT.setEditable(false);
                txtEmail.setEditable(false);

                btnEdit.setText("Chỉnh sửa hồ sơ");
            }

            editing = !editing;
        });
    }
}
