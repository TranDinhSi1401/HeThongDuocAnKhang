package client.bus;

import client.socket.SocketClient;
import common.dto.*;
import common.network.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuDatHangBUS {
    
    public List<SanPhamDTO> danhsachSanPham(){
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_ALL_SAN_PHAM, null));
        return (res.isSuccess()) ? (List<SanPhamDTO>) res.getData() : new ArrayList<>();
    }
    
    public SanPhamDTO timSanPham(String ma){
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_SAN_PHAM_BY_MA, ma));
        return (res.isSuccess()) ? (SanPhamDTO) res.getData() : null;
    }
    
    public NhaCungCapDTO timNhaCC(String ma){
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_NCC_BY_MA, ma));
        return (res.isSuccess()) ? (NhaCungCapDTO) res.getData() : null;
    }
    
    public DonViTinhDTO giaSanPham(String ma){
        // Giả sử lấy đơn vị tính theo mã sản phẩm hoặc mã DVT
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_DVT_BY_MA, ma));
        return (res.isSuccess()) ? (DonViTinhDTO) res.getData() : null;
    }
       
    public double tinh(int sl, DonViTinhDTO x){
        if (x == null) return 0;
        return x.getGiaBanTheoDonVi() * sl;
    }
}
