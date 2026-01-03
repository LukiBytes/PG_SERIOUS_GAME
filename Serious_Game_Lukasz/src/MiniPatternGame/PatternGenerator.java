package MiniPatternGame;

import java.util.Random;

public class PatternGenerator {


    Random random = new Random();

    public Pattern generateArithmetic(int difficulty){

        int length;
        int minFirst, maxFirst;
        int minDiff, maxDiff; //różnica r

        if (difficulty == 1){
            length = random.nextInt(2) + 4; //4, 5
            minFirst = 1;
            maxFirst = 10;
            minDiff = 2;
            maxDiff = 5;
        } else if (difficulty == 2) {
            length = random.nextInt(2) + 5; //5, 6
            minFirst = 5;
            maxFirst = 20;
            minDiff = 3;
            maxDiff = 10;
        } else {
        length = random.nextInt(2) + 6; //6,7,8
        minFirst = 10;
        maxFirst = 50;
        minDiff = 5;
        maxDiff = 15;
        }

        //=========LOSUJ PARAMETRY =========

        //obliczanie zakresu
        int firstNumber = random.nextInt(maxFirst - minFirst + 1) + minFirst;
        int difference = random.nextInt(maxDiff - minDiff + 1) + minDiff;

        int[] sequence = new int[length];

        for (int i = 0; i < length; i++){
            sequence[i] = firstNumber + (i * difference);
        }

        int correctAnswer = sequence[length - 1] + difference;

        return new Pattern(sequence, correctAnswer, "Arytmetyczny", difficulty);
    }

    public Pattern generateGeometric(int difficulty) {
        int length;
        int minFirst, maxFirst;
        int[] multipliers;

        if (difficulty == 1) {
            length = 4;
            minFirst = 1;
            maxFirst = 5;
            multipliers = new int[]{2,3};           // Tylko proste
        }
        else if (difficulty == 2) {
            length = random.nextInt(2) + 4;      // 4 lub 5
            minFirst = 2;
            maxFirst = 10;
            multipliers = new int[]{2,3,5};
        }
        else {
            length = random.nextInt(2) + 5;      // 5 lub 6
            minFirst = 1;
            maxFirst = 10;
            multipliers = new int[]{2,3,4,5,10};
        }

        int firstNumber = random.nextInt(maxFirst - minFirst + 1) + minFirst;
        int multiplier = random.nextInt(maxFirst - minFirst + 1) + minFirst;

        int[] sequence = new int[length];

        for (int i = 0; i < length; i++) {
            sequence[i] = (int) (firstNumber * Math.pow(multiplier, i));
        }

        int correctAnswer = sequence[length - 1] * multiplier;

        return new Pattern(sequence, correctAnswer, "Geometryczny", difficulty);
    }



    public Pattern generatePowers(int difficulty) {
        //TODO
        return null;
    }

    public Pattern generateAlternating(int difficulty) {
        //TODO
        return null;
    }

    public Pattern generateFibonacci(int difficulty) {
        //TODO
        return null;
    }

    public Pattern generateRecursive(int difficulty) {
        //TODO
        return null;
    }

    public Pattern generatePattern(int difficulty){

       int draw;

        if (difficulty == 1){
            draw = random.nextInt(2);
        } else if (difficulty == 2) {
            draw = random.nextInt(4);
        }else {
            draw = random.nextInt(6);
        }

        switch (draw){
            case 0:
                return generateArithmetic(difficulty);
            case 1:
                return generateGeometric(difficulty);
            case 2:
                if (difficulty >= 2) {
                    return generatePowers(difficulty);
                } else {
                    return generateArithmetic(difficulty);
                }
            case 3:
                if (difficulty >= 2) {
                    return generateAlternating(difficulty);
                } else {
                    return generateGeometric(difficulty);
                }
            case 4:
                if (difficulty >= 3) {
                    return generateFibonacci(difficulty);
                } else {
                    return generateArithmetic(difficulty);
                }
            case 5:
                if (difficulty >= 3) {
                    return generateRecursive(difficulty);
                } else {
                    return generateGeometric(difficulty);
                }
            default:
                return generateArithmetic(difficulty);


        }
   }



}
