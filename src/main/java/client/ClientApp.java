package client;

import client.gui.DangNhapGUI;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;

/**
 * Điểm khởi động phía Client.
 *
 * Client KHÔNG kết nối DB trực tiếp. Mọi dữ liệu đều được
 * lấy từ Server qua Socket ({@link client.socket.SocketClient}).
 *
 * Cách chạy:
 *   1. Khởi động {@link server.socket.Server} trước.
 *   2. Chạy class này.
 */
public class ClientApp {

    public static void main(String[] args) {
        // ── Khởi động Look & Feel ──────────────────────────────
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);

        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("[ClientApp] Không thể áp dụng LookAndFeel: " + e.getMessage());
        }

        // ── Mở màn hình Đăng nhập ─────────────────────────────
        SwingUtilities.invokeLater(DangNhapGUI::new);
    }
}
