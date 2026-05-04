package common.dto;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChiTietPhieuDatHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maPhieuDat;
    private String maSP;
    private String tenSP;
    private int soLuong;
    private double donGia;
    private double thanhTien;
}
