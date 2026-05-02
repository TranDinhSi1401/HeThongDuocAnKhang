/*
 * @ (#) LoSanPhamDto.java       1.0     5/2/2026
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
public class LoSanPhamDto implements Serializable {
    private String maLoSanPham;
    private SanPhamDto sanPham;
    private int soLuong;
    private LocalDate ngaySanXuat;
    private LocalDate ngayHetHan;
    private boolean daHuy;
}
