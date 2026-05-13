package com.thuexe.gui;

import com.thuexe.bus.LoaiXeBUS;
import com.thuexe.dto.LoaiXeDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmLoaiXe extends JFrame {

    // Các thành phần giao diện
    private JTable tblLoaiXe;
    private DefaultTableModel tableModel;
    
    private JTextField txtMaLoai;
    private JTextField txtTenLoai;
    private JTextField txtNhienLieu;
    private JTextField txtGiaNgay;
    private JTextField txtGiaGio;
    
    private JButton btnCapNhat;
    private JButton btnLamMoi;
    private JButton btnThoat;

    // Đối tượng xử lý nghiệp vụ
    private LoaiXeBUS loaiXeBUS;

    public FrmLoaiXe() {
        loaiXeBUS = new LoaiXeBUS();
        initComponents();
        initEvents();
        // Giả sử BUS của bạn có hàm getAll() để lấy danh sách. Nếu chưa có, bạn cần tạo thêm trong LoaiXeBUS.
        // loadDataToTable(); 
    }

    // Hàm khởi tạo giao diện
    private void initComponents() {
        setTitle("Quản Lý Giá Thuê Loại Xe");
        setSize(800, 500);
        setLocationRelativeTo(null); // Hiển thị ở giữa màn hình
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chỉ đóng form này, không tắt cả app
        setLayout(new BorderLayout(10, 10));

        // ================= PHẦN TRÊN: FORM NHẬP LIỆU =================
        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin Loại Xe"));

        pnlInput.add(new JLabel("Mã loại xe:"));
        txtMaLoai = new JTextField();
        txtMaLoai.setEnabled(false); // Không cho phép sửa Khóa chính
        pnlInput.add(txtMaLoai);

        pnlInput.add(new JLabel("Tên loại xe:"));
        txtTenLoai = new JTextField();
        pnlInput.add(txtTenLoai);

        pnlInput.add(new JLabel("Nhiên liệu:"));
        txtNhienLieu = new JTextField();
        pnlInput.add(txtNhienLieu);

        pnlInput.add(new JLabel("Giá thuê Ngày (VNĐ):"));
        txtGiaNgay = new JTextField();
        pnlInput.add(txtGiaNgay);

        pnlInput.add(new JLabel("Giá thuê Giờ (VNĐ):"));
        txtGiaGio = new JTextField();
        pnlInput.add(txtGiaGio);

        // Dummy label để căn chỉnh bố cục GridLayout cho đẹp
        pnlInput.add(new JLabel("")); 
        pnlInput.add(new JLabel("")); 

        add(pnlInput, BorderLayout.NORTH);

        // ================= PHẦN GIỮA: BẢNG DỮ LIỆU =================
        String[] columnNames = {"Mã Loại", "Tên Loại", "Nhiên Liệu", "Giá Ngày", "Giá Giờ"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa trực tiếp trên bảng
            }
        };
        tblLoaiXe = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblLoaiXe);
        add(scrollPane, BorderLayout.CENTER);

        // ================= PHẦN DƯỚI: NÚT CHỨC NĂNG =================
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCapNhat = new JButton("Cập nhật Giá");
        btnLamMoi = new JButton("Làm mới Form");
        btnThoat = new JButton("Thoát");

        pnlButtons.add(btnCapNhat);
        pnlButtons.add(btnLamMoi);
        pnlButtons.add(btnThoat);
        add(pnlButtons, BorderLayout.SOUTH);
    }

    // Hàm bắt sự kiện
    private void initEvents() {
        // 1. Sự kiện click chuột vào một dòng trong bảng (JTable)
        tblLoaiXe.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblLoaiXe.getSelectedRow();
                if (row >= 0) {
                    txtMaLoai.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenLoai.setText(tableModel.getValueAt(row, 1).toString());
                    txtNhienLieu.setText(tableModel.getValueAt(row, 2).toString());
                    txtGiaNgay.setText(tableModel.getValueAt(row, 3).toString());
                    txtGiaGio.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        // 2. Sự kiện nhấn nút Cập nhật
        btnCapNhat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMaLoai.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(FrmLoaiXe.this, "Vui lòng chọn loại xe từ bảng để cập nhật!");
                    return;
                }

                // Hiển thị hộp thoại xác nhận trước khi sửa giá
                int confirm = JOptionPane.showConfirmDialog(FrmLoaiXe.this, 
                        "Bạn có chắc chắn muốn cập nhật thông tin loại xe này?", 
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        int maLoai = Integer.parseInt(txtMaLoai.getText());
                        String tenLoai = txtTenLoai.getText().trim();
                        String nhienLieu = txtNhienLieu.getText().trim();
                        double giaNgay = Double.parseDouble(txtGiaNgay.getText().trim());
                        double giaGio = Double.parseDouble(txtGiaGio.getText().trim());

                        // Đóng gói dữ liệu vào DTO
                        LoaiXeDTO loaiXeUpdate = new LoaiXeDTO(maLoai, tenLoai, nhienLieu, giaNgay, giaGio);

                        // Gọi BUS để cập nhật
                        String msg = loaiXeBUS.updateGiaThue(loaiXeUpdate);
                        if (msg.equals("SUCCESS")) {
                            JOptionPane.showMessageDialog(FrmLoaiXe.this, "Cập nhật thành công!");
                            clearFields();
                            // loadDataToTable(); // Cập nhật lại bảng
                        } else {
                            JOptionPane.showMessageDialog(FrmLoaiXe.this, "Lỗi: " + msg, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(FrmLoaiXe.this, 
                            "Dữ liệu Giá thuê phải là số hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // 3. Sự kiện nhấn nút Làm mới
        btnLamMoi.addActionListener(e -> clearFields());

        // 4. Sự kiện nhấn nút Thoát
        btnThoat.addActionListener(e -> this.dispose());
    }

    // Hàm xóa trắng các ô nhập liệu
    private void clearFields() {
        txtMaLoai.setText("");
        txtTenLoai.setText("");
        txtNhienLieu.setText("");
        txtGiaNgay.setText("");
        txtGiaGio.setText("");
        tblLoaiXe.clearSelection();
    }

    // [CHÚ Ý] Bạn cần bổ sung hàm getAll() vào LoaiXeBUS/LoaiXeDAO để hàm này hoạt động
    /*
    private void loadDataToTable() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        List<LoaiXeDTO> danhSach = loaiXeBUS.getAll();
        for (LoaiXeDTO loai : danhSach) {
            tableModel.addRow(new Object[]{
                loai.getMaLoaiXe(),
                loai.getTenLoai(),
                loai.getLoaiNhienLieu(),
                loai.getGiaThueNgay(),
                loai.getGiaThueGio()
            });
        }
    }
    */

    // Hàm main để chạy thử form (có thể xóa khi ráp vào project chính)
    public static void main(String[] args) {
        // Cài đặt giao diện Look and Feel giống Windows
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> {
            new FrmLoaiXe().setVisible(true);
        });
    }
}