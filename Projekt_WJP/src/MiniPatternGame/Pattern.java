package MiniPatternGame;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Pattern {

    Random random = new Random();
    private int[] fullSequence;
    private int hiddenIndex;
    private int correctAnswer;
    private int[] answerOptions;
    private int correctAnswerIndex;
    private int difficulty;

    public Pattern(int[] fullSequence, int hiddenIndex, int difficulty) {
        this.fullSequence = fullSequence;
        this.hiddenIndex = hiddenIndex;
        this.correctAnswer = fullSequence[hiddenIndex];
        this.difficulty = difficulty;

        generateAnswerOptions();
    }

    private void generateAnswerOptions() {
        answerOptions = new int[4];

        System.out.println("Poprawna: " + correctAnswer);

        answerOptions[0] = correctAnswer;
        answerOptions[1] = correctAnswer + random.nextInt(3) + 1;  // +1 do +3
        answerOptions[2] = correctAnswer - random.nextInt(3) - 1;  // -1 do -3
        answerOptions[3] = correctAnswer + random.nextInt(5) + 4;  // +4 do +8


        // Pomieszaj
        shuffle(answerOptions);

        // Znajdź poprawną
        for (int i = 0; i < 4; i++) {
            if (answerOptions[i] == correctAnswer) {
                correctAnswerIndex = i;
                break;
            }
        }

        System.out.println("Odpowiedzi: " + java.util.Arrays.toString(answerOptions));
    }


    private void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public int[] getFullSequence() {
        return fullSequence;
    }

    public int getHiddenIndex() {
        return hiddenIndex;
    }

    public int[] getAnswerOptions() {
        return answerOptions;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }


    public boolean checkAnswer(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }

    public int getDifficulty(){
        return difficulty;
    }
}