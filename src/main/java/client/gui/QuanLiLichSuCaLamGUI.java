package client.gui;

import com.toedter.calendar.JDateChooser;
import common.dto.LichSuCaLamDTO;
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
import java.beans.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.List;
import javax.swing.event.*;

public class QuanLiLichSuCaLamGUI extends JPanel {

    private JTextField txtTimKiem;
    private JDateChooser dcsNgayTimKiem;
    private JPanel pnlNhapLieu;
    private JTable table;
    private JComboBox<String> cmbTieuChiTimKiem;
    private DefaultTableModel model;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private JLabel lblTongSoDong;
    private JLabel lblSoDongChon;

    public QuanLiLichSuCaLamGUI() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel pnlNorth = new JPanel(new BorderLayout());
        JPanel pnlNorthRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        cmbTieuChiTimKiem = new JComboBox<>(new String[]{"Mã NV", "Ngày Làm (yyyy-MM-dd)"});
        cmbTieuChiTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        cmbTieuChiTimKiem.setPreferredSize(new Dimension(180, 30));

        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));

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

        String[] columnNames = {"STT", "Mã NV", "Ngày Làm", "Mã Ca", "Giờ Vào", "Giờ Ra", "Ghi Chú"};
        model = new DefaultTableModel(new Object[][]{}, columnNames) {
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

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i : new int[]{0,1,2,3,4,5}) table.getColumnModel().getColumn(i).setCellRenderer(center);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        lblTongSoDong = new JLabel("Tổng số lịch sử ca làm: 0");
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

    private void updateTable(List<LichSuCaLamDTO> dsLS) {
        model.setRowCount(0);
        if (dsLS == null) { lblTongSoDong.setText("Tổng số lịch sử ca làm: 0"); return; }
        int stt = 1;
        for (LichSuCaLamDTO ls : dsLS) {
            String gioRa = (ls.getThoiGianRaCa() != null) ? ls.getThoiGianRaCa().format(timeFormatter) : "Chưa ra ca";
            model.addRow(new Object[]{stt++, ls.getMaNV(), ls.getNgayLamViec(), ls.getMaCa(),
                ls.getThoiGianVaoCa() != null ? ls.getThoiGianVaoCa().format(timeFormatter) : "", gioRa, ls.getGhiChu()});
        }
        lblTongSoDong.setText("Tổng số lịch sử ca làm: " + dsLS.size());
    }

    private void updateTable() {
        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.GET_ALL_LICH_SU_CA_LAM, null));
        if (res.isSuccess() && res.getData() != null)
            updateTable((List<LichSuCaLamDTO>) res.getData());
        else updateTable((List<LichSuCaLamDTO>) null);
    }

    private void addEvents() {
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { xuLyTimKiem(); }
            @Override public void removeUpdate(DocumentEvent e) { xuLyTimKiem(); }
            @Override public void changedUpdate(DocumentEvent e) {}
        });
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
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
                lblSoDongChon.setText("Đang chọn: " + table.getSelectedRowCount());
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

        List<LichSuCaLamDTO> ds = new ArrayList<>();
        try {
            Response res;
            if (tieuChi.equals("Mã NV")) {
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_LSCL_BY_MA_NV, tuKhoa));
                if (res.isSuccess() && res.getData() != null) ds = (List<LichSuCaLamDTO>) res.getData();
            } else {
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_LSCL_BY_NGAY, tuKhoa));
                if (res.isSuccess() && res.getData() != null) ds = (List<LichSuCaLamDTO>) res.getData();
            }
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày nhập phải đúng định dạng YYYY-MM-DD.", "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
        }
        updateTable(ds);
    }

    private void hienThiChiTiet(MouseEvent e) {
        int row = table.getSelectedRow();
        if (row != -1 && e.getClickCount() == 2) {
            ChiTietLichSuCaLamGUI pnl = new ChiTietLichSuCaLamGUI();
            JDialog dlg = new JDialog();
            dlg.setTitle("Thông tin chi tiết Ca Làm");
            dlg.setModal(true); dlg.setResizable(false);
            dlg.setContentPane(pnl); dlg.pack(); dlg.setLocationRelativeTo(null);

            pnl.setTxtMaNhanVien(model.getValueAt(row,1).toString());
            pnl.setTxtNgayLam(model.getValueAt(row,2).toString());
            pnl.setTxtMaCa(model.getValueAt(row,3).toString());
            pnl.setTxtGioVao(model.getValueAt(row,4).toString());
            pnl.setTxtGioRa(model.getValueAt(row,5).toString());
            Object ghiChuObj = model.getValueAt(row,6);
            pnl.setTxtGhiChu(ghiChuObj != null ? ghiChuObj.toString() : "");

            pnl.getBtnHuy().setVisible(false);
            pnl.getBtnXacNhan().setVisible(true);
            pnl.getBtnXacNhan().setText("Đóng");
            for (ActionListener al : pnl.getBtnXacNhan().getActionListeners())
                pnl.getBtnXacNhan().removeActionListener(al);
            pnl.getBtnXacNhan().addActionListener(l -> dlg.dispose());
            dlg.setVisible(true);
        }
    }
}
