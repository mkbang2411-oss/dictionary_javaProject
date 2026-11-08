// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RandomPanel extends JPanel implements ActionListener {
    private SlangDictionary dictionary;
    private Random random = new Random();
    private JLabel slangLabel;
    private JPanel meaningPanel;
    private static final Font MEANING_FONT = new Font("SansSerif", Font.PLAIN, 20);
    private JButton randomButton;

    // Hàm khởi tạo: dựng giao diện màn hình random slang word (On this day slang word)
    public RandomPanel(SlangDictionary dictionary) {
        this.dictionary = dictionary;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        slangLabel = new JLabel(" ", SwingConstants.CENTER);
        slangLabel.setFont(UIHelper.BIG_FONT);

        meaningPanel = new JPanel();
        meaningPanel.setLayout(new BoxLayout(meaningPanel, BoxLayout.Y_AXIS));
        meaningPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        UIHelper.fillCenteredLines(meaningPanel, " ", MEANING_FONT, UIHelper.TEXT_COLOR, 520);
        slangLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        randomButton = UIHelper.createButton("Random another word");
        randomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        randomButton.addActionListener(this);

        // Xếp slang, nghĩa và nút thành 1 khối dọc, canh giữa nhau
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.add(slangLabel);
        body.add(Box.createVerticalStrut(20));
        body.add(meaningPanel);
        body.add(Box.createVerticalStrut(30));
        body.add(randomButton);

        add(UIHelper.createCenteredPage(UIHelper.createTitle("On this day slang word"), body, 560, false), BorderLayout.CENTER);
    }

    // Hàm chọn ngẫu nhiên 1 slang trong từ điển và hiển thị slang cùng nghĩa của nó
    public void showRandom() {
        List<String> slangs = dictionary.getAllSlangs();
        if (slangs.isEmpty()) {
            slangLabel.setText(" ");
            UIHelper.fillCenteredLines(meaningPanel, "The dictionary is empty.", MEANING_FONT, UIHelper.TEXT_COLOR, 520);
            return;
        }
        String slang = slangs.get(random.nextInt(slangs.size()));
        List<String> meanings = dictionary.getMeanings(slang);

        // Mỗi nghĩa 1 dòng (tự xuống dòng nếu dài), canh giữa
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < meanings.size(); i++) {
            sb.append((i + 1)).append(". ").append(meanings.get(i)).append("\n");
        }
        slangLabel.setText(slang);
        UIHelper.fillCenteredLines(meaningPanel, sb.toString(), MEANING_FONT, UIHelper.TEXT_COLOR, 520);
    }

    // Hàm xử lý khi bấm nút Random another word
    public void actionPerformed(ActionEvent e) {
        showRandom();
    }
}
