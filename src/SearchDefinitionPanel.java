// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SearchDefinitionPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private JTextField keywordField;
    private JButton searchButton;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel infoLabel;

    // Hàm khởi tạo: dựng giao diện màn hình tìm kiếm theo definition
    public SearchDefinitionPanel(SlangDictionary dictionary) {
        this.dictionary = dictionary;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        keywordField = UIHelper.createTextField(25);
        keywordField.addActionListener(this);
        searchButton = UIHelper.createButton("Search");
        searchButton.addActionListener(this);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputRow.add(UIHelper.createLabel("Keyword in definition:"));
        inputRow.add(keywordField);
        inputRow.add(searchButton);

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.add(UIHelper.createTitle("Search by definition"), BorderLayout.NORTH);
        top.add(inputRow, BorderLayout.CENTER);

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

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);
    }

    // Hàm xử lý khi bấm Search hoặc Enter: tìm các slang có definition chứa keyword và đổ vào bảng
    public void actionPerformed(ActionEvent e) {
        String keyword = keywordField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a keyword.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        long start = System.nanoTime();
        List<String> results = dictionary.searchByDefinition(keyword);
        double ms = (System.nanoTime() - start) / 1000000.0;

        tableModel.setRowCount(0);
        for (int i = 0; i < results.size(); i++) {
            String slang = results.get(i);
            String meaning = SlangDictionary.joinMeanings(dictionary.getMeanings(slang));
            tableModel.addRow(new Object[] { slang, meaning });
        }
        infoLabel.setText(results.size() + " result(s) found. Search time: " + String.format("%.3f", ms) + " ms");
    }
}
