package server.dao;

import server.entity.ChiTietHoaDon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public int getSoCTHDCuoiCungTrongNgay(LocalDate ngay) {
        // 1. Định dạng lại ngày thành ddMMyy để khớp với CTHD-010625-xxxx
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String ngayFormatted = ngay.format(formatter);
        String prefix = "CTHD-" + ngayFormatted + "-";

        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                            "SELECT cthd.maChiTietHoaDon FROM ChiTietHoaDon cthd " +
                                    "WHERE cthd.maChiTietHoaDon LIKE :prefix " +
                                    "ORDER BY cthd.maChiTietHoaDon DESC", String.class)
                    .setParameter("prefix", prefix + "%")
                    .setMaxResults(1)
                    .getResultList();

            if (!result.isEmpty() && result.get(0) != null) {
                String maCuoi = result.get(0);
                // Cắt chuỗi từ sau dấu gạch ngang cuối cùng để lấy số thứ tự
                String soThuTuStr = maCuoi.substring(maCuoi.lastIndexOf("-") + 1);
                return Integer.parseInt(soThuTuStr);
            }
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
