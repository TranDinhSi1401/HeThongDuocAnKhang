package server.socket;

import server.dao.HibernateUtil;
import server.service.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server TCP lắng nghe kết nối từ Client.
 * Mỗi kết nối được xử lý trong một Thread riêng (Thread Pool).
 */
public class Server {

    private static final int PORT = 9090;
    private static final int MAX_THREADS = 50;

    public static void main(String[] args) {
        // Khởi tạo các Service (singleton, dùng chung cho mọi ClientHandler)
        SanPhamService sanPhamService = new SanPhamService();
        KhachHangService khachHangService = new KhachHangService();
        NhanVienService nhanVienService = new NhanVienService();
        HoaDonService hoaDonService = new HoaDonService();
        LoSanPhamService loSanPhamService = new LoSanPhamService();
        NhaCungCapService nhaCungCapService = new NhaCungCapService();
        KhuyenMaiService khuyenMaiService = new KhuyenMaiService();
        PhieuService phieuService = new PhieuService();
        CaLamService caLamService = new CaLamService();

        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);

        // Đóng EMF khi JVM thoát
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            pool.shutdown();
            HibernateUtil.close();
            System.out.println("Server đã đóng tài nguyên.");
        }));

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("=== Server Nhà Thuốc Dược An Khang đang chạy tại cổng " + PORT + " ===");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[Server] Client kết nối: " + clientSocket.getInetAddress());

                pool.execute(new ClientHandler(
                        clientSocket,
                        sanPhamService, khachHangService, nhanVienService,
                        hoaDonService, loSanPhamService, nhaCungCapService,
                        khuyenMaiService, phieuService, caLamService));
            }
        } catch (IOException e) {
            System.err.println("[Server] Lỗi khởi động server: " + e.getMessage());
        }
    }
}
