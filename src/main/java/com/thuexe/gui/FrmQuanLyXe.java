package com.thuexe.gui;

import com.thuexe.dao.LoaiXeDAO;
import com.thuexe.dao.XeDAO;
import com.thuexe.dto.LoaiXeDTO;
import com.thuexe.dto.XeDTO;
import com.thuexe.util.ValidationUtil;

import javax.swing.*;
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

    public FrmQuanLyXe() {
        initComponents();
        loadComboBoxLoaiXe();
        loadDataToTable();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Xe");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- Panel Nhập liệu (West) ---
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin xe"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addInputRow(pnlInput, "Biển số xe:", txtBienSo = new JTextField(15), 0);
        addInputRow(pnlInput, "Thương hiệu:", txtThuongHieu = new JTextField(15), 1);
        addInputRow(pnlInput, "Tên xe:", txtTenXe = new JTextField(15), 2);
        addInputRow(pnlInput, "Số chỗ:", txtSoCho = new JTextField(15), 3);
        
        // Hàng chọn ảnh
        gbc.gridx = 0; gbc.gridy = 4;
        pnlInput.add(new JLabel("Hình ảnh:"), gbc);
        gbc.gridx = 1;
        txtHinhAnh = new JTextField(10);
        txtHinhAnh.setEditable(false);
        pnlInput.add(txtHinhAnh, gbc);
        gbc.gridx = 2;
        btnChonAnh = new JButton("...");
        pnlInput.add(btnChonAnh, gbc);

        // Hàng chọn Loại xe
        gbc.gridx = 0; gbc.gridy = 5;
        pnlInput.add(new JLabel("Loại xe:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        cboLoaiXe = new JComboBox<>();
        pnlInput.add(cboLoaiXe, gbc);

        add(pnlInput, BorderLayout.WEST);

        // --- Panel Bảng (Center) ---
        model = new DefaultTableModel(new Object[]{"Biển số", "Tên xe", "Thương hiệu", "Số chỗ", "Mã Loại"}, 0);
        tblXe = new JTable(model);
        add(new JScrollPane(tblXe), BorderLayout.CENTER);

        // --- Panel Nút bấm (South) ---
        JPanel pnlButtons = new JPanel();
        pnlButtons.add(btnThem = new JButton("Thêm mới"));
        pnlButtons.add(btnSua = new JButton("Cập nhật"));
        pnlButtons.add(btnXoa = new JButton("Xóa xe"));
        pnlButtons.add(btnLamMoi = new JButton("Làm mới"));
        add(pnlButtons, BorderLayout.SOUTH);

        // --- Sự kiện ---
        btnChonAnh.addActionListener(e -> chonAnh());
        btnThem.addActionListener(e -> xuLyThem());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> lamMoi());
        
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
        for (LoaiXeDTO l : list) {
            cboLoaiXe.addItem(l);
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

        // Nếu xeDAO của bạn tên hàm là themXe thì để themXe, nếu là insertXe thì đổi thành insertXe nhé
        if (xeDAO.themXe(xe)) { 
            JOptionPane.showMessageDialog(this, "Thêm xe thành công!");
            loadDataToTable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại. Kiểm tra lại dữ liệu!");
        }
    }

    private void xuLyXoa() {
        int row = tblXe.getSelectedRow();
        if (row < 0) return;
        String bienSo = model.getValueAt(row, 0).toString();
        // Nếu xeDAO của bạn tên hàm là xoaXe thì để xoaXe, nếu là deleteXe thì đổi thành deleteXe
        if (xeDAO.xoaXe(bienSo)) { 
            loadDataToTable();
            lamMoi();
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
}