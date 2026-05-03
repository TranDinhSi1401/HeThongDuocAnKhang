package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TaiKhoanDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maNV;
    private String matKhau;
    private boolean quanLy;
    private boolean biKhoa;
    private String email;
    private LocalDateTime ngayTao;
    private boolean daXoa;
    private boolean quanLyLo;
}
