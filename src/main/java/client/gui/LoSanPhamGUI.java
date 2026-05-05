/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package client.gui;

import client.socket.SocketClient;
import common.dto.LoSanPhamDTO;
import common.dto.LichSuLoDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import org.apache.poi.ss.usermodel.DateUtil;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author admin
 */
public class LoSanPhamGUI extends javax.swing.JPanel {

    private String ngayLap;
    private SwingWorker<Void, Object[]> currentSearchWorker = null;

    /**
     * Creates new form LoSanPhamGUI
     */
    public LoSanPhamGUI() {
        try {
            initComponents();
            banThongKeKhac(); // Khởi tạo 4 cards dashboard
            focusTxt(txtTimKiem, "Nhập mã lô...");
            focusTxt(txtMaLoSP, "Nhập thông tin tìm kiếm...");

            // tải dữ liệu của các lô hàng đang có vào bảng
            loadDanhSachLoSanPham();
            // kiểm tra trạng thái của các lô hàng
            capNhatSoLo();
            // tblLoSanPham.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            loadLichSuLo();
            tblLoSanPham.getColumnModel().getColumn(0).setPreferredWidth(120);
            tblLoSanPham.getColumnModel().getColumn(1).setPreferredWidth(307);
            tblLoSanPham.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblLoSanPham.getColumnModel().getColumn(3).setPreferredWidth(100);
            tblLoSanPham.getColumnModel().getColumn(4).setPreferredWidth(100);

            tblThemSanPham.getColumnModel().getColumn(0).setPreferredWidth(80);// 0
            tblThemSanPham.getColumnModel().getColumn(1).setPreferredWidth(240);
            tblThemSanPham.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblThemSanPham.getColumnModel().getColumn(3).setPreferredWidth(180);
            tblThemSanPham.getColumnModel().getColumn(4).setPreferredWidth(80);// 4
            tblThemSanPham.getColumnModel().getColumn(5).setPreferredWidth(90);
            tblThemSanPham.getColumnModel().getColumn(6).setPreferredWidth(90);
            tblThemSanPham.getColumnModel().getColumn(7).setPreferredWidth(90);
            tblThemSanPham.getColumnModel().getColumn(8).setPreferredWidth(90);
            tblThemSanPham.getColumnModel().getColumn(9).setPreferredWidth(100);
            tblThemSanPham.getColumnModel().getColumn(10).setPreferredWidth(60);
            tblThemSanPham.getColumnModel().getColumn(11).setPreferredWidth(110);

            tblKetQua.getColumnModel().getColumn(0).setPreferredWidth(70);
            tblKetQua.getColumnModel().getColumn(1).setPreferredWidth(250);
            tblKetQua.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblKetQua.getColumnModel().getColumn(3).setPreferredWidth(230);
            tblKetQua.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblKetQua.getColumnModel().getColumn(5).setPreferredWidth(75);
            tblKetQua.getColumnModel().getColumn(6).setPreferredWidth(75);
            tblKetQua.getColumnModel().getColumn(7).setPreferredWidth(80);

            tblThemSanPham.getTableHeader().setReorderingAllowed(false);
            tblLoSanPham.getTableHeader().setReorderingAllowed(false);
            tblKetQua.getTableHeader().setReorderingAllowed(false);
            tblLichSuHoatDong.getTableHeader().setReorderingAllowed(false);

            tblLichSuHoatDong.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã lô
            tblLichSuHoatDong.getColumnModel().getColumn(1).setPreferredWidth(280); // Tên sản phẩm
            tblLichSuHoatDong.getColumnModel().getColumn(2).setPreferredWidth(140); // Thời gian
            tblLichSuHoatDong.getColumnModel().getColumn(3).setPreferredWidth(130); // Loại thao tác
            tblLichSuHoatDong.getColumnModel().getColumn(4).setPreferredWidth(100); // Số lượng sau
            tblLichSuHoatDong.getColumnModel().getColumn(5).setPreferredWidth(150); // Ghi chú
            tblLichSuHoatDong.getColumnModel().getColumn(6).setPreferredWidth(120); // Người thực hiện

            // truyền dữ liệu để hiển thị
            tblLoSanPham.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                if (e.getValueIsAdjusting()) {
                    int x1 = tblLoSanPham.getSelectedRow();
                    if (x1 >= 0) {
                        String maSP = tblLoSanPham.getValueAt(x1, 0).toString();
                        String ten = tblLoSanPham.getValueAt(x1, 1).toString();
                        String maLO = tblLoSanPham.getValueAt(x1, 2).toString();
                        String donVi = tblLoSanPham.getValueAt(x1, 3).toString();
                        String sl = tblLoSanPham.getValueAt(x1, 4).toString();

                        txtMaSanPham.setText(maSP);
                        txtTenSanPham.setText(ten);
                        txtMaLo.setText(maLO);
                        txtDonViTinh.setText(donVi);
                        txtSoLuong.setText(sl);

                        try {
                            Response res = SocketClient.getInstance().sendRequest(
                                    new Request(CommandType.GET_LO_SAN_PHAM_BY_MA, maLO));
                            if (res.isSuccess() && res.getData() instanceof LoSanPhamDTO lo) {
                                txtNhaCungCap.setText(safeString(lo.getTenNhaCungCap()));
                                txtNgaySanXuat.setText(formatLocalDate(lo.getNgaySanXuat()));
                                txtNgayHetHan.setText(formatLocalDate(lo.getNgayHetHan()));
                                txtGiaNhap.setText(lo.getGiaNhap() > 0 ? String.valueOf(lo.getGiaNhap()) : "");
                                if (!safeString(lo.getTenSP()).isEmpty()) {
                                    txtTenSanPham.setText(lo.getTenSP());
                                }
                                if (!safeString(lo.getTenDonVi()).isEmpty()) {
                                    txtDonViTinh.setText(lo.getTenDonVi());
                                }
                            } else {
                                txtNhaCungCap.setText("");
                                txtNgaySanXuat.setText("");
                                txtNgayHetHan.setText("");
                                txtGiaNhap.setText("");
                            }
                        } catch (Exception ex) {
                            txtNhaCungCap.setText("");
                            txtNgaySanXuat.setText("");
                            txtNgayHetHan.setText("");
                            txtGiaNhap.setText("");
                        }
                    }
                }
            });

            SwingUtilities.invokeLater(() -> {
                tblTab.requestFocusInWindow();
                cmbTimKiemTheo.requestFocusInWindow();
                cmbTimKiemTheo.setSelectedIndex(0);
            });

            tblTab.addChangeListener((ChangeEvent e) -> {
                int index = tblTab.getSelectedIndex();
                switch (index) {
                    case 0 ->
                        CanhBao.requestFocusInWindow();
                    case 1 ->
                        QuanLyLo.requestFocusInWindow();
                    default -> {
                    }
                }
            });
            tblTab.addChangeListener(e -> {
                int index = tblTab.getSelectedIndex();
                if (index == 0) {
                    reLoadTheoDoiVaCanhBao();
                } else if (index == 1) {
                    DefaultTableModel tbl = (DefaultTableModel) tblLoSanPham.getModel();
                    tbl.setRowCount(0);
                    loadDanhSachLoSanPham();
                }
            });

