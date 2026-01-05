package MiniPatternGame;

import ModelScore.ScoreView;
import ModelScore.EntryScore;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatternGame extends JDialog {

    private List<Pattern> questions;
    private int currentQuestionIndex;
    private int score;
    private int difficulty;
    private int totalQuestions;

    private JPanel gamePanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;

    private JLabel questionNumberLabel;
    private JLabel scoreLabel;
    private JLabel instructionLabel;
    private JLabel sequenceLabel;

    public static List<EntryScore> scores = new ArrayList<>();

    private JButton[] answerButtons;

    public PatternGame(JFrame parent, int difficulty, int questions) {
        super(parent, "Wzorce - Gra", true);

        this.difficulty = difficulty;
        this.totalQuestions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.answerButtons = new JButton[4];

        generateQuestions();
        initializeUI();
        displayQuestion(0);

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void initializeUI() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(230, 230, 230));

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(230, 230, 230));
        topPanel.setPreferredSize(new Dimension(800, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        questionNumberLabel = new JLabel("Pytanie: 1/" + totalQuestions);
        questionNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        scoreLabel = new JLabel("Wynik: 0/" + totalQuestions);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(questionNumberLabel, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(230, 230, 230));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        instructionLabel = new JLabel("Jaki jest następny element w ciągu?");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        sequenceLabel = new JLabel("");
        sequenceLabel.setFont(new Font("Courier New", Font.BOLD, 48));
        sequenceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sequenceLabel.setForeground(new Color(50, 50, 50));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        centerPanel.add(sequenceLabel);
        centerPanel.add(Box.createVerticalGlue());

        bottomPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        bottomPanel.setBackground(new Color(230, 230, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 40, 100));

        for (int i = 0; i < 4; i++) {
            answerButtons[i] = new JButton("");
            answerButtons[i].setFont(new Font("Arial", Font.BOLD, 32));
            answerButtons[i].setFocusable(false);
            answerButtons[i].setPreferredSize(new Dimension(150, 80));

            answerButtons[i].setOpaque(true);
            answerButtons[i].setBorderPainted(false);
            answerButtons[i].setBackground(Color.WHITE);
            answerButtons[i].setForeground(Color.BLACK);
            answerButtons[i].setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));

            final int buttonIndex = i;
            answerButtons[i].addActionListener(e -> checkAnswer(buttonIndex));

            bottomPanel.add(answerButtons[i]);
        }

        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        add(gamePanel);
    }

    private void generateQuestions() {
        PatternGenerator generator = new PatternGenerator();
        questions = new ArrayList<>();

        for (int i = 0; i < totalQuestions; i++) {
            Pattern pattern = generator.generatePattern(difficulty);
            questions.add(pattern);
        }
    }

    private void displayQuestion(int index) {
        Pattern currentPattern = questions.get(index);

        questionNumberLabel.setText("Pytanie: " + (index + 1) + "/" + totalQuestions);
        scoreLabel.setText("Wynik: " + score + "/" + totalQuestions);

        int[] fullSequence = currentPattern.getFullSequence();
        int hiddenIndex = currentPattern.getHiddenIndex();

        StringBuilder sequenceText = new StringBuilder();

        for (int i = 0; i < fullSequence.length; i++) {
            if (i == hiddenIndex) {
                sequenceText.append("?");
            } else {
                sequenceText.append(fullSequence[i]);
            }

            if (i < fullSequence.length - 1) {
                sequenceText.append("    ");
            }
        }

        sequenceLabel.setText(sequenceText.toString());

        int[] answers = currentPattern.getAnswerOptions();

        for (int i = 0; i < 4; i++) {
            answerButtons[i].setText(String.valueOf(answers[i]));
        }

        for (JButton btn : answerButtons) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Color.BLACK);
            btn.setEnabled(true);
        }
    }

    private void checkAnswer(int buttonIndex) {
        Pattern currentPattern = questions.get(currentQuestionIndex);
        boolean correct = currentPattern.checkAnswer(buttonIndex);

        for (JButton btn : answerButtons) {
            btn.setEnabled(false);
        }

        if (correct) {
            score++;
            answerButtons[buttonIndex].setBackground(new Color(40, 217, 115));
            answerButtons[buttonIndex].setForeground(Color.WHITE);
        } else {
            answerButtons[buttonIndex].setBackground(new Color(255, 80, 80));
            answerButtons[buttonIndex].setForeground(Color.WHITE);

            int correctIndex = currentPattern.getCorrectAnswerIndex();
            answerButtons[correctIndex].setBackground(new Color(40, 217, 115));
            answerButtons[correctIndex].setForeground(Color.WHITE);
        }

        Timer timer = new Timer(2000, e -> nextQuestion());
        timer.setRepeats(false);
        timer.start();
    }

    private void nextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < totalQuestions) {
            displayQuestion(currentQuestionIndex);
        } else {
            showFinalScore();
        }
    }

    private void showFinalScore() {
        double percentage = (score * 100.0) / totalQuestions;
        scores.add(new EntryScore("Lukasz", score, difficulty));
        String message = String.format(
                "Koniec gry!\n\nTwój wynik: %d/%d\nProcent: %.1f%%",
                score, totalQuestions, percentage
        );

        String title = "Wynik końcowy";

        JPanel panel = new JPanel(new BorderLayout(5,5));
        JLabel label = new JLabel("Podaj swoje imię:");
        JTextField nameField = new JTextField(15);

        panel.add(label, BorderLayout.NORTH);
        panel.add(nameField, BorderLayout.CENTER);

        int choice = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Czy chcesz zapisać wynik?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            String playerName = nameField.getText().trim();

            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Imię nie może być puste!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }


            scores.add(new EntryScore(playerName, score,difficulty));
            JOptionPane.showMessageDialog(this, "Wynik zapisany!");

        }
        dispose();
    }
}