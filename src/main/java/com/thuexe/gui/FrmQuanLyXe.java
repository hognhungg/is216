package com.thuexe.gui;

import com.thuexe.dao.LoaiXeDAO;
import com.thuexe.dao.XeDAO;
import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.dto.XeDTO;
import com.thuexe.util.ValidationUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FrmQuanLyXe extends JFrame {

    private JTextField txtBienSo, txtThuongHieu, txtTenXe, txtSoCho, txtHinhAnh;
    private JComboBox<LoaiXeDTO> cboLoaiXe;
    private JTable tblXe;
    private DefaultTableModel model;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnChonAnh;
    
    private XeDAO xeDAO = new XeDAO();
    private LoaiXeDAO loaiXeDAO = new LoaiXeDAO();

    // Định nghĩa bảng màu đồng bộ với hệ thống
    private static final Color COLOR_BG = new Color(245, 246, 248);
    private static final Color COLOR_TEXT_DARK = new Color(40, 44, 52);

    public FrmQuanLyXe() {
        initComponents();
        loadComboBoxLoaiXe();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Xe");
        setSize(1100, 650); // Nới rộng kích thước một chút để giao diện thoáng hơn
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Tạo panel nền chính có khoảng đệm border xung quanh
        JPanel pnlMain = new JPanel(new BorderLayout(15, 15));
        pnlMain.setBackground(COLOR_BG);
        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(pnlMain);

        // =========================================================================
        // PANEL NHẬP LIỆU (WEST)
        // =========================================================================
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin xe"));
        pnlInput.setBackground(COLOR_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addInputRow(pnlInput, "Biển số xe:", txtBienSo = new JTextField(15), 0);
        addInputRow(pnlInput, "Thương hiệu:", txtThuongHieu = new JTextField(15), 1);
        addInputRow(pnlInput, "Tên xe:", txtTenXe = new JTextField(15), 2);
        addInputRow(pnlInput, "Số chỗ:", txtSoCho = new JTextField(15), 3);
        
        // Hàng chọn ảnh
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        pnlInput.add(new JLabel("Hình ảnh:"), gbc);
        gbc.gridx = 1;
        txtHinhAnh = new JTextField(10);
        txtHinhAnh.setEditable(false);
        pnlInput.add(txtHinhAnh, gbc);
        gbc.gridx = 2;
        btnChonAnh = new JButton("...");
        pnlInput.add(btnChonAnh, gbc);

        // Hàng chọn Loại xe
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1;
        pnlInput.add(new JLabel("Loại xe:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboLoaiXe = new JComboBox<>();
        pnlInput.add(cboLoaiXe, gbc);

        pnlMain.add(pnlInput, BorderLayout.WEST);

        // =========================================================================
        // 🛠️ CỤM GIAO DIỆN TRUNG TÂM (CENTER) - BAO GỒM TIÊU ĐỀ, NÚT VÀ BẢNG DỮ LIỆU
        // =========================================================================
        JPanel pnlCenterContainer = new JPanel(new BorderLayout(0, 15));
        pnlCenterContainer.setBackground(COLOR_BG);

        // Thanh tiêu đề bảng phối hợp bộ nút Thêm, Sửa, Xóa (Nằm trên bảng)
        JPanel pnlActionRow = new JPanel(new BorderLayout());
        pnlActionRow.setBackground(COLOR_BG);

        // Thêm tiêu đề chữ đậm y hệt hình mẫu
        JLabel lblTableTitle = new JLabel("Danh Sách Phân Loại Xe Hệ Thống");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTableTitle.setForeground(COLOR_TEXT_DARK);
        pnlActionRow.add(lblTableTitle, BorderLayout.WEST);

        // Bộ nút tác vụ nhóm bên phải tiêu đề
        JPanel pnlTopButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTopButtons.setBackground(COLOR_BG);

        btnThem = createStyledButton("Thêm xe", new Color(74, 137, 95));
        btnSua = createStyledButton("Sửa", new Color(225, 140, 50));
        btnXoa = createStyledButton("Xóa", new Color(190, 50, 50));

        pnlTopButtons.add(btnThem);
        pnlTopButtons.add(btnSua);
        pnlTopButtons.add(btnXoa);
        pnlActionRow.add(pnlTopButtons, BorderLayout.EAST);

        // Đưa thanh tiêu đề + nút vào phía Bắc của khu trung tâm
        pnlCenterContainer.add(pnlActionRow, BorderLayout.NORTH);

        // Cấu trúc bảng hiển thị dữ liệu
        String[] headers = {"Biển số", "Tên xe", "Thương hiệu", "Số chỗ", "Mã Loại"};
        model = new DefaultTableModel(headers, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblXe = new JTable(model);
        tblXe.setRowHeight(35);
        tblXe.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblXe.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblXe.getTableHeader().setBackground(new Color(230, 235, 240));

        JScrollPane scrollPane = new JScrollPane(tblXe);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        pnlCenterContainer.add(scrollPane, BorderLayout.CENTER);

        pnlMain.add(pnlCenterContainer, BorderLayout.CENTER);

        // =========================================================================
        // PANEL NÚT BẤM PHỤ (SOUTH)
        // =========================================================================
        JPanel pnlBottomButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBottomButtons.setBackground(COLOR_BG);

        btnLamMoi = createStyledButton("Làm mới", new Color(110, 110, 110));
        pnlBottomButtons.add(btnLamMoi);
        pnlMain.add(pnlBottomButtons, BorderLayout.SOUTH);

        // =========================================================================
        // SỰ KIỆN KÍCH HOẠT CHỨC NĂNG
        // =========================================================================
        btnChonAnh.addActionListener(e -> chonAnh());
        btnThem.addActionListener(e -> xuLyThem());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> lamMoi());
        
        // Sự kiện click nút Sửa
        btnSua.addActionListener(e -> {
            int row = tblXe.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng xe trên bảng để chỉnh sửa!");
                return;
            }
            // Gọi phương thức xử lý cập nhật của ông tại đây
            JOptionPane.showMessageDialog(this, "Tính năng sửa thông tin xe.");
        });
        
        tblXe.getSelectionModel().addListSelectionListener(e -> {
            int row = tblXe.getSelectedRow();
            if (row >= 0) fillForm(row);
        });
    }

    private void addInputRow(JPanel pnl, String label, JTextField txt, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0; gbc.gridy = y;
        pnl.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        pnl.add(txt, gbc);
    }

    private void loadComboBoxLoaiXe() {
        List<LoaiXeDTO> list = loaiXeDAO.getAllLoaiXe(); 
        if (list != null) {
            for (LoaiXeDTO l : list) {
                cboLoaiXe.addItem(l);
            }
        }
    }

    private void loadDataToTable() {
        model.setRowCount(0);
        List<XeDTO> list = xeDAO.getAllXe();
        if (list != null) {
            for (XeDTO x : list) {
                model.addRow(new Object[]{
                    x.getBienSoXe(), x.getTenXe(), x.getThuongHieu(), 
                    x.getSoCho(), x.getMaLoaiXe()
                });
            }
        }
    }

    private void chonAnh() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                File destDir = new File("src/images");
                if (!destDir.exists()) destDir.mkdirs();

                File destFile = new File(destDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                txtHinhAnh.setText(selectedFile.getName());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi lưu ảnh: " + ex.getMessage());
            }
        }
    }

    private void xuLyThem() {
        String bienSo = txtBienSo.getText().trim();
        if (!ValidationUtil.isBienSoXe(bienSo)) {
            JOptionPane.showMessageDialog(this, "Biển số xe không đúng định dạng!");
            return;
        }

        XeDTO xe = new XeDTO();
        xe.setBienSoXe(bienSo);
        xe.setTenXe(txtTenXe.getText());
        xe.setThuongHieu(txtThuongHieu.getText());
        xe.setSoCho(Integer.parseInt(txtSoCho.getText()));
        xe.setHinhAnh(txtHinhAnh.getText());
        
        LoaiXeDTO loai = (LoaiXeDTO) cboLoaiXe.getSelectedItem();
        xe.setMaLoaiXe(loai.getMaLoaiXe());

        if (xeDAO.themXe(xe)) { 
            JOptionPane.showMessageDialog(this, "Thêm xe thành công!");
            loadDataToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Kiểm tra lại dữ liệu!");
        }
    }

    private void xuLyXoa() {
        int row = tblXe.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn xe cần xóa!");
            return;
        }
        String bienSo = model.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa xe biển số " + bienSo + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (xeDAO.xoaXe(bienSo)) { 
                JOptionPane.showMessageDialog(this, "Xóa xe thành công!");
                loadDataToTable();
                lamMoi();
            }
        }
    }

    private void fillForm(int row) {
        txtBienSo.setText(model.getValueAt(row, 0).toString());
        txtTenXe.setText(model.getValueAt(row, 1).toString());
        txtThuongHieu.setText(model.getValueAt(row, 2).toString());
        txtSoCho.setText(model.getValueAt(row, 3).toString());
    }

    private void lamMoi() {
        txtBienSo.setText("");
        txtTenXe.setText("");
        txtThuongHieu.setText("");
        txtSoCho.setText("");
        txtHinhAnh.setText("");
        tblXe.clearSelection();
    }

    // Hàm tạo nút bo góc đồng bộ màu sắc hiện đại giống các form trước
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
            @Override public void paint(Graphics g, JComponent c) {
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