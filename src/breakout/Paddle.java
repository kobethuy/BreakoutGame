
package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Toan
 */
public class Paddle {
    
   
    
    //Value
    private double x, y;
    private int width, height, startWidth, startHeight;
    private long widthTimer;
    private boolean altWidth;
    private double targetx;
    private final int PADDLE_X_START = (BrickBreaker.WIDTH / 2) - (width / 2);
    private final int PADDLE_Y_START = BrickBreaker.HEIGHT - height;
    
    public final int YPOS = BrickBreaker.HEIGHT - 50;
    //Constructor
    public Paddle(int theWidth, int theHeight){
        altWidth = false;
        width = theWidth;
        startWidth = theWidth;
        height = theHeight;
        startHeight = height;
        
        x = BrickBreaker.WIDTH / 2 - width / 2;
    }
    
    //Places the paddle back in starting position at center of screen
    public void reset() {
	x = PADDLE_X_START;
	y = PADDLE_Y_START;
    }
    //update
    public void update(){
        if((System.nanoTime() - widthTimer) / 1000 > 6000000){
            width = startWidth;
            altWidth = false;
        }
        x +=(targetx - x) * .3;
        
        int dif = (int)Math.abs(targetx -x) / 5;
        height = startHeight - dif;
        if(height < 2){height = 2;}
    }
    
    //draw
    public void draw(Graphics2D g){
        
        int yDraw = YPOS + (startHeight - height) / 2;
        g.setColor(Color.DARK_GRAY);
        g.fillRect((int)x, yDraw, width, height);
        
        if(altWidth == true){
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Courier New", Font.BOLD, 18));
            g.drawString(" Shrinking in " + (6 - (System.nanoTime() - widthTimer) / 1000000000), (int)x, YPOS + 18);
            
        }
    }
    
    public void mouseMoved(int mouseXPos){
        targetx = mouseXPos - width / 2;
        
        if(targetx > BrickBreaker.WIDTH - width){
            targetx = BrickBreaker.WIDTH - width;
        }
        if(targetx < 0){targetx = 0;}
    }
    
    public Rectangle getRect(){
        return new Rectangle((int)x, YPOS, width, height);
    }
     public int getWidth(){
        return width;
    }
    public void setWidth(int newWidth){
        altWidth = true;
        width = newWidth;
        setWidthTimer();
    }
    
    public void setWidthTimer(){
        widthTimer = System.nanoTime();
    }
    
    public void setX(int x) {
	this.x = x;
    }

    public double getX() {
	return x;
    }  
}
