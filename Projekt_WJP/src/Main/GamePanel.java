package Main;

import Block.BlockHandler;
import MiniMazeGame.MazeGameHome;
import MiniMemoryGame.MemoryGameHome;
import MiniPatternGame.PatternGameHome;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    //SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    public int tileSize = originalTileSize * scale;

    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //FPS
    int FPS = 60;

    BlockHandler blockHandler = new BlockHandler(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    //PLAYER DEFAULT POSITION
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    int enterMiniGame1 = 0;
    int enterMiniGame2 = 0;
    int enterMiniGame3 = 0;


    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyH);

        blockHandler.blockImage();
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null){

            update();
            repaint();

            //System.out.println("Player X position: "+ playerX);
            //System.out.println("Player Y position: " + playerY);

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if(remainingTime < 0){
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(){

        if (keyH.upPressed) {
            playerY -= playerSpeed;
        }
        if (keyH.downPressed) {
            playerY += playerSpeed;
        }
        if (keyH.leftPressed) {
            playerX -= playerSpeed;
        }
        if (keyH.rightPressed) {
            playerX += playerSpeed;
        }


        playerX = Math.max(0, Math.min(playerX, 720));
        playerY = Math.max(0, Math.min(playerY, 526));


        //GRA 1
        if (playerX == 0 && playerY > 133 && playerY < 155){
            //System.out.println("GRA1");
            enterMiniGame1 = 1;
            if (keyH.ePressed){
                startMiniGame1();
                keyH.ePressed = false; //żeby nie otwierac miliona okien
            }
        } else {
            enterMiniGame1 =0;
        }

        //GRA 2
        if (playerY == 526 && playerX > 474 && playerX < 490){
            //System.out.println("GRA2");
            enterMiniGame2 = 1;
            if (keyH.ePressed){
                startMiniGame2();
                keyH.ePressed = false; //żeby nie otwierac miliona okien
            }
        } else {
            enterMiniGame2 =0;
        }

        //GRA 3
        if (playerX == 720 && playerY > 230 && playerY < 252){
            //System.out.println("GRA3");
            enterMiniGame3 = 1;
            if (keyH.ePressed){
                startMiniGame3();
                keyH.ePressed = false; //żeby nie otwierac miliona okien
            }
        } else {
            enterMiniGame3 =0;
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        blockHandler.draw(g2);

        g2.setColor(Color.BLUE);
        g2.fillOval(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }

    public void startMiniGame1(){
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new MazeGameHome(parentFrame);
    }

    public void startMiniGame2(){
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new PatternGameHome(parentFrame);
    }

    public void startMiniGame3(){
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        new MemoryGameHome(parentFrame);
    }

    public int getEnterMiniGame1(){
        return enterMiniGame1;
    }

    public int getEnterMiniGame2(){
        return enterMiniGame2;
    }

    public int getEnterMiniGame3(){
        return enterMiniGame3;
    }
}