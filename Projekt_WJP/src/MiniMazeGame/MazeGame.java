package MiniMazeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class MazeGame extends JDialog {
    private JPanel gamePanel;
    private AreaType[][] maze;
    private int cellSize;

    private Timer timer;
    private int seconds = 0;

    private final int WINDOW_WIDTH = 800;
    private final int WINDOW_HEIGHT = 600;

    // ✅ NOWE - ścieżka gracza
    private Set<Point> playerPath;
    private Point currentCell;
    private boolean isDrawing;
    private Point startCell;
    private Point endCell;
    private boolean gameWon;

    private int offsetX;
    private int offsetY;

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

        // ✅ Inicjalizacja ścieżki
        playerPath = new HashSet<>();
        isDrawing = false;
        gameWon = false;

        // ✅ Znajdź start i koniec
        findStartAndEnd();

        // Panel gry
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMaze(g);
            }
        };

        // ✅ OBSŁUGA MYSZY
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point cell = getCellFromPixel(e.getX(), e.getY());
                if (cell != null && cell.equals(startCell)) {
                    isDrawing = true;
                    playerPath.clear();
                    playerPath.add(cell);
                    currentCell = cell;
                    gamePanel.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDrawing = false;
                playerPath.clear();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDrawing && !gameWon) {
                    Point cell = getCellFromPixel(e.getX(), e.getY());

                    if (cell != null && !cell.equals(currentCell)) {
                        // Sprawdź czy można przejść
                        if (canMoveTo(cell)) {
                            playerPath.add(cell);
                            currentCell = cell;

                            // Sprawdź czy dotarliśmy do mety
                            if (cell.equals(endCell)) {
                                gameWon = true;
                                timer.stop();
                                showVictory();
                            }

                            gamePanel.repaint();
                        } else {
                            // Trafiliśmy na ścianę - restart
                            isDrawing = false;
                            playerPath.clear();
                            gamePanel.repaint();
                        }
                    }
                }
            }
        };

        gamePanel.addMouseListener(mouseAdapter);
        gamePanel.addMouseMotionListener(mouseAdapter);

        timer = new Timer(1000, e -> {
            seconds++;
            gamePanel.repaint();
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

    // ✅ Znajdź pozycje startu i końca
    private void findStartAndEnd() {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == AreaType.start) {
                    startCell = new Point(x, y);
                }
                if (maze[y][x] == AreaType.end) {
                    endCell = new Point(x, y);
                }
            }
        }
    }

    // ✅ Konwersja pikseli na komórkę
    private Point getCellFromPixel(int px, int py) {
        int mazePixelWidth = maze[0].length * cellSize;
        int mazePixelHeight = maze.length * cellSize;

        offsetX = (WINDOW_WIDTH - mazePixelWidth) / 2;
        offsetY = (WINDOW_HEIGHT - mazePixelHeight) / 2;

        int x = (px - offsetX) / cellSize;
        int y = (py - offsetY) / cellSize;

        if (x >= 0 && x < maze[0].length && y >= 0 && y < maze.length) {
            return new Point(x, y);
        }
        return null;
    }

    // ✅ Sprawdź czy można przejść do komórki
    private boolean canMoveTo(Point cell) {
        if (cell.x < 0 || cell.x >= maze[0].length ||
                cell.y < 0 || cell.y >= maze.length) {
            return false;
        }

        AreaType type = maze[cell.y][cell.x];

        // Można przejść przez: start, end, empty, border
        return type == AreaType.start ||
                type == AreaType.end ||
                type == AreaType.empty ||
                type == AreaType.border;
    }

    private int calculateCellSize(int mazeWidth, int mazeHeight) {
        int availableWidth = WINDOW_WIDTH - 80;
        int availableHeight = WINDOW_HEIGHT - 80;

        int cellSizeByWidth = availableWidth / mazeWidth;
        int cellSizeByHeight = availableHeight / mazeHeight;

        return Math.min(cellSizeByWidth, cellSizeByHeight);
    }

    private void drawMaze(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        int mazePixelWidth = maze[0].length * cellSize;
        int mazePixelHeight = maze.length * cellSize;

        offsetX = (WINDOW_WIDTH - mazePixelWidth) / 2;
        offsetY = (WINDOW_HEIGHT - mazePixelHeight) / 2;

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

        // ✅ RYSUJ ŚCIEŻKĘ GRACZA (niebieska)
        g2.setColor(new Color(50, 150, 255, 180));  // Przezroczyste niebieskie
        for (Point p : playerPath) {
            int px = offsetX + (p.x * cellSize);
            int py = offsetY + (p.y * cellSize);
            g2.fillRect(px, py, cellSize, cellSize);
        }

        // Czas
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.drawString("Czas: " + seconds + "s", WINDOW_WIDTH - 120, 30);

        // ✅ Komunikat jeśli wygrana
        if (gameWon) {
            showVictory();
        }
    }

    // ✅ Pokaż komunikat zwycięstwa
    private void showVictory() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Gratulacje!\n\nUkończyłeś labirynt w " + seconds + " sekund!",
                    "Zwycięstwo! 🎉",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        });
    }
}