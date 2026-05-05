package server.mapper;

import common.dto.CaLamDTO;
import common.dto.ChiTietHoaDonDTO;
import common.dto.ChiTietPhieuNhapDTO;
import common.dto.ChiTietPhieuTraHangDTO;
import common.dto.ChiTietXuatLoDTO;
import common.dto.DonViTinhDTO;
import common.dto.HoaDonDTO;
import common.dto.KhachHangDTO;
import common.dto.KhuyenMaiDTO;
import common.dto.KhuyenMaiSanPhamDTO;
import common.dto.LichSuCaLamDTO;
import common.dto.LichSuLoDTO;
import common.dto.LoSanPhamDTO;
import common.dto.MaVachSanPhamDTO;
import common.dto.NhaCungCapDTO;
import common.dto.NhanVienDTO;
import common.dto.PhieuNhapDTO;
import common.dto.PhieuTraHangDTO;
import common.dto.SanPhamCungCapDTO;
import common.dto.SanPhamDTO;
import common.dto.TaiKhoanDTO;
import server.entity.CaLam;
import server.entity.ChiTietHoaDon;
import server.entity.ChiTietPhieuNhap;
import server.entity.ChiTietPhieuTraHang;
import server.entity.ChiTietXuatLo;
import server.entity.DonViTinh;
import server.entity.HoaDon;
import server.entity.KhachHang;
import server.entity.KhuyenMai;
import server.entity.KhuyenMaiSanPham;
import server.entity.LichSuCaLam;
import server.entity.LichSuLo;
import server.entity.LoSanPham;
import server.entity.LoaiKhuyenMaiEnum;
import server.entity.LoaiSanPhamEnum;
import server.entity.MaVachSanPham;
import server.entity.NhaCungCap;
import server.entity.NhanVien;
import server.entity.PhieuNhap;
import server.entity.PhieuTraHang;
import server.entity.SanPham;
import server.entity.SanPhamCungCap;
import server.entity.TaiKhoan;

/**
 * "Phiên dịch viên" giữa Entity (JPA, chỉ dùng phía Server)
 * và DTO (Serializable, dùng để truyền qua Socket).
 *
 * Quy tắc:
 *  - toEntity(DTO)  → tạo Entity từ DTO để lưu DB (persist/merge).
 *  - toDTO(Entity)  → tạo DTO từ Entity để gửi qua mạng cho Client.
 *
 * Với các mối quan hệ ManyToOne/OneToOne, chỉ map khóa ngoài (String ID)
 * để tránh vòng lặp đệ quy và LazyInitializationException.
 */
public class EntityMapper {

    private EntityMapper() {}

    // ============================================================
    //  CaLam
    // ============================================================
    public static CaLam toEntity(CaLamDTO dto) {
        return CaLam.builder()
                .maCa(dto.getMaCa())
                .tenCa(dto.getTenCa())
                .thoiGianBatDau(dto.getThoiGianBatDau())
                .thoiGianKetThuc(dto.getThoiGianKetThuc())
                .build();
    }

    public static CaLamDTO toDTO(CaLam entity) {
        return CaLamDTO.builder()
                .maCa(entity.getMaCa())
                .tenCa(entity.getTenCa())
                .thoiGianBatDau(entity.getThoiGianBatDau())
                .thoiGianKetThuc(entity.getThoiGianKetThuc())
                .build();
    }

    // ============================================================
    //  KhachHang
    // ============================================================
    public static KhachHang toEntity(KhachHangDTO dto) {
        return KhachHang.builder()
                .maKH(dto.getMaKH())
                .hoTenDem(dto.getHoTenDem())
                .ten(dto.getTen())
                .sdt(dto.getSdt())
                .diemTichLuy(dto.getDiemTichLuy())
                .daXoa(dto.isDaXoa())
                .build();
    }

    public static KhachHangDTO toDTO(KhachHang entity) {
        return KhachHangDTO.builder()
                .maKH(entity.getMaKH())
                .hoTenDem(entity.getHoTenDem())
                .ten(entity.getTen())
                .sdt(entity.getSdt())
                .diemTichLuy(entity.getDiemTichLuy())
                .daXoa(entity.isDaXoa())
                .build();
    }

