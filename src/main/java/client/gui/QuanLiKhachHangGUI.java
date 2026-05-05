package client.gui;

import client.bus.KhachHangBUS;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import common.dto.KhachHangDTO;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class QuanLiKhachHangGUI extends JPanel {

    private JButton btnThem, btnXoa, btnSua, btnXuatExcel, btnLamMoi;
    private JTextField txtTimKiem;
    private JTable table;
    private JComboBox<String> cmbTieuChiTimKiem;
    private DefaultTableModel model;
    private JLabel lblTongSoDong;
    private JLabel lblSoDongChon;
    private KhachHangBUS khachHangBUS;

    public QuanLiKhachHangGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.khachHangBUS = new KhachHangBUS();

        // PANEL NORTH
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BorderLayout());

        // Panel Chức năng (Thêm, xóa, sửa)
        JPanel pnlNorthLeft = new JPanel();
        pnlNorthLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlNorthLeft.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnThem = new JButton("Thêm - F6");
        btnXoa = new JButton("Xóa - Del");
        btnSua = new JButton("Sửa - F2");
        btnXuatExcel = new JButton("Xuất Excel");
        btnLamMoi = new JButton("Làm mới - F5");

        mapKeyToClickButton("F6", btnThem);
        mapKeyToClickButton("DELETE", btnXoa);
        mapKeyToClickButton("F2", btnSua);
        mapKeyToClickButton("F5", btnLamMoi);

        setupTopButton(btnThem, new Color(25, 118, 210)); // Xanh dương
        setupTopButton(btnXoa, new Color(255, 51, 51));  // Đỏ
        setupTopButton(btnSua, new Color(0, 203, 0));    // Xanh lá
        setupTopButton(btnXuatExcel, new Color(255, 255, 255)); // Trắng
        setupTopButton(btnLamMoi, new Color(255, 255, 255)); // Trắng

        pnlNorthLeft.add(btnThem);
        pnlNorthLeft.add(btnXoa);
        pnlNorthLeft.add(btnSua);
        pnlNorthLeft.add(btnXuatExcel);
        pnlNorthLeft.add(btnLamMoi);

        pnlNorth.add(pnlNorthLeft, BorderLayout.WEST);

        // Panel Tìm kiếm
        JPanel pnlNorthRight = new JPanel();
        pnlNorthRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        cmbTieuChiTimKiem = new JComboBox<>(new String[]{"Mã khách hàng", "Tên khách hàng", "Số điện thoại"});
        cmbTieuChiTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbTieuChiTimKiem.setPreferredSize(new Dimension(150, 30));

        txtTimKiem = new JTextField(20);
        txtTimKiem.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tìm kiếm...");
        txtTimKiem.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("resources/images/search.svg", 16, 16));
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(250, 35));

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlTimKiem.add(txtTimKiem);

        pnlNorthRight.add(new JLabel("Tìm theo"));
        pnlNorthRight.add(cmbTieuChiTimKiem);
        pnlNorthRight.add(pnlTimKiem);

        pnlNorth.add(pnlNorthRight, BorderLayout.EAST);

        this.add(pnlNorth, BorderLayout.NORTH);

        // PANEL CENTER (Bảng dữ liệu)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));

        String[] columnNames = {"STT", "Mã KH", "Họ tên đệm", "Tên", "Số điện thoại", "Điểm tích lũy"};
        Object[][] data = {};

        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));

        // Thiết lập Header
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setReorderingAllowed(false);

        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        // CHỈNH KÍCH THƯỚC CỘT
        TableColumnModel columnModel = table.getColumnModel();

        // Cột STT (Cột 0)
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);

        // Cột Mã KH (Cột 1)
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(1).setMaxWidth(100);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);

        // Cột Số điện thoại (Cột 4)
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(4).setMaxWidth(100);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);

        // Cột Điểm tích lũy (Cột 5)
        columnModel.getColumn(5).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);

        // PANEL SOUTH (FOOTER)
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        pnlSouth.setBorder(new EmptyBorder(5, 0, 0, 0));

        Font fontFooter = new Font("Arial", Font.BOLD, 13);

        lblTongSoDong = new JLabel("Tổng số khách hàng: 0");
        lblTongSoDong.setFont(fontFooter);
        lblTongSoDong.setForeground(new Color(0, 102, 204)); // Màu xanh đậm

        lblSoDongChon = new JLabel("Đang chọn: 0");
        lblSoDongChon.setFont(fontFooter);
        lblSoDongChon.setForeground(new Color(204, 0, 0)); // Màu đỏ đậm

        pnlSouth.add(lblTongSoDong);
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL)); // Đường ngăn cách
        pnlSouth.add(lblSoDongChon);

        this.add(pnlSouth, BorderLayout.SOUTH);

        // Đăng kí sự kiện
        updateTable();
        addEvents();
    }

    private void setupTopButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setMargin(new Insets(5, 10, 5, 10));
        button.setPreferredSize(new Dimension(100, 30));
    }

    private void updateTable(List<KhachHangDTO> dsKH) {
        model.setRowCount(0);
        if (dsKH == null) {
            lblTongSoDong.setText("Tổng số khách hàng: 0");
            return;
        }

        int stt = 1;
        for (KhachHangDTO kh : dsKH) {
            Object[] row = {
                stt++,
                kh.getMaKH(),
                kh.getHoTenDem(),
                kh.getTen(),
                kh.getSdt(),
                kh.getDiemTichLuy()
            };
            model.addRow(row);
        }

        lblTongSoDong.setText("Tổng số khách hàng: " + dsKH.size());
    }

    private void updateTable() {
        List<KhachHangDTO> dsKH = khachHangBUS.getAllKhachHang();
        updateTable(dsKH);
    }

    // HÀM ĐĂNG KÝ TẤT CẢ SỰ KIỆN
    private void addEvents() {
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyThem();
            }
        });

        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyXoa();
            }
        });

        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLySua();
            }
        });

        btnXuatExcel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyXuatExcel();
            }
        });

        btnLamMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
                txtTimKiem.setText("");
            }
        });

        txtTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyTimKiem();
            }
        });

        cmbTieuChiTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtTimKiem.selectAll();
                txtTimKiem.requestFocus();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTietKhachHang(e);
            }
        });

        // SỰ KIỆN CHỌN DÒNG TRONG TABLE
        // Sử dụng ListSelectionListener hỗ trợ cả phím Shift/Ctrl
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // getValueIsAdjusting() trả về true khi người dùng đang kéo chuột, chỉ cập nhật khi hành động kết thúc (false)
                if (!e.getValueIsAdjusting()) {
                    int soDongDaChon = table.getSelectedRowCount();
                    lblSoDongChon.setText("Đang chọn: " + soDongDaChon);
                }
            }
        });
    }

    // HÀM XỬ LÝ TÌM KIẾM
    private void xuLyTimKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        String tieuChi = cmbTieuChiTimKiem.getSelectedItem().toString();

        List<KhachHangDTO> dsKetQua = new ArrayList<>();

        if (tuKhoa.isEmpty()) {
            dsKetQua = khachHangBUS.getAllKhachHang();
        } else {
            switch (tieuChi) {
                case "Mã khách hàng":
                    KhachHangDTO kh = khachHangBUS.getKhachHangByMa(tuKhoa);
                    if (kh != null) dsKetQua.add(kh);
                    break;
                case "Tên khách hàng":
                    dsKetQua = khachHangBUS.getKhachHangByTen(tuKhoa);
                    break;
                case "Số điện thoại":
                    // Bây giờ BUS sẽ trả về List cho tìm kiếm
                    dsKetQua = khachHangBUS.searchKhachHangBySdt(tuKhoa);
                    break;
            }
        }
        updateTable(dsKetQua);
    }

    private void xuLyXuatExcel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }
            try {
                common.utils.ExcelUtil.exportTableToExcel(table, "Danh sách khách hàng", filePath);
                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //HÀM XỬ LÝ NÚT THÊM
    private void xuLyThem() {
        ThemKhachHangGUI pnlThemKH = new ThemKhachHangGUI();
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm khách hàng mới");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setContentPane(pnlThemKH);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        int maKHCUoiCung = khachHangBUS.getMaKHCuoi();
        maKHCUoiCung++;
        String maKHNew = String.format("KH-%05d", maKHCUoiCung);

        pnlThemKH.setTxtMaKhachHang(maKHNew);
        pnlThemKH.setTxtDiemTichLuy(0);
        pnlThemKH.getTxtDiemTichLuy().setEnabled(false);

        boolean isSuccess = false;

        while (!isSuccess) {
            dialog.setVisible(true);

            KhachHangDTO khNew = pnlThemKH.getKhachHangMoi();

            if (khNew == null) {
                break;
            }

            if (khachHangBUS.addKhachHang(khNew)) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                updateTable();
                isSuccess = true;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thêm khách hàng thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //HÀM XỬ LÝ NÚT XÓA
    private void xuLyXoa() {
        int selectedRows[] = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.");
            return;
        }

        String message;
        if (selectedRows.length == 1) {
            String tenKH = model.getValueAt(selectedRows[0], 2).toString() + " " + model.getValueAt(selectedRows[0], 3).toString();
            message = "Bạn có chắc muốn xóa khách hàng '" + tenKH + "' không?";
        } else {
            message = "Bạn có chắc muốn xóa " + selectedRows.length + " khách hàng đã chọn không?";
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                message,
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int soLuongXoaThanhCong = 0;

            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                String maKH = model.getValueAt(row, 1).toString();

                if (khachHangBUS.xoaKhachHang(maKH)) {
                    soLuongXoaThanhCong++;
                }
            }

            if (soLuongXoaThanhCong > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công " + soLuongXoaThanhCong + " khách hàng.");
                updateTable();
                lblSoDongChon.setText("Đang chọn: 0");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại (có thể do khách hàng đã có hóa đơn).", "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
            }
        }
        table.clearSelection();
    }

    //HÀM XỬ LÝ NÚT SỬA
    private void xuLySua() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.");
            return;
        }

        String maKH = model.getValueAt(selectedRow, 1).toString();
        KhachHangDTO khCanSua = khachHangBUS.getKhachHangByMa(maKH);
        if (khCanSua == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng để sửa.");
            return;
        }

        ThemKhachHangGUI pnlThemKH = new ThemKhachHangGUI();
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa thông tin khách hàng");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setContentPane(pnlThemKH);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        pnlThemKH.setTxtMaKhachHang(khCanSua.getMaKH());
        pnlThemKH.setTxtHoTenDem(khCanSua.getHoTenDem());
        pnlThemKH.setTxtTen(khCanSua.getTen());
        pnlThemKH.setTxtSDT(khCanSua.getSdt());
        pnlThemKH.setTxtDiemTichLuy(khCanSua.getDiemTichLuy());
        pnlThemKH.getTxtDiemTichLuy().setEnabled(true);

        boolean isSuccess = false;

        while (!isSuccess) {
            dialog.setVisible(true);

            KhachHangDTO khNew = pnlThemKH.getKhachHangMoi();

            if (khNew == null) {
                break;
            }

            if (khachHangBUS.suaKhachHang(maKH, khNew)) {
                JOptionPane.showMessageDialog(this, "Sửa thông tin khách hàng thành công!");
                updateTable();
                isSuccess = true;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sửa thông tin khách hàng thất bại!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hienThiChiTietKhachHang(MouseEvent e) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            String maKH = model.getValueAt(selectRow, 1).toString();
            KhachHangDTO khDaChon = khachHangBUS.getKhachHangByMa(maKH);

            if (khDaChon != null && e.getClickCount() == 2) { // Double click
                ThemKhachHangGUI pnlThemKH = new ThemKhachHangGUI();
                JDialog dialog = new JDialog();
                dialog.setTitle("Thông tin chi tiết khách hàng");
                dialog.setModal(true);
                dialog.setResizable(false);
                dialog.setContentPane(pnlThemKH);
                dialog.pack();
                dialog.setLocationRelativeTo(null);

                pnlThemKH.getBtnHuy().setVisible(false);
                pnlThemKH.getBtnXacNhan().setText("Đóng");
                pnlThemKH.getBtnXacNhan().addActionListener(l -> dialog.dispose());

                pnlThemKH.getTxtHoTenDem().setEditable(false);
                pnlThemKH.getTxtTen().setEditable(false);
                pnlThemKH.getTxtSDT().setEditable(false);
                pnlThemKH.getTxtDiemTichLuy().setEditable(false);

                pnlThemKH.setTxtMaKhachHang(khDaChon.getMaKH());
                pnlThemKH.setTxtHoTenDem(khDaChon.getHoTenDem());
                pnlThemKH.setTxtTen(khDaChon.getTen());
                pnlThemKH.setTxtSDT(khDaChon.getSdt());
                pnlThemKH.setTxtDiemTichLuy(khDaChon.getDiemTichLuy());

                dialog.setVisible(true);
            }
        }
    }

    private void mapKeyToFocus(String key, JComponent component) {
        InputMap im = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = component.getActionMap();

        im.put(KeyStroke.getKeyStroke(key), "focus_" + key);
        am.put("focus_" + key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                component.requestFocus();
                if (component instanceof JTextField jTextField) {
                    jTextField.selectAll();
                }
            }
        });
    }

    private void mapKeyToClickButton(String key, AbstractButton button) {
        InputMap im = button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = button.getActionMap();

        im.put(KeyStroke.getKeyStroke(key), "click_" + key);
        am.put("click_" + key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button.doClick(); // kích hoạt sự kiện button
            }
        });
    }
}
