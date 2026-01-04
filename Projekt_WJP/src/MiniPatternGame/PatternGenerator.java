package MiniPatternGame;

import java.util.Random;

public class PatternGenerator {

    Random random = new Random();

    public Pattern generateArithmetic(int difficulty) {

        int length = 4;
        int minFirst, maxFirst;
        int minDiff, maxDiff;

        if (difficulty == 1) {
            minFirst = 1;
            maxFirst = 10;
            minDiff = 2;
            maxDiff = 5;
        } else if (difficulty == 2) {
            minFirst = 5;
            maxFirst = 20;
            minDiff = 3;
            maxDiff = 10;
        } else {
            minFirst = 10;
            maxFirst = 50;
            minDiff = 5;
            maxDiff = 15;
        }

        int firstNumber = random.nextInt(maxFirst - minFirst + 1) + minFirst;
        int difference = random.nextInt(maxDiff - minDiff + 1) + minDiff;

        int[] fullSequence = new int[length];

        for (int i = 0; i < length; i++) {
            fullSequence[i] = firstNumber + (i * difference);
        }


        int hiddenIndex;
        if (length <= 3) {
            hiddenIndex = random.nextInt(length);
        } else {
            hiddenIndex = random.nextInt(length - 2) + 1;
        }

        return new Pattern(fullSequence, hiddenIndex, difficulty);
    }

    public Pattern generatePattern(int difficulty) {

        int draw;

        if (difficulty == 1) {
            draw = 0;
        } else if (difficulty == 2) {
            draw = 1;
        } else {
            draw = 2;
        }

        switch (draw) {
            case 0:
                return generateArithmetic(difficulty);
            case 1:
                return generateArithmetic(difficulty);
            case 2:
                return generateArithmetic(difficulty);
            default:
                return generateArithmetic(difficulty);
        }
    }
}