    // ============================================================
    //  NhanVien
    // ============================================================
    public static NhanVien toEntity(NhanVienDTO dto) {
        return NhanVien.builder()
                .maNV(dto.getMaNV())
                .hoTenDem(dto.getHoTenDem())
                .ten(dto.getTen())
                .sdt(dto.getSdt())
                .cccd(dto.getCccd())
                .gioiTinh(dto.isGioiTinh())
                .ngaySinh(dto.getNgaySinh())
                .diaChi(dto.getDiaChi())
                .nghiViec(dto.isNghiViec())
                .build();
    }

    public static NhanVienDTO toDTO(NhanVien entity) {
        return NhanVienDTO.builder()
                .maNV(entity.getMaNV())
                .hoTenDem(entity.getHoTenDem())
                .ten(entity.getTen())
                .sdt(entity.getSdt())
                .cccd(entity.getCccd())
                .gioiTinh(entity.isGioiTinh())
                .ngaySinh(entity.getNgaySinh())
                .diaChi(entity.getDiaChi())
                .nghiViec(entity.isNghiViec())
                .build();
    }

    // ============================================================
    //  TaiKhoan
    // ============================================================
    public static TaiKhoanDTO toDTO(TaiKhoan entity) {
        return TaiKhoanDTO.builder()
                .maNV(entity.getMaNV())
                .matKhau(entity.getMatKhau())
                .quanLy(entity.isQuanLy())
                .biKhoa(entity.isBiKhoa())
                .email(entity.getEmail())
                .ngayTao(entity.getNgayTao())
                .daXoa(entity.isDaXoa())
                .quanLyLo(entity.isQuanLyLo())
                .build();
    }

    /**
     * Tạo TaiKhoan entity từ DTO — NhanVien phải được set riêng sau khi gọi hàm này.
     */
    public static TaiKhoan toEntityTaiKhoan(TaiKhoanDTO dto) {
        return TaiKhoan.builder()
                .maNV(dto.getMaNV())
                .matKhau(dto.getMatKhau())
                .quanLy(dto.isQuanLy())
                .biKhoa(dto.isBiKhoa())
                .email(dto.getEmail())
                .ngayTao(dto.getNgayTao())
                .daXoa(dto.isDaXoa())
                .quanLyLo(dto.isQuanLyLo())
                .build();
    }

    // ============================================================
    //  SanPham
    // ============================================================
    public static SanPham toEntity(SanPhamDTO dto) {
        return SanPham.builder()
                .maSP(dto.getMaSP())
                .ten(dto.getTen())
                .moTa(dto.getMoTa())
                .thanhPhan(dto.getThanhPhan())
                .loaiSanPham(dto.getLoaiSanPham() != null
                        ? LoaiSanPhamEnum.valueOf(dto.getLoaiSanPham()) : null)
                .tonToiThieu(dto.getTonToiThieu())
                .tonToiDa(dto.getTonToiDa())
                .daXoa(dto.isDaXoa())
                .build();
    }

    public static SanPhamDTO toDTO(SanPham entity) {
        return SanPhamDTO.builder()
                .maSP(entity.getMaSP())
                .ten(entity.getTen())
                .moTa(entity.getMoTa())
                .thanhPhan(entity.getThanhPhan())
                .loaiSanPham(entity.getLoaiSanPham() != null
                        ? entity.getLoaiSanPham().name() : null)
                .tonToiThieu(entity.getTonToiThieu())
                .tonToiDa(entity.getTonToiDa())
                .daXoa(entity.isDaXoa())
                .build();
    }

    // ============================================================
    //  DonViTinh
    // ============================================================
    /**
     * Chú ý: sanPham phải được set riêng (load từ DB qua maSP) sau khi gọi hàm này.
     */
    public static DonViTinh toEntity(DonViTinhDTO dto) {
        return DonViTinh.builder()
                .maDonViTinh(dto.getMaDonViTinh())
                .tenDonViTinh(dto.getTenDonViTinh())
                .heSoQuyDoi(dto.getHeSoQuyDoi())
                .giaBanTheoDonVi(dto.getGiaBanTheoDonVi())
                .donViTinhCoBan(dto.isDonViTinhCoBan())
                .daXoa(dto.isDaXoa())
                .build();
    }

    public static DonViTinhDTO toDTO(DonViTinh entity) {
        return DonViTinhDTO.builder()
                .maDonViTinh(entity.getMaDonViTinh())
                .maSP(entity.getSanPham() != null ? entity.getSanPham().getMaSP() : null)
                .tenDonViTinh(entity.getTenDonViTinh())
                .heSoQuyDoi(entity.getHeSoQuyDoi())
                .giaBanTheoDonVi(entity.getGiaBanTheoDonVi())
                .donViTinhCoBan(entity.isDonViTinhCoBan())
                .daXoa(entity.isDaXoa())
                .build();
    }

