/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package breakout;

/**
 *
 * @author kobethuy
 */
public class Brick {
    
    private int health, powerUp;
    
    public Brick(int health) {
        this.health = health;
    }
    
    public int getHealth() {
        return this.health;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }
    
    public int getPowerUp() {
        return this.powerUp;
    }
    
    public void setPowerUp(int powerUp) {
        this.powerUp = powerUp;
    }
}
