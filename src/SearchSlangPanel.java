// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SearchSlangPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private HistoryManager history;
    private JTextField slangField;
    private JButton searchButton;
    private JTextArea resultArea;
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

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputRow.add(UIHelper.createLabel("Slang word:"));
        inputRow.add(slangField);
        inputRow.add(searchButton);

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.add(UIHelper.createTitle("Search by slang word"), BorderLayout.NORTH);
        top.add(inputRow, BorderLayout.CENTER);

        resultArea = UIHelper.createReadOnlyArea();
        infoLabel = UIHelper.createLabel(" ");

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);
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

        if (entry == null) {
            resultArea.setText("Slang word \"" + text + "\" was not found.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Slang: ").append(entry.getKey()).append("\n\n");
            List<String> meanings = entry.getValue();
            for (int i = 0; i < meanings.size(); i++) {
                sb.append((i + 1)).append(". ").append(meanings.get(i)).append("\n");
            }
            resultArea.setText(sb.toString());
            resultArea.setCaretPosition(0);
        }
        infoLabel.setText("Search time: " + String.format("%.3f", ms) + " ms");
    }
}
