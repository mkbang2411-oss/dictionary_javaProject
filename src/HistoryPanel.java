// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HistoryPanel extends JPanel implements ActionListener {
    private HistoryManager history;
    private DefaultListModel<String> listModel;
    private JList<String> list;
    private JButton clearButton;
    private JLabel infoLabel;

    // Hàm khởi tạo: dựng giao diện màn hình lịch sử tìm kiếm
    public HistoryPanel(HistoryManager history) {
        this.history = history;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        listModel = new DefaultListModel<String>();
        list = new JList<String>(listModel);
        list.setFont(UIHelper.NORMAL_FONT);

        clearButton = UIHelper.createButton("Clear history");
        clearButton.addActionListener(this);
        infoLabel = UIHelper.createLabel(" ");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(infoLabel, BorderLayout.WEST);
        bottom.add(clearButton, BorderLayout.EAST);

        add(UIHelper.createTitle("Search history (newest first)"), BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // Hàm cập nhật danh sách hiển thị theo lịch sử mới nhất (mới nhất nằm trên cùng)
    public void refresh() {
        listModel.clear();
        List<String> all = history.getAll();
        for (int i = all.size() - 1; i >= 0; i--) {
            listModel.addElement(all.get(i));
        }
        infoLabel.setText(all.size() + " search(es)");
    }

    // Hàm xử lý khi bấm nút Clear history: hỏi xác nhận rồi xóa toàn bộ lịch sử
    public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this, "Delete all search history?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            history.clear();
            refresh();
        }
    }
}
