package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KhachHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maKH;
    private String hoTenDem;
    private String ten;
    private String sdt;
    private int diemTichLuy;
    private boolean daXoa;
}
