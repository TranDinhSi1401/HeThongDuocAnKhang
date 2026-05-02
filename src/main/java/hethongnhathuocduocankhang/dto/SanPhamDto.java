/*
 * @ (#) SanPhamDto.java       1.0     5/2/2026
 *
 * Copyright (c) 2026 .
 * IUH.
 * All rights reserved.
 */
package hethongnhathuocduocankhang.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SanPhamDto implements Serializable {
    private String maSP;
    private String ten;
    private String moTa;
    private String thanhPhan;
    private LoaiSanPhamEnum loaiSanPhamEnum;
    private int tonToiThieu;
    private int tonToiDa;
    private boolean daXoa;
}
