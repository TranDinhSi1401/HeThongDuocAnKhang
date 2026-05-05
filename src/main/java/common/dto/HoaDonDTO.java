package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HoaDonDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maHoaDon;
    /** maNV của nhân viên lập hóa đơn */
    private String maNV;
    /** maKH của khách hàng (có thể null nếu khách vãng lai) */
    private String maKH;
    private LocalDateTime ngayLapHoaDon;
    private boolean chuyenKhoan;
    private double tongTien;
    
    // Thêm các trường hiển thị
    private String tenKH;
    private String tenNV;
    private int diemTichLuyKH;
}
