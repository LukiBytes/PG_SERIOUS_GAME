package MiniMazeGame;

import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private AreaType[][] board;
    private Random random;
    private int width;
    private int height;

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
        board = new AreaType[height][width];
        random = new Random();
    }

    public AreaType[][] generate() {
        addBaseBorders();

        System.out.println("Generowanie labiryntu: " + width + "x" + height);

        List<Element> list = Element.generate(board);
        System.out.println("Wygenerowano " + list.size() + " elementów");

        int iterations = 0;
        while (!list.isEmpty() && iterations < 1000) {
            Element element = list.get(random.nextInt(list.size()));
            if (!element.tryAddElementToBorder()) {
                list.remove(element);
            }
            iterations++;
        }

        System.out.println("Zakończono po " + iterations + " iteracjach");

        addStartAndEnd();

        return board;
    }

    private void addBaseBorders() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (x == 0 || y == 0 || x == board[y].length - 1 || y == board.length - 1) {
                    board[y][x] = AreaType.wall;
                } else if (x == 1 || y == 1 || x == board[y].length - 2 || y == board.length - 2) {
                    board[y][x] = AreaType.border;
                } else {
                    board[y][x] = AreaType.empty;
                }
            }
        }
    }

    private void addStartAndEnd() {
        //start
        for (int y = 2; y < height - 2; y++) {
            if (board[y][2] == AreaType.border || board[y][2] == AreaType.empty) {
                board[y][2] = AreaType.start;
                break;
            }
        }

        //meta
        for (int y = height - 3; y >= 2; y--) {
            if (board[y][width - 3] == AreaType.border || board[y][width - 3] == AreaType.empty) {
                board[y][width - 3] = AreaType.end;
                break;
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}