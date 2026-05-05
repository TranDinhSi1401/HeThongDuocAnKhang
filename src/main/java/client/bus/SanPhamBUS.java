package client.bus;

import client.socket.SocketClient;
import common.dto.*;
import common.network.*;
import client.gui.ThemSanPhamGUI;
import client.gui.QuanLiSanPhamGUI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;

public class SanPhamBUS {

    public List<SanPhamDTO> getAllSanPham() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_SAN_PHAM, null));
        return (res.isSuccess()) ? (List<SanPhamDTO>) res.getData() : new ArrayList<>();
    }

    public SanPhamDTO getSanPhamByMa(String maSP) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, maSP));
        return (res.isSuccess()) ? (SanPhamDTO) res.getData() : null;
    }

    public List<DonViTinhDTO> getDVTByMaSP(String maSP) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_DVT_BY_MA_SP, maSP));
        return (res.isSuccess()) ? (List<DonViTinhDTO>) res.getData() : new ArrayList<>();
    }

    public List<MaVachSanPhamDTO> getMaVachByMaSP(String maSP) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_VACH_BY_MA_SP, maSP));
        return (res.isSuccess()) ? (List<MaVachSanPhamDTO>) res.getData() : new ArrayList<>();
    }

    public List<SanPhamCungCapDTO> getSPCCByMaSP(String maSP) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SPCC_BY_MA_SP, maSP));
        return (res.isSuccess()) ? (List<SanPhamCungCapDTO>) res.getData() : new ArrayList<>();
    }

    public List<KhuyenMaiSanPhamDTO> getKMSPByMaSP(String maSP) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KMSP_BY_MA_SP, maSP));
        return (res.isSuccess()) ? (List<KhuyenMaiSanPhamDTO>) res.getData() : new ArrayList<>();
    }

    public void loadDataToTable(JTable table, List<SanPhamDTO> ds) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<SanPhamDTO> list = (ds != null) ? ds : getAllSanPham();
        int stt = 1;
        for (SanPhamDTO sp : list) {
            model.addRow(new Object[] {
                    stt++,
                    sp.getMaSP(),
                    sp.getTen(),
                    sp.getMoTa(),
                    sp.getThanhPhan(),
                    sp.getLoaiSanPham(),
                    sp.getTonToiThieu(),
                    sp.getTonToiDa(),
                    sp.isDaXoa() ? "Đã xóa" : "Đang bán"
            });
        }
    }

    public void chuanBiFormThem(ThemSanPhamGUI gui) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_SP_CUOI, null));
        int maCuoi = (res.isSuccess()) ? (int) res.getData() : 0;
        gui.getTxtMaSanPham().setText(String.format("SP-%05d", maCuoi + 1));
    }

    public void chuanBiFormSua(ThemSanPhamGUI gui, String maSP) {
        SanPhamDTO sp = getSanPhamByMa(maSP);
        if (sp == null)
            return;

        gui.getTxtMaSanPham().setText(sp.getMaSP());
        gui.getTxtTenSanPham().setText(sp.getTen());
        gui.getTxtMoTa().setText(sp.getMoTa());
        gui.getTxtThanhPhan().setText(sp.getThanhPhan());
        gui.getCmbLoaiSanPham().setSelectedItem(sp.getLoaiSanPham());
        gui.getTxtTonToiThieu().setText(String.valueOf(sp.getTonToiThieu()));
        gui.getTxtTonToiDa().setText(String.valueOf(sp.getTonToiDa()));

        // Barcodes
        List<MaVachSanPhamDTO> dsMV = getMaVachByMaSP(maSP);
        for (MaVachSanPhamDTO mv : dsMV) {
            gui.getModelBarcode().addRow(new Object[] { mv.getMaVach() });
        }

        // DVT
        List<DonViTinhDTO> dsDVT = getDVTByMaSP(maSP);
        for (DonViTinhDTO dvt : dsDVT) {
            gui.getModelDVT().addRow(new Object[] {
                    dvt.getMaDonViTinh(), dvt.getTenDonViTinh(), dvt.getHeSoQuyDoi(), dvt.getGiaBanTheoDonVi(),
                    dvt.isDonViTinhCoBan()
            });
        }

        // NCC
        List<SanPhamCungCapDTO> dsSPCC = getSPCCByMaSP(maSP);
        for (SanPhamCungCapDTO spcc : dsSPCC) {
            Response resNCC = SocketClient.getInstance()
                    .sendRequest(new Request(CommandType.GET_NCC_BY_MA, spcc.getMaNCC()));
            if (resNCC.isSuccess()) {
                NhaCungCapDTO ncc = (NhaCungCapDTO) resNCC.getData();
                gui.getModelNCCChon().addRow(new Object[] { ncc.getMaNCC(), ncc.getTenNCC(), spcc.getGiaNhap() });
            }
        }

        // KM
        List<KhuyenMaiSanPhamDTO> dsKMSP = getKMSPByMaSP(maSP);
        for (KhuyenMaiSanPhamDTO kmsp : dsKMSP) {
            Response resKM = SocketClient.getInstance()
                    .sendRequest(new Request(CommandType.GET_KHUYEN_MAI_BY_MA, kmsp.getMaKhuyenMai()));
            if (resKM.isSuccess()) {
                KhuyenMaiDTO km = (KhuyenMaiDTO) resKM.getData();
                gui.getModelKMChon().addRow(new Object[] { km.getMaKhuyenMai(), km.getMoTa(), km.getPhanTram(), 1, 999,
                        kmsp.getNgayChinhSua() });
            }
        }
    }

    public boolean luuSanPham(ThemSanPhamGUI gui, boolean isUpdate) {
        try {
            SanPhamDTO sp = SanPhamDTO.builder()
                    .maSP(gui.getTxtMaSanPham().getText())
                    .ten(gui.getTxtTenSanPham().getText())
                    .moTa(gui.getTxtMoTa().getText())
                    .thanhPhan(gui.getTxtThanhPhan().getText())
                    .loaiSanPham(gui.getCmbLoaiSanPham().getSelectedItem().toString())
                    .tonToiThieu(Integer.parseInt(gui.getTxtTonToiThieu().getText()))
                    .tonToiDa(Integer.parseInt(gui.getTxtTonToiDa().getText()))
                    .daXoa(false)
                    .build();

            Response res;
            if (isUpdate) {
                res = SocketClient.getInstance()
                        .sendRequest(new Request(CommandType.SUA_SAN_PHAM, new Object[] { sp.getMaSP(), sp }));
            } else {
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_SAN_PHAM, sp));
            }

            if (!res.isSuccess()) {
                JOptionPane.showMessageDialog(gui, "Lưu thông tin cơ bản thất bại: " + res.getMessage());
                return false;
            }

            // Xóa hết DVT, Barcode, NCC cũ nếu là Update (để lưu lại cái mới)
            if (isUpdate) {
                SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_DVT_BY_MA_SP, sp.getMaSP()));
                SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_HET_SPCC_BY_MA_SP, sp.getMaSP()));
                // MaVach thì khó xóa hết vì primary key là maVach, nhưng tạm thời bỏ qua hoặc
                // xử lý sau
            }

            // Lưu Barcodes
            for (int i = 0; i < gui.getModelBarcode().getRowCount(); i++) {
                String barcode = gui.getModelBarcode().getValueAt(i, 0).toString();
                SocketClient.getInstance()
                        .sendRequest(new Request(CommandType.ADD_MA_VACH, new MaVachSanPhamDTO(barcode, sp.getMaSP())));
            }

            // Lưu DVT
            for (int i = 0; i < gui.getModelDVT().getRowCount(); i++) {
                DonViTinhDTO dvt = DonViTinhDTO.builder()
                        .maDonViTinh(gui.getModelDVT().getValueAt(i, 0).toString())
                        .tenDonViTinh(gui.getModelDVT().getValueAt(i, 1).toString())
                        .heSoQuyDoi(Integer.parseInt(gui.getModelDVT().getValueAt(i, 2).toString()))
                        .giaBanTheoDonVi(Double.parseDouble(gui.getModelDVT().getValueAt(i, 3).toString()))
                        .donViTinhCoBan((boolean) gui.getModelDVT().getValueAt(i, 4))
                        .maSP(sp.getMaSP())
                        .build();
                SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_DON_VI_TINH, dvt));
            }

            // Lưu NCC
            for (int i = 0; i < gui.getModelNCCChon().getRowCount(); i++) {
                SanPhamCungCapDTO spcc = SanPhamCungCapDTO.builder()
                        .maNCC(gui.getModelNCCChon().getValueAt(i, 0).toString())
                        .maSP(sp.getMaSP())
                        .giaNhap(Double.parseDouble(gui.getModelNCCChon().getValueAt(i, 2).toString()))
                        .trangThaiHopTac(true)
                        .build();
                SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_SAN_PHAM_CUNG_CAP, spcc));
            }

            // Lưu KM (Tạm thời bỏ qua hoặc xử lý đơn giản)
            // ...

            JOptionPane.showMessageDialog(gui, "Lưu sản phẩm thành công!");
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gui, "Lỗi: " + e.getMessage());
            return false;
        }
    }

    public void xuLyThemDVT(ThemSanPhamGUI gui) {
        String ten = gui.getCboTenDonVi().getSelectedItem().toString().trim();
        if (ten.isEmpty())
            return;

        int heSo = 1;
        if (!gui.getChkDonViCoBan().isSelected()) {
            try {
                heSo = Integer.parseInt(gui.getTxtHeSoQuyDoi().getText());
            } catch (Exception e) {
                return;
            }
        }
        double gia = 0;
        try {
            gia = Double.parseDouble(gui.getTxtGiaBanDonVi().getText().replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return;
        }

        Response resMa = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_DVT_CUOI, null));
        int maCuoi = (resMa.isSuccess()) ? (int) resMa.getData() : 0;
        String maDVT = String.format("DVT-%05d", maCuoi + 1 + gui.getModelDVT().getRowCount());

        gui.getModelDVT().addRow(new Object[] { maDVT, ten, heSo, gia, gui.getChkDonViCoBan().isSelected() });
    }

    public void xuLyXoaDVT(ThemSanPhamGUI gui) {
        int row = gui.getTblDonViTinh().getSelectedRow();
        if (row >= 0)
            gui.getModelDVT().removeRow(row);
    }

    public void xuLyTimNCC(ThemSanPhamGUI gui) {
        String keyword = gui.getTxtTimNCC().getText().trim();
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NCC_BY_TEN, keyword));
        if (res.isSuccess()) {
            List<NhaCungCapDTO> list = (List<NhaCungCapDTO>) res.getData();
            gui.getModelTimKiemNCC().setRowCount(0);
            for (NhaCungCapDTO ncc : list) {
                gui.getModelTimKiemNCC()
                        .addRow(new Object[] { ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi() });
            }
        }
    }

    public void xuLyThemNCC(ThemSanPhamGUI gui) {
        int row = gui.getTableKQTimKiemNCC().getSelectedRow();
        if (row < 0)
            return;
        String ma = gui.getModelTimKiemNCC().getValueAt(row, 0).toString();
        String ten = gui.getModelTimKiemNCC().getValueAt(row, 1).toString();
        double gia = 0;
        try {
            gia = Double.parseDouble(gui.getTxtGiaNhap().getText().replaceAll("[^\\d.]", ""));
        } catch (Exception e) {
            return;
        }

        gui.getModelNCCChon().addRow(new Object[] { ma, ten, gia });
    }

    public void xuLyXoaNCC(ThemSanPhamGUI gui) {
        int row = gui.getTblNCCChon().getSelectedRow();
        if (row >= 0)
            gui.getModelNCCChon().removeRow(row);
    }

    public void xuLyTimKM(ThemSanPhamGUI gui) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_KHUYEN_MAI, null));
        if (res.isSuccess()) {
            List<KhuyenMaiDTO> list = (List<KhuyenMaiDTO>) res.getData();
            gui.getModelKQTimKiemKM().setRowCount(0);
            for (KhuyenMaiDTO km : list) {
                gui.getModelKQTimKiemKM().addRow(new Object[] { km.getMaKhuyenMai(), km.getMoTa(), km.getPhanTram(),
                        km.getNgayBatDau(), km.getNgayKetThuc() });
            }
        }
    }

    public void xuLyThemKM(ThemSanPhamGUI gui) {
        int row = gui.getTblTimKiemKM().getSelectedRow();
        if (row < 0)
            return;
        String ma = gui.getModelKQTimKiemKM().getValueAt(row, 0).toString();
        String desc = gui.getModelKQTimKiemKM().getValueAt(row, 1).toString();
        double tile = Double.parseDouble(gui.getModelKQTimKiemKM().getValueAt(row, 2).toString());

        gui.getModelKMChon().addRow(new Object[] { ma, desc, tile, 1, 999, LocalDateTime.now() });
    }

    public void xuLyXoaKM(ThemSanPhamGUI gui) {
        int row = gui.getTblKMChon().getSelectedRow();
        if (row >= 0)
            gui.getModelKMChon().removeRow(row);
    }

    public void xuLyXoaSanPham(QuanLiSanPhamGUI gui) {
        int row = gui.getTable().getSelectedRow();
        if (row < 0)
            return;
        String ma = gui.getModel().getValueAt(row, 1).toString();
        int opt = JOptionPane.showConfirmDialog(gui, "Bạn có chắc muốn xóa sản phẩm " + ma + "?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_SAN_PHAM, ma));
            if (res.isSuccess()) {
                JOptionPane.showMessageDialog(gui, "Đã xóa thành công!");
                loadDataToTable(gui.getTable(), null);
            } else {
                JOptionPane.showMessageDialog(gui, "Lỗi: " + res.getMessage());
            }
        }
    }

    public void xuLyTimKiem(QuanLiSanPhamGUI gui) {
        String kw = gui.getTxtTimKiem().getText().trim();
        String criteria = gui.getCmbTieuChiTimKiem().getSelectedItem().toString();
        List<SanPhamDTO> result = new ArrayList<>();
        if (kw.isEmpty()) {
            result = getAllSanPham();
        } else {
            if (criteria.equals("Mã sản phẩm")) {
                SanPhamDTO sp = getSanPhamByMa(kw);
                if (sp != null)
                    result.add(sp);
            } else if (criteria.equals("Tên sản phẩm")) {
                Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_TEN, kw));
                if (res.isSuccess())
                    result = (List<SanPhamDTO>) res.getData();
            }
            // NCC search placeholder
        }
        loadDataToTable(gui.getTable(), result);
    }

    public void xuLyLoc(QuanLiSanPhamGUI gui) {
        String loc = gui.getCmbBoLoc().getSelectedItem().toString();
        if (loc.equals("Tất cả")) {
            loadDataToTable(gui.getTable(), null);
        } else {
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_LOAI, loc));
            if (res.isSuccess())
                loadDataToTable(gui.getTable(), (List<SanPhamDTO>) res.getData());
        }
    }
}
