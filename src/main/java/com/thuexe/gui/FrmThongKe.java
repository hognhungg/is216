package com.thuexe.gui;

import com.thuexe.dao.HoaDonDAO;
import com.thuexe.util.ExcelExporter;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.io.File;
import java.util.Vector;

public class FrmThongKe extends JFrame {

    private JDateChooser dateStart;
    private JDateChooser dateEnd;

    private JTable tblThongKe;

    private DefaultTableModel model;

    private JLabel lblTongDoanhThu;

    private JButton btnThongKe;
    private JButton btnXuatExcel;

    public FrmThongKe() {

        initUI();

        addEvents();
    }

    private void initUI() {

        setTitle("Thong Ke Doanh Thu");

        setSize(900, 550);

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLayout(new BorderLayout());

        // PANEL TOP
        JPanel pnlTop =
                new JPanel(new FlowLayout());

        dateStart =
                new JDateChooser();

        dateEnd =
                new JDateChooser();

        btnThongKe =
                new JButton("Thong Ke");

        btnXuatExcel =
                new JButton("Xuat Excel");

        pnlTop.add(
                new JLabel("Tu ngay")
        );

        pnlTop.add(dateStart);

        pnlTop.add(
                new JLabel("Den ngay")
        );

        pnlTop.add(dateEnd);

        pnlTop.add(btnThongKe);

        pnlTop.add(btnXuatExcel);

        add(
                pnlTop,
                BorderLayout.NORTH
        );

        // TABLE
        String[] columns = {
                "Ma HD",
                "Ngay Lap",
                "Ma Bien Ban",
                "Tong Tien"
        };

        model =
                new DefaultTableModel(columns, 0);

        tblThongKe =
                new JTable(model);

        add(
                new JScrollPane(tblThongKe),
                BorderLayout.CENTER
        );

        // LABEL TONG TIEN
        lblTongDoanhThu =
                new JLabel(
                        "Tong doanh thu: 0 VNĐ",
                        JLabel.RIGHT
                );

        lblTongDoanhThu.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        add(
                lblTongDoanhThu,
                BorderLayout.SOUTH
        );
    }

    private void addEvents() {

        // THONG KE
        btnThongKe.addActionListener(e -> {

            if (dateStart.getDate() == null
                    || dateEnd.getDate() == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui long chon ngay!"
                );

                return;
            }

            loadData();

            tinhTongDoanhThu();
        });

        // XUAT EXCEL
        btnXuatExcel.addActionListener(e -> {

            JFileChooser fc =
                    new JFileChooser();

            int result =
                    fc.showSaveDialog(this);

            if (result ==
                    JFileChooser.APPROVE_OPTION) {

                try {

                    File file =
                            fc.getSelectedFile();

                    ExcelExporter exporter =
                            new ExcelExporter();

                    exporter.exportTable(
                            tblThongKe,
                            file
                    );

                    JOptionPane.showMessageDialog(
                            this,
                            "Xuat Excel thanh cong!"
                    );

                } catch (Exception ex) {

                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(
                            this,
                            "Loi xuat file!"
                    );
                }
            }
        });
    }

    private void loadData() {

        model.setRowCount(0);

        HoaDonDAO dao =
                new HoaDonDAO();

        Vector<Vector<Object>> data =
                dao.getThongKe(
                        dateStart.getDate(),
                        dateEnd.getDate()
                );

        for (Vector<Object> row : data) {

            model.addRow(row);
        }
    }

    private void tinhTongDoanhThu() {

        double tong = 0;

        for (int i = 0;
             i < tblThongKe.getRowCount();
             i++) {

            Object value =
                    tblThongKe.getValueAt(i, 3);

            if (value != null) {

                tong += Double.parseDouble(
                        value.toString()
                );
            }
        }

        lblTongDoanhThu.setText(
                "Tong doanh thu: "
                        + String.format("%,.0f", tong)
                        + " VNĐ"
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new FrmThongKe().setVisible(true);
        });
    }
}
