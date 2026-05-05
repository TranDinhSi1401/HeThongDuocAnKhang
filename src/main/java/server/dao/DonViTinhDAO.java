package server.dao;

import server.entity.DonViTinh;

import java.util.List;

/**
 * DAO cho entity DonViTinh.
 */
public class DonViTinhDAO extends AbstractGenericDaoImpl<DonViTinh, String> {

    public DonViTinhDAO() {
        super(DonViTinh.class);
    }

    /** Lấy tất cả đơn vị tính của một sản phẩm (chưa bị xóa). */
    public List<DonViTinh> getDonViTinhTheoMaSP(String maSP) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT dvt FROM DonViTinh dvt WHERE dvt.sanPham.maSP = :maSP AND dvt.daXoa = false",
                DonViTinh.class)
              .setParameter("maSP", maSP)
              .getResultList()
        );
    }

    @Override
    public  DonViTinh findById(String s) {
        return super.findById(s);
    }

    /** Tìm đơn vị tính cơ bản của sản phẩm. */
    public DonViTinh getDVTCoBanTheoMaSP(String maSP) {
        return doInTransaction(em -> {
            List<DonViTinh> result = em.createQuery(
                "SELECT dvt FROM DonViTinh dvt WHERE dvt.sanPham.maSP = :maSP " +
                "AND dvt.donViTinhCoBan = true AND dvt.daXoa = false", DonViTinh.class)
                .setParameter("maSP", maSP)
                .setMaxResults(1)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Cập nhật đơn vị tính. */
    public boolean suaDonViTinh(String maDVT, DonViTinh dvtNew) {
        return doInTransaction(em -> {
            DonViTinh dvt = em.find(DonViTinh.class, maDVT);
            if (dvt == null) return false;
            dvt.setTenDonViTinh(dvtNew.getTenDonViTinh());
            dvt.setHeSoQuyDoi(dvtNew.getHeSoQuyDoi());
            dvt.setGiaBanTheoDonVi(dvtNew.getGiaBanTheoDonVi());
            dvt.setDonViTinhCoBan(dvtNew.getDonViTinhCoBan());
            em.merge(dvt);
            return true;
        });
    }

    /** Xóa mềm đơn vị tính (set daXoa = true). */
    public boolean xoaDonViTinh(String maDVT) {
        return doInTransaction(em -> {
            DonViTinh dvt = em.find(DonViTinh.class, maDVT);
            if (dvt == null) return false;
            dvt.setDaXoa(true);
            em.merge(dvt);
            return true;
        });
    }

    /** Xóa mềm tất cả đơn vị tính của một sản phẩm. */
    public boolean xoaDonViTinhTheoMaSP(String maSP) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE DonViTinh dvt SET dvt.daXoa = true WHERE dvt.sanPham.maSP = :maSP")
                .setParameter("maSP", maSP)
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Lấy số thứ tự mã DVT cuối cùng theo format "DVT-XXXX". */
    public int getMaDVTCuoiCung() {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                "SELECT dvt.maDonViTinh FROM DonViTinh dvt", String.class)
                .getResultList();
            int max = 0;
            for(String ma : result) {
                if(ma != null && ma.startsWith("DVT-")) {
                    try {
                        int num = Integer.parseInt(ma.substring(4));
                        if(num > max) max = num;
                    } catch(Exception ignored){}
                }
            }
            return max;
        });
    }
}
