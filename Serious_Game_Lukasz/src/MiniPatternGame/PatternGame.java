package MiniPatternGame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PatternGame extends JDialog {

    private int currentQuestionIndex;
    private int score;
    private int difficulty;
    private int totalQuestions;


    public PatternGame(JFrame parent, int difficulty, int questions) {
        super(parent, "Wzorce - Gra", true);

        this.difficulty = difficulty;
        this.totalQuestions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0;


        //TODO
        //initializeUI();

        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    //private void initializeUI() {}


}

