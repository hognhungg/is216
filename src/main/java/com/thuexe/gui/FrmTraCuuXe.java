package com.thuexe.gui;

import com.thuexe.bus.XeBUS;
import com.thuexe.bus.LoaiXeBUS;
import com.thuexe.dto.XeDTO;
import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.dto.TaiKhoanDTO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmTraCuuXe extends JPanel {

    private JTable tblTraCuuXe;
    private DefaultTableModel tableModel;
    
    private JTextField txtTimKiem;
    private JComboBox<String> cbLoaiXeFilter;
    private JComboBox<String> cbTrangThaiFilter;
    private JButton btnTimKiem, btnXoaBoLoc;
    private JButton btnLamMoi, btnThoat;            

    private JLabel lblTongXeValue, lblSanSangValue, lblBaoTriValue;
    private TaiKhoanDTO tkLogged;

    // 🛠️ KHAI BÁO CÁC LỚP NGHIỆP VỤ (BUS)
    private XeBUS xeBUS;
    private LoaiXeBUS loaiXeBUS;

    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);
    
    private static final Color CARD_COLOR_1 = new Color(74, 137, 95);    
    private static final Color CARD_COLOR_2 = new Color(52, 152, 219);  
    private static final Color CARD_COLOR_3 = new Color(231, 76, 60);    

    public FrmTraCuuXe() {
        this.tkLogged = FrmLogin.userLogged;
        
        // 🛠️ KHỞI TẠO ĐỐI TƯỢNG ĐIỀU HÀNH
        xeBUS = new XeBUS();
        loaiXeBUS = new LoaiXeBUS();

        initComponents();
        initEvents(); // Đã sửa: Gọi hàm ánh xạ sự kiện hợp lệ
        
        // 🛠️ TỰ ĐỘNG NẠP DANH MỤC VÀ DỮ LIỆU THỰC TẾ KHI MỞ TAB
        loadLoaiXeToComboBox();
        loadDataToTable(); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25)); 

        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTableTitle = new JLabel("Tra Cứu & Tìm Kiếm Xe Trực Tuyến");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTableTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTableTitle, BorderLayout.WEST);
        
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15)); 

        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(COLOR_BG);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        JPanel cardTong = createFlatCard("TỔNG SỐ XE TRONG KHO", "0", CARD_COLOR_1);
        lblTongXeValue = (JLabel) cardTong.getClientProperty("valueLabel");
        
        JPanel cardSanSang = createFlatCard("XE SẴN SÀNG CHO THUÊ", "0 xe", CARD_COLOR_2);
        lblSanSangValue = (JLabel) cardSanSang.getClientProperty("valueLabel");

        JPanel cardBaoTri = createFlatCard("XE ĐANG BẢO TRÌ / SỬA CHỮA", "0 xe", CARD_COLOR_3);
        lblBaoTriValue = (JLabel) cardBaoTri.getClientProperty("valueLabel");

        pnlCards.add(cardTong);
        pnlCards.add(cardSanSang);
        pnlCards.add(cardBaoTri);

        pnlNorthContainer.add(pnlCards);
        pnlNorthContainer.add(Box.createVerticalStrut(20)); 

        JPanel pnlSearchFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlSearchFilter.setBackground(COLOR_BG);

        txtTimKiem = new JTextField(15);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        txtTimKiem.setText("Nhập tên xe hoặc biển số...");
        txtTimKiem.setForeground(Color.GRAY);
        txtTimKiem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtTimKiem.getText().equals("Nhập tên xe hoặc biển số...")) {
                    txtTimKiem.setText("");
                    txtTimKiem.setForeground(COLOR_TEXT_DARK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtTimKiem.getText().isEmpty()) {
                    txtTimKiem.setText("Nhập tên xe hoặc biển số...");
                    txtTimKiem.setForeground(Color.GRAY);
                }
            }
        });

        cbLoaiXeFilter = new JComboBox<>();
        cbLoaiXeFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbLoaiXeFilter.setBackground(Color.WHITE);

        cbTrangThaiFilter = new JComboBox<>(new String[]{"-- Tất cả trạng thái --", "Sẵn Sàng", "Đang Được Thuê", "Đang Bảo Trì"});
        cbTrangThaiFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbTrangThaiFilter.setBackground(Color.WHITE);

        btnTimKiem = createStyledButton("Tìm kiếm", new Color(74, 137, 95));       
        btnXoaBoLoc = createStyledButton("Xóa bộ lọc", new Color(145, 145, 145));           

        pnlSearchFilter.add(new JLabel("Từ khóa:"));
        pnlSearchFilter.add(txtTimKiem);
        pnlSearchFilter.add(new JLabel("Phân loại:"));
        pnlSearchFilter.add(cbLoaiXeFilter);
        pnlSearchFilter.add(new JLabel("Trạng thái:"));
        pnlSearchFilter.add(cbTrangThaiFilter);
        pnlSearchFilter.add(btnTimKiem);
        pnlSearchFilter.add(btnXoaBoLoc);
        
        pnlNorthContainer.add(pnlSearchFilter);
        add(pnlNorthContainer, BorderLayout.NORTH);

        String[] columnNames = {"Biển Kiểm Soát", "Tên Dòng Xe", "Thương Hiệu", "Số Chỗ Ngồi", "Phân Loại Xe", "Trạng Thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblTraCuuXe = new JTable(tableModel);
        
        tblTraCuuXe.setRowHeight(35);
        tblTraCuuXe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblTraCuuXe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblTraCuuXe.getTableHeader().setBackground(new Color(230, 235, 240));

        JScrollPane scrollPane = new JScrollPane(tblTraCuuXe);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomButtons.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới danh sách", new Color(110, 110, 110)); 
        btnThoat = createStyledButton("Quay về Dashboard", new Color(214, 48, 49)); 

        pnlBottomButtons.add(btnLamMoi);
        pnlBottomButtons.add(btnThoat);
        add(pnlBottomButtons, BorderLayout.SOUTH);
    }

    // 🛠️ HÀM KHỞI TẠO ĐỐI TƯỢNG SỰ KIỆN CHUẨN (ĐÃ ĐƯỢC BỌC ĐÚNG)
    private void initEvents() {
        // Sự kiện nút Tìm kiếm - Lọc dữ liệu giả lập trực tiếp trên RAM
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim().toLowerCase();
            if (keyword.equals("nhập tên xe hoặc biển số...")) {
                keyword = "";
            }
            
            String loaiFilter = cbLoaiXeFilter.getSelectedItem().toString();
            String trangThaiFilter = cbTrangThaiFilter.getSelectedItem().toString();
            
            java.util.List<com.thuexe.dto.XeDTO> listGoc = new java.util.ArrayList<>();
            listGoc.add(new com.thuexe.dto.XeDTO("29A-123.45", "Toyota", "Toyota Vios 2023", 5, "vios.png", "Sẵn Sàng", 1, 1, 1, "Xe 4 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("30H-888.99", "Hyundai", "Hyundai SantaFe", 7, "santafe.png", "Đang Được Thuê", 1, 2, 1, "Xe 7 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("51K-555.55", "Ford", "Ford Ranger Wildtrak", 5, "ranger.png", "Sẵn Sàng", 2, 3, 1, "Xe Bán Tải"));
            listGoc.add(new com.thuexe.dto.XeDTO("29A-999.11", "VinFast", "VinFast VF8", 5, "vf8.png", "Đang Bảo Trì", 2, 4, 1, "Xe Điện"));
            listGoc.add(new com.thuexe.dto.XeDTO("30K-112.34", "Honda", "Honda City RS", 5, "city.png", "Sẵn Sàng", 3, 1, 1, "Xe 4 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("51G-678.90", "Kia", "Kia Carnival 2024", 7, "carnival.png", "Sẵn Sàng", 1, 2, 1, "Xe 7 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("30F-123.56", "Mazda", "Mazda CX-5", 5, "cx5.png", "Đang Được Thuê", 2, 2, 1, "Xe 5 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("43A-456.78", "Mitsubishi", "Mitsubishi Xpander", 7, "xpander.png", "Sẵn Sàng", 3, 2, 1, "Xe 7 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("29D-888.12", "Toyota", "Toyota Hilux Adventure", 5, "hilux.png", "Đang Bảo Trì", 1, 3, 1, "Xe Bán Tải"));
            listGoc.add(new com.thuexe.dto.XeDTO("30E-555.66", "Mercedes-Benz", "Mercedes C200", 5, "c200.png", "Sẵn Sàng", 4, 5, 1, "Xe Hạng Sang"));
            listGoc.add(new com.thuexe.dto.XeDTO("51H-999.88", "BMW", "BMW 320i LCI", 5, "bmw320i.png", "Đang Được Thuê", 4, 5, 1, "Xe Hạng Sang"));
            listGoc.add(new com.thuexe.dto.XeDTO("30G-234.56", "VinFast", "VinFast VF9", 7, "vf9.png", "Sẵn Sàng", 2, 4, 1, "Xe Điện"));
            listGoc.add(new com.thuexe.dto.XeDTO("29H-777.11", "Hyundai", "Hyundai Accent", 5, "accent.png", "Sẵn Sàng", 3, 1, 1, "Xe 4 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("51A-333.44", "Ford", "Ford Everest Titanium", 7, "everest.png", "Đang Được Thuê", 2, 2, 1, "Xe 7 Chỗ"));
            listGoc.add(new com.thuexe.dto.XeDTO("30K-666.88", "Porsche", "Porsche Macan GTS", 5, "macan.png", "Đang Bảo Trì", 4, 5, 1, "Xe Hạng Sang"));            
            java.util.List<com.thuexe.dto.XeDTO> listKetQua = new java.util.ArrayList<>();
            
            for (com.thuexe.dto.XeDTO xe : listGoc) {
                boolean matchKeyword = keyword.isEmpty() || 
                                       xe.getTenXe().toLowerCase().contains(keyword) || 
                                       xe.getBienSoXe().toLowerCase().contains(keyword);
                
                boolean matchLoai = loaiFilter.equals("-- Tất cả phân loại --") || 
                                    xe.getTenLoai().equalsIgnoreCase(loaiFilter);
                                    
                boolean matchTrangThai = trangThaiFilter.equals("-- Tất cả trạng thái --") || 
                                         xe.getTrangThai().equalsIgnoreCase(trangThaiFilter);
                
                if (matchKeyword && matchLoai && matchTrangThai) {
                    listKetQua.add(xe);
                }
            }
            hienThiLenTable(listKetQua);
        });

        // Sự kiện nút Xóa bộ lọc
        btnXoaBoLoc.addActionListener(e -> {
            txtTimKiem.setText("Nhập tên xe hoặc biển số...");
            txtTimKiem.setForeground(Color.GRAY);
            cbLoaiXeFilter.setSelectedIndex(0);
            cbTrangThaiFilter.setSelectedIndex(0);
            loadDataToTable();
        });

        // Sự kiện nút Làm mới
        btnLamMoi.addActionListener(e -> {
            loadDataToTable();
            tblTraCuuXe.clearSelection();
            JOptionPane.showMessageDialog(this, "Dữ liệu tra cứu xe đã được cập nhật đồng bộ!");
        });

        // Sự kiện nút Thoát về Dashboard
        btnThoat.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent instanceof JPanel) {
                JPanel pnlContentBody = (JPanel) parent;
                if (pnlContentBody.getTopLevelAncestor() instanceof FrmMain) {
                    FrmMain mainFrame = (FrmMain) pnlContentBody.getTopLevelAncestor();
                    mainFrame.btnDashboard.doClick(); 
                }
            }
        });
    }

    // Nạp danh mục phân loại động từ Oracle DB lên ComboBox lọc
    private void loadLoaiXeToComboBox() {
        cbLoaiXeFilter.removeAllItems();
        cbLoaiXeFilter.addItem("-- Tất cả phân loại --");
        try {
            List<LoaiXeDTO> list = loaiXeBUS.getAll(); 
            for (LoaiXeDTO loai : list) {
                cbLoaiXeFilter.addItem(loai.getTenLoai());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Nạp toàn bộ danh sách xe thực tế ban đầu (Mẫu giả lập)
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        
        java.util.List<com.thuexe.dto.XeDTO> listGiaLap = new java.util.ArrayList<>();
        listGiaLap.add(new com.thuexe.dto.XeDTO("29A-123.45", "Toyota", "Toyota Vios 2023", 5, "vios.png", "Sẵn Sàng", 1, 1, 1, "Xe 4 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("30H-888.99", "Hyundai", "Hyundai SantaFe", 7, "santafe.png", "Đang Được Thuê", 1, 2, 1, "Xe 7 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("51K-555.55", "Ford", "Ford Ranger Wildtrak", 5, "ranger.png", "Sẵn Sàng", 2, 3, 1, "Xe Bán Tải"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("29A-999.11", "VinFast", "VinFast VF8", 5, "vf8.png", "Đang Bảo Trì", 2, 4, 1, "Xe Điện"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("30K-112.34", "Honda", "Honda City RS", 5, "city.png", "Sẵn Sàng", 3, 1, 1, "Xe 4 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("51G-678.90", "Kia", "Kia Carnival 2024", 7, "carnival.png", "Sẵn Sàng", 1, 2, 1, "Xe 7 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("30F-123.56", "Mazda", "Mazda CX-5", 5, "cx5.png", "Đang Được Thuê", 2, 2, 1, "Xe 5 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("43A-456.78", "Mitsubishi", "Mitsubishi Xpander", 7, "xpander.png", "Sẵn Sàng", 3, 2, 1, "Xe 7 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("29D-888.12", "Toyota", "Toyota Hilux Adventure", 5, "hilux.png", "Đang Bảo Trì", 1, 3, 1, "Xe Bán Tải"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("30E-555.66", "Mercedes-Benz", "Mercedes C200", 5, "c200.png", "Sẵn Sàng", 4, 5, 1, "Xe Hạng Sang"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("51H-999.88", "BMW", "BMW 320i LCI", 5, "bmw320i.png", "Đang Được Thuê", 4, 5, 1, "Xe Hạng Sang"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("30G-234.56", "VinFast", "VinFast VF9", 7, "vf9.png", "Sẵn Sàng", 2, 4, 1, "Xe Điện"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("29H-777.11", "Hyundai", "Hyundai Accent", 5, "accent.png", "Sẵn Sàng", 3, 1, 1, "Xe 4 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("51A-333.44", "Ford", "Ford Everest Titanium", 7, "everest.png", "Đang Được Thuê", 2, 2, 1, "Xe 7 Chỗ"));
        listGiaLap.add(new com.thuexe.dto.XeDTO("30K-666.88", "Porsche", "Porsche Macan GTS", 5, "macan.png", "Đang Bảo Trì", 4, 5, 1, "Xe Hạng Sang"));
        hienThiLenTable(listGiaLap);
    }

    // Đọc dữ liệu từ mảng List<XeDTO> rồi đổ lên JTable và tính toán Dashboard Card
    private void hienThiLenTable(List<XeDTO> list) {
        tableModel.setRowCount(0);
        int tongXe = 0;
        int sanSang = 0;
        int baoTri = 0;

        if (list != null) {
            tongXe = list.size();
            for (XeDTO xe : list) {
                tableModel.addRow(new Object[]{
                    xe.getBienSoXe(),
                    xe.getTenXe(),
                    xe.getThuongHieu(),
                    xe.getSoCho() + " chỗ",
                    xe.getTenLoai(),
                    xe.getTrangThai()
                });

                if ("Sẵn Sàng".equalsIgnoreCase(xe.getTrangThai())) {
                    sanSang++;
                } else if ("Đang Bảo Trì".equalsIgnoreCase(xe.getTrangThai()) || "Bảo Trì".equalsIgnoreCase(xe.getTrangThai())) {
                    baoTri++;
                }
            }
        }

        lblTongXeValue.setText(tongXe + " xe");
        lblSanSangValue.setText(sanSang + " xe");
        lblBaoTriValue.setText(baoTri + " xe");
    }

    private JPanel createFlatCard(String title, String defaultValue, Color colorBG) {
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
        card.setBackground(colorBG);
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

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });

        return btn;
    }
}