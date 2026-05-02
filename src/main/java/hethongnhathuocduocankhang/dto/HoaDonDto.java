/*
 * @ (#) HoaDonDto.java       1.0     5/2/2026
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
public class HoaDonDto implements Serializable {
    private String maHoaDon;
    private NhanVienDto nhanVien;
    private LocalDateTime ngayLapHoaDon;
    private KhachHangDto khachHang;
    private boolean chuyenKhoan;
    private double tongTien;
}
