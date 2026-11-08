// 24127147
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ResetPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private DataManager dataManager;
    private JButton resetButton;

    // Hàm khởi tạo: dựng giao diện màn hình reset danh sách slang word gốc
    public ResetPanel(SlangDictionary dictionary, DataManager dataManager) {
        this.dictionary = dictionary;
        this.dataManager = dataManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel note = UIHelper.createLabel(UIHelper.toHtml(
                "Reset will remove all words you added, edited or deleted, and restore the original slang word list.", 460));
        note.setVerticalAlignment(SwingConstants.TOP);

        resetButton = UIHelper.createButton("Reset to original list");
        resetButton.addActionListener(this);

        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.add(note, BorderLayout.NORTH);
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonRow.add(resetButton);
        center.add(buttonRow, BorderLayout.CENTER);

        add(UIHelper.createCenteredPage(UIHelper.createTitle("Reset slang word list"), center, 0, false), BorderLayout.CENTER);
    }

    // Hàm xử lý khi bấm nút Reset: hỏi xác nhận, xóa file của người dùng và nạp lại từ điển gốc
    public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this,
                "All your changes will be lost. Reset to the original slang word list?", "Confirm reset",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            SlangDictionary original = dataManager.resetDictionary();
            dictionary.replaceData(original);
            JOptionPane.showMessageDialog(this, "Reset done. The dictionary now has " + dictionary.size() + " slang words.", "Done",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Cannot reset the dictionary: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
