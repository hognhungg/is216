package com.thuexe.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.thuexe.bus.TaiKhoanBUS;

public class DlgDoiMatKhau extends JDialog {
    private JPasswordField txtOldPass, txtNewPass, txtConfirmPass;
    private JButton btnXacNhan, btnHuy;
    private String username;
    private String currentDbHash; // Hash mật khẩu hiện tại lấy từ session đăng nhập

    public DlgDoiMatKhau(Frame parent, String username, String currentDbHash) {
        super(parent, "Đổi mật khẩu tài khoản", true);
        this.username = username;
        this.currentDbHash = currentDbHash;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel pnlInput = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlInput.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        pnlInput.add(new JLabel("Mật khẩu cũ:"));
        txtOldPass = new JPasswordField();
        pnlInput.add(txtOldPass);
        
        pnlInput.add(new JLabel("Mật khẩu mới:"));
        txtNewPass = new JPasswordField();
        pnlInput.add(txtNewPass);
        
        pnlInput.add(new JLabel("Nhập lại mật khẩu mới:"));
        txtConfirmPass = new JPasswordField();
        pnlInput.add(txtConfirmPass);
        
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnXacNhan = new JButton("Xác nhận");
        btnHuy = new JButton("Hủy bỏ");
        pnlButtons.add(btnXacNhan);
        pnlButtons.add(btnHuy);
        
        add(pnlInput, BorderLayout.CENTER);
        add(pnlButtons, BorderLayout.SOUTH);
        
        btnHuy.addActionListener(e -> dispose());
        
        btnXacNhan.addActionListener(e -> {
            String oldPass = new String(txtOldPass.getPassword());
            String newPass = new String(txtNewPass.getPassword());
            String confirm = new String(txtConfirmPass.getPassword());
            
            TaiKhoanBUS tkBUS = new TaiKhoanBUS();
            // Tận dụng chính xác hàm changePassword xử lý logic mã hóa Base64 sẵn có của bạn
            String result = tkBUS.changePassword(username, oldPass, newPass, confirm, currentDbHash);
            
            if ("SUCCESS".equals(result)) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, result, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        pack();
        setSize(360, 200);
        setLocationRelativeTo(getParent());
    }
}