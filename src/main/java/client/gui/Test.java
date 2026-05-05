package client.gui;

import client.socket.SocketClient;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;

public class Test {
    public static void main(String[] args) {
        String sdt = "0909000004";
        Request request = new Request(CommandType.GET_KHACH_HANG_BY_SDT, sdt);
        Response response = SocketClient.getInstance().sendRequest(request);
        if (response.isSuccess()) {
            System.out.println(response);
            System.out.println(response.getData());
        } else {
            System.out.println("Lỗi kết nối");
            System.out.println(response);
        }
    }
}
