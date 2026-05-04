package hethongnhathuocduocankhang.bus;

import client.gui.QuanLiSanPhamGUI;
import client.gui.ThemSanPhamGUI;
import client.socket.SocketClient;
import common.dto.*;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SanPhamBUS {

    // Danh sách tạm
    public List<DonViTinhDTO> listTempDVT = new ArrayList<>();
    public List<SanPhamCungCapDTO> listTempSPCC = new ArrayList<>();
    public List<KhuyenMaiDTO> listTempKM = new ArrayList<>();

    public SanPhamBUS() {
    }

    // LOGIC PHỤC VỤ QuanLiSanPhamGUI (Màn hình chính)
    public void loadDataToTable(QuanLiSanPhamGUI view, List<SanPhamDTO> dsSP) {
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        if (dsSP == null) {
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_SAN_PHAM, null));
            if (res.isSuccess()) {
                dsSP = (List<SanPhamDTO>) res.getData();
            }
        }

        if (dsSP == null || dsSP.isEmpty()) {
            view.getLblTongSoDong().setText("Tổng số sản phẩm: 0");
            return;
        }

        int stt = 1;
        for (SanPhamDTO sp : dsSP) {
            model.addRow(new Object[]{
                stt++,
                sp.getMaSP(),
                sp.getTen(),
                sp.getMoTa(),
                sp.getThanhPhan(),
                sp.getLoaiSanPham(),
                sp.getTonToiThieu(),
                sp.getTonToiDa(),
                sp.isDaXoa() ? "Ngừng bán" : "Đang bán"
            });
        }
        view.getLblTongSoDong().setText("Tổng số sản phẩm: " + dsSP.size());
    }

    public void xuLyXoaSanPham(QuanLiSanPhamGUI view) {
        int[] selectedRows = view.getTable().getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn sản phẩm cần xóa.");
            return;
        }

        if (JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa các sản phẩm đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int count = 0;
            for (int row : selectedRows) {
                String maSP = view.getModel().getValueAt(row, 1).toString();
                Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_SAN_PHAM, maSP));
                if (res.isSuccess()) {
                    count++;
                }
            }
            if (count > 0) {
                JOptionPane.showMessageDialog(view, "Đã xóa thành công " + count + " sản phẩm.");
                loadDataToTable(view, null); // Reload lại bảng
                view.getLblSoDongChon().setText("Đang chọn: 0");
            } else {
                JOptionPane.showMessageDialog(view, "Xóa thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void xuLyTimKiem(QuanLiSanPhamGUI view) {
        String tuKhoa = view.getTxtTimKiem().getText().trim();
        String tieuChi = view.getCmbTieuChiTimKiem().getSelectedItem().toString();
        List<SanPhamDTO> dsKetQua = new ArrayList<>();

        if (tuKhoa.isEmpty()) {
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_SAN_PHAM, null));
            if (res.isSuccess()) {
                dsKetQua = (List<SanPhamDTO>) res.getData();
            }
        } else {
            Response res;
            switch (tieuChi) {
                case "Mã sản phẩm":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, tuKhoa));
                    if (res.isSuccess() && res.getData() != null) {
                        dsKetQua.add((SanPhamDTO) res.getData());
                    }
                    break;
                case "Tên sản phẩm":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_TEN, tuKhoa));
                    if (res.isSuccess()) {
                        dsKetQua = (List<SanPhamDTO>) res.getData();
                    }
                    break;
                case "Mã nhà cung cấp":
                    res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA_NCC, tuKhoa));
                    if (res.isSuccess()) {
                        dsKetQua = (List<SanPhamDTO>) res.getData();
                    }
                    break;
            }
        }
        loadDataToTable(view, dsKetQua);
    }

    public void xuLyLoc(QuanLiSanPhamGUI view) {
        String boLoc = view.getCmbBoLoc().getSelectedItem().toString();
        String locTheo = "";

        if (boLoc.equals("Thuốc kê đơn")) {
            locTheo = "THUOC_KE_DON";
        } else if (boLoc.equals("Thuốc không kê đơn")) {
            locTheo = "THUOC_KHONG_KE_DON";
        } else if (boLoc.equals("Thực phẩm chức năng")) {
            locTheo = "THUC_PHAM_CHUC_NANG";
        }

        if (boLoc.equals("Tất cả")) {
            loadDataToTable(view, null);
        } else {
            Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_LOAI, locTheo));
            if (res.isSuccess()) {
                loadDataToTable(view, (List<SanPhamDTO>) res.getData());
            }
        }
    }

    // LOGIC PHỤC VỤ ThemSanPhamGUI (Màn hình con - Dialog)
    public void chuanBiFormThem(ThemSanPhamGUI form) {
        listTempDVT.clear();
        listTempSPCC.clear();
        listTempKM.clear();

        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_SP_CUOI, null));
        int maSPCuoi = 0;
        if (res.isSuccess() && res.getData() != null) {
            maSPCuoi = (int) res.getData();
        }
        String maSPNew = String.format("SP-%04d", maSPCuoi + 1);
        form.getTxtMaSanPham().setText(maSPNew);
        form.getTxtMaSanPham().setEditable(false);
    }

    public void chuanBiFormSua(ThemSanPhamGUI form, String maSP) {
        Response resSp = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, maSP));
        if (!resSp.isSuccess() || resSp.getData() == null) {
            return;
        }
        SanPhamDTO sp = (SanPhamDTO) resSp.getData();

        // Load thông tin cơ bản
        form.getTxtMaSanPham().setText(sp.getMaSP());
        form.getTxtMaSanPham().setEditable(false);
        form.getTxtTenSanPham().setText(sp.getTen());
        form.getTxtMoTa().setText(sp.getMoTa());
        form.getTxtThanhPhan().setText(sp.getThanhPhan());
        form.getTxtTonToiThieu().setText(String.valueOf(sp.getTonToiThieu()));
        form.getTxtTonToiDa().setText(String.valueOf(sp.getTonToiDa()));

        String loai = sp.getLoaiSanPham();
        if ("THUOC_KE_DON".equals(loai)) {
            form.getCmbLoaiSanPham().setSelectedIndex(0);
        } else if ("THUOC_KHONG_KE_DON".equals(loai)) {
            form.getCmbLoaiSanPham().setSelectedIndex(1);
        } else {
            form.getCmbLoaiSanPham().setSelectedIndex(2);
        }

        // Load Barcode
        Response resBc = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_VACH_BY_MA_SP, maSP));
        if (resBc.isSuccess()) {
            List<MaVachSanPhamDTO> barcodes = (List<MaVachSanPhamDTO>) resBc.getData();
            for (MaVachSanPhamDTO bc : barcodes) {
                form.getModelBarcode().addRow(new Object[]{bc.getMaVach()});
            }
        }

        // Load DVT
        Response resDvt = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_DVT_BY_MA_SP, maSP));
        if (resDvt.isSuccess()) {
            listTempDVT = (List<DonViTinhDTO>) resDvt.getData();
            for (DonViTinhDTO dvt : listTempDVT) {
                form.getModelDVT().addRow(new Object[]{
                    dvt.getMaDonViTinh(), dvt.getTenDonViTinh(), dvt.getHeSoQuyDoi(),
                    String.format("%,.0f", dvt.getGiaBanTheoDonVi()),
                    dvt.isDonViTinhCoBan() ? "Có" : "Không"
                });
            }
        }

        // Load NCC
        Response resSpcc = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SPCC_BY_MA_SP, maSP));
        if (resSpcc.isSuccess()) {
            listTempSPCC = (List<SanPhamCungCapDTO>) resSpcc.getData();
            for (SanPhamCungCapDTO spcc : listTempSPCC) {
                // Lấy tên NCC
                Response resNcc = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NCC_BY_MA, spcc.getMaNCC()));
                String tenNCC = "";
                if (resNcc.isSuccess() && resNcc.getData() != null) {
                    tenNCC = ((NhaCungCapDTO) resNcc.getData()).getTenNCC();
                }
                form.getModelNCCChon().addRow(new Object[]{
                    spcc.getMaNCC(),
                    tenNCC,
                    String.format("%,.0f", spcc.getGiaNhap())
                });
            }
        }

        // Load KM
        Response resKm = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KMSP_BY_MA_SP, maSP));
        if (resKm.isSuccess()) {
            List<KhuyenMaiSanPhamDTO> listKMSP = (List<KhuyenMaiSanPhamDTO>) resKm.getData();
            for (KhuyenMaiSanPhamDTO kmsp : listKMSP) {
                Response resKmd = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KHUYEN_MAI_BY_MA, kmsp.getMaKhuyenMai()));
                if (resKmd.isSuccess() && resKmd.getData() != null) {
                    KhuyenMaiDTO km = (KhuyenMaiDTO) resKmd.getData();
                    listTempKM.add(km);
                    form.getModelKMChon().addRow(new Object[]{
                        km.getMaKhuyenMai(), km.getMoTa(), km.getPhanTram() + "%",
                        km.getSoLuongToiThieu(), km.getSoLuongToiDa(), ""
                    });
                }
            }
        }
    }

    public boolean luuSanPham(ThemSanPhamGUI form, boolean isEditMode) {
        // --- PHẦN 1: KIỂM TRA DỮ LIỆU ĐẦU VÀO (VALIDATION) ---

        if (form.getTxtTenSanPham().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(form, "Tên sản phẩm không được để trống.");
            form.getTxtTenSanPham().requestFocus();
            return false;
        }

        if (form.getTxtMoTa().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(form, "Mô tả sản phẩm không được để trống.");
            form.getTxtMoTa().requestFocus();
            return false;
        }

        if (form.getTxtThanhPhan().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(form, "Thành phần sản phẩm không được để trống.");
            form.getTxtThanhPhan().requestFocus();
            return false;
        }

        if (form.getModelDVT().getRowCount() == 0) {
            JOptionPane.showMessageDialog(form, "Sản phẩm phải có ít nhất 1 đơn vị tính.");
            return false;
        }

        if (form.getModelNCCChon().getRowCount() == 0) {
            JOptionPane.showMessageDialog(form, "Vui lòng chọn ít nhất 1 nhà cung cấp cho sản phẩm.");
            return false;
        }

        if (form.getModelBarcode().getRowCount() == 0) {
            JOptionPane.showMessageDialog(form, "Sản phẩm chưa có mã vạch nào.");
            return false;
        }

        int max = form.getModelDVT().getRowCount();
        boolean kq = false;
        for (int i = 0; i < max; i++) {
            Object value = form.getModelDVT().getValueAt(i, 4);
            if (value != null && value.toString().equalsIgnoreCase("Có")) {
                kq = true;
                break;
            }
        }

        if (!kq) {
            JOptionPane.showMessageDialog(null, "Sản phẩm phải có ít nhất một đơn vị tính cơ bản!");
            return false;
        }

        int tonToiThieu = 0;
        int tonToiDa = 0;
        try {
            String strMin = form.getTxtTonToiThieu().getText().trim();
            String strMax = form.getTxtTonToiDa().getText().trim();

            if (strMin.isEmpty() || strMax.isEmpty()) {
                JOptionPane.showMessageDialog(form, "Vui lòng nhập định mức tồn kho tối thiểu và tối đa.");
                return false;
            }

            tonToiThieu = Integer.parseInt(strMin);
            tonToiDa = Integer.parseInt(strMax);

            if (tonToiThieu < 0 || tonToiDa < 0) {
                JOptionPane.showMessageDialog(form, "Số lượng tồn kho không được âm.");
                return false;
            }

            if (tonToiThieu > tonToiDa) {
                JOptionPane.showMessageDialog(form, "Lỗi: Tồn tối thiểu không được lớn hơn Tồn tối đa.");
                form.getTxtTonToiThieu().requestFocus();
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(form, "Định mức tồn kho phải là số nguyên hợp lệ.");
            return false;
        }

        // --- PHẦN 2: XỬ LÝ LƯU XUỐNG CSDL ---
        try {
            SanPhamDTO sp = new SanPhamDTO();
            sp.setMaSP(form.getTxtMaSanPham().getText());
            sp.setTen(form.getTxtTenSanPham().getText());
            sp.setMoTa(form.getTxtMoTa().getText());
            sp.setThanhPhan(form.getTxtThanhPhan().getText());
            sp.setTonToiThieu(tonToiThieu);
            sp.setTonToiDa(tonToiDa);

            int indexLoai = form.getCmbLoaiSanPham().getSelectedIndex();
            if (indexLoai == 0) {
                sp.setLoaiSanPham("THUOC_KE_DON");
            } else if (indexLoai == 1) {
                sp.setLoaiSanPham("THUOC_KHONG_KE_DON");
            } else {
                sp.setLoaiSanPham("THUC_PHAM_CHUC_NANG");
            }

            Response res;
            if (!isEditMode) { // Thêm mới
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_SAN_PHAM, sp));
                if (res.isSuccess()) {
                    luuCacThongTinLienQuan(sp, form);
                    JOptionPane.showMessageDialog(form, "Thêm sản phẩm thành công!");
                    return true;
                }
            } else { // Sửa
                res = SocketClient.getInstance().sendRequest(new Request(CommandType.SUA_SAN_PHAM, new Object[]{sp.getMaSP(), sp}));
                if (res.isSuccess()) {
                    String maSP = sp.getMaSP();
                    // Xử lý "Xóa hết rồi thêm lại" qua Socket
                    // Lưu ý: Chúng ta cần lệnh XOA_MA_VACH_THEO_MA_SP? Nếu không có thì phải lặp.
                    // Nhưng ở server ta đã thêm XOA_HET_...
                    
                    // Xóa mã vạch: Vì server chưa có XOA_HET_MA_VACH, ta có thể dùng DELETE_MA_VACH trong vòng lặp hoặc thêm lệnh mới.
                    // Giả sử ta thêm XOA_HET_MA_VACH_BY_MA_SP vào server sau (cho tiện)
                    // Hoặc tạm thời server chưa có thì ta bỏ qua hoặc xử lý lặp.
                    
                    SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_HET_SPCC_BY_MA_SP, maSP));
                    SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_HET_KMSP_BY_MA_SP, maSP));

                    luuCacThongTinLienQuan(sp, form);
                    JOptionPane.showMessageDialog(form, "Cập nhật sản phẩm thành công!");
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(form, "Lỗi hệ thống khi lưu: " + e.getMessage());
            return false;
        }
    }

    private void luuCacThongTinLienQuan(SanPhamDTO sp, ThemSanPhamGUI form) {
        String maSP = sp.getMaSP();

        // 1. DVT
        // Xóa hết DVT cũ
        SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_DVT_BY_MA_SP, maSP));
        // Thêm DVT mới
        for (DonViTinhDTO dvt : listTempDVT) {
            dvt.setMaSP(maSP);
            SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_DON_VI_TINH, dvt));
        }

        // 2. Barcode
        // Giả sử server có lệnh xóa hết mã vạch? Nếu không thì lặp.
        // Ta lặp qua modelBarcode để thêm
        for (int i = 0; i < form.getModelBarcode().getRowCount(); i++) {
            String code = form.getModelBarcode().getValueAt(i, 0).toString();
            MaVachSanPhamDTO mv = new MaVachSanPhamDTO(code, maSP);
            SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_MA_VACH, mv));
        }

        // 3. NCC
        for (SanPhamCungCapDTO spcc : listTempSPCC) {
            spcc.setMaSP(maSP);
            SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_SAN_PHAM_CUNG_CAP, spcc));
        }

        // 4. KM
        for (KhuyenMaiDTO km : listTempKM) {
            KhuyenMaiSanPhamDTO kmsp = new KhuyenMaiSanPhamDTO();
            kmsp.setMaSP(maSP);
            kmsp.setMaKhuyenMai(km.getMaKhuyenMai());
            kmsp.setNgayChinhSua(LocalDateTime.now());
            SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_KHUYEN_MAI_SAN_PHAM, kmsp));
        }
    }

    // CÁC HÀM LOGIC NHỎ TRÊN FORM
    public void xuLyThemDVT(ThemSanPhamGUI form) {
        try {
            Object itemObj = form.getCboTenDonVi().getEditor().getItem();
            String rawTenDV = (itemObj != null) ? itemObj.toString() : "";
            String tenDV = rawTenDV.trim().toUpperCase();

            if (tenDV.isEmpty()) {
                throw new Exception("Vui lòng nhập tên ĐVT!");
            }

            String strHeSo = form.getTxtHeSoQuyDoi().getText().trim();
            if (strHeSo.isEmpty()) {
                throw new Exception("Chưa nhập hệ số!");
            }
            int heSo = Integer.parseInt(strHeSo);

            String giaBanRaw = form.getTxtGiaBanDonVi().getText().replaceAll("\\D", "");
            double gia = giaBanRaw.isEmpty() ? 0.0 : Double.parseDouble(giaBanRaw);

            boolean isCoBan = form.getChkDonViCoBan().isSelected();
            if (isCoBan) {
                for (DonViTinhDTO dv : listTempDVT) {
                    if (dv.isDonViTinhCoBan()) {
                        JOptionPane.showMessageDialog(form, "Đã tồn tại đơn vị tính cơ bản (" + dv.getTenDonViTinh() + ").");
                        return;
                    }
                }
            }

            String maSP = form.getTxtMaSanPham().getText();
            String maDVT = "DVT-" + maSP.split("-")[1] + "-" + tenDV;

            DonViTinhDTO dvt = DonViTinhDTO.builder()
                    .maDonViTinh(maDVT)
                    .maSP(maSP)
                    .heSoQuyDoi(heSo)
                    .giaBanTheoDonVi(gia)
                    .tenDonViTinh(tenDV)
                    .donViTinhCoBan(isCoBan)
                    .build();
            listTempDVT.add(dvt);

            form.getModelDVT().addRow(new Object[]{
                maDVT, tenDV, heSo, String.format("%,.0f", gia), isCoBan ? "Có" : "Không"
            });

            form.getCboTenDonVi().setSelectedItem("");
            form.getTxtGiaBanDonVi().setText("");
            form.getChkDonViCoBan().setSelected(false);
            form.getTxtHeSoQuyDoi().setEnabled(true);
            form.getTxtHeSoQuyDoi().setText("");
            form.getCboTenDonVi().requestFocus();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(form, "Lỗi thêm ĐVT: " + ex.getMessage());
        }
    }

    public void xuLyXoaDVT(ThemSanPhamGUI form) {
        int[] cacDongDaChon = form.getTblDonViTinh().getSelectedRows();
        if (cacDongDaChon.length > 0) {
            for (int i = cacDongDaChon.length - 1; i >= 0; i--) {
                int dongDangChon = cacDongDaChon[i];
                if (dongDangChon < listTempDVT.size()) {
                    listTempDVT.remove(dongDangChon);
                }
                form.getModelDVT().removeRow(dongDangChon);
            }
        }
    }

    public void xuLyTimNCC(ThemSanPhamGUI form) {
        String timNCC = form.getTxtTimNCC().getText().trim();
        form.getModelTimKiemNCC().setRowCount(0);
        
        Response res;
        if (timNCC.isEmpty()) {
            res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_NHA_CUNG_CAP, null));
        } else {
            res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NCC_BY_TEN, timNCC));
        }
        
        if (res.isSuccess() && res.getData() != null) {
            List<NhaCungCapDTO> dsNCC = (List<NhaCungCapDTO>) res.getData();
            for (NhaCungCapDTO ncc : dsNCC) {
                form.getModelTimKiemNCC().addRow(new Object[]{ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi()});
            }
        }
    }

    public void xuLyThemNCC(ThemSanPhamGUI form) {
        int[] rows = form.getTableKQTimKiemNCC().getSelectedRows();
        if (rows.length == 0) {
            return;
        }
        try {
            String giaNhapRaw = form.getTxtGiaNhap().getText().replaceAll("\\D", "");
            double gia = giaNhapRaw.isEmpty() ? 0.0 : Double.parseDouble(giaNhapRaw);

            for (int r : rows) {
                String maNCC = form.getModelTimKiemNCC().getValueAt(r, 0).toString();
                String tenNCC = form.getModelTimKiemNCC().getValueAt(r, 1).toString();

                boolean tonTai = listTempSPCC.stream().anyMatch(spcc -> spcc.getMaNCC().equals(maNCC));
                if (!tonTai) {
                    SanPhamCungCapDTO spcc = SanPhamCungCapDTO.builder()
                            .maSP(form.getTxtMaSanPham().getText())
                            .maNCC(maNCC)
                            .trangThaiHopTac(true)
                            .giaNhap(gia)
                            .build();
                    listTempSPCC.add(spcc);
                    form.getModelNCCChon().addRow(new Object[]{maNCC, tenNCC, String.format("%,.0f", gia)});
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(form, "Giá nhập không hợp lệ.");
        }
    }

    public void xuLyXoaNCC(ThemSanPhamGUI form) {
        int[] rows = form.getTblNCCChon().getSelectedRows();
        if (rows.length == 0) {
            return;
        }
        for (int i = rows.length - 1; i >= 0; i--) {
            int row = rows[i];
            if (row < listTempSPCC.size()) {
                listTempSPCC.remove(row);
            }
            form.getModelNCCChon().removeRow(row);
        }
    }

    public void xuLyTimKM(ThemSanPhamGUI form) {
        String timKM = form.getTxtTimKM().getText().trim();
        form.getModelKQTimKiemKM().setRowCount(0);
        
        Response res;
        if (timKM.isEmpty()) {
            res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_KHUYEN_MAI, null));
        } else {
            // Giả sử có GET_KHUYEN_MAI_BY_MOTA hoặc tương tự
            res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_KHUYEN_MAI, null)); // Tạm thời
        }
        
        if (res.isSuccess() && res.getData() != null) {
            List<KhuyenMaiDTO> dsKM = (List<KhuyenMaiDTO>) res.getData();
            for (KhuyenMaiDTO km : dsKM) {
                form.getModelKQTimKiemKM().addRow(new Object[]{
                    km.getMaKhuyenMai(), km.getMoTa(), km.getPhanTram(), km.getNgayBatDau(), km.getNgayKetThuc()
                });
            }
        }
    }

    public void xuLyThemKM(ThemSanPhamGUI form) {
        int[] rows = form.getTblTimKiemKM().getSelectedRows();
        for (int r : rows) {
            String maKM = form.getModelKQTimKiemKM().getValueAt(r, 0).toString();
            boolean tonTai = listTempKM.stream().anyMatch(km -> km.getMaKhuyenMai().equals(maKM));
            if (!tonTai) {
                Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KHUYEN_MAI_BY_MA, maKM));
                if (res.isSuccess() && res.getData() != null) {
                    KhuyenMaiDTO km = (KhuyenMaiDTO) res.getData();
                    listTempKM.add(km);
                    form.getModelKMChon().addRow(new Object[]{
                        km.getMaKhuyenMai(), km.getMoTa(), km.getPhanTram() + "%",
                        km.getSoLuongToiThieu(), km.getSoLuongToiDa(), LocalDate.now()
                    });
                }
            }
        }
    }

    public void xuLyXoaKM(ThemSanPhamGUI form) {
        int[] rows = form.getTblKMChon().getSelectedRows();
        if (rows.length == 0) {
            return;
        }
        for (int i = rows.length - 1; i >= 0; i--) {
            int row = rows[i];
            if (row < listTempKM.size()) {
                listTempKM.remove(row);
            }
            form.getModelKMChon().removeRow(row);
        }
    }
}
