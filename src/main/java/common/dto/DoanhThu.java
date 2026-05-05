/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author trand
 */
@Setter
@Getter
public class DoanhThu implements java.io.Serializable {
    private String thoiGian;
    private long tongHoaDon;
    private double tongDoanhThu;

    public DoanhThu(String thoiGian, int tongHoaDon, double tongDoanhThu) {
        this.thoiGian = thoiGian;
        this.tongHoaDon = tongHoaDon;
        this.tongDoanhThu = tongDoanhThu;
    }

    public DoanhThu(Object thoiGian, long tongHoaDon, double tongDoanhThu) {
        this.thoiGian = (thoiGian != null) ? thoiGian.toString() : "";
        this.tongHoaDon = tongHoaDon;
        this.tongDoanhThu = tongDoanhThu;
    }

    @Override
    public String toString() {
        return "DoanhThu{" + "thoiGian=" + thoiGian + ", tongHoaDon=" + tongHoaDon + ", tongDoanhThu=" + tongDoanhThu + '}';
    }
}
