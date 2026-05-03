package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NhanVienDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maNV;
    private String hoTenDem;
    private String ten;
    private String sdt;
    private String cccd;
    private boolean gioiTinh;
    private LocalDate ngaySinh;
    private String diaChi;
    private boolean nghiViec;
}
