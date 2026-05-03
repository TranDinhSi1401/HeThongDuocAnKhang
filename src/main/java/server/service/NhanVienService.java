package server.service;

import common.dto.NhanVienDTO;
import common.dto.TaiKhoanDTO;
import server.dao.NhanVienDAO;
import server.dao.TaiKhoanDAO;
import server.entity.NhanVien;
import server.entity.TaiKhoan;
import server.mapper.EntityMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ NhanVien và TaiKhoan.
 */
public class NhanVienService {

    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    // ===== NhanVien =====

    public List<NhanVienDTO> getAllNhanVien() {
        return nhanVienDAO.getAllNhanVien()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public NhanVienDTO getNhanVienTheoMaNV(String maNV) {
        NhanVien nv = nhanVienDAO.getNhanVienTheoMaNV(maNV);
        return nv != null ? EntityMapper.toDTO(nv) : null;
    }

    public NhanVienDTO timNVTheoMa(String ma) {
        NhanVien nv = nhanVienDAO.timNVTheoMa(ma);
        return nv != null ? EntityMapper.toDTO(nv) : null;
    }

    public List<NhanVienDTO> timNVTheoTen(String ten) {
        return nhanVienDAO.timNVTheoTen(ten)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhanVienDTO> timNVTheoSDT(String sdt) {
        return nhanVienDAO.timNVTheoSDT(sdt)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhanVienDTO> timNVTheoCCCD(String cccd) {
        return nhanVienDAO.timNVTheoCCCD(cccd)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<NhanVienDTO> timNVTheoTrangThai(boolean daNghiViec) {
        return nhanVienDAO.timNVTheoTrangThai(daNghiViec)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public int getMaNVCuoiCung() {
        return nhanVienDAO.getMaNVCuoiCung();
    }

    public boolean addNhanVien(NhanVienDTO dto) {
        NhanVien entity = EntityMapper.toEntity(dto);
        nhanVienDAO.create(entity);
        return true;
    }

    public boolean suaNhanVien(String maNV, NhanVienDTO dto) {
        NhanVien nvNew = EntityMapper.toEntity(dto);
        return nhanVienDAO.suaNhanVien(maNV, nvNew);
    }

    public boolean xoaNhanVien(String maNV) {
        return nhanVienDAO.xoaNhanVien(maNV);
    }

    // ===== TaiKhoan =====

    public TaiKhoanDTO getTaiKhoanTheoMaNV(String maNV) {
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoMaNV(maNV);
        return tk != null ? EntityMapper.toDTO(tk) : null;
    }

    public TaiKhoanDTO getTaiKhoanTheoEmail(String email) {
        TaiKhoan tk = taiKhoanDAO.getTaiKhoanTheoEmail(email);
        return tk != null ? EntityMapper.toDTO(tk) : null;
    }

    public boolean addTaiKhoan(TaiKhoanDTO dto) {
        TaiKhoan entity = EntityMapper.toEntityTaiKhoan(dto);
        // Gắn NhanVien reference
        NhanVien nv = new NhanVien();
        nv.setMaNV(dto.getMaNV());
        entity.setNhanVien(nv);
        taiKhoanDAO.create(entity);
        return true;
    }

    public boolean capNhatTaiKhoan(TaiKhoanDTO dto) {
        TaiKhoan tk = EntityMapper.toEntityTaiKhoan(dto);
        return taiKhoanDAO.capNhatTaiKhoan(tk);
    }

    public boolean xoaTaiKhoan(String maNV) {
        return taiKhoanDAO.xoaTaiKhoan(maNV);
    }

    public boolean updateMatKhau(String maNV, String email, String matKhauHash) {
        return taiKhoanDAO.updateMatKhau(maNV, email, matKhauHash);
    }

    public boolean kiemTraEmailTonTai(String email) {
        return taiKhoanDAO.kiemTraEmailTonTai(email);
    }

    public boolean kiemTraEmailThuocTaiKhoan(String maNV, String email) {
        return taiKhoanDAO.kiemTraEmailThuocTaiKhoan(maNV, email);
    }
}
