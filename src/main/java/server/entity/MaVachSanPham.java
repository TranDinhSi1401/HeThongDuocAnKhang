package server.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = "sanPham")
@Entity
@Table(name = "ma_vach_san_pham")
public class MaVachSanPham implements java.io.Serializable {
    @Id
    @Column(name = "ma_vach")
    private String maVach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sp", nullable = false)
    private SanPham sanPham;
}
