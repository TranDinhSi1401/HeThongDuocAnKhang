/*
 * @ (#) TaiKhoanDto.java       1.0     5/2/2026
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
public class TaiKhoanDto implements Serializable {
    private String tenDangNhap;
    private NhanVienDto nhanVien;
    private String matKhau;
    private boolean quanLy;
    private boolean quanLyLo;
    private boolean biKhoa;
    private String email;
    private LocalDateTime ngayTao;
}
