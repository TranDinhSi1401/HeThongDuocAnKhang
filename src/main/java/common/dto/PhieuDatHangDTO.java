package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhieuDatHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maPhieuDat;
    private String maNV;
    private String tenNV;
    private String maNCC;
    private String tenNCC;
    private LocalDate ngayLap;
    private double tongTien;
}
