/*
 * @ (#) ChiTietPhieuNhapDto.java       1.0     5/2/2026
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
public class ChiTietPhieuNhapDto implements Serializable {
    private PhieuNhapDto phieuNhap;
    private LoSanPhamDto loSanPham;
    private NhaCungCapDto ncc;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private int soLuongYeuCau;
    private String ghiChu;
}