            mapKeyToFocus("F3", txtTimKiem, QuanLyLo);
            mapKeyToFocus("F3", txtMaLoSP, CanhBao);
            mapKeyToClickButton("F4", btnXacNhan, QuanLyLo);
            mapKeyToClickButton("F4", btnTimTheoThongTin, CanhBao);
            mapKeyToClickButton("F5", btnHuyLo, QuanLyLo);
            mapKeyToClickButton("F6", btnThemSanPhamTuExcel, QuanLyLo);
            mapKeyToClickButton("F7", btnChonTatCa, QuanLyLo);
            mapKeyToClickButton("F8", btnTimLoHetHan, QuanLyLo);
            mapKeyToClickButton("F9", btnXoaSanPham, QuanLyLo);
            mapKeyToClickButton("F10", btnXoaTrangLo, QuanLyLo);
            mapKeyToClickButton("F10", txtLamMoi, CanhBao);

            btnThemSanPhamTuExcel.setToolTipText("Thêm danh sách lô từ file excel");
            btnChonTatCa.setToolTipText("Chọn hoặc hủy chọn tất cả");
            btnXoaSanPham.setToolTipText("Xóa sản phẩm được chọn");
            btnXacNhan.setToolTipText("Nhập lô được chọn");
            btnTimLoHetHan.setToolTipText("Tìm danh sách lô hết hạn");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        tblTab = new javax.swing.JTabbedPane();
        CanhBao = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtConHan = new javax.swing.JLabel();
        btnXemConHan = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtSapHetHan = new javax.swing.JLabel();
        btnXemSapHetHan = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtHetHan = new javax.swing.JLabel();
        btnXemHetHan = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtDaHuy = new javax.swing.JLabel();
        btnXemDaHuy = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new JScrollPane();
        tblLichSuHoatDong = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        txtLamMoi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtMaLoSP = new JTextField();
        cmbTrangThai = new javax.swing.JComboBox<>();
        btnTimTheoThongTin = new javax.swing.JButton();
        cmbTimKiemTheo = new javax.swing.JComboBox<>();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane2 = new JScrollPane();
        tblKetQua = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        QuanLyLo = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane3 = new JScrollPane();
        tblThemSanPham = new javax.swing.JTable();
        btnThemSanPhamTuExcel = new javax.swing.JButton();
        btnXacNhan = new javax.swing.JButton();
        btnXoaSanPham = new javax.swing.JButton();
        btnChonTatCa = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMaSanPham = new JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTenSanPham = new JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtNhaCungCap = new JTextField();
        txtNgaySanXuat = new JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNgayHetHan = new JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtGiaNhap = new JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtMaLo = new JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtDonViTinh = new JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtSoLuong = new JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        txtTimKiem = new JTextField();
        btnXoaTrangLo = new javax.swing.JButton();
        btnTimLoHetHan = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane1 = new JScrollPane();
        tblLoSanPham = new javax.swing.JTable();
        btnHuyLo = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        CanhBao.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Thống kê tổng quan"));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(1, 4, 10, 15));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Còn hạn:");

        txtConHan.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtConHan)
                                .addContainerGap(146, Short.MAX_VALUE)));
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtConHan))
                                .addContainerGap(10, Short.MAX_VALUE)));

        jPanel4.add(jPanel6);

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Sắp hết hạn:");

        txtSapHetHan.setText("0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSapHetHan)
                                .addContainerGap(124, Short.MAX_VALUE)));
        jPanel15Layout.setVerticalGroup(
                jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel15Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel10)
                                        .addComponent(txtSapHetHan))
                                .addContainerGap(10, Short.MAX_VALUE)));

        jPanel4.add(jPanel15);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("Hết hạn:");

        txtHetHan.setText("0");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtHetHan)
                                .addContainerGap(149, Short.MAX_VALUE)));
        jPanel16Layout.setVerticalGroup(
                jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel16Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(txtHetHan))
                                .addContainerGap(10, Short.MAX_VALUE)));

        jPanel4.add(jPanel16);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Đã hủy:");

        txtDaHuy.setText("0");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDaHuy)
                                .addContainerGap(154, Short.MAX_VALUE)));
        jPanel17Layout.setVerticalGroup(
                jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel17Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel13)
                                        .addComponent(txtDaHuy))
                                .addContainerGap(10, Short.MAX_VALUE)));

        jPanel4.add(jPanel17);

        jPanel8.add(jPanel4, java.awt.BorderLayout.CENTER);

        CanhBao.add(jPanel8, java.awt.BorderLayout.PAGE_START);

        tblLichSuHoatDong.setModel(new DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã lô", "Tên sản phẩm", "Thời gian", "Loại thao tác", "Số lượng sau", "Ghi chú",
                        "Người thực hiện"
                }));
        jScrollPane4.setViewportView(tblLichSuHoatDong);

        jLabel12.setText("Lịch sử hoạt động");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 920, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 316,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)));

        CanhBao.add(jPanel7, java.awt.BorderLayout.PAGE_END);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Tìm kiếm lô"));
        jPanel14.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 910, Short.MAX_VALUE));
        jPanel18Layout.setVerticalGroup(
                jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE));

        jPanel14.add(jPanel18, java.awt.BorderLayout.PAGE_START);

        jPanel19.setLayout(new java.awt.BorderLayout());

        txtLamMoi.setText("Xóa trắng [F10]");
        txtLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtLamMoiActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Tìm kiếm theo:");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Trạng thái:");

        txtMaLoSP.setText("Nhập thông tin tìm kiếm... [F3]");
        txtMaLoSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtMaLoSPActionPerformed(evt);
            }
        });

        cmbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Tất cả", "Còn hạn", "Sắp hết hạn", "Hết hạn", "Đã hủy" }));
        cmbTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cmbTrangThaiActionPerformed(evt);
            }
        });

        btnTimTheoThongTin.setText("Tìm [F4]");
        btnTimTheoThongTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnTimTheoThongTinActionPerformed(evt);
            }
        });

        cmbTimKiemTheo.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Tất cả", "Mã lô sản phẩm", "Mã sản phẩm", "Tên sản phẩm", "Nhà cung cấp" }));
        cmbTimKiemTheo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cmbTimKiemTheoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbTimKiemTheo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMaLoSP, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTimTheoThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 96,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtLamMoi)
                                .addGap(10, 10, 10)));
        jPanel20Layout.setVerticalGroup(
                jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel20Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel20Layout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel20Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel18)
                                                .addComponent(cmbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                jPanel20Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnTimTheoThongTin,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txtMaLoSP, javax.swing.GroupLayout.Alignment.LEADING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 31,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbTimKiemTheo, javax.swing.GroupLayout.Alignment.LEADING))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel19.add(jPanel20, java.awt.BorderLayout.PAGE_START);

        tblKetQua.setModel(new DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã sản phẩm", "Tên sản phẩm", "Mã lô", "Nhà cung cấp", "Số lượng(viên)", "Ngày sản xuất",
                        "Ngày hết hạn", "Trạng thái"
                }));
        jScrollPane2.setViewportView(tblKetQua);

        jLabel9.setText("Kết quả tìm kiếm");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel21Layout.setVerticalGroup(
                jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel21Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel9)
                                .addGap(4, 4, 4)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 159,
                                        Short.MAX_VALUE)));

        jPanel19.add(jPanel21, java.awt.BorderLayout.CENTER);

        jPanel14.add(jPanel19, java.awt.BorderLayout.CENTER);

        CanhBao.add(jPanel14, java.awt.BorderLayout.CENTER);

        tblTab.addTab("Theo dõi & Cảnh báo", CanhBao);

        QuanLyLo.setLayout(new java.awt.BorderLayout());

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách sản phẩm mới"));

        tblThemSanPham.setModel(new DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã sản phẩm", "Tên sản phẩm", "Mã lô", "Nhà cung cấp", "Đơn vị tính", "Ngày sản xuất",
                        "Ngày hết hạn", "Số lượng đặt", "Số lượng giao", "Giá nhập", "Chọn", "Ghi chú"
                }) {
            Class[] types = new Class[] {
                    Object.class, Object.class, Object.class, Object.class,
                    Object.class, Object.class, Object.class, Object.class,
                    Object.class, Object.class, Boolean.class, Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblThemSanPham);

        btnThemSanPhamTuExcel.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThemSanPhamTuExcel.setIcon(new ImageIcon(getClass().getResource("/images/excel.png"))); // NOI18N
        btnThemSanPhamTuExcel.setText("Excel [F6]");
        btnThemSanPhamTuExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnThemSanPhamTuExcelActionPerformed(evt);
            }
        });

        btnXacNhan.setBackground(new java.awt.Color(0, 203, 0));
        btnXacNhan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXacNhan.setForeground(new java.awt.Color(255, 255, 255));
        btnXacNhan.setText("Thêm lô [F4]");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        btnXoaSanPham.setBackground(new java.awt.Color(255, 51, 51));
        btnXoaSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXoaSanPham.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaSanPham.setText("Xóa [F9]");
        btnXoaSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnXoaSanPhamActionPerformed(evt);
            }
        });

        btnChonTatCa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnChonTatCa.setText("Chọn tất cả [F7]");
        btnChonTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnChonTatCaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 898,
                                                Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout
                                                .createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnThemSanPhamTuExcel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnChonTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 135,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnXoaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 88,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 119,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap()));
        jPanel12Layout.setVerticalGroup(
                jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnXacNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnXoaSanPham, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnThemSanPhamTuExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 33,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnChonTatCa, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 213,
                                        Short.MAX_VALUE)));

        QuanLyLo.add(jPanel12, java.awt.BorderLayout.PAGE_START);

        jPanel13.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Thông tin chi tiết"));

        jLabel3.setText("Mã sản phẩm:");

        jLabel4.setText("Tên sản phầm:");

        txtTenSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtTenSanPhamActionPerformed(evt);
            }
        });

        jLabel5.setText("Nhà cung cấp:");

        txtNhaCungCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtNhaCungCapActionPerformed(evt);
            }
        });

        txtNgaySanXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtNgaySanXuatActionPerformed(evt);
            }
        });

        jLabel6.setText("Ngày sản xuất:");

        jLabel7.setText("Ngày hết hạn");

        txtNgayHetHan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtNgayHetHanActionPerformed(evt);
            }
        });

        jLabel8.setText("Giá nhập:");

        txtGiaNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtGiaNhapActionPerformed(evt);
            }
        });

        jLabel25.setText("Mã lô:");

        txtMaLo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtMaLoActionPerformed(evt);
            }
        });

        jLabel14.setText("Đơn vị tính:");

        txtDonViTinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtDonViTinhActionPerformed(evt);
            }
        });

        jLabel15.setText("Số lượng:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel3))
                                                .addGap(9, 9, 9)
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtTenSanPham,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, 272,
                                                                Short.MAX_VALUE)
                                                        .addComponent(txtMaSanPham)))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel8)
                                                        .addComponent(jLabel7)
                                                        .addComponent(jLabel6))
                                                .addGap(8, 8, 8)
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtGiaNhap,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtNgayHetHan,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtNgaySanXuat,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel25)
                                                        .addComponent(jLabel14)
                                                        .addComponent(jLabel15))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel5Layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtDonViTinh,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtNhaCungCap,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtMaLo,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtSoLuong))))
                                .addContainerGap()));
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel25)
                                        .addComponent(txtMaLo, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel14)
                                        .addComponent(txtDonViTinh, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel15)
                                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNgaySanXuat, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtNgayHetHan, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtGiaNhap, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

        jPanel1.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel1, java.awt.BorderLayout.LINE_END);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách lô"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        txtTimKiem.setText("Nhập mã lô...[F3]");
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnXoaTrangLo.setText("Xóa trắng [F10]");
        btnXoaTrangLo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnXoaTrangLoActionPerformed(evt);
            }
        });

        btnTimLoHetHan.setBackground(new java.awt.Color(25, 118, 210));
        btnTimLoHetHan.setForeground(new java.awt.Color(255, 255, 255));
        btnTimLoHetHan.setText("Tìm lô hết hạn [F8]");
        btnTimLoHetHan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnTimLoHetHanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnXoaTrangLo)
                                .addGap(12, 12, 12)
                                .addComponent(btnTimLoHetHan, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));
        jPanel22Layout.setVerticalGroup(
                jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(txtTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, 37,
                                                        Short.MAX_VALUE)
                                                .addComponent(btnXoaTrangLo, javax.swing.GroupLayout.DEFAULT_SIZE, 37,
                                                        Short.MAX_VALUE))
                                        .addComponent(btnTimLoHetHan, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap()));

        jPanel3.add(jPanel22, java.awt.BorderLayout.PAGE_START);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Danh sách lô trong hệ thống"));

        tblLoSanPham.setModel(new DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Mã sản phẩm", "Tên sản phẩm", "Mã lô", "Đơn vị tính", "Số lượng"
                }));
        jScrollPane1.setViewportView(tblLoSanPham);

        btnHuyLo.setBackground(new java.awt.Color(255, 51, 51));
        btnHuyLo.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnHuyLo.setForeground(new java.awt.Color(255, 255, 255));
        btnHuyLo.setText("Hủy lô [F5]");
        btnHuyLo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnHuyLoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 509,
                                                Short.MAX_VALUE)
                                        .addGroup(jPanel23Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(btnHuyLo)))
                                .addContainerGap()));
        jPanel23Layout.setVerticalGroup(
                jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnHuyLo, javax.swing.GroupLayout.PREFERRED_SIZE, 29,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

        jPanel3.add(jPanel23, java.awt.BorderLayout.CENTER);

        jPanel13.add(jPanel3, java.awt.BorderLayout.CENTER);

        QuanLyLo.add(jPanel13, java.awt.BorderLayout.CENTER);

        tblTab.addTab("Quản lý lô", QuanLyLo);

        jPanel2.add(tblTab);

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHuyLoActionPerformed(ActionEvent evt) {
        int selectedRow = tblLoSanPham.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn lô cần hủy.");
            return;
        }

        String maLo = String.valueOf(tblLoSanPham.getValueAt(selectedRow, 2));
        String maSP = String.valueOf(tblLoSanPham.getValueAt(selectedRow, 0));
        String tenSP = String.valueOf(tblLoSanPham.getValueAt(selectedRow, 1));
        String soLuongText = String.valueOf(tblLoSanPham.getValueAt(selectedRow, 4));

        JTextArea noiDungXoaLo = new JTextArea(5, 30);
        noiDungXoaLo.setLineWrap(true);
        noiDungXoaLo.setWrapStyleWord(true);
        JScrollPane cuon = new JScrollPane(noiDungXoaLo);

        int check = JOptionPane.showConfirmDialog(
                this,
                cuon,
                "Nhập lý do hủy lô " + maLo,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (check != JOptionPane.OK_OPTION) {
            return;
        }

        String lyDo = noiDungXoaLo.getText().trim();
        if (lyDo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập lý do hủy lô.");
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận hủy lô " + maLo + "?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int soLuong = parseIntValue(soLuongText, 0);
            LoSanPhamDTO lo = new LoSanPhamDTO();
            lo.setMaLoSanPham(maLo);
            lo.setMaSP(maSP);
            lo.setSoLuong(soLuong);
            lo.setDaHuy(true);

            Response huyRes = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.HUY_LO_SAN_PHAM, lo));
            if (!huyRes.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Lỗi hủy lô: " + huyRes.getMessage());
                return;
            }

            LichSuLoDTO lichSu = new LichSuLoDTO();
            lichSu.setMaLichSuLo("LSL-" + java.util.UUID.randomUUID());
            lichSu.setMaLoSanPham(maLo);
            lichSu.setMaNV(GiaoDienChinhGUI.getTk() != null ? GiaoDienChinhGUI.getTk().getMaNV() : null);
            lichSu.setThoiGian(java.time.LocalDateTime.now());
            lichSu.setHanhDong("HUY_LO");
            lichSu.setSoLuongSau(0);
            lichSu.setGhiChu(lyDo);

            Response lichSuRes = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.ADD_LICH_SU_LO, lichSu));
            if (!lichSuRes.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Hủy lô thành công nhưng lưu lịch sử thất bại: " + lichSuRes.getMessage());
            }

            JOptionPane.showMessageDialog(this,
                    "Đã hủy lô " + maLo + " - " + tenSP,
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
            loadDanhSachLoSanPham();
            capNhatSoLo();
            loadLichSuLo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi hủy lô: " + ex.getMessage());
        }
    }// GEN-LAST:event_btnHuyLoActionPerformed

    private void txtNhaCungCapActionPerformed(ActionEvent evt) {// GEN-FIRST:event_txtNhaCungCapActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txtNhaCungCapActionPerformed

    private void txtLamMoiActionPerformed(ActionEvent evt) {// GEN-FIRST:event_txtLamMoiActionPerformed
        // Hủy tác vụ tìm kiếm đang chạy nếu có
        if (currentSearchWorker != null && !currentSearchWorker.isDone()) {
            currentSearchWorker.cancel(true);
        }

        DefaultTableModel tbl = (DefaultTableModel) tblKetQua.getModel();
        tbl.setRowCount(0);
        cmbTrangThai.setSelectedIndex(0);
        cmbTimKiemTheo.setSelectedIndex(0);
        txtMaLoSP.setText("");

        // TODO add your handling code here:
    }// GEN-LAST:event_txtLamMoiActionPerformed

    private void btnThemSanPhamTuExcelActionPerformed(ActionEvent evt) {// GEN-FIRST:event_btnThemSanPhamTuExcelActionPerformed
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        if (tbl.getRowCount() > 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng xóa các sản phẩm hiện có rồi thử lại.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        try (FileInputStream input = new FileInputStream(selectedFile); XSSFWorkbook workbook = new XSSFWorkbook(input)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int addedRows = 0;

            for (Row row : sheet) {
                if (row == null || row.getRowNum() == 0) {
                    continue;
                }

                String maSP = readExcelCellAsString(row, 0);
                String tenSP = readExcelCellAsString(row, 1);
                String maLo = readExcelCellAsString(row, 2);
                String nhaCungCap = readExcelCellAsString(row, 3);
                String donViTinh = readExcelCellAsString(row, 4);
                String ngaySanXuat = readExcelCellAsString(row, 5);
                String ngayHetHan = readExcelCellAsString(row, 6);
                String soLuongDat = readExcelCellAsString(row, 7);
                String soLuongGiao = readExcelCellAsString(row, 8);
                String giaNhap = readExcelCellAsString(row, 9);
                String ghiChu = readExcelCellAsString(row, 11);

                if (maSP.isBlank() && maLo.isBlank()) {
                    continue;
                }

                tbl.addRow(new Object[] {
                    maSP,
                    tenSP,
                    maLo,
                    nhaCungCap,
                    donViTinh,
                    ngaySanXuat,
                    ngayHetHan,
                    soLuongDat,
                    soLuongGiao,
                    giaNhap,
                    Boolean.FALSE,
                    ghiChu
                });
                addedRows++;
            }

            if (addedRows == 0) {
                JOptionPane.showMessageDialog(this, "File Excel không có dữ liệu hợp lệ.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi đọc file Excel: " + ex.getMessage());
        }
    }
    private void btnXacNhanActionPerformed(ActionEvent evt) {
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        if (tbl.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm! Vui lòng nhập từ Excel trước.");
            return;
        }

        ArrayList<Integer> dsIndexDaThem = new ArrayList<>();
        int soDongDaChon = 0;
        for (int i = 0; i < tbl.getRowCount(); i++) {
            Boolean chon = (Boolean) tbl.getValueAt(i, 10);
            if (chon != null && chon) {
                soDongDaChon++;
            }
        }

        if (soDongDaChon == 0) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn lô cần thêm, vui lòng chọn lô rồi thử lại sau");
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(
                this,
                "Xác nhận thêm " + soDongDaChon + " lô sản phẩm?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) {
            return;
        }

        int thanhCong = 0;
        int thatBai = 0;
        StringBuilder loi = new StringBuilder();

        for (int i = 0; i < tbl.getRowCount(); i++) {
            Boolean chon = (Boolean) tbl.getValueAt(i, 10);
            if (chon == null || !chon) {
                continue;
            }

            try {
                String maSP = safeString(tbl.getValueAt(i, 0));
                String maLo = safeString(tbl.getValueAt(i, 2));
                String ngaySanXuat = safeString(tbl.getValueAt(i, 5));
                String ngayHetHan = safeString(tbl.getValueAt(i, 6));
                int soLuongDat = parseIntValue(safeString(tbl.getValueAt(i, 7)), 0);
                int soLuongGiao = parseIntValue(safeString(tbl.getValueAt(i, 8)), 0);
                int soLuong = soLuongGiao > 0 ? soLuongGiao : soLuongDat;
                String ghiChu = safeString(tbl.getValueAt(i, 11));

                LoSanPhamDTO lo = new LoSanPhamDTO();
                lo.setMaLoSanPham(maLo);
                lo.setMaSP(maSP);
                lo.setSoLuong(soLuong);
                lo.setNgaySanXuat(parseLocalDate(ngaySanXuat));
                lo.setNgayHetHan(parseLocalDate(ngayHetHan));
                lo.setDaHuy(false);

                Response addRes = SocketClient.getInstance().sendRequest(
                        new Request(CommandType.ADD_LO_SAN_PHAM, lo));
                if (!addRes.isSuccess()) {
                    thatBai++;
                    loi.append("- ").append(maLo).append(": ").append(addRes.getMessage()).append('\n');
                    continue;
                }

                LichSuLoDTO lichSu = new LichSuLoDTO();
                lichSu.setMaLichSuLo("LSL-" + java.util.UUID.randomUUID());
                lichSu.setMaLoSanPham(maLo);
                lichSu.setMaNV(GiaoDienChinhGUI.getTk() != null ? GiaoDienChinhGUI.getTk().getMaNV() : null);
                lichSu.setThoiGian(java.time.LocalDateTime.now());
                lichSu.setHanhDong("NHAP_LO");
                lichSu.setSoLuongSau(soLuong);
                lichSu.setGhiChu(ghiChu.isBlank() ? "Nhập lô từ Excel" : ghiChu);

                Response lichSuRes = SocketClient.getInstance().sendRequest(
                        new Request(CommandType.ADD_LICH_SU_LO, lichSu));
                if (!lichSuRes.isSuccess()) {
                    loi.append("- ").append(maLo).append(": lưu lịch sử thất bại: ").append(lichSuRes.getMessage()).append('\n');
                }

                thanhCong++;
                dsIndexDaThem.add(i);
            } catch (Exception ex) {
                thatBai++;
                loi.append("- ").append(safeString(tbl.getValueAt(i, 2))).append(": ").append(ex.getMessage()).append('\n');
            }
        }

        if (!dsIndexDaThem.isEmpty()) {
            xoaLoVuaThem(dsIndexDaThem);
        }

        loadDanhSachLoSanPham();
        capNhatSoLo();
        loadLichSuLo();

        StringBuilder thongBao = new StringBuilder();
        thongBao.append("Đã thêm thành công ").append(thanhCong).append(" lô.");
        if (thatBai > 0) {
            thongBao.append("\nCó ").append(thatBai).append(" lô thêm thất bại.");
        }
        if (loi.length() > 0) {
            thongBao.append("\n\nChi tiết lỗi:\n").append(loi);
        }
        JOptionPane.showMessageDialog(this, thongBao.toString());
    }

    private void txtTimKiemActionPerformed(ActionEvent evt) {
        String maLo = txtTimKiem.getText().trim();
        if (maLo.isEmpty() || maLo.equals("Nhập mã lô...[F3]")) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã lô!");
            return;
        }

        try {
            Response res = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.GET_LO_SAN_PHAM_BY_MA, maLo));
            if (!res.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm lô: " + res.getMessage());
                return;
            }

            LoSanPhamDTO lo = (LoSanPhamDTO) res.getData();
            if (lo == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy lô sản phẩm có mã: " + maLo);
                return;
            }

            txtMaSanPham.setText(safeString(lo.getMaSP()));
            txtTenSanPham.setText(safeString(lo.getTenSP()));
            txtMaLo.setText(safeString(lo.getMaLoSanPham()));
            txtNhaCungCap.setText(safeString(lo.getTenNhaCungCap()));
            txtNgaySanXuat.setText(formatLocalDate(lo.getNgaySanXuat()));
            txtNgayHetHan.setText(formatLocalDate(lo.getNgayHetHan()));
            txtDonViTinh.setText(safeString(lo.getTenDonVi()));
            txtSoLuong.setText(String.valueOf(lo.getSoLuong()));
            txtGiaNhap.setText(lo.getGiaNhap() > 0 ? String.valueOf(lo.getGiaNhap()) : "");

            for (int i = 0; i < tblLoSanPham.getRowCount(); i++) {
                if (maLo.equalsIgnoreCase(safeString(tblLoSanPham.getValueAt(i, 2)))) {
                    tblLoSanPham.setRowSelectionInterval(i, i);
                    tblLoSanPham.scrollRectToVisible(tblLoSanPham.getCellRect(i, 0, true));
                    break;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm lô: " + ex.getMessage());
        }
    }

    private void btnXoaSanPhamActionPerformed(ActionEvent evt) {
        int a = 0;
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        if (tbl.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng thêm sản phầm rồi thử lại");
            return;
        }
        for (int i = 0; i < tbl.getRowCount(); i++) {
            Boolean sel = (Boolean) tbl.getValueAt(i, 10);
            if (sel != null && sel) {
                a++;
            }
        }
        if (a == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.");
            return;
        }
        int x = JOptionPane.showConfirmDialog(this, "Vui lòng xác nhận xóa " + a + " sản phẩm đã chọn?", "Xác nhận?",
                JOptionPane.YES_NO_OPTION);
        if (x == JOptionPane.YES_OPTION) {
            xoaSanPhamDaChon();
        }
    }

    private void btnXoaTrangLoActionPerformed(ActionEvent evt) {
        txtTimKiem.setText("");
        txtMaSanPham.setText("");
        txtTenSanPham.setText("");
        txtMaLo.setText("");
        txtNhaCungCap.setText("");
        txtNgaySanXuat.setText("");
        txtNgayHetHan.setText("");
        txtDonViTinh.setText("");
        txtSoLuong.setText("");
        txtGiaNhap.setText("");
        txtTimKiem.requestFocus();
        DefaultTableModel tbl = (DefaultTableModel) tblLoSanPham.getModel();
        tbl.setRowCount(0);
        loadDanhSachLoSanPham();
    }

    private void txtNgaySanXuatActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txtNgayHetHanActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txtDonViTinhActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void txtGiaNhapActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnChonTatCaActionPerformed(ActionEvent evt) {
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        if (tbl.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm để chọn, Vui lòng thêm sản phẩm rồi thử lại");
            return;
        }
        chonTatCa();
    }

    private void txtMaLoActionPerformed(ActionEvent evt) {
    }

    private void txtTenSanPhamActionPerformed(ActionEvent evt) {
    }

    private void txtMaLoSPActionPerformed(ActionEvent evt) {
    }

    private void btnTimTheoThongTinActionPerformed(ActionEvent evt) {
        if (currentSearchWorker != null && !currentSearchWorker.isDone()) {
            currentSearchWorker.cancel(true);
        }

        String noiDung = txtMaLoSP.getText().trim();
        String trangThai = String.valueOf(cmbTrangThai.getSelectedItem());
        String tieuChi = String.valueOf(cmbTimKiemTheo.getSelectedItem());

        if (!"Tất cả".equals(tieuChi) && (noiDung.isEmpty() || noiDung.equals("Nhập thông tin tìm kiếm..."))) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập thông tin tìm kiếm.");
            return;
        }

        DefaultTableModel tbl = (DefaultTableModel) tblKetQua.getModel();
        tbl.setRowCount(0);

        currentSearchWorker = new SwingWorker<Void, Object[]>() {
            @Override
            protected Void doInBackground() throws Exception {
                Response res = SocketClient.getInstance().sendRequest(
                        new Request(CommandType.GET_ALL_LO_SAN_PHAM, null));
                if (!res.isSuccess()) {
                    throw new RuntimeException(res.getMessage());
                }

                @SuppressWarnings("unchecked")
                List<LoSanPhamDTO> ds = (List<LoSanPhamDTO>) res.getData();
                if (ds == null) {
                    return null;
                }

                String noiDungLower = noiDung.toLowerCase();
                for (LoSanPhamDTO lo : ds) {
                    if (isCancelled()) {
                        break;
                    }
                    if (!matchesLotSearch(lo, tieuChi, noiDungLower, trangThai)) {
                        continue;
                    }
                    publish(dtoToSearchRow(lo));
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                DefaultTableModel model = (DefaultTableModel) tblKetQua.getModel();
                for (Object[] row : chunks) {
                    model.addRow(row);
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    if (tblKetQua.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(LoSanPhamGUI.this, "Không tìm thấy lô sản phẩm nào khớp với tiêu chí.");
                    }
                } catch (Exception ex) {
                    if (!(ex instanceof java.util.concurrent.CancellationException)) {
                        JOptionPane.showMessageDialog(LoSanPhamGUI.this, "Lỗi tìm kiếm: " + ex.getMessage());
                    }
                }
            }
        };
        currentSearchWorker.execute();
    }

    private void cmbTrangThaiActionPerformed(ActionEvent evt) {
    }

    private void cmbTimKiemTheoActionPerformed(ActionEvent evt) {
        if (cmbTimKiemTheo.getSelectedIndex() == 0) {
            txtMaLoSP.setText("");
            txtMaLoSP.setEnabled(false);
        } else {
            txtMaLoSP.setEnabled(true);
        }
        if (cmbTimKiemTheo.getSelectedIndex() != 0) {
            txtMaLoSP.setText("");
            txtMaLoSP.requestFocus();
        }
    }

    private void btnTimLoHetHanActionPerformed(ActionEvent evt) {
        try {
            Response res = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.GET_ALL_LO_SAN_PHAM, null));
            if (!res.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + res.getMessage());
                return;
            }

            @SuppressWarnings("unchecked")
            List<LoSanPhamDTO> ds = (List<LoSanPhamDTO>) res.getData();
            DefaultTableModel tbl = (DefaultTableModel) tblLoSanPham.getModel();
            tbl.setRowCount(0);

            int dem = 0;
            for (LoSanPhamDTO lo : ds) {
                if (lo.isDaHuy()) {
                    continue;
                }
                if (lo.getNgayHetHan() != null && lo.getNgayHetHan().isBefore(LocalDate.now())) {
                    tbl.addRow(dtoToTableRow(lo));
                    dem++;
                }
            }

            if (dem == 0) {
                JOptionPane.showMessageDialog(this, "Không có lô hết hạn.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm lô hết hạn: " + ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CanhBao;
    private javax.swing.JPanel QuanLyLo;
    private javax.swing.JButton btnChonTatCa;
    private javax.swing.JButton btnHuyLo;
    private javax.swing.JButton btnThemSanPhamTuExcel;
    private javax.swing.JButton btnTimLoHetHan;
    private javax.swing.JButton btnTimTheoThongTin;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoaSanPham;
    private javax.swing.JButton btnXoaTrangLo;
    private javax.swing.JComboBox<String> cmbTimKiemTheo;
    private javax.swing.JComboBox<String> cmbTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JScrollPane jScrollPane4;
    private javax.swing.JTable tblKetQua;
    private javax.swing.JTable tblLichSuHoatDong;
    private javax.swing.JTable tblLoSanPham;
    private javax.swing.JTabbedPane tblTab;
    private javax.swing.JTable tblThemSanPham;
    private javax.swing.JButton btnXemConHan;
    private javax.swing.JButton btnXemSapHetHan;
    private javax.swing.JButton btnXemHetHan;
    private javax.swing.JButton btnXemDaHuy;
    private javax.swing.JLabel txtConHan;
    private javax.swing.JLabel txtDaHuy;
    private JTextField txtDonViTinh;
    private JTextField txtGiaNhap;
    private javax.swing.JLabel txtHetHan;
    private javax.swing.JButton txtLamMoi;
    private JTextField txtMaLo;
    private JTextField txtMaLoSP;
    private JTextField txtMaSanPham;
    private JTextField txtNgayHetHan;
    private JTextField txtNgaySanXuat;
    private JTextField txtNhaCungCap;
    private javax.swing.JLabel txtSapHetHan;
    private JTextField txtSoLuong;
    private JTextField txtTenSanPham;
    private JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables

    @SuppressWarnings("unchecked")
    private void loadDanhSachLoSanPham() {
        try {
            Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_ALL_LO_SAN_PHAM_KHONG_HUY, null)
            );
            
            if (!res.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + res.getMessage());
                return;
            }
            
            @SuppressWarnings("unchecked")
            List<LoSanPhamDTO> dsLo = (List<LoSanPhamDTO>) res.getData();
            if (dsLo == null || dsLo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tồn tại lô sản phẩm");
                return;
            }
            
            SwingWorker<Void, Object[]> worker = new SwingWorker<Void, Object[]>() {
                @Override
                protected Void doInBackground() throws Exception {
                    for (LoSanPhamDTO lo : dsLo) {
                        publish(dtoToTableRow(lo));
                    }
                    return null;
                }

                @Override
                protected void process(List<Object[]> chunks) {
                    DefaultTableModel tblMoi = (DefaultTableModel) tblLoSanPham.getModel();
                    for (Object[] i : chunks) {
                        tblMoi.addRow(i);
                    }
                }

                @Override
                protected void done() {
                    tblLoSanPham.revalidate();
                    tblLoSanPham.repaint();
                }
            };
            worker.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadLaiDanhSachLo() {
        try {
            Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_ALL_LO_SAN_PHAM_KHONG_HUY, null)
            );
            
            if (!res.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + res.getMessage());
                return;
            }
            
            @SuppressWarnings("unchecked")
            List<LoSanPhamDTO> dsLo = (List<LoSanPhamDTO>) res.getData();
            DefaultTableModel tbl = (DefaultTableModel) tblLoSanPham.getModel();
            for (LoSanPhamDTO lo : dsLo) {
                tbl.addRow(dtoToTableRow(lo));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải lại danh sách: " + e.getMessage());
        }
    }

    public void capNhatSoLo() {
        try {
            Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.DEM_LO_THEO_TRANG_THAI, null)
            );
            
            if (!res.isSuccess()) {
                return;
            }
            
            int[] stats = (int[]) res.getData();
            // stats: [0]=Còn hạn, [1]=Sắp hết hạn, [2]=Hết hạn, [3]=Đã hủy
            txtConHan.setText(stats[0] + " lô");
            txtSapHetHan.setText(stats[1] + " lô");
            txtHetHan.setText(stats[2] + " lô");
            txtDaHuy.setText(stats[3] + " lô");
        } catch (Exception e) {
            Logger.getLogger(LoSanPhamGUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadLichSuLo() {
        DefaultTableModel tbl = (DefaultTableModel) tblLichSuHoatDong.getModel();
        tbl.setRowCount(0);

        SwingWorker<Void, Object[]> worker = new SwingWorker<Void, Object[]>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Response res = SocketClient.getInstance().sendRequest(
                        new Request(CommandType.GET_ALL_LICH_SU_LO, null)
                    );
                    
                    if (!res.isSuccess()) {
                        return null;
                    }
                    
                    @SuppressWarnings("unchecked")
                    List<LichSuLoDTO> ds = (List<LichSuLoDTO>) res.getData();
                    if (ds == null) {
                        return null;
                    }

                    for (LichSuLoDTO i : ds) {
                        if (isCancelled()) {
                            break;
                        }

                        Object[] row = new Object[] {
                                i.getMaLoSanPham(),
                                "", // Tên sản phẩm sẽ để trống vì server chỉ trả DTO
                                i.getThoiGian(),
                                i.getHanhDong(),
                                i.getSoLuongSau(),
                                i.getGhiChu(),
                                i.getMaNV()
                        };
                        publish(row);
                    }
                } catch (Exception e) {
                    Logger.getLogger(LoSanPhamGUI.class.getName()).log(Level.SEVERE, null, e);
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                DefaultTableModel model = (DefaultTableModel) tblLichSuHoatDong.getModel();
                for (Object[] row : chunks) {
                    model.addRow(row);
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (Exception e) {
                    if (!(e instanceof java.util.concurrent.CancellationException)) {
                        JOptionPane.showMessageDialog(LoSanPhamGUI.this,
                                "Lỗi khi tải lịch sử lô: " + e.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };

        worker.execute();
    }

    private Object[] dtoToTableRow(LoSanPhamDTO lo) {
        return new Object[] {
            lo.getMaSP(),
            lo.getTenSP() != null ? lo.getTenSP() : "",
            lo.getMaLoSanPham(),
            lo.getTenDonVi() != null ? lo.getTenDonVi() : "",
            lo.getSoLuong()
        };
    }

    private void mapKeyToFocus(String key, JComponent target, JComponent tabPanel) {
        InputMap im = tabPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = tabPanel.getActionMap();
        String actionKey = "focus_" + key + "_" + target.hashCode();
        im.put(KeyStroke.getKeyStroke(key), actionKey);
        am.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                target.requestFocusInWindow();
                if (target instanceof JTextField jtf) {
                    jtf.selectAll();
                }
            }
        });
    }

    private void mapKeyToClickButton(String key, AbstractButton button, JComponent tabPanel) {
        InputMap im = tabPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = tabPanel.getActionMap();
        String actionKey = "click_" + key + "_" + button.hashCode();
        im.put(KeyStroke.getKeyStroke(key), actionKey);
        am.put(actionKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (button.isEnabled() && button.isShowing()) {
                    button.doClick();
                }
            }
        });
    }

    private void banThongKeKhac() {
        jPanel4.removeAll();
        jPanel4.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

        btnXemConHan.setText("Xem");
        btnXemSapHetHan.setText("Xem");
        btnXemHetHan.setText("Xem");
        btnXemDaHuy.setText("Xem");

        jPanel6 = new javax.swing.JPanel();
        jPanel6.setBackground(new java.awt.Color(232, 245, 233));
        jPanel6.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(129, 199, 132), 2, true),
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        jPanel6.setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel pnlConHanTop = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlConHanTop.setOpaque(false);
        javax.swing.JLabel lblConHanIcon = new javax.swing.JLabel();
        javax.swing.JLabel lblConHanTitle = new javax.swing.JLabel("Còn hạn");
        lblConHanTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblConHanTitle.setForeground(new java.awt.Color(46, 125, 50));
        pnlConHanTop.add(lblConHanIcon, java.awt.BorderLayout.WEST);
        pnlConHanTop.add(lblConHanTitle, java.awt.BorderLayout.CENTER);

        txtConHan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 32));
        txtConHan.setForeground(new java.awt.Color(46, 125, 50));

        btnXemConHan.setBackground(new java.awt.Color(76, 175, 80));
        btnXemConHan.setForeground(java.awt.Color.WHITE);
        btnXemConHan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnXemConHan.setFocusPainted(false);
        btnXemConHan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXemConHan.addActionListener(evt -> btnXemConHan_ActionPerformed(evt));

        jPanel6.add(pnlConHanTop, java.awt.BorderLayout.NORTH);
        jPanel6.add(txtConHan, java.awt.BorderLayout.CENTER);
        jPanel6.add(btnXemConHan, java.awt.BorderLayout.SOUTH);

        // Thống kê lô sắp hết hạn
        jPanel15 = new javax.swing.JPanel();
        jPanel15.setBackground(new java.awt.Color(255, 243, 224));
        jPanel15.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 167, 38), 2, true),
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        jPanel15.setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel pnlSapHetHanTop = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlSapHetHanTop.setOpaque(false);
        javax.swing.JLabel lblSapHetHanIcon = new javax.swing.JLabel();
        javax.swing.JLabel lblSapHetHanTitle = new javax.swing.JLabel("Sắp hết hạn");
        lblSapHetHanTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblSapHetHanTitle.setForeground(new java.awt.Color(230, 126, 34));
        pnlSapHetHanTop.add(lblSapHetHanIcon, java.awt.BorderLayout.WEST);
        pnlSapHetHanTop.add(lblSapHetHanTitle, java.awt.BorderLayout.CENTER);

        txtSapHetHan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 32));
        txtSapHetHan.setForeground(new java.awt.Color(230, 126, 34));

        btnXemSapHetHan.setBackground(new java.awt.Color(255, 152, 0));
        btnXemSapHetHan.setForeground(java.awt.Color.WHITE);
        btnXemSapHetHan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnXemSapHetHan.setFocusPainted(false);
        btnXemSapHetHan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXemSapHetHan.addActionListener(evt -> btnXemSapHetHan_ActionPerformed(evt));

        jPanel15.add(pnlSapHetHanTop, java.awt.BorderLayout.NORTH);
        jPanel15.add(txtSapHetHan, java.awt.BorderLayout.CENTER);
        jPanel15.add(btnXemSapHetHan, java.awt.BorderLayout.SOUTH);

        // Thống kế lô hết hạn
        jPanel16 = new javax.swing.JPanel();
        jPanel16.setBackground(new java.awt.Color(255, 235, 238));
        jPanel16.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(239, 83, 80), 2, true),
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        jPanel16.setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel pnlHetHanTop = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlHetHanTop.setOpaque(false);
        javax.swing.JLabel lblHetHanIcon = new javax.swing.JLabel();
        javax.swing.JLabel lblHetHanTitle = new javax.swing.JLabel("Hết hạn");
        lblHetHanTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblHetHanTitle.setForeground(new java.awt.Color(191, 14, 14));
        pnlHetHanTop.add(lblHetHanIcon, java.awt.BorderLayout.WEST);
        pnlHetHanTop.add(lblHetHanTitle, java.awt.BorderLayout.CENTER);

        txtHetHan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 32));
        txtHetHan.setForeground(new java.awt.Color(198, 40, 40));

        btnXemHetHan.setBackground(new java.awt.Color(244, 67, 54));
        btnXemHetHan.setForeground(java.awt.Color.WHITE);
        btnXemHetHan.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnXemHetHan.setFocusPainted(false);
        btnXemHetHan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXemHetHan.addActionListener(evt -> btnXemHetHan_ActionPerformed(evt));

        jPanel16.add(pnlHetHanTop, java.awt.BorderLayout.NORTH);
        jPanel16.add(txtHetHan, java.awt.BorderLayout.CENTER);
        jPanel16.add(btnXemHetHan, java.awt.BorderLayout.SOUTH);

        // Thông kế lô hủy
        jPanel17 = new javax.swing.JPanel();
        jPanel17.setBackground(new java.awt.Color(250, 250, 250));
        jPanel17.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(158, 158, 158), 2, true),
                javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        jPanel17.setLayout(new java.awt.BorderLayout(10, 10));

        javax.swing.JPanel pnlDaHuyTop = new javax.swing.JPanel(new java.awt.BorderLayout());
        pnlDaHuyTop.setOpaque(false);
        javax.swing.JLabel lblDaHuyIcon = new javax.swing.JLabel();
        javax.swing.JLabel lblDaHuyTitle = new javax.swing.JLabel("Đã hủy");
        lblDaHuyTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        lblDaHuyTitle.setForeground(new java.awt.Color(97, 97, 97));
        pnlDaHuyTop.add(lblDaHuyIcon, java.awt.BorderLayout.WEST);
        pnlDaHuyTop.add(lblDaHuyTitle, java.awt.BorderLayout.CENTER);

        txtDaHuy.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 32));
        txtDaHuy.setForeground(new java.awt.Color(97, 97, 97));

        btnXemDaHuy.setBackground(new java.awt.Color(158, 158, 158));
        btnXemDaHuy.setForeground(java.awt.Color.WHITE);
        btnXemDaHuy.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnXemDaHuy.setFocusPainted(false);
        btnXemDaHuy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXemDaHuy.addActionListener(evt -> btnXemDaHuy_ActionPerformed(evt));

        jPanel17.add(pnlDaHuyTop, java.awt.BorderLayout.NORTH);
        jPanel17.add(txtDaHuy, java.awt.BorderLayout.CENTER);
        jPanel17.add(btnXemDaHuy, java.awt.BorderLayout.SOUTH);

        // Thêm 4 cards vào panel chính
        jPanel4.add(jPanel6);
        jPanel4.add(jPanel15);
        jPanel4.add(jPanel16);
        jPanel4.add(jPanel17);

        jPanel4.revalidate();
        jPanel4.repaint();
    }

    private void btnXemConHan_ActionPerformed(ActionEvent evt) {
        cmbTimKiemTheo.setSelectedIndex(0);
        cmbTrangThai.setSelectedItem("Còn hạn");
        btnTimTheoThongTin.doClick();
    }

    private void btnXemSapHetHan_ActionPerformed(ActionEvent evt) {
        cmbTimKiemTheo.setSelectedIndex(0);
        cmbTrangThai.setSelectedItem("Sắp hết hạn");
        btnTimTheoThongTin.doClick();
    }

    private void btnXemHetHan_ActionPerformed(ActionEvent evt) {
        cmbTimKiemTheo.setSelectedIndex(0);
        cmbTrangThai.setSelectedItem("Hết hạn");
        btnTimTheoThongTin.doClick();
    }

    private void btnXemDaHuy_ActionPerformed(ActionEvent evt) {
        cmbTimKiemTheo.setSelectedIndex(0);
        cmbTrangThai.setSelectedItem("Đã hủy");
        btnTimTheoThongTin.doClick();
    }

    /**
     * Tải lại dữ liệu thống kê và lịch sử khi chuyển sang tab "Theo dõi & Cảnh báo"
     * Không throw SQLException - xử lý toàn bộ exception bên trong
     */
    private void reLoadTheoDoiVaCanhBao() {
        try {
            capNhatSoLo();      // Cập nhật số liệu thống kê
            loadLichSuLo();     // Tải lại lịch sử hoạt động
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải lại dữ liệu: " + e.getMessage());
            Logger.getLogger(LoSanPhamGUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Chọn hoặc bỏ chọn tất cả các sản phẩm trong bảng tblThemSanPham
     */
    private void chonTatCa() {
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        boolean allSelected = false;
        for (int i = 0; i < tbl.getRowCount(); i++) {
            Boolean sel = (Boolean) tbl.getValueAt(i, 10);
            if (sel == null || !sel) {
                allSelected = true;
                break;
            }
        }
        for (int i = 0; i < tbl.getRowCount(); i++) {
            tbl.setValueAt(allSelected, i, 10);
        }
        tblThemSanPham.revalidate();
        tblThemSanPham.repaint();
    }

    /**
     * Xóa các sản phẩm được chọn khỏi bảng tblThemSanPham
     */
    private void xoaSanPhamDaChon() {
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        for (int i = tbl.getRowCount() - 1; i >= 0; i--) {
            Boolean sel = (Boolean) tbl.getValueAt(i, 10);
            if (sel != null && sel) {
                tbl.removeRow(i);
            }
        }
        tblThemSanPham.revalidate();
        tblThemSanPham.repaint();
    }

    /**
     * Xóa các dòng vừa được thêm thành công từ bảng tblThemSanPham
     */
    private void xoaLoVuaThem(ArrayList<Integer> dsIndex) {
        DefaultTableModel tbl = (DefaultTableModel) tblThemSanPham.getModel();
        for (int i = dsIndex.size() - 1; i >= 0; i--) {
            int index = dsIndex.get(i);
            if (index < tbl.getRowCount()) {
                tbl.removeRow(index);
            }
        }
        tblThemSanPham.revalidate();
        tblThemSanPham.repaint();
    }

    /**
     * Xử lý sự kiện focus cho các text field với placeholder text
     */
    private void focusTxt(JTextField txt, String placeHolder) {
        txt.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt.getText().equals(placeHolder)) {
                    txt.setText("");
                    txt.setForeground(java.awt.Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txt.getText().isEmpty()) {
                    txt.setText(placeHolder);
                    txt.setForeground(new java.awt.Color(150, 150, 150));
                }
            }
        });
        txt.setText(placeHolder);
        txt.setForeground(new java.awt.Color(150, 150, 150));
    }

    private String safeString(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private int parseIntValue(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.replace(",", "").trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private double parseDoubleValue(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.replace(",", "").trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    private LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value.trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception ex) {
            try {
                return LocalDate.parse(value.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (Exception ignored) {
                return null;
            }
        }
    }

    private String formatLocalDate(LocalDate date) {
        return date == null ? "" : date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private Object[] dtoToSearchRow(LoSanPhamDTO lo) {
        return new Object[] {
            lo.getMaSP(),
            safeString(lo.getTenSP()),
            lo.getMaLoSanPham(),
            safeString(lo.getTenNhaCungCap()),
            lo.getSoLuong(),
            formatLocalDate(lo.getNgaySanXuat()),
            formatLocalDate(lo.getNgayHetHan()),
            tinhTrangLo(lo)
        };
    }

    private String tinhTrangLo(LoSanPhamDTO lo) {
        if (lo == null) {
            return "";
        }
        if (lo.isDaHuy()) {
            return "Đã hủy";
        }
        LocalDate ngayHetHan = lo.getNgayHetHan();
        if (ngayHetHan == null) {
            return "";
        }
        if (ngayHetHan.isBefore(LocalDate.now())) {
            return "Hết hạn";
        }
        if (!ngayHetHan.isAfter(LocalDate.now().plusDays(30))) {
            return "Sắp hết hạn";
        }
        return "Còn hạn";
    }

    private boolean matchesLotSearch(LoSanPhamDTO lo, String tieuChi, String noiDungLower, String trangThai) {
        String trangThaiLo = tinhTrangLo(lo);
        if (!"Tất cả".equalsIgnoreCase(trangThai) && !trangThaiLo.equalsIgnoreCase(trangThai)) {
            return false;
        }

        if ("Tất cả".equalsIgnoreCase(tieuChi)) {
            return true;
        }

        String maLo = safeString(lo.getMaLoSanPham()).toLowerCase();
        String maSP = safeString(lo.getMaSP()).toLowerCase();
        String tenSP = safeString(lo.getTenSP()).toLowerCase();
        String tenNCC = safeString(lo.getTenNhaCungCap()).toLowerCase();

        return switch (tieuChi) {
            case "Mã lô sản phẩm" -> maLo.contains(noiDungLower);
            case "Mã sản phẩm" -> maSP.contains(noiDungLower);
            case "Tên sản phẩm" -> tenSP.contains(noiDungLower);
            case "Nhà cung cấp" -> tenNCC.contains(noiDungLower);
            default -> true;
        };
    }

    private String readExcelCellAsString(Row row, int columnIndex) {
        if (row == null) {
            return "";
        }
        Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> DateUtil.isCellDateFormatted(cell)
                    ? cell.getLocalDateTimeCellValue().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : (Math.floor(cell.getNumericCellValue()) == cell.getNumericCellValue()
                        ? String.valueOf((long) cell.getNumericCellValue())
                        : String.valueOf(cell.getNumericCellValue()));
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
