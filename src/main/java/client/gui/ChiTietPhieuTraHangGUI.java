package client.gui;

import common.dto.ChiTietPhieuTraHangDTO;
import common.dto.PhieuTraHangDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ChiTietPhieuTraHangGUI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblMaPhieuTra;
    private JLabel lblTongTienHoan;

    public ChiTietPhieuTraHangGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new GridLayout(2, 2, 5, 5));
        lblMaPhieuTra = new JLabel("Mã Phiếu Trả: ");
        lblTongTienHoan = new JLabel("Tổng Tiền Hoàn Trả: ");
        lblMaPhieuTra.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongTienHoan.setFont(new Font("Arial", Font.BOLD, 14));
        pnlNorth.add(lblMaPhieuTra);
        pnlNorth.add(new JLabel(""));
        pnlNorth.add(lblTongTienHoan);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {
            "Mã CTHD", "Sản Phẩm", "Đơn vị tính",
            "Số Lượng Trả", "Lý Do Trả", "Tình Trạng SP",
            "Giá Trị Hoàn", "Tiền Hoàn Trả"
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

    /** Được gọi từ QuanLiPhieuNhapHangGUI với PhieuTraHangDTO qua socket */
    public void loadData(PhieuTraHangDTO phieuTraHang) {
        if (phieuTraHang == null) return;
        lblMaPhieuTra.setText("Mã Phiếu Trả: " + phieuTraHang.getMaPhieuTraHang());
        lblTongTienHoan.setText("Tổng Tiền Hoàn Trả: " + String.format("%,.0f VND", phieuTraHang.getTongTienHoaTra()));

        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_CTPTH_BY_MA_PTH, phieuTraHang.getMaPhieuTraHang()));
        model.setRowCount(0);
        if (res.isSuccess() && res.getData() != null) {
            List<ChiTietPhieuTraHangDTO> ds = (List<ChiTietPhieuTraHangDTO>) res.getData();
            for (ChiTietPhieuTraHangDTO ct : ds) {
                String lyDo = mapTruongHop(ct.getTruongHopDoiTra());
                String tinhTrang = mapTinhTrang(ct.getTinhTrangSanPham());
                model.addRow(new Object[]{
                    ct.getMaChiTietHoaDon(),
                    ct.getTenSP() != null ? ct.getTenSP() : "",
                    ct.getTenDVT() != null ? ct.getTenDVT() : "",
                    ct.getSoLuong(), lyDo, tinhTrang,
                    ct.getGiaTriHoanTra(),
                    String.format("%,.0f", ct.getThanhTienHoanTra())
                });
            }
        }
    }

    private String mapTruongHop(String val) {
        if (val == null) return "";
        switch (val) {
            case "HANG_LOI_DO_NHA_SAN_XUAT": return "Hàng lỗi do nhà sản xuất";
            case "DI_UNG_MAN_CAM": return "Khách hàng dị ứng, mẫn cảm";
            default: return "Nhu cầu khách hàng";
        }
    }

    private String mapTinhTrang(String val) {
        if (val == null) return "";
        switch (val) {
            case "HANG_NGUYEN_VEN": return "Hàng nguyên vẹn";
            case "HANG_KHONG_NGUYEN_VEN": return "Hàng không nguyên vẹn";
            default: return "Hàng đã sử dụng";
        }
    }
}