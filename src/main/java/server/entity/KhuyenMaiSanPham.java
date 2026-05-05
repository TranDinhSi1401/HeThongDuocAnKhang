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
@Table(name = "khuyen_mai_san_pham", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "ma_khuyen_mai", "ma_sp" })
})
public class KhuyenMaiSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ma_khuyen_mai")
    private KhuyenMai khuyenMai;

    @ManyToOne
    @JoinColumn(name = "ma_sp")
    private SanPham sanPham;

    @Column(name = "ngay_chinh_sua")
    private LocalDateTime ngayChinhSua;
}
