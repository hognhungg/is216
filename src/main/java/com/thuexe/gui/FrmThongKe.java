package com.thuexe.gui;

import com.toedter.calendar.JDateChooser;

// Thư viện JFreeChart core và định dạng plot
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class FrmThongKe extends JPanel {

    private JDateChooser dateStart;
    private JDateChooser dateEnd;
    private JLabel lblTongDoanhThu;
    private JButton btnThongKe;
    private JButton btnXuatExcel;

    // 4 vùng chứa biểu đồ động
    private JPanel pnlChart1;
    private JPanel pnlChart2;
    private JPanel pnlChart3;
    private JPanel pnlChart4;

    private Vector<Vector<Object>> currentData;

    public FrmThongKe() {
        currentData = new Vector<>();
        initUI();
        addEvents();
        
        // Cài đặt ngày mặc định hệ thống
        dateEnd.setDate(new java.util.Date()); 
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
        dateStart.setDate(cal.getTime());
        
        loadAndAnalyzeData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(248, 249, 250)); 

        // 1. PANEL THANH ĐIỀU HƯỚNG TRÊN (NORTH)
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnlTop.setBackground(new Color(245, 246, 248));

        dateStart = new JDateChooser();
        dateStart.setPreferredSize(new Dimension(150, 25));
        dateEnd = new JDateChooser();
        dateEnd.setPreferredSize(new Dimension(150, 25));

        btnThongKe = new JButton("Thống Kê");
        btnThongKe.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnThongKe.setBackground(new Color(52, 152, 219));  
        btnThongKe.setForeground(Color.WHITE);              
        btnThongKe.setPreferredSize(new Dimension(110, 25));
        btnThongKe.setOpaque(true);                         
        btnThongKe.setBorderPainted(false);                 

        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnXuatExcel.setBackground(new Color(46, 204, 113)); 
        btnXuatExcel.setForeground(Color.WHITE);             
        btnXuatExcel.setPreferredSize(new Dimension(110, 25));
        btnXuatExcel.setOpaque(true);                        
        btnXuatExcel.setBorderPainted(false);                

        pnlTop.add(new JLabel("Từ ngày:"));
        pnlTop.add(dateStart);
        pnlTop.add(new JLabel("Đến ngày:"));
        pnlTop.add(dateEnd);
        pnlTop.add(btnThongKe);
        pnlTop.add(btnXuatExcel);

        add(pnlTop, BorderLayout.NORTH);

        // 2. PANEL LƯỚI TRUNG TÂM CHỨA 4 BIỂU ĐỒ (GRID 2x2)
        JPanel pnlCenterGrid = new JPanel(new GridLayout(2, 2, 20, 20));
        pnlCenterGrid.setBackground(new Color(248, 249, 250));
        pnlCenterGrid.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Khởi tạo style đồng nhất cho 4 ô chứa biểu đồ
        pnlChart1 = createChartPanelWrapper("1. XU HƯỚNG DOANH THU");
        pnlChart2 = createChartPanelWrapper("2. TỶ TRỌNG DOANH THU ĐƠN HÀNG");
        pnlChart3 = createChartPanelWrapper("3. LƯỢT KHÁCH HÀNG THUÊ XE");
        pnlChart4 = createChartPanelWrapper("4. ĐÁNH GIÁ CHẤT LƯỢNG DỊCH VỤ");

        pnlCenterGrid.add(pnlChart1);
        pnlCenterGrid.add(pnlChart2);
        pnlCenterGrid.add(pnlChart3);
        pnlCenterGrid.add(pnlChart4);
        
        add(pnlCenterGrid, BorderLayout.CENTER);

        // 3. THANH THÔNG TIN DOANH THU DƯỚI (SOUTH)
        JPanel pnlBottom = new JPanel(new BorderLayout());
        pnlBottom.setBackground(new Color(248, 249, 250));
        pnlBottom.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        lblTongDoanhThu = new JLabel("TỔNG DOANH THU HỆ THỐNG: 0 VNĐ", JLabel.RIGHT);
        lblTongDoanhThu.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTongDoanhThu.setForeground(new Color(231, 76, 60)); 

        pnlBottom.add(lblTongDoanhThu, BorderLayout.EAST);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    // Hàm tạo khung bọc biểu đồ sạch sẽ, bo góc nhẹ
    private JPanel createChartPanelWrapper(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 233, 237), 1), 
                title, TitledBorder.LEFT, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)));
        return panel;
    }

    private void addEvents() {
        btnThongKe.addActionListener(e -> {
            if (dateStart.getDate() == null || dateEnd.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ khoảng thời gian thống kê!");
                return;
            }
            loadAndAnalyzeData();
        });
    }

    private void loadAndAnalyzeData() {
        currentData = new Vector<>();

        // Giả lập dữ liệu chuỗi hóa đơn thương mại (Mã HD, Ngày, Mã BB, Tiền)
        Vector<Object> hd1 = new Vector<>();
        hd1.add("101"); hd1.add("2026-05-10"); hd1.add("BB01"); hd1.add(4500000);
        currentData.add(hd1);

        Vector<Object> hd2 = new Vector<>();
        hd2.add("102"); hd2.add("2026-05-14"); hd2.add("BB02"); hd2.add(9800000);
        currentData.add(hd2);

        Vector<Object> hd3 = new Vector<>();
        hd3.add("103"); hd3.add("2026-05-18"); hd3.add("BB03"); hd3.add(6200000);
        currentData.add(hd3);

        Vector<Object> hd4 = new Vector<>();
        hd4.add("104"); hd4.add("2026-05-22"); hd4.add("BB04"); hd4.add(12500000);
        currentData.add(hd4);

        double tongDoanhThu = 0;
        for (Vector<Object> row : currentData) {
            if (row.get(3) != null) {
                tongDoanhThu += Double.parseDouble(row.get(3).toString());
            }
        }

        lblTongDoanhThu.setText("TỔNG DOANH THU HỆ THỐNG: " + String.format("%,.0f", tongDoanhThu) + " VNĐ");
        updateCharts(currentData);
    }

    private void updateCharts(Vector<Vector<Object>> data) {
        // Xóa sạch dữ liệu cũ để vẽ mới
        pnlChart1.removeAll(); pnlChart2.removeAll(); pnlChart3.removeAll(); pnlChart4.removeAll();

        if (data == null || data.isEmpty()) {
            displayEmptyMessage();
            return;
        }

        // --- KHỞI TẠO CÁC DATASET ---
        DefaultCategoryDataset doanhThuDataset = new DefaultCategoryDataset();
        DefaultPieDataset tyTrongDataset = new DefaultPieDataset();
        DefaultCategoryDataset khachHangDataset = new DefaultCategoryDataset();
        DefaultPieDataset danhGiaDataset = new DefaultPieDataset();

        // Đổ dữ liệu giả lập vào bộ nạp
        for (Vector<Object> row : data) {
            String maHD = "HD " + row.get(0).toString();
            String ngayLap = row.get(1).toString();
            double tien = Double.parseDouble(row.get(3).toString());

            doanhThuDataset.addValue(tien, "Doanh Thu", ngayLap);
            tyTrongDataset.setValue(maHD, tien);
        }

        // Giả lập dữ liệu Khách hàng theo ngày (Biểu đồ đường Line)
        khachHangDataset.addValue(5, "Lượt khách", "2026-05-10");
        khachHangDataset.addValue(12, "Lượt khách", "2026-05-14");
        khachHangDataset.addValue(8, "Lượt khách", "2026-05-18");
        khachHangDataset.addValue(15, "Lượt khách", "2026-05-22");

        // Giả lập dữ liệu Đánh giá sao dịch vụ (Biểu đồ tròn 2)
        danhGiaDataset.setValue("5 Sao (Xuất Sắc)", 65);
        danhGiaDataset.setValue("4 Sao (Tốt)", 25);
        danhGiaDataset.setValue("3 Sao (Trung Bình)", 10);

        // ====================================================================
        // BIỂU ĐỒ 1: CỘT DOANH THU (MÀU PHẲNG HIỆN ĐẠI - FLAT DESIGN)
        // ====================================================================
        JFreeChart barChart = ChartFactory.createBarChart(null, null, "VNĐ", doanhThuDataset, PlotOrientation.VERTICAL, false, true, false);
        styleCategoryPlot(barChart.getCategoryPlot());
        BarRenderer barRenderer = (BarRenderer) barChart.getCategoryPlot().getRenderer();
        barRenderer.setSeriesPaint(0, new Color(52, 152, 219)); // Màu xanh Blue thanh lịch
        pnlChart1.add(new ChartPanel(barChart), BorderLayout.CENTER);

        // ====================================================================
        // BIỂU ĐỒ 2: TRÒN TỶ TRỌNG (NỀN TRẮNG + HIỂN THỊ RÕ %)
        // ====================================================================
        JFreeChart pieChart1 = ChartFactory.createPieChart(null, tyTrongDataset, true, true, false);
        stylePiePlot(pieChart1);
        pnlChart2.add(new ChartPanel(pieChart1), BorderLayout.CENTER);

        // ====================================================================
        // BIỂU ĐỒ 3: ĐƯỜNG XU HƯỚNG LƯỢT KHÁCH TĂNG TRƯỞNG
        // ====================================================================
        JFreeChart lineChart = ChartFactory.createLineChart(null, null, "Số lượt", khachHangDataset, PlotOrientation.VERTICAL, false, true, false);
        styleCategoryPlot(lineChart.getCategoryPlot());
        LineAndShapeRenderer lineRenderer = (LineAndShapeRenderer) lineChart.getCategoryPlot().getRenderer();
        lineRenderer.setSeriesPaint(0, new Color(231, 76, 60)); // Đường line màu đỏ coral nổi bật
        lineRenderer.setSeriesStroke(0, new BasicStroke(2.5f));
        pnlChart3.add(new ChartPanel(lineChart), BorderLayout.CENTER);

        // ====================================================================
        // BIỂU ĐỒ 4: TRÒN ĐÁNH GIÁ CHẤT LƯỢNG
        // ====================================================================
        JFreeChart pieChart2 = ChartFactory.createPieChart(null, danhGiaDataset, true, true, false);
        stylePiePlot(pieChart2);
        pnlChart4.add(new ChartPanel(pieChart2), BorderLayout.CENTER);

        // Refresh lại layout UI
        revalidate();
        repaint();
    }

    // --- HÀM MAKEUP STYLE BIỂU ĐỒ CỘT/ĐƯỜNG (XÓA BỎ KHUNG XÁM XẤU XÍ) ---
    private void styleCategoryPlot(CategoryPlot plot) {
        plot.setBackgroundPaint(Color.WHITE);                // Đổi nền xám thành trắng tinh khôi
        plot.setRangeGridlinePaint(new Color(230, 233, 237)); // Màu đường kẻ vạch mờ thanh lịch
        plot.setOutlineVisible(false);                       // Xóa bỏ khung viền bao quanh thô cứng
        
        // Custom font chữ trục X, Y cho nét và dễ đọc
        plot.getDomainAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        plot.getRangeAxis().setTickLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
    }

    // --- HÀM MAKEUP STYLE BIỂU ĐỒ TRÒN (HIỂN THỊ PHẦN TRĂM % ĐẸP MẮT) ---
    private void stylePiePlot(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setShadowPaint(null); // TẮT ĐỔ BÓNG 3D QUÁ LỖI THỜI
        
        // Thiết lập hiển thị: Tên nhãn = Số phần trăm cụ thể (Ví dụ: HD 101: 28%)
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}"));
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 11));
        plot.setLabelBackgroundPaint(new Color(248, 249, 250));
    }

    private void displayEmptyMessage() {
        pnlChart1.add(new JLabel("Chưa có dữ liệu", JLabel.CENTER));
        pnlChart2.add(new JLabel("Chưa có dữ liệu", JLabel.CENTER));
        pnlChart3.add(new JLabel("Chưa có dữ liệu", JLabel.CENTER));
        pnlChart4.add(new JLabel("Chưa có dữ liệu", JLabel.CENTER));
    }
}