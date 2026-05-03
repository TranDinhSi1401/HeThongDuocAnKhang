package server.service;

import common.dto.HoaDonDTO;
import common.dto.ChiTietHoaDonDTO;
import common.dto.ChiTietXuatLoDTO;
import server.dao.*;
import server.entity.*;
import server.mapper.EntityMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ HoaDon, ChiTietHoaDon, ChiTietXuatLo.
 */
public class HoaDonService {

    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO();
    private final ChiTietXuatLoDAO chiTietXuatLoDAO = new ChiTietXuatLoDAO();

    // ===== HoaDon =====

    public HoaDonDTO getHoaDonMoiNhatTrongNgay() {
        HoaDon hd = hoaDonDAO.getHoaDonMoiNhatTrongNgay();
        return hd != null ? EntityMapper.toDTO(hd) : null;
    }

    public HoaDonDTO getHoaDonTheoMaHD(String maHD) {
        HoaDon hd = hoaDonDAO.getHoaDonTheoMaHD(maHD);
        return hd != null ? EntityMapper.toDTO(hd) : null;
    }

    public List<HoaDonDTO> timHDTheoMaNV(String maNV) {
        return hoaDonDAO.timHDTheoMaNV(maNV)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<HoaDonDTO> timHDTheoMaKH(String maKH) {
        return hoaDonDAO.timHDTheoMaKH(maKH)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<HoaDonDTO> timHDTheoNgayLap(LocalDate date) {
        return hoaDonDAO.timHDTheoNgayLap(date)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<HoaDonDTO> timHDTheoKhoangNgay(LocalDate start, LocalDate end) {
        return hoaDonDAO.timHDTheoKhoangNgay(start, end)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<HoaDonDTO> timHDTheoSDTKH(String sdt) {
        return hoaDonDAO.timHDTheoSDTKH(sdt)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<HoaDonDTO> timHDTheoHinhThuc(boolean chuyenKhoan) {
        return hoaDonDAO.timHDTheoHinhThuc(chuyenKhoan)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getSoHDCuoiCungTrongNgay(String ngay) {
        return hoaDonDAO.getSoHDCuoiCungTrongNgay(ngay);
    }

    public int getSoPTH(String maHoaDon) {
        return hoaDonDAO.getSoPTH(maHoaDon);
    }

    public double getTongTienCacPTH(String maHoaDon) {
        return hoaDonDAO.getTongTienCacPTH(maHoaDon);
    }

    /**
     * Thêm mới hóa đơn. HoaDonDTO phải chứa maNV (và maKH nếu có).
     */
    public boolean addHoaDon(HoaDonDTO dto) {
        HoaDon entity = new HoaDon();
        entity.setMaHoaDon(dto.getMaHoaDon());
        entity.setNgayLapHoaDon(dto.getNgayLapHoaDon());
        entity.setChuyenKhoan(dto.isChuyenKhoan());
        entity.setTongTien(dto.getTongTien());
        NhanVien nv = new NhanVien();
        nv.setMaNV(dto.getMaNV());
        entity.setNhanVien(nv);
        if (dto.getMaKH() != null) {
            KhachHang kh = new KhachHang();
            kh.setMaKH(dto.getMaKH());
            entity.setKhachHang(kh);
        }
        hoaDonDAO.create(entity);
        return true;
    }

    // ===== Doanh thu =====

    public double getDoanhThuTheoNgay(LocalDate date) {
        return hoaDonDAO.getDoanhThuTheoNgay(date);
    }

    public double getDoanhThuTheoThang(LocalDate date) {
        return hoaDonDAO.getDoanhThuTheoThang(date);
    }

    public Map<Integer, Double> getDoanhThuTungNgayTrongThang(LocalDate date) {
        return hoaDonDAO.getDoanhThuTungNgayTrongThang(date);
    }

    public Map<Integer, Double> getDoanhThuTungThangTrongNam(LocalDate date) {
        return hoaDonDAO.getDoanhThuTungThangTrongNam(date);
    }

    public Map<String, Integer> getNamHoaDonCuNhatVaMoiNhat() {
        return hoaDonDAO.getNamHoaDonCuNhatVaMoiNhat();
    }

    // ===== ChiTietHoaDon =====

    public List<ChiTietHoaDonDTO> getCTHDByMaHD(String maHD) {
        return chiTietHoaDonDAO.getChiTietHoaDonTheoMaHoaDon(maHD)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addChiTietHoaDon(ChiTietHoaDonDTO dto) {
        ChiTietHoaDon entity = new ChiTietHoaDon();
        entity.setMaChiTietHoaDon(dto.getMaChiTietHoaDon());
        entity.setSoLuong(dto.getSoLuong());
        entity.setDonGia(dto.getDonGia());
        entity.setGiamGia(dto.getGiamGia());
        entity.setThanhTien(dto.getThanhTien());
        HoaDon hd = new HoaDon();
        hd.setMaHoaDon(dto.getMaHoaDon());
        entity.setHoaDon(hd);
        DonViTinh dvt = new DonViTinh();
        dvt.setMaDonViTinh(dto.getMaDonViTinh());
        entity.setDonViTinh(dvt);
        chiTietHoaDonDAO.create(entity);
        return true;
    }

    public boolean xoaCTHDByMaHD(String maHD) {
        return chiTietHoaDonDAO.xoaChiTietHoaDonTheoMaHD(maHD);
    }

    // ===== ChiTietXuatLo =====

    public boolean addChiTietXuatLo(ChiTietXuatLoDTO dto) {
        ChiTietXuatLo entity = new ChiTietXuatLo();
        entity.setSoLuong(dto.getSoLuong());
        LoSanPham lsp = new LoSanPham();
        lsp.setMaLoSanPham(dto.getMaLoSanPham());
        entity.setLoSanPham(lsp);
        ChiTietHoaDon cthd = new ChiTietHoaDon();
        cthd.setMaChiTietHoaDon(dto.getMaChiTietHoaDon());
        entity.setChiTietHoaDon(cthd);
        chiTietXuatLoDAO.create(entity);
        return true;
    }

    public List<ChiTietXuatLoDTO> getCTXLByMaCTHD(String maCTHD) {
        return chiTietXuatLoDAO.getChiTietXuatLoTheoMaCTHD(maCTHD)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }
}
