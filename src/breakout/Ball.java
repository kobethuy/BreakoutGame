
package breakout;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Toan
 */
public class Ball{
    
    
    //Value
    private double x, y;
    private double dx = 1, dy = -1;
    int ballSize = 20;
    private boolean onScreen;
    private final double BALL_X_START = 320;
    private final double BALL_Y_START = 360;
    
    public Ball(){ 

        this.ballSize = 20;
        
//        x = 200;
//        y = 200;
//        dx = 1;
//        dy = 3;
        
    }

    Ball(int i, int i0, int i1, int i2, int i3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public boolean youLose(){
        boolean loser = false;
        
        if(y > BrickBreaker.HEIGHT - ballSize * 2){
            loser = true;
        }
        return loser;
        
    }
    public void setOnScreen(boolean onScreen) {
	this.onScreen = onScreen;
    }
    
    public boolean isOnScreen() {
	return onScreen;
    }
}
