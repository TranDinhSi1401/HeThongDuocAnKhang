package server.dao;

import server.entity.KhachHang;

import java.util.List;

/**
 * DAO cho entity KhachHang.
 */
public class KhachHangDAO extends AbstractGenericDaoImpl<KhachHang, String> {

    public KhachHangDAO() {
        super(KhachHang.class);
    }

    /** Lấy tất cả khách hàng chưa bị xóa. */
    public List<KhachHang> getAllKhachHang() {
        return doInTransaction(em ->
            em.createQuery("SELECT kh FROM KhachHang kh WHERE kh.daXoa = false", KhachHang.class)
              .getResultList()
        );
    }

    /** Tìm khách hàng theo SĐT (chưa bị xóa). */
    public KhachHang getKhachHangTheoSdt(String sdt) {
        return doInTransaction(em -> {
            List<KhachHang> result = em.createQuery(
                "SELECT kh FROM KhachHang kh WHERE kh.sdt = :sdt AND kh.daXoa = false", KhachHang.class)
                .setParameter("sdt", sdt)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Tìm khách hàng theo mã (chưa bị xóa). */
    public KhachHang timKHTheoMa(String ma) {
        return doInTransaction(em -> {
            List<KhachHang> result = em.createQuery(
                "SELECT kh FROM KhachHang kh WHERE kh.maKH = :ma AND kh.daXoa = false", KhachHang.class)
                .setParameter("ma", ma)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Xóa mềm khách hàng (set daXoa = true). */
    public boolean xoaKhachHang(String maKH) {
        return doInTransaction(em -> {
            KhachHang kh = em.find(KhachHang.class, maKH);
            if (kh == null) return false;
            kh.setDaXoa(true);
            em.merge(kh);
            return true;
        });
    }

    /** Cập nhật thông tin khách hàng. */
    public boolean suaKhachHang(String maKH, KhachHang khNew) {
        return doInTransaction(em -> {
            KhachHang kh = em.find(KhachHang.class, maKH);
            if (kh == null) return false;
            kh.setHoTenDem(khNew.getHoTenDem());
            kh.setTen(khNew.getTen());
            kh.setSdt(khNew.getSdt());
            kh.setDiemTichLuy(khNew.getDiemTichLuy());
            em.merge(kh);
            return true;
        });
    }

    /** Cộng điểm tích lũy cho khách hàng. */
    public boolean updateDiemTichLuy(int diem, String maKH) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE KhachHang kh SET kh.diemTichLuy = kh.diemTichLuy + :diem WHERE kh.maKH = :ma AND kh.daXoa = false")
                .setParameter("diem", diem)
                .setParameter("ma", maKH)
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Trừ điểm tích lũy của khách hàng. */
    public boolean truDiemTichLuy(int diem, String maKH) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE KhachHang kh SET kh.diemTichLuy = kh.diemTichLuy - :diem WHERE kh.maKH = :ma AND kh.daXoa = false")
                .setParameter("diem", diem)
                .setParameter("ma", maKH)
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Tìm khách hàng theo tên (LIKE, chưa bị xóa). */
    public List<KhachHang> timKHTheoTen(String tenKH) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT kh FROM KhachHang kh WHERE CONCAT(kh.hoTenDem, ' ', kh.ten) LIKE :ten AND kh.daXoa = false",
                KhachHang.class)
              .setParameter("ten", "%" + tenKH + "%")
              .getResultList()
        );
    }

    /** Tìm khách hàng theo SĐT (danh sách, LIKE, chưa bị xóa). */
    public List<KhachHang> timKHTheoSDT(String sdtKH) {
        return doInTransaction(em ->
            em.createQuery("SELECT kh FROM KhachHang kh WHERE kh.sdt LIKE :sdt AND kh.daXoa = false", KhachHang.class)
              .setParameter("sdt", "%" + sdtKH + "%")
              .getResultList()
        );
    }

    /** Lấy số thứ tự mã KH cuối cùng theo format "KH-XXXXX". */
    public int getMaKHCuoiCung() {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                "SELECT kh.maKH FROM KhachHang kh ORDER BY kh.maKH DESC", String.class)
                .setMaxResults(1)
                .getResultList();
            if (!result.isEmpty()) {
                String maMax = result.get(0);
                if (maMax != null && maMax.matches("KH-\\d{5}"))
                    return Integer.parseInt(maMax.substring(3));
            }
            return 0;
        });
    }
}
