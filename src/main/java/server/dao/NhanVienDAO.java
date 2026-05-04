package server.dao;

import server.entity.NhanVien;

import java.util.List;

/**
 * DAO cho entity NhanVien.
 */
public class NhanVienDAO extends AbstractGenericDaoImpl<NhanVien, String> {

    public NhanVienDAO() {
        super(NhanVien.class);
    }

    /** Lấy tất cả nhân viên chưa nghỉ việc. */
    public List<NhanVien> getAllNhanVien() {
        return doInTransaction(em ->
            em.createQuery("SELECT nv FROM NhanVien nv WHERE nv.nghiViec = false", NhanVien.class)
              .getResultList()
        );
    }

    /** Lấy nhân viên theo mã (chỉ dùng khi cần kiểm tra nghiViec). */
    public NhanVien getNhanVienTheoMaNV(String maNV) {
        return findById(maNV);
    }

    /** Lấy nhân viên theo trạng thái nghỉ việc. */
    public List<NhanVien> timNVTheoTrangThai(boolean daNghiViec) {
        return doInTransaction(em ->
            em.createQuery("SELECT nv FROM NhanVien nv WHERE nv.nghiViec = :trangThai", NhanVien.class)
              .setParameter("trangThai", daNghiViec)
              .getResultList()
        );
    }

    /** Xóa mềm nhân viên (set nghiViec = true). */
    public boolean xoaNhanVien(String maNV) {
        return doInTransaction(em -> {
            NhanVien nv = em.find(NhanVien.class, maNV);
            if (nv == null) return false;
            nv.setNghiViec(true);
            em.merge(nv);
            return true;
        });
    }

    /** Cập nhật thông tin nhân viên. */
    public boolean suaNhanVien(String maNV, NhanVien nvNew) {
        return doInTransaction(em -> {
            NhanVien nv = em.find(NhanVien.class, maNV);
            if (nv == null) return false;
            nv.setHoTenDem(nvNew.getHoTenDem());
            nv.setTen(nvNew.getTen());
            nv.setSdt(nvNew.getSdt());
            nv.setCccd(nvNew.getCccd());
            nv.setGioiTinh(nvNew.isGioiTinh());
            nv.setNgaySinh(nvNew.getNgaySinh());
            nv.setDiaChi(nvNew.getDiaChi());
            nv.setNghiViec(nvNew.isNghiViec());
            em.merge(nv);
            return true;
        });
    }

    /** Tìm nhân viên theo mã (chưa nghỉ việc). */
    public NhanVien timNVTheoMa(String ma) {
        return doInTransaction(em -> {
            List<NhanVien> result = em.createQuery(
                "SELECT nv FROM NhanVien nv WHERE nv.maNV = :ma AND nv.nghiViec = false", NhanVien.class)
                .setParameter("ma", ma)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Tìm nhân viên theo tên (LIKE, chưa nghỉ việc). */
    public List<NhanVien> timNVTheoTen(String tenNV) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT nv FROM NhanVien nv WHERE CONCAT(nv.hoTenDem, ' ', nv.ten) LIKE :ten AND nv.nghiViec = false",
                NhanVien.class)
              .setParameter("ten", "%" + tenNV + "%")
              .getResultList()
        );
    }

    /** Tìm nhân viên theo SĐT (chưa nghỉ việc). */
    public List<NhanVien> timNVTheoSDT(String sdtNV) {
        return doInTransaction(em ->
            em.createQuery("SELECT nv FROM NhanVien nv WHERE nv.sdt = :sdt AND nv.nghiViec = false", NhanVien.class)
              .setParameter("sdt", sdtNV)
              .getResultList()
        );
    }

    /** Tìm nhân viên theo CCCD (chưa nghỉ việc). */
    public List<NhanVien> timNVTheoCCCD(String cccdNV) {
        return doInTransaction(em ->
            em.createQuery("SELECT nv FROM NhanVien nv WHERE nv.cccd = :cccd AND nv.nghiViec = false", NhanVien.class)
              .setParameter("cccd", cccdNV)
              .getResultList()
        );
    }

    /** Lấy số thứ tự mã NV cuối cùng theo format "NV-XXXX". */
    public int getMaNVCuoiCung() {
        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                "SELECT nv.maNV FROM NhanVien nv ORDER BY nv.maNV DESC", String.class)
                .setMaxResults(1)
                .getResultList();
            if (!result.isEmpty()) {
                String maMax = result.get(0);
                if (maMax != null && maMax.matches("^NV-\\d{4}$"))
                    return Integer.parseInt(maMax.substring(3));
            }
            return 0;
        });
    }
}
