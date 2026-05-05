package server.dao;

import server.entity.LoaiSanPhamEnum;
import server.entity.SanPham;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * DAO cho entity SanPham.
 */
public class SanPhamDAO extends AbstractGenericDaoImpl<SanPham, String> {

    public SanPhamDAO() {
        super(SanPham.class);
    }

    /** Lấy tất cả sản phẩm chưa bị xóa. */
    public List<SanPham> getAllTableSanPham() {
        return doInTransaction(em -> em
                .createQuery("SELECT sp FROM SanPham sp WHERE (sp.daXoa = false OR sp.daXoa IS NULL)", SanPham.class)
                .getResultList());
    }

    /** Xóa mềm sản phẩm (set daXoa = true). */
    public boolean xoaSanPham(String maSP) {
        return doInTransaction(em -> {
            SanPham sp = em.find(SanPham.class, maSP);
            if (sp == null)
                return false;
            sp.setDaXoa(true);
            em.merge(sp);
            return true;
        });
    }

    /** Cập nhật thông tin sản phẩm. */
    public boolean suaSanPham(String maSP, SanPham spNew) {
        return doInTransaction(em -> {
            SanPham sp = em.find(SanPham.class, maSP);
            if (sp == null || Boolean.TRUE.equals(sp.getDaXoa()))
                return false;
            sp.setTen(spNew.getTen());
            sp.setMoTa(spNew.getMoTa());
            sp.setThanhPhan(spNew.getThanhPhan());
            sp.setLoaiSanPham(spNew.getLoaiSanPham());
            sp.setTonToiThieu(spNew.getTonToiThieu());
            sp.setTonToiDa(spNew.getTonToiDa());
            em.merge(sp);
            return true;
        });
    }

    /** Tìm sản phẩm theo mã (chưa bị xóa). */
    public SanPham timSPTheoMa(String ma) {
        return doInTransaction(em -> {
            List<SanPham> result = em.createQuery(
                    "SELECT sp FROM SanPham sp WHERE sp.maSP = :ma AND (sp.daXoa = false OR sp.daXoa IS NULL)",
                    SanPham.class)
                    .setParameter("ma", ma)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Tìm sản phẩm theo tên (LIKE, chưa bị xóa). */
    public List<SanPham> timSPTheoTen(String tenSP) {
        return doInTransaction(em -> em
                .createQuery(
                        "SELECT sp FROM SanPham sp WHERE sp.ten LIKE :ten AND (sp.daXoa = false OR sp.daXoa IS NULL)",
                        SanPham.class)
                .setParameter("ten", "%" + tenSP + "%")
                .getResultList());
    }

    /** Tìm sản phẩm theo loại (chưa bị xóa). */
    public List<SanPham> timSPTheoLoai(LoaiSanPhamEnum loaiSP) {
        return doInTransaction(em -> em.createQuery(
                "SELECT sp FROM SanPham sp WHERE sp.loaiSanPham = :loai AND (sp.daXoa = false OR sp.daXoa IS NULL)",
                SanPham.class)
                .setParameter("loai", loaiSP)
                .getResultList());
    }

    /** Tìm sản phẩm theo mã nhà cung cấp (chưa bị xóa). */
    public List<SanPham> timSPTheoMaNCC(String maNhaCC) {
        return doInTransaction(em -> em.createQuery(
                "SELECT DISTINCT sp FROM SanPham sp " +
                        "JOIN sp.sanPhamCungCaps spcc " +
                        "WHERE spcc.nhaCungCap.maNCC = :maNCC AND (sp.daXoa = false OR sp.daXoa IS NULL)",
                SanPham.class)
                .setParameter("maNCC", maNhaCC)
                .getResultList());
    }

    /** Lấy sản phẩm đã hết hàng (tổng tồn kho = 0). */
    public List<SanPham> getSPDaHetHang() {
        return doInTransaction(em -> em.createQuery(
                "SELECT sp FROM SanPham sp JOIN sp.loSanPhams lsp " +
                        "WHERE (sp.daXoa = false OR sp.daXoa IS NULL) GROUP BY sp HAVING SUM(lsp.soLuong) = 0",
                SanPham.class)
                .getResultList());
    }

    /** Lấy sản phẩm sắp hết hàng: Map<SanPham, tồn kho hiện tại>. */
    public Map<SanPham, Integer> getSPSapHetHang() {
        return doInTransaction(em -> {
            Map<SanPham, Integer> result = new HashMap<>();
            List<Object[]> rows = em.createQuery(
                    "SELECT sp, SUM(lsp.soLuong) FROM SanPham sp JOIN sp.loSanPhams lsp " +
                            "WHERE (sp.daXoa = false OR sp.daXoa IS NULL) GROUP BY sp " +
                            "HAVING SUM(lsp.soLuong) > 0 AND SUM(lsp.soLuong) <= sp.tonToiThieu",
                    Object[].class).getResultList();
            for (Object[] row : rows)
                result.put((SanPham) row[0], ((Number) row[1]).intValue());
            return result;
        });
    }

    /** Lấy mã sản phẩm theo mã vạch. */
    public String getMaSpTheoMaVach(String maVach) {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                    "SELECT mv.sanPham.maSP FROM MaVachSanPham mv WHERE mv.maVach = :maVach", String.class)
                    .setParameter("maVach", maVach)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Lấy số thứ tự mã SP cuối cùng theo format "SP-XXXX". */
    public int getMaSPCuoiCung() {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                    "SELECT sp.maSP FROM SanPham sp", String.class)
                    .getResultList();
            int max = 0;
            for(String ma : result) {
                if(ma != null && ma.startsWith("SP-")) {
                    try {
                        int num = Integer.parseInt(ma.substring(3));
                        if(num > max) max = num;
                    } catch(Exception ignored){}
                }
            }
            return max;
        });
    }

