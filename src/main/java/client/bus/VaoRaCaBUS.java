package client.bus;

import client.socket.SocketClient;
import common.dto.*;
import common.network.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VaoRaCaBUS {
    
    public boolean kiemTraDangLamViec(String maNV) {
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_LSCL_DANG_LAM_BY_MA_NV, maNV));
        return (res.isSuccess() && res.getData() != null);
    }

    public CaLamDTO getCaLamHienTai() {
        LocalTime gioHienTai = LocalTime.now();
        String tenCa = "";
        
        if (gioHienTai.getHour() >= 6 && gioHienTai.getHour() < 14) {
            tenCa = "SANG";
        } else if (gioHienTai.getHour() >= 14 && gioHienTai.getHour() < 22){
            tenCa = "TOI";
        }
        
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.GET_CA_LAM_BY_TEN, tenCa));
        return (res.isSuccess()) ? (CaLamDTO) res.getData() : null;
    }

    public boolean xuLyVaoCa(String maNV, CaLamDTO caLam) throws Exception {
        LocalDate ngayHienTai = LocalDate.now();
        LocalTime gioHienTai = LocalTime.now();
        
        LichSuCaLamDTO ls = LichSuCaLamDTO.builder()
                .maNV(maNV)
                .maCa(caLam.getMaCa())
                .ngayLamViec(ngayHienTai.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .thoiGianVaoCa(gioHienTai)
                .build();
        
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.ADD_LICH_SU_CA_LAM, ls));
        return res.isSuccess();
    }

    public boolean xuLyRaCa(String maNV, CaLamDTO caLam, String ghiChu) throws Exception {
        LocalDate ngayHienTai = LocalDate.now();
        LocalTime gioHienTai = LocalTime.now();
        
        // Cần đảm bảo server handle Object[] này hoặc DTO tương ứng
        Response res = SocketClient.getInstance().sendRequest(new Request(CommandType.UPDATE_LICH_SU_CA_LAM, new Object[]{maNV, caLam.getMaCa(), ngayHienTai.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), gioHienTai, ghiChu}));
        return res.isSuccess();
    }
}
