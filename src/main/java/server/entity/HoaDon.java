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
@ToString(exclude = { "chiTietHoaDons", "phieuTraHangs" })
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @ManyToOne
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ma_kh")
    private KhachHang khachHang;

    @Column(name = "ngay_lap_hoa_don")
    private LocalDateTime ngayLapHoaDon;

    @Column(name = "chuyen_khoan")
    private Boolean chuyenKhoan;

    @Column(name = "tong_tien")
    private double tongTien;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PhieuTraHang> phieuTraHangs;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietHoaDon> chiTietHoaDons;
}
