package server.service;

import common.dto.*;
import server.dao.*;
import server.entity.*;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/** Service xử lý nghiệp vụ KhuyenMai, KhuyenMaiSanPham, PhieuNhap, ChiTietPhieuNhap. */
public class KhuyenMaiService {

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();
    private final KhuyenMaiSanPhamDAO kmspDAO = new KhuyenMaiSanPhamDAO();

    public List<KhuyenMaiDTO> getAllKhuyenMai() {
        return khuyenMaiDAO.getAllKhuyenMai().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<KhuyenMaiDTO> getKhuyenMaiDangHoatDong() {
        return khuyenMaiDAO.getKhuyenMaiDangHoatDong().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public KhuyenMaiDTO timKMTheoMa(String maKM) {
        KhuyenMai km = khuyenMaiDAO.findById(maKM);
        return km != null ? EntityMapper.toDTO(km) : null;
    }

    public List<KhuyenMaiDTO> getKhuyenMaiTheoMaSp(String maSP) {
        return khuyenMaiDAO.getKhuyenMaiTheoMaSP(maSP).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<KhuyenMaiDTO> timKhuyenMaiTheoMoTa(String moTa) {
        return khuyenMaiDAO.timKhuyenMaiTheoMoTa(moTa).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<KhuyenMaiDTO> timKhuyenMaiTheoLoai(String loai) {
        return khuyenMaiDAO.timKhuyenMaiTheoLoai(loai).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getMaKMCuoiCung() { return khuyenMaiDAO.getMaKMCuoiCung(); }

    public boolean addKhuyenMai(KhuyenMaiDTO dto) {
        khuyenMaiDAO.create(EntityMapper.toEntity(dto));
        return true;
    }

    public boolean suaKhuyenMai(String maKM, KhuyenMaiDTO dto) {
        return khuyenMaiDAO.suaKhuyenMai(maKM, EntityMapper.toEntity(dto));
    }

    public boolean xoaKhuyenMai(String maKM) { return khuyenMaiDAO.xoaKhuyenMai(maKM); }

    // ===== KhuyenMaiSanPham =====

    public List<KhuyenMaiSanPhamDTO> getKMSPTheoMaKM(String maKM) {
        return kmspDAO.getKhuyenMaiSanPhamTheoMaKM(maKM).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<KhuyenMaiSanPhamDTO> getKMSPTheoMaSP(String maSP) {
        return kmspDAO.getKhuyenMaiSanPhamTheoMaSP(maSP).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addKhuyenMaiSanPham(KhuyenMaiSanPhamDTO dto) {
        KhuyenMaiSanPham e = new KhuyenMaiSanPham();
        e.setNgayChinhSua(dto.getNgayChinhSua());
        KhuyenMai km = khuyenMaiDAO.findById(dto.getMaKhuyenMai());
        SanPham sp = new server.dao.SanPhamDAO().findById(dto.getMaSP());
        e.setKhuyenMai(km);
        e.setSanPham(sp);
        kmspDAO.create(e);
        return true;
    }

    public boolean deleteKhuyenMaiSanPham(Long id) { return kmspDAO.delete(id); }

    public boolean deleteKMSPByMaSP(String maSP) {
        return kmspDAO.xoaHetKMCuaSP(maSP);
    }
}
