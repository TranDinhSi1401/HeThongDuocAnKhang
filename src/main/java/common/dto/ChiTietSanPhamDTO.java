package common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class ChiTietSanPhamDTO implements Serializable {
    private SanPhamDTO sanPham;
    private List<DonViTinhDTO> dsDVT;
    private List<KhuyenMaiDTO> dsKM;
    private List<LoSanPhamDTO> dsLSP;
}
