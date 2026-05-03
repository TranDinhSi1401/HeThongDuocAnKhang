package server.dao;

import server.entity.LichSuCaLam;

import java.util.List;

/**
 * DAO cho entity LichSuCaLam.
 */
public class LichSuCaLamDAO extends AbstractGenericDaoImpl<LichSuCaLam, Long> {

    public LichSuCaLamDAO() {
        super(LichSuCaLam.class);
    }

    /** Lấy lịch sử ca làm theo mã nhân viên. */
    public List<LichSuCaLam> getLichSuCaLamTheoMaNV(String maNV) {
        return doInTransaction(em -> em.createQuery(
                "SELECT lscl FROM LichSuCaLam lscl WHERE lscl.nhanVien.maNV = :maNV", LichSuCaLam.class)
                .setParameter("maNV", maNV)
                .getResultList());
    }

    /** Lấy lịch sử ca làm theo mã ca. */
    public List<LichSuCaLam> getLichSuCaLamTheoMaCa(String maCa) {
        return doInTransaction(em -> em.createQuery(
                "SELECT lscl FROM LichSuCaLam lscl WHERE lscl.caLam.maCa = :maCa", LichSuCaLam.class)
                .setParameter("maCa", maCa)
                .getResultList());
    }

    /** Tìm lịch sử ca làm theo ngày làm việc. */
    public List<LichSuCaLam> getLichSuCaLamTheoNgay(String ngayLamViec) {
        return doInTransaction(em -> em.createQuery(
                "SELECT lscl FROM LichSuCaLam lscl WHERE lscl.ngayLamViec = :ngay", LichSuCaLam.class)
                .setParameter("ngay", ngayLamViec)
                .getResultList());
    }

    /** Tìm ca làm đang vào ca (thoiGianRaCa = null) theo mã nhân viên. */
    public LichSuCaLam getLichSuCaLamDangLamTheoMaNV(String maNV) {
        return doInTransaction(em -> {
            List<LichSuCaLam> result = em.createQuery(
                "SELECT lscl FROM LichSuCaLam lscl WHERE lscl.nhanVien.maNV = :maNV AND lscl.thoiGianRaCa IS NULL",
                LichSuCaLam.class)
                .setParameter("maNV", maNV)
                .setMaxResults(1)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }
}
