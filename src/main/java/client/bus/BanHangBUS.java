//package client.bus;
//
//import client.gui.GiaoDienChinhGUI;
//import client.socket.SocketClient;
//import common.network.CommandType;
//import common.network.Request;
//import common.network.Response;
//import common.dto.*;
//import java.awt.Font;
//import java.text.DecimalFormat;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import javax.swing.JDialog;
//import javax.swing.JOptionPane;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextArea;
//import javax.swing.table.DefaultTableModel;
//
//
///**
// *
// * @author trand
// */
//public class BanHangBUS {
//    private static final String LINE = "=".repeat(120);
//    private static final String SEPARATOR = "-".repeat(120);
//
//    /**
//     * Lấy integer từ giá trị bảng, loại bỏ dấu phân cách
//     * Ví dụ: "4.000" → 4, "50,000" → 50000
//     */
//    private static int parseIntFromTable(Object value) {
//        if (value == null) return 0;
//        String str = value.toString()
//            .replaceAll("[^0-9-]", "")  // Loại bỏ mọi ký tự không phải số hoặc dấu âm
//            .trim();
//        if (str.isEmpty()) return 0;
//        return Integer.parseInt(str);
//    }
//
//    /**
//     * Lấy double từ giá trị bảng, loại bỏ dấu phân cách và ký tự tệ tệ
//     * Ví dụ: "50.000 ₫" → 50000.0, "10.5%" → 10.5
//     * Logic: loại bỏ mọi ký tự không phải số, sau đó parse
//     */
//    private static double parseDoubleFromTable(Object value) {
//        if (value == null) return 0.0;
//        String str = value.toString()
//            .replaceAll("[^0-9.]", "")  // Chỉ giữ số và dấu chấm
//            .trim();
//        if (str.isEmpty()) return 0.0;
//
//        // Xử lý dấu chấm: loại bỏ tất cả dấu chấm ngoại trừ dấu chấm cuối (nếu có)
//        // Pattern: nếu có dấu chấm ở vị trí thứ n từ cuối với đúng 3 chữ số, đó là dấu phân cách
//        int countDot = (int) str.chars().filter(c -> c == '.').count();
//
//        if (countDot == 0) {
//            // Không có dấu chấm → là số nguyên
//            return Double.parseDouble(str);
//        } else if (countDot == 1) {
//            // Một dấu chấm: có thể là dấu thập phân hoặc dấu phân cách
//            int dotIndex = str.indexOf('.');
//            int digitsAfterDot = str.length() - dotIndex - 1;
//
//            if (digitsAfterDot == 3) {
//                // 3 chữ số sau dấu chấm → là dấu phân cách, loại bỏ nó
//                str = str.replace(".", "");
//            }
//            // Còn lại là dấu thập phân, giữ nguyên
//        } else {
//            // Nhiều dấu chấm: đó là dấu phân cách hàng nghìn, loại bỏ tất cả trừ dấu chấm cuối
//            // Ví dụ: "1.234.567,89" → giữ dấu chấm cuối nếu 2 chữ số sau, nếu không loại bỏ tất cả
//            int lastDotIndex = str.lastIndexOf('.');
//            int digitsAfterLastDot = str.length() - lastDotIndex - 1;
//
//            if (digitsAfterLastDot == 3) {
//                // Dấu chấm cuối là dấu phân cách → loại bỏ tất cả dấu chấm
//                str = str.replaceAll("\\.", "");
//            } else if (digitsAfterLastDot == 2 || digitsAfterLastDot == 1) {
//                // Dấu chấm cuối là dấu thập phân → loại bỏ các dấu chấm khác
//                str = str.substring(0, lastDotIndex).replaceAll("\\.", "") + str.substring(lastDotIndex);
//            } else {
//                // Loại bỏ tất cả dấu chấm
//                str = str.replaceAll("\\.", "");
//            }
//        }
//
//        return Double.parseDouble(str);
//    }
//
//    public String chuanHoaMaSP(String input) {
//        if (input == null) return "";
//        input = input.trim().toUpperCase();
//        // Nếu dạng chuẩn rồi thì giữ nguyên
//        if (input.matches("^SP-\\d{4}$")) {
//            return input;
//        }
//        // Nếu nhân viên nhập sp0001
//        if (input.matches("^SP\\d{4}$")) {
//            return input.substring(0, 2) + "-" + input.substring(2);
//        }
//        // Nếu nhân viên chỉ nhập số
//        if (input.matches("^\\d{4}$")) {
//            return "SP-" + input;
//        }
//        // Nếu nhân viên quét mã sản phẩm
//        if (input.matches("^\\d{12}$")) {
//            Response res = client.socket.SocketClient.getInstance().sendRequest(
//                new common.network.Request(common.network.CommandType.GET_MA_SP_BY_MA_VACH, input)
//            );
//            if (res.isSuccess() && res.getData() != null) {
//                return (String) res.getData();
//            }
//        }
//        return input;
//    }
//
//    public Object[] themChiTietHoaDon(String maSp, JTable tblCTHD) throws Exception {
//        String maSP = chuanHoaMaSP(maSp);
//
//        // Fetch SanPham
//        Response resSp = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_SAN_PHAM_BY_MA, maSP));
//        common.dto.SanPhamDTO sp = (resSp.isSuccess()) ? (common.dto.SanPhamDTO) resSp.getData() : null;
//        if (sp == null) throw new Exception("Sản phẩm không tồn tại!");
//
//        // Fetch DonViTinh
//        Response resDvt = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_DVT_BY_MA_SP, maSP));
//        java.util.List<common.dto.DonViTinhDTO> dsDVT = (resDvt.isSuccess()) ? (java.util.List<common.dto.DonViTinhDTO>) resDvt.getData() : null;
//        if (dsDVT == null || dsDVT.isEmpty()) throw new Exception("Sản phẩm này chưa có đơn vị tính!");
//
//        // Fetch KhuyenMai
//        Response resKm = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_KHUYEN_MAI_BY_MA_SP, maSP));
//        java.util.List<common.dto.KhuyenMaiDTO> dsKM = (resKm.isSuccess()) ? (java.util.List<common.dto.KhuyenMaiDTO>) resKm.getData() : new java.util.ArrayList<>();
//
//        // Fetch LoSanPham
//        Response resLo = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_LO_BY_MA_SP, maSP));
//        java.util.List<common.dto.LoSanPhamDTO> dsLSP = (resLo.isSuccess()) ? (java.util.List<common.dto.LoSanPhamDTO>) resLo.getData() : new java.util.ArrayList<>();
//        int tongSoLuong = dsLSP.stream().mapToInt(common.dto.LoSanPhamDTO::getSoLuong).sum();
//        if (tongSoLuong <= 0) throw new Exception("Sản phẩm hiện tại hết hàng!");
//
//        // Lấy đơn vị tính cơ bản (hệ số thấp nhất)
//        dsDVT.sort((a, b) -> Integer.compare(a.getHeSoQuyDoi(), b.getHeSoQuyDoi()));
//        common.dto.DonViTinhDTO dvtMacDinh = dsDVT.get(0);
//        String tenDVT = dvtMacDinh.getTenDonViTinh();
//
//        // Nếu trùng sản phẩm trong bảng
//        for (int i = 0; i < tblCTHD.getRowCount(); i++) {
//            if (dvtMacDinh.getMaDonViTinh().equalsIgnoreCase(tblCTHD.getValueAt(i, 7).toString())) {
//                int soLuong = parseIntFromTable(tblCTHD.getValueAt(i, 4));
//                soLuong += 1;
//                tblCTHD.setValueAt(soLuong, i, 4);
//                return null;
//            }
//        }
//
//        // Tạo thông tin chi tiết hóa đơn
//        String tenSP = sp.getTen();
//        double donGia = dvtMacDinh.getGiaBanTheoDonVi();
//        int soLuong = 1;
//        double giamGia = 0;
//        dsKM.sort((b, a) -> Double.compare(a.getPhanTram(), b.getPhanTram()));
//        for (common.dto.KhuyenMaiDTO km : dsKM) {
//            if (soLuong >= km.getSoLuongToiThieu() && soLuong <= km.getSoLuongToiDa()) {
//                giamGia = km.getPhanTram();
//                break;
//            }
//        }
//        double thanhTien = soLuong * donGia * (1 - giamGia / 100);
//        String maDVT = dvtMacDinh.getMaDonViTinh();
//
//        Object[] newRow = {0, tenSP, tenDVT, donGia, soLuong, giamGia, thanhTien, maDVT, maSP};
//        return newRow;
//    }
//
//    public Object[] thayDoiChiTietHoaDon(String maSP, int soLuong, String tenDVT) throws Exception {
//        // Lấy khuyến mãi đủ điều kiện
//        Response resKm = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_KHUYEN_MAI_BY_MA_SP, maSP));
//        java.util.List<common.dto.KhuyenMaiDTO> dskm = (resKm.isSuccess()) ? (java.util.List<common.dto.KhuyenMaiDTO>) resKm.getData() : new java.util.ArrayList<>();
//        dskm.sort((b, a) -> Double.compare(a.getPhanTram(), b.getPhanTram()));
//        double giamGia = 0;
//        for (common.dto.KhuyenMaiDTO km : dskm) {
//            if (soLuong >= km.getSoLuongToiThieu() && soLuong <= km.getSoLuongToiDa()) {
//                giamGia = km.getPhanTram();
//                break;
//            }
//        }
//
//        // Lấy số lượng trong kho
//        Response resLo = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_LO_BY_MA_SP, maSP));
//        java.util.List<common.dto.LoSanPhamDTO> dsLSP = (resLo.isSuccess()) ? (java.util.List<common.dto.LoSanPhamDTO>) resLo.getData() : new java.util.ArrayList<>();
//        int tongSoLuong = dsLSP.stream().mapToInt(common.dto.LoSanPhamDTO::getSoLuong).sum();
//
//        Response resDvt = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_DVT_BY_MA_SP, maSP));
//        java.util.List<common.dto.DonViTinhDTO> dsDVT = (resDvt.isSuccess()) ? (java.util.List<common.dto.DonViTinhDTO>) resDvt.getData() : new java.util.ArrayList<>();
//        dsDVT.sort((a, b) -> Integer.compare(a.getHeSoQuyDoi(), b.getHeSoQuyDoi()));
//
//        for (common.dto.DonViTinhDTO dvt : dsDVT) {
//            if (dvt.getTenDonViTinh().equals(tenDVT)) {
//                int heSoQuyDoi = dvt.getHeSoQuyDoi();
//                double donGia = dvt.getGiaBanTheoDonVi();
//                double thanhTien = soLuong * donGia * (1 - giamGia / 100);
//                String maDVT = dvt.getMaDonViTinh();
//                int tonTheoDonVi = tongSoLuong / heSoQuyDoi;
//                if (soLuong * heSoQuyDoi > tongSoLuong) {
//                    throw new Exception("Không đủ số lượng\nTổng số lượng còn lại: " + tonTheoDonVi + " " + tenDVT);
//                }
//                if (soLuong < 1) {
//                    throw new Exception("Số lượng phải lớn hơn bằng 1");
//                }
//                Object[] updatedInfo = {donGia, giamGia, thanhTien, maDVT};
//                return updatedInfo;
//            }
//        }
//        return null;
//    }
//
//    public common.dto.KhachHangDTO layThongTinKhachHang(String sdt) {
//        Response res = client.socket.SocketClient.getInstance().sendRequest(
//            new common.network.Request(common.network.CommandType.GET_KHACH_HANG_BY_SDT, sdt)
//        );
//        if (res.isSuccess() && res.getData() != null) {
//            return (common.dto.KhachHangDTO) res.getData();
//        }
//        return null;
//    }
//
//    public boolean kiemTraKeDon(String maSP) {
//        Response res = client.socket.SocketClient.getInstance().sendRequest(
//            new common.network.Request(common.network.CommandType.GET_SAN_PHAM_BY_MA, chuanHoaMaSP(maSP))
//        );
//        if (res.isSuccess() && res.getData() != null) {
//            common.dto.SanPhamDTO sp = (common.dto.SanPhamDTO) res.getData();
//            return "THUOC_KE_DON".equals(sp.getLoaiSanPham());
//        }
//        return false;
//    }
//
//    public boolean thanhToan(JTable tblCTHD, String maKH, boolean chuyenKhoan, double tongTien, double tienKhachDua, double tienThua) throws Exception {
//        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn thanh toán không?", "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//        if (confirm != JOptionPane.YES_OPTION) return false;
//
//        common.dto.TaiKhoanDTO tk = GiaoDienChinhGUI.getTkDTO(); // Giả sử có phương thức getTkDTO
//        if (tk == null) throw new Exception("Vui lòng nhấn vào ca làm trước khi thanh toán");
//        if (tblCTHD.getRowCount() == 0) throw new Exception("Vui lòng thêm sản phẩm cần thanh toán");
//        if (tienThua != (tienKhachDua - tongTien)) throw new Exception("Tiền thừa phải bằng tiền khách đưa trừ tổng tiền");
//
//        for (int i = 0; i < tblCTHD.getRowCount(); i++) {
//            String maSP = tblCTHD.getValueAt(i, 8).toString();
//            if (kiemTraKeDon(maSP) && maKH.equalsIgnoreCase("KH-00000")) {
//                throw new Exception("Vui lòng lưu thông tin khách hàng trước khi thanh toán vì có thuốc kê đơn");
//            }
//        }
//        if (tongTien > tienKhachDua) throw new Exception("Tiền khách đưa phải lớn hơn hoặc bằng tổng tiền");
//
//        // 1. Kiểm tra tồn kho toàn cục
//        Map<String, Integer> tongYeuCauTheoSP = new HashMap<>();
//        for (int i = 0; i < tblCTHD.getRowCount(); i++) {
//            String maSP = tblCTHD.getValueAt(i, 8).toString();
//            String maDVT = tblCTHD.getValueAt(i, 7).toString();
//
//            Response resDvt = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_DVT_BY_MA, maDVT));
//            common.dto.DonViTinhDTO dvt = (resDvt.isSuccess()) ? (common.dto.DonViTinhDTO) resDvt.getData() : null;
//            if (dvt == null) throw new Exception("Lỗi khi lấy đơn vị tính " + maDVT);
//
//            int heSoQuyDoi = dvt.getHeSoQuyDoi();
//            int soLuongYeuCau = parseIntFromTable(tblCTHD.getValueAt(i, 4)) * heSoQuyDoi;
//            tongYeuCauTheoSP.merge(maSP, soLuongYeuCau, Integer::sum);
//        }
//
//        for (Map.Entry<String, Integer> entry : tongYeuCauTheoSP.entrySet()) {
//            String maSP = entry.getKey();
//            int tongYeuCau = entry.getValue();
//
//            Response resLo = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_LO_BY_MA_SP, maSP));
//            java.util.List<common.dto.LoSanPhamDTO> dsLSP = (resLo.isSuccess()) ? (java.util.List<common.dto.LoSanPhamDTO>) resLo.getData() : new java.util.ArrayList<>();
//            int tongTon = dsLSP.stream().mapToInt(common.dto.LoSanPhamDTO::getSoLuong).sum();
//
//            if (tongYeuCau > tongTon) {
//                throw new Exception("Không đủ hàng cho sản phẩm " + maSP + " (Yêu cầu: " + tongYeuCau + ", Tồn: " + tongTon + ")");
//            }
//        }
//
//        // 2. Tạo mã hóa đơn
//        Response resHdMoi = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_HOA_DON_MOI_NHAT_TRONG_NGAY, null));
//        common.dto.HoaDonDTO hdMoiNhat = (resHdMoi.isSuccess()) ? (common.dto.HoaDonDTO) resHdMoi.getData() : null;
//        LocalDateTime now = LocalDateTime.now();
//        String maHDMoi;
//        if (hdMoiNhat == null) {
//            maHDMoi = taoMaHoaDonMoi(now.toLocalDate(), 1);
//        } else {
//            maHDMoi = taoMaHoaDonMoi(now.toLocalDate(), laySoThuTu(hdMoiNhat.getMaHoaDon()) + 1);
//        }
//
//        // 3. Chèn Hóa đơn
//        common.dto.HoaDonDTO hdDto = common.dto.HoaDonDTO.builder()
//                .maHoaDon(maHDMoi)
//                .maNV(tk.getMaNV())
//                .maKH(maKH)
//                .ngayLapHoaDon(now)
//                .chuyenKhoan(chuyenKhoan)
//                .tongTien(tongTien)
//                .build();
//        Response resInsHd = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.ADD_HOA_DON, hdDto));
//        if (!resInsHd.isSuccess()) throw new Exception("Tạo hóa đơn thất bại: " + resInsHd.getMessage());
//
//        // 4. Chèn Chi tiết hóa đơn và xử lý kho
//        java.util.ArrayList<common.dto.ChiTietHoaDonDTO> dsCTHD_DTO = new java.util.ArrayList<>();
//        DefaultTableModel model = (DefaultTableModel) tblCTHD.getModel();
//        for (int i = 0; i < model.getRowCount(); i++) {
//            String maDVT = String.valueOf(model.getValueAt(i, 7));
//            Response resDvtInfo = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_DVT_BY_MA, maDVT));
//            common.dto.DonViTinhDTO dvtInfo = (resDvtInfo.isSuccess()) ? (common.dto.DonViTinhDTO) resDvtInfo.getData() : null;
//            int heSoQuyDoi = (dvtInfo != null) ? dvtInfo.getHeSoQuyDoi() : 1;
//
//            int soLuong = parseIntFromTable(model.getValueAt(i, 4));
//            double donGia = parseDoubleFromTable(model.getValueAt(i, 3));
//            double giamGia = parseDoubleFromTable(model.getValueAt(i, 5));
//            double thanhTien = parseDoubleFromTable(model.getValueAt(i, 6));
//            String maSP = (String) model.getValueAt(i, 8);
//
//            Response resCtMoi = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_CTHD_MOI_NHAT_TRONG_NGAY, null));
//            common.dto.ChiTietHoaDonDTO cthdMoiNhat = (resCtMoi.isSuccess()) ? (common.dto.ChiTietHoaDonDTO) resCtMoi.getData() : null;
//            String maCTHDMoi;
//            if (cthdMoiNhat == null) {
//                maCTHDMoi = taoMaChiTietHoaDonMoi(now.toLocalDate(), 1);
//            } else {
//                maCTHDMoi = taoMaChiTietHoaDonMoi(now.toLocalDate(), laySoThuTu(cthdMoiNhat.getMaChiTietHoaDon()) + 1);
//            }
//
//            common.dto.ChiTietHoaDonDTO cthdDto = common.dto.ChiTietHoaDonDTO.builder()
//                    .maChiTietHoaDon(maCTHDMoi)
//                    .maHoaDon(maHDMoi)
//                    .maDonViTinh(maDVT)
//                    .soLuong(soLuong)
//                    .donGia(donGia)
//                    .giamGia(giamGia)
//                    .thanhTien(thanhTien)
//                    .build();
//
//            Response resInsCt = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.ADD_CHI_TIET_HOA_DON, cthdDto));
//            if (!resInsCt.isSuccess()) throw new Exception("Tạo chi tiết hóa đơn thất bại cho " + maSP);
//            dsCTHD_DTO.add(cthdDto);
//
//            // 5. Trừ tồn kho và tạo chi tiết xuất lô
//            int soLuongXuat = soLuong * heSoQuyDoi;
//            Response resLoList = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_LO_BY_MA_SP, maSP));
//            java.util.List<common.dto.LoSanPhamDTO> dsLSP = (resLoList.isSuccess()) ? (java.util.List<common.dto.LoSanPhamDTO>) resLoList.getData() : new java.util.ArrayList<>();
//
//            for (common.dto.LoSanPhamDTO lsp : dsLSP) {
//                if (soLuongXuat <= 0) break;
//                int soLuongTon = lsp.getSoLuong();
//                int xuatThucTe = Math.min(soLuongTon, soLuongXuat);
//
//                // Trừ số lượng lô
//                client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.TRU_SO_LUONG_LO, new Object[]{lsp.getMaLoSanPham(), xuatThucTe}));
//
//                // Chèn ChiTietXuatLo
//                common.dto.ChiTietXuatLoDTO ctxl = common.dto.ChiTietXuatLoDTO.builder()
//                        .maLoSanPham(lsp.getMaLoSanPham())
//                        .maChiTietHoaDon(maCTHDMoi)
//                        .soLuong(xuatThucTe)
//                        .build();
//                client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.ADD_CHI_TIET_XUAT_LO, ctxl));
//
//                soLuongXuat -= xuatThucTe;
//            }
//            if (soLuongXuat > 0) throw new Exception("Lỗi logic tồn kho cho sản phẩm " + maSP);
//        }
//
//        // 6. Cập nhật điểm tích lũy
//        if (tongTien >= 1000 && !"KH-99999".equals(maKH)) {
//            int diemTichLuy = (int) Math.floor(tongTien / 1000);
//            client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.UPDATE_DIEM_TICH_LUY, new Object[]{diemTichLuy, maKH}));
//        }
//
//        // 7. Hiển thị hóa đơn
//        String noiDung = taoNoiDungHoaDon(hdDto, dsCTHD_DTO, tienKhachDua, tienThua);
//        JDialog dialog = new JDialog();
//        dialog.setTitle(maHDMoi);
//        dialog.setSize(1000, 600);
//        dialog.setLocationRelativeTo(null);
//        dialog.setModal(true);
//        JTextArea area = new JTextArea(noiDung);
//        area.setEditable(false);
//        area.setFont(new Font("Courier New", Font.PLAIN, 13));
//        dialog.add(new JScrollPane(area));
//        dialog.setVisible(true);
//
//        return true;
//    }
//
//    private String taoMaHoaDonMoi(LocalDate ngay, int soThuTu) {
//        return String.format("HD-%s-%04d",
//            ngay.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy")),
//            soThuTu);
//    }
//
//    private String taoMaChiTietHoaDonMoi(LocalDate ngay, int soThuTu) {
//        return String.format("CTHD-%s-%04d",
//            ngay.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy")),
//            soThuTu);
//    }
//
//    private int laySoThuTu(String ma) {
//        try {
//            String[] parts = ma.split("-");
//            return Integer.parseInt(parts[2]);
//        } catch (NumberFormatException e) {
//            return 0;
//        }
//    }
//
//    public static String taoNoiDungHoaDon(common.dto.HoaDonDTO hd, java.util.ArrayList<common.dto.ChiTietHoaDonDTO> dsCTHD, double tienKhachDua, double tienThua) {
//        int WIDTH = 120;
//        String LINE = "=".repeat(WIDTH);
//        String SEPARATOR = "-".repeat(WIDTH);
//
//        StringBuilder sb = new StringBuilder();
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//        DecimalFormat df = new DecimalFormat("#,###");
//
//        sb.append(LINE).append("\n");
//        sb.append(center("HÓA ĐƠN BÁN HÀNG", WIDTH)).append("\n");
//        sb.append(LINE).append("\n");
//
//        sb.append(String.format("Mã hóa đơn : %s\n", hd.getMaHoaDon()));
//        sb.append(String.format("Ngày lập   : %s\n", hd.getNgayLapHoaDon().format(fmt)));
//        sb.append(String.format("Nhân viên  : %s\n", hd.getMaNV()));
//        sb.append(String.format("Khách hàng : %s\n", hd.getMaKH()));
//        sb.append(String.format("Hình thức  : %s\n", hd.isChuyenKhoan() ? "Chuyển khoản" : "Tiền mặt"));
//
//        sb.append("\n").append(SEPARATOR).append("\n");
//
//        sb.append(String.format("%-4s %-55s %-8s %-15s %-12s %-10s %-12s\n",
//                "STT", "Sản phẩm", "SL", "ĐVT", "Đơn giá", "Giảm giá", "Thành tiền"));
//
//        sb.append(SEPARATOR).append("\n");
//
//        int stt = 1;
//        double tongTien = 0;
//
//        for (common.dto.ChiTietHoaDonDTO cthd : dsCTHD) {
//            // Lấy thông tin hiển thị (Tên SP, Tên DVT)
//            Response resDvt = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_DVT_BY_MA, cthd.getMaDonViTinh()));
//            common.dto.DonViTinhDTO dvt = (resDvt.isSuccess()) ? (common.dto.DonViTinhDTO) resDvt.getData() : null;
//
//            String tenSP = "N/A";
//            String tenDVT = (dvt != null) ? dvt.getTenDonViTinh() : "N/A";
//
//            if (dvt != null) {
//                Response resSp = client.socket.SocketClient.getInstance().sendRequest(new common.network.Request(common.network.CommandType.GET_SAN_PHAM_BY_MA, dvt.getMaSP()));
//                common.dto.SanPhamDTO sp = (resSp.isSuccess()) ? (common.dto.SanPhamDTO) resSp.getData() : null;
//                if (sp != null) tenSP = sp.getTen();
//            }
//
//            String giamStr = String.format("%.0f%%", cthd.getGiamGia());
//            double thanhTien = cthd.getThanhTien();
//            tongTien += thanhTien;
//
//            if (tenSP.length() > 55) tenSP = tenSP.substring(0, 55);
//
//            sb.append(String.format(
//                    "%-4d %-55s %-8d %-15s %-12s %-10s %-12s\n",
//                    stt++, tenSP, cthd.getSoLuong(), tenDVT,
//                    df.format(cthd.getDonGia()), giamStr, df.format(thanhTien)
//            ));
//        }
//
//        sb.append(SEPARATOR).append("\n");
//        String tongCongStr = "TỔNG CỘNG: " + df.format(tongTien) + " VND";
//        sb.append(alignRight(tongCongStr, WIDTH)).append("\n");
//        if (hd.isChuyenKhoan()) {
//            String tienKhachDuaStr = "KHÁCH CHUYỂN KHOẢN: " + df.format(tongTien) + " VND";
//            sb.append(alignRight(tienKhachDuaStr, WIDTH)).append("\n");
//            sb.append(LINE).append("\n");
//        } else {
//            String tienKhachDuaStr = "KHÁCH ĐƯA: " + df.format(tienKhachDua) + " VND";
//            sb.append(alignRight(tienKhachDuaStr, WIDTH)).append("\n");
//            String tienThuaStr = "TIỀN THỪA: " + df.format(tienThua) + " VND";
//            sb.append(alignRight(tienThuaStr, WIDTH)).append("\n");
//            sb.append(LINE).append("\n");
//        }
//        sb.append(center("CẢM ƠN QUÝ KHÁCH, HẸN GẶP LẠI!", WIDTH)).append("\n");
//        sb.append(LINE).append("\n");
//        return sb.toString();
//    }
//
//
//    private static String center(String text, int width) {
//        int padSize = (width - text.length()) / 2;
//        return " ".repeat(Math.max(0, padSize)) + text;
//    }
//
//    private static String alignRight(String text, int width) {
//        int padding = width - text.length();
//        if (padding < 0) padding = 0;
//        return " ".repeat(padding) + text;
//    }
//}
