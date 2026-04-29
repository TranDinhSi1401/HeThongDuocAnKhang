package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = "khuyenMaiSanPhams")
@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai {
    @Id
    @Column(name = "ma_khuyen_mai")
    private String maKhuyenMai;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "phan_tram")
    private double phanTram;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_khuyen_mai")
    private LoaiKhuyenMaiEnum loaiKhuyenMai;

    @Column(name = "ngay_bat_dau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDateTime ngayKetThuc;

    @Column(name = "so_luong_toi_thieu")
    private int soLuongToiThieu;

    @Column(name = "so_luong_toi_da")
    private int soLuongToiDa;

    @Column(name = "ngay_chinh_sua")
    private LocalDateTime ngayChinhSua;

    @Column(name = "da_xoa")
    private boolean daXoa;

    @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<KhuyenMaiSanPham> khuyenMaiSanPhams;
}