    // ============================================================
    //  MaVachSanPham
    // ============================================================
    public static MaVachSanPhamDTO toDTO(MaVachSanPham entity) {
        return MaVachSanPhamDTO.builder()
                .maVach(entity.getMaVach())
                .maSP(entity.getSanPham() != null ? entity.getSanPham().getMaSP() : null)
                .build();
    }

    public static MaVachSanPham toEntity(MaVachSanPhamDTO dto) {
        return MaVachSanPham.builder()
                .maVach(dto.getMaVach())
                .build();
        // sanPham phải set riêng
    }

    // ============================================================
    //  LoSanPham
    // ============================================================
    public static LoSanPham toEntity(LoSanPhamDTO dto) {
        return LoSanPham.builder()
                .maLoSanPham(dto.getMaLoSanPham())
                .soLuong(dto.getSoLuong())
                .ngaySanXuat(dto.getNgaySanXuat())
                .ngayHetHan(dto.getNgayHetHan())
                .daHuy(dto.isDaHuy())
                .build();
        // sanPham phải set riêng
    }

    public static LoSanPhamDTO toDTO(LoSanPham entity) {
                String tenSanPham = null;
                String tenDonViTinh = null;
                String tenNhaCungCap = null;
                double giaNhap = 0D;

                if (entity.getSanPham() != null) {
                        tenSanPham = entity.getSanPham().getTen();

                        if (entity.getSanPham().getDonViTinhs() != null) {
                                DonViTinh donViTinhChon = null;
                                for (DonViTinh donViTinh : entity.getSanPham().getDonViTinhs()) {
                                        if (donViTinhChon == null || donViTinh.isDonViTinhCoBan()) {
                                                donViTinhChon = donViTinh;
                                                if (donViTinh.isDonViTinhCoBan()) {
                                                        break;
                                                }
                                        }
                                }
                                if (donViTinhChon != null) {
                                        tenDonViTinh = donViTinhChon.getTenDonViTinh();
                                }
                        }
                }

                if (entity.getChiTietPhieuNhaps() != null) {
                        for (ChiTietPhieuNhap chiTietPhieuNhap : entity.getChiTietPhieuNhaps()) {
                                if (chiTietPhieuNhap.getNhaCungCap() != null) {
                                        tenNhaCungCap = chiTietPhieuNhap.getNhaCungCap().getTenNCC();
                                }
                                giaNhap = chiTietPhieuNhap.getDonGia();
                                break;
                        }
                }

        return LoSanPhamDTO.builder()
                .maLoSanPham(entity.getMaLoSanPham())
                .maSP(entity.getSanPham() != null ? entity.getSanPham().getMaSP() : null)
                                .tenSP(tenSanPham)
                                .tenDonVi(tenDonViTinh)
                                .tenNhaCungCap(tenNhaCungCap)
                                .giaNhap(giaNhap)
                .soLuong(entity.getSoLuong())
                .ngaySanXuat(entity.getNgaySanXuat())
                .ngayHetHan(entity.getNgayHetHan())
                .daHuy(entity.isDaHuy())
                .build();
    }

    // ============================================================
    //  NhaCungCap
    // ============================================================
    public static NhaCungCap toEntity(NhaCungCapDTO dto) {
        return NhaCungCap.builder()
                .maNCC(dto.getMaNCC())
                .tenNCC(dto.getTenNCC())
                .diaChi(dto.getDiaChi())
                .sdt(dto.getSdt())
                .email(dto.getEmail())
                .daXoa(dto.isDaXoa())
                .build();
    }

    public static NhaCungCapDTO toDTO(NhaCungCap entity) {
        return NhaCungCapDTO.builder()
                .maNCC(entity.getMaNCC())
                .tenNCC(entity.getTenNCC())
                .diaChi(entity.getDiaChi())
                .sdt(entity.getSdt())
                .email(entity.getEmail())
                .daXoa(entity.isDaXoa())
                .build();
    }

    // ============================================================
    //  SanPhamCungCap
    // ============================================================
    public static SanPhamCungCapDTO toDTO(SanPhamCungCap entity) {
        return SanPhamCungCapDTO.builder()
                .id(entity.getId())
                .maSP(entity.getSanPham() != null ? entity.getSanPham().getMaSP() : null)
                .maNCC(entity.getNhaCungCap() != null ? entity.getNhaCungCap().getMaNCC() : null)
                .trangThaiHopTac(entity.isTrangThaiHopTac())
                .giaNhap(entity.getGiaNhap())
                .build();
    }

