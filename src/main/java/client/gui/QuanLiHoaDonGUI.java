/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.gui;

import com.toedter.calendar.JDateChooser;
import common.dto.HoaDonDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class QuanLiHoaDonGUI extends JPanel {

    private JButton btnLamMoi;
    private JTextField txtTimKiem;
    private JDateChooser chonLichNgayTimKiem, chonLichNgayKetThuc;
    private JPanel pnlNhapLieuTiimKiem, pnlDateRange;
    private JTable table;
    private JComboBox<String> cmbTieuChiTimKiem;
    private JComboBox<String> cmbBoLoc;
    private DefaultTableModel model;
    private JLabel lblTongSoDong;
    private JLabel lblSoDongChon;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public QuanLiHoaDonGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        // PANEL NORTH
        JPanel pnlNorth = new JPanel();
        pnlNorth.setLayout(new BorderLayout());

        // Panel Chức năng (LEFT)
        JPanel pnlNorthLeft = new JPanel();
        pnlNorthLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnlNorthLeft.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        btnLamMoi = new JButton("Làm mới (F5)");
        setupButton(btnLamMoi, new Color(255, 255, 255));
        
        pnlNorthLeft.add(btnLamMoi);
        
        pnlNorth.add(pnlNorthLeft, BorderLayout.WEST);

        // Panel Tìm kiếm và Lọc
        JPanel pnlNorthRight = new JPanel();
        pnlNorthRight.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        cmbTieuChiTimKiem = new JComboBox<>(new String[]{
            "Mã Hóa Đơn",
            "Mã Nhân Viên",
            "Mã Khách Hàng",
            "SĐT Khách Hàng",
            "Ngày lập (Một ngày)",
            "Khoảng ngày"
        });
        cmbTieuChiTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbTieuChiTimKiem.setPreferredSize(new Dimension(180, 30));

        cmbBoLoc = new JComboBox<>(new String[]{
            "Tất cả",
            "Tiền mặt",
            "Chuyển khoản"
        });
        cmbBoLoc.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbBoLoc.setPreferredSize(new Dimension(150, 30));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(5, 5, 5, 5)
        ));

        chonLichNgayTimKiem = new JDateChooser();
        chonLichNgayTimKiem.setDateFormatString("dd/MM/yyyy");
        chonLichNgayTimKiem.setPreferredSize(new Dimension(150, 30));
        chonLichNgayTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        chonLichNgayTimKiem.setDate(new Date());

        chonLichNgayKetThuc = new JDateChooser();
        chonLichNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        chonLichNgayKetThuc.setPreferredSize(new Dimension(150, 30));
        chonLichNgayKetThuc.setFont(new Font("Arial", Font.PLAIN, 14));
        chonLichNgayKetThuc.setDate(new Date());

        pnlDateRange = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        pnlDateRange.add(new JLabel("Từ:"));
        pnlDateRange.add(chonLichNgayTimKiem);
        pnlDateRange.add(new JLabel("Đến:"));
        pnlDateRange.add(chonLichNgayKetThuc);

        pnlNhapLieuTiimKiem = new JPanel(new CardLayout());
        pnlNhapLieuTiimKiem.add(txtTimKiem, "text");
        pnlNhapLieuTiimKiem.add(chonLichNgayTimKiem, "date");
        pnlNhapLieuTiimKiem.add(pnlDateRange, "range");

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlTimKiem.add(new JLabel("Tìm kiếm"));
        pnlTimKiem.add(pnlNhapLieuTiimKiem);

        pnlNorthRight.add(new JLabel("Tìm theo"));
        pnlNorthRight.add(cmbTieuChiTimKiem);
        pnlNorthRight.add(pnlTimKiem);
        pnlNorthRight.add(new JLabel("Lọc theo"));
        pnlNorthRight.add(cmbBoLoc);

        pnlNorth.add(pnlNorthRight, BorderLayout.EAST);
        this.add(pnlNorth, BorderLayout.NORTH);

        // PANEL CENTER (TABLE)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));

        String[] columnNames = {
            "STT",
            "Mã HĐ",
            "Mã Nhân Viên",
            "Mã Khách Hàng",
            "Ngày lập",
            "Tổng tiền",
            "Hình thức TT",
        };
        Object[][] data = {};

        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(230, 230, 230));
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setSelectionBackground(new Color(220, 240, 255));

        // CẤU HÌNH CỘT
        TableColumnModel columnModel = table.getColumnModel();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        rightRenderer.setBorder(new EmptyBorder(0, 0, 0, 5));

        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(50);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);

        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(1).setMaxWidth(150);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);

        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);

        columnModel.getColumn(3).setPreferredWidth(120);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);

        columnModel.getColumn(4).setPreferredWidth(130);
        columnModel.getColumn(4).setCellRenderer(centerRenderer);

        columnModel.getColumn(5).setPreferredWidth(120);
        columnModel.getColumn(5).setCellRenderer(rightRenderer);

        columnModel.getColumn(6).setPreferredWidth(120);
        columnModel.getColumn(6).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        this.add(centerPanel, BorderLayout.CENTER);

        // PANEL SOUTH (FOOTER)
        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        Font fontFooter = new Font("Arial", Font.BOLD, 13);

        lblTongSoDong = new JLabel("Tổng số hóa đơn: 0");
        lblTongSoDong.setFont(fontFooter);
        lblTongSoDong.setForeground(new Color(0, 102, 204));

        lblSoDongChon = new JLabel("Đang chọn: 0");
        lblSoDongChon.setFont(fontFooter);
        lblSoDongChon.setForeground(new Color(204, 0, 0));

        pnlInfo.add(lblTongSoDong);
        pnlInfo.add(new JSeparator(JSeparator.VERTICAL));
        pnlInfo.add(lblSoDongChon);

        pnlSouth.add(pnlInfo, BorderLayout.WEST);

        this.add(pnlSouth, BorderLayout.SOUTH);

        // Load dữ liệu ban đầu
        updateTable();
        addEvents();
    }

    private void updateTable(List<HoaDonDTO> dsHD) {
        model.setRowCount(0);
        if (dsHD == null) {
            lblTongSoDong.setText("Tổng số hóa đơn: 0");
            return;
        }
        int stt = 1;
        for (HoaDonDTO hd : dsHD) {
            String tenKH = (hd.getTenKH() != null && !hd.getTenKH().isEmpty())
                    ? hd.getTenKH() : (hd.getMaKH() != null ? hd.getMaKH() : "Khách lẻ");
            Object[] row = {
                stt++,
                hd.getMaHoaDon(),
                hd.getMaNV(),
                tenKH,
                hd.getNgayLapHoaDon() != null ? hd.getNgayLapHoaDon().format(formatter) : "",
                String.format("%,.0f VND", hd.getTongTien()),
                hd.isChuyenKhoan() ? "Chuyển khoản" : "Tiền mặt"
            };
            model.addRow(row);
        }
        lblTongSoDong.setText("Tổng số hóa đơn: " + dsHD.size());
    }

    private void updateTable() {
        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_ALL_HOA_DON, null));
        if (res.isSuccess() && res.getData() != null) {
            updateTable((List<HoaDonDTO>) res.getData());
        } else {
            updateTable((List<HoaDonDTO>) null);
        }
    }

    private void addEvents() {
        btnLamMoi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        txtTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyTimKiem();
            }
        });

        PropertyChangeListener dateChange = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    xuLyTimKiem();
                }
            }
        };
        chonLichNgayTimKiem.addPropertyChangeListener(dateChange);
        chonLichNgayKetThuc.addPropertyChangeListener(dateChange);

        cmbBoLoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xuLyLoc();
            }
        });

        cmbTieuChiTimKiem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) pnlNhapLieuTiimKiem.getLayout();
                String tieuChi = cmbTieuChiTimKiem.getSelectedItem().toString();

                if (tieuChi.equals("Ngày lập (Một ngày)")) {
                    cl.show(pnlNhapLieuTiimKiem, "date");
                } else if (tieuChi.equals("Khoảng ngày")) {
                    cl.show(pnlNhapLieuTiimKiem, "range");
                } else {
                    cl.show(pnlNhapLieuTiimKiem, "text");
                    txtTimKiem.selectAll();
                    txtTimKiem.requestFocus();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTietHoaDon(e);
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    lblSoDongChon.setText("Đang chọn: " + table.getSelectedRowCount());
                }
            }
        });
    }

    private void xuLyTimKiem() {
        String tieuChi = cmbTieuChiTimKiem.getSelectedItem().toString();
        List<HoaDonDTO> dsKetQua = new ArrayList<>();
        Response res;

        if (tieuChi.equals("Ngày lập (Một ngày)")) {
            Date date = chonLichNgayTimKiem.getDate();
            if (date != null) {
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_NGAY, localDate));
                if (res.isSuccess() && res.getData() != null) dsKetQua = (List<HoaDonDTO>) res.getData();
            }
        } else if (tieuChi.equals("Khoảng ngày")) {
            Date fromDate = chonLichNgayTimKiem.getDate();
            Date toDate = chonLichNgayKetThuc.getDate();
            if (fromDate != null && toDate != null) {
                LocalDate ldFrom = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate ldTo = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_KHOANG_NGAY, new Object[]{ldFrom, ldTo}));
                if (res.isSuccess() && res.getData() != null) dsKetQua = (List<HoaDonDTO>) res.getData();
            }
        } else {
            String tuKhoa = txtTimKiem.getText().trim();
            if (tuKhoa.isEmpty()) {
                updateTable();
                return;
            }
            switch (tieuChi) {
                case "Mã Hóa Đơn":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_MA, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) dsKetQua.add((HoaDonDTO) res.getData());
                    break;
                case "Mã Nhân Viên":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_MA_NV, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) dsKetQua = (List<HoaDonDTO>) res.getData();
                    break;
                case "Mã Khách Hàng":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_MA_KH, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) dsKetQua = (List<HoaDonDTO>) res.getData();
                    break;
                case "SĐT Khách Hàng":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_SDT_KH, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) dsKetQua = (List<HoaDonDTO>) res.getData();
                    break;
            }
        }
        updateTable(dsKetQua);
    }

    private void xuLyLoc() {
        String boLoc = cmbBoLoc.getSelectedItem().toString();

        switch (boLoc) {
            case "Tất cả":
                updateTable();
                break;
            case "Tiền mặt":
                locTheoHinhThuc(false);
                break;
            case "Chuyển khoản":
                locTheoHinhThuc(true);
                break;
        }
    }

    private void locTheoHinhThuc(boolean chuyenKhoan) {
        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_HOA_DON_BY_HINH_THUC, chuyenKhoan));
        if (res.isSuccess() && res.getData() != null) {
            updateTable((List<HoaDonDTO>) res.getData());
        } else {
            updateTable((List<HoaDonDTO>) null);
        }
    }

    private void hienThiChiTietHoaDon(MouseEvent e) {
        int selectRow = table.getSelectedRow();
        if (selectRow != -1) {
            String maHD = model.getValueAt(selectRow, 1).toString();

            if (e.getClickCount() == 2) {
                Response res = SocketClient.getInstance().sendRequest(
                        new Request(CommandType.GET_HOA_DON_BY_MA, maHD));
                if (res.isSuccess() && res.getData() != null) {
                    HoaDonDTO hdDaChon = (HoaDonDTO) res.getData();
                    ChiTietHoaDonGUI pnlChiTiet = new ChiTietHoaDonGUI();
                    pnlChiTiet.loadData(hdDaChon);

                    JDialog dialog = new JDialog();
                    dialog.setTitle("Danh sách chi tiết Hóa Đơn: " + maHD);
                    dialog.setModal(true);
                    dialog.setContentPane(pnlChiTiet);
                    dialog.setPreferredSize(new Dimension(800, 400));
                    dialog.pack();
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                }
            }
        }
    }

    private void setupButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
