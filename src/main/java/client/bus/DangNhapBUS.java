/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.bus;

import common.dto.Request;
import common.dto.Response;
import server.entity.TaiKhoan;
import client.socket.ClientSocket;
import client.util.EmailUtil;
import client.util.PasswordUtil;
import java.util.HashMap;
import java.util.Map;

public class DangNhapBUS {
    public static TaiKhoan dangNhap(String userName, String plainPassword) {
        Map<String, Object> data = new HashMap<>();
        data.put("userName", userName);
        data.put("password", plainPassword);
        
        Request req = new Request("DANG_NHAP", data);
        Response res = ClientSocket.sendRequest(req);
        
        if (res.isSuccess() && res.getData() != null) {
            return (TaiKhoan) res.getData();
        }
        return null;
    }
    
    public static boolean quenMatKhau(String taiKhoan, String email) {
        // Sinh mật khẩu mới
        String newPassword = PasswordUtil.generateTempPassword();
        // Hash
        String hashed = PasswordUtil.hashPassword(newPassword);
        
        Map<String, Object> data = new HashMap<>();
        data.put("userName", taiKhoan);
        data.put("email", email);
        data.put("newPassword", hashed);
        
        Request req = new Request("QUEN_MAT_KHAU", data);
        Response res = ClientSocket.sendRequest(req);
        
        if (res.isSuccess()) {
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
            return true;
        }
        return false;
    }
}

