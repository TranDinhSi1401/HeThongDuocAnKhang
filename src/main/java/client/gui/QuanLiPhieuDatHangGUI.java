package client.gui;

import com.toedter.calendar.JDateChooser;
import common.dto.PhieuDatHangDTO;
import common.dto.ChiTietPhieuDatHangDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;

public class QuanLiPhieuDatHangGUI extends JPanel {

    private JTextField txtTimKiem;
    private JDateChooser dcsNgayTimKiem;
    private JPanel pnlNhapLieu;
    private JTable table;
    private JComboBox<String> cmbTieuChiTimKiem;
    private DefaultTableModel model;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public QuanLiPhieuDatHangGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout());
        JPanel pnlNorthRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        cmbTieuChiTimKiem = new JComboBox<>(new String[]{
            "Mã Phiếu Đặt", "Mã Nhà Cung Cấp", "Mã Nhân Viên", "Ngày Lập (yyyy-MM-dd)"});
        cmbTieuChiTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbTieuChiTimKiem.setPreferredSize(new Dimension(180, 30));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5,5,5,5)));

        dcsNgayTimKiem = new JDateChooser();
        dcsNgayTimKiem.setDateFormatString("yyyy-MM-dd");
        dcsNgayTimKiem.setPreferredSize(new Dimension(200, 30));

        pnlNhapLieu = new JPanel(new CardLayout());
        pnlNhapLieu.add(txtTimKiem, "text");
        pnlNhapLieu.add(dcsNgayTimKiem, "date");

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlTimKiem.add(new JLabel("Tìm kiếm"));
        pnlTimKiem.add(pnlNhapLieu);
        pnlNorthRight.add(new JLabel("Tìm theo"));
        pnlNorthRight.add(cmbTieuChiTimKiem);
        pnlNorthRight.add(pnlTimKiem);
        pnlNorth.add(pnlNorthRight, BorderLayout.EAST);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"Mã Phiếu Đặt", "Nhà Cung Cấp", "Nhân Viên Lập", "Ngày Lập", "Tổng Tiền"};
        model = new DefaultTableModel(new Object[][]{}, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        updateTable();
        addEvents();
    }

    private void updateTable(List<PhieuDatHangDTO> dsPDH) {
        model.setRowCount(0);
        if (dsPDH == null) return;
        for (PhieuDatHangDTO pdh : dsPDH) {
            String tenNCC = (pdh.getTenNCC() != null && !pdh.getTenNCC().isEmpty()) ? pdh.getTenNCC() : pdh.getMaNCC();
            String tenNV  = (pdh.getTenNV() != null && !pdh.getTenNV().isEmpty()) ? pdh.getTenNV() : pdh.getMaNV();
            model.addRow(new Object[]{pdh.getMaPhieuDat(), tenNCC, tenNV,
                pdh.getNgayLap() != null ? pdh.getNgayLap().format(formatter) : "",
                String.format("%,.0f VND", pdh.getTongTien())});
        }
    }

    private void updateTable() {
        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_ALL_PHIEU_DAT_HANG, null));
        if (res.isSuccess() && res.getData() != null)
            updateTable((List<PhieuDatHangDTO>) res.getData());
        else updateTable((List<PhieuDatHangDTO>) null);
    }

    private void addEvents() {
        txtTimKiem.addActionListener(e -> xuLyTimKiem());
        dcsNgayTimKiem.addPropertyChangeListener("date", e -> xuLyTimKiem());
        cmbTieuChiTimKiem.addActionListener(e -> {
            CardLayout cl = (CardLayout) pnlNhapLieu.getLayout();
            if (cmbTieuChiTimKiem.getSelectedItem().toString().contains("Ngày")) {
                cl.show(pnlNhapLieu, "date");
            } else {
                cl.show(pnlNhapLieu, "text");
                txtTimKiem.setText(""); txtTimKiem.requestFocus();
            }
        });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { hienThiChiTiet(e); }
        });
    }

    private void xuLyTimKiem() {
        String tieuChi = cmbTieuChiTimKiem.getSelectedItem().toString();
        String tuKhoa = "";
        if (tieuChi.contains("Ngày")) {
            java.util.Date date = dcsNgayTimKiem.getDate();
            if (date != null) tuKhoa = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        } else {
            tuKhoa = txtTimKiem.getText().trim();
        }
        if (tuKhoa.isEmpty()) { updateTable(); return; }

        List<PhieuDatHangDTO> ds = new ArrayList<>();
        try {
            Response res;
            switch (tieuChi) {
                case "Mã Phiếu Đặt":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PHIEU_DAT_BY_MA, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) ds.add((PhieuDatHangDTO) res.getData());
                    break;
                case "Mã Nhà Cung Cấp":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PHIEU_DAT_BY_MA_NCC, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) ds = (List<PhieuDatHangDTO>) res.getData();
                    break;
                case "Mã Nhân Viên":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PHIEU_DAT_BY_MA_NV, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) ds = (List<PhieuDatHangDTO>) res.getData();
                    break;
                case "Ngày Lập (yyyy-MM-dd)":
                    LocalDate date = LocalDate.parse(tuKhoa);
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PHIEU_DAT_BY_NGAY, date));
                    if (res.isSuccess() && res.getData() != null) ds = (List<PhieuDatHangDTO>) res.getData();
                    break;
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày nhập phải đúng định dạng YYYY-MM-DD.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
        updateTable(ds);
    }

    private void hienThiChiTiet(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row != -1 && e.getClickCount() == 2) {
            String maPDH = model.getValueAt(row, 0).toString();
            Response res = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.GET_PHIEU_DAT_BY_MA, maPDH));
            if (res.isSuccess() && res.getData() != null) {
                PhieuDatHangDTO pdhChon = (PhieuDatHangDTO) res.getData();
                ChiTietPhieuDatHangGUI pnl = new ChiTietPhieuDatHangGUI();
                pnl.loadData(pdhChon);
                JDialog dlg = new JDialog();
                dlg.setTitle("Chi tiết Phiếu Đặt: " + maPDH);
                dlg.setModal(true); dlg.setResizable(true);
                dlg.setContentPane(pnl);
                dlg.setPreferredSize(new Dimension(800, 400));
                dlg.pack(); dlg.setLocationRelativeTo(null);
                dlg.setVisible(true);
            }
        }
    }
}