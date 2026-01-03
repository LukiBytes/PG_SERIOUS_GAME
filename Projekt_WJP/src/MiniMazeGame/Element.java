package MiniMazeGame;

import java.awt.Point;
import java.util.*;

public class Element {
    private Set<Point> points;
    private Set<Point> borderPoints;
    private AreaType[][] border;
    private int boardWidth;
    private int boardHeight;

    public Element(AreaType[][] border) {
        this.border = border;
        points = new HashSet<>();
        borderPoints = new HashSet<>();
        this.boardWidth = border[0].length;
        this.boardHeight = border.length;
    }

    public boolean tryAddElementToBorder() {
        int bestX = -1;
        int bestY = -1;
        int bestCount = 0;

        for (int y = 2; y < boardHeight - 2; y++) {
            for (int x = 2; x < boardWidth - 2; x++) {
                if (checkPosition(x, y)) {
                    int count = calculateCommonPoints(x, y);
                    if (bestCount < count) {
                        bestCount = count;
                        bestX = x;
                        bestY = y;
                    }
                }
            }
        }

        if (bestX == -1 && bestY == -1) {
            return false;
        }

        for (Point p : points) {
            border[p.y + bestY][p.x + bestX] = AreaType.wall;
        }
        for (Point p : borderPoints) {
            border[p.y + bestY][p.x + bestX] = AreaType.border;
        }

        return true;
    }

    private void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    private void calculateBorder() {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            addBorderPoint(x - 1, y - 1);
            addBorderPoint(x, y - 1);
            addBorderPoint(x + 1, y - 1);
            addBorderPoint(x - 1, y);
            addBorderPoint(x + 1, y);
            addBorderPoint(x - 1, y + 1);
            addBorderPoint(x, y + 1);
            addBorderPoint(x + 1, y + 1);
        }
    }

    private void addBorderPoint(int x, int y) {
        Point border = new Point(x, y);
        if (!points.contains(border) && !borderPoints.contains(border)) {
            borderPoints.add(border);
        }
    }

    public boolean checkPosition(int x, int y) {
        for (Point p : points) {
            int px = p.x + x;
            int py = p.y + y;
            if (px <= 1 || boardWidth - 2 <= px || py <= 1 || boardHeight - 2 <= py) {
                return false;
            }
            if (border[py][px] != AreaType.empty) {
                return false;
            }
        }
        return true;
    }

    public int calculateCommonPoints(int x, int y) {
        int count = 0;
        for (Point p : borderPoints) {
            int px = p.x + x;
            int py = p.y + y;
            if (border[py][px] == AreaType.border) {
                count++;
            }
        }
        return count;
    }

    private void moveFigureToBegin() {
        int ymin = Integer.MAX_VALUE;
        int xmin = Integer.MAX_VALUE;

        for (Point p : points) {
            if (ymin > p.y) ymin = p.y;
            if (xmin > p.x) xmin = p.x;
        }

        Set<Point> newSet = new HashSet<>();
        for (Point p : points) {
            newSet.add(new Point(p.x - xmin, p.y - ymin));
        }
        points = newSet;
    }

    public static List<Element> generate(AreaType[][] border) {
        List<Element> list = new ArrayList<>();

        // Krzyże różnych rozmiarów
        list.add(generateCross(border, 3, 3, 3, 3));
        generateCrossWithoutOnePart(list, border, 3);
        list.add(generateCross(border, 2, 2, 2, 2));
        generateCrossWithoutOnePart(list, border, 2);

        // Prostokąty
        list.add(generateArea(border, 1, 1));
        list.add(generateArea(border, 2, 1));
        list.add(generateArea(border, 1, 2));
        list.add(generateArea(border, 1, 3));
        list.add(generateArea(border, 3, 1));
        list.add(generateArea(border, 2, 2));
        list.add(generateArea(border, 2, 3));
        list.add(generateArea(border, 3, 2));

        return list;
    }

    private static void generateCrossWithoutOnePart(List<Element> list, AreaType[][] border, int x) {
        list.add(generateCross(border, 0, x, x, x));
        list.add(generateCross(border, x, 0, x, x));
        list.add(generateCross(border, x, x, 0, x));
        list.add(generateCross(border, x, x, x, 0));
    }

    private static Element generateCross(AreaType[][] border, int up, int right, int down, int left) {
        Element element = new Element(border);
        generateLine(element, 0, 0, Move.up, up);
        generateLine(element, 0, 0, Move.right, right);
        generateLine(element, 0, 0, Move.down, down);
        generateLine(element, 0, 0, Move.left, left);
        element.moveFigureToBegin();
        element.calculateBorder();
        return element;
    }

    private static Element generateArea(AreaType[][] border, int width, int height) {
        Element element = new Element(border);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                element.addPoint(x, y);
            }
        }
        element.calculateBorder();
        return element;
    }

    private static void generateLine(Element element, int x, int y, Move move, int len) {
        for (int i = 0; i < len; i++) {
            element.addPoint(x, y);
            switch (move) {
                case up -> y--;
                case down -> y++;
                case left -> x--;
                case right -> x++;
            }
        }
    }

    enum Move {
        up, down, left, right
    }
}