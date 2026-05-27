package com.thuexe.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FrmDanhGia extends JPanel {

    private JTable tblDanhGia;
    private DefaultTableModel model;
    
    // Đã xóa hoàn toàn biến btnSua theo yêu cầu
    private JButton btnThem, btnXoa, btnLamMoi, btnDashboard;
    
    private JLabel lblTotalCount, lblAverageScore, lblSatisfactionRate;

    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);

    public FrmDanhGia() {
        initComponents();
        initEvents();
        loadDataPlaceholder(); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // =========================================================================
        // PHẦN BẮC (NORTH): CHỨA TIÊU ĐỀ, THẺ THỐNG KÊ VÀ THANH NÚT TÁC VỤ
        // =========================================================================
        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTitle = new JLabel("Danh Sách Đánh Giá Dịch Vụ Hệ Thống");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTitle, BorderLayout.WEST);
        
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15));

        JPanel pnlDashboardCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlDashboardCards.setBackground(COLOR_BG);
        pnlDashboardCards.setPreferredSize(new Dimension(0, 100));

        JPanel cardTotal = createStatCard("TỔNG SỐ ĐÁNH GIÁ", "0", new Color(111, 96, 216));
        lblTotalCount = (JLabel) cardTotal.getClientProperty("valueLabel");
        
        JPanel cardAverage = createStatCard("ĐIỂM TRUNG BÌNH", "0.0 / 5 \u2605", new Color(145, 145, 255));
        lblAverageScore = (JLabel) cardAverage.getClientProperty("valueLabel");
        lblAverageScore.setFont(new Font("Dialog", Font.BOLD, 26)); 

        JPanel cardSatisfaction = createStatCard("TỶ LỆ HÀI LÒNG", "0%", new Color(254, 202, 87));
        lblSatisfactionRate = (JLabel) cardSatisfaction.getClientProperty("valueLabel");

        pnlDashboardCards.add(cardTotal);
        pnlDashboardCards.add(cardAverage);
        pnlDashboardCards.add(cardSatisfaction);

        pnlNorthContainer.add(pnlDashboardCards);
        pnlNorthContainer.add(Box.createVerticalStrut(20));

        // Thanh nút chức năng tác vụ - ĐÃ XÓA BỎ NÚT SỬA
        JPanel pnlActionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActionRow.setBackground(COLOR_BG);

        btnThem = createStyledButton("Thêm đánh giá", new Color(74, 137, 95));
        btnXoa = createStyledButton("Xóa đánh giá", new Color(190, 50, 50));

        pnlActionRow.add(btnThem);
        pnlActionRow.add(btnXoa);

        pnlNorthContainer.add(pnlActionRow);
        add(pnlNorthContainer, BorderLayout.NORTH);

        // =========================================================================
        // PHẦN TRUNG TÂM (CENTER): BẢNG HIỂN THỊ CHI TIẾT (Đã map theo cấu trúc DB)
        // =========================================================================
        String[] headers = {"Mã ĐG (NUMBER)", "Mã Phiếu Thuê (FK)", "Số Điểm (DiemSo)", "Nội Dung Góp Ý"};
        model = new DefaultTableModel(headers, 0) {
            @Override 
            public boolean isCellEditable(int r, int c) { return false; }
        };
        
        tblDanhGia = new JTable(model);
        tblDanhGia.setRowHeight(35);
        tblDanhGia.setFont(new Font("Dialog", Font.PLAIN, 14));
        tblDanhGia.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblDanhGia.getTableHeader().setBackground(new Color(230, 235, 240));
        tblDanhGia.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(tblDanhGia);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // PHẦN NAM (SOUTH): THANH CHỨC NĂNG ĐIỀU HƯỚNG PHỤ
        // =========================================================================
        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomButtons.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới danh sách", new Color(110, 110, 110));
        btnDashboard = createStyledButton("Quay về Dashboard", new Color(214, 48, 49));

        pnlBottomButtons.add(btnLamMoi);
        pnlBottomButtons.add(btnDashboard);
        add(pnlBottomButtons, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnLamMoi.addActionListener(e -> loadDataPlaceholder());

        btnDashboard.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent instanceof JPanel && ((JPanel) parent).getTopLevelAncestor() instanceof FrmMain) {
                ((FrmMain) ((JPanel) parent).getTopLevelAncestor()).btnDashboard.doClick();
            }
        });

        // =========================================================================
        // LOGIC XỬ LÝ THAO TÁC NÚT THÊM ĐÁNH GIÁ (GIAO DIỆN LỚN + CHỌN SAO MÀU SẮC)
        // =========================================================================
        btnThem.addActionListener(e -> {
            // Tạo một JDialog tùy biến độc lập để kiểm soát kích thước lớn nhỏ theo ý muốn
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm Đánh Giá Mới Vào Hệ Thống", true);
            dialog.setSize(500, 540); 
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);

            // Panel chính sử dụng BoxLayout theo trục dọc
            JPanel pnlMain = new JPanel();
            pnlMain.setLayout(new BoxLayout(pnlMain, BoxLayout.Y_AXIS));
            pnlMain.setBorder(new EmptyBorder(25, 40, 25, 40));
            pnlMain.setBackground(new Color(250, 251, 253));

            Font fontLabel = new Font("Segoe UI", Font.BOLD, 14);
            Color colorLabel = new Color(70, 80, 95);

            // --- HÀNG 1: MÃ ĐÁNH GIÁ (CĂN GIỮA) ---
            int nextId = model.getRowCount() + 1;
            JPanel pnlMa = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            pnlMa.setBackground(pnlMain.getBackground());
            pnlMa.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa component trong BoxLayout
            
            JLabel lblMaDG = new JLabel("Mã Đánh Giá (NUMBER): ");
            lblMaDG.setFont(fontLabel);
            lblMaDG.setForeground(colorLabel);
            
            JLabel lblMaValue = new JLabel(String.valueOf(nextId));
            lblMaValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblMaValue.setForeground(new Color(111, 96, 216));
            
            pnlMa.add(lblMaDG);
            pnlMa.add(lblMaValue);

            // --- HÀNG 2: MÃ PHIẾU THUÊ (CĂN GIỮA) ---
            JLabel lblMaPhieu = new JLabel("Mã Phiếu Thuê (Mã Hợp Đồng):");
            lblMaPhieu.setFont(fontLabel);
            lblMaPhieu.setForeground(colorLabel);
            lblMaPhieu.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa chữ tiêu đề
            
            JTextField txtMaPhieuThue = new JTextField("20260" + nextId);
            txtMaPhieuThue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtMaPhieuThue.setHorizontalAlignment(JTextField.CENTER); // Căn giữa chữ nhập inside ô text
            txtMaPhieuThue.setMaximumSize(new Dimension(380, 38)); // Ràng buộc kích thước ô nhập
            txtMaPhieuThue.setAlignmentX(Component.CENTER_ALIGNMENT); // Căn giữa ô textfield
            txtMaPhieuThue.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 205, 215), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
            ));

            // --- HÀNG 3: CHỌN ĐIỂM SỐ (CĂN GIỮA 5 NGÔI SAO) ---
            JLabel lblDiemSo = new JLabel("Chọn Điểm Số Dịch Vụ:");
            lblDiemSo.setFont(fontLabel);
            lblDiemSo.setForeground(colorLabel);
            lblDiemSo.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Sử dụng FlowLayout CENTER để gom cụm 5 ngôi sao vào chính giữa
            JPanel pnlStars = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 5));
            pnlStars.setBackground(pnlMain.getBackground());
            pnlStars.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JToggleButton[] btnStars = new JToggleButton[5];
            final int[] selectedScore = {5}; 

            for (int i = 0; i < 5; i++) {
                int score = i + 1;
                btnStars[i] = new JToggleButton("★"); 
                btnStars[i].setFont(new Font("Dialog", Font.PLAIN, 34)); // Tăng nhẹ kích thước sao cho cân xứng
                btnStars[i].setFocusPainted(false);
                btnStars[i].setBorderPainted(false);
                btnStars[i].setContentAreaFilled(false);
                btnStars[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnStars[i].setSelected(true);
                btnStars[i].setForeground(new Color(255, 193, 7)); 

                btnStars[i].addActionListener(evt -> {
                    selectedScore[0] = score;
                    for (int j = 0; j < 5; j++) {
                        if (j < score) {
                            btnStars[j].setSelected(true);
                            btnStars[j].setForeground(new Color(255, 193, 7)); 
                        } else {
                            btnStars[j].setSelected(false);
                            btnStars[j].setForeground(new Color(210, 215, 225)); 
                        }
                    }
                });
                pnlStars.add(btnStars[i]);
            }

            // --- HÀNG 4: NỘI DUNG GÓP Ý (CĂN GIỮA) ---
            JLabel lblNoiDung = new JLabel("Nội Dung Góp Ý / Phản Hồi:");
            lblNoiDung.setFont(fontLabel);
            lblNoiDung.setForeground(colorLabel);
            lblNoiDung.setAlignmentX(Component.CENTER_ALIGNMENT);

            JTextArea txtNoiDung = new JTextArea();
            txtNoiDung.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txtNoiDung.setLineWrap(true);
            txtNoiDung.setWrapStyleWord(true);
            
            JScrollPane scrollNoiDung = new JScrollPane(txtNoiDung);
            scrollNoiDung.setMaximumSize(new Dimension(380, 110)); // Định kích thước khung cuộn chuẩn mực
            scrollNoiDung.setAlignmentX(Component.CENTER_ALIGNMENT);
            scrollNoiDung.setBorder(BorderFactory.createLineBorder(new Color(200, 205, 215), 1, true));

            // --- THANH NÚT CHỨC NĂNG (CĂN GIỮA CÂN ĐỐI) ---
            JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0)); // Dùng CENTER cho 2 nút dưới
            pnlButtons.setBackground(pnlMain.getBackground());
            pnlButtons.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton btnConfirm = createStyledButton("Xác Nhận Lưu", new Color(74, 137, 95));
            JButton btnCancel = createStyledButton("Hủy Bỏ", new Color(180, 185, 190));
            btnCancel.setForeground(Color.DARK_GRAY);

            pnlButtons.add(btnCancel);
            pnlButtons.add(btnConfirm);

            // Đưa các khối đã cấu hình căn giữa vào khung hiển thị dọc
            pnlMain.add(pnlMa);
            pnlMain.add(Box.createVerticalStrut(15));
            pnlMain.add(lblMaPhieu);
            pnlMain.add(Box.createVerticalStrut(6));
            pnlMain.add(txtMaPhieuThue);
            pnlMain.add(Box.createVerticalStrut(15));
            pnlMain.add(lblDiemSo);
            pnlMain.add(pnlStars);
            pnlMain.add(Box.createVerticalStrut(15));
            pnlMain.add(lblNoiDung);
            pnlMain.add(Box.createVerticalStrut(6));
            pnlMain.add(scrollNoiDung);
            pnlMain.add(Box.createVerticalStrut(25));
            pnlMain.add(pnlButtons);

            final boolean[] isSaved = {false};
            btnConfirm.addActionListener(evt -> {
                if (txtMaPhieuThue.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Mã phiếu thuê không được bỏ trống!", "Cảnh báo", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                isSaved[0] = true;
                dialog.dispose();
            });

            btnCancel.addActionListener(evt -> dialog.dispose());

            dialog.add(pnlMain);
            dialog.setVisible(true); 

            if (isSaved[0]) {
                String maPhieu = txtMaPhieuThue.getText().trim();
                String noiDung = txtNoiDung.getText().trim();
                int finalStars = selectedScore[0];

                model.addRow(new Object[]{
                    nextId, 
                    maPhieu, 
                    finalStars + " \u2605", 
                    noiDung.isEmpty() ? "Khách hàng không để lại ý kiến." : noiDung
                });

                updateStatistics();
                JOptionPane.showMessageDialog(this, "Đã lưu bản ghi đánh giá " + finalStars + " Sao thành công!");
            }
        });

        // =========================================================================
        // LOGIC XỬ LÝ THAO TÁC NÚT XÓA ĐÁNH GIÁ (KẾT NỐI TRỰC TIẾP VÀO TABLE MODEL)
        // =========================================================================
        btnXoa.addActionListener(e -> {
            int selectedRow = tblDanhGia.getSelectedRow();
            
            if (selectedRow < 0) { 
                JOptionPane.showMessageDialog(this, "Vui lòng click chọn 1 dòng đánh giá trên bảng để tiến hành xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            
            String maDg = model.getValueAt(selectedRow, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa bản ghi đánh giá có mã số [" + maDg + "] không?", "Xác nhận xóa từ hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                model.removeRow(selectedRow);
                updateStatistics();
                JOptionPane.showMessageDialog(this, "Đã xóa bản ghi thành công ra khỏi danh sách hiển thị.");
            }
        });
    }

    private void loadDataPlaceholder() {
        model.setRowCount(0);

        model.addRow(new Object[]{1, 1001, "5 \u2605", "Xe chạy rất êm, phục vụ nhiệt tình chu đáo, 10 điểm!"});
        model.addRow(new Object[]{2, 1005, "4 \u2605", "Thủ tục bàn giao xe nhanh gọn, tuy nhiên xe hơi bị bụi nhẹ ở nội thất."});
        model.addRow(new Object[]{3, 1010, "5 \u2605", "Dịch vụ tuyệt vời, dòng xe đời mới chạy cực kỳ tiết kiệm xăng."});
        model.addRow(new Object[]{4, 1015, "5 \u2605", "Thuê xe máy côn tay đi phượt rất bốc, máy móc bảo trì tốt, an tâm."});
        model.addRow(new Object[]{5, 1022, "3 \u2605", "Giao xe trễ hẹn 15 phút do tắc đường, bù lại nhân viên xin lỗi rất lịch sự."});
        model.addRow(new Object[]{6, 1030, "5 \u2605", "Lần đầu tiên thử xe điện 4 chỗ, trải nghiệm mượt mà, sạc pin nhanh."});
        model.addRow(new Object[]{7, 1034, "4 \u2605", "Giá thuê ngày cuối tuần hơi cao một chút nhưng chất lượng xe xứng đáng."});
        model.addRow(new Object[]{8, 1041, "5 \u2605", "Xe 7 chỗ SUV gia đình rộng rãi, điều hòa mát rượi, cả nhà ai cũng thích."});
        model.addRow(new Object[]{9, 1048, "2 \u2605", "Điều hòa băng ghế sau hơi yếu, đề nghị kỹ thuật kiểm tra lại giùm."});
        model.addRow(new Object[]{10, 1055, "5 \u2605", "Hỗ trợ cứu hộ dọc đường rất nhanh khi xe mình bị dính đinh. Quá chuyên nghiệp!"});

        updateStatistics();
    }

    private void updateStatistics() {
        int totalRows = model.getRowCount();
        if (totalRows == 0) {
            lblTotalCount.setText("0");
            lblAverageScore.setText("0.0 / 5 \u2605");
            lblSatisfactionRate.setText("0.0 %");
            return;
        }

        double totalStars = 0;
        int satisfiedCount = 0;

        for (int i = 0; i < totalRows; i++) {
            String starStr = model.getValueAt(i, 2).toString(); 
            int starValue = Integer.parseInt(starStr.replaceAll("[^0-9]", "").trim());
            
            totalStars += starValue;
            if (starValue >= 4) { 
                satisfiedCount++;
            }
        }

        double average = totalStars / totalRows;
        double satisfactionRate = ((double) satisfiedCount / totalRows) * 100;

        lblTotalCount.setText(String.valueOf(totalRows));
        lblAverageScore.setText(String.format("%.1f / 5 \u2605", average));
        lblSatisfactionRate.setText(String.format("%.1f %%", satisfactionRate));
    }

    private JPanel createStatCard(String title, String defaultValue, Color bg) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setBackground(bg);
        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(240, 240, 240));

        JLabel lblValue = new JLabel(defaultValue);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        card.putClientProperty("valueLabel", lblValue);
        return card;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        btn.setForeground(Color.WHITE); 
        btn.setBackground(bg);
        btn.setFocusPainted(false); 
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override 
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground()); 
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 6, 6);
                g2.dispose(); 
                super.paint(g, c);
            }
        });
        return btn;
    }
}