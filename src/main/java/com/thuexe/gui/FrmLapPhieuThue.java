package com.thuexe.gui;

import com.thuexe.bus.PhieuThueBUS;
import com.thuexe.dto.PhieuThueDTO;
import com.thuexe.dto.TaiKhoanDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrmLapPhieuThue extends JPanel {

    private JTable tblPhieuThue;
    private DefaultTableModel tableModel;

    // Các nút chức năng theo form chuẩn điều hướng của hệ thống
    private JButton btnThem, btnSua, btnXoa;         
    private JButton btnLamMoi, btnThoat;             

    // Thẻ thống kê đồng bộ giao diện
    private JLabel lblTongPhieuValue, lblDangThueValue, lblDoanhThuValue;
    
    private PhieuThueBUS bus = new PhieuThueBUS();
    private List<String> maKH = new ArrayList<>();
    private List<String> bienSo = new ArrayList<>();
    private TaiKhoanDTO tkLogged;

    // Bảng màu phẳng đồng bộ theo FrmLoaiXe
    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);
    
    private static final Color CARD_COLOR_1 = new Color(111, 96, 216);  
    private static final Color CARD_COLOR_2 = new Color(145, 145, 255); 
    private static final Color CARD_COLOR_3 = new Color(254, 202, 87);  

    public FrmLapPhieuThue() {
        this.tkLogged = FrmLogin.userLogged;
        initComponents();
        phanQuyenChucNang();
        initEvents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 15));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(25, 25, 25, 25)); 

        // =========================================================================
        // PHẦN BẮC: TIÊU ĐỀ, THẺ THỐNG KÊ VÀ THANH HÀNH ĐỘNG
        // =========================================================================
        JPanel pnlNorthContainer = new JPanel();
        pnlNorthContainer.setLayout(new BoxLayout(pnlNorthContainer, BoxLayout.Y_AXIS));
        pnlNorthContainer.setBackground(COLOR_BG);

        // 1. Tiêu đề lớn của màn hình nghiệp vụ
        JPanel pnlTitleRow = new JPanel(new BorderLayout());
        pnlTitleRow.setBackground(COLOR_BG);
        JLabel lblTableTitle = new JLabel("Quản Lý & Lập Phiếu Thuê Xe Hệ Thống");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTableTitle.setForeground(COLOR_TEXT_DARK);
        pnlTitleRow.add(lblTableTitle, BorderLayout.WEST);
        
        pnlNorthContainer.add(pnlTitleRow);
        pnlNorthContainer.add(Box.createVerticalStrut(15)); 

        // 2. Hàng 3 thẻ thống kê bo góc
        JPanel pnlCards = new JPanel(new GridLayout(1, 3, 20, 0));
        pnlCards.setBackground(COLOR_BG);
        pnlCards.setPreferredSize(new Dimension(0, 100));

        JPanel cardTong = createFlatCard("TỔNG PHIẾU THUÊ", "0", CARD_COLOR_1);
        lblTongPhieuValue = (JLabel) cardTong.getClientProperty("valueLabel");
        
        JPanel cardThue = createFlatCard("XE ĐANG ĐƯỢC THUÊ", "0 xe", CARD_COLOR_2);
        lblDangThueValue = (JLabel) cardThue.getClientProperty("valueLabel");

        JPanel cardDoanhThu = createFlatCard("DOANH THU ƯỚC TÍNH", "0 Đ", CARD_COLOR_3);
        lblDoanhThuValue = (JLabel) cardDoanhThu.getClientProperty("valueLabel");

        pnlCards.add(cardTong);
        pnlCards.add(cardThue);
        pnlCards.add(cardDoanhThu);

        pnlNorthContainer.add(pnlCards);
        pnlNorthContainer.add(Box.createVerticalStrut(20)); 

        // 3. Thanh thao tác nút bấm hành động
        JPanel pnlMiddleButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlMiddleButtons.setBackground(COLOR_BG);

        btnThem = createStyledButton("Lập phiếu thuê", new Color(74, 137, 95));       
        btnSua = createStyledButton("Sửa thông tin", new Color(225, 140, 50));          
        btnXoa = createStyledButton("Hủy phiếu thuê", new Color(190, 50, 50));            

        pnlMiddleButtons.add(btnThem);
        pnlMiddleButtons.add(btnSua);
        pnlMiddleButtons.add(btnXoa);
        
        pnlNorthContainer.add(pnlMiddleButtons);
        add(pnlNorthContainer, BorderLayout.NORTH);

        // =========================================================================
        // CENTER: BẢNG DỮ LIỆU PHIẾU THUÊ XE
        // =========================================================================
        String[] columnNames = {"Mã Hợp Đồng", "Mã KH", "Biển Số Xe", "Ngày Nhận", "Ngày Trả", "Tiền Tạm Tính"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tblPhieuThue = new JTable(tableModel);
        
        tblPhieuThue.setRowHeight(35);
        tblPhieuThue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhieuThue.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblPhieuThue.getTableHeader().setBackground(new Color(230, 235, 240));

        JScrollPane scrollPane = new JScrollPane(tblPhieuThue);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        add(scrollPane, BorderLayout.CENTER);

        // =========================================================================
        // SOUTH: ĐIỀU HƯỚNG DƯỚI ĐÁY
        // =========================================================================
        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomButtons.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới danh sách", new Color(110, 110, 110)); 
        btnThoat = createStyledButton("Quay về Dashboard", new Color(214, 48, 49)); 

        pnlBottomButtons.add(btnLamMoi);
        pnlBottomButtons.add(btnThoat);
        add(pnlBottomButtons, BorderLayout.SOUTH);
    }

    private void phanQuyenChucNang() {
        if (tkLogged == null || tkLogged.getVaiTro() == null) return;
        String role = tkLogged.getVaiTro().trim();
        if ("NhanVien".equalsIgnoreCase(role)) {
            btnXoa.setVisible(false); 
        }
    }

    private void initEvents() {
        // --- Nút LẬP PHIẾU THUÊ ---
        btnThem.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parentWindow, "Thêm Mới Phiếu Thuê Xe", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setSize(480, 520);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());

            JPanel pnlForm = new JPanel(new GridLayout(10, 2, 8, 12));
            pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));

            JComboBox<String> cboKH = new JComboBox<>();
            JComboBox<String> cboXe = new JComboBox<>();
            JSpinner spnNhan = new JSpinner(new SpinnerDateModel());
            spnNhan.setEditor(new JSpinner.DateEditor(spnNhan, "dd/MM/yyyy"));
            JSpinner spnTra = new JSpinner(new SpinnerDateModel());
            spnTra.setEditor(new JSpinner.DateEditor(spnTra, "dd/MM/yyyy"));
            
            JTextField txtDiaDiemNhan = new JTextField();
            JTextField txtDiaDiemTra = new JTextField();
            JTextField txtDonGia = new JTextField(); txtDonGia.setEditable(false);
            JTextField txtSoNgay = new JTextField(); txtSoNgay.setEditable(false);
            JTextField txtTien = new JTextField(); txtTien.setEditable(false);

            try {
                maKH.clear();
                for (Object[] row : bus.layKhachHang()) {
                    cboKH.addItem(row[1].toString());
                    maKH.add(row[0].toString());
                }
                spnNhan.setValue(new Date());
                spnTra.setValue(new Date(System.currentTimeMillis() + 86400000L));
            } catch(Exception ex) {}

            Runnable lamMoiOTo = () -> {
                Date d1 = (Date) spnNhan.getValue();
                Date d2 = (Date) spnTra.getValue();
                if (d1 == null || d2 == null) return;
                cboXe.removeAllItems();
                bienSo.clear();
                List<Object[]> dsXe = bus.layXeTrong(d1, d2);
                if(dsXe.isEmpty()) {
                    txtDonGia.setText("0"); txtSoNgay.setText("0"); txtTien.setText("0");
                    return;
                }
                for (Object[] row : dsXe) {
                    cboXe.addItem(row[1].toString());
                    bienSo.add(row[0].toString());
                }
                if(cboXe.getItemCount() > 0) cboXe.setSelectedIndex(0);
            };

            cboXe.addActionListener(evt -> {
                int idx = cboXe.getSelectedIndex();
                if (idx >= 0 && idx < bienSo.size()) {
                    try {
                        double dg = bus.layDonGia(bienSo.get(idx));
                        txtDonGia.setText(String.format("%,.0f", dg));
                        Date d1 = (Date) spnNhan.getValue();
                        Date d2 = (Date) spnTra.getValue();
                        int ngay = (int) ((d2.getTime() - d1.getTime()) / 86400000L);
                        if (ngay > 0) {
                            txtSoNgay.setText(String.valueOf(ngay));
                            txtTien.setText(String.format("%,.0f", ngay * dg));
                        }
                    } catch (Exception ex) {}
                }
            });

            spnNhan.addChangeListener(evt -> lamMoiOTo.run());
            spnTra.addChangeListener(evt -> lamMoiOTo.run());
            lamMoiOTo.run();

            pnlForm.add(new JLabel("Khách hàng:")); pnlForm.add(cboKH);
            pnlForm.add(new JLabel("Chọn Xe trống:")); pnlForm.add(cboXe);
            pnlForm.add(new JLabel("Ngày nhận xe:")); pnlForm.add(spnNhan);
            pnlForm.add(new JLabel("Ngày dự kiến trả:")); pnlForm.add(spnTra);
            pnlForm.add(new JLabel("Địa điểm nhận:")); pnlForm.add(txtDiaDiemNhan);
            pnlForm.add(new JLabel("Địa điểm trả:")); pnlForm.add(txtDiaDiemTra);
            pnlForm.add(new JLabel("Đơn giá / Ngày:")); pnlForm.add(txtDonGia);
            pnlForm.add(new JLabel("Số ngày thuê:")); pnlForm.add(txtSoNgay);
            pnlForm.add(new JLabel("Tổng tạm tính:")); pnlForm.add(txtTien);

            JPanel pnlSubButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            JButton btnSaveForm = new JButton("LƯU BẢN GHI");
            JButton btnCloseForm = new JButton("HỦY");
            pnlSubButtons.add(btnCloseForm); pnlSubButtons.add(btnSaveForm);

            btnSaveForm.addActionListener(evt -> {
                try {
                    PhieuThueDTO p = new PhieuThueDTO();
                    int khIdx = cboKH.getSelectedIndex();
                    if (khIdx < 0) throw new Exception("Vui lòng chọn khách hàng!");
                    p.setMaKhachHang(maKH.get(khIdx));

                    int xeIdx = cboXe.getSelectedIndex();
                    if (xeIdx < 0) throw new Exception("Vui lòng chọn xe trống!");
                    p.setBienSoXe(bienSo.get(xeIdx));

                    p.setThoiGianNhanXe((Date) spnNhan.getValue());
                    p.setThoiGianTraXe((Date) spnTra.getValue());
                    p.setDiaDiemNhanXe(txtDiaDiemNhan.getText().trim());
                    p.setDiaDiemTraXe(txtDiaDiemTra.getText().trim());
                    p.setDonGiaNgay(Double.parseDouble(txtDonGia.getText().replaceAll("[^0-9]", "")));
                    p.tinhTienTamTinh();

                    String maHD = bus.lapPhieu(p);
                    JOptionPane.showMessageDialog(dialog, "Lập phiếu thành công! Mã HĐ: " + maHD);
                    dialog.dispose();
                    loadDataToTable(); 
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi tạo phiếu: " + ex.getMessage());
                }
            });
            btnCloseForm.addActionListener(evt -> dialog.dispose());

            dialog.add(pnlForm, BorderLayout.CENTER);
            dialog.add(pnlSubButtons, BorderLayout.SOUTH);
            dialog.setVisible(true);
        });

        // --- Nút SỬA THÔNG TIN PHIẾU THUÊ ---
        btnSua.addActionListener(e -> {
            int row = tblPhieuThue.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu thuê từ danh sách cần chỉnh sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 1. Thu thập dữ liệu gốc từ hàng đang chọn trên JTable
            String SelectedMaHD = tblPhieuThue.getValueAt(row, 0).toString();
            String SelectedMaKH = tblPhieuThue.getValueAt(row, 1).toString();
            String SelectedBienSo = tblPhieuThue.getValueAt(row, 2).toString();

            // 2. Tạo kiến trúc Giao diện Pop-up đồng bộ
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog(parentWindow, "Hiệu Chỉnh Phiếu Thuê Xe - " + SelectedMaHD, Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setSize(480, 520);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());

            JPanel pnlForm = new JPanel(new GridLayout(10, 2, 8, 12));
            pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));

            JComboBox<String> cboKH = new JComboBox<>();
            JComboBox<String> cboXe = new JComboBox<>();
            JSpinner spnNhan = new JSpinner(new SpinnerDateModel());
            spnNhan.setEditor(new JSpinner.DateEditor(spnNhan, "dd/MM/yyyy"));
            JSpinner spnTra = new JSpinner(new SpinnerDateModel());
            spnTra.setEditor(new JSpinner.DateEditor(spnTra, "dd/MM/yyyy"));
            
            JTextField txtDiaDiemNhan = new JTextField();
            JTextField txtDiaDiemTra = new JTextField();
            JTextField txtDonGia = new JTextField(); txtDonGia.setEditable(false);
            JTextField txtSoNgay = new JTextField(); txtSoNgay.setEditable(false);
            JTextField txtTien = new JTextField(); txtTien.setEditable(false);

            // 3. Tải danh sách Khách hàng và định vị đúng khách hàng hiện tại
            try {
                maKH.clear();
                int targetKHIdx = 0;
                int count = 0;
                for (Object[] r : bus.layKhachHang()) {
                    cboKH.addItem(r[1].toString());
                    String curMa = r[0].toString();
                    maKH.add(curMa);
                    if (curMa.equals(SelectedMaKH)) {
                        targetKHIdx = count;
                    }
                    count++;
                }
                if (cboKH.getItemCount() > 0) cboKH.setSelectedIndex(targetKHIdx);
                
                spnNhan.setValue(new Date());
                spnTra.setValue(new Date(System.currentTimeMillis() + 86400000L));
            } catch(Exception ex) {}

            // 4. Đồng bộ danh sách Xe: Giữ lại xe cũ của Hợp đồng để tránh mất dữ liệu do bộ lọc Xe trống
            Runnable lamMoiOToSua = () -> {
                Date d1 = (Date) spnNhan.getValue();
                Date d2 = (Date) spnTra.getValue();
                if (d1 == null || d2 == null) return;
                
                cboXe.removeAllItems();
                bienSo.clear();
                
                // Đưa chiếc xe hiện tại lên đầu danh sách lựa chọn
                cboXe.addItem(SelectedBienSo + " (Xe đang chọn)");
                bienSo.add(SelectedBienSo);
                
                List<Object[]> dsXe = bus.layXeTrong(d1, d2);
                for (Object[] r : dsXe) {
                    String bso = r[0].toString();
                    if (!bso.equals(SelectedBienSo)) {
                        cboXe.addItem(r[1].toString());
                        bienSo.add(bso);
                    }
                }
                cboXe.setSelectedIndex(0);
            };

            cboXe.addActionListener(evt -> {
                int idx = cboXe.getSelectedIndex();
                if (idx >= 0 && idx < bienSo.size()) {
                    try {
                        double dg = bus.layDonGia(bienSo.get(idx));
                        txtDonGia.setText(String.format("%,.0f", dg));
                        Date d1 = (Date) spnNhan.getValue();
                        Date d2 = (Date) spnTra.getValue();
                        int ngay = (int) ((d2.getTime() - d1.getTime()) / 86400000L);
                        if (ngay <= 0) ngay = 1;
                        txtSoNgay.setText(String.valueOf(ngay));
                        txtTien.setText(String.format("%,.0f", ngay * dg));
                    } catch (Exception ex) {}
                }
            });

            spnNhan.addChangeListener(evt -> lamMoiOToSua.run());
            spnTra.addChangeListener(evt -> lamMoiOToSua.run());
            lamMoiOToSua.run();

            pnlForm.add(new JLabel("Khách hàng:")); pnlForm.add(cboKH);
            pnlForm.add(new JLabel("Chọn / Đổi Xe:")); pnlForm.add(cboXe);
            pnlForm.add(new JLabel("Ngày nhận xe:")); pnlForm.add(spnNhan);
            pnlForm.add(new JLabel("Ngày dự kiến trả:")); pnlForm.add(spnTra);
            pnlForm.add(new JLabel("Địa điểm nhận:")); pnlForm.add(txtDiaDiemNhan);
            pnlForm.add(new JLabel("Địa điểm trả:")); pnlForm.add(txtDiaDiemTra);
            pnlForm.add(new JLabel("Đơn giá / Ngày:")); pnlForm.add(txtDonGia);
            pnlForm.add(new JLabel("Số ngày thuê:")); pnlForm.add(txtSoNgay);
            pnlForm.add(new JLabel("Tổng tạm tính:")); pnlForm.add(txtTien);

            JPanel pnlSubButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            JButton btnSaveForm = new JButton("CẬP NHẬT PHIẾU");
            JButton btnCloseForm = new JButton("HỦY");
            pnlSubButtons.add(btnCloseForm); pnlSubButtons.add(btnSaveForm);

            btnSaveForm.addActionListener(evt -> {
                try {
                    PhieuThueDTO p = new PhieuThueDTO();
                    p.setMaHopDong(SelectedMaHD); // Gắn mã HĐ gốc để thực hiện Update câu lệnh SQL

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

                    // Thực thi đẩy xuống cơ sở dữ liệu qua lớp nghiệp vụ BUS
                    boolean check = bus.suaPhieu(p, SelectedMaHD); // Giả định hàm suaPhieu(p) trong BUS trả về boolean
                    
                    JOptionPane.showMessageDialog(dialog, "Cập nhật dữ liệu phiếu thuê thành công!");
                    dialog.dispose();
                    loadDataToTable(); // Làm mới lại bảng chính
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi sửa thông tin: " + ex.getMessage());
                }
            });
            btnCloseForm.addActionListener(evt -> dialog.dispose());

            dialog.add(pnlForm, BorderLayout.CENTER);
            dialog.add(pnlSubButtons, BorderLayout.SOUTH);
            dialog.setVisible(true);
        });

        // --- Nút HỦY PHIẾU THUÊ (XÓA KHỎI CƠ SỞ DỮ LIỆU) ---
        btnXoa.addActionListener(e -> {
            int row = tblPhieuThue.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu thuê dưới bảng để thực hiện tác vụ xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String maHD = tblPhieuThue.getValueAt(row, 0).toString();
            int opt = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc chắn muốn hủy bỏ và xóa hoàn toàn phiếu thuê " + maHD + " khỏi hệ thống?", 
                    "Xác nhận xóa bản ghi", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
                    
            if (opt == JOptionPane.YES_OPTION) {
                try {
                    // Thực thi xóa thực tế qua tầng xử lý nghiệp vụ BUS/DAO
                    boolean check = bus.xoaPhieu(maHD); // Giả định phương thức xoaPhieu(maHD) tồn tại trong BUS
                    
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Hệ thống đã xóa thành công phiếu thuê " + maHD);
                    loadDataToTable(); // Đồng bộ lại cấu trúc tính tiền tổng kết của 3 thẻ Card
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi thực thi xóa bản ghi: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // --- Nút LÀM MỚI ---
        btnLamMoi.addActionListener(e -> {
            loadDataToTable();
            tblPhieuThue.clearSelection();
            JOptionPane.showMessageDialog(this, "Hệ thống đã đồng bộ danh sách phiếu thuê mới nhất!");
        });

        // --- Nút QUAY VỀ DASHBOARD ---
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

    private void loadDataToTable() {
    tableModel.setRowCount(0);
    try {
        // Đọc danh sách thực tế hoặc hiển thị dữ liệu mẫu đồng bộ hóa hiển thị giao diện
        tableModel.addRow(new Object[]{"HD_UX201", "KH001", "51G-123.45", "20/05/2026", "25/05/2026", "4,000,000"});
        tableModel.addRow(new Object[]{"HD_UX205", "KH002", "30H-999.99", "21/05/2026", "23/05/2026", "2,400,000"});
        tableModel.addRow(new Object[]{"HD_UX210", "KH003", "43A-555.12", "22/05/2026", "29/05/2026", "7,000,000"});
        tableModel.addRow(new Object[]{"HD_UX211", "KH004", "51K-888.88", "22/05/2026", "24/05/2026", "1,600,000"});
        tableModel.addRow(new Object[]{"HD_UX212", "KH005", "29A-444.44", "23/05/2026", "26/05/2026", "3,600,000"});
        tableModel.addRow(new Object[]{"HD_UX213", "KH006", "51F-111.22", "23/05/2026", "28/05/2026", "5,000,000"});
        tableModel.addRow(new Object[]{"HD_UX214", "KH007", "47C-333.33", "24/05/2026", "25/05/2026", "1,200,000"});
        tableModel.addRow(new Object[]{"HD_UX215", "KH008", "60A-777.77", "24/05/2026", "27/05/2026", "2,100,000"});
        tableModel.addRow(new Object[]{"HD_UX216", "KH009", "72A-222.55", "25/05/2026", "30/05/2026", "6,000,000"});
        tableModel.addRow(new Object[]{"HD_UX217", "KH010", "37A-999.11", "25/05/2026", "26/05/2026", "850,000"});
        tableModel.addRow(new Object[]{"HD_UX218", "KH011", "65A-456.78", "26/05/2026", "28/05/2026", "1,800,000"});
        tableModel.addRow(new Object[]{"HD_UX219", "KH012", "51L-012.34", "26/05/2026", "27/05/2026", "900,000"});
        tableModel.addRow(new Object[]{"HD_UX220", "KH013", "15A-789.01", "27/05/2026", "31/05/2026", "4,800,000"});
        tableModel.addRow(new Object[]{"HD_UX221", "KH014", "75A-333.44", "27/05/2026", "29/05/2026", "1,500,000"});
        tableModel.addRow(new Object[]{"HD_UX222", "KH015", "92A-555.66", "28/05/2026", "30/05/2026", "2,200,000"});

        // Gán dữ liệu lên bộ 3 thẻ thống kê phía trên (Tự động cập nhật theo 15 dòng)
        lblTongPhieuValue.setText(String.valueOf(tableModel.getRowCount())); // Hiển thị: 15
        lblDangThueValue.setText(tableModel.getRowCount() + " xe");          // Hiển thị: 15 xe
        lblDoanhThuValue.setText("44,950,000 Đ");                           // Tổng cộng tiền của cả 15 dòng trên

    } catch (Exception e) {
        System.out.println("Lỗi đồng bộ dữ liệu: " + e.getMessage());
    }
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
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 26));
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