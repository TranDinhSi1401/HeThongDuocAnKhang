package server.service;

import common.dto.*;
import server.dao.*;
import server.entity.*;
import server.mapper.EntityMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/** Service xử lý nghiệp vụ PhieuNhap, ChiTietPhieuNhap, PhieuTraHang, ChiTietPhieuTraHang. */
public class PhieuService {

    private final PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();
    private final ChiTietPhieuNhapDAO ctpnDAO = new ChiTietPhieuNhapDAO();
    private final PhieuTraHangDAO phieuTraHangDAO = new PhieuTraHangDAO();
    private final ChiTietPhieuTraHangDAO ctpthDAO = new ChiTietPhieuTraHangDAO();
    private final ChiTietHoaDonDAO cthDAO = new ChiTietHoaDonDAO();

    // ===== PhieuNhap =====

    public PhieuNhapDTO getPhieuNhapTheoMa(String maPN) {
        PhieuNhap pn = phieuNhapDAO.findById(maPN);
        return pn != null ? EntityMapper.toDTO(pn) : null;
    }

    public List<PhieuNhapDTO> getAllPhieuNhap() {
        return phieuNhapDAO.loadAll().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getSoPhieuNhapCuoiCung() { return phieuNhapDAO.getSoPhieuNhapCuoiCung(); }

    public boolean addPhieuNhap(PhieuNhapDTO dto) {
        PhieuNhap e = new PhieuNhap();
        e.setMaPhieuNhap(dto.getMaPhieuNhap());
        e.setNgayTao(dto.getNgayTao());
        e.setTongTien(dto.getTongTien());
        e.setGhiChu(dto.getGhiChu());
        NhanVien nv = new NhanVien(); nv.setMaNV(dto.getMaNV()); e.setNhanVien(nv);
        phieuNhapDAO.create(e);
        return true;
    }

    // ===== ChiTietPhieuNhap =====

    public List<ChiTietPhieuNhapDTO> getCTPNTheoMaPN(String maPN) {
        return ctpnDAO.getChiTietPhieuNhapTheoMaPN(maPN).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addChiTietPhieuNhap(ChiTietPhieuNhapDTO dto) {
        ChiTietPhieuNhap e = new ChiTietPhieuNhap();
        e.setSoLuong(dto.getSoLuong()); e.setSoLuongYeuCau(dto.getSoLuongYeuCau());
        e.setDonGia(dto.getDonGia()); e.setThanhTien(dto.getThanhTien()); e.setGhiChu(dto.getGhiChu());
        PhieuNhap pn = new PhieuNhap(); pn.setMaPhieuNhap(dto.getMaPhieuNhap()); e.setPhieuNhap(pn);
        LoSanPham lo = new LoSanPham(); lo.setMaLoSanPham(dto.getMaLoSanPham()); e.setLoSanPham(lo);
        NhaCungCap ncc = new NhaCungCap(); ncc.setMaNCC(dto.getMaNCC()); e.setNhaCungCap(ncc);
        ctpnDAO.create(e);
        return true;
    }

    // ===== PhieuTraHang =====

    public PhieuTraHangDTO getPhieuTraHangTheoMa(String maPTH) {
        PhieuTraHang pth = phieuTraHangDAO.findById(maPTH);
        return pth != null ? EntityMapper.toDTO(pth) : null;
    }

    public List<PhieuTraHangDTO> getPTHTheoMaHD(String maHD) {
        return phieuTraHangDAO.getPhieuTraHangTheoMaHoaDon(maHD).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getSoPhieuTraHangCuoiCung() { return phieuTraHangDAO.getSoPhieuTraHangCuoiCung(); }

    public boolean addPhieuTraHang(PhieuTraHangDTO dto) {
        // Server tự sinh mã (KHÔNG dùng dto.getMaPhieuTraHang())

        String maMoi = sinhMaPhieuTraHang();
        PhieuTraHang e = new PhieuTraHang();

        e.setMaPhieuTraHang(maMoi);
        e.setNgayLapPhieuTraHang(LocalDateTime.now());
        e.setTongTienHoaTra(dto.getTongTienHoaTra());
        NhanVien nv = new NhanVien(); nv.setMaNV(dto.getMaNV()); e.setNhanVien(nv);
        HoaDon   hd = new HoaDon();   hd.setMaHoaDon(dto.getMaHoaDon()); e.setHoaDon(hd);
        phieuTraHangDAO.create(e);

        // Set lại mã thực tế vào DTO để client biết
        dto.setMaPhieuTraHang(maMoi);
        return true;
    }

    private String sinhMaPhieuTraHang() {
        LocalDate t = LocalDate.now();
        String ddMMyy = String.format("%02d%02d%02d",
                t.getDayOfMonth(), t.getMonthValue(), t.getYear() % 100);

        int soCuoi = phieuTraHangDAO.getSoPTHCuoiCungTrongNgay(ddMMyy);   // dùng method có sẵn
        return String.format("PTH-%s-%04d", ddMMyy, soCuoi + 1);
    }

    // ===== ChiTietPhieuTraHang =====

    public List<ChiTietPhieuTraHangDTO> getCTPTHTheoMaPTH(String maPTH) {
        return ctpthDAO.getChiTietPhieuTraHangTheoMaPTH(maPTH).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addChiTietPhieuTraHang(ChiTietPhieuTraHangDTO dto) {
        ChiTietPhieuTraHang e = new ChiTietPhieuTraHang();
        e.setSoLuong(dto.getSoLuong());
        e.setGiaTriHoanTra(dto.getGiaTriHoanTra());
        e.setThanhTienHoanTra(dto.getThanhTienHoanTra());
        if (dto.getTruongHopDoiTra() != null)
            e.setTruongHopDoiTra(TruongHopDoiTraEnum.valueOf(dto.getTruongHopDoiTra()));
        if (dto.getTinhTrangSanPham() != null)
            e.setTinhTrangSanPham(TinhTrangSanPhamEnum.valueOf(dto.getTinhTrangSanPham()));

        return ctpthDAO.createWithRefs(e, dto.getMaPhieuTraHang(), dto.getMaChiTietHoaDon());
    }
}
