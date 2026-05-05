package server.service;

import java.util.List;
import java.util.stream.Collectors;

import common.dto.LichSuLoDTO;
import common.dto.LoSanPhamDTO;
import server.dao.LichSuLoDAO;
import server.dao.LoSanPhamDAO;
import server.dao.SanPhamDAO;
import server.entity.LichSuLo;
import server.entity.LoSanPham;
import server.entity.NhanVien;
import server.entity.SanPham;
import server.mapper.EntityMapper;

/**
 * Service xử lý nghiệp vụ LoSanPham và LichSuLo.
 */
public class LoSanPhamService {

    private final LoSanPhamDAO loSanPhamDAO = new LoSanPhamDAO();
    private final LichSuLoDAO lichSuLoDAO = new LichSuLoDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    public List<LoSanPhamDTO> getLoSanPhamTheoMaSP(String maSP) {
        return loSanPhamDAO.getLoSanPhamTheoMaSP(maSP)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LoSanPhamDTO> getAllLoSanPham() {
        return loSanPhamDAO.getAllLoSanPham()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    public List<LoSanPhamDTO> getAllLoSanPhamKhongHuy() {
        return loSanPhamDAO.getAllLoSanPhamKhongHuy()
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
        // Bước 1: Tìm sản phẩm trực tiếp bằng mã sản phẩm gốc (vd: SP-0001)
        SanPham sp = sanPhamDAO.findById(dto.getMaSP());

        // Bước 2: Nếu không tìm thấy, thử coi giá trị đó là mã vạch và tra cứu
        if (sp == null) {
            String maGoc = sanPhamDAO.getMaSpTheoMaVach(dto.getMaSP());
            if (maGoc != null) {
                sp = sanPhamDAO.findById(maGoc);
            }
        }

        // Bước 3: Vẫn không tìm thấy → báo lỗi rõ ràng
        if (sp == null) {
            throw new RuntimeException(
                "Không tìm thấy sản phẩm với mã hoặc mã vạch: " + dto.getMaSP());
        }

        // Chuyển DTO thành entity và gán sản phẩm đã tìm được
        LoSanPham entity = EntityMapper.toEntity(dto);
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

    public List<LichSuLoDTO> getAllLichSuLo() {
        return lichSuLoDAO.getAllLichSuLo()
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

    public LoSanPhamDTO getLoSanPhamByMa(String maLo) {
        LoSanPham lo = loSanPhamDAO.findById(maLo);
        return lo != null ? EntityMapper.toDTO(lo) : null;
    }
}
