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

    @Column(name = "quan_ly")
    private boolean quanLy;

    @Column(name = "bi_khoa")
    private boolean biKhoa;

    @Column(name = "email")
    private String email;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "da_xoa")
    private boolean daXoa;

    @Column(name = "quan_ly_lo")
    private boolean quanLyLo;
}
