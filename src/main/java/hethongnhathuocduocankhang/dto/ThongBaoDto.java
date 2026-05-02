/*
 * @ (#) ThongBaoDto.java       1.0     5/2/2026
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
public class ThongBaoDto implements Serializable {
    public static final String HET_HAN = "HẾT HẠN";
    public static final String CANH_BAO = "CẢNH BÁO";

    private String maLoSanPham;
    private String maSanPham;
    private String tenSanPham;
    private LocalDate ngayHetHan;
    private int soLuong;
    private String loaiThongBao;
    private int soNgayConLai;
}
