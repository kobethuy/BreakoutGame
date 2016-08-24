
package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author Toan
 */
public class HUD {
    //Value
    private int score;
    
    //Constructor
    public HUD(){
        
        init();
        
    }
    public void init(){
        score = 0;
    }
    public void draw(Graphics2D g){
        
        g.setFont(new Font("Courier New", Font.PLAIN, 14));
        g.setColor(Color.RED);
        g.drawString("Score: " + score, 20, 20);
    }
    public int getScore(){
        return score;
    }
    public void addScore(int scoreToAdd){
        score = score + scoreToAdd;
    }
}
