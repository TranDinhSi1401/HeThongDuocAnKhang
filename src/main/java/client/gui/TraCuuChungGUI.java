package client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình Tra cứu chung - Đã bị vô hiệu hóa theo yêu cầu.
 */
public class TraCuuChungGUI extends JPanel {

    public TraCuuChungGUI() {
        setLayout(new BorderLayout());
        JLabel lbl = new JLabel("Tính năng Tra cứu chung đã bị vô hiệu hóa.", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lbl.setForeground(Color.RED);
        add(lbl, BorderLayout.CENTER);
        
        JLabel lblSub = new JLabel("Vui lòng sử dụng các chức năng quản lý riêng biệt.", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(lblSub, BorderLayout.SOUTH);
    }

    // Các helper method để tránh lỗi ở các file khác gọi tới (nếu có)
    public void mapKeyToClickButton(String key, AbstractButton button) {}
    public void mapKeyToFocus(String key, JComponent component) {}
}
