package server.service;

import common.dto.*;
import server.dao.*;
import server.entity.*;
import server.mapper.EntityMapper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    // ===== HoaDon =====

    public HoaDonDTO getHoaDonMoiNhatTrongNgay() {
        HoaDon hd = hoaDonDAO.getHoaDonMoiNhatTrongNgay();
        return hd != null ? EntityMapper.toDTO(hd) : null;
    }

    public HoaDonDTO getHoaDonTheoMaHD(String maHD) {
        HoaDon hd = hoaDonDAO.getHoaDonTheoMaHD(maHD);
        return hd != null ? EntityMapper.toDTO(hd) : null;
    }

    public List<HoaDonDTO> getAllHoaDon() {
        return hoaDonDAO.loadAll()
                .stream().map(EntityMapper::toDTO).collect(Collectors.toList());
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

    public int getSoHDCuoiCungTrongNgay(LocalDate ngay) {
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

    private static int parseIntFromTable(Object value) {
        if (value == null) return 0;
        String str = value.toString()
                .replaceAll("[^0-9-]", "")  // Loại bỏ mọi ký tự không phải số hoặc dấu âm
                .trim();
        if (str.isEmpty()) return 0;
        return Integer.parseInt(str);
    }

    private static double parseDoubleFromTable(Object value) {
        if (value == null) return 0.0;
        String str = value.toString()
                .replaceAll("[^0-9.]", "")  // Chỉ giữ số và dấu chấm
                .trim();
        if (str.isEmpty()) return 0.0;

        // Xử lý dấu chấm: loại bỏ tất cả dấu chấm ngoại trừ dấu chấm cuối (nếu có)
        // Pattern: nếu có dấu chấm ở vị trí thứ n từ cuối với đúng 3 chữ số, đó là dấu phân cách
        int countDot = (int) str.chars().filter(c -> c == '.').count();

        if (countDot == 0) {
            // Không có dấu chấm → là số nguyên
            return Double.parseDouble(str);
        } else if (countDot == 1) {
            // Một dấu chấm: có thể là dấu thập phân hoặc dấu phân cách
            int dotIndex = str.indexOf('.');
            int digitsAfterDot = str.length() - dotIndex - 1;

            if (digitsAfterDot == 3) {
                // 3 chữ số sau dấu chấm → là dấu phân cách, loại bỏ nó
                str = str.replace(".", "");
            }
            // Còn lại là dấu thập phân, giữ nguyên
        } else {
            // Nhiều dấu chấm: đó là dấu phân cách hàng nghìn, loại bỏ tất cả trừ dấu chấm cuối
            // Ví dụ: "1.234.567,89" → giữ dấu chấm cuối nếu 2 chữ số sau, nếu không loại bỏ tất cả
            int lastDotIndex = str.lastIndexOf('.');
            int digitsAfterLastDot = str.length() - lastDotIndex - 1;

            if (digitsAfterLastDot == 3) {
                // Dấu chấm cuối là dấu phân cách → loại bỏ tất cả dấu chấm
                str = str.replaceAll("\\.", "");
            } else if (digitsAfterLastDot == 2 || digitsAfterLastDot == 1) {
                // Dấu chấm cuối là dấu thập phân → loại bỏ các dấu chấm khác
                str = str.substring(0, lastDotIndex).replaceAll("\\.", "") + str.substring(lastDotIndex);
            } else {
                // Loại bỏ tất cả dấu chấm
                str = str.replaceAll("\\.", "");
            }
        }

        return Double.parseDouble(str);
    }

    private String taoMaHoaDonMoi(LocalDate ngay, int soThuTu) {
        return String.format("HD-%s-%04d",
                ngay.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy")),
                soThuTu);
    }

    private String taoMaChiTietHoaDonMoi(LocalDate ngay, int soThuTu) {
        return String.format("CTHD-%s-%04d",
                ngay.format(java.time.format.DateTimeFormatter.ofPattern("ddMMyy")),
                soThuTu);
    }

    public synchronized Object thanhToan(List<Map<String, Object>> dsSP, String maKH, boolean chuyenKhoan, double tongTien, TaiKhoanDTO taiKhoan, double tienKhachDua, double tienThua) throws Exception {
            try {
                Map<String, Integer> tongYeuCauTheoSP = new HashMap<>();
                DonViTinhDAO donViTinhDAO = new DonViTinhDAO();
                LoSanPhamDAO loSanPhamDAO = new LoSanPhamDAO();

                for (Map<String, Object> item : dsSP) {
                    String maSP = item.get("maSP").toString();
                    String maDVT = item.get("maDVT").toString();

                    DonViTinh dvt = donViTinhDAO.findById(maDVT);
                    int heSoQuyDoi = dvt.getHeSoQuyDoi();
                    int soLuongYeuCau = parseIntFromTable(item.get("soLuong").toString()) * heSoQuyDoi;

                    // cộng dồn số lượng nếu trùng mã sản phẩm
                    tongYeuCauTheoSP.merge(maSP, soLuongYeuCau, Integer::sum);
                }

                for (Map.Entry<String, Integer> entry : tongYeuCauTheoSP.entrySet()) {
                    String maSP = entry.getKey();
                    int tongYeuCau = entry.getValue();

                    int tongTon = loSanPhamDAO.dsLoTheoMaSanPham(maSP)
                            .stream()
                            .mapToInt(LoSanPham::getSoLuong)
                            .sum();

                    if (tongYeuCau > tongTon) {
                        throw new Exception("Không đủ hàng cho sản phẩm " + maSP
                                + " (Yêu cầu: " + tongYeuCau + ", Tồn: " + tongTon + ")");
                    }
                }

                // Lấy mã hóa đơn mới nhất
                int soHDMoiNhat = hoaDonDAO.getSoHDCuoiCungTrongNgay(LocalDate.now());
                LocalDateTime now = LocalDateTime.now();
                String maHDMoi;

                if (soHDMoiNhat == 0) {
                    maHDMoi = taoMaHoaDonMoi(now.toLocalDate(), 1);
                } else {
                    maHDMoi = taoMaHoaDonMoi(now.toLocalDate(), soHDMoiNhat + 1);
                }

                // Tạo hóa đơn
                String maNV = taiKhoan.getMaNV();
                LocalDateTime ngayLapHD = LocalDateTime.now();
                HoaDon hd = HoaDon.builder()
                        .maHoaDon(maHDMoi)
                        .nhanVien(NhanVien.builder().maNV(maNV).build())
                        .khachHang(KhachHang.builder().maKH(maKH).build())
                        .ngayLapHoaDon(ngayLapHD)
                        .chuyenKhoan(chuyenKhoan)
                        .tongTien(tongTien)
                        .build();

                if (hoaDonDAO.create(hd) == null) {
                    throw new Exception("Tạo hóa đơn thất bại");
                }

                // Tạo danh sách chi tiết hóa đơn
                ArrayList<ChiTietHoaDon> dsCTHD = new ArrayList<>();
                int soCTHDMoiNhat = chiTietHoaDonDAO.getSoCTHDCuoiCungTrongNgay(LocalDate.now()) + 1;
                for (Map<String, Object> item : dsSP) {
                    String maDVT = item.get("maDVT").toString();
                    int heSoQuyDoi = donViTinhDAO.findById(maDVT).getHeSoQuyDoi();
                    int soLuong = parseIntFromTable(item.get("soLuong").toString());
                    double donGia = parseDoubleFromTable(item.get("donGia").toString());
                    double giamGia = parseDoubleFromTable(item.get("giamGia").toString());
                    double thanhTien = parseDoubleFromTable(item.get("thanhTien").toString());

                    String maSP = item.get("maSP").toString();


                    String maCTHDMoi;
                    if (soCTHDMoiNhat == 0) {
                        maCTHDMoi = taoMaChiTietHoaDonMoi(now.toLocalDate(), 1);
                    } else {
                        maCTHDMoi = taoMaChiTietHoaDonMoi(now.toLocalDate(), soCTHDMoiNhat++);
                    }

                    ChiTietHoaDon cthd = ChiTietHoaDon.builder()
                            .maChiTietHoaDon(maCTHDMoi)
                            .hoaDon(HoaDon.builder().maHoaDon(maHDMoi).build())
                            .donViTinh(DonViTinh.builder().maDonViTinh(maDVT).build())
                            .soLuong(soLuong)
                            .donGia(donGia)
                            .giamGia(giamGia)
                            .thanhTien(thanhTien)
                            .build();
                    dsCTHD.add(cthd);
                    if (null == chiTietHoaDonDAO.create(cthd)) {
                        throw new Exception("Tạo chi tiết hóa đơn thất bại");
                    }

                    // Trừ tồn kho và tạo chi tiết xuất lô
                    int soLuongXuat = soLuong * heSoQuyDoi;
                    List<LoSanPham> dsLSP = loSanPhamDAO.getLoSanPhamTheoMaSP(maSP);
                    for (LoSanPham lsp : dsLSP) {
                        int soLuongTon = lsp.getSoLuong();
                        if (soLuongXuat <= 0)
                            break;

                        if (soLuongTon >= soLuongXuat) {
                            loSanPhamDAO.truSoLuong(lsp.getMaLoSanPham(), soLuongXuat);
                            //ChiTietXuatLo ctxl = new ChiTietXuatLo(new LoSanPham(lsp.getMaLoSanPham()), new ChiTietHoaDon(maCTHDMoi), soLuongXuat);
                            ChiTietXuatLo ctxl = ChiTietXuatLo.builder()
                                    .loSanPham(LoSanPham.builder().maLoSanPham(lsp.getMaLoSanPham()).build())
                                    .chiTietHoaDon(ChiTietHoaDon.builder().maChiTietHoaDon(maCTHDMoi).build())
                                    .soLuong(soLuongXuat)
                                    .build();
                            chiTietXuatLoDAO.create(ctxl);

                            soLuongXuat = 0;
                        } else {
                            loSanPhamDAO.truSoLuong(lsp.getMaLoSanPham(), soLuongTon);
                            //ChiTietXuatLo ctxl = new ChiTietXuatLo(new LoSanPham(lsp.getMaLoSanPham()), new ChiTietHoaDon(maCTHDMoi), soLuongTon);
                            ChiTietXuatLo ctxl = ChiTietXuatLo.builder()
                                    .loSanPham(LoSanPham.builder().maLoSanPham(lsp.getMaLoSanPham()).build())
                                    .chiTietHoaDon(ChiTietHoaDon.builder().maChiTietHoaDon(maCTHDMoi).build())
                                    .soLuong(soLuongTon)
                                    .build();
                            chiTietXuatLoDAO.create(ctxl);
                            soLuongXuat -= soLuongTon;
                        }
                    }

                    if (soLuongXuat > 0) {
                        throw new Exception("Không đủ số lượng");
                    }
                }

                // Cập nhật điểm tích lũy cho khách hàng mua lớn hơn bằng 1 ngàn
                if (tongTien >= 1000 && !"KH-99999".equals(maKH)) {
                    int diemTichLuy = (int) Math.floor(tongTien / 1000);
                    khachHangDAO.updateDiemTichLuy(diemTichLuy, maKH);
                }
                System.out.println("Thanh toán thành công. Mã hóa đơn: " + maHDMoi);
                String noiDung = taoNoiDungHoaDon(hd, dsCTHD, tienKhachDua, tienThua);
                return new Object[] {true, noiDung};
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new Object[] {false, e.getMessage()};
            }
    }

    public static String taoNoiDungHoaDon(HoaDon hd, ArrayList<ChiTietHoaDon> dsCTHD, double tienKhachDua, double tienThua) {
        int WIDTH = 120;
        String LINE = "=".repeat(WIDTH);
        String SEPARATOR = "-".repeat(WIDTH);

        StringBuilder sb = new StringBuilder();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DecimalFormat df = new DecimalFormat("#,###");


        sb.append(LINE).append("\n");
        sb.append(center("HÓA ĐƠN BÁN HÀNG", WIDTH)).append("\n");
        sb.append(LINE).append("\n");

        sb.append(String.format("Mã hóa đơn : %s\n", hd.getMaHoaDon()));
        sb.append(String.format("Ngày lập   : %s\n", hd.getNgayLapHoaDon().format(fmt)));
        sb.append(String.format("Nhân viên  : %s\n", hd.getNhanVien().getMaNV()));
        sb.append(String.format("Khách hàng : %s\n", hd.getKhachHang().getMaKH()));
        sb.append(String.format("Hình thức  : %s\n", Boolean.TRUE.equals(hd.getChuyenKhoan()) ? "Chuyển khoản" : "Tiền mặt"));

        sb.append("\n").append(SEPARATOR).append("\n");

        // Giảm độ rộng cột cho phù hợp chiều ngang 120 ký tự
        sb.append(String.format("%-4s %-55s %-8s %-15s %-12s %-10s %-12s\n",
                "STT", "Sản phẩm", "SL", "ĐVT", "Đơn giá", "Giảm giá", "Thành tiền"));

        sb.append(SEPARATOR).append("\n");

        int stt = 1;
        double tongTien = 0;

        DonViTinhDAO donViTinhDAO = new DonViTinhDAO();
        SanPhamDAO sanPhamDAO = new SanPhamDAO();

        for (ChiTietHoaDon cthd : dsCTHD) {

            String maSP = donViTinhDAO.findById(cthd.getDonViTinh().getMaDonViTinh()).getSanPham().getMaSP();
            String tenSP = sanPhamDAO.findById(maSP).getTen();
            String tenDVT = donViTinhDAO.findById(cthd.getDonViTinh().getMaDonViTinh()).getTenDonViTinh();

            String giamStr = String.format("%.0f%%", cthd.getGiamGia());

            double thanhTien = cthd.getThanhTien();
            tongTien += thanhTien;

            if (tenSP.length() > 55)
                tenSP = tenSP.substring(0, 55); // tránh tràn dòng

            sb.append(String.format(
                    "%-4d %-55s %-8d %-15s %-12s %-10s %-12s\n",
                    stt++, tenSP, cthd.getSoLuong(), tenDVT,
                    df.format(cthd.getDonGia()), giamStr, df.format(thanhTien)
            ));
        }

        sb.append(SEPARATOR).append("\n");
        String tongCongStr = "TỔNG CỘNG: " + df.format(tongTien) + " VND";
        sb.append(alignRight(tongCongStr, WIDTH)).append("\n");
        if(Boolean.TRUE.equals(hd.getChuyenKhoan())) {
            String tienKhachDuaStr = "KHÁCH CHUYỂN KHOẢN: " + df.format(tongTien) + " VND";
            sb.append(alignRight(tienKhachDuaStr, WIDTH)).append("\n");
            sb.append(LINE).append("\n");
        } else {
            String tienKhachDuaStr = "KHÁCH ĐƯA: " + df.format(tienKhachDua) + " VND";
            sb.append(alignRight(tienKhachDuaStr, WIDTH)).append("\n");
            String tienThuaStr = "TIỀN THỪA: " + df.format(tienThua) + " VND";
            sb.append(alignRight(tienThuaStr, WIDTH)).append("\n");
            sb.append(LINE).append("\n");
        }

        sb.append(center("CẢM ƠN QUÝ KHÁCH, HẸN GẶP LẠI!", WIDTH)).append("\n");

        sb.append(LINE).append("\n");

        return sb.toString();
    }


    private static String center(String text, int width) {
        int padSize = (width - text.length()) / 2;
        return " ".repeat(Math.max(0, padSize)) + text;
    }

    private static String alignRight(String text, int width) {
        int padding = width - text.length();
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + text;
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

    public List<DoanhThu> getDoanhThuTungNgayTrongKhoangThoiGian(LocalDate begin, LocalDate end) {
        return hoaDonDAO.getDoanhThuTungNgayTrongKhoangThoiGian(begin, end);
    }

    public List<DoanhThu> getDoanhThuTungThangTrongNam(int nam) {
        return hoaDonDAO.getDoanhThuTungThangTrongNam(nam);
    }

    public List<DoanhThu> getDoanhThuTungQuyTrongNam(int nam) {
        return hoaDonDAO.getDoanhThuTungQuyTrongNam(nam);
    }

    public List<DoanhThu> getDoanhThuTungNamTheoKhoang(int namBatDau, int namKetThuc) {
        return hoaDonDAO.getDoanhThuTungNamTheoKhoang(namBatDau, namKetThuc);
    }

}
