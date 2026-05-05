package client.bus;

import client.socket.SocketClient;
import common.dto.NhanVienDTO;
import common.dto.TaiKhoanDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import java.util.ArrayList;
import java.util.List;

public class NhanVienBUS {

    public List<NhanVienDTO> getAllNhanVien() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_NHAN_VIEN, null));
        return (res.isSuccess()) ? (List<NhanVienDTO>) res.getData() : new ArrayList<>();
    }

    public NhanVienDTO getNhanVienTheoMaNV(String maNV) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_MA, maNV));
        return (res.isSuccess()) ? (NhanVienDTO) res.getData() : null;
    }

    public List<NhanVienDTO> timNVTheoTen(String ten) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_TEN, ten));
        return (res.isSuccess()) ? (List<NhanVienDTO>) res.getData() : new ArrayList<>();
    }

    public List<NhanVienDTO> timNVTheoSDT(String sdt) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_SDT, sdt));
        return (res.isSuccess()) ? (List<NhanVienDTO>) res.getData() : new ArrayList<>();
    }

    public List<NhanVienDTO> timNVTheoTrangThai(boolean daNghiViec) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NHAN_VIEN_BY_TRANG_THAI, daNghiViec));
        return (res.isSuccess()) ? (List<NhanVienDTO>) res.getData() : new ArrayList<>();
    }

    public int getMaNVCuoiCung() {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_MA_NV_CUOI, null));
        return (res.isSuccess()) ? (int) res.getData() : 0;
    }

    public boolean addNhanVien(NhanVienDTO nv) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_NHAN_VIEN, nv));
        return res.isSuccess();
    }

    public boolean suaNhanVien(String maNV, NhanVienDTO nv) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.SUA_NHAN_VIEN, new Object[]{maNV, nv}));
        return res.isSuccess();
    }

    public boolean xoaNhanVien(String maNV) {
        // Xóa tài khoản trước do ràng buộc khóa ngoại (nếu có)
        SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_TAI_KHOAN, maNV));
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.XOA_NHAN_VIEN, maNV));
        return res.isSuccess();
    }

    // Tài khoản
    public TaiKhoanDTO getTaiKhoanTheoMaNV(String maNV) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_TAI_KHOAN_BY_MA_NV, maNV));
        return (res.isSuccess()) ? (TaiKhoanDTO) res.getData() : null;
    }

    public boolean addTaiKhoan(TaiKhoanDTO tk) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_TAI_KHOAN, tk));
        return res.isSuccess();
    }

    public boolean capNhatTaiKhoan(TaiKhoanDTO tk) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.CAP_NHAT_TAI_KHOAN, tk));
        return res.isSuccess();
    }
}
