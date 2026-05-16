package com.thuexe.quanlythuexe;

import javax.swing.UIManager;

import com.thuexe.gui.FrmLogin;

public class QuanLyThueXe {

    public static void main(String[] args) {
        // 1. Thiết lập giao diện giống hệ điều hành (cho đẹp và chuyên nghiệp)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. Kiểm tra kết nối Database trước khi mở ứng dụng (Tùy chọn)
        System.out.println("Đang khởi động hệ thống...");
        
        // 3. Gọi Form Đăng nhập hiện lên
        java.awt.EventQueue.invokeLater(() -> {
            new FrmLogin().setVisible(true);
        });
    }
}