
package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author Toan
 */
public class Map {
    
    //Value
    private int[][] theMap;
    private int brickHeight, brickWidth;
    
    public final int HOR_PAD = 80, VERT_PAD = 50;
    
    public Map(int row, int col) throws Exception{
        initMap(row, col);
        brickWidth = (BrickBreaker.WIDTH - 2 * HOR_PAD) / col;
        brickHeight = (BrickBreaker.HEIGHT / 2 - VERT_PAD * 2) / row;
    }
    private void initMap(int row, int col) throws Exception{
        theMap = new int[row][col];
        readMap(theMap, "1.txt"); 
    }
    
    private void readMap(int[][] map, String name) throws Exception {
        FileReader fp = new FileReader(name);
        BufferedReader br = new BufferedReader(fp);
        String line = br.readLine();
        int b = 0;
        
        while(line != null) {
            
            for (int foo = 0; foo < line.length(); foo++) {
                theMap[b][foo] = Integer.parseInt(String.valueOf(line.charAt(foo)));
            }
            
            System.out.println();
            line = br.readLine();
            b++;
        }
    }
    public void draw(Graphics2D g){
        
        for(int row = 0; row < theMap.length; row++){
            for(int col = 0; col < theMap[row].length; col++){
                //System.out.print(theMap[row].length);
                if(theMap[row][col] > 0){
                    
                    if(theMap[row][col] == 1) {
                        g.setColor(new Color(0, 200, 200) );
                    }
                    if(theMap[row][col] == 2) {
                        g.setColor(new Color(0, 150, 150) );
                    }
                    if(theMap[row][col] == 3) {
                        g.setColor(new Color(0, 100, 100) );
                    }
                    
                    if(theMap[row][col] == PowerUp.WIDEPADDLE){
                        g.setColor(PowerUp.WIDECOLOR);
                    }
                    if(theMap[row][col] == PowerUp.FASTBALL){
                        g.setColor(PowerUp.FASTCOLOR);
                    }
                    
                    g.fillRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(2));
                    g.setColor(Color.WHITE);
                    g.drawRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
                
                }   
            }
            System.out.println();
        }
    }
    
    public boolean isThereAWin(){
        
       boolean thereIsAWin = false;
       
       int bricksRemaining = 0;
       
       for(int row = 0; row < theMap.length; row ++){
            for(int col = 0; col < theMap[0].length; col++){
                bricksRemaining += theMap[row][col];
            }
        }
        if(bricksRemaining == 0){
            thereIsAWin = true;
        }
       
       return thereIsAWin;
    }
    
    public int[][] getMapArray(){
        return theMap;
    }
    public void setBrick(int row, int col, int value){
        theMap[row][col] = value;
    }
    public int getBrickWidth(){
        return brickWidth;
    }
    public int getBrickHeight(){
        return brickHeight;
    }
    public void hitBrick(int row, int col){
        theMap[row][col] -= 1;
        if(theMap[row][col] < 0){
            theMap[row][col] = 0;
        }
    }
}
