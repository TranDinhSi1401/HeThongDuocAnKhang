package client.gui;

import client.socket.SocketClient;
import common.dto.DoanhThu;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import server.dao.ChiTietHoaDonDAO;
import server.dao.HoaDonDAO;

import java.time.LocalDate;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        //String sdt = "0909000004";
//        Request request = new Request(CommandType.GET_DOANH_THU_THEO_NGAY_TRONG_KHOANG_THOI_GIAN, new Object[]{LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 30)});
//        Response response = SocketClient.getInstance().sendRequest(request);
//
//        if (response.isSuccess()) {
//            System.out.println(response);
//            System.out.println(response.getData());
//        } else {
//            System.out.println("Lỗi kết nối");
//            System.out.println(response);
//        }
        List<DoanhThu> doanhThuList = new HoaDonDAO().getDoanhThuTungNamTheoKhoang(2024, 2026);
        System.out.println(doanhThuList);
    }
}
