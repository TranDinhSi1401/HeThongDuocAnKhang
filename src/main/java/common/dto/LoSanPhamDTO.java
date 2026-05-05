package common.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoSanPhamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String maLoSanPham;
    /** maSP của sản phẩm cha */
    private String maSP;
    private String tenSP;
    private String tenDonVi;
    private String tenNhaCungCap;
    private double giaNhap;
    private int soLuong;
    private LocalDate ngaySanXuat;
    private LocalDate ngayHetHan;
    private boolean daHuy;
}
