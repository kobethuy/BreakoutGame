
package breakout;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author Toan
 */
public class BrickExplosion {
    //Value
    private final ArrayList<BrickPiece> pieces;
    private final int x, y;
    private final Map map;
    private boolean isActive;
    private final long startTime;
    
    //Constructor
    public BrickExplosion(int theX, int theY, Map theMap){
        x = theX;
        y = theY;
        this.map = theMap;
        isActive = true;
        startTime = System.nanoTime();
        pieces = new ArrayList<>();
        init();
    }
    private void init(){
        int randNum = (int)(Math.random() * 20 + 5);
        
        for (int i = 0; i < randNum; i++){
            pieces.add(new BrickPiece(x, y, map));
        }
    }
    public void update(){
        for(BrickPiece bp : pieces){
            bp.update();
        }
        if((System.nanoTime() - startTime) / 1000000 > 2000 && isActive){
            isActive = false;
        }
    }
    public void draw(Graphics2D g){
        for(BrickPiece bp : pieces){
            bp.draw(g);
        }
    }
    public boolean getIsActive(){return isActive;}
}
