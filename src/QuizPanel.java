// 24127147
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class QuizPanel extends JPanel implements ActionListener {
    public static final int MODE_GUESS_MEANING = 1;
    public static final int MODE_GUESS_SLANG = 2;

    private SlangDictionary dictionary;
    private int mode;
    private Random random = new Random();

    private JTextArea questionArea;
    private JPanel highlightPanel;
    private static final Font HIGHLIGHT_FONT = new Font("SansSerif", Font.BOLD, 28);
    private JButton[] optionButtons = new JButton[4];
    private JLabel resultLabel;
    private JLabel scoreLabel;
    private JButton nextButton;
    private Color defaultButtonColor;

    private String[] options = new String[4];
    private int correctIndex;
    private int score;
    private int total;
    private boolean answered;

    // Hàm khởi tạo: dựng giao diện đố vui (mode 1: cho slang đoán nghĩa, mode 2: cho nghĩa đoán slang)
    public QuizPanel(SlangDictionary dictionary, int mode) {
        this.dictionary = dictionary;
        this.mode = mode;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        String title = "Quiz: guess the meaning of a slang word";
        if (mode == MODE_GUESS_SLANG) {
            title = "Quiz: guess the slang word from its meaning";
        }

        questionArea = UIHelper.createReadOnlyArea();
        questionArea.setFont(new Font("SansSerif", Font.BOLD, 20));
        questionArea.setRows(1);
        questionArea.setOpaque(false);

        // Slang (hoặc nghĩa) cần đoán: canh giữa, màu xanh lá để tách khỏi câu hỏi
        highlightPanel = new JPanel();
        highlightPanel.setLayout(new BoxLayout(highlightPanel, BoxLayout.Y_AXIS));
        UIHelper.fillCenteredLines(highlightPanel, " ", HIGHLIGHT_FONT, UIHelper.QUIZ_GREEN, 740);

        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = UIHelper.createButton(" ");
            optionButtons[i].addActionListener(this);
            optionsPanel.add(optionButtons[i]);
        }
        defaultButtonColor = optionButtons[0].getBackground();

        JPanel questionBlock = new JPanel(new BorderLayout(0, 12));
        questionBlock.add(questionArea, BorderLayout.NORTH);
        questionBlock.add(highlightPanel, BorderLayout.CENTER);

        resultLabel = UIHelper.createLabel(" ");
        resultLabel.setFont(UIHelper.BOLD_FONT);
        scoreLabel = UIHelper.createLabel("Score: 0/0");
        nextButton = UIHelper.createButton("Next question");
        nextButton.addActionListener(this);

        JPanel bottom = new JPanel(new BorderLayout(10, 0));
        bottom.add(resultLabel, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        right.add(scoreLabel);
        right.add(nextButton);
        bottom.add(right, BorderLayout.EAST);

        JPanel body = new JPanel(new BorderLayout(0, 15));
        body.add(questionBlock, BorderLayout.NORTH);
        body.add(optionsPanel, BorderLayout.CENTER);
        body.add(bottom, BorderLayout.SOUTH);

        add(UIHelper.createCenteredPage(UIHelper.createTitle(title), body, 800, true), BorderLayout.CENTER);
    }

    // Hàm hiển thị câu hỏi: dòng câu hỏi canh trái, phần slang / nghĩa cần đoán canh giữa bên dưới
    private void setQuestion(String prompt, String highlight) {
        questionArea.setText(prompt);
        UIHelper.fillCenteredLines(highlightPanel, highlight, HIGHLIGHT_FONT, UIHelper.QUIZ_GREEN, 740);
    }

    // Hàm bắt đầu lượt chơi mới: đặt điểm về 0 và ra câu hỏi đầu tiên
    public void startQuiz() {
        score = 0;
        total = 0;
        updateScore();
        newQuestion();
    }

    // Hàm cập nhật nhãn điểm số
    private void updateScore() {
        scoreLabel.setText("Score: " + score + "/" + total);
    }

    // Hàm bật/tắt 4 nút đáp án
    private void setOptionsEnabled(boolean enabled) {
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setEnabled(enabled);
        }
    }

    // Hàm ra câu hỏi mới: random câu hỏi và 4 đáp án rồi hiển thị lên màn hình
    private void newQuestion() {
        resultLabel.setText(" ");
        answered = false;
        nextButton.setEnabled(false);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setBackground(defaultButtonColor);
        }

        List<String> slangs = dictionary.getAllSlangs();
        boolean ok = false;
        if (slangs.size() >= 4) {
            if (mode == MODE_GUESS_MEANING) {
                ok = makeMeaningQuestion(slangs);
            } else {
                ok = makeSlangQuestion(slangs);
            }
        }

        if (!ok) {
            setQuestion("Not enough different slang words to make a question (need at least 4).", " ");
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(" ");
            }
            setOptionsEnabled(false);
            return;
        }

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(UIHelper.toHtml(options[i], 330));
        }
        setOptionsEnabled(true);
    }

    // Hàm tạo câu hỏi dạng: cho 1 slang, chọn nghĩa đúng trong 4 đáp án. Trả về false nếu không tạo được
    private boolean makeMeaningQuestion(List<String> slangs) {
        String answerSlang = slangs.get(random.nextInt(slangs.size()));
        String correctText = SlangDictionary.joinMeanings(dictionary.getMeanings(answerSlang));

        List<String> list = new ArrayList<String>();
        list.add(correctText);
        int attempts = 0;
        while (list.size() < 4 && attempts < 2000) {
            attempts++;
            String slang = slangs.get(random.nextInt(slangs.size()));
            String text = SlangDictionary.joinMeanings(dictionary.getMeanings(slang));
            if (!SlangDictionary.containsIgnoreCase(list, text)) {
                list.add(text);
            }
        }
        if (list.size() < 4) {
            return false;
        }

        Collections.shuffle(list, random);
        for (int i = 0; i < 4; i++) {
            options[i] = list.get(i);
        }
        correctIndex = list.indexOf(correctText);
        setQuestion("What is the meaning of this slang word?", answerSlang);
        return true;
    }

    // Hàm tạo câu hỏi dạng: cho 1 nghĩa, chọn slang đúng trong 4 đáp án. Trả về false nếu không tạo được
    private boolean makeSlangQuestion(List<String> slangs) {
        String answerSlang = slangs.get(random.nextInt(slangs.size()));
        List<String> meanings = dictionary.getMeanings(answerSlang);
        String shownMeaning = meanings.get(random.nextInt(meanings.size()));

        List<String> list = new ArrayList<String>();
        list.add(answerSlang);
        int attempts = 0;
        while (list.size() < 4 && attempts < 2000) {
            attempts++;
            String slang = slangs.get(random.nextInt(slangs.size()));
            if (SlangDictionary.containsIgnoreCase(list, slang)) {
                continue;
            }
            if (SlangDictionary.containsIgnoreCase(dictionary.getMeanings(slang), shownMeaning)) {
                continue;
            }
            list.add(slang);
        }
        if (list.size() < 4) {
            return false;
        }

        Collections.shuffle(list, random);
        for (int i = 0; i < 4; i++) {
            options[i] = list.get(i);
        }
        correctIndex = list.indexOf(answerSlang);
        setQuestion("Which slang word has this meaning?", shownMeaning);
        return true;
    }

    // Hàm xử lý khi người dùng chọn 1 đáp án: chấm đúng/sai, tô màu, cộng điểm
    private void checkAnswer(int chosen) {
        if (answered) {
            return;
        }
        answered = true;
        total++;

        optionButtons[correctIndex].setBackground(new Color(150, 230, 150));
        optionButtons[correctIndex].setText(UIHelper.toHtml("\u2714 " + options[correctIndex], 330));

        if (chosen == correctIndex) {
            score++;
            resultLabel.setForeground(UIHelper.GREEN);
            resultLabel.setText("Correct!");
        } else {
            optionButtons[chosen].setBackground(new Color(240, 150, 150));
            optionButtons[chosen].setText(UIHelper.toHtml("\u2716 " + options[chosen], 330));
            resultLabel.setForeground(UIHelper.RED);
            resultLabel.setText("Wrong! The correct answer is marked with a check.");
        }
        updateScore();
        nextButton.setEnabled(true);
    }

    // Hàm xử lý khi bấm nút: nút Next thì ra câu mới, nút đáp án thì chấm điểm
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            newQuestion();
            return;
        }
        for (int i = 0; i < 4; i++) {
            if (e.getSource() == optionButtons[i]) {
                checkAnswer(i);
            }
        }
    }
}
