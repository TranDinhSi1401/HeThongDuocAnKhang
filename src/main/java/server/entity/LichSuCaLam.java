package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
@Table(name = "lich_su_ca_lam")
public class LichSuCaLam implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "ma_ca")
    private CaLam caLam;

    @Column(name = "ngay_lam_viec")
    private String ngayLamViec;

    @Column(name = "thoi_gian_vao_ca")
    private LocalTime thoiGianVaoCa;

    @Column(name = "thoi_gian_ra_ca")
    private LocalTime thoiGianRaCa;

    @Column(name = "ghi_chu")
    private String ghiChu;
}
