package server.dao;

import server.entity.ChiTietPhieuNhap;

import java.util.List;

/**
 * DAO cho entity ChiTietPhieuNhap.
 */
public class ChiTietPhieuNhapDAO extends AbstractGenericDaoImpl<ChiTietPhieuNhap, Long> {

    public ChiTietPhieuNhapDAO() {
        super(ChiTietPhieuNhap.class);
    }

    /** Lấy tất cả chi tiết phiếu nhập theo mã phiếu nhập. */
    public List<ChiTietPhieuNhap> getTheoMaPhieuNhap(String maPhieuNhap) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctpn FROM ChiTietPhieuNhap ctpn WHERE ctpn.phieuNhap.maPhieuNhap = :maPNhap",
                ChiTietPhieuNhap.class)
              .setParameter("maPNhap", maPhieuNhap)
              .getResultList()
        );
    }

    /** Lấy chi tiết phiếu nhập theo mã lô sản phẩm. */
    public List<ChiTietPhieuNhap> getTheoMaLo(String maLoSanPham) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctpn FROM ChiTietPhieuNhap ctpn WHERE ctpn.loSanPham.maLoSanPham = :maLo",
                ChiTietPhieuNhap.class)
              .setParameter("maLo", maLoSanPham)
              .getResultList()
        );
    }

    /** Lấy chi tiết phiếu nhập theo mã nhà cung cấp. */
    public List<ChiTietPhieuNhap> getTheoMaNCC(String maNCC) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ctpn FROM ChiTietPhieuNhap ctpn WHERE ctpn.nhaCungCap.maNCC = :maNCC",
                ChiTietPhieuNhap.class)
              .setParameter("maNCC", maNCC)
              .getResultList()
        );
    }
}
