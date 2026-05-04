package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DonViTinhDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maDonViTinh;
    /** maSP của sản phẩm cha (thay vì nhúng cả SanPhamDTO để tránh đệ quy) */
    private String maSP;
    private String tenDonViTinh;
    private int heSoQuyDoi;
    private double giaBanTheoDonVi;
    private boolean donViTinhCoBan;
    private boolean daXoa;
}
