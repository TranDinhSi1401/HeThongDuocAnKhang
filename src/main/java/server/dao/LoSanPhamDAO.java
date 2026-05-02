package server.dao;

import server.entity.LoSanPham;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho entity LoSanPham.
 */
public class LoSanPhamDAO extends AbstractGenericDaoImpl<LoSanPham, String> {

    public LoSanPhamDAO() {
        super(LoSanPham.class);
    }

    /** Lấy lô theo mã SP (còn hạn, còn hàng, sắp xếp ASC theo ngày hết hạn). */
    public List<LoSanPham> getLoSanPhamTheoMaSP(String maSP) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT lsp FROM LoSanPham lsp WHERE lsp.sanPham.maSP = :maSP " +
                "AND lsp.ngayHetHan > CURRENT_DATE AND lsp.soLuong > 0 ORDER BY lsp.ngayHetHan ASC",
                LoSanPham.class)
              .setParameter("maSP", maSP)
              .getResultList()
        );
    }

    /** Tìm lô theo mã lô. */
    public LoSanPham timLoSanPham(String maLo) {
        return findById(maLo);
    }

    /** Tìm lô sản phẩm theo mã chi tiết hóa đơn. */
    public LoSanPham getLoSanPhamTheoMaCTHD(String maChiTietHoaDon) {
        return doInTransaction(em -> {
            List<LoSanPham> result = em.createQuery(
                "SELECT ctxl.loSanPham FROM ChiTietXuatLo ctxl " +
                "WHERE ctxl.chiTietHoaDon.maChiTietHoaDon = :maCTHD", LoSanPham.class)
                .setParameter("maCTHD", maChiTietHoaDon)
                .setMaxResults(1)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Trừ số lượng lô (khi xuất hàng). */
    public boolean truSoLuong(String maLo, int soLuong) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE LoSanPham lsp SET lsp.soLuong = lsp.soLuong - :sl WHERE lsp.maLoSanPham = :maLo")
                .setParameter("sl", soLuong)
                .setParameter("maLo", maLo)
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Cộng số lượng lô (khi trả hàng), nhân theo hệ số quy đổi. */
    public boolean congSoLuong(String maLo, int soLuong, int heSoQuyDoi) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE LoSanPham lsp SET lsp.soLuong = lsp.soLuong + :sl WHERE lsp.maLoSanPham = :maLo")
                .setParameter("sl", soLuong * heSoQuyDoi)
                .setParameter("maLo", maLo)
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Cập nhật số lượng lô (set = soLuong hiện tại + slDat). */
    public boolean capNhatSoLuongLo(LoSanPham lo, int slDat) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE LoSanPham lsp SET lsp.soLuong = :slMoi WHERE lsp.maLoSanPham = :maLo")
                .setParameter("slMoi", lo.getSoLuong() + slDat)
                .setParameter("maLo", lo.getMaLoSanPham())
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Hủy lô sản phẩm (set daHuy = true). */
    public boolean huyLoSanPham(LoSanPham lo) {
        return doInTransaction(em -> {
            LoSanPham lsp = em.find(LoSanPham.class, lo.getMaLoSanPham());
            if (lsp == null) return false;
            lsp.setDaHuy(true);
            em.merge(lsp);
            return true;
        });
    }

    /** Lấy danh sách lô theo mã SP (tất cả trạng thái). */
    public List<LoSanPham> dsLoTheoMaSanPham(String maSP) {
        return doInTransaction(em ->
            em.createQuery("SELECT lsp FROM LoSanPham lsp WHERE lsp.sanPham.maSP = :maSP", LoSanPham.class)
              .setParameter("maSP", maSP)
              .getResultList()
        );
    }

    /** Lấy lô theo mã nhà cung cấp (qua ChiTietPhieuNhap). */
    public List<LoSanPham> getLoSPTheoMaNhaCungCap(String ma) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT DISTINCT ctpn.loSanPham FROM ChiTietPhieuNhap ctpn WHERE ctpn.nhaCungCap.maNCC = :maNCC",
                LoSanPham.class)
              .setParameter("maNCC", ma)
              .getResultList()
        );
    }

    /** Lấy danh sách lô đã xuất theo mã CTHD (kèm soLuong đã xuất). */
    public List<LoSanPham> getDanhSachLoDaXuatTheoMaCTHD(String maCTHD) {
        return doInTransaction(em -> {
            List<Object[]> rows = em.createQuery(
                "SELECT ctxl.loSanPham.maLoSanPham, ctxl.soLuong FROM ChiTietXuatLo ctxl " +
                "WHERE ctxl.chiTietHoaDon.maChiTietHoaDon = :maCTHD ORDER BY ctxl.loSanPham.ngayHetHan DESC",
                Object[].class)
                .setParameter("maCTHD", maCTHD)
                .getResultList();
            List<LoSanPham> dsLo = new ArrayList<>();
            for (Object[] row : rows) {
                LoSanPham lo = new LoSanPham();
                lo.setMaLoSanPham((String) row[0]);
                lo.setSoLuong(((Number) row[1]).intValue());
                dsLo.add(lo);
            }
            return dsLo;
        });
    }

    /** Đếm lô theo 4 trạng thái: [0]=Còn hạn, [1]=Sắp hết hạn, [2]=Hết hạn, [3]=Đã hủy. */
    public int[] demLoTheoTrangThai() {
        return doInTransaction(em -> {
            int[] counts = new int[4];
            LocalDate plus30 = LocalDate.now().plusDays(30);
            counts[3] = ((Number) em.createQuery(
                "SELECT COUNT(lsp) FROM LoSanPham lsp WHERE lsp.daHuy = true").getSingleResult()).intValue();
            counts[2] = ((Number) em.createQuery(
                "SELECT COUNT(lsp) FROM LoSanPham lsp WHERE lsp.daHuy = false AND lsp.ngayHetHan < CURRENT_DATE")
                .getSingleResult()).intValue();
            counts[1] = ((Number) em.createQuery(
                "SELECT COUNT(lsp) FROM LoSanPham lsp WHERE lsp.daHuy = false " +
                "AND lsp.ngayHetHan >= CURRENT_DATE AND lsp.ngayHetHan <= :ngay30")
                .setParameter("ngay30", plus30).getSingleResult()).intValue();
            counts[0] = ((Number) em.createQuery(
                "SELECT COUNT(lsp) FROM LoSanPham lsp WHERE lsp.daHuy = false AND lsp.ngayHetHan > :ngay30")
                .setParameter("ngay30", plus30).getSingleResult()).intValue();
            return counts;
        });
    }
}
