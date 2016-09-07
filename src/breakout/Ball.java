
package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Toan
 */
public class Ball{
    
    
    //Value
    private double x, y;
    private double dx = 1, dy = -1;
    private int ballSize = 20;
    private boolean onScreen;
    private final double BALL_X_START = BrickBreaker.WIDTH / 2 - ballSize / 2;
    private final double BALL_Y_START = BrickBreaker.HEIGHT - 70;
    
    public Ball(){ 
        this.ballSize = 20;    
    }
    
    public Ball(double x, double y, double dx, double dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    public void update(){
        setPosition();
    }
    
    //Resets the ball to original position at center of screen
    public void reset() {
        x = BALL_X_START;
	y = BALL_Y_START;
	dx = 1;
	dy = -1;
    }
    
    public void setPosition(){
        setX(getX() + getDX());
        setY(getY() + getDY());
        
        if((getX() < 0) || (this.x > BrickBreaker.WIDTH - ballSize)){
            setDX(-dx);
        }
        
        if((getY() < 0) || (this.y > BrickBreaker.HEIGHT - ballSize)){
            setDY(-dy);
        }
    }
    public void draw(Graphics2D g){
        /*
        try {
            URL bg = getClass().getResource("../Resources/img/ball.png");
            g.drawImage(ImageIO.read(bg), (int) x, (int) y, null);
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        g.setColor(Color.DARK_GRAY);
        g.setStroke(new BasicStroke(4));
        g.fillOval((int)x, (int)y, ballSize, ballSize);
        
    }
    
    public Rectangle getRect(){
        return new Rectangle((int)x, (int)y, ballSize, ballSize);
    }
    
    public double getDY(){
        return this.dy;
    }
    public void setDX(double dx){
        this.dx = dx;
    }
    
    public void setDY(double dy){
        this.dy = dy;
    }
    
    public double getDX(){
        return this.dx;
    }
    public double getX(){
        return this.x;
    }
    public double getY() {
	return this.y;
    }
    
    public void setX(double x){
        this.x = x;
    }
    public void setY(double y) {
	this.y = y;
    }
    
    public boolean isLose(){
        boolean lose = false;
        
        if(y > BrickBreaker.HEIGHT - ballSize * 2){
            lose = true;
        }
        return lose;
        
    }
    public void setOnScreen(boolean onScreen) {
	this.onScreen = onScreen;
    }
    
    public boolean isOnScreen() {
	return onScreen;
    }
}
