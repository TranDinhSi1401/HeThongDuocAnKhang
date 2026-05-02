/*
 * @ (#) NhanVienDto.java       1.0     5/2/2026
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
public class NhanVienDto implements Serializable {
    private String maNV;
    private String hoTenDem;
    private String ten;
    private String sdt;
    private String cccd;
    private boolean gioiTinh;
    private LocalDate ngaySinh;
    private String diaChi;
    private boolean nghiViec;
}