    // ============================================================
    //  KhuyenMai
    // ============================================================
    public static KhuyenMai toEntity(KhuyenMaiDTO dto) {
        return KhuyenMai.builder()
                .maKhuyenMai(dto.getMaKhuyenMai())
                .moTa(dto.getMoTa())
                .phanTram(dto.getPhanTram())
                .loaiKhuyenMai(dto.getLoaiKhuyenMai() != null
                        ? LoaiKhuyenMaiEnum.valueOf(dto.getLoaiKhuyenMai()) : null)
                .ngayBatDau(dto.getNgayBatDau())
                .ngayKetThuc(dto.getNgayKetThuc())
                .soLuongToiThieu(dto.getSoLuongToiThieu())
                .soLuongToiDa(dto.getSoLuongToiDa())
                .ngayChinhSua(dto.getNgayChinhSua())
                .daXoa(dto.isDaXoa())
                .build();
    }

    public static KhuyenMaiDTO toDTO(KhuyenMai entity) {
        return KhuyenMaiDTO.builder()
                .maKhuyenMai(entity.getMaKhuyenMai())
                .moTa(entity.getMoTa())
                .phanTram(entity.getPhanTram())
                .loaiKhuyenMai(entity.getLoaiKhuyenMai() != null
                        ? entity.getLoaiKhuyenMai().name() : null)
                .ngayBatDau(entity.getNgayBatDau())
                .ngayKetThuc(entity.getNgayKetThuc())
                .soLuongToiThieu(entity.getSoLuongToiThieu())
                .soLuongToiDa(entity.getSoLuongToiDa())
                .ngayChinhSua(entity.getNgayChinhSua())
                .daXoa(entity.isDaXoa())
                .build();
    }

    // ============================================================
    //  KhuyenMaiSanPham
    // ============================================================
    public static KhuyenMaiSanPhamDTO toDTO(KhuyenMaiSanPham entity) {
        return KhuyenMaiSanPhamDTO.builder()
                .id(entity.getId())
                .maKhuyenMai(entity.getKhuyenMai() != null ? entity.getKhuyenMai().getMaKhuyenMai() : null)
                .maSP(entity.getSanPham() != null ? entity.getSanPham().getMaSP() : null)
                .ngayChinhSua(entity.getNgayChinhSua())
                .build();
    }

    // ============================================================
    //  HoaDon
    // ============================================================
    public static HoaDonDTO toDTO(HoaDon entity) {
        return HoaDonDTO.builder()
                .maHoaDon(entity.getMaHoaDon())
                .maNV(entity.getNhanVien() != null ? entity.getNhanVien().getMaNV() : null)
                .maKH(entity.getKhachHang() != null ? entity.getKhachHang().getMaKH() : null)
                .ngayLapHoaDon(entity.getNgayLapHoaDon())
                .chuyenKhoan(entity.isChuyenKhoan())
                .tongTien(entity.getTongTien())
                .build();
    }

    // ============================================================
    //  ChiTietHoaDon
    // ============================================================
    public static ChiTietHoaDonDTO toDTO(ChiTietHoaDon entity) {
        return ChiTietHoaDonDTO.builder()
                .maChiTietHoaDon(entity.getMaChiTietHoaDon())
                .maHoaDon(entity.getHoaDon() != null ? entity.getHoaDon().getMaHoaDon() : null)
                .maDonViTinh(entity.getDonViTinh() != null ? entity.getDonViTinh().getMaDonViTinh() : null)
                .soLuong(entity.getSoLuong())
                .donGia(entity.getDonGia())
                .giamGia(entity.getGiamGia())
                .thanhTien(entity.getThanhTien())
                .build();
    }

    // ============================================================
    //  ChiTietXuatLo
    // ============================================================
    public static ChiTietXuatLoDTO toDTO(ChiTietXuatLo entity) {
        return ChiTietXuatLoDTO.builder()
                .id(entity.getId())
                .maLoSanPham(entity.getLoSanPham() != null ? entity.getLoSanPham().getMaLoSanPham() : null)
                .maChiTietHoaDon(entity.getChiTietHoaDon() != null
                        ? entity.getChiTietHoaDon().getMaChiTietHoaDon() : null)
                .soLuong(entity.getSoLuong())
                .build();
    }

