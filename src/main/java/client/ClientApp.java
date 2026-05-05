package client;

import client.gui.DangNhapGUI;
import client.gui.SplashScreen;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import java.awt.*;

public class ClientApp {

    public static void main(String[] args) {
        // ── Look & Feel ──
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("client.theme");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacLightLaf.setup();

        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("[ClientApp] LookAndFeel: " + e.getMessage());
        }

        // ── SplashScreen ──
        SplashScreen splash = new SplashScreen(0); // dur=0: sẽ đóng khi worker hoàn tất

        SwingWorker<Void, Void> startWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                setProgress(10);
                firePropertyChange("message", null, "Đang khởi động hệ thống...");
                Thread.sleep(400);

                setProgress(40);
                firePropertyChange("message", null, "Đang kết nối đến máy chủ...");
                Thread.sleep(500);

                setProgress(80);
                firePropertyChange("message", null, "Đang tải giao diện...");
                Thread.sleep(400);

                setProgress(100);
                firePropertyChange("message", null, "Sẵn sàng!");
                Thread.sleep(200);
                return null;
            }

            @Override
            protected void done() {
                splash.setVisible(false);
                splash.dispose();
                SwingUtilities.invokeLater(DangNhapGUI::new);
            }
        };

        splash.showDuring(startWorker);
    }
}