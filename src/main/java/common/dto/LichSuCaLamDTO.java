package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuCaLamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String maNV;
    private String maCa;
    private String ngayLamViec;
    private LocalTime thoiGianVaoCa;
    private LocalTime thoiGianRaCa;
    private String ghiChu;
}
