package Block;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;


public class BlockHandler {
    GamePanel gp;
    Block[] mapBlocks;  //tablica


    public BlockHandler(GamePanel gp){
        this.gp = gp;
        mapBlocks = new Block[10];

    }

    public void blockImage(){
        try{
            mapBlocks[0] = new Block();
            mapBlocks[0].image = ImageIO.read(getClass().getResourceAsStream("/Images/floor.png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){

        int column = 0;
        int row = 0;
        int x = 0;
        int y = 0;

        while(column < gp.maxScreenCol && row < gp.maxScreenRow){
            g2.drawImage(mapBlocks[0].image,x,y,gp.tileSize,gp.tileSize,null);
            column++;
            x += gp.tileSize;

            if(column == gp.maxScreenCol){
                column = 0;
                x = 0;
                row++;
                y+= gp.tileSize;
            }
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(0,gp.tileSize * 3,gp.tileSize,gp.tileSize);

        if (gp.getEnterMiniGame1() == 1){
            g2.setColor(Color.RED);
            g2.fillRect(0,gp.tileSize * 3,gp.tileSize,gp.tileSize);
        }

        g2.setColor(Color.BLACK);
        g2.fillRect(gp.tileSize * 10,gp.tileSize * 11,gp.tileSize,gp.tileSize);
        
        if (gp.getEnterMiniGame2() == 1){
            g2.setColor(Color.RED);
            g2.fillRect(gp.tileSize * 10,gp.tileSize * 11,gp.tileSize,gp.tileSize);
        }
    }
}