package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(exclude = "chiTietPhieuNhaps")
@Entity
@Table(name = "phieu_nhap")
public class PhieuNhap {
    @Id
    @Column(name = "ma_phieu_nhap")
    private String maPhieuNhap;

    @ManyToOne
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "tong_tien")
    private double tongTien;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietPhieuNhap> chiTietPhieuNhaps;
}
