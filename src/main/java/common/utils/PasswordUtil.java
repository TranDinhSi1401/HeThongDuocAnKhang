/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common.utils;
import java.security.SecureRandom;

import client.socket.SocketClient;
import common.network.CommandType;
import common.network.Request;
import org.mindrot.jbcrypt.BCrypt;
/**
 *
 * @author trand
 */
public class PasswordUtil {
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "@#$%^&*-_";

    private static final String ALL = LOWER + UPPER + DIGITS + SPECIAL;
    private static final SecureRandom random = new SecureRandom();

    public static String generateTempPassword() {
        StringBuilder password = new StringBuilder();

        // đảm bảo có ít nhất 1 ký tự của mỗi loại
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // sinh thêm cho đủ độ dài 8
        for (int i = 4; i < 8; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        // xáo trộn vị trí các ký tự để tránh dự đoán
        return shuffleString(password.toString());
    }

    private static String shuffleString(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int j = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
    
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12); // 12 là cost factor (độ mạnh)
        return BCrypt.hashpw(plainPassword, salt);
    }
    
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static boolean quenMatKhau(String taiKhoan, String email) {
        // Sinh mật khẩu mới
        String newPassword = PasswordUtil.generateTempPassword();
        // Cập nhật
        Request request = new Request(CommandType.QUEN_MAT_KHAU, new Object[]{taiKhoan, email, newPassword});
        Boolean check = (Boolean) SocketClient.getInstance().sendRequest(request).getData();
        // Gửi mail
        String subject = "Yêu cầu đặt lại mật khẩu";
        String body =
                "<div style='font-family:Segoe UI, sans-serif; font-size:14px;'>"
                        + "<h2 style='color:#1976D2;'>Đặt lại mật khẩu</h2>"
                        + "<p>Xin chào,</p>"
                        + "<p>Mật khẩu mới của bạn là:</p>"
                        + "<div style='padding:10px 15px; "
                        + "background-color:#E3F2FD; color:#0D47A1; "
                        + "border-radius:5px; display:inline-block; "
                        + "font-size:18px; font-weight:bold;'>"
                        + newPassword
                        + "</div>"
                        + "<p>Vui lòng đăng nhập và đổi mật khẩu ngay.</p>"
                        + "<p style='color:#555;'>Trân trọng,<br>Hệ thống nhà thuốc Dược An Khang</p>"
                        + "</div>";

        EmailUtil.sendEmail(email, subject, body);

        return check;
    }
}
