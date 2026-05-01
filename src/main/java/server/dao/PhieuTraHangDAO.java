package server.dao;

import server.entity.PhieuTraHang;

import java.util.List;

/**
 * DAO cho entity PhieuTraHang.
 */
public class PhieuTraHangDAO extends AbstractGenericDaoImpl<PhieuTraHang, String> {

    public PhieuTraHangDAO() {
        super(PhieuTraHang.class);
    }

    /** Tìm phiếu trả hàng theo mã hóa đơn. */
    public List<PhieuTraHang> timPTHTheoMaHoaDon(String maHoaDon) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT pth FROM PhieuTraHang pth WHERE pth.hoaDon.maHoaDon = :maHD", PhieuTraHang.class)
              .setParameter("maHD", maHoaDon)
              .getResultList()
        );
    }

    /** Tìm phiếu trả hàng theo mã nhân viên. */
    public List<PhieuTraHang> timPTHTheoMaNV(String maNV) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT pth FROM PhieuTraHang pth WHERE pth.nhanVien.maNV = :maNV", PhieuTraHang.class)
              .setParameter("maNV", maNV)
              .getResultList()
        );
    }

    /** Lấy số thứ tự PTH cuối cùng trong ngày theo format "PTH-YYYYMMDD-XXX". */
    public int getSoPTHCuoiCungTrongNgay(String ngay) {
        return doInTransaction(em -> {
            String prefix = "PTH-" + ngay + "-";
            List<String> result = em.createQuery(
                "SELECT pth.maPhieuTraHang FROM PhieuTraHang pth " +
                "WHERE pth.maPhieuTraHang LIKE :prefix ORDER BY pth.maPhieuTraHang DESC", String.class)
                .setParameter("prefix", prefix + "%")
                .setMaxResults(1)
                .getResultList();
            if (!result.isEmpty() && result.get(0) != null)
                return Integer.parseInt(result.get(0).substring(prefix.length()));
            return 0;
        });
    }
}