    /**
     * Lấy sản phẩm bán chạy trong tháng: Map<SP, [SL bán, Tổng tiền, Đơn giá TB]>.
     */
    public LinkedHashMap<SanPham, Number[]> getSPBanChayTrongThang(LocalDate tg) {
        return doInTransaction(em -> {
            LinkedHashMap<SanPham, Number[]> dsSP = new LinkedHashMap<>();
            List<Object[]> rows = em.createQuery(
                    "SELECT sp, SUM(cthd.soLuong * dvt.heSoQuyDoi), SUM(cthd.soLuong * cthd.donGia), AVG(cthd.donGia) "
                            +
                            "FROM ChiTietHoaDon cthd JOIN cthd.donViTinh dvt JOIN dvt.sanPham sp JOIN cthd.hoaDon hd " +
                            "WHERE MONTH(hd.ngayLapHoaDon) = :thang AND YEAR(hd.ngayLapHoaDon) = :nam AND (sp.daXoa = false OR sp.daXoa IS NULL) "
                            +
                            "GROUP BY sp ORDER BY SUM(cthd.soLuong * dvt.heSoQuyDoi) DESC",
                    Object[].class)
                    .setParameter("thang", tg.getMonthValue())
                    .setParameter("nam", tg.getYear())
                    .getResultList();
            for (Object[] row : rows)
                dsSP.put((SanPham) row[0], new Number[] {
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).doubleValue(),
                        ((Number) row[3]).doubleValue() });
            return dsSP;
        });
    }

    /**
     * Lấy sản phẩm bán chạy trong năm: Map<SP, [SL bán, Tổng tiền, Đơn giá TB]>.
     */
    public LinkedHashMap<SanPham, Number[]> getSPBanChayTrongNam(LocalDate tg) {
        return doInTransaction(em -> {
            LinkedHashMap<SanPham, Number[]> dsSP = new LinkedHashMap<>();
            List<Object[]> rows = em.createQuery(
                    "SELECT sp, SUM(cthd.soLuong * dvt.heSoQuyDoi), SUM(cthd.soLuong * cthd.donGia), AVG(cthd.donGia) "
                            +
                            "FROM ChiTietHoaDon cthd JOIN cthd.donViTinh dvt JOIN dvt.sanPham sp JOIN cthd.hoaDon hd " +
                            "WHERE YEAR(hd.ngayLapHoaDon) = :nam AND (sp.daXoa = false OR sp.daXoa IS NULL) " +
                            "GROUP BY sp ORDER BY SUM(cthd.soLuong * dvt.heSoQuyDoi) DESC",
                    Object[].class)
                    .setParameter("nam", tg.getYear())
                    .getResultList();
            for (Object[] row : rows)
                dsSP.put((SanPham) row[0], new Number[] {
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).doubleValue(),
                        ((Number) row[3]).doubleValue() });
            return dsSP;
        });
    }
}
