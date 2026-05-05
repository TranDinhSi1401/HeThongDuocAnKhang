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

    /** Lấy tất cả lô sản phẩm (có fetch thông tin liên quan). */
    public List<LoSanPhamDTO> getAllLoSanPham() {
        return loSanPhamDAO.getAllLoSanPham()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Lấy tất cả lịch sử lô. */
    public List<LichSuLoDTO> getAllLichSuLo() {
        return lichSuLoDAO.getAllLichSuLo()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Lấy các lô sản phẩm theo mã sản phẩm (chỉ lấy lô còn hạn và còn hàng). */
    public List<LoSanPhamDTO> getLoSanPhamTheoMaSP(String maSP) {
        return loSanPhamDAO.getLoSanPhamTheoMaSP(maSP)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Lấy tất cả lô sản phẩm chưa bị hủy. */
    public List<LoSanPhamDTO> getAllLoSanPhamKhongHuy() {
        return loSanPhamDAO.getAllLoSanPhamKhongHuy()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Tìm lô theo mã lô. */
    public LoSanPhamDTO timLoSanPham(String maLo) {
        LoSanPham lo = loSanPhamDAO.timLoSanPham(maLo);
        return lo != null ? EntityMapper.toDTO(lo) : null;
    }

    /** Lấy lô sản phẩm theo mã chi tiết hóa đơn. */
    public LoSanPhamDTO getLoSanPhamTheoMaCTHD(String maCTHD) {
        LoSanPham lo = loSanPhamDAO.getLoSanPhamTheoMaCTHD(maCTHD);
        return lo != null ? EntityMapper.toDTO(lo) : null;
    }

    /** Lấy danh sách lô theo mã sản phẩm (tất cả trạng thái). */
    public List<LoSanPhamDTO> dsLoTheoMaSanPham(String maSP) {
        return loSanPhamDAO.dsLoTheoMaSanPham(maSP)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Lấy các lô theo mã nhà cung cấp. */
    public List<LoSanPhamDTO> getLoSPTheoMaNhaCungCap(String maNCC) {
        return loSanPhamDAO.getLoSPTheoMaNhaCungCap(maNCC)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Lấy danh sách lô đã xuất của một chi tiết hóa đơn. */
    public List<LoSanPhamDTO> getDanhSachLoDaXuatTheoMaCTHD(String maCTHD) {
        return loSanPhamDAO.getDanhSachLoDaXuatTheoMaCTHD(maCTHD)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Thống kê số lượng lô theo 4 trạng thái. */
    public int[] demLoTheoTrangThai() {
        return loSanPhamDAO.demLoTheoTrangThai();
    }

    /** Lấy danh sách lô sắp hết hạn. */
    public List<LoSanPhamDTO> getLoSapHetHan() {
        return loSanPhamDAO.getLoSapHetHan()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Thêm mới lô sản phẩm. */
    public boolean addLoSanPham(LoSanPhamDTO dto) {
        // Bước 1: Tìm sản phẩm trực tiếp bằng mã sản phẩm gốc
        SanPham sp = sanPhamDAO.findById(dto.getMaSP());

        // Bước 2: Nếu không tìm thấy, thử coi giá trị đó là mã vạch
        if (sp == null) {
            String maGoc = sanPhamDAO.getMaSpTheoMaVach(dto.getMaSP());
            if (maGoc != null) {
                sp = sanPhamDAO.findById(maGoc);
            }
        }

        // Bước 3: Vẫn không tìm thấy → báo lỗi
        if (sp == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm với mã hoặc mã vạch: " + dto.getMaSP());
        }

        // Chuyển DTO thành entity và gán sản phẩm
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

    /** Lấy lịch sử của một lô cụ thể. */
    public List<LichSuLoDTO> getLichSuLoTheoMaLo(String maLo) {
        return lichSuLoDAO.getLichSuLoTheoMaLo(maLo)
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
    }

    /** Lưu lịch sử biến động lô. */
    public boolean addLichSuLo(LichSuLoDTO dto) {
        LichSuLo entity = EntityMapper.toEntity(dto);
        
        // Cần gán tham chiếu Entity vì Mapper chỉ map String ID
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
