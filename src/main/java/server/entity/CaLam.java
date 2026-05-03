package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = "lichSuCaLams")
@Entity
@Table(name = "ca_lam")
public class CaLam implements java.io.Serializable {
    @Id
    @Column(name = "ma_ca")
    private String maCa;

    @Column(name = "ten_ca")
    private String tenCa;

    @Column(name = "thoi_gian_bat_dau")
    private String thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    private String thoiGianKetThuc;

    @OneToMany(mappedBy = "caLam", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LichSuCaLam> lichSuCaLams;
}
