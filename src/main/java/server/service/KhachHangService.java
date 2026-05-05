package server.service;

import common.dto.KhachHangDTO;
import server.dao.KhachHangDAO;
import server.entity.KhachHang;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ KhachHang.
 */
public class KhachHangService {

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public List<KhachHangDTO> getAllKhachHang() {
        return khachHangDAO.getAllKhachHang()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public KhachHangDTO getKhachHangTheoSdt(String sdt) {
        KhachHang kh = khachHangDAO.getKhachHangTheoSdt(sdt);
        return kh != null ? EntityMapper.toDTO(kh) : null;
    }

    public KhachHangDTO timKHTheoMa(String ma) {
        KhachHang kh = khachHangDAO.timKHTheoMa(ma);
        return kh != null ? EntityMapper.toDTO(kh) : null;
    }

    public List<KhachHangDTO> timKHTheoTen(String ten) {
        return khachHangDAO.timKHTheoTen(ten)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<KhachHangDTO> timKHTheoSDT(String sdt) {
        return khachHangDAO.timKHTheoSDT(sdt)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getMaKHCuoiCung() {
        return khachHangDAO.getMaKHCuoiCung();
    }

    public boolean addKhachHang(KhachHangDTO dto) {
        if (khachHangDAO.getKhachHangTheoSdt(dto.getSdt()) != null) {
            return false; // SĐT đã tồn tại
        }
        KhachHang entity = EntityMapper.toEntity(dto);
        khachHangDAO.create(entity);
        return true;
    }

    public boolean suaKhachHang(String maKH, KhachHangDTO dto) {
        KhachHang existing = khachHangDAO.getKhachHangTheoSdt(dto.getSdt());
        if (existing != null && !existing.getMaKH().equals(maKH)) {
            return false; // SĐT đã thuộc về KH khác
        }
        KhachHang khNew = EntityMapper.toEntity(dto);
        return khachHangDAO.suaKhachHang(maKH, khNew);
    }

    public boolean xoaKhachHang(String maKH) {
        return khachHangDAO.xoaKhachHang(maKH);
    }

    public boolean updateDiemTichLuy(int diem, String maKH) {
        return khachHangDAO.updateDiemTichLuy(diem, maKH);
    }

    public boolean truDiemTichLuy(int diem, String maKH) {
        return khachHangDAO.truDiemTichLuy(diem, maKH);
    }
}
