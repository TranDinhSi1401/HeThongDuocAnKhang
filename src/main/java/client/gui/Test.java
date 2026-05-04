package client.gui;

import client.socket.SocketClient;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;

public class Test {
    public static void main(String[] args) {
        Request request = new Request(CommandType.GET_CHI_TIET_SP, "SP-0001");
        Response response = SocketClient.getInstance().sendRequest(request);
        if (response.isSuccess()) {
            System.out.println(response.getData());
        } else {
            System.out.println("Lỗi kết nối");
            System.out.println(response);
        }
    }
}
