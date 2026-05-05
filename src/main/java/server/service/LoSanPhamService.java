package server.service;

import common.dto.LoSanPhamDTO;
import common.dto.LichSuLoDTO;
import server.dao.LoSanPhamDAO;
import server.dao.LichSuLoDAO;
import server.entity.*;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ LoSanPham và LichSuLo.
 */
public class LoSanPhamService {

    private final LoSanPhamDAO loSanPhamDAO = new LoSanPhamDAO();
    private final LichSuLoDAO lichSuLoDAO = new LichSuLoDAO();

    public List<LoSanPhamDTO> getAllLoSanPham() {
        return loSanPhamDAO.loadAll().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LichSuLoDTO> getAllLichSuLo() {
        return lichSuLoDAO.loadAll().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LoSanPhamDTO> getLoSanPhamTheoMaSP(String maSP) {
        return loSanPhamDAO.getLoSanPhamTheoMaSP(maSP)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public LoSanPhamDTO timLoSanPham(String maLo) {
        LoSanPham lo = loSanPhamDAO.timLoSanPham(maLo);
        return lo != null ? EntityMapper.toDTO(lo) : null;
    }

    public LoSanPhamDTO getLoSanPhamTheoMaCTHD(String maCTHD) {
        LoSanPham lo = loSanPhamDAO.getLoSanPhamTheoMaCTHD(maCTHD);
        return lo != null ? EntityMapper.toDTO(lo) : null;
    }

    public List<LoSanPhamDTO> dsLoTheoMaSanPham(String maSP) {
        return loSanPhamDAO.dsLoTheoMaSanPham(maSP)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LoSanPhamDTO> getLoSPTheoMaNhaCungCap(String maNCC) {
        return loSanPhamDAO.getLoSPTheoMaNhaCungCap(maNCC)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LoSanPhamDTO> getDanhSachLoDaXuatTheoMaCTHD(String maCTHD) {
        return loSanPhamDAO.getDanhSachLoDaXuatTheoMaCTHD(maCTHD)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int[] demLoTheoTrangThai() {
        return loSanPhamDAO.demLoTheoTrangThai();
    }

    public boolean addLoSanPham(LoSanPhamDTO dto) {
        LoSanPham entity = EntityMapper.toEntity(dto);
        SanPham sp = new SanPham();
        sp.setMaSP(dto.getMaSP());
        entity.setSanPham(sp);
        loSanPhamDAO.create(entity);
        return true;
    }

    public boolean truSoLuong(String maLo, int soLuong) {
        return loSanPhamDAO.truSoLuong(maLo, soLuong);
    }

    public boolean congSoLuong(String maLo, int soLuong, int heSoQuyDoi) {
        return loSanPhamDAO.congSoLuong(maLo, soLuong, heSoQuyDoi);
    }

    public boolean capNhatSoLuongLo(LoSanPhamDTO dto, int slDat) {
        LoSanPham lo = EntityMapper.toEntity(dto);
        return loSanPhamDAO.capNhatSoLuongLo(lo, slDat);
    }

    public boolean huyLoSanPham(LoSanPhamDTO dto) {
        LoSanPham lo = EntityMapper.toEntity(dto);
        return loSanPhamDAO.huyLoSanPham(lo);
    }

    // ===== LichSuLo =====

    public List<LichSuLoDTO> getLichSuLoTheoMaLo(String maLo) {
        return lichSuLoDAO.getLichSuLoTheoMaLo(maLo)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addLichSuLo(LichSuLoDTO dto) {
        LichSuLo entity = new LichSuLo();
        entity.setMaLichSuLo(dto.getMaLichSuLo());
        entity.setThoiGian(dto.getThoiGian());
        entity.setHanhDong(dto.getHanhDong());
        entity.setSoLuongSau(dto.getSoLuongSau());
        entity.setGhiChu(dto.getGhiChu());
        LoSanPham lo = new LoSanPham();
        lo.setMaLoSanPham(dto.getMaLoSanPham());
        entity.setLoSanPham(lo);
        NhanVien nv = new NhanVien();
        nv.setMaNV(dto.getMaNV());
        entity.setNhanVien(nv);
        lichSuLoDAO.create(entity);
        return true;
    }
}
