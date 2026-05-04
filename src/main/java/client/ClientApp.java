package client;

import client.gui.DangNhapGUI;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import hethongnhathuocduocankhang.connectDB.ConnectDB;   // ★ thêm import này

import javax.swing.*;

public class ClientApp {

    public static void main(String[] args) {
        // ── Look & Feel ──
        FlatRobotoFont.install();
        FlatLaf.setPreferredFontFamily(FlatRobotoFont.FAMILY);
        FlatLaf.setPreferredLightFontFamily(FlatRobotoFont.FAMILY_LIGHT);
        FlatLaf.setPreferredSemiboldFontFamily(FlatRobotoFont.FAMILY_SEMIBOLD);

        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("[ClientApp] LookAndFeel: " + e.getMessage());
        }

        // ★ Kết nối SQL Server cho các DAO legacy
        try {
            ConnectDB.getInstance().connect();
            System.out.println("[ClientApp] Kết nối CSDL thành công");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Không kết nối được CSDL SQL Server:\n" + e.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(DangNhapGUI::new);
    }
}