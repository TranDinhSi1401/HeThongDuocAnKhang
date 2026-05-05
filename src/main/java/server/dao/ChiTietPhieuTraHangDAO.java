package server.dao;

import server.entity.ChiTietHoaDon;
import server.entity.ChiTietPhieuTraHang;
import server.entity.PhieuTraHang;

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

    // Trong server/dao/ChiTietPhieuTraHangDAO.java
    public boolean createWithRefs(ChiTietPhieuTraHang ct, String maPTH, String maCTHD) {
        return doInTransaction(em -> {
            // getReference trả proxy managed trong EM này, không query DB
            ct.setPhieuTraHang(em.getReference(PhieuTraHang.class, maPTH));
            ct.setChiTietHoaDon(em.getReference(ChiTietHoaDon.class, maCTHD));
            em.persist(ct);
            return true;
        });
    }

    public List<ChiTietPhieuTraHang> getChiTietPhieuTraHangTheoMaPTH(String maPTH) { return getTheoMaPhieuTra(maPTH); }
}
