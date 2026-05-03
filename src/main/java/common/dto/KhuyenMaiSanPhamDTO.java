package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KhuyenMaiSanPhamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String maKhuyenMai;
    private String maSP;
    private LocalDateTime ngayChinhSua;
}
