package com.thuexe.gui;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FrmDanhGia extends JDialog {
    private JRadioButton r1, r2, r3, r4, r5;
    private JTextArea txtCamNhan;
    private JButton btnGui;

    public FrmDanhGia(Frame parent) {
        super(parent, "Đánh giá dịch vụ", true); // true = Modal (khóa form cha)
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // --- Header ---
        JPanel pnlHeader = new JPanel();
        pnlHeader.add(new JLabel("Mức độ hài lòng của bạn?"));
        add(pnlHeader, BorderLayout.NORTH);

        // --- Center (Sao và Cảm nhận) ---
        JPanel pnlContent = new JPanel();
        pnlContent.setLayout(new BoxLayout(pnlContent, BoxLayout.Y_AXIS));
        pnlContent.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Radio Buttons (1-5 Sao)
        JPanel pnlStars = new JPanel();
        r1 = new JRadioButton("1★");
        r2 = new JRadioButton("2★");
        r3 = new JRadioButton("3★");
        r4 = new JRadioButton("4★");
        r5 = new JRadioButton("5★", true); // Mặc định chọn 5 sao

        ButtonGroup group = new ButtonGroup();
        group.add(r1); group.add(r2); group.add(r3); group.add(r4); group.add(r5);

        pnlStars.add(r1); pnlStars.add(r2); pnlStars.add(r3); pnlStars.add(r4); pnlStars.add(r5);
        pnlContent.add(pnlStars);

        // Cảm nhận
        pnlContent.add(new JLabel("Cảm nhận của khách hàng:"));
        txtCamNhan = new JTextArea(6, 20);
        txtCamNhan.setLineWrap(true);
        txtCamNhan.setWrapStyleWord(true);
        pnlContent.add(new JScrollPane(txtCamNhan));

        add(pnlContent, BorderLayout.CENTER);

        // --- Bottom (Nút gửi) ---
        JPanel pnlBottom = new JPanel();
        btnGui = new JButton("Gửi đánh giá");
        pnlBottom.add(btnGui);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- Event ---
        btnGui.addActionListener(e -> {
            // Logic: Lấy giá trị đánh giá (demo)
            String rating = "5";
            if(r1.isSelected()) rating = "1";
            if(r2.isSelected()) rating = "2";
            if(r3.isSelected()) rating = "3";
            if(r4.isSelected()) rating = "4";

            // Thông báo
            JOptionPane.showMessageDialog(this, "Cảm ơn khách hàng đã đánh giá " + rating + " sao!");
            
            // Đóng cửa sổ
            this.dispose();
        });
    }
}