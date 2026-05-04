package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoSanPhamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maLoSanPham;
    /** maSP của sản phẩm cha */
    private String maSP;
    private int soLuong;
    private LocalDate ngaySanXuat;
    private LocalDate ngayHetHan;
    private boolean daHuy;
}
