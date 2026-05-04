package client.gui;

import common.dto.NhanVienDTO;
import common.dto.TaiKhoanDTO;
import client.socket.SocketClient;
import common.network.Request;
import common.network.Response;
import common.network.CommandType;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class QuanLiNhanVienGUI extends JPanel {

    private JButton btnThem, btnXoa, btnSua;
    private JTextField txtTimKiem;
    private JTable table;
    private JComboBox<String> cmbTieuChiTimKiem;
    private JComboBox<String> cmbBoLoc;
    private DefaultTableModel model;
    private JLabel lblTongSoDong;
    private JLabel lblSoDongChon;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public QuanLiNhanVienGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // PANEL NORTH
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BorderLayout());

        JPanel pnlNorthLeft = new JPanel();
        pnlNorthLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlNorthLeft.setBorder(new EmptyBorder(0, 0, 10, 0));

        btnThem = new JButton("Thêm - F6");
        btnXoa = new JButton("Xóa - Del");
        btnSua = new JButton("Sửa - F2");

        mapKeyToClickButton("F6", btnThem);
        mapKeyToClickButton("DELETE", btnXoa);
        mapKeyToClickButton("F2", btnSua);

        setupTopButton(btnThem, new Color(25, 118, 210)); // Xanh dương
        setupTopButton(btnXoa, new Color(255, 51, 51));  // Đỏ
        setupTopButton(btnSua, new Color(0, 203, 0));    // Xanh lá

        pnlNorthLeft.add(btnThem);
        pnlNorthLeft.add(btnXoa);
        pnlNorthLeft.add(btnSua);

        pnlNorth.add(pnlNorthLeft, BorderLayout.WEST);

        JPanel pnlNorthRight = new JPanel();
        pnlNorthRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        cmbTieuChiTimKiem = new JComboBox<>(new String[]{"Mã nhân viên", "Tên nhân viên", "SĐT", "CCCD"});
        cmbTieuChiTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbTieuChiTimKiem.setPreferredSize(new Dimension(150, 30));

        cmbBoLoc = new JComboBox<>(new String[]{"Tất cả", "Đang làm", "Đã nghỉ"});
        cmbBoLoc.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbBoLoc.setPreferredSize(new Dimension(150, 30));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlTimKiem.add(new JLabel("Tìm kiếm"));
        pnlTimKiem.add(txtTimKiem);

        pnlNorthRight.add(new JLabel("Tìm theo"));
        pnlNorthRight.add(cmbTieuChiTimKiem);
        pnlNorthRight.add(pnlTimKiem);
        pnlNorthRight.add(new JLabel("Lọc theo"));
        pnlNorthRight.add(cmbBoLoc);

        pnlNorth.add(pnlNorthRight, BorderLayout.EAST);

        this.add(pnlNorth, BorderLayout.NORTH);

        // PANEL CENTER (TABLE)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));

        String[] columnNames = {"STT", "Mã NV", "Họ tên đệm", "Tên", "SĐT", "CCCD", "Giới tính", "Ngày sinh", "Địa chỉ", "Vai trò", "Trạng thái"};
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

        // Header
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setReorderingAllowed(false);

        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        TableColumnModel columnModel = table.getColumnModel();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // 0. STT: Căn giữa
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(40);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);

        // 1. Mã NV: Căn giữa
        columnModel.getColumn(1).setPreferredWidth(80);
        columnModel.getColumn(1).setMaxWidth(100);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);

        // 2. Họ tên đệm
        columnModel.getColumn(2).setPreferredWidth(150);

        // 3. Tên
        columnModel.getColumn(3).setPreferredWidth(80);

        // 4. SĐT: Căn giữa
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);

        // 5. CCCD: Căn giữa
        columnModel.getColumn(5).setPreferredWidth(110);
        columnModel.getColumn(5).setCellRenderer(centerRenderer);

        // 6. Giới tính: Căn giữa
        columnModel.getColumn(6).setPreferredWidth(70);
        columnModel.getColumn(6).setMaxWidth(80);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);

        // 7. Ngày sinh: Căn giữa
        columnModel.getColumn(7).setPreferredWidth(90);
        columnModel.getColumn(7).setCellRenderer(centerRenderer);

        // 8. Địa chỉ: Rộng (Tự giãn nở các phần còn lại)
        columnModel.getColumn(8).setPreferredWidth(150);

        // 9. VAI TRÒ
        columnModel.getColumn(9).setPreferredWidth(110);
        columnModel.getColumn(9).setCellRenderer(centerRenderer);

        // 10. Trạng thái
        columnModel.getColumn(10).setPreferredWidth(90);
        columnModel.getColumn(10).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        this.add(centerPanel, BorderLayout.CENTER);

        // PANEL SOUTH (FOOTER)
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        pnlSouth.setBorder(new EmptyBorder(5, 0, 0, 0));

        Font fontFooter = new Font("Arial", Font.BOLD, 13);

        lblTongSoDong = new JLabel("Tổng số nhân viên: 0");
        lblTongSoDong.setFont(fontFooter);
        lblTongSoDong.setForeground(new Color(0, 102, 204));

        lblSoDongChon = new JLabel("Đang chọn: 0");
        lblSoDongChon.setFont(fontFooter);
        lblSoDongChon.setForeground(new Color(204, 0, 0));

        pnlSouth.add(lblTongSoDong);
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(lblSoDongChon);

        this.add(pnlSouth, BorderLayout.SOUTH);

        // Sự kiện
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

    private void updateTable(List<NhanVienDTO> dsNV) {
        model.setRowCount(0);
        if (dsNV == null) {
            lblTongSoDong.setText("Tổng số nhân viên: 0");
            return;
        }
        int stt = 1;
        for (NhanVienDTO nv : dsNV) {
            String ngaySinhStr = "";
            if (nv.getNgaySinh() != null) {
                ngaySinhStr = nv.getNgaySinh().format(formatter);
            }

            TaiKhoanDTO tk = null;
            Response resTk = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_TAI_KHOAN_BY_MA_NV, nv.getMaNV()));
            if (resTk.isSuccess() && resTk.getData() != null) {
                tk = (TaiKhoanDTO) resTk.getData();
            }
            String vaiTro = "Nhân viên";

            if (tk != null) {
                if (tk.isQuanLy()) {
                    vaiTro = "Quản lý (Admin)"; // Ưu tiên hiển thị Admin cao nhất
                } else if (tk.isQuanLyLo()) {
                    vaiTro = "Quản lý lô";    // Nếu không phải Admin mà là Quản lý lô
                }
            }

            Object[] row = {
                stt++,
                nv.getMaNV(),
                nv.getHoTenDem(),
                nv.getTen(),
                nv.getSdt(),
                nv.getCccd(),
                nv.isGioiTinh() ? "Nam" : "Nữ",
                ngaySinhStr,
                nv.getDiaChi(),
                vaiTro,
                nv.isNghiViec() ? "Đã nghỉ" : "Đang làm"
            };
            model.addRow(row);
        }
        lblTongSoDong.setText("Tổng số nhân viên: " + dsNV.size());
    }

    private void updateTable() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_NHAN_VIEN, null));
        if (res.isSuccess()) {
            List<NhanVienDTO> dsNV = (List<NhanVienDTO>) res.getData();
            updateTable(dsNV);
        } else {
            updateTable(new ArrayList<>());
        }
    }

    private void addEvents() {
        btnThem.addActionListener(e -> xuLyThem());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnSua.addActionListener(e -> xuLySua());
        txtTimKiem.addActionListener(e -> xuLyTimKiem());
        cmbBoLoc.addActionListener(e -> xuLyLoc());

        cmbTieuChiTimKiem.addActionListener(e -> {
            txtTimKiem.selectAll();
            txtTimKiem.requestFocus();
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTietNhanVien(e);
            }
        });

        // Sự kiện đếm dòng chọn có hỗ trợ shift/ctrl
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                lblSoDongChon.setText("Đang chọn: " + table.getSelectedRowCount());
            }
        });
    }

    private void xuLyTimKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        String tieuChi = cmbTieuChiTimKiem.getSelectedItem().toString();
        List<NhanVienDTO> dsKetQua = new ArrayList<>();

        if (tuKhoa.isEmpty()) {
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_NHAN_VIEN, null));
            if (res.isSuccess()) {
                dsKetQua = (List<NhanVienDTO>) res.getData();
            }
        } else {
            Response res = null;
            switch (tieuChi) {
                case "Mã nhân viên":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_MA, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) {
                        dsKetQua.add((NhanVienDTO) res.getData());
                    }
                    break;
                case "Tên nhân viên":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_TEN, tuKhoa));
                    if (res.isSuccess()) dsKetQua = (List<NhanVienDTO>) res.getData();
                    break;
                case "SĐT":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_SDT, tuKhoa));
                    if (res.isSuccess()) dsKetQua = (List<NhanVienDTO>) res.getData();
                    break;
                case "CCCD":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_CCCD, tuKhoa));
                    if (res.isSuccess()) dsKetQua = (List<NhanVienDTO>) res.getData();
                    break;
            }
        }
        updateTable(dsKetQua);
    }

    private void xuLyLoc() {
        String boLoc = cmbBoLoc.getSelectedItem().toString();
        if (boLoc.equals("Tất cả")) {
            updateTable();
        } else {
            boolean daNghiViec = boLoc.equals("Đã nghỉ");
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_TRANG_THAI, daNghiViec));
            if (res.isSuccess()) {
                updateTable((List<NhanVienDTO>) res.getData());
            } else {
                updateTable(new ArrayList<>());
            }
        }
    }

    private void xuLyThem() {
        ThemNhanVienGUI pnlThemNV = new ThemNhanVienGUI();
        JDialog dialog = new JDialog();
        dialog.setTitle("Thêm nhân viên mới");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setContentPane(pnlThemNV);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        Response resMa = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_NV_CUOI, null));
        int maNVCUoiCung = 0;
        if (resMa.isSuccess() && resMa.getData() != null) {
            String maNVStr = (String) resMa.getData();
            if (maNVStr != null && maNVStr.startsWith("NV-")) {
                maNVCUoiCung = Integer.parseInt(maNVStr.substring(3));
            }
        }
        String maNVNew = String.format("NV-%04d", maNVCUoiCung + 1);
        pnlThemNV.setTxtMaNhanVien(maNVNew);
        pnlThemNV.setTxtTenDangNhap(maNVNew);
        pnlThemNV.setTxtNgayTao(LocalDateTime.now());

        boolean isSuccess = false;

        while (!isSuccess) {
            dialog.setVisible(true);

            NhanVienDTO nvNew = pnlThemNV.getNhanVienMoi();
            TaiKhoanDTO tkNew = pnlThemNV.getTaiKhoanMoi();

            if (nvNew == null || tkNew == null) {
                break;
            }

            Response resNv = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_NHAN_VIEN, nvNew));
            if (resNv.isSuccess()) {
                Response resTk = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_TAI_KHOAN, tkNew));
                if (resTk.isSuccess()) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    updateTable();
                    isSuccess = true;
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại: " + resTk.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại: " + resNv.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void xuLyXoa() {
        int selectedRows[] = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa.");
            return;
        }

        String message = (selectedRows.length == 1)
                ? "Bạn có chắc muốn xóa nhân viên '" + model.getValueAt(selectedRows[0], 3).toString() + " không?"
                : "Bạn có chắc muốn xóa " + selectedRows.length + " nhân viên đã chọn không?";

        if (JOptionPane.showConfirmDialog(this, message, "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int count = 0;
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                String maNV = model.getValueAt(selectedRows[i], 1).toString();
                Response resTk = SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_TAI_KHOAN, maNV));
                Response resNv = SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_NHAN_VIEN, maNV));
                if (resNv.isSuccess() && resTk.isSuccess()) {
                    count++;
                }
            }
            if (count > 0) {
                JOptionPane.showMessageDialog(this, "Đã xóa thành công " + count + " nhân viên.");
                updateTable();
                lblSoDongChon.setText("Đang chọn: 0");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại (có thể nhân viên đã lập hóa đơn).", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        table.clearSelection();
    }

    private void xuLySua() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa.");
            return;
        }

        String maNV = model.getValueAt(selectedRow, 1).toString();
        
        Response resNv = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_MA, maNV));
        Response resTk = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_TAI_KHOAN_BY_MA_NV, maNV));
        
        if (!resNv.isSuccess() || resNv.getData() == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên.");
            return;
        }
        
        NhanVienDTO nvCanSua = (NhanVienDTO) resNv.getData();
        TaiKhoanDTO tkCanSua = null;
        if (resTk.isSuccess()) tkCanSua = (TaiKhoanDTO) resTk.getData();

        ThemNhanVienGUI pnlThemNV = new ThemNhanVienGUI();
        JDialog dialog = new JDialog();
        dialog.setTitle("Sửa thông tin nhân viên");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.setContentPane(pnlThemNV);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        pnlThemNV.setTxtMaNhanVien(nvCanSua.getMaNV());
        pnlThemNV.setTxtHoTenDem(nvCanSua.getHoTenDem());
        pnlThemNV.setTxtTen(nvCanSua.getTen());
        pnlThemNV.setTxtSDT(nvCanSua.getSdt());
        pnlThemNV.setTxtCCCD(nvCanSua.getCccd());
        pnlThemNV.setCmbGioiTinh(nvCanSua.isGioiTinh());
        pnlThemNV.setTxtNgaySinh(nvCanSua.getNgaySinh());
        pnlThemNV.setTxtDiaChi(nvCanSua.getDiaChi());
        pnlThemNV.setTxtTenDangNhap(maNV);
        if (tkCanSua != null) {
            pnlThemNV.setTxtMatKhau(tkCanSua.getMatKhau());
            pnlThemNV.setChkQuanLy(tkCanSua.isQuanLy());
            pnlThemNV.setChkQuanLyLo(tkCanSua.isQuanLyLo());
            pnlThemNV.setTxtEmail(tkCanSua.getEmail());
            pnlThemNV.setTxtNgayTao(tkCanSua.getNgayTao());
        }

        pnlThemNV.getTxtMatKhau().setEditable(false);

        boolean isSuccess = false;

        while (!isSuccess) {
            dialog.setVisible(true);

            NhanVienDTO nvNew = pnlThemNV.getNhanVienMoi();
            TaiKhoanDTO tkNew = pnlThemNV.getTaiKhoanMoi();

            if (nvNew == null || tkNew == null) {
                break;
            }

            Response resUpdateNv = SocketClient.getInstance().sendRequest(new Request(CommandType.SUA_NHAN_VIEN, new Object[]{maNV, nvNew}));
            Response resUpdateTk = SocketClient.getInstance().sendRequest(new Request(CommandType.CAP_NHAT_TAI_KHOAN, tkNew));

            if (resUpdateNv.isSuccess() && resUpdateTk.isSuccess()) {
                JOptionPane.showMessageDialog(this, "Sửa thông tin nhân viên thành công!");
                updateTable();
                isSuccess = true;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Sửa thông tin nhân viên thất bại.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hienThiChiTietNhanVien(MouseEvent e) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1 && e.getClickCount() == 2) {
            String maNV = model.getValueAt(selectRow, 1).toString();
            
            Response resNv = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_MA, maNV));
            Response resTk = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_TAI_KHOAN_BY_MA_NV, maNV));
            if (!resNv.isSuccess() || resNv.getData() == null) return;

            NhanVienDTO nvDaChon = (NhanVienDTO) resNv.getData();
            TaiKhoanDTO tk = null;
            if (resTk.isSuccess()) tk = (TaiKhoanDTO) resTk.getData();

            ThemNhanVienGUI pnlThemNV = new ThemNhanVienGUI();
            JDialog dialog = new JDialog();
            dialog.setTitle("Thông tin chi tiết nhân viên");
            dialog.setModal(true);
            dialog.setResizable(false);
            dialog.setContentPane(pnlThemNV);
            dialog.pack();
            dialog.setLocationRelativeTo(null);

            pnlThemNV.getBtnHuy().setVisible(false);
            pnlThemNV.getBtnXacNhan().setText("Đóng");
            pnlThemNV.getBtnXacNhan().addActionListener(l -> dialog.dispose());

            pnlThemNV.getTxtHoTenDem().setEditable(false);
            pnlThemNV.getTxtTen().setEditable(false);
            pnlThemNV.getTxtSDT().setEditable(false);
            pnlThemNV.getTxtCCCD().setEditable(false);

            pnlThemNV.getChonLichNgaySinh().setEnabled(false);
            pnlThemNV.getTxtDiaChi().setEditable(false);
            pnlThemNV.getCmbGioiTinh().setEditable(false);
            pnlThemNV.getChkQuanLy().setEnabled(false);
            pnlThemNV.getChkQuanLyLo().setEnabled(false);
            pnlThemNV.getTxtEmail().setEditable(false);
            pnlThemNV.getTxtTenDangNhap().setEditable(false);
            pnlThemNV.getTxtMatKhau().setEditable(false);

            pnlThemNV.setTxtMaNhanVien(nvDaChon.getMaNV());
            pnlThemNV.setTxtHoTenDem(nvDaChon.getHoTenDem());
            pnlThemNV.setTxtTen(nvDaChon.getTen());
            pnlThemNV.setTxtSDT(nvDaChon.getSdt());
            pnlThemNV.setTxtCCCD(nvDaChon.getCccd());
            pnlThemNV.setCmbGioiTinh(nvDaChon.isGioiTinh());
            pnlThemNV.setTxtNgaySinh(nvDaChon.getNgaySinh());
            pnlThemNV.setTxtDiaChi(nvDaChon.getDiaChi());

            if (tk != null) {
                pnlThemNV.setChkQuanLy(tk.isQuanLy());
                pnlThemNV.setChkQuanLyLo(tk.isQuanLyLo());
                pnlThemNV.setTxtEmail(tk.getEmail());
                pnlThemNV.setTxtTenDangNhap(nvDaChon.getMaNV());
                pnlThemNV.setTxtMatKhau(tk.getMatKhau());
                pnlThemNV.setTxtNgayTao(tk.getNgayTao());
            }

            dialog.setVisible(true);
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
