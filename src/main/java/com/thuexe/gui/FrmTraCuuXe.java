package com.thuexe.gui;

import com.thuexe.dao.XeDAO;
import com.thuexe.dto.XeDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmTraCuuXe extends JFrame {

    private JTable tblXe;
    private DefaultTableModel model;

    public FrmTraCuuXe() {
        setTitle("Tra cứu xe");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("DANH SÁCH XE", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel();
        tblXe = new JTable(model);

        JScrollPane scroll = new JScrollPane(tblXe);
        add(scroll, BorderLayout.CENTER);

        // Load data
        loadData();
    }

    // ===== LOAD DATA =====
    private void loadData() {
        // Tạo cột
        model.setRowCount(0);
        model.setColumnCount(0);

        model.addColumn("Biển số");
        model.addColumn("Tên xe");
        model.addColumn("Thương hiệu");
        model.addColumn("Số chỗ");
        model.addColumn("Loại xe");
        model.addColumn("Trạng thái");

        // Lấy dữ liệu từ DAO
        XeDAO dao = new XeDAO();
        List<XeDTO> list = dao.getAllXe();

        // Đổ vào bảng
        for (XeDTO x : list) {
            model.addRow(new Object[]{
                    x.getBienSoXe(),
                    x.getTenXe(),
                    x.getThuongHieu(),
                    x.getSoCho(),
                    x.getTenLoai(),
                    x.getTrangThai()
            });
        }
    }

    // Test riêng form này
    public static void main(String[] args) {
        new FrmTraCuuXe().setVisible(true);
    }
}