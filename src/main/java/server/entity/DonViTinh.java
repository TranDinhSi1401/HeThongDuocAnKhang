package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = "chiTietHoaDons")
@Entity
@Table(name = "don_vi_tinh")
public class DonViTinh {
    @Id
    @Column(name = "ma_don_vi_tinh")
    private String maDonViTinh;

    @ManyToOne
    @JoinColumn(name = "ma_sp")
    private SanPham sanPham;

    @Column(name = "ten_don_vi_tinh")
    private String tenDonViTinh;

    @Column(name = "he_so_quy_doi")
    private int heSoQuyDoi;

    @Column(name = "gia_ban_theo_don_vi")
    private double giaBanTheoDonVi;

    @Column(name = "don_vi_tinh_co_ban", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean donViTinhCoBan;

    @Column(name = "da_xoa", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean daXoa;

    @OneToMany(mappedBy = "donViTinh", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietHoaDon> chiTietHoaDons;
}
