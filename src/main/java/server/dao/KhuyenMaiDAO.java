package server.dao;

import server.entity.KhuyenMai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import server.entity.LoaiKhuyenMaiEnum;

/**
 * DAO cho entity KhuyenMai.
 */
public class KhuyenMaiDAO extends AbstractGenericDaoImpl<KhuyenMai, String> {

    public KhuyenMaiDAO() {
        super(KhuyenMai.class);
    }

    /** Lấy tất cả khuyến mãi chưa bị xóa. */
    public List<KhuyenMai> getAllKhuyenMai() {
        return doInTransaction(em -> em
                .createQuery("SELECT km FROM KhuyenMai km WHERE (km.daXoa = false OR km.daXoa IS NULL)",
                        KhuyenMai.class)
                .getResultList());
    }

    /** Lấy khuyến mãi đang hoạt động (trong khoảng ngày hiện tại). */
    public List<KhuyenMai> getKhuyenMaiDangHoatDong() {
        return doInTransaction(em -> {
            LocalDateTime now = LocalDateTime.now();
            return em.createQuery(
                    "SELECT km FROM KhuyenMai km WHERE (km.daXoa = false OR km.daXoa IS NULL) " +
                            "AND km.ngayBatDau <= :now AND km.ngayKetThuc >= :now",
                    KhuyenMai.class)
                    .setParameter("now", now)
                    .getResultList();
        });
    }

    /** Xóa mềm khuyến mãi. */
    public boolean xoaKhuyenMai(String maKM) {
        return doInTransaction(em -> {
            KhuyenMai km = em.find(KhuyenMai.class, maKM);
            if (km == null)
                return false;
            km.setDaXoa(true);
            em.merge(km);
            return true;
        });
    }

    /** Cập nhật khuyến mãi. */
    public boolean suaKhuyenMai(String maKM, KhuyenMai kmNew) {
        return doInTransaction(em -> {
            KhuyenMai km = em.find(KhuyenMai.class, maKM);
            if (km == null)
                return false;
            km.setMoTa(kmNew.getMoTa());
            km.setPhanTram(kmNew.getPhanTram());
            km.setLoaiKhuyenMai(kmNew.getLoaiKhuyenMai());
            km.setNgayBatDau(kmNew.getNgayBatDau());
            km.setNgayKetThuc(kmNew.getNgayKetThuc());
            km.setSoLuongToiThieu(kmNew.getSoLuongToiThieu());
            km.setSoLuongToiDa(kmNew.getSoLuongToiDa());
            km.setNgayChinhSua(LocalDateTime.now());
            em.merge(km);
            return true;
        });
    }

    /** Lấy danh sách khuyến mãi theo mã sản phẩm. */
    public List<KhuyenMai> getKhuyenMaiTheoMaSP(String maSP) {
        return doInTransaction(em -> {
            return em.createQuery(
                    "SELECT DISTINCT km FROM KhuyenMai km " +
                            "JOIN km.khuyenMaiSanPhams kmsp " +
                            "WHERE kmsp.sanPham.maSP = :maSP AND (km.daXoa = false OR km.daXoa IS NULL)",
                    KhuyenMai.class)
                    .setParameter("maSP", maSP)
                    .getResultList();
        });
    }

    /** Lấy số thứ tự mã KM cuối cùng theo format "KM-XXXX". */
    public int getMaKMCuoiCung() {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                    "SELECT km.maKhuyenMai FROM KhuyenMai km ORDER BY km.maKhuyenMai DESC", String.class)
                    .setMaxResults(1).getResultList();
            if (!result.isEmpty()) {
                String maMax = result.get(0);
                if (maMax != null && maMax.matches("^KM-\\d{4}$"))
                    return Integer.parseInt(maMax.substring(3));
            }
            return 0;
        });
    }

    /** Tìm khuyến mãi theo mô tả. */
    public List<KhuyenMai> timKhuyenMaiTheoMoTa(String moTa) {
        return doInTransaction(em -> em.createQuery(
                "SELECT km FROM KhuyenMai km WHERE km.moTa LIKE :moTa AND (km.daXoa = false OR km.daXoa IS NULL)",
                KhuyenMai.class)
                .setParameter("moTa", "%" + moTa + "%")
                .getResultList());
    }

    /** Tìm khuyến mãi theo loại. */
    public List<KhuyenMai> timKhuyenMaiTheoLoai(String loai) {
        return doInTransaction(em -> {
            try {
                LoaiKhuyenMaiEnum loaiEnum = LoaiKhuyenMaiEnum.valueOf(loai);
                return em.createQuery(
                        "SELECT km FROM KhuyenMai km WHERE km.loaiKhuyenMai = :loai AND (km.daXoa = false OR km.daXoa IS NULL)",
                        KhuyenMai.class)
                        .setParameter("loai", loaiEnum)
                        .getResultList();
            } catch (IllegalArgumentException e) {
                return new ArrayList<>();
            }
        });
    }
}
