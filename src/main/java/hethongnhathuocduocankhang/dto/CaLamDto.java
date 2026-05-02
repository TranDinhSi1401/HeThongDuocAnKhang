/*
 * @ (#) CaLamDto.java       1.0     5/2/2026
 *
 * Copyright (c) 2026 .
 * IUH.
 * All rights reserved.
 */
package hethongnhathuocduocankhang.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CaLamDto implements Serializable {
    private String maCa;
    private String tenCa;
    private LocalTime thoiGianBatDau;
    private LocalTime thoiGianKetThuc;
}
