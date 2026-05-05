package server.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = {"maVachSanPhams", "khuyenMaiSanPhams", "sanPhamCungCaps", "loSanPhams", "donViTinhs"})
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @Column(name = "ma_sp")
    private String maSP;

    @Column(name = "ten")
    private String ten;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "thanh_phan")
    private String thanhPhan;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_san_pham")
    private LoaiSanPhamEnum loaiSanPham;

    @Column(name = "ton_toi_thieu")
    private int tonToiThieu;

    @Column(name = "ton_toi_da")
    private int tonToiDa;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MaVachSanPham> maVachSanPhams;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KhuyenMaiSanPham> khuyenMaiSanPhams;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SanPhamCungCap> sanPhamCungCaps;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LoSanPham> loSanPhams;

    @Column(name = "da_xoa")
    private Boolean daXoa;

    @OneToMany(mappedBy = "sanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DonViTinh> donViTinhs;
}
