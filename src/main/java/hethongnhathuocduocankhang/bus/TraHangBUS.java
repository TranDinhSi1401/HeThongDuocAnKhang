package hethongnhathuocduocankhang.bus;

import client.socket.SocketClient;
import common.dto.*;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TraHangBUS {

    public HoaDonDTO kiemTraDieuKienTraHang(String maHoaDon) throws Exception {
        if (maHoaDon == null || maHoaDon.trim().isEmpty()) {
            throw new Exception("Vui lòng nhập mã hóa đơn!");
        }

        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_MA, maHoaDon));
        if (!res.isSuccess() || res.getData() == null) {
            throw new Exception("Không tìm thấy hóa đơn với mã: " + maHoaDon);
        }
        HoaDonDTO hoaDon = (HoaDonDTO) res.getData();

        LocalDateTime ngayLapHoaDon = hoaDon.getNgayLapHoaDon();
        long ngayDaTroiQua = ChronoUnit.DAYS.between(ngayLapHoaDon, LocalDateTime.now());

        if (ngayDaTroiQua > 30) {
            throw new Exception("Hóa đơn đã lập " + ngayDaTroiQua + " ngày (quá 30 ngày), không thể trả hàng!");
        }

        return hoaDon;
    }

    public static String layPhanTramHoanTra(String lyDo, boolean isNguyenVen) {
        if (isNguyenVen) {
            return "100%";
        }
        if ("HANG_LOI_DO_NHA_SAN_XUAT".equals(lyDo)) return "100%";
        if ("DI_UNG_MAN_CAM".equals(lyDo)) return "70%";
        return "Miễn trả hàng";
    }

    public static double tinhTienHoanTraItem(double thanhTienGoc, String lyDo, boolean isNguyenVen) {
        if (isNguyenVen) {
            return thanhTienGoc;
        }
        if ("HANG_LOI_DO_NHA_SAN_XUAT".equals(lyDo)) {
            return thanhTienGoc;
        } else if ("DI_UNG_MAN_CAM".equals(lyDo)) {
            return thanhTienGoc * 0.7;
        } else {
            return 0;
        }
    }

    public static String phatSinhMaPhieuTraHang() {
        int ngay = LocalDate.now().getDayOfMonth();
        int thang = LocalDate.now().getMonthValue();
        int nam = LocalDate.now().getYear() % 100; 
        String ngayThangNam = String.format("%02d%02d%02d", ngay, thang, nam);
        
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SO_PTH_CUOI, null));
        int soPhieuHomNay = (res.isSuccess() && res.getData() != null) ? (int) res.getData() : 0;
        return String.format("PTH-%s-%04d", ngayThangNam, soPhieuHomNay + 1);
    }

    public static void xuLyTraHang(PhieuTraHangDTO pth, List<ChiTietPhieuTraHangDTO> listChiTiet) {
        // 1. Lưu phiếu trả hàng
        SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_PHIEU_TRA_HANG, pth));

        // 2. Lưu chi tiết phiếu trả & Cập nhật kho
        for (ChiTietPhieuTraHangDTO ctpth : listChiTiet) {
            SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_CHI_TIET_PHIEU_TRA_HANG, ctpth));
            
            // --- LOGIC CỘNG KHO ---
            String tinhTrang = ctpth.getTinhTrangSanPham();
            String truongHop = ctpth.getTruongHopDoiTra();
            
            boolean hangTraVeKho = "HANG_NGUYEN_VEN".equals(tinhTrang) 
                    && ("NHU_CAU_KHACH_HANG".equals(truongHop) || "DI_UNG_MAN_CAM".equals(truongHop));

            if (hangTraVeKho) {
                Response resCt = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_BY_MA, ctpth.getMaChiTietHoaDon()));
                ChiTietHoaDonDTO cthd = (resCt.isSuccess() && resCt.getData() != null) ? (ChiTietHoaDonDTO) resCt.getData() : null;
                
                if (cthd != null) {
                    Response resDvt = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_DVT_BY_MA_SP, cthd.getMaSP()));
                    List<DonViTinhDTO> dvts = (resDvt.isSuccess() && resDvt.getData() != null) ? (List<DonViTinhDTO>) resDvt.getData() : null;
                    int heSoQuyDoi = 1;
                    if (dvts != null) {
                        for (DonViTinhDTO d : dvts) {
                            if (d.getTenDonVi().equals(cthd.getTenDonVi())) {
                                heSoQuyDoi = d.getHeSoQuyDoi();
                                break;
                            }
                        }
                    }

                    int soLuongCanTra = ctpth.getSoLuong() * heSoQuyDoi;
                    // Logic cộng kho này nên được server xử lý khi nhận ADD_CHI_TIET_PHIEU_TRA_HANG
                }
            }
        }

        // 3. Trừ điểm tích lũy khách hàng
        Response resHd = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_MA, pth.getMaHoaDon()));
        HoaDonDTO hd = (resHd.isSuccess() && resHd.getData() != null) ? (HoaDonDTO) resHd.getData() : null;
        
        if (hd != null && hd.getMaKhachHang() != null && !hd.getMaKhachHang().equalsIgnoreCase("KH-00000")) {
            if (pth.getTongTienHoaTra() >= 1000) {
                int diemTru = (int) (pth.getTongTienHoaTra() / 1000);
                if (diemTru > 0) {
                    SocketClient.getInstance().sendRequest(new Request(CommandType.TRU_DIEM_TICH_LUY, new Object[]{hd.getMaKhachHang(), diemTru}));
                }
            }
        }
    }

    public static double tinhThanhTienGoc(int soLuong, double donGia, double phanTramGiamGia) {
        return (soLuong * donGia) - (soLuong * donGia * (phanTramGiamGia / 100));
    }

    public int getSoPTH(String maHoaDon) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SO_PTH, maHoaDon));
        if (res.isSuccess() && res.getData() != null) {
            return (int) res.getData();
        }
        return 0;
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonByMaHoaDon(String maHoaDon) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_BY_MA_HD, maHoaDon));
        if (res.isSuccess() && res.getData() != null) {
            return (List<ChiTietHoaDonDTO>) res.getData();
        }
        return null;
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonDaTruPTHTheoMaHD(String maHoaDon) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_DA_TRU_PTH_BY_MA_HD, maHoaDon));
        if (res.isSuccess() && res.getData() != null) {
            return (List<ChiTietHoaDonDTO>) res.getData();
        }
        return null;
    }

    public double getTongTienCacPTH(String maHoaDon) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_TONG_TIEN_CAC_PTH, maHoaDon));
        if (res.isSuccess() && res.getData() != null) {
            return (double) res.getData();
        }
        return 0.0;
    }

    public ChiTietHoaDonDTO getChiTietHoaDonByMa(String maCTHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_BY_MA, maCTHD));
        if (res.isSuccess() && res.getData() != null) {
            return (ChiTietHoaDonDTO) res.getData();
        }
        return null;
    }

    public ChiTietHoaDonDTO getChiTietHoaDonDaTungTraRoiByMa(String maCTHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_DA_TUNG_TRA_ROI_BY_MA, maCTHD));
        if (res.isSuccess() && res.getData() != null) {
            return (ChiTietHoaDonDTO) res.getData();
        }
        return null;
    }
}