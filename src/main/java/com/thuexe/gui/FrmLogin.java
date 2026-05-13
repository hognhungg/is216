package com.thuexe.gui;

import com.thuexe.dao.TaiKhoanDAO;
import com.thuexe.dto.TaiKhoanDTO;
import javax.swing.*;
import java.awt.*;

public class FrmLogin extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JCheckBox chkShow;
    private JButton btnLogin, btnExit; // Đã xóa btnRegister

    public static TaiKhoanDTO userLogged = null;

    public FrmLogin() {
        setTitle("Đăng nhập hệ thống thuê xe");
        setSize(400, 280); // Chỉnh lại chiều cao cho cân đối
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // ===== PANEL CENTER =====
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUser = new JTextField();
        panel.add(txtUser, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1;
        txtPass = new JPasswordField();
        panel.add(txtPass, gbc);

        // Show password
        gbc.gridx = 1; gbc.gridy = 2;
        chkShow = new JCheckBox("Hiện mật khẩu");
        panel.add(chkShow, gbc);

        add(panel, BorderLayout.CENTER);

        // ===== PANEL BUTTON =====
        JPanel panelBtn = new JPanel();
        btnLogin = new JButton("Đăng nhập");
        btnExit = new JButton("Thoát");

        // Cài đặt kích thước nút cho đều nhau
        btnLogin.setPreferredSize(new Dimension(100, 30));
        btnExit.setPreferredSize(new Dimension(100, 30));

        panelBtn.add(btnLogin);
        panelBtn.add(btnExit);
        panelBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(panelBtn, BorderLayout.SOUTH);

        // ===== EVENTS =====
        // Hiện/Ẩn mật khẩu
        chkShow.addActionListener(e -> {
            if (chkShow.isSelected()) txtPass.setEchoChar((char) 0);
            else txtPass.setEchoChar('•');
        });

        // Nút đăng nhập
        btnLogin.addActionListener(e -> login());

        // Nút thoát
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword()).trim();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        TaiKhoanDAO dao = new TaiKhoanDAO();
        userLogged = dao.login(user, pass); 

        if (userLogged != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!\nChào mừng " 
                + userLogged.getTenDangNhap() + " [" + userLogged.getVaiTro() + "]");
    
            // 1. Mở Form chính (Giao diện Menu)
            new FrmMain().setVisible(true); 
    
        // 2. Đóng form đăng nhập
        this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}