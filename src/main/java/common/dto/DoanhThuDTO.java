package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DoanhThuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String thoiGian;
    private int tongHoaDon;
    private double tongDoanhThu;
}
