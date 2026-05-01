package server.dao;

import server.entity.TaiKhoan;

import java.util.List;

/**
 * DAO cho entity TaiKhoan.
 */
public class TaiKhoanDAO extends AbstractGenericDaoImpl<TaiKhoan, String> {

    public TaiKhoanDAO() {
        super(TaiKhoan.class);
    }

    /** Lấy tài khoản theo mã NV. */
    public TaiKhoan getTaiKhoanTheoMaNV(String maNV) {
        return findById(maNV);
    }

    /** Lấy tài khoản theo email. */
    public TaiKhoan getTaiKhoanTheoEmail(String email) {
        return doInTransaction(em -> {
            List<TaiKhoan> result = em.createQuery(
                "SELECT tk FROM TaiKhoan tk WHERE tk.email = :email AND tk.daXoa = false", TaiKhoan.class)
                .setParameter("email", email)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Khóa tài khoản (set biKhoa = true). */
    public boolean xoaTaiKhoan(String maNV) {
        return doInTransaction(em -> {
            TaiKhoan tk = em.find(TaiKhoan.class, maNV);
            if (tk == null) return false;
            tk.setBiKhoa(true);
            em.merge(tk);
            return true;
        });
    }

    /** Cập nhật thông tin quyền, khóa, email của tài khoản. */
    public boolean capNhatTaiKhoan(TaiKhoan tk) {
        return doInTransaction(em -> {
            TaiKhoan existing = em.find(TaiKhoan.class, tk.getMaNV());
            if (existing == null) return false;
            existing.setQuanLy(tk.isQuanLy());
            existing.setQuanLyLo(tk.isQuanLyLo());
            existing.setBiKhoa(tk.isBiKhoa());
            existing.setEmail(tk.getEmail());
            em.merge(existing);
            return true;
        });
    }

    /** Cập nhật mật khẩu theo maNV và email. */
    public boolean updateMatKhau(String maNV, String email, String matKhauHash) {
        return doInTransaction(em -> {
            int rows = em.createQuery(
                "UPDATE TaiKhoan tk SET tk.matKhau = :mk WHERE tk.email = :email AND tk.maNV = :maNV")
                .setParameter("mk", matKhauHash)
                .setParameter("email", email)
                .setParameter("maNV", maNV)
                .executeUpdate();
            return rows > 0;
        });
    }

    /** Kiểm tra email đã tồn tại chưa. */
    public boolean kiemTraEmailTonTai(String email) {
        return doInTransaction(em -> {
            Long count = em.createQuery(
                "SELECT COUNT(tk) FROM TaiKhoan tk WHERE tk.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
            return count > 0;
        });
    }

    /** Kiểm tra email có thuộc tài khoản (maNV) cụ thể không. */
    public boolean kiemTraEmailThuocTaiKhoan(String maNV, String email) {
        return doInTransaction(em -> {
            Long count = em.createQuery(
                "SELECT COUNT(tk) FROM TaiKhoan tk WHERE tk.maNV = :maNV AND tk.email = :email", Long.class)
                .setParameter("maNV", maNV)
                .setParameter("email", email)
                .getSingleResult();
            return count > 0;
        });
    }
}
