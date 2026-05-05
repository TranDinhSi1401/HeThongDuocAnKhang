package server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;

import common.dto.CaLamDTO;
import common.dto.ChiTietHoaDonDTO;
import common.dto.ChiTietPhieuNhapDTO;
import common.dto.ChiTietPhieuTraHangDTO;
import common.dto.ChiTietXuatLoDTO;
import common.dto.DonViTinhDTO;
import common.dto.HoaDonDTO;
import common.dto.KhachHangDTO;
import common.dto.KhuyenMaiDTO;
import common.dto.KhuyenMaiSanPhamDTO;
import common.dto.LichSuCaLamDTO;
import common.dto.LichSuLoDTO;
import common.dto.LoSanPhamDTO;
import common.dto.MaVachSanPhamDTO;
import common.dto.NhaCungCapDTO;
import common.dto.NhanVienDTO;
import common.dto.PhieuNhapDTO;
import common.dto.PhieuTraHangDTO;
import common.dto.SanPhamCungCapDTO;
import common.dto.SanPhamDTO;
import common.dto.TaiKhoanDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.service.CaLamService;
import server.service.HoaDonService;
import server.service.KhachHangService;
import server.service.KhuyenMaiService;
import server.service.LoSanPhamService;
import server.service.NhaCungCapService;
import server.service.NhanVienService;
import server.service.PhieuService;
import server.service.SanPhamService;

