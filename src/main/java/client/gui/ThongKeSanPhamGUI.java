package client.gui;

import common.dto.SanPhamDTO;
import common.dto.DonViTinhDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.socket.SocketClient;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ThongKeSanPhamGUI extends JPanel {

    private JTextField txtTopN;
    private JComboBox<String> cmbThang;
    private JComboBox<Integer> cmbNam;
    private JComboBox<String> cmbTieuChi;
    private JButton btnXemThongKe;
    private JTable table;
    private DefaultTableModel model;
    private JLabel lblTongDoanhThuTop;
    private JLabel lblThoiGianThongKe;

    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public ThongKeSanPhamGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout());
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlFilter.setBorder(new EmptyBorder(0, 0, 10, 0));

        Font fontLabel = new Font("Arial", Font.BOLD, 14);
        Font fontInput = new Font("Arial", Font.PLAIN, 14);

        JLabel lblTop = new JLabel("Top:");
        lblTop.setFont(fontLabel);
        txtTopN = new JTextField("10", 3);
        txtTopN.setFont(fontInput);
        txtTopN.setHorizontalAlignment(JTextField.CENTER);
        txtTopN.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), new EmptyBorder(5,5,5,5)));
        txtTopN.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> txtTopN.selectAll());
            }
        });

        JLabel lblTieuChi = new JLabel("Thống kê theo:");
        lblTieuChi.setFont(fontLabel);
        cmbTieuChi = new JComboBox<>(new String[]{"Số Lượng Bán"});
        cmbTieuChi.setFont(fontInput);
        cmbTieuChi.setPreferredSize(new Dimension(150, 30));

        JLabel lblThang = new JLabel("Tháng:");
        lblThang.setFont(fontLabel);
        String[] arrThang = new String[13];
        arrThang[0] = "Tất cả";
        for (int i = 1; i <= 12; i++) arrThang[i] = "Tháng " + i;
        cmbThang = new JComboBox<>(arrThang);
        cmbThang.setFont(fontInput);
        cmbThang.setPreferredSize(new Dimension(120, 30));

        JLabel lblNam = new JLabel("Năm:");
        lblNam.setFont(fontLabel);
        Vector<Integer> arrNam = new Vector<>();
        for (int i = 2025; i <= 2045; i++) arrNam.add(i);
        cmbNam = new JComboBox<>(arrNam);
        cmbNam.setFont(fontInput);
        cmbNam.setPreferredSize(new Dimension(100, 30));

        pnlFilter.add(lblTop); pnlFilter.add(txtTopN);
        pnlFilter.add(lblTieuChi); pnlFilter.add(cmbTieuChi);
        pnlFilter.add(lblThang); pnlFilter.add(cmbThang);
        pnlFilter.add(lblNam); pnlFilter.add(cmbNam);

        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnXemThongKe = new JButton("Xem thống kê");
        btnXemThongKe.setPreferredSize(new Dimension(140, 30));
        setupTopButton(btnXemThongKe, new Color(50, 150, 250));
        pnlActions.add(btnXemThongKe);

        pnlNorth.add(pnlFilter, BorderLayout.WEST);
        pnlNorth.add(pnlActions, BorderLayout.CENTER);
        this.add(pnlNorth, BorderLayout.NORTH);

        String[] columnNames = {"STT", "Mã SP", "Tên Sản Phẩm", "Đơn Vị Tính",
            "Số Lượng Bán", "Giá bán trung bình", "Tổng Doanh Thu"};
        model = new DefaultTableModel(new Object[][]{}, columnNames) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                if (c == 0 || c == 4) return Integer.class;
                if (c == 5 || c == 6) return Double.class;
                return String.class;
            }
        };
        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setAutoCreateRowSorter(false);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(220, 220, 220));
        table.getTableHeader().setReorderingAllowed(false);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        TableColumnModel cm = table.getColumnModel();
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer right = new DefaultTableCellRenderer();
        right.setHorizontalAlignment(JLabel.RIGHT);
        right.setBorder(new EmptyBorder(0,0,0,5));

        cm.getColumn(0).setPreferredWidth(50); cm.getColumn(0).setMaxWidth(50); cm.getColumn(0).setCellRenderer(center);
        cm.getColumn(1).setPreferredWidth(100); cm.getColumn(1).setCellRenderer(center);
        cm.getColumn(2).setPreferredWidth(250);
        cm.getColumn(3).setPreferredWidth(80); cm.getColumn(3).setCellRenderer(center);
        cm.getColumn(4).setPreferredWidth(100); cm.getColumn(4).setCellRenderer(center);
        cm.getColumn(5).setPreferredWidth(120); cm.getColumn(5).setCellRenderer(right);
        cm.getColumn(6).setPreferredWidth(150); cm.getColumn(6).setCellRenderer(right);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(new EmptyBorder(5, 0, 0, 0));
        lblThoiGianThongKe = new JLabel("Thời gian: Chưa chọn");
        lblThoiGianThongKe.setFont(new Font("Arial", Font.ITALIC, 14));
        lblTongDoanhThuTop = new JLabel("Tổng doanh thu (Top N): 0 đ");
        lblTongDoanhThuTop.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThuTop.setForeground(new Color(204, 0, 0));
        pnlSouth.add(lblThoiGianThongKe, BorderLayout.WEST);
        pnlSouth.add(lblTongDoanhThuTop, BorderLayout.EAST);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnXemThongKe.addActionListener(e -> updateTable());
    }

    private void updateTable() {
        model.setRowCount(0);
        int thang = cmbThang.getSelectedIndex();
        int nam = (Integer) cmbNam.getSelectedItem();

        int topN = 0;
        try {
            topN = Integer.parseInt(txtTopN.getText());
            if (topN <= 0) {
                JOptionPane.showMessageDialog(this, "Top N phải là số nguyên dương.");
                txtTopN.setText("10"); return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số nguyên cho Top N.");
            return;
        }

        if (thang == 0) lblThoiGianThongKe.setText("Thời gian: Cả năm " + nam);
        else lblThoiGianThongKe.setText("Thời gian: Tháng " + thang + "/" + nam);

        // Gọi server lấy sản phẩm bán chạy
        Response res;
        List<SanPhamDTO> dsSP;
        if (thang == 0) {
            res = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.GET_SP_BAN_CHAY_NAM, LocalDate.of(nam, 1, 1)));
        } else {
            res = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.GET_SP_BAN_CHAY_THANG, LocalDate.of(nam, thang, 1)));
        }

        if (!res.isSuccess() || res.getData() == null) {
            lblTongDoanhThuTop.setText("Tổng doanh thu (Top " + topN + "): 0 đ");
            return;
        }

        // Server trả về List<Object[]> hoặc List<SanPhamDTO> tùy implementation
        // Giả sử server trả về List<Object[]> với [maSP, tenSP, soLuong, tongTien, donGiaTB]
        List<Object[]> dsRaw = (List<Object[]>) res.getData();

        double tongDoanhThuTopN = 0;
        int limit = Math.min(topN, dsRaw.size());
        for (int i = 0; i < limit; i++) {
            Object[] entry = dsRaw.get(i);
            String maSP    = entry[0].toString();
            String tenSP   = entry[1].toString();
            int soLuong    = ((Number) entry[2]).intValue();
            double tongTien = ((Number) entry[3]).doubleValue();
            double donGiaTB = ((Number) entry[4]).doubleValue();

            // Lấy đơn vị tính cơ bản
            String tenDVT = "N/A";
            Response resDVT = SocketClient.getInstance().sendRequest(
                    new Request(CommandType.GET_DVT_CO_BAN_BY_MA_SP, maSP));
            if (resDVT.isSuccess() && resDVT.getData() != null) {
                DonViTinhDTO dvt = (DonViTinhDTO) resDVT.getData();
                tenDVT = dvt.getTenDonViTinh();
            }

            tongDoanhThuTopN += tongTien;
            model.addRow(new Object[]{i+1, maSP, tenSP, tenDVT, soLuong,
                String.format("%,.0f VND", donGiaTB), String.format("%,.0f VND", tongTien)});
        }
        lblTongDoanhThuTop.setText("Tổng doanh thu (Top " + topN + "): " + currencyFormat.format(tongDoanhThuTopN));
    }

    private void setupTopButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setMargin(new Insets(5, 10, 5, 10));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
    }

    public JTextField getTxtTopN() { return txtTopN; }
    public JComboBox<String> getCmbThang() { return cmbThang; }
    public JComboBox<Integer> getCmbNam() { return cmbNam; }
    public JComboBox<String> getCmbTieuChi() { return cmbTieuChi; }
    public JButton getBtnXemThongKe() { return btnXemThongKe; }
    public JTable getTable() { return table; }
    public DefaultTableModel getModel() { return model; }
    public JLabel getLblTongDoanhThuTop() { return lblTongDoanhThuTop; }
}
