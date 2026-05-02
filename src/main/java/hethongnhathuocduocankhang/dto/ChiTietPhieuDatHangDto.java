/*
 * @ (#) ChiTietPhieuDatHangDto.java       1.0     5/2/2026
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
public class ChiTietPhieuDatHangDto implements Serializable {
    private SanPhamDto sanPham;
    private int soLuong;
    private PhieuDatHangDto phieuDatHang;
    private double donGia;
    private double thanhTien;
}
