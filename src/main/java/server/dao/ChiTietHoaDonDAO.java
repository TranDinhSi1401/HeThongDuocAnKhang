package server.dao;

import server.entity.ChiTietHoaDon;

import java.util.List;

/**
 * DAO cho entity ChiTietHoaDon.
 */
public class ChiTietHoaDonDAO extends AbstractGenericDaoImpl<ChiTietHoaDon, String> {

    public ChiTietHoaDonDAO() {
        super(ChiTietHoaDon.class);
    }

    /** Lấy tất cả chi tiết hóa đơn theo mã hóa đơn. */
    public List<ChiTietHoaDon> getChiTietHoaDonTheoMaHoaDon(String maHoaDon) {
        return getCTHDTheoMaHoaDon(maHoaDon);
    }

    public List<ChiTietHoaDon> getCTHDTheoMaHoaDon(String maHoaDon) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT cthd FROM ChiTietHoaDon cthd WHERE cthd.hoaDon.maHoaDon = :maHD",
                ChiTietHoaDon.class)
              .setParameter("maHD", maHoaDon)
              .getResultList()
        );
    }

    /** Lấy chi tiết hóa đơn theo mã đơn vị tính. */
    public List<ChiTietHoaDon> getCTHDTheoMaDVT(String maDVT) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT cthd FROM ChiTietHoaDon cthd WHERE cthd.donViTinh.maDonViTinh = :maDVT",
                ChiTietHoaDon.class)
              .setParameter("maDVT", maDVT)
              .getResultList()
        );
    }

    /** Lấy số thứ tự CTHD cuối cùng trong ngày theo format "CTHD-YYYYMMDD-XXX". */
    public int getSoCTHDCuoiCungTrongNgay(String ngay) {
        return doInTransaction(em -> {
            String prefix = "CTHD-" + ngay + "-";
            List<String> result = em.createQuery(
                "SELECT cthd.maChiTietHoaDon FROM ChiTietHoaDon cthd " +
                "WHERE cthd.maChiTietHoaDon LIKE :prefix ORDER BY cthd.maChiTietHoaDon DESC", String.class)
                .setParameter("prefix", prefix + "%")
                .setMaxResults(1)
                .getResultList();
            if (!result.isEmpty() && result.get(0) != null)
                return Integer.parseInt(result.get(0).substring(prefix.length()));
            return 0;
        });
    }

    /** Xóa tất cả chi tiết hóa đơn theo mã hóa đơn. */
    public boolean xoaChiTietHoaDonTheoMaHD(String maHoaDon) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "DELETE FROM ChiTietHoaDon cthd WHERE cthd.hoaDon.maHoaDon = :maHD")
                .setParameter("maHD", maHoaDon)
                .executeUpdate();
            return rows >= 0;
        });
    }
}
