package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(exclude = { "sanPhamCungCaps", "chiTietPhieuNhaps" })
@Builder
@Entity
@Table(name = "nha_cung_cap")
public class NhaCungCap {
    @Id
    @Column(name = "ma_ncc")
    private String maNCC;

    @Column(name = "ten_ncc")
    private String tenNCC;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email")
    private String email;

    @Column(name = "da_xoa", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean daXoa;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SanPhamCungCap> sanPhamCungCaps;

    @OneToMany(mappedBy = "nhaCungCap", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChiTietPhieuNhap> chiTietPhieuNhaps;
}
