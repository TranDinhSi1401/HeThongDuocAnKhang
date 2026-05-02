/*
 * @ (#) ChiTietHoaDonDto.java       1.0     5/2/2026
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
public class ChiTietHoaDonDto implements Serializable {
    private String maChiTietHoaDon;
    private HoaDonDto hoaDon;
    private DonViTinhDto donViTinh;
    private int soLuong;
    private double donGia;
    private double giamGia;
    private double thanhTien;
}
