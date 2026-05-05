package client.gui;

import client.socket.SocketClient;
import com.formdev.flatlaf.FlatClientProperties;
import common.dto.TaiKhoanDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;
import client.gui.*;
import client.menu.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Cửa sổ chính phía CLIENT (sau khi đăng nhập thành công).
 *
 * Nhận {@link TaiKhoanDTO} từ Server và lưu làm phiên làm việc.
 * Các panel màn hình (BanHangGUI, QuanLiNhanVienGUI, …) vẫn được
 * dùng lại từ package {@code client.gui},
 * trong khi tầng auth / đổi mật khẩu đã chuyển sang SocketClient.
 */
public class GiaoDienChinhGUI extends JFrame {

    // ── Session ──────────────────────────────────────────────
    private static TaiKhoanDTO currentTaiKhoan;
    private static GiaoDienChinhGUI instance;

    // ── Layout ───────────────────────────────────────────────
    private final MainForm mainForm;
    private static final Map<String, JPanel> cachedPanels = new HashMap<>();
    private static String         currentKey      = null;
    private static Supplier<JPanel> currentSupplier = null;
    
    public static TaiKhoanDTO getTkDTO() {
        return currentTaiKhoan;
    }



    // ── Constructor ──────────────────────────────────────────
    public GiaoDienChinhGUI(TaiKhoanDTO taiKhoan) {
        currentTaiKhoan = taiKhoan;
        instance        = this;

        initComponents();

        URL iconUrl = getClass().getResource("/resources/images/logo.png");
        if (iconUrl != null)
            setIconImage(Toolkit.getDefaultToolkit().createImage(iconUrl));

        setTitle("Hệ thống nhà thuốc Dược An Khang");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setResizable(false);

        mainForm = new MainForm();
        setContentPane(mainForm);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);

        // Mở dashboard theo quyền
        if (taiKhoan.isQuanLy()) {
            showFormByKey("dashboardQuanLi", DashBoardQuanLi::new);
        } else {
            showFormByKey("dashboardNhanVien", DashBoardNhanVien::new);
        }
        mainForm.setSelectedMenu(0, 0);

        // F1 – Hướng dẫn sử dụng
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "openHelp");
        getRootPane().getActionMap().put("openHelp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainForm.setSelectedMenu(8, 2);
                showFormByKey("huongDanSuDung", HuongDanSuDungGUI::new);
            }
        });

        // F5 – Tải lại trang hiện tại
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "reloadPage");
        getRootPane().getActionMap().put("reloadPage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentKey != null && currentSupplier != null)
                    reloadForm(currentKey, currentSupplier);
                else
                    Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    // ── Đăng xuất ────────────────────────────────────────────
    public static void logout() {
        int confirm = JOptionPane.showConfirmDialog(instance,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            instance.dispose();
            cachedPanels.clear();
            currentTaiKhoan = null;
            SwingUtilities.invokeLater(DangNhapGUI::new);
        }
    }

    // ── Đổi mật khẩu (qua Socket) ────────────────────────────
    public static void taoPanelDoiMatKhau() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPasswordField txtCu    = new JPasswordField(20);
        JPasswordField txtMoi   = new JPasswordField(20);
        JPasswordField txtXnMoi = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Mật khẩu cũ:"),        gbc);
        gbc.gridx = 1; panel.add(txtCu, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Mật khẩu mới:"),       gbc);
        gbc.gridx = 1; panel.add(txtMoi, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Xác nhận mật khẩu:"), gbc);
        gbc.gridx = 1; panel.add(txtXnMoi, gbc);

        int result = JOptionPane.showConfirmDialog(instance, panel, "Đổi mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String oldPass     = new String(txtCu.getPassword()).trim();
        String newPass     = new String(txtMoi.getPassword()).trim();
        String confirmPass = new String(txtXnMoi.getPassword()).trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(instance, "Vui lòng nhập đầy đủ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,}$")) {
            JOptionPane.showMessageDialog(instance,
                    "Mật khẩu phải ≥ 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt!",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(instance, "Mật khẩu xác nhận không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (oldPass.equalsIgnoreCase(newPass)) {
            JOptionPane.showMessageDialog(instance, "Mật khẩu mới phải khác mật khẩu cũ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gửi request đổi mật khẩu qua Socket
        // Server sẽ kiểm tra mật khẩu cũ và cập nhật mật khẩu mới có hash
        Response res = SocketClient.getInstance().sendRequest(
                new Request(CommandType.UPDATE_MAT_KHAU,
                        new Object[]{currentTaiKhoan.getMaNV(), oldPass, newPass}));

        if (res.isSuccess() && Boolean.TRUE.equals(res.getData())) {
            JOptionPane.showMessageDialog(instance, "Đổi mật khẩu thành công!");
        } else {
            JOptionPane.showMessageDialog(instance, "Mật khẩu cũ không đúng hoặc có lỗi xảy ra!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Hiển thị About ───────────────────────────────────────
    public static void showAboutGUI() {
        new AboutGUI(instance).setVisible(true);
    }

    // ── Quản lý form panel ───────────────────────────────────
    public static void showForm(Component component) {
        component.applyComponentOrientation(instance.getComponentOrientation());
        instance.mainForm.showForm(component);
    }

    public static void showFormByKey(String key, Supplier<JPanel> creator) {
        currentKey      = key;
        currentSupplier = creator;
        JPanel panel = cachedPanels.computeIfAbsent(key, k -> creator.get());
        showForm(panel);
    }

    public static void reloadForm(String key, Supplier<JPanel> supplier) {
        cachedPanels.remove(key);
        showFormByKey(key, supplier);
    }

    public static void setSelectedMenu(int index, int subIndex) {
        instance.mainForm.setSelectedMenu(index, subIndex);
    }

    // ── Getters ──────────────────────────────────────────────
    public static TaiKhoanDTO getCurrentTaiKhoan() {
        return currentTaiKhoan;
    }

    /** Compat helper: trả về true/false theo quyền quản lý */
    public static boolean isQuanLy() {
        return currentTaiKhoan != null && currentTaiKhoan.isQuanLy();
    }

    public static boolean isQuanLyLo() {
        return currentTaiKhoan != null && currentTaiKhoan.isQuanLyLo();
    }

    // ── Init components (minimal) ─────────────────────────────
    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup().addGap(0, 719, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup().addGap(0, 521, Short.MAX_VALUE));
        pack();
    }

    public static void showNhacNhoDoiMatKhau(boolean can) {
        if (can) JOptionPane.showMessageDialog(instance,
                "Bạn vừa đặt lại mật khẩu.\nHãy đổi lại mật khẩu trong Tiện ích → Đổi mật khẩu.",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
}
