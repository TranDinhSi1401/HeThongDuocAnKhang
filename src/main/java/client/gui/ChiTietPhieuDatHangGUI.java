package client.gui;

import common.dto.ChiTietPhieuDatHangDTO;
import common.dto.PhieuDatHangDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChiTietPhieuDatHangGUI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblMaPhieuDat;
    private JLabel lblTongTien;

    public ChiTietPhieuDatHangGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 2, 5, 5));
        lblMaPhieuDat = new JLabel("Mã Phiếu Đặt: ");
        lblTongTien = new JLabel("Tổng Tiền Phiếu Đặt: ");
        lblMaPhieuDat.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        pnlNorth.add(lblMaPhieuDat);
        pnlNorth.add(new JLabel(""));
        pnlNorth.add(lblTongTien);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {"Mã SP", "Tên Sản Phẩm", "Số Lượng", "Đơn Giá", "Thành Tiền"};
        model = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    /** Được gọi khi mở từ QuanLiPhieuDatHangGUI với PhieuDatHangDTO đã lấy qua socket */
    public void loadData(PhieuDatHangDTO phieuDat) {
        if (phieuDat == null) return;
        lblMaPhieuDat.setText("Mã Phiếu Đặt: " + phieuDat.getMaPhieuDat());
        lblTongTien.setText("Tổng Tiền Phiếu Đặt: " + String.format("%,.0f VND", phieuDat.getTongTien()));

        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_CT_PHIEU_DAT_BY_MA_PDH, phieuDat.getMaPhieuDat()));
        model.setRowCount(0);
        if (res.isSuccess() && res.getData() != null) {
            List<ChiTietPhieuDatHangDTO> ds = (List<ChiTietPhieuDatHangDTO>) res.getData();
            for (ChiTietPhieuDatHangDTO ct : ds) {
                model.addRow(new Object[]{
                    ct.getMaSP(), ct.getTenSP(), ct.getSoLuong(),
                    String.format("%,.0f", ct.getDonGia()),
                    String.format("%,.0f", ct.getThanhTien())
                });
            }
        }
    }
}