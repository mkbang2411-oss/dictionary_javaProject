// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SearchSlangPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private HistoryManager history;
    private JTextField slangField;
    private JButton searchButton;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel infoLabel;

    // Hàm khởi tạo: dựng giao diện màn hình tìm kiếm theo slang word
    public SearchSlangPanel(SlangDictionary dictionary, HistoryManager history) {
        this.dictionary = dictionary;
        this.history = history;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        slangField = UIHelper.createTextField(25);
        slangField.addActionListener(this);
        searchButton = UIHelper.createButton("Search");
        searchButton.addActionListener(this);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        inputRow.add(UIHelper.createLabel("Slang word:"));
        inputRow.add(slangField);
        inputRow.add(searchButton);

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

        infoLabel = UIHelper.createLabel(" ");

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.add(inputRow, BorderLayout.NORTH);
        body.add(new JScrollPane(table), BorderLayout.CENTER);
        body.add(infoLabel, BorderLayout.SOUTH);

        add(UIHelper.createCenteredPage(UIHelper.createTitle("Search by slang word"), body, 820, true), BorderLayout.CENTER);
    }

    // Hàm xử lý khi bấm nút Search hoặc nhấn Enter: tìm slang, hiển thị nghĩa và lưu vào history
    public void actionPerformed(ActionEvent e) {
        String text = slangField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a slang word.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        long start = System.nanoTime();
        Map.Entry<String, List<String>> entry = dictionary.findEntry(text);
        double ms = (System.nanoTime() - start) / 1000000.0;

        history.add(text);

        tableModel.setRowCount(0);
        if (entry != null) {
            tableModel.addRow(new Object[] { entry.getKey(), SlangDictionary.joinMeanings(entry.getValue()) });
        }
        int found = entry == null ? 0 : 1;
        infoLabel.setText(found + " result(s) found. Search time: " + String.format("%.3f", ms) + " ms");
    }
}
