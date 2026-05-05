package server.dao;

import common.dto.DoanhThu;
import server.entity.HoaDon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyy");
        String ngayStr = ngay.format(dtf);
        String prefix = "HD-" + ngayStr + "-";

        return doInTransaction(em -> {
            List<String> result = em.createQuery(
                            "SELECT hd.maHoaDon FROM HoaDon hd " +
                                    "WHERE hd.maHoaDon LIKE :prefix " +
                                    "ORDER BY hd.maHoaDon DESC", String.class)
                    .setParameter("prefix", prefix + "%")
                    .setMaxResults(1)
                    .getResultList();

            if (!result.isEmpty() && result.get(0) != null) {
                String maCuoi = result.get(0);
                // Cắt lấy 4 số cuối cùng
                String soThuTuStr = maCuoi.substring(maCuoi.lastIndexOf("-") + 1);
                return Integer.parseInt(soThuTuStr);
            }
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
    // Lấy doanh từng ngày theo khoảng thời gian (dùng cho biểu đồ doanh thu theo ngày)
    public List<DoanhThu> getDoanhThuTungNgayTrongKhoangThoiGian(LocalDate begin, LocalDate end) {
        if (begin == null || end == null || begin.isAfter(end)) {
            return new ArrayList<>();
        }

        return doInTransaction(em -> {
            // Dùng inclusiveEnd để lấy trọn vẹn dữ liệu ngày cuối cùng
            LocalDate inclusiveEnd = end.plusDays(1);

            String jpql = """
            SELECT new common.dto.DoanhThu(
                CAST(hd.ngayLapHoaDon AS date), 
                COUNT(hd.maHoaDon), 
                SUM(hd.tongTien)
            )
            FROM HoaDon hd
            WHERE hd.ngayLapHoaDon >= :begin 
              AND hd.ngayLapHoaDon < :end
            GROUP BY CAST(hd.ngayLapHoaDon AS date)
            ORDER BY CAST(hd.ngayLapHoaDon AS date)
            """;

            return em.createQuery(jpql, DoanhThu.class)
                    .setParameter("begin", begin.atStartOfDay())
                    .setParameter("end", inclusiveEnd.atStartOfDay())
                    .getResultList();
        });
    }

    public List<DoanhThu> getDoanhThuTungThangTrongNam(int nam) {
        // 1. Lấy dữ liệu thực tế từ DB bằng JPQL
        List<DoanhThu> resultsFromDB = doInTransaction(em -> {
            String jpql = """
            SELECT new common.dto.DoanhThu(
                MONTH(hd.ngayLapHoaDon), 
                COUNT(hd.maHoaDon), 
                SUM(hd.tongTien)
            )
            FROM HoaDon hd
            WHERE YEAR(hd.ngayLapHoaDon) = :nam
            GROUP BY MONTH(hd.ngayLapHoaDon)
            ORDER BY MONTH(hd.ngayLapHoaDon)
            """;

            return em.createQuery(jpql, DoanhThu.class)
                    .setParameter("nam", nam)
                    .getResultList();
        });

        // 2. Map dữ liệu vào đủ 12 tháng (để các tháng không có doanh thu vẫn hiện số 0)
        List<DoanhThu> fullYearList = new ArrayList<>();
        Map<Integer, DoanhThu> dataMap = new HashMap<>();

        // Đưa kết quả từ DB vào Map để tra cứu nhanh
        for (DoanhThu dt : resultsFromDB) {
            // Vì trong Constructor DoanhThu ta ép kiểu toString(),
            // ta cần lấy lại số tháng để làm key
            dataMap.put(Integer.parseInt(dt.getThoiGian()), dt);
        }

        // Tạo đủ 12 tháng
        for (int i = 1; i <= 12; i++) {
            if (dataMap.containsKey(i)) {
                DoanhThu dt = dataMap.get(i);
                // Định dạng lại thoiGian hiển thị thành "Tháng/Năm"
                dt.setThoiGian(i + "/" + nam);
                fullYearList.add(dt);
            } else {
                // Tháng không có dữ liệu thì tạo mới với giá trị 0
                fullYearList.add(new DoanhThu(i + "/" + nam, 0L, 0.0));
            }
        }

        return fullYearList;
    }

    public List<DoanhThu> getDoanhThuTungQuyTrongNam(int nam) {
        // 1. Lấy dữ liệu thực tế từ DB
        List<DoanhThu> resultsFromDB = doInTransaction(em -> {
            // Công thức (MONTH(hd.ngayLapHoaDon) + 2) / 3 sẽ trả về Quý (1, 2, 3, 4)
            String jpql = """
            SELECT new common.dto.DoanhThu(
                (MONTH(hd.ngayLapHoaDon) + 2) / 3, 
                COUNT(hd.maHoaDon), 
                SUM(hd.tongTien)
            )
            FROM HoaDon hd
            WHERE YEAR(hd.ngayLapHoaDon) = :nam
            GROUP BY (MONTH(hd.ngayLapHoaDon) + 2) / 3
            ORDER BY (MONTH(hd.ngayLapHoaDon) + 2) / 3
            """;

            return em.createQuery(jpql, DoanhThu.class)
                    .setParameter("nam", nam)
                    .getResultList();
        });

        // 2. Map dữ liệu vào đủ 4 Quý để đảm bảo không bị thiếu Quý nào
        List<DoanhThu> fullYearQuarters = new ArrayList<>();
        Map<Integer, DoanhThu> dataMap = new HashMap<>();

        for (DoanhThu dt : resultsFromDB) {
            // Ép kiểu ngược lại từ String (do Constructor DTO xử lý) về Integer để làm key
            dataMap.put(Double.valueOf(dt.getThoiGian()).intValue(), dt);
        }

        for (int i = 1; i <= 4; i++) {
            if (dataMap.containsKey(i)) {
                DoanhThu dt = dataMap.get(i);
                dt.setThoiGian("Quý " + i + "/" + nam);
                fullYearQuarters.add(dt);
            } else {
                // Quý không có doanh thu
                fullYearQuarters.add(new DoanhThu("Quý " + i + "/" + nam, 0L, 0.0));
            }
        }

        return fullYearQuarters;
    }

    public List<DoanhThu> getDoanhThuTungNamTheoKhoang(int namBatDau, int namKetThuc) {
        if (namBatDau > namKetThuc) {
            return new ArrayList<>();
        }

        // 1. Lấy dữ liệu thực tế từ Database
        List<DoanhThu> resultsFromDB = doInTransaction(em -> {
            String jpql = """
            SELECT new common.dto.DoanhThu(
                YEAR(hd.ngayLapHoaDon), 
                COUNT(hd.maHoaDon), 
                SUM(hd.tongTien)
            )
            FROM HoaDon hd
            WHERE YEAR(hd.ngayLapHoaDon) >= :start 
              AND YEAR(hd.ngayLapHoaDon) <= :end
            GROUP BY YEAR(hd.ngayLapHoaDon)
            ORDER BY YEAR(hd.ngayLapHoaDon)
            """;

            return em.createQuery(jpql, DoanhThu.class)
                    .setParameter("start", namBatDau)
                    .setParameter("end", namKetThuc)
                    .getResultList();
        });

        // 2. Map dữ liệu vào khoảng năm từ namBatDau đến namKetThuc
        List<DoanhThu> fullYearRangeList = new ArrayList<>();
        Map<Integer, DoanhThu> dataMap = new HashMap<>();

        for (DoanhThu dt : resultsFromDB) {
            // Ép kiểu ngược từ String (do Constructor DTO xử lý) về Integer làm key
            dataMap.put(Double.valueOf(dt.getThoiGian()).intValue(), dt);
        }

        for (int nam = namBatDau; nam <= namKetThuc; nam++) {
            if (dataMap.containsKey(nam)) {
                DoanhThu dt = dataMap.get(nam);
                dt.setThoiGian(String.valueOf(nam)); // Đảm bảo hiển thị đúng số năm
                fullYearRangeList.add(dt);
            } else {
                // Năm không có dữ liệu doanh thu
                fullYearRangeList.add(new DoanhThu(String.valueOf(nam), 0L, 0.0));
            }
        }

        return fullYearRangeList;
    }
}
