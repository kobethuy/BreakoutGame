
package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Toan
 */
public class Map {
    
    //Value
    private Brick[][] map;
    private final int brickHeight, brickWidth;
    
    public final int HOR_PAD = 80, VERT_PAD = 50;
    
    public Map(int row, int col) throws Exception{
        initMap(row, col);
        brickWidth = (BrickBreaker.WIDTH - 2 * HOR_PAD) / col;
        brickHeight = (BrickBreaker.HEIGHT / 2 - VERT_PAD * 2) / row;
    }
    private void initMap(int row, int col) throws Exception{
        map = new Brick[row][col];
        readMap(map, "1.txt");
    }
    
    private void readMap(Brick[][] map, String name) throws Exception {
        Path fp = Paths.get(name);
        Scanner br = new Scanner(fp);
        int b = 0;
        
        while(br.hasNextLine()) {
            String line = br.nextLine();
            for (int foo = 0; foo < line.length(); foo++) {
                this.map[b][foo] = new Brick(Integer.parseInt(String.valueOf(line.charAt(foo))));
            }
            b++;
        }
    }
    
    public void draw(Graphics2D g){
        
        for(int row = 0; row < map.length; row++){
            for(int col = 0; col < map[row].length; col++){
                //System.out.print(map[row].length);
                if(map[row][col].getHealth() > 0){
                    
                    if(map[row][col].getHealth() == 1) {
                        g.setColor(new Color(0, 200, 200) );
                    }
                    if(map[row][col].getHealth() == 2) {
                        g.setColor(new Color(0, 150, 150) );
                    }
                    if(map[row][col].getHealth() == 3) {
                        g.setColor(new Color(0, 100, 100) );
                    }
                    
                    g.fillRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(2));
                    g.setColor(Color.WHITE);
                    g.drawRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
                
                }   
            }
        }
    }
    
    public boolean winCheck(){
        
       boolean isWin = false;
       
       int bricksRemaining = 0;
       
        for (Brick[] mapArr : map) {
            for (int col = 0; col < map[0].length; col++) {
                bricksRemaining += mapArr[col].getHealth();
            }
        }
        if(bricksRemaining == 0){
            isWin = true;
        }
       
       return isWin;
    }
    
    public Brick[][] getMapArray(){
        return map;
    }
    public void setBrick(int row, int col, int value){
        map[row][col].setHealth(value);
    }
    public int getBrickWidth(){
        return brickWidth;
    }
    public int getBrickHeight(){
        return brickHeight;
    }
    public void hitBrick(int row, int col){
        map[row][col].setHealth(map[row][col].getHealth() - 1);
        if(map[row][col].getHealth() < 0){
            map[row][col].setHealth(0);
        }
    }
}
