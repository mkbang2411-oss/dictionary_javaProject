// 24127147
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    private HistoryPanel historyPanel;
    private RandomPanel randomPanel;

    // Hàm khởi tạo: tạo cửa sổ chính, thanh menu và các màn hình chức năng
    public MainFrame(SlangDictionary dictionary, DataManager dataManager, HistoryManager history) {
        setTitle("Slang Dictionary");
        setSize(950, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        historyPanel = new HistoryPanel(history);
        randomPanel = new RandomPanel(dictionary);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(createHomePanel(), "home");
        cardPanel.add(new SearchSlangPanel(dictionary, history), "search_slang");
        cardPanel.add(new SearchDefinitionPanel(dictionary), "search_definition");
        cardPanel.add(historyPanel, "history");
        cardPanel.add(new EditPanel(dictionary, dataManager), "edit");
        cardPanel.add(new DeletePanel(dictionary, dataManager), "delete");
        cardPanel.add(new ResetPanel(dictionary, dataManager), "reset");
        cardPanel.add(randomPanel, "random");

        setJMenuBar(createMenuBar());
        add(cardPanel);
        cardLayout.show(cardPanel, "home");
    }

    // Hàm tạo màn hình chào mừng hiển thị khi mới mở chương trình
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Slang Dictionary", SwingConstants.CENTER);
        title.setFont(UIHelper.BIG_FONT);

        JLabel hint = new JLabel("Choose a function from the menu bar above.", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.PLAIN, 18));

        panel.add(title, BorderLayout.CENTER);
        panel.add(hint, BorderLayout.SOUTH);
        return panel;
    }

    // Hàm tạo 1 mục menu với tên hiển thị và mã lệnh, gắn sự kiện vào cửa sổ chính
    private JMenuItem createItem(String text, String command) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(UIHelper.NORMAL_FONT);
        item.setActionCommand(command);
        item.addActionListener(this);
        return item;
    }

    // Hàm tạo thanh menu gồm các nhóm chức năng: File, Search, Manage, Fun
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(UIHelper.BOLD_FONT);
        fileMenu.add(createItem("Exit", "exit"));

        JMenu searchMenu = new JMenu("Search");
        searchMenu.setFont(UIHelper.BOLD_FONT);
        searchMenu.add(createItem("Search by slang word", "search_slang"));
        searchMenu.add(createItem("Search by definition", "search_definition"));
        searchMenu.add(createItem("Search history", "history"));

        JMenu manageMenu = new JMenu("Manage");
        manageMenu.setFont(UIHelper.BOLD_FONT);
        manageMenu.add(createItem("Edit a slang word", "edit"));
        manageMenu.add(createItem("Delete a slang word", "delete"));
        manageMenu.add(createItem("Reset to original list", "reset"));

        JMenu funMenu = new JMenu("Fun");
        funMenu.setFont(UIHelper.BOLD_FONT);
        funMenu.add(createItem("Random slang word (On this day)", "random"));

        menuBar.add(fileMenu);
        menuBar.add(searchMenu);
        menuBar.add(manageMenu);
        menuBar.add(funMenu);
        return menuBar;
    }

    // Hàm xử lý khi chọn 1 mục menu: chuyển sang màn hình tương ứng (và làm mới màn hình đó nếu cần)
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("exit")) {
            System.exit(0);
        } else if (command.equals("history")) {
            historyPanel.refresh();
        } else if (command.equals("random")) {
            randomPanel.showRandom();
        }

        if (!command.equals("exit")) {
            cardLayout.show(cardPanel, command);
        }
    }
}