/**
 * Xử lý một kết nối Client trong Thread riêng.
 * Đọc Request → gọi Service tương ứng → gửi trả Response.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final SanPhamService sanPhamService;
    private final KhachHangService khachHangService;
    private final NhanVienService nhanVienService;
    private final HoaDonService hoaDonService;
    private final LoSanPhamService loSanPhamService;
    private final NhaCungCapService nhaCungCapService;
    private final KhuyenMaiService khuyenMaiService;
    private final PhieuService phieuService;
    private final CaLamService caLamService;

    public ClientHandler(Socket socket,
                         SanPhamService sanPhamService,
                         KhachHangService khachHangService,
                         NhanVienService nhanVienService,
                         HoaDonService hoaDonService,
                         LoSanPhamService loSanPhamService,
                         NhaCungCapService nhaCungCapService,
                         KhuyenMaiService khuyenMaiService,
                         PhieuService phieuService,
                         CaLamService caLamService) {
        this.socket           = socket;
        this.sanPhamService   = sanPhamService;
        this.khachHangService = khachHangService;
        this.nhanVienService  = nhanVienService;
        this.hoaDonService    = hoaDonService;
        this.loSanPhamService = loSanPhamService;
        this.nhaCungCapService= nhaCungCapService;
        this.khuyenMaiService = khuyenMaiService;
        this.phieuService     = phieuService;
        this.caLamService     = caLamService;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream  in  = new ObjectInputStream(socket.getInputStream())) {

            Request  req = (Request) in.readObject();
            Response res = handle(req);
            out.writeObject(res);
            out.flush();

        } catch (Exception e) {
            System.err.println("[ClientHandler] Lỗi xử lý client: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    @SuppressWarnings("unchecked")
    private Response handle(Request req) {
        CommandType cmd  = req.getCommand();
        Object      data = req.getData();
        try {
            return switch (cmd) {

                // ===== AUTH =====
                case LOGIN -> {
                    Object[] arr = (Object[]) data;
                    TaiKhoanDTO dto = nhanVienService.login((String) arr[0], (String) arr[1]);
                    if (dto == null)
                        yield Response.fail("Sai tài khoản hoặc mật khẩu!");
                    if (dto.isBiKhoa())
                        yield Response.fail("Tài khoản đã bị khóa!");
                    yield Response.ok("Đăng nhập thành công", dto);
                }

                // ===== SAN PHAM =====
                case GET_ALL_SAN_PHAM             -> Response.ok(sanPhamService.getAllSanPham());
                case GET_SAN_PHAM_BY_MA           -> Response.ok(sanPhamService.timSPTheoMa((String) data));
                case GET_SAN_PHAM_BY_TEN          -> Response.ok(sanPhamService.timSPTheoTen((String) data));
                case GET_SAN_PHAM_BY_LOAI         -> Response.ok(sanPhamService.timSPTheoLoai((String) data));
                case GET_SAN_PHAM_BY_MA_NCC       -> Response.ok(sanPhamService.timSPTheoMaNCC((String) data));
                case GET_SAN_PHAM_SAP_HET_HANG    -> Response.ok(sanPhamService.getSPSapHetHang());
                case GET_SAN_PHAM_DA_HET_HANG     -> Response.ok(sanPhamService.getSPDaHetHang());
                case GET_MA_SP_BY_MA_VACH         -> Response.ok(sanPhamService.getMaSpTheoMaVach((String) data));
                case GET_MA_SP_CUOI               -> Response.ok(sanPhamService.getMaSPCuoiCung());
                case ADD_SAN_PHAM                 -> Response.ok("Thêm SP thành công", sanPhamService.addSanPham((SanPhamDTO) data));
                case XOA_SAN_PHAM                 -> Response.ok(sanPhamService.xoaSanPham((String) data));
                case GET_SP_BAN_CHAY_THANG        -> Response.ok(sanPhamService.getSPBanChayTrongThang((LocalDate) data));
                case GET_SP_BAN_CHAY_NAM          -> Response.ok(sanPhamService.getSPBanChayTrongNam((LocalDate) data));
                case GET_CHI_TIET_SP -> Response.ok(sanPhamService.getChiTietSpBangMaSp((String) data));
                // SUA_SAN_PHAM: data = Object[]{maSP, SanPhamDTO}
                case SUA_SAN_PHAM -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(sanPhamService.suaSanPham((String) arr[0], (SanPhamDTO) arr[1]));
                }

                // ===== DON VI TINH =====
                case GET_DVT_BY_MA_SP          -> Response.ok(sanPhamService.getDonViTinhTheoMaSP((String) data));
                case GET_DVT_CO_BAN_BY_MA_SP   -> Response.ok(sanPhamService.getDVTCoBanTheoMaSP((String) data));
                case GET_MA_DVT_CUOI           -> Response.ok(sanPhamService.getMaDVTCuoiCung());
                case ADD_DON_VI_TINH           -> Response.ok(sanPhamService.addDonViTinh((DonViTinhDTO) data));
                case XOA_DON_VI_TINH           -> Response.ok(sanPhamService.xoaDonViTinh((String) data));
                case XOA_DVT_BY_MA_SP          -> Response.ok(sanPhamService.xoaDVTTheoMaSP((String) data));
                case SUA_DON_VI_TINH -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(sanPhamService.suaDonViTinh((String) arr[0], (DonViTinhDTO) arr[1]));
                }

                // ===== MA VACH =====
                case ADD_MA_VACH    -> Response.ok(sanPhamService.addMaVach((MaVachSanPhamDTO) data));
                case DELETE_MA_VACH -> Response.ok(sanPhamService.deleteMaVach((String) data));

                // ===== KHACH HANG =====
                case GET_ALL_KHACH_HANG      -> Response.ok(khachHangService.getAllKhachHang());
                case GET_KHACH_HANG_BY_SDT   -> Response.ok(khachHangService.getKhachHangTheoSdt((String) data));
                case GET_KHACH_HANG_BY_MA    -> Response.ok(khachHangService.timKHTheoMa((String) data));
                case GET_KHACH_HANG_BY_TEN   -> Response.ok(khachHangService.timKHTheoTen((String) data));
                case GET_MA_KH_CUOI          -> Response.ok(khachHangService.getMaKHCuoiCung());
                case ADD_KHACH_HANG          -> Response.ok(khachHangService.addKhachHang((KhachHangDTO) data));
                case XOA_KHACH_HANG          -> Response.ok(khachHangService.xoaKhachHang((String) data));
                case SUA_KHACH_HANG -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(khachHangService.suaKhachHang((String) arr[0], (KhachHangDTO) arr[1]));
                }
                case UPDATE_DIEM_TICH_LUY -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(khachHangService.updateDiemTichLuy((Integer) arr[0], (String) arr[1]));
                }
                case TRU_DIEM_TICH_LUY -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(khachHangService.truDiemTichLuy((Integer) arr[0], (String) arr[1]));
                }

                // ===== NHAN VIEN =====
                case GET_ALL_NHAN_VIEN        -> Response.ok(nhanVienService.getAllNhanVien());
                case GET_NHAN_VIEN_BY_MA      -> Response.ok(nhanVienService.timNVTheoMa((String) data));
                case GET_NHAN_VIEN_BY_TEN     -> Response.ok(nhanVienService.timNVTheoTen((String) data));
                case GET_NHAN_VIEN_BY_SDT     -> Response.ok(nhanVienService.timNVTheoSDT((String) data));
                case GET_NHAN_VIEN_BY_CCCD    -> Response.ok(nhanVienService.timNVTheoCCCD((String) data));
                case GET_NHAN_VIEN_BY_TRANG_THAI -> Response.ok(nhanVienService.timNVTheoTrangThai((Boolean) data));
                case GET_MA_NV_CUOI           -> Response.ok(nhanVienService.getMaNVCuoiCung());
                case ADD_NHAN_VIEN            -> Response.ok(nhanVienService.addNhanVien((NhanVienDTO) data));
                case XOA_NHAN_VIEN            -> Response.ok(nhanVienService.xoaNhanVien((String) data));
                case SUA_NHAN_VIEN -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(nhanVienService.suaNhanVien((String) arr[0], (NhanVienDTO) arr[1]));
                }

                // ===== TAI KHOAN =====
                case QUEN_MAT_KHAU -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(nhanVienService.updateMatKhau((String) arr[0], (String) arr[1], (String) arr[2]));
                }
                case GET_TAI_KHOAN_BY_MA_NV -> Response.ok(nhanVienService.getTaiKhoanTheoMaNV((String) data));
                case GET_TAI_KHOAN_BY_EMAIL -> Response.ok(nhanVienService.getTaiKhoanTheoEmail((String) data));
                case ADD_TAI_KHOAN          -> Response.ok(nhanVienService.addTaiKhoan((TaiKhoanDTO) data));
                case CAP_NHAT_TAI_KHOAN     -> Response.ok(nhanVienService.capNhatTaiKhoan((TaiKhoanDTO) data));
                case XOA_TAI_KHOAN          -> Response.ok(nhanVienService.xoaTaiKhoan((String) data));
                case KIEM_TRA_EMAIL_TON_TAI  -> Response.ok(nhanVienService.kiemTraEmailTonTai((String) data));
                case UPDATE_MAT_KHAU -> {
                    // arr[0]=maNV, arr[1]=matKhauCuTho, arr[2]=matKhauMoiTho
                    Object[] arr = (Object[]) data;
                    yield Response.ok(nhanVienService.doiMatKhau((String) arr[0], (String) arr[1], (String) arr[2]));
                }
                case KIEM_TRA_EMAIL_THUOC_TAI_KHOAN -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(nhanVienService.kiemTraEmailThuocTaiKhoan((String) arr[0], (String) arr[1]));
                }

                // ===== HOA DON =====
                case GET_HOA_DON_MOI_NHAT_TRONG_NGAY -> Response.ok(hoaDonService.getHoaDonMoiNhatTrongNgay());
                case GET_HOA_DON_BY_MA               -> Response.ok(hoaDonService.getHoaDonTheoMaHD((String) data));
                case GET_HOA_DON_BY_MA_NV            -> Response.ok(hoaDonService.timHDTheoMaNV((String) data));
                case GET_HOA_DON_BY_MA_KH            -> Response.ok(hoaDonService.timHDTheoMaKH((String) data));
                case GET_HOA_DON_BY_NGAY             -> Response.ok(hoaDonService.timHDTheoNgayLap((LocalDate) data));
                case GET_HOA_DON_BY_SDT_KH           -> Response.ok(hoaDonService.timHDTheoSDTKH((String) data));
                case GET_HOA_DON_BY_HINH_THUC        -> Response.ok(hoaDonService.timHDTheoHinhThuc((Boolean) data));
                case GET_SO_HD_CUOI_TRONG_NGAY       -> Response.ok(hoaDonService.getSoHDCuoiCungTrongNgay((String) data));
                case GET_SO_PTH                      -> Response.ok(hoaDonService.getSoPTH((String) data));
                case GET_TONG_TIEN_CAC_PTH           -> Response.ok(hoaDonService.getTongTienCacPTH((String) data));
                case ADD_HOA_DON                     -> Response.ok(hoaDonService.addHoaDon((HoaDonDTO) data));
                case GET_DOANH_THU_NGAY              -> Response.ok(hoaDonService.getDoanhThuTheoNgay((LocalDate) data));
                case GET_DOANH_THU_THANG             -> Response.ok(hoaDonService.getDoanhThuTheoThang((LocalDate) data));
                case GET_DOANH_THU_TUNG_NGAY         -> Response.ok(hoaDonService.getDoanhThuTungNgayTrongThang((LocalDate) data));
                case GET_DOANH_THU_TUNG_THANG        -> Response.ok(hoaDonService.getDoanhThuTungThangTrongNam((LocalDate) data));
                case GET_NAM_HOA_DON                 -> Response.ok(hoaDonService.getNamHoaDonCuNhatVaMoiNhat());
                case GET_HOA_DON_BY_KHOANG_NGAY -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(hoaDonService.timHDTheoKhoangNgay((LocalDate) arr[0], (LocalDate) arr[1]));
                }

                // ===== CHI TIET HOA DON =====
                case GET_CTHD_BY_MA_HD     -> Response.ok(hoaDonService.getCTHDByMaHD((String) data));
                case ADD_CHI_TIET_HOA_DON  -> Response.ok(hoaDonService.addChiTietHoaDon((ChiTietHoaDonDTO) data));
                case XOA_CTHD_BY_MA_HD     -> Response.ok(hoaDonService.xoaCTHDByMaHD((String) data));

                // ===== CHI TIET XUAT LO =====
                case ADD_CHI_TIET_XUAT_LO -> Response.ok(hoaDonService.addChiTietXuatLo((ChiTietXuatLoDTO) data));
                case GET_CTXL_BY_MA_CTHD  -> Response.ok(hoaDonService.getCTXLByMaCTHD((String) data));

                // ===== LO SAN PHAM =====
                case GET_ALL_LO_SAN_PHAM      -> Response.ok(loSanPhamService.getAllLoSanPham());
                case GET_ALL_LO_SAN_PHAM_KHONG_HUY -> Response.ok(loSanPhamService.getAllLoSanPhamKhongHuy());
                case GET_LO_SAN_PHAM_BY_MA    -> Response.ok(loSanPhamService.timLoSanPham((String) data));
                case GET_LO_BY_MA_SP       -> Response.ok(loSanPhamService.getLoSanPhamTheoMaSP((String) data));
                case GET_LO_BY_MA          -> Response.ok(loSanPhamService.timLoSanPham((String) data));
                case GET_LO_BY_MA_CTHD     -> Response.ok(loSanPhamService.getLoSanPhamTheoMaCTHD((String) data));
                case GET_LO_BY_MA_NCC      -> Response.ok(loSanPhamService.getLoSPTheoMaNhaCungCap((String) data));
                case GET_DS_LO_BY_MA_SP    -> Response.ok(loSanPhamService.dsLoTheoMaSanPham((String) data));
                case GET_DS_LO_DA_XUAT_BY_CTHD -> Response.ok(loSanPhamService.getDanhSachLoDaXuatTheoMaCTHD((String) data));
                case DEM_LO_THEO_TRANG_THAI     -> Response.ok(loSanPhamService.demLoTheoTrangThai());
                case ADD_LO_SAN_PHAM            -> Response.ok(loSanPhamService.addLoSanPham((LoSanPhamDTO) data));
                case HUY_LO_SAN_PHAM            -> Response.ok(loSanPhamService.huyLoSanPham((LoSanPhamDTO) data));
                case TRU_SO_LUONG_LO -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(loSanPhamService.truSoLuong((String) arr[0], (Integer) arr[1]));
                }
                case CONG_SO_LUONG_LO -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(loSanPhamService.congSoLuong((String) arr[0], (Integer) arr[1], (Integer) arr[2]));
                }
                case CAP_NHAT_SO_LUONG_LO -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(loSanPhamService.capNhatSoLuongLo((LoSanPhamDTO) arr[0], (Integer) arr[1]));
                }

                // ===== LICH SU LO =====
                case GET_LICH_SU_LO_BY_MA_LO -> Response.ok(loSanPhamService.getLichSuLoTheoMaLo((String) data));
                case GET_ALL_LICH_SU_LO      -> Response.ok(loSanPhamService.getAllLichSuLo());
                case ADD_LICH_SU_LO           -> Response.ok(loSanPhamService.addLichSuLo((LichSuLoDTO) data));

                // ===== NHA CUNG CAP =====
                case GET_ALL_NHA_CUNG_CAP -> Response.ok(nhaCungCapService.getAllNhaCungCap());
                case GET_NCC_BY_MA        -> Response.ok(nhaCungCapService.timNCCTheoMa((String) data));
                case GET_NCC_BY_TEN       -> Response.ok(nhaCungCapService.timNCCTheoTen((String) data));
                case GET_NCC_BY_SDT       -> Response.ok(nhaCungCapService.timNCCTheoSDT((String) data));
                case GET_NCC_BY_EMAIL     -> Response.ok(nhaCungCapService.timNCCTheoEmail((String) data));
                case GET_MA_NCC_CUOI      -> Response.ok(nhaCungCapService.getMaNCCCuoiCung());
                case ADD_NHA_CUNG_CAP     -> Response.ok(nhaCungCapService.addNhaCungCap((NhaCungCapDTO) data));
                case XOA_NHA_CUNG_CAP     -> Response.ok(nhaCungCapService.xoaNhaCungCap((String) data));
                case SUA_NHA_CUNG_CAP -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(nhaCungCapService.suaNhaCungCap((String) arr[0], (NhaCungCapDTO) arr[1]));
                }

                // ===== SAN PHAM CUNG CAP =====
                case GET_SPCC_BY_MA_SP         -> Response.ok(nhaCungCapService.getSPCCTheoMaSP((String) data));
                case ADD_SAN_PHAM_CUNG_CAP     -> Response.ok(nhaCungCapService.addSanPhamCungCap((SanPhamCungCapDTO) data));
                case DELETE_SAN_PHAM_CUNG_CAP  -> Response.ok(nhaCungCapService.deleteSanPhamCungCap((Long) data));

                // ===== KHUYEN MAI =====
                case GET_ALL_KHUYEN_MAI          -> Response.ok(khuyenMaiService.getAllKhuyenMai());
                case GET_KHUYEN_MAI_DANG_HOAT_DONG -> Response.ok(khuyenMaiService.getKhuyenMaiDangHoatDong());
                case GET_MA_KM_CUOI              -> Response.ok(khuyenMaiService.getMaKMCuoiCung());
                case ADD_KHUYEN_MAI              -> Response.ok(khuyenMaiService.addKhuyenMai((KhuyenMaiDTO) data));
                case XOA_KHUYEN_MAI              -> Response.ok(khuyenMaiService.xoaKhuyenMai((String) data));
                case SUA_KHUYEN_MAI -> {
                    Object[] arr = (Object[]) data;
                    yield Response.ok(khuyenMaiService.suaKhuyenMai((String) arr[0], (KhuyenMaiDTO) arr[1]));
                }

                // ===== KHUYEN MAI SAN PHAM =====
                case GET_KMSP_BY_MA_KM         -> Response.ok(khuyenMaiService.getKMSPTheoMaKM((String) data));
                case GET_KMSP_BY_MA_SP         -> Response.ok(khuyenMaiService.getKMSPTheoMaSP((String) data));
                case ADD_KHUYEN_MAI_SAN_PHAM   -> Response.ok(khuyenMaiService.addKhuyenMaiSanPham((KhuyenMaiSanPhamDTO) data));
                case DELETE_KHUYEN_MAI_SAN_PHAM-> Response.ok(khuyenMaiService.deleteKhuyenMaiSanPham((Long) data));

                // ===== PHIEU NHAP =====
                case GET_PHIEU_NHAP_BY_MA -> Response.ok(phieuService.getPhieuNhapTheoMa((String) data));
                case GET_ALL_PHIEU_NHAP   -> Response.ok(phieuService.getAllPhieuNhap());
                case GET_SO_PN_CUOI       -> Response.ok(phieuService.getSoPhieuNhapCuoiCung());
                case ADD_PHIEU_NHAP       -> Response.ok(phieuService.addPhieuNhap((PhieuNhapDTO) data));

                // ===== CHI TIET PHIEU NHAP =====
                case GET_CTPN_BY_MA_PN     -> Response.ok(phieuService.getCTPNTheoMaPN((String) data));
                case ADD_CHI_TIET_PHIEU_NHAP -> Response.ok(phieuService.addChiTietPhieuNhap((ChiTietPhieuNhapDTO) data));

                // ===== PHIEU TRA HANG =====
                case GET_PTH_BY_MA    -> Response.ok(phieuService.getPhieuTraHangTheoMa((String) data));
                case GET_PTH_BY_MA_HD -> Response.ok(phieuService.getPTHTheoMaHD((String) data));
                case GET_SO_PTH_CUOI  -> Response.ok(phieuService.getSoPhieuTraHangCuoiCung());
                case ADD_PHIEU_TRA_HANG -> Response.ok(phieuService.addPhieuTraHang((PhieuTraHangDTO) data));

                // ===== CHI TIET PHIEU TRA HANG =====
                case GET_CTPTH_BY_MA_PTH     -> Response.ok(phieuService.getCTPTHTheoMaPTH((String) data));
                case ADD_CHI_TIET_PHIEU_TRA_HANG -> Response.ok(phieuService.addChiTietPhieuTraHang((ChiTietPhieuTraHangDTO) data));

                // ===== CA LAM =====
                case GET_ALL_CA_LAM    -> Response.ok(caLamService.getAllCaLam());
                case GET_CA_LAM_BY_TEN -> Response.ok(caLamService.timCaLamTheoTen((String) data));
                case ADD_CA_LAM        -> Response.ok(caLamService.addCaLam((CaLamDTO) data));
                case SUA_CA_LAM        -> Response.ok(caLamService.suaCaLam((CaLamDTO) data));
                case XOA_CA_LAM        -> Response.ok(caLamService.xoaCaLam((String) data));

                // ===== LICH SU CA LAM =====
                case GET_LSCL_BY_MA_NV      -> Response.ok(caLamService.getLSCLTheoMaNV((String) data));
                case GET_LSCL_BY_NGAY        -> Response.ok(caLamService.getLSCLTheoNgay((String) data));
                case GET_LSCL_DANG_LAM_BY_MA_NV -> Response.ok(caLamService.getLSCLDangLamTheoMaNV((String) data));
                case ADD_LICH_SU_CA_LAM      -> Response.ok(caLamService.addLichSuCaLam((LichSuCaLamDTO) data));
                case UPDATE_LICH_SU_CA_LAM   -> Response.ok(caLamService.updateLichSuCaLam((LichSuCaLamDTO) data));
                default -> Response.fail("Không hỗ trợ command: " + cmd);
            };
        } catch (Exception e) {
            return Response.fail("Lỗi server: " + e.getMessage());
        }
    }
}
