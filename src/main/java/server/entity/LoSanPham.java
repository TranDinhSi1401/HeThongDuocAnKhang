package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = {"chiTietPhieuNhaps", "lichSuLos", "chiTietXuatLos"})
@Entity
@Table(name = "lo_san_pham")
public class LoSanPham {
    @Id
    @Column(name = "ma_lo_san_pham")
    private String maLoSanPham;

    @ManyToOne
    @JoinColumn(name = "ma_sp")
    private SanPham sanPham;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "ngay_san_xuat")
    private LocalDate ngaySanXuat;

    @Column(name = "ngay_het_han")
    private LocalDate ngayHetHan;

    @Column(name = "da_huy")
    private Boolean daHuy;

    @OneToMany(mappedBy = "loSanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietPhieuNhap> chiTietPhieuNhaps;

    @OneToMany(mappedBy = "loSanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LichSuLo> lichSuLos;

    @OneToMany(mappedBy = "loSanPham", cascade = CascadeType.ALL, orphanRemoval = true)
    private  Set<ChiTietXuatLo> chiTietXuatLos;
}
