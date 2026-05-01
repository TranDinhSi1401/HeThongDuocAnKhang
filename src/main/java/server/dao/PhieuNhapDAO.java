package server.dao;

import server.entity.PhieuNhap;

import java.time.LocalDate;
import java.util.List;

/**
 * DAO cho entity PhieuNhap.
 */
public class PhieuNhapDAO extends AbstractGenericDaoImpl<PhieuNhap, String> {

    public PhieuNhapDAO() {
        super(PhieuNhap.class);
    }

    /** Tìm phiếu nhập theo mã nhân viên. */
    public List<PhieuNhap> timPNTheoMaNV(String maNV) {
        return doInTransaction(em ->
            em.createQuery("SELECT pn FROM PhieuNhap pn WHERE pn.nhanVien.maNV = :maNV", PhieuNhap.class)
              .setParameter("maNV", maNV)
              .getResultList()
        );
    }

    /** Tìm phiếu nhập theo ngày tạo. */
    public List<PhieuNhap> timPNTheoNgay(LocalDate ngay) {
        return doInTransaction(em ->
            em.createQuery("SELECT pn FROM PhieuNhap pn WHERE pn.ngayTao = :ngay", PhieuNhap.class)
              .setParameter("ngay", ngay)
              .getResultList()
        );
    }

    /** Tìm phiếu nhập trong khoảng ngày. */
    public List<PhieuNhap> timPNTheoKhoangNgay(LocalDate startDate, LocalDate endDate) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT pn FROM PhieuNhap pn WHERE pn.ngayTao BETWEEN :start AND :end", PhieuNhap.class)
              .setParameter("start", startDate)
              .setParameter("end", endDate)
              .getResultList()
        );
    }

    /** Lấy số thứ tự PN cuối cùng trong ngày theo format "PN-YYYYMMDD-XXX". */
    public int getSoPNCuoiCungTrongNgay(String ngay) {
        return doInTransaction(em -> {
            String prefix = "PN-" + ngay + "-";
            List<String> result = em.createQuery(
                "SELECT pn.maPhieuNhap FROM PhieuNhap pn WHERE pn.maPhieuNhap LIKE :prefix ORDER BY pn.maPhieuNhap DESC",
                String.class)
                .setParameter("prefix", prefix + "%")
                .setMaxResults(1)
                .getResultList();
            if (!result.isEmpty() && result.get(0) != null)
                return Integer.parseInt(result.get(0).substring(prefix.length()));
            return 0;
        });
    }
}
