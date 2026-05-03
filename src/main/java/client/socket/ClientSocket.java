package client.socket;

import common.dto.Request;
import common.dto.Response;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;

    public static Response sendRequest(Request request) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();

            return (Response) in.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new Response(false, "Lỗi kết nối máy chủ: " + e.getMessage(), null);
        }
    }
}
