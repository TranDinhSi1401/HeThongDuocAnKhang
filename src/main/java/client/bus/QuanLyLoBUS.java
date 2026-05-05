package client.bus;

import client.socket.SocketClient;
import common.dto.LoSanPhamDTO;
import common.dto.DonViTinhDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class QuanLyLoBUS {
    
    public List<LoSanPhamDTO> getAllLoSanPham() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_LO_SAN_PHAM, null));
        return (res.isSuccess()) ? (List<LoSanPhamDTO>) res.getData() : new ArrayList<>();
    }

    public List<LoSanPhamDTO> getAllLo() {
        return getAllLoSanPham();
    }

    public List<LoSanPhamDTO> getLoSapHetHan(int soThang) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_LO_SAP_HET_HAN, soThang));
        return (res.isSuccess()) ? (List<LoSanPhamDTO>) res.getData() : new ArrayList<>();
    }

    public List<LoSanPhamDTO> getLoSapHetHang(int soLuong) {
        List<LoSanPhamDTO> all = getAllLoSanPham();
        List<LoSanPhamDTO> result = new ArrayList<>();
        for(LoSanPhamDTO lo : all) {
            if(lo.getSoLuong() <= soLuong) result.add(lo);
        }
        return result;
    }

    public List<LoSanPhamDTO> getLoKhongHuy() {
        return getAllLoSanPham(); 
    }

    public String tinhTrangThaiLo(LoSanPhamDTO lo) {
        if (lo.getSoLuong() == 0) return "Hết hàng";
        if (lo.getNgayHetHan().isBefore(java.time.LocalDate.now())) return "Hết hạn";
        return "Đang bán";
    }

    public Object[] toTableRow(LoSanPhamDTO lo) {
        Response resDVT = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_DVT_CO_BAN_BY_MA_SP, lo.getMaSP()));
        DonViTinhDTO dvt = (resDVT.isSuccess()) ? (DonViTinhDTO) resDVT.getData() : null;
        
        return new Object[]{
            lo.getMaLoSanPham(),
            lo.getMaSP(),
            lo.getSoLuong(),
            (dvt != null ? dvt.getTenDonViTinh() : ""),
            lo.getNgaySanXuat(),
            lo.getNgayHetHan(),
            tinhTrangThaiLo(lo)
        };
    }

    public String chuyenDinhDang(String dateStr) {
        return dateStr;
    }

    public ArrayList<LoSanPhamDTO> timKiemLoVoiNhieuDieuKien(String tieuChi, String noiDung, String trangThai) {
        List<LoSanPhamDTO> all = getAllLoSanPham();
        ArrayList<LoSanPhamDTO> result = new ArrayList<>();
        for (LoSanPhamDTO lo : all) {
            boolean match = true;
            if (noiDung != null && !noiDung.isEmpty()) {
                if ("Mã lô".equals(tieuChi) && !lo.getMaLoSanPham().contains(noiDung)) match = false;
                else if ("Mã sản phẩm".equals(tieuChi) && !lo.getMaSP().contains(noiDung)) match = false;
            }
            // logic for trangThai...
            if (match) result.add(lo);
        }
        return result;
    }

    public int[] layThongKeLoTheoTrangThaiArray() {
        List<LoSanPhamDTO> all = getAllLoSanPham();
        int[] stats = new int[4]; // [0]=Còn hạn, [1]=Sắp hết hạn, [2]=Hết hạn, [3]=Đã hủy
        for (LoSanPhamDTO lo : all) {
            String tt = tinhTrangThaiLo(lo);
            if ("Đang bán".equals(tt)) stats[0]++;
            else if ("Sắp hết hạn".equals(tt)) stats[1]++;
            else if ("Hết hạn".equals(tt)) stats[2]++;
            else if ("Hết hàng".equals(tt)) stats[3]++; // Giả sử hết hàng là đã hủy/hết
        }
        return stats;
    }

    public Map<String, Object> thongKe(List<LoSanPhamDTO> ds) {
        Map<String, Object> result = new HashMap<>();
        List<LoSanPhamDTO> dsLoHetHan = new ArrayList<>();
        for (LoSanPhamDTO lo : ds) {
            if (lo.getNgayHetHan().isBefore(java.time.LocalDate.now())) {
                dsLoHetHan.add(lo);
            }
        }
        result.put("dsLoHetHan", dsLoHetHan);
        return result;
    }
}
