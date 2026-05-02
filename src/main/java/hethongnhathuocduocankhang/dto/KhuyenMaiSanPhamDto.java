/*
 * @ (#) KhuyenMaiSanPhamDto.java       1.0     5/2/2026
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
public class KhuyenMaiSanPhamDto implements Serializable {
    private KhuyenMaiDto khuyenMai;
    private SanPhamDto sanPham;
    private LocalDate ngayChinhSua;
}
