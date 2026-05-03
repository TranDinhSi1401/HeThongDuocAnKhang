package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SanPhamCungCapDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String maSP;
    private String maNCC;
    private boolean trangThaiHopTac;
    private double giaNhap;
}
