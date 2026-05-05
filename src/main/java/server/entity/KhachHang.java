package server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString(exclude = "hoaDons")
@Entity
@Table(name = "khach_hang")
public class KhachHang {
    @Id
    @Column(name = "ma_kh")
    private String maKH;

    @Column(name = "ho_ten_dem")
    private String hoTenDem;

    @Column(name = "ten")
    private String ten;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "diem_tich_luy")
    private int diemTichLuy;

    @Column(name = "da_xoa")
    private Boolean daXoa;

    @OneToMany(mappedBy = "khachHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HoaDon> hoaDons;
}
