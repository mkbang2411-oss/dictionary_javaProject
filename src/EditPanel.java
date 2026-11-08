// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class EditPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private DataManager dataManager;
    private JTextField findField;
    private JButton loadButton;
    private JTextField newSlangField;
    private JTextField meaningField;
    private JButton saveButton;
    private String editingSlang = null;

    // Hàm khởi tạo: dựng giao diện màn hình sửa slang word
    public EditPanel(SlangDictionary dictionary, DataManager dataManager) {
        this.dictionary = dictionary;
        this.dataManager = dataManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        findField = UIHelper.createTextField(30);
        findField.addActionListener(this);
        loadButton = UIHelper.createButton("Load");
        loadButton.addActionListener(this);
        newSlangField = UIHelper.createTextField(30);
        meaningField = UIHelper.createTextField(30);
        saveButton = UIHelper.createButton("Save changes");
        saveButton.addActionListener(this);
        setEditEnabled(false);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        form.add(UIHelper.createLabel("Slang word to edit:"), c);
        c.gridx = 1;
        form.add(findField, c);
        c.gridx = 2;
        form.add(loadButton, c);

        c.gridx = 0;
        c.gridy = 1;
        form.add(UIHelper.createLabel("Slang word:"), c);
        c.gridx = 1;
        form.add(newSlangField, c);

        c.gridx = 0;
        c.gridy = 2;
        form.add(UIHelper.createLabel("Meaning:"), c);
        c.gridx = 1;
        form.add(meaningField, c);

        c.gridx = 1;
        c.gridy = 3;
        form.add(UIHelper.createLabel("(Separate several meanings with the | character)"), c);

        c.gridy = 4;
        form.add(saveButton, c);

        add(UIHelper.createCenteredPage(UIHelper.createTitle("Edit a slang word"), form, 0, false), BorderLayout.CENTER);
    }

    // Hàm bật/tắt các ô chỉnh sửa (chỉ bật sau khi đã Load 1 slang)
    private void setEditEnabled(boolean enabled) {
        newSlangField.setEnabled(enabled);
        meaningField.setEnabled(enabled);
        saveButton.setEnabled(enabled);
    }

    // Hàm xử lý các nút: Load (nạp slang cần sửa lên form) và Save changes (lưu thay đổi)
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadButton || e.getSource() == findField) {
            loadSlang();
        } else if (e.getSource() == saveButton) {
            saveChanges();
        }
    }

    // Hàm nạp slang cần sửa: tìm trong từ điển rồi hiển thị tên và nghĩa lên các ô chỉnh sửa
    private void loadSlang() {
        String text = findField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the slang word to edit.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Map.Entry<String, List<String>> entry = dictionary.findEntry(text);
        if (entry == null) {
            JOptionPane.showMessageDialog(this, "Slang word \"" + text + "\" was not found.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            editingSlang = null;
            setEditEnabled(false);
            return;
        }
        editingSlang = entry.getKey();
        newSlangField.setText(entry.getKey());
        meaningField.setText(SlangDictionary.joinMeanings(entry.getValue()));
        setEditEnabled(true);
    }

    // Hàm lưu thay đổi: kiểm tra dữ liệu, không cho đổi tên trùng với slang khác, rồi cập nhật và lưu file
    private void saveChanges() {
        String newSlang = newSlangField.getText().trim();
        String meaningText = meaningField.getText().trim();
        List<String> meanings = SlangDictionary.splitMeanings(meaningText);

        if (editingSlang == null || !dictionary.contains(editingSlang)) {
            JOptionPane.showMessageDialog(this, "The slang word no longer exists. Please load it again.", "Notice", JOptionPane.WARNING_MESSAGE);
            editingSlang = null;
            setEditEnabled(false);
            return;
        }
        if (newSlang.isEmpty() || meanings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The slang word and its meaning must not be empty.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (newSlang.indexOf('`') >= 0 || meaningText.indexOf('`') >= 0) {
            JOptionPane.showMessageDialog(this, "The ` character is not allowed.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!newSlang.equalsIgnoreCase(editingSlang) && dictionary.contains(newSlang)) {
            JOptionPane.showMessageDialog(this, "Another slang word named \"" + newSlang + "\" already exists.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dictionary.edit(editingSlang, newSlang, meanings);
        if (!dataManager.saveUserDictionary(dictionary)) {
            JOptionPane.showMessageDialog(this, "Cannot save the data file!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Slang word updated successfully.", "Done", JOptionPane.INFORMATION_MESSAGE);
        editingSlang = null;
        findField.setText("");
        newSlangField.setText("");
        meaningField.setText("");
        setEditEnabled(false);
    }
}
