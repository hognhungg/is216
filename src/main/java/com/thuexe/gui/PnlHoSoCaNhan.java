package com.thuexe.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.thuexe.bus.KhachHangBUS;
import com.thuexe.bus.NhanVienBUS;
import com.thuexe.dao.KhachHangDAO;
import com.thuexe.dto.KhachHangDTO;

public class PnlHoSoCaNhan extends JPanel {
    private JLabel lblAvatar;
    private JButton btnChangeAvt, btnEditSave, btnChangePass;
    private JPanel pnlFields;
    
    // Các trường dữ liệu biểu mẫu Flat
    private JTextField txtField1, txtField2, txtField3, txtField4, txtField5, txtField6, txtField7;
    private JLabel lbl1, lbl2, lbl3, lbl4, lbl5, lbl6, lbl7;

    private int maChuThe;
    private String tenDangNhap;
    private String vaiTro;
    private String passwordHash;
    private boolean isEditing = false;
    
    private KhachHangDAO khDAO = new KhachHangDAO();
    private KhachHangBUS khBUS = new KhachHangBUS();
    private NhanVienBUS nvBUS = new NhanVienBUS();
    
    private KhachHangDTO currentKH;
    private Object[] currentNV; 

    public PnlHoSoCaNhan(int maChuThe, String tenDangNhap, String vaiTro, String passwordHash) {
        this.maChuThe = maChuThe;
        this.tenDangNhap = tenDangNhap;
        this.vaiTro = vaiTro;
        this.passwordHash = passwordHash;
        
        initComponents();
        loadDataAndTargetRole();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // --- CỤM BÊN TRÁI: AVATAR ---
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setOpaque(false);
        
        lblAvatar = new JLabel("Default Icon User", SwingConstants.CENTER);
        lblAvatar.setPreferredSize(new Dimension(160, 160));
        lblAvatar.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        
        btnChangeAvt = new JButton("Thay đổi ảnh đại diện");
        btnChangeAvt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        pnlLeft.add(lblAvatar, BorderLayout.CENTER);
        pnlLeft.add(btnChangeAvt, BorderLayout.SOUTH);

        // --- CỤM BÊN PHẢI: KHUNG THÔNG TIN CHI TIẾT ---
        JPanel pnlRight = new JPanel(new BorderLayout(15, 15));
        pnlRight.setBackground(Color.WHITE);
        pnlRight.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(240, 240, 240), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        pnlFields = new JPanel(new GridBagLayout());
        pnlFields.setOpaque(false);
        
        txtField1 = createFlatTextField();
        txtField2 = createFlatTextField();
        txtField3 = createFlatTextField();
        txtField4 = createFlatTextField();
        txtField5 = createFlatTextField();
        txtField6 = createFlatTextField();
        txtField7 = createFlatTextField();

        lbl1 = new JLabel(); lbl2 = new JLabel(); lbl3 = new JLabel();
        lbl4 = new JLabel(); lbl5 = new JLabel(); lbl6 = new JLabel(); lbl7 = new JLabel();

        pnlRight.add(pnlFields, BorderLayout.CENTER);

        // --- ĐÁY GÓC PHẢI: NÚT ĐIỀU KHIỂN ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        pnlBottom.setOpaque(false);
        
        btnEditSave = new JButton("Chỉnh sửa hồ sơ");
        btnChangePass = new JButton("Đổi mật khẩu");
        
        pnlBottom.add(btnChangePass);
        pnlBottom.add(btnEditSave);
        pnlRight.add(pnlBottom, BorderLayout.SOUTH);

        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        btnEditSave.addActionListener(e -> handleEditOrSave());
        btnChangePass.addActionListener(e -> {
            Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            new DlgDoiMatKhau(topFrame, tenDangNhap, passwordHash).setVisible(true);
        });
    }

