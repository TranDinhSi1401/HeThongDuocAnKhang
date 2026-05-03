package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SanPhamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maSP;
    private String ten;
    private String moTa;
    private String thanhPhan;
    /** Giá trị enum dưới dạng String để tránh vấn đề serialize */
    private String loaiSanPham;
    private int tonToiThieu;
    private int tonToiDa;
    private boolean daXoa;
}
