package server.service;

import common.dto.*;
import server.dao.SanPhamDAO;
import server.dao.DonViTinhDAO;
import server.dao.MaVachSanPhamDAO;
import server.entity.*;
import server.mapper.EntityMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Service xử lý nghiệp vụ liên quan đến SanPham, DonViTinh, MaVachSanPham.
 */
public class SanPhamService {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final DonViTinhDAO donViTinhDAO = new DonViTinhDAO();
    private final MaVachSanPhamDAO maVachSanPhamDAO = new MaVachSanPhamDAO();

    // ===== SanPham =====

    public List<SanPhamDTO> getAllSanPham() {
        return sanPhamDAO.getAllTableSanPham()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public SanPhamDTO timSPTheoMa(String ma) {
        SanPham sp = sanPhamDAO.timSPTheoMa(ma);
        return sp != null ? EntityMapper.toDTO(sp) : null;
    }

    public List<SanPhamDTO> timSPTheoTen(String ten) {
        return sanPhamDAO.timSPTheoTen(ten)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<SanPhamDTO> timSPTheoLoai(String loaiSanPham) {
        LoaiSanPhamEnum loai = LoaiSanPhamEnum.valueOf(loaiSanPham);
        return sanPhamDAO.timSPTheoLoai(loai)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<SanPhamDTO> timSPTheoMaNCC(String maNCC) {
        return sanPhamDAO.timSPTheoMaNCC(maNCC)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<SanPhamDTO> getSPDaHetHang() {
        return sanPhamDAO.getSPDaHetHang()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Trả về Map<SanPhamDTO, Integer> (tồn kho hiện tại).
     */
    public Map<SanPhamDTO, Integer> getSPSapHetHang() {
        Map<SanPhamDTO, Integer> result = new java.util.LinkedHashMap<>();
        sanPhamDAO.getSPSapHetHang()
                .forEach((sp, ton) -> result.put(EntityMapper.toDTO(sp), ton));
        return result;
    }

    public String getMaSpTheoMaVach(String maVach) {
        return sanPhamDAO.getMaSpTheoMaVach(maVach);
    }

    public int getMaSPCuoiCung() {
        return sanPhamDAO.getMaSPCuoiCung();
    }

    public boolean addSanPham(SanPhamDTO dto) {
        SanPham entity = EntityMapper.toEntity(dto);
        sanPhamDAO.create(entity);
        return true;
    }

    public boolean suaSanPham(String maSP, SanPhamDTO dto) {
        SanPham spNew = EntityMapper.toEntity(dto);
        return sanPhamDAO.suaSanPham(maSP, spNew);
    }

    public boolean xoaSanPham(String maSP) {
        return sanPhamDAO.xoaSanPham(maSP);
    }

    /**
     * Trả về List<Object[]> đã serializable dưới dạng Map<SanPhamDTO, Number[]>.
     */
    public Map<SanPhamDTO, Number[]> getSPBanChayTrongThang(LocalDate tg) {
        Map<SanPhamDTO, Number[]> result = new LinkedHashMap<>();
        sanPhamDAO.getSPBanChayTrongThang(tg)
                .forEach((sp, nums) -> result.put(EntityMapper.toDTO(sp), nums));
        return result;
    }

    public Map<SanPhamDTO, Number[]> getSPBanChayTrongNam(LocalDate tg) {
        Map<SanPhamDTO, Number[]> result = new LinkedHashMap<>();
        sanPhamDAO.getSPBanChayTrongNam(tg)
                .forEach((sp, nums) -> result.put(EntityMapper.toDTO(sp), nums));
        return result;
    }

    public ChiTietSanPhamDTO getChiTietSpBangMaSp(String maSP) {
        SanPhamDTO sp = timSPTheoMa(maSP);
        List<DonViTinhDTO> dsDVT = getDonViTinhTheoMaSP(maSP);
        List<KhuyenMaiDTO> dsKM = new KhuyenMaiService().getKhuyenMaiTheoMaSp(maSP);
        List<LoSanPhamDTO> dsLSP = new LoSanPhamService().getLoSanPhamTheoMaSP(maSP);
        return ChiTietSanPhamDTO.builder()
                .sanPham(sp)
                .dsDVT(dsDVT)
                .dsKM(dsKM)
                .dsLSP(dsLSP)
                .build();
    }

    // ===== DonViTinh =====

    public List<DonViTinhDTO> getDonViTinhTheoMaSP(String maSP) {
        return donViTinhDAO.getDonViTinhTheoMaSP(maSP)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public DonViTinhDTO getDVTCoBanTheoMaSP(String maSP) {
        DonViTinh dvt = donViTinhDAO.getDVTCoBanTheoMaSP(maSP);
        return dvt != null ? EntityMapper.toDTO(dvt) : null;
    }

    public int getMaDVTCuoiCung() {
        return donViTinhDAO.getMaDVTCuoiCung();
    }

    public boolean addDonViTinh(DonViTinhDTO dto) {
        DonViTinh entity = EntityMapper.toEntity(dto);
        // Gắn SanPham reference (lazy proxy) — dùng getReference để không load toàn bộ entity
        entity.setSanPham(new SanPham());
        entity.getSanPham().setMaSP(dto.getMaSP());
        donViTinhDAO.create(entity);
        return true;
    }

    public boolean suaDonViTinh(String maDVT, DonViTinhDTO dto) {
        DonViTinh dvtNew = EntityMapper.toEntity(dto);
        return donViTinhDAO.suaDonViTinh(maDVT, dvtNew);
    }

    public boolean xoaDonViTinh(String maDVT) {
        return donViTinhDAO.xoaDonViTinh(maDVT);
    }

    public boolean xoaDVTTheoMaSP(String maSP) {
        return donViTinhDAO.xoaDonViTinhTheoMaSP(maSP);
    }

    // ===== MaVachSanPham =====

    public boolean addMaVach(MaVachSanPhamDTO dto) {
        MaVachSanPham entity = EntityMapper.toEntity(dto);
        SanPham sp = new SanPham();
        sp.setMaSP(dto.getMaSP());
        entity.setSanPham(sp);
        maVachSanPhamDAO.create(entity);
        return true;
    }

    public boolean deleteMaVach(String maVach) {
        return maVachSanPhamDAO.delete(maVach);
    }
}
