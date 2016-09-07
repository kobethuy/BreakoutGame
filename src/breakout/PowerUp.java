
package breakout;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 *
 * @author Toan
 */
public class PowerUp {
    //Value
    private int x, y, dy, type, width, height;
    
    private boolean isOnScreen;
    private boolean wasUsed;
    
    private final Color color;

    public final static Color COLOR = Color.GREEN;
    
    //Constructor
    public PowerUp(int x, int y, int type, int width, int height){
        
        this.x = x;
        this.y = y;
        this.type = type;
        this.width = width;
        this.height = height; 
        color = COLOR;
        
        if(type < 4){ type = 4;}
        if(type > 5){ type = 5;}
        
        dy = 5;
        
        isOnScreen = true;
        
        wasUsed = false;
    }
    public void draw(Graphics2D g){
        
        g.setColor(color);
        g.fillRect(x, y, width, height);
        /*
        try {
            g.drawImage(ImageIO.read(new File(getClass().getResource("../Resources/img/powerup.png").toURI())), (int) x, (int) y, null);
        } catch (Exception ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
    public void update(){
        y += dy;
        if(y > BrickBreaker.HEIGHT){
            isOnScreen = false;
        }
    }
    public int getX(){
        return x;
    }
    public void setX(int newX){
        x = newX;
    }
    public int getY(){
        return y;
    }
    public void setY(int newY){
        y = newY;
    }
    public int getDY(){
        return dy;
    }
    public void setDY(int newDY){
        dy = newDY;
    }
    public int getType(){
        return type;
    }
    public boolean getIsOnScreen(){
        return isOnScreen;
    }
    public void setIsOnScreen(boolean onScreen){
        isOnScreen = onScreen;
    }
    public boolean getWasUsed(){ 
        return wasUsed;
    }
    
    public void setWasUsed(boolean used){
        wasUsed = used;
    }
    
    public Rectangle getRect(){
        return new Rectangle(x, y, width, height);
    }
}
