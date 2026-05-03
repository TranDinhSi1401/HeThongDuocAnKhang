package client.gui;

import client.socket.SocketClient;
import com.formdev.flatlaf.FlatClientProperties;
import common.dto.TaiKhoanDTO;
import common.network.CommandType;
import common.network.Request;
import common.network.Response;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

/**
 * Màn hình Đăng nhập phía CLIENT.
 *
 * Không kết nối DB trực tiếp – tất cả đi qua {@link SocketClient}.
 * Khi đăng nhập thành công, server trả về {@link TaiKhoanDTO} và
 * ứng dụng chuyển sang {@link GiaoDienChinhGUI}.
 */
public class DangNhapGUI extends JFrame {

    // ── UI components ────────────────────────────────────────
    private JTextField      txtTaiKhoan;
    private JPasswordField  txtMatKhau;
    private JButton         btnDangNhap;
    private JButton         btnCheMatKhau;
    private JLabel          lblQuenMatKhau;

    private boolean showPassword = false;

    // ── Constructor ──────────────────────────────────────────
    public DangNhapGUI() {
        buildUI();
        setTitle("Đăng nhập – Nhà Thuốc Dược An Khang");
        setSize(850, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // App icon
        URL iconUrl = getClass().getResource("/resources/images/logo.png");
        if (iconUrl != null)
            setIconImage(Toolkit.getDefaultToolkit().createImage(iconUrl));

        setVisible(true);
    }

    // ── Build UI ─────────────────────────────────────────────
    private void buildUI() {
        // Nền xanh dương
        JPanel pCenter = new JPanel(new GridBagLayout());
        pCenter.setBackground(new Color(25, 118, 210));
        setContentPane(pCenter);

        // Card trắng
        JPanel card = new JPanel(null); // AbsoluteLayout thay thế
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(450, 370));
        card.putClientProperty(FlatClientProperties.STYLE, "arc:20");

        // Logo
        JLabel lblLogo = new JLabel();
        URL logoUrl = getClass().getResource("/resources/images/logo.png");
        if (logoUrl != null)
            lblLogo.setIcon(new ImageIcon(logoUrl));
        lblLogo.setBounds(90, 10, 100, 100);
        card.add(lblLogo);

        // Tiêu đề
        JLabel lbl1 = new JLabel("NHÀ THUỐC");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl1.setForeground(new Color(25, 118, 210));
        lbl1.setBounds(210, 30, 220, 30);
        card.add(lbl1);

        JLabel lbl2 = new JLabel("DƯỢC AN KHANG");
        lbl2.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl2.setForeground(new Color(25, 118, 210));
        lbl2.setBounds(190, 60, 240, 30);
        card.add(lbl2);

        // Tài khoản
        JLabel lblUser = new JLabel("Tài khoản:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUser.setForeground(new Color(25, 118, 210));
        lblUser.setBounds(60, 110, 120, 25);
        card.add(lblUser);

        txtTaiKhoan = new JTextField();
        txtTaiKhoan.setBackground(new Color(245, 245, 245));
        txtTaiKhoan.setBounds(60, 135, 330, 40);
        card.add(txtTaiKhoan);

        // Mật khẩu
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblPass.setForeground(new Color(25, 118, 210));
        lblPass.setBounds(60, 185, 120, 25);
        card.add(lblPass);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setBackground(new Color(245, 245, 245));
        txtMatKhau.setEchoChar('•');
        txtMatKhau.setBounds(60, 205, 290, 40);
        txtMatKhau.addActionListener(e -> dangNhap());
        card.add(txtMatKhau);

        // Nút hiện/ẩn mật khẩu
        btnCheMatKhau = new JButton();
        btnCheMatKhau.setBackground(new Color(245, 245, 245));
        URL eyeUrl = getClass().getResource("/resources/images/hidePasswordEye.png");
        if (eyeUrl != null)
            btnCheMatKhau.setIcon(new ImageIcon(eyeUrl));
        btnCheMatKhau.setBounds(350, 205, 40, 40);
        btnCheMatKhau.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCheMatKhau.addActionListener(e -> togglePassword());
        card.add(btnCheMatKhau);

        // Quên mật khẩu
        lblQuenMatKhau = new JLabel("<html><u>Quên mật khẩu?</u></html>");
        lblQuenMatKhau.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblQuenMatKhau.setBounds(300, 250, 90, 25);
        lblQuenMatKhau.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblQuenMatKhau.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { quenMatKhau(); }
        });
        card.add(lblQuenMatKhau);

        // Nút Đăng nhập
        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBackground(new Color(25, 118, 210));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnDangNhap.setBounds(60, 285, 330, 45);
        btnDangNhap.addActionListener(e -> dangNhap());
        card.add(btnDangNhap);

        pCenter.add(card, new GridBagConstraints());
    }

    // ── Đăng nhập ────────────────────────────────────────────
    private void dangNhap() {
        String tenDangNhap = txtTaiKhoan.getText().trim();
        String matKhau     = new String(txtMatKhau.getPassword()).trim();

        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng nhập đầy đủ tài khoản và mật khẩu!",
                    "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gửi yêu cầu LOGIN tới Server qua Socket
        Request req = new Request(CommandType.LOGIN,
                new Object[]{tenDangNhap, matKhau});
        Response res = SocketClient.getInstance().sendRequest(req);

        if (!res.isSuccess()) {
            JOptionPane.showMessageDialog(this,
                    res.getMessage(),
                    "Đăng nhập thất bại", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Server trả về TaiKhoanDTO
        TaiKhoanDTO taiKhoan = (TaiKhoanDTO) res.getData();
        dispose();
        SwingUtilities.invokeLater(() -> new GiaoDienChinhGUI(taiKhoan).setVisible(true));
    }

    // ── Quên mật khẩu ────────────────────────────────────────
    private void quenMatKhau() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel guide = new JLabel("Nhập tài khoản và email đã đăng ký để đặt lại mật khẩu:");
        guide.setFont(guide.getFont().deriveFont(Font.BOLD));
        guide.setForeground(new Color(0x19, 0x76, 0xD2));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.insets = new Insets(2, 2, 10, 2);
        panel.add(guide, gbc);

        gbc.gridwidth = 1; gbc.insets = new Insets(4, 4, 4, 4);

        JTextField txtUser  = new JTextField(15);
        JTextField txtEmail = new JTextField(15);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1; panel.add(txtUser, gbc);
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; panel.add(txtEmail, gbc);

        int result = JOptionPane.showConfirmDialog(this, panel, "Quên mật khẩu",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        String user  = txtUser.getText().trim();
        String email = txtEmail.getText().trim();
        if (user.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra email khớp tài khoản
        Response checkRes = SocketClient.getInstance().sendRequest(
                new Request(CommandType.KIEM_TRA_EMAIL_THUOC_TAI_KHOAN,
                        new Object[]{user, email}));

        if (!checkRes.isSuccess() || Boolean.FALSE.equals(checkRes.getData())) {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc email không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Tính năng gửi mail được xử lý bởi Server.\nVui lòng liên hệ quản trị viên.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Hiện/ẩn mật khẩu ─────────────────────────────────────
    private void togglePassword() {
        showPassword = !showPassword;
        txtMatKhau.setEchoChar(showPassword ? (char) 0 : '•');
    }
}
