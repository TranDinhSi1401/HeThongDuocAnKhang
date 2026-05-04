package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = { "chiTietXuatLos", "chiTietPhieuTraHangs" })
@Entity
@Table(name = "chi_tiet_hoa_don")
public class ChiTietHoaDon {
    @Id
    @Column(name = "ma_chi_tiet_hoa_don")
    private String maChiTietHoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_don_vi_tinh")
    private DonViTinh donViTinh;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "don_gia")
    private double donGia;

    @Column(name = "giam_gia")
    private double giamGia;

    @Column(name = "thanh_tien")
    private double thanhTien;

    @OneToMany(mappedBy = "chiTietHoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietXuatLo> chiTietXuatLos;

    @OneToMany(mappedBy = "chiTietHoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietPhieuTraHang> chiTietPhieuTraHangs;
}
