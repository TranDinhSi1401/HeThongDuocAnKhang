package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietXuatLoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String maLoSanPham;
    private String maChiTietHoaDon;
    private int soLuong;
}
