package com.thuexe.gui;

import com.thuexe.dao.TaiKhoanDAO;
import com.thuexe.dto.TaiKhoanDTO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;

public class FrmLogin extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JCheckBox chkShow;
    private JButton btnLogin, btnExit;

    // Bảng màu Flat UI hiện đại theo tone màu xanh lá cây
    private static final Color COLOR_PRIMARY = new Color(74, 137, 95);  // Xanh lá chuẩn mẫu
    private static final Color COLOR_TEXT = new Color(50, 50, 50);      // Chữ đen xám dịu mắt
    private static final Color COLOR_INPUT = new Color(245, 247, 244);  // Nền xám nhạt cho ô nhập
    private static final Color COLOR_BG = Color.WHITE;                  // Nền form trắng
    private static final Font FONT_MODERN = new Font("Segoe UI", Font.PLAIN, 14); 

    public static TaiKhoanDTO userLogged = null;

    public FrmLogin() {
        setTitle("Đăng nhập hệ thống thuê xe");
        setSize(750, 400); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false); 

        // Sử dụng GridBagLayout tổng thể để định vị chính xác vị trí
        setLayout(new GridBagLayout());
        GridBagConstraints gbc_main = new GridBagConstraints();

        // ========================================================
        // KHU VỰC 1: NỬA BÊN TRÁI - HIỂN THỊ HÌNH ẢNH (ÉP SÁT VIỀN TRÁI)
        // ========================================================
        gbc_main.gridx = 0; 
        gbc_main.gridy = 0;
        gbc_main.weightx = 0.0; // Không cho phép co giãn tự do làm hở viền
        gbc_main.weighty = 1.0;
        gbc_main.fill = GridBagConstraints.VERTICAL;
        gbc_main.anchor = GridBagConstraints.WEST; // Ép chặt sát rìa mép bên trái
        gbc_main.insets = new Insets(0, 0, 0, 0);  // Xóa bỏ hoàn toàn khoảng đệm biên

        JPanel panelLeft = new JPanel(new BorderLayout());
        panelLeft.setBackground(Color.WHITE);

        // Quét ổ D giữ nguyên gốc 100%
        File imgFile = new File("D:/Java/QuanLyThueXe/src/main/java/com/thuexe/gui/login_banner.png");
        if (!imgFile.exists()) {
            imgFile = new File("D:/Java/QuanLyThueXe/src/main/java/com/thuexe/gui/login_banner.jpg");
        }

        if (imgFile.exists()) {
            try {
                // Điều chỉnh kích thước ảnh chuẩn xác khít khịt với chiều cao khung cửa sổ (375x400)
                Image img = ImageIO.read(imgFile).getScaledInstance(375, 400, Image.SCALE_SMOOTH);
                JLabel lblImage = new JLabel(new ImageIcon(img));
                panelLeft.add(lblImage, BorderLayout.CENTER);
            } catch (Exception e) {
                setFallbackBackground(panelLeft);
            }
        } else {
            setFallbackBackground(panelLeft);
        }
        add(panelLeft, gbc_main);

        // ==========================================
        // KHU VỰC 2: NỬA BÊN PHẢI - GIAO DIỆN FLAT MODERN
        // ==========================================
        gbc_main.gridx = 1; 
        gbc_main.weightx = 1.0;
        gbc_main.fill = GridBagConstraints.BOTH;
        gbc_main.anchor = GridBagConstraints.CENTER;
        
        JPanel panelRight = new JPanel(new GridBagLayout());
        panelRight.setBackground(COLOR_BG);
        panelRight.setBorder(new EmptyBorder(25, 35, 25, 35)); // Cân đối đệm chữ cho thoáng
        add(panelRight, gbc_main);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);

        // Tiêu đề lớn "Xin chào!"
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel lblWelcome = new JLabel("Xin chào !");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(COLOR_TEXT);
        panelRight.add(lblWelcome, gbc);

        // Subtitle nhỏ bên dưới tiêu đề
        gbc.gridy = 1; gbc.insets = new Insets(-5, 0, 20, 0);
        JLabel lblSub = new JLabel("Vui lòng đăng nhập hệ thống dashboard", JLabel.LEFT);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(Color.GRAY);
        panelRight.add(lblSub, gbc);

        // Style viền mỏng phẳng (Flat Border) cho ô nhập liệu
        Border inputBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(225, 228, 224), 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        );

        // Ô nhập: Tên đăng nhập
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 12, 0);
        txtUser = new JTextField();
        setupInputField(txtUser, inputBorder);
        panelRight.add(txtUser, gbc);

        // Ô nhập: Mật khẩu
        gbc.gridy = 3;
        txtPass = new JPasswordField();
        setupInputField(txtPass, inputBorder);
        panelRight.add(txtPass, gbc);

        // Checkbox hiện mật khẩu phong cách Flat
        gbc.gridy = 4; gbc.insets = new Insets(-5, 0, 20, 0);
        chkShow = new JCheckBox("Hiện mật khẩu");
        chkShow.setBackground(COLOR_BG);
        chkShow.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkShow.setForeground(Color.GRAY);
        chkShow.setFocusPainted(false);
        panelRight.add(chkShow, gbc);

        // Nút bấm: Đăng nhập (Màu xanh lá Flat, bo góc)
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 8, 0);
        btnLogin = new JButton("Đăng nhập");
        setupPrimaryButton(btnLogin);
        panelRight.add(btnLogin, gbc);
        
        // Nút bấm: Thoát (Nền trắng viền mỏng)
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 0, 0);
        btnExit = new JButton("Thoát");
        setupExitButton(btnExit);
        panelRight.add(btnExit, gbc);

        // ==========================================
        // SỰ KIỆN ĐIỀU HƯỚNG
        // ==========================================
        chkShow.addActionListener(e -> {
            if (chkShow.isSelected()) txtPass.setEchoChar((char) 0);
            else txtPass.setEchoChar('•');
        });

        btnLogin.addActionListener(e -> login());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private void setFallbackBackground(JPanel panel) {
        panel.setBackground(COLOR_PRIMARY);
        JLabel lblFallback = new JLabel("HỆ THỐNG THUÊ XE", JLabel.CENTER);
        lblFallback.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblFallback.setForeground(Color.WHITE);
        panel.add(lblFallback, BorderLayout.CENTER);
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
            
            new FrmMain().setVisible(true);
            this.dispose(); 
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupInputField(JTextField field, Border border) {
        field.setFont(FONT_MODERN);
        field.setForeground(COLOR_TEXT);
        field.setBackground(COLOR_INPUT);
        field.setBorder(border);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, 40));
    }

    private void setupPrimaryButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(COLOR_PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 8, 8);
                super.paint(g2, c);
                g2.dispose();
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(62, 117, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY);
            }
        });
    }

    private void setupExitButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setForeground(Color.GRAY);
        button.setBackground(COLOR_BG);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 35));
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}