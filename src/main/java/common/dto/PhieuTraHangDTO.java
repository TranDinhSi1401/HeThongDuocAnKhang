package common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PhieuTraHangDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maPhieuTraHang;
    private String maNV;
    private String maHoaDon;
    private LocalDateTime ngayLapPhieuTraHang;
    private double tongTienHoaTra;

    /** Danh sách chi tiết – chỉ dùng khi gửi request ADD, không map xuống DB */
    private List<Object[]> dsChiTietTraHang;
}