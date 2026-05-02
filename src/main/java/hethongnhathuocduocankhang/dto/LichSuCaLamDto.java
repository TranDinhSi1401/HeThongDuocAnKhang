/*
 * @ (#) LichSuCaLamDto.java       1.0     5/2/2026
 *
 * Copyright (c) 2026 .
 * IUH.
 * All rights reserved.
 */
package hethongnhathuocduocankhang.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LichSuCaLamDto implements Serializable {
    private NhanVienDto nhanVien;
    private LocalDate ngayLamViec;
    private CaLamDto caLam;
    private LocalTime thoiGianVaoCa;
    private LocalTime thoiGianRaCa;
    private String ghiChu;
}
