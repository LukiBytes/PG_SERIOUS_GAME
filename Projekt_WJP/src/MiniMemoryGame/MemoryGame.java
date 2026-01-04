package MiniMemoryGame;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JDialog {

    boolean canPlay = false;
    int playerProgress = 0;

    Timer timer;
    JPanel gamePanel;
    JPanel topPanel;
    JPanel centerPanel;
    JPanel bottomPanel;

    JLabel scoreLabel;
    JLabel instructionLabel;

    JButton back;
    JButton mainColorButton;
    JButton[] answerButtons = new JButton[6];

    int sequenceLength;
    int totalQuestions;

    List<Color> colorList;

    public MemoryGame(JFrame parent, int difficulty) {
        super(parent, "Memory - Gra", true);

        if (difficulty == 1) {
            sequenceLength = 4;
        } else if (difficulty == 2) {
            sequenceLength = 5;
        } else {
            sequenceLength = 6;
        }

        colorList = generateSequence(sequenceLength);

        initializeUI();
        getColorButtons();
        start();

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public List<Color> generateSequence(int sequenceLength) {
        List<Color> list = new ArrayList<>();
        list.add(Color.BLUE);
        list.add(Color.BLACK);
        list.add(Color.GREEN);
        list.add(Color.GRAY);
        list.add(Color.ORANGE);
        list.add(Color.RED);
        list.add(Color.PINK);
        list.add(Color.YELLOW);

        Collections.shuffle(list);

        if (sequenceLength < list.size()) {
            list.subList(sequenceLength, list.size()).clear();
        }

        System.out.println("Sekwencja: " + list);
        return list;
    }

    public void start() {
        canPlay = false;
        playerProgress = 0;


        for (int i = 0; i < sequenceLength; i++) {
            answerButtons[i].setEnabled(false);
        }

        int delay = 1000;
        int[] index = {0};

        timer = new Timer(delay, e -> {
            if (index[0] < colorList.size()) {
                mainColorButton.setBackground(colorList.get(index[0]));
                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
                mainColorButton.setBackground(Color.WHITE);

                canPlay = true;


                for (int i = 0; i < sequenceLength; i++) {
                    answerButtons[i].setEnabled(true);
                }
            }
        });

        timer.start();
    }

    public void checkAnswer(int buttonIndex) {
        if (!canPlay) {
            return; //wychodzi z funckji nie sprawdza odpowiedzi
        }

        Color pressedButton = answerButtons[buttonIndex].getBackground();
        Color rightColor = colorList.get(playerProgress);

        if (pressedButton.equals(rightColor)) {

            answerButtons[buttonIndex].setEnabled(false);
            answerButtons[buttonIndex].setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));

            playerProgress++;

            if (playerProgress == colorList.size()) {

                JOptionPane.showMessageDialog(this, "BRAWO! Wygrałeś!");
                dispose();
            }
        } else {

            answerButtons[buttonIndex].setBorder(BorderFactory.createLineBorder(Color.RED, 3));

            Timer errorTimer = new Timer(500, evt -> {
                JOptionPane.showMessageDialog(this, "Zła kolejność! Spróbuj ponownie.");
                dispose();
            });
            errorTimer.setRepeats(false);
            errorTimer.start();
        }
    }

    public void initializeUI() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(230, 230, 230));

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(230, 230, 230));
        topPanel.setPreferredSize(new Dimension(800, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        scoreLabel = new JLabel("Progress: 0/" + sequenceLength);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(180, 80));

        back = new JButton("← Powrót");
        back.setOpaque(true);
        back.setBorderPainted(false);
        back.setFont(new Font("Segoe UI", Font.BOLD, 16));
        back.setBackground(new Color(200, 200, 200));
        back.setForeground(Color.BLACK);
        back.setPreferredSize(new Dimension(140, 50));
        back.setFocusable(false);
        back.addActionListener(e -> dispose());

        leftPanel.add(back);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(scoreLabel, BorderLayout.EAST);

        mainColorButton = new JButton("");
        mainColorButton.setFont(new Font("Arial", Font.BOLD, 32));
        mainColorButton.setFocusable(false);
        mainColorButton.setPreferredSize(new Dimension(200, 200));
        mainColorButton.setMaximumSize(new Dimension(200, 200));
        mainColorButton.setOpaque(true);
        mainColorButton.setBorderPainted(false);
        mainColorButton.setBackground(Color.WHITE);
        mainColorButton.setForeground(Color.BLACK);
        mainColorButton.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        mainColorButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(230, 230, 230));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        instructionLabel = new JLabel("Zapamiętaj kolejność kolorów");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(instructionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(mainColorButton);
        centerPanel.add(Box.createVerticalGlue());

        bottomPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        bottomPanel.setBackground(new Color(230, 230, 230));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 40, 100));

        for (int i = 0; i < sequenceLength; i++) {
            answerButtons[i] = new JButton("");
            answerButtons[i].setFont(new Font("Arial", Font.BOLD, 32));
            answerButtons[i].setFocusable(false);
            answerButtons[i].setPreferredSize(new Dimension(120, 80));

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

    public void getColorButtons() {

        List<Color> shuffleList = new ArrayList<>(colorList);
        Collections.shuffle(shuffleList);

        for (int i = 0; i < sequenceLength; i++) {
            answerButtons[i].setBackground(shuffleList.get(i));
        }
    }
}