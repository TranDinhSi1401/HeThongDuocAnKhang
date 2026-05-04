package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CaLamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maCa;
    private String tenCa;
    private String thoiGianBatDau;
    private String thoiGianKetThuc;
}
