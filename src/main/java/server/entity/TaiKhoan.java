package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    private String maNV;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @Column(name = "mat_khau")
    private String matKhau;

    @Column(name = "la_quan_ly", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean quanLy;

    @Column(name = "bi_khoa", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean biKhoa;

    @Column(name = "email")
    private String email;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "da_xoa", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean daXoa;

    @Column(name = "quan_ly_lo", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean quanLyLo;
}
