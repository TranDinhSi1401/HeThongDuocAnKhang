package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhieuTraHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maPhieuTraHang;
    private String maNV;
    private String maHoaDon;
    private LocalDateTime ngayLapPhieuTraHang;
    private double tongTienHoaTra;
}
