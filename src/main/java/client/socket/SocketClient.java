package client.socket;

import common.network.Request;
import common.network.Response;

import java.io.*;
import java.net.Socket;

/**
 * Lớp tiện ích phía Client để gửi Request lên Server và nhận Response.
 *
 * Mỗi lần gọi {@link #sendRequest(Request)} sẽ mở một kết nối mới,
 * gửi yêu cầu, nhận phản hồi rồi đóng kết nối.
 */
public class SocketClient {

    private static final String HOST = "localhost";
    private static final int PORT = 9090;

    /** Singleton instance */
    private static SocketClient instance;

    private String host;
    private int port;

    private SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /** Lấy instance mặc định (localhost:9090). */
    public static synchronized SocketClient getInstance() {
        if (instance == null) {
            instance = new SocketClient(HOST, PORT);
        }
        return instance;
    }

    /** Lấy instance với host/port tuỳ chỉnh. */
    public static synchronized SocketClient getInstance(String host, int port) {
        if (instance == null) {
            instance = new SocketClient(host, port);
        }
        return instance;
    }

    /**
     * Gửi một {@link Request} lên Server và trả về {@link Response}.
     *
     * @param request yêu cầu cần gửi
     * @return phản hồi từ Server, hoặc Response.fail(...) nếu có lỗi mạng
     */
    public Response sendRequest(Request request) {
        try (Socket socket = new Socket(host, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            return Response.fail("Lỗi kết nối Server: " + e.getMessage());
        }
    }
}
