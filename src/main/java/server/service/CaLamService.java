package server.service;

import common.dto.CaLamDTO;
import common.dto.LichSuCaLamDTO;
import server.dao.CaLamDAO;
import server.dao.LichSuCaLamDAO;
import server.entity.*;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/** Service xử lý nghiệp vụ CaLam và LichSuCaLam. */
public class CaLamService {

    private final CaLamDAO caLamDAO = new CaLamDAO();
    private final LichSuCaLamDAO lichSuCaLamDAO = new LichSuCaLamDAO();

    public List<CaLamDTO> getAllCaLam() {
        return caLamDAO.loadAll().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<CaLamDTO> timCaLamTheoTen(String tenCa) {
        return caLamDAO.timCaLamTheoTen(tenCa).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public boolean addCaLam(CaLamDTO dto) { caLamDAO.create(EntityMapper.toEntity(dto)); return true; }

    public boolean suaCaLam(CaLamDTO dto) { caLamDAO.update(EntityMapper.toEntity(dto)); return true; }

    public boolean xoaCaLam(String maCa) { return caLamDAO.delete(maCa); }

    // ===== LichSuCaLam =====

    public List<LichSuCaLamDTO> getAllLichSuCaLam() {
        return lichSuCaLamDAO.loadAll().stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LichSuCaLamDTO> getLSCLTheoMaNV(String maNV) {
        return lichSuCaLamDAO.getLichSuCaLamTheoMaNV(maNV).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LichSuCaLamDTO> getLSCLTheoNgay(String ngay) {
        return lichSuCaLamDAO.getLichSuCaLamTheoNgay(ngay).stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public LichSuCaLamDTO getLSCLDangLamTheoMaNV(String maNV) {
        LichSuCaLam lscl = lichSuCaLamDAO.getLichSuCaLamDangLamTheoMaNV(maNV);
        return lscl != null ? EntityMapper.toDTO(lscl) : null;
    }

    public boolean addLichSuCaLam(LichSuCaLamDTO dto) {
        LichSuCaLam e = new LichSuCaLam();
        e.setNgayLamViec(dto.getNgayLamViec());
        e.setThoiGianVaoCa(dto.getThoiGianVaoCa());
        e.setThoiGianRaCa(dto.getThoiGianRaCa());
        e.setGhiChu(dto.getGhiChu());
        NhanVien nv = new NhanVien(); nv.setMaNV(dto.getMaNV()); e.setNhanVien(nv);
        CaLam ca = new CaLam(); ca.setMaCa(dto.getMaCa()); e.setCaLam(ca);
        lichSuCaLamDAO.create(e);
        return true;
    }

    public boolean updateLichSuCaLam(LichSuCaLamDTO dto) {
        LichSuCaLam e = new LichSuCaLam();
        e.setId(dto.getId());
        e.setNgayLamViec(dto.getNgayLamViec());
        e.setThoiGianVaoCa(dto.getThoiGianVaoCa());
        e.setThoiGianRaCa(dto.getThoiGianRaCa());
        e.setGhiChu(dto.getGhiChu());
        NhanVien nv = new NhanVien(); nv.setMaNV(dto.getMaNV()); e.setNhanVien(nv);
        CaLam ca = new CaLam(); ca.setMaCa(dto.getMaCa()); e.setCaLam(ca);
        lichSuCaLamDAO.update(e);
        return true;
    }
}
