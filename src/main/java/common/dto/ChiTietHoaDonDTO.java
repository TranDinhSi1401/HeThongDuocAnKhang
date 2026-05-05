package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietHoaDonDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maChiTietHoaDon;
    private String maHoaDon;
    private String maDonViTinh;
    private int soLuong;
    private double donGia;
    private double giamGia;
    private double thanhTien;
    
    // Thêm các trường hiển thị (không lưu DB trực tiếp trong bảng này nhưng cần cho GUI)
    private String tenSP;
    private String tenDVT;
}
