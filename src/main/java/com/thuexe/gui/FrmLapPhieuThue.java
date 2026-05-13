package com.thuexe.gui;

import com.thuexe.bus.PhieuThueBUS;
import com.thuexe.dto.PhieuThueDTO;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrmLapPhieuThue extends JFrame {
    
    private JComboBox<String> cboKH, cboXe;
    private JTextField txtDonGia, txtSoNgay, txtTien, txtDiaDiemNhan, txtDiaDiemTra;
    private JSpinner spnNhan, spnTra;
    private JButton btnLuu, btnHuy;
    
    private PhieuThueBUS bus = new PhieuThueBUS();
    private List<String> maKH = new ArrayList<>();
    private List<String> bienSo = new ArrayList<>();

    public FrmLapPhieuThue() {
        initComponents();
        loadData();
    }

    // ===== KHỞI TẠO GIAO DIỆN =====
    private void initComponents() {
        setTitle("LẬP PHIẾU THUÊ XE");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(10, 2, 5, 5));

        add(new JLabel("Khách hàng:"));
        cboKH = new JComboBox<>();
        add(cboKH);

        add(new JLabel("Xe:"));
        cboXe = new JComboBox<>();
        add(cboXe);

        add(new JLabel("Ngày nhận:"));
        spnNhan = new JSpinner(new SpinnerDateModel());
        spnNhan.setEditor(new JSpinner.DateEditor(spnNhan, "dd/MM/yyyy"));
        add(spnNhan);

        add(new JLabel("Ngày trả:"));
        spnTra = new JSpinner(new SpinnerDateModel());
        spnTra.setEditor(new JSpinner.DateEditor(spnTra, "dd/MM/yyyy"));
        add(spnTra);

        add(new JLabel("Địa điểm nhận:"));
        txtDiaDiemNhan = new JTextField();
        add(txtDiaDiemNhan);

        add(new JLabel("Địa điểm trả:"));
        txtDiaDiemTra = new JTextField();
        add(txtDiaDiemTra);

        add(new JLabel("Đơn giá/ngày:"));
        txtDonGia = new JTextField();
        txtDonGia.setEditable(false);
        add(txtDonGia);

        add(new JLabel("Số ngày:"));
        txtSoNgay = new JTextField();
        txtSoNgay.setEditable(false);
        add(txtSoNgay);

        add(new JLabel("Tiền tạm tính:"));
        txtTien = new JTextField();
        txtTien.setEditable(false);
        add(txtTien);

        btnLuu = new JButton("LƯU");
        btnHuy = new JButton("HỦY");
        add(btnLuu);
        add(btnHuy);

        // Sự kiện
        cboXe.addActionListener(e -> capNhatDonGia());
        spnNhan.addChangeListener(e -> {
            loadXeTrong();
            tinhTien();
        });
        spnTra.addChangeListener(e -> {
            loadXeTrong();
            tinhTien();
        });
        btnLuu.addActionListener(e -> luuPhieu());
        btnHuy.addActionListener(e -> dispose());
    }

    // ===== LOAD DỮ LIỆU BAN ĐẦU =====
    private void loadData() {
        try {
            // Load khách hàng
            for (Object[] row : bus.layKhachHang()) {
                cboKH.addItem(row[1].toString());
                maKH.add(row[0].toString());
            }

            // Set ngày mặc định
            spnNhan.setValue(new Date());
            spnTra.setValue(new Date(System.currentTimeMillis() + 86400000L));

            // Load xe trống theo ngày hiện tại
            loadXeTrong();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + ex.getMessage());
        }
    }

    // ===== LOAD XE TRỐNG THEO NGÀY ĐÃ CHỌN =====
    private void loadXeTrong() {
        Date ngayNhan = (Date) spnNhan.getValue();
        Date ngayTra = (Date) spnTra.getValue();

        if (ngayNhan == null || ngayTra == null) return;

        cboXe.removeAllItems();
        bienSo.clear();

        List<Object[]> dsXe = bus.layXeTrong(ngayNhan, ngayTra);
        
        if (dsXe.isEmpty()) {
            txtDonGia.setText("0");
            txtSoNgay.setText("0");
            txtTien.setText("0");
            return;
        }

        for (Object[] row : dsXe) {
            cboXe.addItem(row[1].toString());
            bienSo.add(row[0].toString());
        }

        cboXe.setSelectedIndex(0);
        capNhatDonGia();
    }

    // ===== CẬP NHẬT ĐƠN GIÁ KHI CHỌN XE =====
    private void capNhatDonGia() {
        int i = cboXe.getSelectedIndex();
        if (i >= 0 && i < bienSo.size()) {
            try {
                double donGia = bus.layDonGia(bienSo.get(i));
                txtDonGia.setText(String.format("%,.0f", donGia));
                tinhTien();
            } catch (Exception ex) {
                txtDonGia.setText("0");
            }
        }
    }

    // ===== TÍNH TIỀN =====
    private void tinhTien() {
        Date d1 = (Date) spnNhan.getValue();
        Date d2 = (Date) spnTra.getValue();
        
        if (d1 == null || d2 == null) return;

        int ngay = (int) ((d2.getTime() - d1.getTime()) / 86400000L);
        
        if (ngay <= 0) {
            txtSoNgay.setText("0");
            txtTien.setText("0");
            return;
        }

        txtSoNgay.setText(String.valueOf(ngay));

        try {
            double gia = Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9]", ""));
            double tien = ngay * gia;
            txtTien.setText(String.format("%,.0f", tien));
        } catch (NumberFormatException e) {
            txtTien.setText("0");
        }
    }

    // ===== LƯU PHIẾU THUÊ =====
    private void luuPhieu() {
        try {
            PhieuThueDTO p = new PhieuThueDTO();
            
            int khIdx = cboKH.getSelectedIndex();
            if (khIdx < 0) throw new Exception("Vui lòng chọn khách hàng!");
            p.setMaKhachHang(maKH.get(khIdx));

            int xeIdx = cboXe.getSelectedIndex();
            if (xeIdx < 0) throw new Exception("Vui lòng chọn xe!");
            p.setBienSoXe(bienSo.get(xeIdx));

            p.setThoiGianNhanXe((Date) spnNhan.getValue());
            p.setThoiGianTraXe((Date) spnTra.getValue());
            p.setDiaDiemNhanXe(txtDiaDiemNhan.getText().trim());
            p.setDiaDiemTraXe(txtDiaDiemTra.getText().trim());
            p.setDonGiaNgay(Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9]", "")));
            p.tinhTienTamTinh();

            String maHD = bus.lapPhieu(p);
            JOptionPane.showMessageDialog(this, "Thành công! Mã HĐ: " + maHD);
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    // =====  TEST =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmLapPhieuThue().setVisible(true));
    }
}