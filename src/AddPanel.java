// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private DataManager dataManager;
    private JTextField slangField;
    private JTextField meaningField;
    private JButton addButton;

    // Hàm khởi tạo: dựng giao diện màn hình thêm slang word mới
    public AddPanel(SlangDictionary dictionary, DataManager dataManager) {
        this.dictionary = dictionary;
        this.dataManager = dataManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        slangField = UIHelper.createTextField(30);
        meaningField = UIHelper.createTextField(30);
        addButton = UIHelper.createButton("Add");
        addButton.addActionListener(this);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        form.add(UIHelper.createLabel("Slang word:"), c);
        c.gridx = 1;
        form.add(slangField, c);

        c.gridx = 0;
        c.gridy = 1;
        form.add(UIHelper.createLabel("Meaning:"), c);
        c.gridx = 1;
        form.add(meaningField, c);

        c.gridx = 1;
        c.gridy = 2;
        form.add(UIHelper.createLabel("(Separate several meanings with the | character)"), c);

        c.gridy = 3;
        form.add(addButton, c);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.add(form);

        add(UIHelper.createTitle("Add a new slang word"), BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

    // Hàm lưu từ điển của người dùng, báo lỗi nếu lưu thất bại
    private void saveData() {
        if (!dataManager.saveUserDictionary(dictionary)) {
            JOptionPane.showMessageDialog(this, "Cannot save the data file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm xử lý khi bấm nút Add: kiểm tra dữ liệu nhập, nếu slang trùng thì hỏi overwrite hay duplicate
    public void actionPerformed(ActionEvent e) {
        String slang = slangField.getText().trim();
        String meaningText = meaningField.getText().trim();
        List<String> meanings = SlangDictionary.splitMeanings(meaningText);

        if (slang.isEmpty() || meanings.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both the slang word and its meaning.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (slang.indexOf('`') >= 0 || meaningText.indexOf('`') >= 0) {
            JOptionPane.showMessageDialog(this, "The ` character is not allowed.", "Notice", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!dictionary.contains(slang)) {
            dictionary.addNew(slang, meanings);
            saveData();
            JOptionPane.showMessageDialog(this, "Slang word added successfully.", "Done", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            return;
        }

        String oldMeaning = SlangDictionary.joinMeanings(dictionary.getMeanings(slang));
        Object[] options = { "Overwrite", "Duplicate", "Cancel" };
        int choice = JOptionPane.showOptionDialog(this,
                "The slang word \"" + slang + "\" already exists with meaning:\n" + oldMeaning
                        + "\n\nOverwrite the old meaning, or duplicate (keep the old one and add the new meaning)?",
                "Slang word already exists", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);

        if (choice == 0) {
            dictionary.overwrite(slang, meanings);
            saveData();
            JOptionPane.showMessageDialog(this, "Slang word overwritten.", "Done", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else if (choice == 1) {
            int added = dictionary.addMeanings(slang, meanings);
            if (added == 0) {
                JOptionPane.showMessageDialog(this, "This meaning already exists for that slang word.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            } else {
                saveData();
                JOptionPane.showMessageDialog(this, "New meaning added to the existing slang word.", "Done", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
            }
        }
    }

    // Hàm xóa trống các ô nhập sau khi thêm xong
    private void clearFields() {
        slangField.setText("");
        meaningField.setText("");
    }
}
