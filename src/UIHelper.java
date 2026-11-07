// 24127147
import java.awt.*;
import javax.swing.*;

public class UIHelper {
    public static final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 15);
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    public static final Font BIG_FONT = new Font("SansSerif", Font.BOLD, 36);
    public static final Color GREEN = new Color(0, 140, 0);
    public static final Color RED = new Color(200, 0, 0);

    // Hàm tạo label tiêu đề cho mỗi màn hình chức năng
    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        return label;
    }

    // Hàm tạo label thường với font chuẩn
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(NORMAL_FONT);
        return label;
    }

    // Hàm tạo nút bấm với font chuẩn
    public static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(NORMAL_FONT);
        return button;
    }

    // Hàm tạo ô nhập text với font chuẩn
    public static JTextField createTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(NORMAL_FONT);
        return field;
    }

    // Hàm tạo vùng text nhiều dòng, chỉ đọc, tự xuống dòng
    public static JTextArea createReadOnlyArea() {
        JTextArea area = new JTextArea();
        area.setFont(NORMAL_FONT);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    // Hàm đổi text thường sang HTML có độ rộng cố định để nút bấm tự xuống dòng (có thay các ký tự đặc biệt như < > &)
    public static String toHtml(String text, int width) {
        String safe = text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<html><body style='width: " + width + "px'>" + safe + "</body></html>";
    }
}
