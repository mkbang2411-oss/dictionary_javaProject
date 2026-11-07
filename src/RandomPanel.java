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
    private JTextArea meaningArea;
    private JButton randomButton;

    // Hàm khởi tạo: dựng giao diện màn hình random slang word (On this day slang word)
    public RandomPanel(SlangDictionary dictionary) {
        this.dictionary = dictionary;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        slangLabel = new JLabel(" ", SwingConstants.CENTER);
        slangLabel.setFont(UIHelper.BIG_FONT);

        meaningArea = UIHelper.createReadOnlyArea();
        meaningArea.setFont(new Font("SansSerif", Font.PLAIN, 20));
        meaningArea.setOpaque(false);

        randomButton = UIHelper.createButton("Random another word");
        randomButton.addActionListener(this);

        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setBorder(new EmptyBorder(30, 30, 30, 30));
        center.add(slangLabel, BorderLayout.NORTH);
        center.add(meaningArea, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottom.add(randomButton);

        add(UIHelper.createTitle("On this day slang word"), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    // Hàm chọn ngẫu nhiên 1 slang trong từ điển và hiển thị slang cùng nghĩa của nó
    public void showRandom() {
        List<String> slangs = dictionary.getAllSlangs();
        if (slangs.isEmpty()) {
            slangLabel.setText(" ");
            meaningArea.setText("The dictionary is empty.");
            return;
        }
        String slang = slangs.get(random.nextInt(slangs.size()));
        List<String> meanings = dictionary.getMeanings(slang);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < meanings.size(); i++) {
            sb.append((i + 1)).append(". ").append(meanings.get(i)).append("\n");
        }
        slangLabel.setText(slang);
        meaningArea.setText(sb.toString());
    }

    // Hàm xử lý khi bấm nút Random another word
    public void actionPerformed(ActionEvent e) {
        showRandom();
    }
}
