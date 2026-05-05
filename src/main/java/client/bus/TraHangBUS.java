package client.bus;

import client.socket.SocketClient;
import common.dto.*;
import common.network.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TraHangBUS {

    public HoaDonDTO timHoaDon(String maHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_HOA_DON_BY_MA, maHD));
        return (res.isSuccess()) ? (HoaDonDTO) res.getData() : null;
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonByMaHoaDon(String maHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_BY_MA_HD, maHD));
        return (res.isSuccess()) ? (List<ChiTietHoaDonDTO>) res.getData() : new ArrayList<>();
    }

    public HoaDonDTO kiemTraDieuKienTraHang(String maHD) throws Exception {
        HoaDonDTO hd = timHoaDon(maHD);
        if (hd == null) throw new Exception("Không tìm thấy hóa đơn này!");
        
        long daysBetween = ChronoUnit.DAYS.between(hd.getNgayLapHoaDon().toLocalDate(), LocalDate.now());
        if (daysBetween > 30) {
            throw new Exception("Hóa đơn đã quá hạn đổi trả (30 ngày)!");
        }
        
        return hd;
    }

    public int getSoPTH(String maHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SO_LUONG_PHIEU_TRA_BY_MA_HD, maHD));
        return (res.isSuccess() && res.getData() != null) ? (int) res.getData() : 0;
    }

    public double getTongTienCacPTH(String maHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_TONG_TIEN_TRA_BY_MA_HD, maHD));
        return (res.isSuccess() && res.getData() != null) ? (double) res.getData() : 0.0;
    }

    public String layPhanTramHoanTra(String lyDoEnum, boolean isNguyenVen) {
        if ("HANG_LOI_DO_NHA_SAN_XUAT".equals(lyDoEnum)) return "100%";
        if ("DI_UNG_MAN_CAM".equals(lyDoEnum)) return "100%";
        if ("NHU_CAU_KHACH_HANG".equals(lyDoEnum)) {
            return isNguyenVen ? "90%" : "50%";
        }
        return "0%";
    }

    public double tinhTienHoanTraItem(double thanhTienGoc, String lyDoEnum, boolean isNguyenVen) {
        String phanTramStr = layPhanTramHoanTra(lyDoEnum, isNguyenVen).replace("%", "");
        double phanTram = Double.parseDouble(phanTramStr);
        return thanhTienGoc * (phanTram / 100.0);
    }

    public static String phatSinhMaPhieuTraHang() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GENERATE_MA_PHIEU_TRA_HANG, null));
        return (res.isSuccess()) ? (String) res.getData() : "PTH-ERROR";
    }

    public static boolean xuLyTraHang(PhieuTraHangDTO phieuTra, List<ChiTietPhieuTraHangDTO> dsChiTiet) throws Exception {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_PHIEU_TRA_HANG_FULL, new Object[]{phieuTra, dsChiTiet}));
        if (!res.isSuccess()) throw new Exception(res.getMessage());
        return true;
    }

    public List<ChiTietHoaDonDTO> getChiTietHoaDonDaTruPTHTheoMaHD(String maHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_CHUA_TRA_BY_MA_HD, maHD));
        return (res.isSuccess()) ? (List<ChiTietHoaDonDTO>) res.getData() : new ArrayList<>();
    }

    public ChiTietHoaDonDTO getChiTietHoaDonByMa(String maCTHD) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CTHD_BY_MA, maCTHD));
        return (res.isSuccess()) ? (ChiTietHoaDonDTO) res.getData() : null;
    }

    public ChiTietHoaDonDTO getChiTietHoaDonDaTungTraRoiByMa(String maCTHD) {
        // Thực tế server có thể xử lý việc trừ đi số lượng đã trả
        return getChiTietHoaDonByMa(maCTHD);
    }

    public static double tinhThanhTienGoc(int soLuong, double donGia, double khuyenMai) {
        return soLuong * donGia * (1 - khuyenMai / 100.0);
    }
}
