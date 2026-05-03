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
@Table(name = "lich_su_lo")
public class LichSuLo implements java.io.Serializable {
    @Id
    @Column(name = "ma_lich_su_lo")
    private String maLichSuLo;

    @ManyToOne
    @JoinColumn(name = "ma_lo_san_pham")
    private LoSanPham loSanPham;

    @ManyToOne
    @JoinColumn(name = "ma_nv")
    private NhanVien nhanVien;

    @Column(name = "thoi_gian")
    private LocalDateTime thoiGian;

    @Column(name = "hanh_dong")
    private String hanhDong;

    @Column(name = "so_luong_sau")
    private int soLuongSau;

    @Column(name = "ghi_chu")
    private String ghiChu;
}
