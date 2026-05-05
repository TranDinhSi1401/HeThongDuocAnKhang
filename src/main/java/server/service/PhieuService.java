package server.service;

import common.dto.*;
import server.dao.*;
import server.entity.*;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/** Service xử lý nghiệp vụ PhieuNhap, ChiTietPhieuNhap, PhieuTraHang, ChiTietPhieuTraHang. */
public class PhieuService {

    private final PhieuNhapDAO phieuNhapDAO = new PhieuNhapDAO();
    private final ChiTietPhieuNhapDAO ctpnDAO = new ChiTietPhieuNhapDAO();
    private final PhieuTraHangDAO phieuTraHangDAO = new PhieuTraHangDAO();
    private final ChiTietPhieuTraHangDAO ctpthDAO = new ChiTietPhieuTraHangDAO();

    // ===== PhieuNhap =====

    public PhieuNhapDTO getPhieuNhapTheoMa(String maPN) {
        PhieuNhap pn = phieuNhapDAO.findById(maPN);
        return pn != null ? EntityMapper.toDTO(pn) : null;
    }

    public List<PhieuNhapDTO> getPNTheoMaNV(String maNV) {
        return phieuNhapDAO.timPNTheoMaNV(maNV).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<PhieuNhapDTO> getPNTheoNgay(java.time.LocalDate ngay) {
        return phieuNhapDAO.timPNTheoNgay(ngay).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<PhieuNhapDTO> getPNTheoKhoangNgay(java.time.LocalDate start, java.time.LocalDate end) {
        return phieuNhapDAO.timPNTheoKhoangNgay(start, end).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
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

    public List<PhieuTraHangDTO> getPTHTheoMaNV(String maNV) {
        return phieuTraHangDAO.timPTHTheoMaNV(maNV).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<PhieuTraHangDTO> getPTHTheoNgay(java.time.LocalDate ngay) {
        return phieuTraHangDAO.timPTHTheoNgay(ngay).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<PhieuTraHangDTO> getAllPhieuTraHang() {
        return phieuTraHangDAO.loadAll().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getSoPhieuTraHangCuoiCung() { return phieuTraHangDAO.getSoPhieuTraHangCuoiCung(); }

    public boolean addPhieuTraHang(PhieuTraHangDTO dto) {
        PhieuTraHang e = new PhieuTraHang();
        e.setMaPhieuTraHang(dto.getMaPhieuTraHang());
        e.setNgayLapPhieuTraHang(dto.getNgayLapPhieuTraHang());
        e.setTongTienHoaTra(dto.getTongTienHoaTra());
        NhanVien nv = new NhanVien(); nv.setMaNV(dto.getMaNV()); e.setNhanVien(nv);
        HoaDon hd = new HoaDon(); hd.setMaHoaDon(dto.getMaHoaDon()); e.setHoaDon(hd);
        phieuTraHangDAO.create(e);
        return true;
    }

    // ===== ChiTietPhieuTraHang =====

    public List<ChiTietPhieuTraHangDTO> getCTPTHTheoMaPTH(String maPTH) {
        return ctpthDAO.getChiTietPhieuTraHangTheoMaPTH(maPTH).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addChiTietPhieuTraHang(ChiTietPhieuTraHangDTO dto) {
        ChiTietPhieuTraHang e = new ChiTietPhieuTraHang();
        e.setSoLuong(dto.getSoLuong()); e.setGiaTriHoanTra(dto.getGiaTriHoanTra());
        e.setThanhTienHoanTra(dto.getThanhTienHoanTra());
        if (dto.getTruongHopDoiTra() != null) e.setTruongHopDoiTra(server.entity.TruongHopDoiTraEnum.valueOf(dto.getTruongHopDoiTra()));
        if (dto.getTinhTrangSanPham() != null) e.setTinhTrangSanPham(server.entity.TinhTrangSanPhamEnum.valueOf(dto.getTinhTrangSanPham()));
        PhieuTraHang pth = new PhieuTraHang(); pth.setMaPhieuTraHang(dto.getMaPhieuTraHang()); e.setPhieuTraHang(pth);
        ChiTietHoaDon cthd = new ChiTietHoaDon(); cthd.setMaChiTietHoaDon(dto.getMaChiTietHoaDon()); e.setChiTietHoaDon(cthd);
        ctpthDAO.create(e);
        return true;
    }
}
