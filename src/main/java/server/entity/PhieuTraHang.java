package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "chiTietPhieuTraHangs")
@Entity
@Table(name = "phieu_tra_hang")
public class PhieuTraHang {
    @Id
    @Column(name = "ma_phieu_tra_hang")
    private String maPhieuTraHang;

    @ManyToOne
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ma_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "ngay_lap_phieu_tra_hang")
    private LocalDateTime ngayLapPhieuTraHang;

    @Column(name = "tong_tien_hoa_tra")
    private double tongTienHoaTra;

    @OneToMany(mappedBy = "phieuTraHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietPhieuTraHang> chiTietPhieuTraHangs;
}
