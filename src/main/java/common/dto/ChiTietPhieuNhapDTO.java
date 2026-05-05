package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietPhieuNhapDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String maPhieuNhap;
    private String maLoSanPham;
    private String maNCC;
    private int soLuong;
    private int soLuongYeuCau;
    private double donGia;
    private double thanhTien;
    private String ghiChu;
    // Trường hiển thị
    private String tenSP;
    private String tenNCC;
}
