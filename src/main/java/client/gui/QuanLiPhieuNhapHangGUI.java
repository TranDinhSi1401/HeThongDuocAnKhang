package client.gui;

import common.dto.PhieuTraHangDTO;
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
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import javax.swing.event.*;

/**
 * GUI quản lý danh sách Phiếu Trả Hàng.
 * (Đây là màn hình danh sách; không có thêm/xóa/sửa ở đây)
 */
public class QuanLiPhieuNhapHangGUI extends JPanel {

    private JTextField txtTimKiem;
    private JTable table;
    private JComboBox<String> cmbTieuChiTimKiem;
    private DefaultTableModel model;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private JLabel lblTongSoDong;
    private JLabel lblSoDongChon;

    public QuanLiPhieuNhapHangGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout());
        JPanel pnlNorthRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        cmbTieuChiTimKiem = new JComboBox<>(new String[]{
            "Mã Phiếu Trả", "Mã Hóa Đơn Gốc", "Mã Nhân Viên"});
        cmbTieuChiTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbTieuChiTimKiem.setPreferredSize(new Dimension(180, 30));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5,5,5,5)));

        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pnlTimKiem.add(new JLabel("Tìm kiếm"));
        pnlTimKiem.add(txtTimKiem);
        pnlNorthRight.add(new JLabel("Tìm theo"));
        pnlNorthRight.add(cmbTieuChiTimKiem);
        pnlNorthRight.add(pnlTimKiem);
        pnlNorth.add(pnlNorthRight, BorderLayout.EAST);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã Phiếu Trả", "Mã Hóa Đơn Gốc", "Mã Nhân Viên", "Ngày Lập", "Tổng Tiền Hoàn Trả"};
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

        TableColumnModel cm = table.getColumnModel();
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        cm.getColumn(0).setPreferredWidth(40); cm.getColumn(0).setMaxWidth(40); cm.getColumn(0).setCellRenderer(center);
        cm.getColumn(1).setPreferredWidth(120); cm.getColumn(1).setCellRenderer(center);
        cm.getColumn(2).setPreferredWidth(120); cm.getColumn(2).setCellRenderer(center);
        cm.getColumn(3).setPreferredWidth(100); cm.getColumn(3).setCellRenderer(center);
        cm.getColumn(4).setPreferredWidth(130); cm.getColumn(4).setCellRenderer(center);
        cm.getColumn(5).setPreferredWidth(120); cm.getColumn(5).setCellRenderer(right);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        lblTongSoDong = new JLabel("Tổng số phiếu trả: 0");
        lblTongSoDong.setFont(new Font("Arial", Font.BOLD, 13));
        lblTongSoDong.setForeground(new Color(0, 102, 204));
        lblSoDongChon = new JLabel("Đang chọn: 0");
        lblSoDongChon.setFont(new Font("Arial", Font.BOLD, 13));
        lblSoDongChon.setForeground(new Color(204, 0, 0));
        pnlSouth.add(lblTongSoDong);
        pnlSouth.add(new JSeparator(JSeparator.VERTICAL));
        pnlSouth.add(lblSoDongChon);
        this.add(pnlSouth, BorderLayout.SOUTH);

        updateTable();
        addEvents();
    }

    private void updateTable(List<PhieuTraHangDTO> dsPTH) {
        model.setRowCount(0);
        if (dsPTH == null) { lblTongSoDong.setText("Tổng số phiếu trả: 0"); return; }
        int stt = 1;
        for (PhieuTraHangDTO pth : dsPTH) {
            model.addRow(new Object[]{stt++, pth.getMaPhieuTraHang(), pth.getMaHoaDon(), pth.getMaNV(),
                pth.getNgayLapPhieuTraHang() != null ? pth.getNgayLapPhieuTraHang().format(formatter) : "",
                String.format("%,.0f VND", pth.getTongTienHoaTra())});
        }
        lblTongSoDong.setText("Tổng số phiếu trả: " + dsPTH.size());
    }

    private void updateTable() {
        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_ALL_PHIEU_TRA_HANG, null));
        if (res.isSuccess() && res.getData() != null)
            updateTable((List<PhieuTraHangDTO>) res.getData());
        else updateTable((List<PhieuTraHangDTO>) null);
    }

    private void addEvents() {
        txtTimKiem.addActionListener(e -> xuLyTimKiem());
        cmbTieuChiTimKiem.addActionListener(e -> { txtTimKiem.setText(""); txtTimKiem.requestFocus(); });
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { hienThiChiTiet(e); }
        });
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                lblSoDongChon.setText("Đang chọn: " + table.getSelectedRowCount());
        });
    }

    private void xuLyTimKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        String tieuChi = cmbTieuChiTimKiem.getSelectedItem().toString();
        if (tuKhoa.isEmpty()) { updateTable(); return; }

        List<PhieuTraHangDTO> ds = new ArrayList<>();
        Response res;
        switch (tieuChi) {
            case "Mã Phiếu Trả":
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PTH_BY_MA, tuKhoa));
                if (res.isSuccess() && res.getData() != null) ds.add((PhieuTraHangDTO) res.getData());
                break;
            case "Mã Hóa Đơn Gốc":
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PTH_BY_MA_HD, tuKhoa));
                if (res.isSuccess() && res.getData() != null) ds = (List<PhieuTraHangDTO>) res.getData();
                break;
            case "Mã Nhân Viên":
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PTH_BY_MA_NV, tuKhoa));
                if (res.isSuccess() && res.getData() != null) ds = (List<PhieuTraHangDTO>) res.getData();
                break;
        }
        updateTable(ds);
    }

    private void hienThiChiTiet(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row != -1 && e.getClickCount() == 2) {
            String maPTH = model.getValueAt(row, 1).toString();
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_PTH_BY_MA, maPTH));
            if (res.isSuccess() && res.getData() != null) {
                PhieuTraHangDTO pthChon = (PhieuTraHangDTO) res.getData();
                ChiTietPhieuTraHangGUI pnl = new ChiTietPhieuTraHangGUI();
                pnl.loadData(pthChon);
                JDialog dlg = new JDialog();
                dlg.setTitle("Chi tiết Phiếu Trả Hàng: " + maPTH);
                dlg.setModal(true); dlg.setResizable(true);
                dlg.setContentPane(pnl);
                dlg.setPreferredSize(new Dimension(900, 400));
                dlg.pack(); dlg.setLocationRelativeTo(null);
                dlg.setVisible(true);
            }
        }
    }
}
