package server.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "san_pham_cung_cap", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ma_sp", "ma_ncc" })
})
public class SanPhamCungCap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_sp")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "ma_ncc")
    private NhaCungCap nhaCungCap;

    @Column(name = "trang_thai_hop_tac", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean trangThaiHopTac;

    @Column(name = "gia_nhap")
    private double giaNhap;
}
