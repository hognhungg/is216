package com.thuexe.gui;

import com.thuexe.bus.XeBUS;
import com.thuexe.bus.LoaiXeBUS;
import com.thuexe.dto.XeDTO;
import com.thuexe.dto.LoaiXeDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmXe extends JPanel {
    private JTable tblXe;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;
    private JComboBox<String> cbLoaiXe, cbTrangThai;
    private JButton btnTimKiem, btnThem, btnXoa, btnLamMoi;
    
    private XeBUS xeBUS;
    private LoaiXeBUS loaiXeBUS;

    public FrmXe() {
        xeBUS = new XeBUS();
        loaiXeBUS = new LoaiXeBUS();
        
        initComponents();      // Khởi tạo giao diện (Gần tương tự FrmLoaiXe của ông)
        loadLoaiXeToComboBox(); // Đổ dữ liệu phân loại vào ô lọc
        loadDataToTable();      // Đổ danh sách xe thực tế lên bảng
        initEvents();           // Gắn sự kiện nút bấm
    }

    private void initComponents() {
        // ... Thiết kế Layout, add các nút bấm, ô tìm kiếm và JTable ...
        // Các cột cho JTable xe cụ thể:
        String[] columnNames = {"Biển Số Xe", "Tên Xe", "Thương Hiệu", "Số Chỗ", "Phân Loại", "Trạng Thái"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tblXe = new JTable(tableModel);
        // ... Thêm ScrollPane ...
    }

    // 1. Gọi LoaiXeBUS để nạp dữ liệu cho ComboBox lọc
    private void loadLoaiXeToComboBox() {
        cbLoaiXe.removeAllItems();
        cbLoaiXe.addItem("-- Tất cả phân loại --");
        List<LoaiXeDTO> list = loaiXeBUS.getAll();
        for (LoaiXeDTO loai : list) {
            cbLoaiXe.addItem(loai.getTenLoai());
        }
    }

    // 2. Gọi XeBUS để nạp danh sách xe lên bảng JTable
    private void loadDataToTable() {
        tableModel.setRowCount(0);
        List<XeDTO> list = xeBUS.getAllXe();
        for (XeDTO xe : list) {
            tableModel.addRow(new Object[]{
                xe.getBienSoXe(), xe.getTenXe(), xe.getThuongHieu(),
                xe.getSoCho(), xe.getTenLoai(), xe.getTrangThai()
            });
        }
    }

    // 3. Xử lý sự kiện Tìm Kiếm Nâng Cao
    private void initEvents() {
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText();
            String trangThai = cbTrangThai.getSelectedItem().toString();
            
            // Lấy mã loại xe dựa theo index được chọn (Bổ trợ từ danh mục)
            int maLoaiXe = 0; 
            int selectedLoaiIndex = cbLoaiXe.getSelectedIndex();
            if (selectedLoaiIndex > 0) {
                // Lấy lại danh sách để dò ID tương ứng với index
                maLoaiXe = loaiXeBUS.getAll().get(selectedLoaiIndex - 1).getMaLoaiXe();            }

            // Gọi hàm tìm kiếm động siêu mượt dưới tầng BUS/DAO đã viết
            List<XeDTO> ketQua = xeBUS.timKiemXeNangCao(keyword, maLoaiXe, trangThai);
            
            // Cập nhật lại bảng hiển thị kết quả lọc
            tableModel.setRowCount(0);
            for (XeDTO xe : ketQua) {
                tableModel.addRow(new Object[]{
                    xe.getBienSoXe(), xe.getTenXe(), xe.getThuongHieu(),
                    xe.getSoCho(), xe.getTenLoai(), xe.getTrangThai()
                });
            }
        });
    }
}