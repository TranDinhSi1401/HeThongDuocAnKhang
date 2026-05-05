package server.dao;

import server.entity.NhaCungCap;

import java.util.List;

/**
 * DAO cho entity NhaCungCap.
 */
public class NhaCungCapDAO extends AbstractGenericDaoImpl<NhaCungCap, String> {

    public NhaCungCapDAO() {
        super(NhaCungCap.class);
    }

    /** Lấy tất cả nhà cung cấp chưa bị xóa. */
    public List<NhaCungCap> getAllNhaCungCap() {
        return doInTransaction(em ->
            em.createQuery("SELECT ncc FROM NhaCungCap ncc WHERE (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
              .getResultList()
        );
    }

    /** Xóa mềm nhà cung cấp. */
    public boolean xoaNhaCungCap(String maNCC) {
        return doInTransaction(em -> {
            NhaCungCap ncc = em.find(NhaCungCap.class, maNCC);
            if (ncc == null) return false;
            ncc.setDaXoa(true);
            em.merge(ncc);
            return true;
        });
    }

    /** Cập nhật thông tin nhà cung cấp. */
    public boolean suaNhaCungCap(String maNCC, NhaCungCap nccNew) {
        return doInTransaction(em -> {
            NhaCungCap ncc = em.find(NhaCungCap.class, maNCC);
            if (ncc == null) return false;
            ncc.setTenNCC(nccNew.getTenNCC());
            ncc.setDiaChi(nccNew.getDiaChi());
            ncc.setSdt(nccNew.getSdt());
            ncc.setEmail(nccNew.getEmail());
            em.merge(ncc);
            return true;
        });
    }

    /** Tìm nhà cung cấp theo mã (chưa bị xóa). */
    public NhaCungCap timNCCTheoMa(String ma) {
        return doInTransaction(em -> {
            List<NhaCungCap> result = em.createQuery(
                "SELECT ncc FROM NhaCungCap ncc WHERE ncc.maNCC = :ma AND (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
                .setParameter("ma", ma).getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Tìm nhà cung cấp theo tên (LIKE, chưa bị xóa). */
    public List<NhaCungCap> timNCCTheoTen(String tenNCCInput) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ncc FROM NhaCungCap ncc WHERE ncc.tenNCC LIKE :ten AND (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
              .setParameter("ten", "%" + tenNCCInput + "%")
              .getResultList()
        );
    }

    /** Tìm nhà cung cấp theo SĐT. */
    public List<NhaCungCap> timNCCTheoSDT(String sdtInput) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ncc FROM NhaCungCap ncc WHERE ncc.sdt = :sdt AND (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
              .setParameter("sdt", sdtInput)
              .getResultList()
        );
    }

    /** Tìm nhà cung cấp theo email (LIKE). */
    public List<NhaCungCap> timNCCTheoEmail(String emailInput) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT ncc FROM NhaCungCap ncc WHERE ncc.email LIKE :email AND (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
              .setParameter("email", "%" + emailInput + "%")
              .getResultList()
        );
    }

    /** Lấy số thứ tự mã NCC cuối cùng theo format "NCC-XXXX". */
    public int getMaNCCCuoiCung() {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                "SELECT ncc.maNCC FROM NhaCungCap ncc ORDER BY ncc.maNCC DESC", String.class)
                .setMaxResults(1).getResultList();
            if (!result.isEmpty()) {
                String maMax = result.get(0);
                if (maMax != null && maMax.matches("NCC-\\d{4}"))
                    return Integer.parseInt(maMax.substring(4));
            }
            return 0;
        });
    }

    /** Lấy một NCC theo tên (exact/like). */
    public NhaCungCap getNhaCungCapTheoTen(String ten) {
        return doInTransaction(em -> {
            List<NhaCungCap> result = em.createQuery(
                "SELECT ncc FROM NhaCungCap ncc WHERE ncc.tenNCC LIKE :ten AND (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
                .setParameter("ten", ten).setMaxResults(1).getResultList();
            return result.isEmpty() ? new NhaCungCap() : result.get(0);
        });
    }

    /** Tìm một NCC theo tên (LIKE). */
    public NhaCungCap timMotNCCTheoTen(String tenNCCInput) {
        return doInTransaction(em -> {
            List<NhaCungCap> result = em.createQuery(
                "SELECT ncc FROM NhaCungCap ncc WHERE ncc.tenNCC LIKE :ten AND (ncc.daXoa = false OR ncc.daXoa IS NULL)", NhaCungCap.class)
                .setParameter("ten", "%" + tenNCCInput + "%").setMaxResults(1).getResultList();
            return result.isEmpty() ? new NhaCungCap() : result.get(0);
        });
    }
}
