package server.dao;

import server.entity.HoaDon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO cho entity HoaDon.
 */
public class HoaDonDAO extends AbstractGenericDaoImpl<HoaDon, String> {

    public HoaDonDAO() {
        super(HoaDon.class);
    }

    /** Lấy hóa đơn mới nhất trong ngày hôm nay. */
    public HoaDon getHoaDonMoiNhatTrongNgay() {
        return doInTransaction(em -> {
            LocalDate today = LocalDate.now();
            List<HoaDon> result = em.createQuery(
                "SELECT hd FROM HoaDon hd WHERE hd.ngayLapHoaDon >= :start AND hd.ngayLapHoaDon < :end ORDER BY hd.maHoaDon DESC",
                HoaDon.class)
                .setParameter("start", today.atStartOfDay())
                .setParameter("end", today.plusDays(1).atStartOfDay())
                .setMaxResults(1)
                .getResultList();
            return result.isEmpty() ? null : result.get(0);
        });
    }

    /** Lấy hóa đơn theo mã. */
    public HoaDon getHoaDonTheoMaHD(String maHD) {
        return findById(maHD);
    }

    /** Tìm hóa đơn theo mã nhân viên. */
    public List<HoaDon> timHDTheoMaNV(String maNV) {
        return doInTransaction(em ->
            em.createQuery("SELECT hd FROM HoaDon hd WHERE hd.nhanVien.maNV = :maNV", HoaDon.class)
              .setParameter("maNV", maNV)
              .getResultList()
        );
    }

    /** Tìm hóa đơn theo mã khách hàng. */
    public List<HoaDon> timHDTheoMaKH(String maKH) {
        return doInTransaction(em ->
            em.createQuery("SELECT hd FROM HoaDon hd WHERE hd.khachHang.maKH = :maKH", HoaDon.class)
              .setParameter("maKH", maKH)
              .getResultList()
        );
    }

