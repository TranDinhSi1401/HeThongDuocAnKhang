package server.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Entity
@Table(name = "chi_tiet_phieu_nhap", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ma_phieu_nhap", "ma_lo_san_pham"})
})
public class ChiTietPhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_phieu_nhap")
    private PhieuNhap phieuNhap;

    @ManyToOne
    @JoinColumn(name = "ma_lo_san_pham")
    private LoSanPham loSanPham;

    @ManyToOne
    @JoinColumn(name = "ma_ncc")
    private NhaCungCap nhaCungCap;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "so_luong_yeu_cau")
    private int soLuongYeuCau;

    @Column(name = "don_gia")
    private double donGia;

    @Column(name = "thanh_tien")
    private double thanhTien;

    @Column(name = "ghi_chu")
    private String ghiChu;
}
