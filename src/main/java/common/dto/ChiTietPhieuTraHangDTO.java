package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietPhieuTraHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String maPhieuTraHang;
    private String maChiTietHoaDon;
    private int soLuong;
    /** Giá trị enum TruongHopDoiTraEnum dưới dạng String */
    private String truongHopDoiTra;
    /** Giá trị enum TinhTrangSanPhamEnum dưới dạng String */
    private String tinhTrangSanPham;
    private String giaTriHoanTra;
    private double thanhTienHoanTra;
    // Trường hiển thị
    private String tenSP;
    private String tenDVT;
}
