package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SPSapHetHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maSP;
    private String tenSP;
    private String tenDVT;
    private int tonKho;
    private int tonMin;
}
