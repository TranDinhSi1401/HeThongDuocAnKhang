package client.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;

import client.menu.Menu;
import client.menu.MenuAction;

/**
 *
 * @author Raven
 */
public class MainForm extends JLayeredPane {

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());
        initMenuArrowIcon();
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");
        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);
        initMenuArrowIcon();
    }

    private void initMenuArrowIcon() {
        if (menuButton == null) {
            menuButton = new JButton();
        }
        String icon = (getComponentOrientation().isLeftToRight()) ? "menu_left.svg" : "menu_right.svg";
        menuButton.setIcon(new FlatSVGIcon("client/icon/svg/" + icon, 0.8f));
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            if (index == 0) {
                if (GiaoDienChinhGUI.getTk().isQuanLy()) {
                    //GiaoDienChinhGUI.showFormByKey("dashboardQuanLi", DashBoardQuanLi::new);
                } else {
                    //GiaoDienChinhGUI.showFormByKey("dashboardNhanVien", DashBoardNhanVien::new);
                }
            } else if (index == 1) {
                GiaoDienChinhGUI.showFormByKey("banHang", BanHangGUI::new);
            } else if (index == 2) {
                //GiaoDienChinhGUI.showFormByKey("traHang", TraHangGUI::new);
            } else if (index == 3) {
                //GiaoDienChinhGUI.showFormByKey("traCuuChung", TraCuuChungGUI::new);
            } else if (index == 4) {
                if(GiaoDienChinhGUI.getTk().isQuanLy()) {
                    switch (subIndex) {
                        case 1 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiKhachHang", QuanLiKhachHangGUI::new);
                        case 2 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiSanPham", QuanLiSanPhamGUI::new);
                        case 3 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiNhanVien", QuanLiNhanVienGUI::new);
                        case 4 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiHoaDon", QuanLiHoaDonGUI::new);
                        case 5 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiNhanVien", QuanLiNhanVienGUI::new);
                        case 6 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiNhaCungCap", QuanLiNhaCungCapGUI::new);
                        case 7 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiLichSuCaLam", QuanLiLichSuCaLamGUI::new);
                        case 8 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiPhieuTraHang", QuanLiPhieuTraHangGUI::new);
                        case 9 -> {}
                            //GiaoDienChinhGUI.showFormByKey("quanLiPhieuNhapHang", QuanLiPhieuNhapHangGUI::new);
                        default -> {
                            action.cancel();
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này");
                    action.cancel();
                }
            } else if (index == 5) {
                if(GiaoDienChinhGUI.getTk().isQuanLy() || GiaoDienChinhGUI.getTk().isQuanLyLo()) {
                    GiaoDienChinhGUI.showFormByKey("loSanPham", () -> {
                        try {
                            return new LoSanPhamGUI();
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(null,
                                "Lỗi khi tải Lô sản phẩm:\n" + ex.getMessage(),
                                "Database Error",
                                JOptionPane.ERROR_MESSAGE);
                            return new JPanel(); // hoặc trả về panel rỗng để không null
                        }
                    });
                }else {
                    JOptionPane.showMessageDialog(this, "Bạn không có quyền truy cập chức năng này");
                    action.cancel();
                }
            } else if (index == 6) {
                //GiaoDienChinhGUI.showFormByKey("thongKeHoaDon", ThongKeHoaDonGUI::new);
            } else if (index == 7) {
                //GiaoDienChinhGUI.showFormByKey("baoCao", BaoCaoGUI::new);
            } else if (index == 8) {
                switch (subIndex) {
                    case 1 -> {
                        // Giới thiệu - About
                        showAboutDialog();
                    }
                    case 2 -> {
                        // Hướng dẫn sử dụng - Help
                        showHelpDialog();
                    }
                    case 3 -> {
                        GiaoDienChinhGUI.taoPanelDoiMatKhau();
                    }
                    default -> {
                        action.cancel();
                    }
                }
            } else if (index == 9) {
                GiaoDienChinhGUI.logout();
            } else {
                action.cancel();
            }
        });
    }

    private void setMenuFull(boolean full) {
        String icon;
        if (getComponentOrientation().isLeftToRight()) {
            icon = (full) ? "menu_left.svg" : "menu_right.svg";
        } else {
            icon = (full) ? "menu_right.svg" : "menu_left.svg";
        }
        menuButton.setIcon(new FlatSVGIcon("client/icon/svg/" + icon, 0.8f));
        menu.setMenuFull(full);
        revalidate();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) {
        menu.setSelectedMenu(index, subIndex);
    }

    /**
     * Hiển thị dialog Giới thiệu về ứng dụng
     */
    private void showAboutDialog() {
        String aboutText = "Hệ Thống Dược An Khang\n" +
                "=====================================\n\n" +
                "Phiên bản: 1.0.0\n\n" +
                "Mô tả:\n" +
                "Hệ thống quản lý dữ liệu dược phẩm, bán hàng và quản lý lô sản phẩm.\n" +
                "Ứng dụng này hỗ trợ các chức năng quản lý kho, bán hàng, theo dõi hạn dùng,\n" +
                "quản lý khách hàng, nhà cung cấp và các báo cáo thống kê.\n\n" +
                "Đội phát triển:\n" +
                "- Phát triển hệ thống\n" +
                "- Thiết kế giao diện\n" +
                "- Quản lý dữ liệu\n\n" +
                "Năm phát hành: 2026-2027\n\n" +
                "© Bản quyền được bảo vệ.\n" +
                "Mọi quyền được bảo lưu.";

        JTextArea textArea = new JTextArea(aboutText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(new java.awt.Font("Segoe UI", 0, 12));
        textArea.setBackground(new java.awt.Color(240, 240, 240));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Giới thiệu - Hệ Thống Dược An Khang",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Hiển thị dialog Hướng dẫn sử dụng
     */
    private void showHelpDialog() {
        String helpText = "HƯỚNG DẪN SỬ DỤNG - Hệ Thống Dược An Khang\n" +
                "========================================\n\n" +
                "1. BÁN HÀNG:\n" +
                "   - Chọn menu \"Bán hàng\"\n" +
                "   - Tìm kiếm sản phẩm theo mã hoặc tên\n" +
                "   - Nhập số lượng và thêm vào giỏ hàng\n" +
                "   - Nhấn \"Thanh toán\" để hoàn thành\n\n" +
                "2. QUẢN LÝ LÔ SẢN PHẨM:\n" +
                "   - Tab \"Theo dõi & Cảnh báo\": Xem thống kê lô hàng\n" +
                "   - Tab \"Quản lý lô\": Thêm, sửa, xóa lô sản phẩm\n" +
                "   - Nhập lô từ file Excel [F6]\n" +
                "   - Tìm kiếm lô theo mã [F3]\n" +
                "   - Hủy lô sản phẩm [F5]\n\n" +
                "3. TÌM KIẾM:\n" +
                "   - Sử dụng các bộ lọc để tìm kiếm theo:\n" +
                "     * Mã lô sản phẩm\n" +
                "     * Mã sản phẩm\n" +
                "     * Tên sản phẩm\n" +
                "     * Nhà cung cấp\n" +
                "   - Chọn trạng thái lô (Còn hạn, Sắp hết hạn, Hết hạn)\n\n" +
                "4. PHÍM TẮT:\n" +
                "   - [F3]: Tìm kiếm\n" +
                "   - [F4]: Xác nhận/Tìm\n" +
                "   - [F5]: Hủy lô\n" +
                "   - [F6]: Nhập từ Excel\n" +
                "   - [F7]: Chọn tất cả\n" +
                "   - [F8]: Tìm lô hết hạn\n" +
                "   - [F9]: Xóa\n" +
                "   - [F10]: Xóa trắng\n\n" +
                "5. LIÊN HỆ HỖTRỢ:\n" +
                "   - Liên hệ bộ phận IT để được hỗ trợ.\n";

        JTextArea textArea = new JTextArea(helpText);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setFont(new java.awt.Font("Segoe UI", 0, 11));
        textArea.setBackground(new java.awt.Color(245, 245, 245));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Hướng dẫn sử dụng",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {

        @Override
        public void addLayoutComponent(String name, Component comp) {
        }

        @Override
        public void removeLayoutComponent(Component comp) {
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(5, 5);
            }
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                return new Dimension(0, 0);
            }
        }

        @Override
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                boolean ltr = parent.getComponentOrientation().isLeftToRight();
                Insets insets = UIScale.scale(parent.getInsets());
                int x = insets.left;
                int y = insets.top;
                int width = parent.getWidth() - (insets.left + insets.right);
                int height = parent.getHeight() - (insets.top + insets.bottom);
                int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
                int menuX = ltr ? x : x + width - menuWidth;
                menu.setBounds(menuX, y, menuWidth, height);
                int menuButtonWidth = menuButton.getPreferredSize().width;
                int menuButtonHeight = menuButton.getPreferredSize().height;
                int menubX;
                if (ltr) {
                    menubX = (int) (x + menuWidth - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f)));
                } else {
                    menubX = (int) (menuX - (menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f)));
                }
                menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);
                int gap = UIScale.scale(5);
                int bodyWidth = width - menuWidth - gap;
                int bodyHeight = height;
                int bodyx = ltr ? (x + menuWidth + gap) : x;
                int bodyy = y;
                panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
            }
        }
    }
}
