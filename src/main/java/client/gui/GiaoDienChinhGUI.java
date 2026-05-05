/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.gui;

import client.socket.SocketClient;
import com.formdev.flatlaf.FlatClientProperties;
import common.dto.TaiKhoanDTO;
import common.network.CommandType;
import common.network.Request;
<<<<<<< HEAD
import common.network.Response;
import client.gui.*;
import client.menu.Menu;
=======
import lombok.Getter;
>>>>>>> master

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
<<<<<<< HEAD
 * Nhận {@link TaiKhoanDTO} từ Server và lưu làm phiên làm việc.
 * Các panel màn hình (BanHangGUI, QuanLiNhanVienGUI, …) vẫn được
 * dùng lại từ package {@code client.gui},
 * trong khi tầng auth / đổi mật khẩu đã chuyển sang SocketClient.
=======
 * @author trand
>>>>>>> master
 */
@Getter
public class GiaoDienChinhGUI extends JFrame{
    @Getter
    private static TaiKhoanDTO tk = null;
    private static GiaoDienChinhGUI app;
    private final MainForm mainForm;
    private static final Map<String, JPanel> cachedPanels = new HashMap<>();
    private static String currentKey = null;
    private static Supplier<JPanel> currentSupplier = null;
    
    public static TaiKhoanDTO getTkDTO() {
        return currentTaiKhoan;
    }



    public static void setCurrentForm(String key, Supplier<JPanel> supplier) {
       currentKey = key;
        currentSupplier = supplier;
    }
        
    public GiaoDienChinhGUI(TaiKhoanDTO tk) {
        if (tk != null) {
            GiaoDienChinhGUI.tk = tk;
        }
        app = this;
        initComponents();

        URL url = GiaoDienChinhGUI.class.getResource("/images/logo.png");
        if (url != null) {
            Image icon = Toolkit.getDefaultToolkit().createImage(url);
            this.setIconImage(icon);
        }
        setTitle("Hệ thống nhà thuốc Dược An Khang");

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        mainForm = new MainForm();
        setContentPane(mainForm);
        setResizable(false);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        if(GiaoDienChinhGUI.getTk() != null && GiaoDienChinhGUI.getTk().isQuanLy()) {
            // TODO: Hiển thị dashboard quản lý
            //GiaoDienChinhGUI.showFormByKey("dashboarQuanLi", DashBoardQuanLi::new);
        } else {
            // TODO: Hiển thị dashboard nhân viên
            //GiaoDienChinhGUI.showFormByKey("dashboardNhanVien", DashBoardNhanVien::new);
        } 
        mainForm.setSelectedMenu(0, 0);
        // Phím tắt F1 mở hướng dẫn sử dụng và chọn menu tương ứng
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "openHelp");
        getRootPane().getActionMap().put("openHelp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainForm.setSelectedMenu(8, 2);
                //GiaoDienChinhGUI.showFormByKey("huongDanSuDung", HuongDanSuDungGUI::new);
            }
        });
        // Phím tắt F5 để reload trang hiện tại
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "reloadPage");

        getRootPane().getActionMap().put("reloadPage", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentKey != null && currentSupplier != null) {
                    // Giữ menu đang chọn
                    System.out.println("Reload form: " + currentKey);
                    reloadForm(currentKey, currentSupplier);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }
    
    public static void reloadForm(String key, Supplier<JPanel> supplier) {
        cachedPanels.remove(key); // xóa cache cũ
        showFormByKey(key, supplier); // tạo form mới
    }

    public static void showForm(Component component) {
        if (app != null && app.mainForm != null) {
            app.mainForm.showForm(component);
        }
    }
    
    public static JPanel getOrCreatePanel(String key, Supplier<JPanel> creator) {
        return cachedPanels.computeIfAbsent(key, k -> creator.get());
    }

    public static void showFormByKey(String key, Supplier<JPanel> creator) {
        // Lưu context phục vụ F5 reload
        currentKey = key;
        currentSupplier = creator;
        // Lấy hoặc tạo panel từ cache
        JPanel panel = getOrCreatePanel(key, creator);
        // Hiển thị panel
        showForm(panel);
    }
    
    public static void logout() {
        int confirm = JOptionPane.showConfirmDialog(app,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
                app.dispose(); // đóng cửa sổ hiện tại
                cachedPanels.clear();
                new DangNhapGUI().setVisible(true);
        }
    }

    public static void setSelectedMenu(int index, int subIndex) {
        app.mainForm.setSelectedMenu(index, subIndex);
    }
    
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }
    
    public static void taoPanelDoiMatKhau() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Label và PasswordField cho Mật khẩu cũ
        JLabel lblMatKhauCu = new JLabel("Mật khẩu cũ:");
        JTextField txtMatKhauCu = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblMatKhauCu, gbc);
        gbc.gridx = 1;
        panel.add(txtMatKhauCu, gbc);

        // Label và PasswordField cho Mật khẩu mới
        JLabel lblMatKhauMoi = new JLabel("Mật khẩu mới:");
        JTextField txtMatKhauMoi = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblMatKhauMoi, gbc);
        gbc.gridx = 1;
        panel.add(txtMatKhauMoi, gbc);

        // Label và PasswordField cho Xác nhận mật khẩu mới
        JLabel lblXacNhan = new JLabel("Xác nhận mật khẩu mới:");
        JTextField txtXacNhan = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblXacNhan, gbc);
        gbc.gridx = 1;
        panel.add(txtXacNhan, gbc);

        int result = JOptionPane.showConfirmDialog(
                app,
                panel,
                "Đổi mật khẩu",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPass = txtMatKhauCu.getText().trim();
            String newPass = txtMatKhauMoi.getText().trim();
            String confirmPass = txtXacNhan.getText().trim();
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                JOptionPane.showMessageDialog(app, "Vui lòng nhập đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else if (!newPass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\W).{8,}$")) {
                JOptionPane.showMessageDialog(app, "Mật khẩu phải có tối thiểu 8 ký tự, gồm chữ hoa, chữ thường, chữ số và ký tự đặc biệt!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(app, "Mật khẩu xác nhận không khớp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else if (oldPass.equalsIgnoreCase(newPass)) {
                JOptionPane.showMessageDialog(app, "Mật khẩu mới phải khác mật khẩu cũ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            } else {
                // Gọi business logic xử lý đổi mật khẩu
                Request updatePassRequest = new Request(CommandType.UPDATE_MAT_KHAU, new Object[]{GiaoDienChinhGUI.getTk().getMaNV(), oldPass, newPass});
                boolean check = (boolean) SocketClient.getInstance().sendRequest(updatePassRequest).getData();
                if(check) {
                    JOptionPane.showMessageDialog(app, "Đổi mật khẩu thành công!");
                } else {
                    JOptionPane.showMessageDialog(app, "Mật khẩu cũ không đúng!");
                }             
            }
        }
    }
    
    public static void showNhacNhoDoiMatKhau(boolean canDoiMatKhau) {
        if(canDoiMatKhau) {
            JOptionPane.showMessageDialog(app, "Bạn vừa đặt lại mật khẩu\nHãy đổi lại mật khẩu mới trong phần tiện ích -> đổi mật khẩu", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }
}
