package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhieuNhapDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maPhieuNhap;
    private String maNV;
    private LocalDate ngayTao;
    private double tongTien;
    private String ghiChu;
}
