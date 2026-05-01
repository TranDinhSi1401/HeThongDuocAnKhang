package server.dao;

import server.entity.MaVachSanPham;

import java.util.List;

/**
 * DAO cho entity MaVachSanPham.
 */
public class MaVachSanPhamDAO extends AbstractGenericDaoImpl<MaVachSanPham, String> {

    public MaVachSanPhamDAO() {
        super(MaVachSanPham.class);
    }

    /** Lấy tất cả mã vạch của một sản phẩm. */
    public List<MaVachSanPham> getMaVachTheoMaSP(String maSP) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT mv FROM MaVachSanPham mv WHERE mv.sanPham.maSP = :maSP", MaVachSanPham.class)
              .setParameter("maSP", maSP)
              .getResultList()
        );
    }

    /** Xóa tất cả mã vạch của một sản phẩm. */
    public boolean xoaMaVachTheoMaSP(String maSP) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "DELETE FROM MaVachSanPham mv WHERE mv.sanPham.maSP = :maSP")
                .setParameter("maSP", maSP)
                .executeUpdate();
            return rows > 0;
        });
    }
}
