// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DeletePanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private DataManager dataManager;
    private JTextField slangField;
    private JButton deleteButton;

    // Hàm khởi tạo: dựng giao diện màn hình xóa slang word
    public DeletePanel(SlangDictionary dictionary, DataManager dataManager) {
        this.dictionary = dictionary;
        this.dataManager = dataManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        slangField = UIHelper.createTextField(25);
        slangField.addActionListener(this);
        deleteButton = UIHelper.createButton("Delete");
        deleteButton.addActionListener(this);

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputRow.add(UIHelper.createLabel("Slang word to delete:"));
        inputRow.add(slangField);
        inputRow.add(deleteButton);

        add(UIHelper.createCenteredPage(UIHelper.createTitle("Delete a slang word"), inputRow, 0, false), BorderLayout.CENTER);
    }

    // Hàm xử lý khi bấm Delete: tìm slang, hỏi xác nhận, rồi xóa và lưu file
    public void actionPerformed(ActionEvent e) {
        String text = slangField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the slang word to delete.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Map.Entry<String, List<String>> entry = dictionary.findEntry(text);
        if (entry == null) {
            JOptionPane.showMessageDialog(this, "Slang word \"" + text + "\" was not found.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String message = "Are you sure you want to delete this slang word?\n\n" + entry.getKey() + " : "
                + SlangDictionary.joinMeanings(entry.getValue());
        int choice = JOptionPane.showConfirmDialog(this, message, "Confirm delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        dictionary.delete(entry.getKey());
        if (!dataManager.saveUserDictionary(dictionary)) {
            JOptionPane.showMessageDialog(this, "Cannot save the data file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Slang word deleted.", "Done", JOptionPane.INFORMATION_MESSAGE);
        slangField.setText("");
    }
}
