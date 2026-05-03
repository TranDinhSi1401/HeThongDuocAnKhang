package server.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
@Table(name = "chi_tiet_xuat_lo", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ma_lo_san_pham", "ma_chi_tiet_hoa_don"})
})
public class ChiTietXuatLo implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_lo_san_pham")
    private LoSanPham loSanPham;

    @ManyToOne
    @JoinColumn(name = "ma_chi_tiet_hoa_don")
    private ChiTietHoaDon chiTietHoaDon;

    @Column(name = "so_luong")
    private int soLuong;
}
