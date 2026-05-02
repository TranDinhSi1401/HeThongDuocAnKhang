/*
 * @ (#) LichSuLoDto.java       1.0     5/2/2026
 *
 * Copyright (c) 2026 .
 * IUH.
 * All rights reserved.
 */
package hethongnhathuocduocankhang.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LichSuLoDto implements Serializable {
    private String maLichSuLo;
    private LoSanPhamDto lo;
    private LocalDate thoiGian;
    private String hanhDong;
    private int soLuongSau;
    private String ghiChu;
    private NhanVienDto nv;
}
