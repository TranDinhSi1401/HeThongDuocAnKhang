/*
 * @ (#) PhieuTraHangDto.java       1.0     5/2/2026
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
public class PhieuTraHangDto implements Serializable {
    private String maPhieuTraHang;
    private LocalDateTime ngayLapPhieuTraHang;
    private double tongTienHoanTra;
    private NhanVienDto nhanVien;
    private HoaDonDto hoaDon;
}
