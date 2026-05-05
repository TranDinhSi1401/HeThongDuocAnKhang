package client.gui;

import common.dto.ChiTietPhieuNhapDTO;
import common.dto.PhieuNhapDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChiTietPhieuNhapGUI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblMaPhieuNhap;
    private JLabel lblTongTien;

    public ChiTietPhieuNhapGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 2, 5, 5));
        lblMaPhieuNhap = new JLabel("Mã Phiếu Nhập: ");
        lblTongTien = new JLabel("Tổng Tiền: ");
        lblMaPhieuNhap.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        pnlNorth.add(lblMaPhieuNhap);
        pnlNorth.add(new JLabel(""));
        pnlNorth.add(lblTongTien);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {
            "Mã Lô SP", "Sản Phẩm", "Nhà Cung Cấp",
            "Số Lượng", "Đơn Giá", "Thành Tiền",
            "Số Lượng Yêu Cầu", "Ghi Chú"
        };
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

    /** Được gọi từ các màn hình quản lý phiếu nhập với PhieuNhapDTO qua socket */
    public void loadData(PhieuNhapDTO phieuNhap) {
        if (phieuNhap == null) return;
        lblMaPhieuNhap.setText("Mã Phiếu Nhập: " + phieuNhap.getMaPhieuNhap());
        lblTongTien.setText("Tổng Tiền: " + String.format("%,.0f VND", phieuNhap.getTongTien()));

        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_CTPN_BY_MA_PN, phieuNhap.getMaPhieuNhap()));
        model.setRowCount(0);
        if (res.isSuccess() && res.getData() != null) {
            List<ChiTietPhieuNhapDTO> ds = (List<ChiTietPhieuNhapDTO>) res.getData();
            for (ChiTietPhieuNhapDTO ct : ds) {
                model.addRow(new Object[]{
                    ct.getMaLoSanPham(),
                    ct.getTenSP() != null ? ct.getTenSP() : "",
                    ct.getTenNCC() != null ? ct.getTenNCC() : ct.getMaNCC(),
                    ct.getSoLuong(),
                    String.format("%,.0f", ct.getDonGia()),
                    String.format("%,.0f", ct.getThanhTien()),
                    ct.getSoLuongYeuCau(),
                    ct.getGhiChu()
                });
            }
        }
    }
}
