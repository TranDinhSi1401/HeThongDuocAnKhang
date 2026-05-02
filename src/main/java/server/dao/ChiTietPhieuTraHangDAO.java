package server.dao;

import server.entity.ChiTietPhieuTraHang;

import java.util.List;

/**
 * DAO cho entity ChiTietPhieuTraHang.
 */
public class ChiTietPhieuTraHangDAO extends AbstractGenericDaoImpl<ChiTietPhieuTraHang, Long> {

    public ChiTietPhieuTraHangDAO() {
        super(ChiTietPhieuTraHang.class);
    }

    /** Lấy tất cả chi tiết phiếu trả hàng theo mã phiếu trả. */
    public List<ChiTietPhieuTraHang> getTheoMaPhieuTra(String maPhieuTraHang) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctpth FROM ChiTietPhieuTraHang ctpth " +
                "WHERE ctpth.phieuTraHang.maPhieuTraHang = :maPTH",
                ChiTietPhieuTraHang.class)
              .setParameter("maPTH", maPhieuTraHang)
              .getResultList()
        );
    }

    /** Lấy chi tiết phiếu trả theo mã chi tiết hóa đơn. */
    public List<ChiTietPhieuTraHang> getTheoMaCTHD(String maChiTietHoaDon) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctpth FROM ChiTietPhieuTraHang ctpth " +
                "WHERE ctpth.chiTietHoaDon.maChiTietHoaDon = :maCTHD",
                ChiTietPhieuTraHang.class)
              .setParameter("maCTHD", maChiTietHoaDon)
              .getResultList()
        );
    }
}
