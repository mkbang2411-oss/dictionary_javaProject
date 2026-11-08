// 24127147
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class UIHelper {
    public static final Font NORMAL_FONT = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 15);
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 24);
    public static final Font BIG_FONT = new Font("SansSerif", Font.BOLD, 36);

    // Bảng màu pastel dùng chung cho toàn chương trình
    public static final Color BG_BLUE = new Color(0xD6ECFA); // nền các màn hình: xanh nhạt
    public static final Color BAR_PINK = new Color(0xF9D3E4); // thanh menu: hồng pastel
    public static final Color BAR_PINK_DARK = new Color(0xEFA9C8); // viền, màu khi rê chuột vào menu
    public static final Color POPUP_PINK = new Color(0xFEEFF5); // nền của menu thả xuống
    public static final Color BORDER_BLUE = new Color(0x8EC3E6); // viền ô nhập, bảng
    public static final Color TITLE_PINK = new Color(0xF28DB2); // chữ tiêu đề: hồng pastel
    public static final Color HINT_GREEN = new Color(0x5FCB8F); // chữ hướng dẫn: xanh lá pastel
    public static final Color QUIZ_GREEN = new Color(0x2FB36B); // slang / nghĩa trong quiz: xanh lá dễ nhìn
    public static final Color GREEN = new Color(0x1E8449); // xanh lá, dùng báo đúng trong quiz
    public static final Color RED = new Color(0xC0392B);
    public static final Color TEXT_COLOR = new Color(0x3F4756); // chữ thường: xám xanh đậm
    public static final Color BUTTON_PINK = new Color(0xF9D3E4); // nền nút bấm
    public static final Color FIELD_BG = new Color(0xFDFEFF); // nền ô nhập, bảng, danh sách

    // Hàm tạo label tiêu đề cho mỗi màn hình chức năng
    public static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(TITLE_FONT);
        label.setForeground(TITLE_PINK);
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

    // Hàm hiển thị đoạn text nhiều dòng, canh giữa từng dòng: tự ngắt dòng theo độ rộng tối đa (ký tự \n là xuống dòng thủ công)
    public static void fillCenteredLines(JPanel panel, String text, Font font, Color color, int maxWidth) {
        panel.removeAll();
        FontMetrics fm = panel.getFontMetrics(font);
        boolean any = false;
        for (String paragraph : text.split("\n")) {
            StringBuilder line = new StringBuilder();
            String[] words = paragraph.trim().split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String candidate = line.length() == 0 ? words[i] : line + " " + words[i];
                if (line.length() > 0 && fm.stringWidth(candidate) > maxWidth) {
                    panel.add(createCenteredLine(line.toString(), font, color));
                    any = true;
                    line = new StringBuilder(words[i]);
                } else {
                    line = new StringBuilder(candidate);
                }
            }
            if (line.length() > 0) {
                panel.add(createCenteredLine(line.toString(), font, color));
                any = true;
            }
        }
        if (!any) {
            panel.add(createCenteredLine(" ", font, color));
        }
        panel.revalidate();
        panel.repaint();
    }

    // Hàm tạo 1 dòng chữ canh giữa (dùng cho fillCenteredLines)
    private static JLabel createCenteredLine(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // Hàm gom tiêu đề + nội dung thành 1 cụm đặt chính giữa màn hình, tiêu đề canh giữa theo chiều rộng của cụm.
    // width > 0: cố định chiều rộng cụm (width <= 0: lấy theo nội dung). stretch = true: cụm giãn hết chiều cao (dùng cho màn hình có danh sách / bảng)
    public static JPanel createCenteredPage(JLabel title, JComponent body, int width, boolean stretch) {
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel block = new JPanel(new BorderLayout(0, 20));
        block.add(title, BorderLayout.NORTH);
        block.add(body, BorderLayout.CENTER);
        if (width > 0) {
            Dimension size = block.getPreferredSize();
            block.setPreferredSize(new Dimension(width, size.height));
            // Đặt luôn chiều rộng tối thiểu: khi cửa sổ thấp hơn chiều cao mong muốn, GridBagLayout sẽ co cụm về kích thước tối thiểu,
            // nếu không có dòng này thì cụm bị co hẹp lại chỉ còn bằng chiều rộng của tiêu đề
            block.setMinimumSize(new Dimension(width, block.getMinimumSize().height));
        }

        JPanel page = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if (stretch) {
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty = 1;
        }
        page.add(block, c);
        return page;
    }

    // Hàm tạo màu hòa trộn giữa 2 màu (ratio là tỉ lệ lấy từ màu 'to', từ 0 đến 1)
    private static Color blend(Color from, Color to, double ratio) {
        int r = (int) (from.getRed() * (1 - ratio) + to.getRed() * ratio);
        int g = (int) (from.getGreen() * (1 - ratio) + to.getGreen() * ratio);
        int b = (int) (from.getBlue() * (1 - ratio) + to.getBlue() * ratio);
        return new Color(r, g, b);
    }

    // Hàm bật giao diện pastel cho toàn chương trình (gọi 1 lần trong main, trước khi tạo cửa sổ)
    public static void applyTheme() {
        try {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            MetalLookAndFeel.setCurrentTheme(new PastelTheme());
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Nút bấm: dùng kiểu vẽ bo tròn pastel cho tất cả JButton (kể cả nút trong hộp thoại)
        UIManager.put("ButtonUI", PastelButtonUI.class.getName());
        UIManager.put(PastelButtonUI.class.getName(), PastelButtonUI.class);
        UIManager.put("Button.background", new ColorUIResource(BUTTON_PINK));
        UIManager.put("Button.foreground", new ColorUIResource(new Color(0x8A3B62)));
        UIManager.put("Button.border", new BorderUIResource(new EmptyBorder(6, 16, 6, 16)));
        UIManager.put("Button.rollover", Boolean.TRUE);

        // Thanh menu và menu thả xuống
        Border popupBorder = new BorderUIResource(new LineBorder(BAR_PINK_DARK, 1));
        UIManager.put("MenuBar.background", new ColorUIResource(BAR_PINK));
        UIManager.put("Menu.background", new ColorUIResource(BAR_PINK));
        UIManager.put("Menu.foreground", new ColorUIResource(TEXT_COLOR));
        UIManager.put("Menu.selectionBackground", new ColorUIResource(BAR_PINK_DARK));
        UIManager.put("Menu.selectionForeground", new ColorUIResource(TEXT_COLOR));
        UIManager.put("PopupMenu.background", new ColorUIResource(POPUP_PINK));
        UIManager.put("PopupMenu.border", popupBorder);
        UIManager.put("MenuItem.background", new ColorUIResource(POPUP_PINK));
        UIManager.put("MenuItem.foreground", new ColorUIResource(TEXT_COLOR));
        UIManager.put("MenuItem.selectionBackground", new ColorUIResource(BAR_PINK_DARK));
        UIManager.put("MenuItem.selectionForeground", new ColorUIResource(TEXT_COLOR));
        // Bỏ khoảng trống dành cho dấu tick bên trái mỗi mục menu để chữ không bị thụt vào
        UIManager.put("MenuItem.checkIcon", new IconUIResource(new EmptyIcon()));
        UIManager.put("MenuItem.afterCheckIconGap", Integer.valueOf(0));
        UIManager.put("MenuItem.border", new BorderUIResource(new EmptyBorder(7, 13, 7, 18)));

        // Ô nhập, vùng text, bảng, danh sách
        Border fieldBorder = new BorderUIResource(new CompoundBorder(new LineBorder(BORDER_BLUE, 1), new EmptyBorder(4, 6, 4, 6)));
        UIManager.put("TextField.border", fieldBorder);
        UIManager.put("TextField.background", new ColorUIResource(FIELD_BG));
        UIManager.put("TextArea.background", new ColorUIResource(FIELD_BG));
        UIManager.put("List.background", new ColorUIResource(FIELD_BG));
        UIManager.put("Table.background", new ColorUIResource(FIELD_BG));
        UIManager.put("Table.gridColor", new ColorUIResource(new Color(0xC3E0F3)));
        UIManager.put("TableHeader.background", new ColorUIResource(BAR_PINK));
        UIManager.put("TableHeader.foreground", new ColorUIResource(TEXT_COLOR));
        UIManager.put("ScrollPane.border", new BorderUIResource(new LineBorder(BORDER_BLUE, 1)));
        UIManager.put("OptionPane.background", new ColorUIResource(BG_BLUE));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXT_COLOR));
    }

    // Icon rỗng (rộng 0) dùng để xóa khoảng trống dấu tick trong mục menu
    private static class EmptyIcon implements Icon {
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }

        public int getIconWidth() {
            return 0;
        }

        public int getIconHeight() {
            return 0;
        }
    }

    // Bảng màu pastel cho giao diện Metal (nền vàng nhạt, điểm nhấn xanh biển / xanh lá)
    private static class PastelTheme extends DefaultMetalTheme {
        private final ColorUIResource primary1 = new ColorUIResource(0x8EC3E6);
        private final ColorUIResource primary2 = new ColorUIResource(0xBFE0F5);
        private final ColorUIResource primary3 = new ColorUIResource(0xBFE3F5);
        private final ColorUIResource secondary1 = new ColorUIResource(0x9DB8C8);
        private final ColorUIResource secondary2 = new ColorUIResource(0xCFDCE3);
        private final ColorUIResource secondary3 = new ColorUIResource(BG_BLUE);
        private final ColorUIResource black = new ColorUIResource(TEXT_COLOR);
        private final FontUIResource font = new FontUIResource(NORMAL_FONT);

        public String getName() {
            return "Pastel";
        }

        protected ColorUIResource getPrimary1() {
            return primary1;
        }

        protected ColorUIResource getPrimary2() {
            return primary2;
        }

        protected ColorUIResource getPrimary3() {
            return primary3;
        }

        protected ColorUIResource getSecondary1() {
            return secondary1;
        }

        protected ColorUIResource getSecondary2() {
            return secondary2;
        }

        protected ColorUIResource getSecondary3() {
            return secondary3;
        }

        protected ColorUIResource getBlack() {
            return black;
        }

        public FontUIResource getControlTextFont() {
            return font;
        }

        public FontUIResource getSystemTextFont() {
            return font;
        }

        public FontUIResource getUserTextFont() {
            return font;
        }

        public FontUIResource getMenuTextFont() {
            return font;
        }

        public FontUIResource getWindowTitleFont() {
            return font;
        }

        public FontUIResource getSubTextFont() {
            return font;
        }
    }

    // Kiểu vẽ nút bấm pastel: nền bo tròn, đậm hơn một chút khi rê chuột / nhấn, nhạt đi khi bị vô hiệu hóa
    public static class PastelButtonUI extends BasicButtonUI {
        public static ComponentUI createUI(JComponent c) {
            return new PastelButtonUI();
        }

        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            b.setOpaque(false);
            b.setRolloverEnabled(true);
            b.setFocusPainted(false);
            b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void update(Graphics g, JComponent c) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel model = b.getModel();
            Color bg = b.getBackground();
            if (!b.isEnabled()) {
                bg = blend(bg, BG_BLUE, 0.6);
            } else if (model.isPressed() && model.isArmed()) {
                bg = blend(bg, Color.BLACK, 0.16);
            } else if (model.isRollover()) {
                bg = blend(bg, Color.BLACK, 0.07);
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 14, 14);
            g2.setColor(blend(bg, Color.BLACK, 0.18));
            g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 14, 14);
            g2.dispose();

            paint(g, c);
        }

        protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
            FontMetrics fm = g.getFontMetrics();
            Color color = b.getForeground();
            if (!b.isEnabled()) {
                color = new Color(0x9AA59E);
            }
            g.setColor(color);
            g.drawString(text, textRect.x + getTextShiftOffset(), textRect.y + fm.getAscent() + getTextShiftOffset());
        }

        protected void paintFocus(Graphics g, AbstractButton b, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
            // Không vẽ khung nét đứt khi nút đang được focus
        }
    }
}