    // ============================================================
    //  PhieuNhap
    // ============================================================
    public static PhieuNhapDTO toDTO(PhieuNhap entity) {
        return PhieuNhapDTO.builder()
                .maPhieuNhap(entity.getMaPhieuNhap())
                .maNV(entity.getNhanVien() != null ? entity.getNhanVien().getMaNV() : null)
                .ngayTao(entity.getNgayTao())
                .tongTien(entity.getTongTien())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    // ============================================================
    //  ChiTietPhieuNhap
    // ============================================================
    public static ChiTietPhieuNhapDTO toDTO(ChiTietPhieuNhap entity) {
        return ChiTietPhieuNhapDTO.builder()
                .id(entity.getId())
                .maPhieuNhap(entity.getPhieuNhap() != null ? entity.getPhieuNhap().getMaPhieuNhap() : null)
                .maLoSanPham(entity.getLoSanPham() != null ? entity.getLoSanPham().getMaLoSanPham() : null)
                .maNCC(entity.getNhaCungCap() != null ? entity.getNhaCungCap().getMaNCC() : null)
                .soLuong(entity.getSoLuong())
                .soLuongYeuCau(entity.getSoLuongYeuCau())
                .donGia(entity.getDonGia())
                .thanhTien(entity.getThanhTien())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    // ============================================================
    //  PhieuTraHang
    // ============================================================
    public static PhieuTraHangDTO toDTO(PhieuTraHang entity) {
        return PhieuTraHangDTO.builder()
                .maPhieuTraHang(entity.getMaPhieuTraHang())
                .maNV(entity.getNhanVien() != null ? entity.getNhanVien().getMaNV() : null)
                .maHoaDon(entity.getHoaDon() != null ? entity.getHoaDon().getMaHoaDon() : null)
                .ngayLapPhieuTraHang(entity.getNgayLapPhieuTraHang())
                .tongTienHoaTra(entity.getTongTienHoaTra())
                .build();
    }

    // ============================================================
    //  ChiTietPhieuTraHang
    // ============================================================
    public static ChiTietPhieuTraHangDTO toDTO(ChiTietPhieuTraHang entity) {
        return ChiTietPhieuTraHangDTO.builder()
                .id(entity.getId())
                .maPhieuTraHang(entity.getPhieuTraHang() != null
                        ? entity.getPhieuTraHang().getMaPhieuTraHang() : null)
                .maChiTietHoaDon(entity.getChiTietHoaDon() != null
                        ? entity.getChiTietHoaDon().getMaChiTietHoaDon() : null)
                .soLuong(entity.getSoLuong())
                .truongHopDoiTra(entity.getTruongHopDoiTra() != null
                        ? entity.getTruongHopDoiTra().name() : null)
                .tinhTrangSanPham(entity.getTinhTrangSanPham() != null
                        ? entity.getTinhTrangSanPham().name() : null)
                .giaTriHoanTra(entity.getGiaTriHoanTra())
                .thanhTienHoanTra(entity.getThanhTienHoanTra())
                .build();
    }

    // ============================================================
    //  LichSuCaLam
    // ============================================================
    public static LichSuCaLamDTO toDTO(LichSuCaLam entity) {
        return LichSuCaLamDTO.builder()
                .id(entity.getId())
                .maNV(entity.getNhanVien() != null ? entity.getNhanVien().getMaNV() : null)
                .maCa(entity.getCaLam() != null ? entity.getCaLam().getMaCa() : null)
                .ngayLamViec(entity.getNgayLamViec())
                .thoiGianVaoCa(entity.getThoiGianVaoCa())
                .thoiGianRaCa(entity.getThoiGianRaCa())
                .ghiChu(entity.getGhiChu())
                .build();
    }

    // ============================================================
    //  LichSuLo
    // ============================================================
    public static LichSuLoDTO toDTO(LichSuLo entity) {
        return LichSuLoDTO.builder()
                .maLichSuLo(entity.getMaLichSuLo())
                .maLoSanPham(entity.getLoSanPham() != null ? entity.getLoSanPham().getMaLoSanPham() : null)
                .maNV(entity.getNhanVien() != null ? entity.getNhanVien().getMaNV() : null)
                .thoiGian(entity.getThoiGian())
                .hanhDong(entity.getHanhDong())
                .soLuongSau(entity.getSoLuongSau())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}