    private JTextField createFlatTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setEditable(false);
        field.setBackground(Color.WHITE);
        // Thiết kế Flat: Chỉ giữ lại đường gạch dưới nhạt
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        return field;
    }

    private void addFieldToGrid(JLabel label, JTextField field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.25;
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnlFields.add(label, gbc);
        
        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.75;
        pnlFields.add(field, gbc);
    }

    private void loadDataAndTargetRole() {
        pnlFields.removeAll();
        if ("KhachHang".equalsIgnoreCase(vaiTro) || "Khach hang".equalsIgnoreCase(vaiTro)) {
            setupCustomerUI();
        } else {
            setupEmployeeUI();
        }
        pnlFields.revalidate();
        pnlFields.repaint();
    }

    private void setupCustomerUI() {
        lbl1.setText("Mã khách hàng (Chỉ xem):");
        lbl2.setText("Tên tài khoản (Chỉ xem):");
        lbl3.setText("Họ và tên:");
        lbl4.setText("Số điện thoại:");
        lbl5.setText("Số CMND/CCCD (Chỉ xem):");
        lbl6.setText("Số bằng lái:");
        lbl7.setText("Hạng thành viên (Chỉ xem):");

        addFieldToGrid(lbl1, txtField1, 0);
        addFieldToGrid(lbl2, txtField2, 1);
        addFieldToGrid(lbl3, txtField3, 2);
        addFieldToGrid(lbl4, txtField4, 3);
        addFieldToGrid(lbl5, txtField5, 4);
        addFieldToGrid(lbl6, txtField6, 5);
        addFieldToGrid(lbl7, txtField7, 6);

        currentKH = khDAO.getByMaChuThe(maChuThe);
        if (currentKH != null) {
            txtField1.setText(String.valueOf(currentKH.getMaKhachHang()));
            txtField2.setText(tenDangNhap);
            txtField3.setText(currentKH.getHoTen());
            txtField4.setText(currentKH.getSdt());
            txtField5.setText(currentKH.getCccd());
            txtField6.setText(currentKH.getSoBangLai());
            txtField7.setText("Vàng (Mặc định)"); // Đổ dữ liệu giả định hạng thành viên
        }
    }

    private void setupEmployeeUI() {
        lbl1.setText("Mã số nhân viên (Chỉ xem):");
        lbl2.setText("Họ và tên nhân viên (Chỉ xem):");
        lbl3.setText("Tên đăng nhập hệ thống (Chỉ xem):");
        lbl4.setText("Số điện thoại:");
        lbl5.setText("Email công việc:");
        lbl6.setText("Số căn cước công dân (Chỉ xem):");
        lbl7.setText("Chi nhánh / Phòng ban (Chỉ xem):");

        addFieldToGrid(lbl1, txtField1, 0);
        addFieldToGrid(lbl2, txtField2, 1);
        addFieldToGrid(lbl3, txtField3, 2);
        addFieldToGrid(lbl4, txtField4, 3);
        addFieldToGrid(lbl5, txtField5, 4);
        addFieldToGrid(lbl6, txtField6, 5);
        addFieldToGrid(lbl7, txtField7, 6);

        currentNV = nvBUS.getProfile(maChuThe);
        if (currentNV != null) {
            txtField1.setText(String.valueOf(currentNV[0]));
            txtField2.setText(String.valueOf(currentNV[1]));
            txtField3.setText(tenDangNhap);
            txtField4.setText(currentNV[3] != null ? String.valueOf(currentNV[3]) : "");
            txtField5.setText(currentNV[4] != null ? String.valueOf(currentNV[4]) : "");
            txtField6.setText(String.valueOf(currentNV[2]));
            txtField7.setText(currentNV[5] != null ? String.valueOf(currentNV[5]) : "Chi nhánh chính");
        }
    }

    private void handleEditOrSave() {
        if (!isEditing) {
            isEditing = true;
            btnEditSave.setText("Lưu thay đổi");
            btnEditSave.setBackground(new Color(46, 204, 113));
            btnEditSave.setForeground(Color.WHITE);

            if ("KhachHang".equalsIgnoreCase(vaiTro) || "Khach hang".equalsIgnoreCase(vaiTro)) {
                txtField3.setEditable(true); // Họ tên
                txtField4.setEditable(true); // SĐT
                txtField6.setEditable(true); // Số bằng lái
            } else {
                txtField4.setEditable(true); // Nhân viên chỉ được sửa SĐT
                txtField5.setEditable(true); // Nhân viên chỉ được sửa Email
            }
        } else {
            String msg = "";
            if ("KhachHang".equalsIgnoreCase(vaiTro) || "Khach hang".equalsIgnoreCase(vaiTro)) {
                if (currentKH != null) {
                    currentKH.setHoTen(txtField3.getText().trim());
                    currentKH.setSdt(txtField4.getText().trim());
                    currentKH.setSoBangLai(txtField6.getText().trim());
                    msg = khBUS.updateKhachHang(currentKH);
                }
            } else {
                if (currentNV != null) {
                    msg = nvBUS.updateProfile((int) currentNV[0], txtField4.getText().trim(), txtField5.getText().trim());
                }
            }

            if ("SUCCESS".equals(msg)) {
                JOptionPane.showMessageDialog(this, "Cập nhật hồ sơ thành công!");
                isEditing = false;
                btnEditSave.setText("Chỉnh sửa hồ sơ");
                btnEditSave.setBackground(UIManager.getColor("Button.background"));
                btnEditSave.setForeground(Color.BLACK);
                
                txtField3.setEditable(false);
                txtField4.setEditable(false);
                txtField5.setEditable(false);
                txtField6.setEditable(false);
                loadDataAndTargetRole();
            } else {
                JOptionPane.showMessageDialog(this, msg, "Lỗi", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}