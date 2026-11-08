// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class HistoryPanel extends JPanel implements ActionListener {
    private HistoryManager history;
    private SlangDictionary dictionary;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton clearButton;
    private JLabel infoLabel;

    // Hàm khởi tạo: dựng giao diện màn hình lịch sử tìm kiếm (dạng bảng: slang đã tìm và nghĩa của nó)
    public HistoryPanel(HistoryManager history, SlangDictionary dictionary) {
        this.history = history;
        this.dictionary = dictionary;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        tableModel = new DefaultTableModel(new String[] { "Slang", "Meaning" }, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFont(UIHelper.NORMAL_FONT);
        table.setRowHeight(24);
        table.getTableHeader().setFont(UIHelper.BOLD_FONT);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(600);

        clearButton = UIHelper.createButton("Clear history");
        clearButton.addActionListener(this);
        infoLabel = UIHelper.createLabel(" ");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(infoLabel, BorderLayout.WEST);
        bottom.add(clearButton, BorderLayout.EAST);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(bottom, BorderLayout.SOUTH);

        add(UIHelper.createCenteredPage(UIHelper.createTitle("Search history (newest first)"), body, 820, true), BorderLayout.CENTER);
    }

    // Hàm cập nhật bảng theo lịch sử mới nhất (mới nhất nằm trên cùng), tra nghĩa hiện tại của từng slang trong từ điển
    public void refresh() {
        tableModel.setRowCount(0);
        List<String> all = history.getAll();
        for (int i = all.size() - 1; i >= 0; i--) {
            String text = all.get(i);
            Map.Entry<String, List<String>> entry = dictionary.findEntry(text);
            String meaning = "(not found)";
            if (entry != null) {
                meaning = SlangDictionary.joinMeanings(entry.getValue());
            }
            tableModel.addRow(new Object[] { text, meaning });
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
