package hethongnhathuocduocankhang.bus;

import client.socket.SocketClient;
import common.dto.*;
import common.network.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VaoRaCaBUS {
    
    /**
     * Kiểm tra xem nhân viên đã vào ca trong ngày chưa để set trạng thái nút
     */
    public boolean kiemTraDangLamViec(String maNV) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_LSCL_DANG_LAM_BY_MA_NV, maNV));
        return (res.isSuccess() && res.getData() != null);
    }

    public CaLamDTO getCaLamHienTai() {
        LocalTime gioHienTai = LocalTime.now();
        String maCa = "";
        
        if (gioHienTai.getHour() >= 6 && gioHienTai.getHour() < 14) {
            maCa = "SANG";
        } else if (gioHienTai.getHour() >= 14 && gioHienTai.getHour() < 22){
            maCa = "TOI";
        }
        
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CA_LAM_BY_TEN, maCa));
        return (res.isSuccess()) ? (CaLamDTO) res.getData() : null;
    }

    public boolean xuLyVaoCa(String maNV, CaLamDTO caLam) throws Exception {
        LocalDate ngayHienTai = LocalDate.now();
        LocalTime gioHienTai = LocalTime.now();
        
        LichSuCaLamDTO ls = LichSuCaLamDTO.builder()
                .maNV(maNV)
                .maCa(caLam.getMaCa())
                .ngayLam(ngayHienTai)
                .gioVao(gioHienTai)
                .build();
        
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_LICH_SU_CA_LAM, ls));
        return res.isSuccess();
    }

    public boolean xuLyRaCa(String maNV, CaLamDTO caLam, String ghiChu) throws Exception {
        LocalDate ngayHienTai = LocalDate.now();
        LocalTime gioHienTai = LocalTime.now();
        
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.UPDATE_LICH_SU_CA_LAM, new Object[]{maNV, caLam.getMaCa(), ngayHienTai, gioHienTai, ghiChu}));
        return res.isSuccess();
    }
}