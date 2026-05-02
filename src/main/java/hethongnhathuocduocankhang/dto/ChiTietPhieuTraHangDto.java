/*
 * @ (#) ChiTietPhieuTraHangDto.java       1.0     5/2/2026
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
public class ChiTietPhieuTraHangDto implements Serializable {
    private PhieuTraHangDto phieuTraHang;
    private ChiTietHoaDonDto chiTietHoaDon;
    private int soLuong;
    private TruongHopDoiTraEnum truongHopDoiTra;
    private TinhTrangSanPhamEnum tinhTrangSanPham;
    private String giaTriHoanTra;
    private double thanhTienHoanTra;
}
