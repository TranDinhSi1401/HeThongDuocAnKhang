package server.dao;

import server.entity.SanPhamCungCap;

import java.util.List;

/**
 * DAO cho entity SanPhamCungCap.
 */
public class SanPhamCungCapDAO extends AbstractGenericDaoImpl<SanPhamCungCap, Long> {

    public SanPhamCungCapDAO() {
        super(SanPhamCungCap.class);
    }

    /** Lấy tất cả sản phẩm cung cấp theo mã nhà cung cấp. */
    public List<SanPhamCungCap> getTheoMaNCC(String maNCC) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT spcc FROM SanPhamCungCap spcc WHERE spcc.nhaCungCap.maNCC = :maNCC",
                SanPhamCungCap.class)
              .setParameter("maNCC", maNCC)
              .getResultList()
        );
    }

    /** Lấy tất cả nhà cung cấp của một sản phẩm. */
    public List<SanPhamCungCap> getTheoMaSP(String maSP) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT spcc FROM SanPhamCungCap spcc WHERE spcc.sanPham.maSP = :maSP",
                SanPhamCungCap.class)
              .setParameter("maSP", maSP)
              .getResultList()
        );
    }

    /** Cập nhật trạng thái hợp tác và giá nhập. */
    public boolean sua(Long id, SanPhamCungCap spccNew) {
        return doInTransaction(em -> {
            SanPhamCungCap spcc = em.find(SanPhamCungCap.class, id);
            if (spcc == null) return false;
            spcc.setTrangThaiHopTac(spccNew.isTrangThaiHopTac());
            spcc.setGiaNhap(spccNew.getGiaNhap());
            em.merge(spcc);
            return true;
        });
    }
}
