package server.dao;

import server.entity.ChiTietXuatLo;

import java.util.List;

/**
 * DAO cho entity ChiTietXuatLo.
 */
public class ChiTietXuatLoDAO extends AbstractGenericDaoImpl<ChiTietXuatLo, Long> {

    public ChiTietXuatLoDAO() {
        super(ChiTietXuatLo.class);
    }

    /** Lấy danh sách xuất lô theo mã chi tiết hóa đơn. */
    public List<ChiTietXuatLo> getTheoMaCTHD(String maChiTietHoaDon) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctxl FROM ChiTietXuatLo ctxl " +
                "WHERE ctxl.chiTietHoaDon.maChiTietHoaDon = :maCTHD",
                ChiTietXuatLo.class)
              .setParameter("maCTHD", maChiTietHoaDon)
              .getResultList()
        );
    }

    /** Lấy danh sách xuất lô theo mã lô sản phẩm. */
    public List<ChiTietXuatLo> getTheoMaLo(String maLoSanPham) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctxl FROM ChiTietXuatLo ctxl " +
                "WHERE ctxl.loSanPham.maLoSanPham = :maLo",
                ChiTietXuatLo.class)
              .setParameter("maLo", maLoSanPham)
              .getResultList()
        );
    }
}
