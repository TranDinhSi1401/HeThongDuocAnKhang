/*
 * @ (#) PhieuDatHangDto.java       1.0     5/2/2026
 *
 * Copyright (c) 2026 .
 * IUH.
 * All rights reserved.
 */
package hethongnhathuocduocankhang.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PhieuDatHangDto implements Serializable {
    private String maPhieuDat;
    private NhaCungCapDto nhaCungCap;
    private LocalDateTime ngayLap;
    private NhanVienDto nhanVien;
    private double tongTien;
}
