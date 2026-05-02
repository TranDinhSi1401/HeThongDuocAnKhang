/*
 * @ (#) MaVachSanPhamDto.java       1.0     5/2/2026
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
public class MaVachSanPhamDto implements Serializable {
    private SanPhamDto sanPham;
    private String maVach;
}
