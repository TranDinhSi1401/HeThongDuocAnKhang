package client.gui;

import common.dto.ChiTietHoaDonDTO;
import common.dto.HoaDonDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChiTietHoaDonGUI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblMaHoaDon;
    private JLabel lblTongTien;

    public ChiTietHoaDonGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 2, 5, 5));
        lblMaHoaDon = new JLabel("Mã Hóa Đơn: ");
        lblTongTien = new JLabel("Tổng Tiền Hóa Đơn: ");
        lblMaHoaDon.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        pnlNorth.add(lblMaHoaDon);
        pnlNorth.add(new JLabel(""));
        pnlNorth.add(lblTongTien);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {"Mã CTHD", "Sản Phẩm", "ĐVT", "Số Lượng", "Đơn Giá", "Giảm Giá", "Thành Tiền"};
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

    /** Được gọi khi mở từ QuanLiHoaDonGUI với HoaDonDTO đã lấy qua socket */
    public void loadData(HoaDonDTO hoaDon) {
        if (hoaDon == null) return;
        lblMaHoaDon.setText("Mã Hóa Đơn: " + hoaDon.getMaHoaDon());
        lblTongTien.setText("Tổng Tiền Hóa Đơn: " + String.format("%,.0f VND", hoaDon.getTongTien()));

        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_CTHD_BY_MA_HD, hoaDon.getMaHoaDon()));
        model.setRowCount(0);
        if (res.isSuccess() && res.getData() != null) {
            List<ChiTietHoaDonDTO> dsCTHD = (List<ChiTietHoaDonDTO>) res.getData();
            for (ChiTietHoaDonDTO cthd : dsCTHD) {
                model.addRow(new Object[]{
                    cthd.getMaChiTietHoaDon(),
                    cthd.getTenSP(),
                    cthd.getTenDVT(),
                    cthd.getSoLuong(),
                    String.format("%,.0f", cthd.getDonGia()),
                    String.format("%.0f%%", cthd.getGiamGia() * 100),
                    String.format("%,.0f", cthd.getThanhTien())
                });
            }
        }
    }
}