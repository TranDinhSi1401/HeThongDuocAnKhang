package client;

import client.gui.DangNhapGUI;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf; // ★ thêm import này

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

        SwingUtilities.invokeLater(DangNhapGUI::new);
    }
}