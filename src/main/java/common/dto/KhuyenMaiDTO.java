package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KhuyenMaiDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maKhuyenMai;
    private String moTa;
    private double phanTram;
    /** Giá trị enum dưới dạng String */
    private String loaiKhuyenMai;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private int soLuongToiThieu;
    private int soLuongToiDa;
    private LocalDateTime ngayChinhSua;
    private boolean daXoa;
}
