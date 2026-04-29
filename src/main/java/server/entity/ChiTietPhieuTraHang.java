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
@Table(name = "chi_tiet_phieu_tra_hang", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ma_phieu_tra_hang", "ma_chi_tiet_hoa_don"})
})
public class ChiTietPhieuTraHang {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_phieu_tra_hang")
    private PhieuTraHang phieuTraHang;

    @ManyToOne
    @JoinColumn(name = "ma_chi_tiet_hoa_don")
    private ChiTietHoaDon chiTietHoaDon;

    @Column(name = "so_luong")
    private int soLuong;

    @Enumerated(EnumType.STRING)
    @Column(name = "truong_hop_doi_tra")
    private TruongHopDoiTraEnum truongHopDoiTra;

    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang_san_pham")
    private TinhTrangSanPhamEnum tinhTrangSanPham;

    @Column(name = "gia_tri_hoan_tra")
    private String giaTriHoanTra;

    @Column(name = "thanh_tien_hoan_tra")
    private double thanhTienHoanTra;
}
