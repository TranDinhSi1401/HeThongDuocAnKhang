package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NhaCungCapDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maNCC;
    private String tenNCC;
    private String diaChi;
    private String sdt;
    private String email;
    private boolean daXoa;
}
