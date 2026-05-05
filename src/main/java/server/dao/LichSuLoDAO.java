package server.dao;

import java.util.List;

import server.entity.LichSuLo;

/**
 * DAO cho entity LichSuLo.
 */
public class LichSuLoDAO extends AbstractGenericDaoImpl<LichSuLo, Long> {

    public LichSuLoDAO() {
        super(LichSuLo.class);
    }

    /** Lấy toàn bộ lịch sử lô. */
    public List<LichSuLo> getAllLichSuLo() {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT lsl FROM LichSuLo lsl ORDER BY lsl.thoiGian DESC",
                LichSuLo.class)
              .getResultList()
        );
    }

    /** Lấy lịch sử lô theo mã lô sản phẩm. */
    public List<LichSuLo> getLichSuLoTheoMaLo(String maLoSanPham) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT lsl FROM LichSuLo lsl WHERE lsl.loSanPham.maLoSanPham = :maLo ORDER BY lsl.thoiGian DESC",
                LichSuLo.class)
              .setParameter("maLo", maLoSanPham)
              .getResultList()
        );
    }

    /** Lấy lịch sử lô theo mã nhân viên. */
    public List<LichSuLo> getLichSuLoTheoMaNV(String maNV) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT lsl FROM LichSuLo lsl WHERE lsl.nhanVien.maNV = :maNV ORDER BY lsl.thoiGian DESC",
                LichSuLo.class)
              .setParameter("maNV", maNV)
              .getResultList()
        );
    }
}
