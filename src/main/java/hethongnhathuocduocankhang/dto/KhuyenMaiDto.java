/*
 * @ (#) KhuyenMaiDto.java       1.0     5/2/2026
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
public class KhuyenMaiDto implements Serializable {
    private String maKhuyenMai;
    private String moTa;
    private double phanTram;
    private LoaiKhuyenMaiEnum loaiKhuyenMai;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private int soLuongToiThieu;
    private int soLuongToiDa;
    private LocalDateTime ngayChinhSua;
}
