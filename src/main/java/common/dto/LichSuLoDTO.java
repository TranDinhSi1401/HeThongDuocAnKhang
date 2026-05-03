package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LichSuLoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maLichSuLo;
    private String maLoSanPham;
    private String maNV;
    private LocalDateTime thoiGian;
    private String hanhDong;
    private int soLuongSau;
    private String ghiChu;
}
