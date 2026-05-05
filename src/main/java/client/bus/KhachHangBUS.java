package client.bus;

import client.socket.SocketClient;
import common.dto.KhachHangDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import java.util.ArrayList;
import java.util.List;

public class KhachHangBUS {

    public List<KhachHangDTO> getAllKhachHang() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_KHACH_HANG, null));
        return (res.isSuccess()) ? (List<KhachHangDTO>) res.getData() : new ArrayList<>();
    }

    public KhachHangDTO getKhachHangByMa(String maKH) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KHACH_HANG_BY_MA, maKH));
        return (res.isSuccess()) ? (KhachHangDTO) res.getData() : null;
    }

    public KhachHangDTO getKhachHangBySdt(String sdt) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KHACH_HANG_BY_SDT, sdt));
        return (res.isSuccess()) ? (KhachHangDTO) res.getData() : null;
    }

    public List<KhachHangDTO> getKhachHangByTen(String ten) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_KHACH_HANG_BY_TEN, ten));
        return (res.isSuccess()) ? (List<KhachHangDTO>) res.getData() : new ArrayList<>();
    }

    public List<KhachHangDTO> searchKhachHangBySdt(String sdt) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.SEARCH_KHACH_HANG_BY_SDT, sdt));
        return (res.isSuccess()) ? (List<KhachHangDTO>) res.getData() : new ArrayList<>();
    }

    public int getMaKHCuoi() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_KH_CUOI, null));
        return (res.isSuccess()) ? (int) res.getData() : 0;
    }

    public boolean addKhachHang(KhachHangDTO kh) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_KHACH_HANG, kh));
        return res.isSuccess();
    }

    public boolean suaKhachHang(String maKH, KhachHangDTO kh) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.SUA_KHACH_HANG, new Object[]{maKH, kh}));
        return res.isSuccess();
    }

    public boolean xoaKhachHang(String maKH) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_KHACH_HANG, maKH));
        return res.isSuccess();
    }

    public boolean updateDiemTichLuy(int diem, String maKH) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.UPDATE_DIEM_TICH_LUY, new Object[]{diem, maKH}));
        return res.isSuccess();
    }
}
