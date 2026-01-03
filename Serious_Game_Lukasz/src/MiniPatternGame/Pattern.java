package MiniPatternGame;

public class Pattern {

    private int[] sequence;
    private int correctAnswer;
    private int[] answerOptions;
    private int correctAnswerIndex;
    private String patternType;
    private int difficulty;

    public Pattern(int[] sequence, int correctAnswer, String patternType, int difficulty) {
        this.sequence = sequence;
        this.correctAnswer = correctAnswer;
        this.patternType = patternType;
        this.difficulty = difficulty;
    }


}