    /** Tìm hóa đơn theo ngày lập. */
    public List<HoaDon> timHDTheoNgayLap(LocalDate date) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT hd FROM HoaDon hd WHERE hd.ngayLapHoaDon >= :start AND hd.ngayLapHoaDon < :end", HoaDon.class)
              .setParameter("start", date.atStartOfDay())
              .setParameter("end", date.plusDays(1).atStartOfDay())
              .getResultList()
        );
    }

    /** Tìm hóa đơn trong khoảng ngày. */
    public List<HoaDon> timHDTheoKhoangNgay(LocalDate startDate, LocalDate endDate) {
        return doInTransaction(em ->
            em.createQuery(
                "SELECT hd FROM HoaDon hd WHERE hd.ngayLapHoaDon >= :start AND hd.ngayLapHoaDon < :end",
                HoaDon.class)
              .setParameter("start", startDate.atStartOfDay())
              .setParameter("end", endDate.plusDays(1).atStartOfDay())
              .getResultList()
        );
    }

    /** Tìm hóa đơn theo SĐT khách hàng. */
    public List<HoaDon> timHDTheoSDTKH(String sdt) {
        return doInTransaction(em ->
            em.createQuery("SELECT hd FROM HoaDon hd WHERE hd.khachHang.sdt = :sdt", HoaDon.class)
              .setParameter("sdt", sdt)
              .getResultList()
        );
    }

    /** Tìm hóa đơn theo hình thức thanh toán. */
    public List<HoaDon> timHDTheoHinhThuc(boolean chuyenKhoan) {
        return doInTransaction(em ->
            em.createQuery("SELECT hd FROM HoaDon hd WHERE hd.chuyenKhoan = :ck", HoaDon.class)
              .setParameter("ck", chuyenKhoan)
              .getResultList()
        );
    }

    /** Lấy số thứ tự HD cuối cùng trong ngày theo format "HD-YYYYMMDD-XXX". */
    public int getSoHDCuoiCungTrongNgay(LocalDate ngay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String ngayFormatted = ngay.format(formatter);

        return doInTransaction(em -> {
            String prefix = "HD-" + ngay + "-";
            List<String> result = em.createQuery(
                "SELECT hd.maHoaDon FROM HoaDon hd WHERE hd.maHoaDon LIKE :prefix ORDER BY hd.maHoaDon DESC",
                String.class)
                .setParameter("prefix", prefix + "%")
                .setMaxResults(1)
                .getResultList();
            if (!result.isEmpty() && result.get(0) != null)
                return Integer.parseInt(result.get(0).substring(prefix.length()));
            return 0;
        });
    }

    /** Lấy số lượng phiếu trả hàng của một hóa đơn. */
    public int getSoPTH(String maHoaDon) {
        return doInTransaction(em ->
            em.createQuery("SELECT COUNT(pth) FROM PhieuTraHang pth WHERE pth.hoaDon.maHoaDon = :maHD", Long.class)
              .setParameter("maHD", maHoaDon)
              .getSingleResult()
              .intValue()
        );
    }

    /** Lấy tổng tiền hoàn trả của tất cả phiếu trả thuộc hóa đơn. */
    public double getTongTienCacPTH(String maHoaDon) {
        return doInTransaction(em -> {
            Double result = em.createQuery(
                "SELECT SUM(pth.tongTienHoaTra) FROM PhieuTraHang pth WHERE pth.hoaDon.maHoaDon = :maHD", Double.class)
                .setParameter("maHD", maHoaDon)
                .getSingleResult();
            return result == null ? 0.0 : result;
        });
    }

    /** Doanh thu theo ngày. */
    public double getDoanhThuTheoNgay(LocalDate date) {
        return doInTransaction(em -> {
            Double result = em.createQuery(
                "SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE FUNCTION('DATE', hd.ngayLapHoaDon) = :ngay",
                Double.class)
                .setParameter("ngay", date)
                .getSingleResult();
            return result == null ? 0.0 : result;
        });
    }

    /** Doanh thu theo tháng. */
    public double getDoanhThuTheoThang(LocalDate date) {
        return doInTransaction(em -> {
            Double result = em.createQuery(
                "SELECT SUM(hd.tongTien) FROM HoaDon hd " +
                "WHERE MONTH(hd.ngayLapHoaDon) = :thang AND YEAR(hd.ngayLapHoaDon) = :nam", Double.class)
                .setParameter("thang", date.getMonthValue())
                .setParameter("nam", date.getYear())
                .getSingleResult();
            return result == null ? 0.0 : result;
        });
    }

    /** Doanh thu từng ngày trong tháng: Map&lt;ngay, tongTien&gt;. */
    public Map<Integer, Double> getDoanhThuTungNgayTrongThang(LocalDate date) {
        return doInTransaction(em -> {
            Map<Integer, Double> result = new HashMap<>();
            for (int i = 1; i <= date.lengthOfMonth(); i++) result.put(i, 0.0);
            List<Object[]> rows = em.createQuery(
                "SELECT DAY(hd.ngayLapHoaDon), SUM(hd.tongTien) FROM HoaDon hd " +
                "WHERE MONTH(hd.ngayLapHoaDon) = :thang AND YEAR(hd.ngayLapHoaDon) = :nam " +
                "GROUP BY DAY(hd.ngayLapHoaDon)", Object[].class)
                .setParameter("thang", date.getMonthValue())
                .setParameter("nam", date.getYear())
                .getResultList();
            for (Object[] row : rows)
                result.put(((Number) row[0]).intValue(), ((Number) row[1]).doubleValue());
            return result;
        });
    }

    /** Doanh thu từng tháng trong năm: Map&lt;thang, tongTien&gt;. */
    public Map<Integer, Double> getDoanhThuTungThangTrongNam(LocalDate date) {
        return doInTransaction(em -> {
            Map<Integer, Double> result = new HashMap<>();
            for (int i = 1; i <= 12; i++) result.put(i, 0.0);
            List<Object[]> rows = em.createQuery(
                "SELECT MONTH(hd.ngayLapHoaDon), SUM(hd.tongTien) FROM HoaDon hd " +
                "WHERE YEAR(hd.ngayLapHoaDon) = :nam GROUP BY MONTH(hd.ngayLapHoaDon)", Object[].class)
                .setParameter("nam", date.getYear())
                .getResultList();
            for (Object[] row : rows)
                result.put(((Number) row[0]).intValue(), ((Number) row[1]).doubleValue());
            return result;
        });
    }

    /** Lấy năm hóa đơn cũ nhất và mới nhất: Map "namCuNhat"/"namMoiNhat". */
    public Map<String, Integer> getNamHoaDonCuNhatVaMoiNhat() {
        return doInTransaction(em -> {
            Map<String, Integer> result = new HashMap<>();
            Object[] row = (Object[]) em.createQuery(
                "SELECT MIN(YEAR(hd.ngayLapHoaDon)), MAX(YEAR(hd.ngayLapHoaDon)) FROM HoaDon hd")
                .getSingleResult();
            if (row[0] != null && row[1] != null) {
                result.put("namCuNhat", ((Number) row[0]).intValue());
                result.put("namMoiNhat", ((Number) row[1]).intValue());
            }
            return result;
        });
    }
}
