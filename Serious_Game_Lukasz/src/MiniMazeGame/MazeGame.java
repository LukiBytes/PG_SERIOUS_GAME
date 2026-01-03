package MiniMazeGame;

import javax.swing.*;
import java.awt.*;

public class MazeGame extends JDialog {
    private JPanel gamePanel;
    private AreaType[][] maze;
    private int cellSize;

    //TODO
    //Back Button

    private Timer timer;
    private int seconds = 0;



    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;

    public MazeGame(JFrame parent, int difficulty) {
        super(parent, "Labirynt - Gra", true);

        int mazeWidth, mazeHeight;


        switch (difficulty) {
            case 1:
                mazeWidth = 10;
                mazeHeight = 10;
                break;

            case 2:
                mazeWidth = 15;
                mazeHeight = 15;
                break;

            case 3:
                mazeWidth = 20;
                mazeHeight = 20;
                break;

            default:
                mazeWidth = 35;
                mazeHeight = 25;
        }


        MazeGenerator generator = new MazeGenerator(mazeWidth, mazeHeight);
        maze = generator.generate();


        cellSize = calculateCellSize(mazeWidth, mazeHeight);

        // Panel gry
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };

        timer = new Timer(1000, e -> {
            seconds++;
            gamePanel.repaint();  // Odśwież -> nowy czas
        });
        timer.start();

        gamePanel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        gamePanel.setBackground(Color.WHITE);

        add(gamePanel);
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        setResizable(false);
        setVisible(true);
    }


    private int calculateCellSize(int mazeWidth, int mazeHeight) {
        // Margines
        int availableWidth = WINDOW_WIDTH - 80;
        int availableHeight = WINDOW_HEIGHT - 80;

        // jak duża może być kratka
        int cellSizeByWidth = availableWidth / mazeWidth;
        int cellSizeByHeight = availableHeight / mazeHeight;


        return Math.min(cellSizeByWidth, cellSizeByHeight);
    }

    private void drawMaze(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;


        int mazePixelWidth = maze[0].length * cellSize;
        int mazePixelHeight = maze.length * cellSize;

        int offsetX = (WINDOW_WIDTH - mazePixelWidth) / 2;
        int offsetY = (WINDOW_HEIGHT - mazePixelHeight) / 2;

        // Rysuj labirynt
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                int px = offsetX + (x * cellSize);
                int py = offsetY + (y * cellSize);

                switch (maze[y][x]) {
                    case wall:
                        g2.setColor(Color.BLACK);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                    case start:
                        g2.setColor(Color.GREEN);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                    case end:
                        g2.setColor(Color.RED);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                    default:
                        g2.setColor(Color.WHITE);
                        g2.fillRect(px, py, cellSize, cellSize);
                        break;
                }

                // Siatka
                g2.setColor(new Color(200, 200, 200));
                g2.drawRect(px, py, cellSize, cellSize);
            }
        }


        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Czas: " + seconds + "s", WINDOW_WIDTH - 120,30);


    }



}