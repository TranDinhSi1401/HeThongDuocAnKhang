package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaVachSanPhamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maVach;
    /** maSP của sản phẩm cha */
    private String maSP;
}
