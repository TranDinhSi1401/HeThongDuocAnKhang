package server.dao;

import server.entity.KhuyenMaiSanPham;

import java.util.List;

/**
 * DAO cho entity KhuyenMaiSanPham.
 */
public class KhuyenMaiSanPhamDAO extends AbstractGenericDaoImpl<KhuyenMaiSanPham, Long> {

    public KhuyenMaiSanPhamDAO() {
        super(KhuyenMaiSanPham.class);
    }

    /** Lấy tất cả sản phẩm trong một chương trình khuyến mãi. */
    public List<KhuyenMaiSanPham> getTheoMaKM(String maKM) {
        return doInTransaction(em -> em.createQuery(
                "SELECT kmsp FROM KhuyenMaiSanPham kmsp WHERE kmsp.khuyenMai.maKhuyenMai = :maKM",
                KhuyenMaiSanPham.class)
                .setParameter("maKM", maKM)
                .getResultList());
    }

    /** Lấy tất cả khuyến mãi của một sản phẩm. */
    public List<KhuyenMaiSanPham> getTheoMaSP(String maSP) {
        return doInTransaction(em -> em.createQuery(
                "SELECT kmsp FROM KhuyenMaiSanPham kmsp WHERE kmsp.sanPham.maSP = :maSP",
                KhuyenMaiSanPham.class)
                .setParameter("maSP", maSP)
                .getResultList());
    }

    /** Xóa tất cả khuyến mãi sản phẩm theo mã khuyến mãi. */
    public boolean xoaTheoMaKM(String maKM) {
        return doInTransaction(em -> {
            em.createQuery(
                    "DELETE FROM KhuyenMaiSanPham kmsp WHERE kmsp.khuyenMai.maKhuyenMai = :maKM")
                    .setParameter("maKM", maKM)
                    .executeUpdate();
            return true;
        });
    }
}
