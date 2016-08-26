
package breakout;

import java.awt.BorderLayout;
import javax.swing.JFrame;


public class BrickBreaker {
    
    public static final int WIDTH = 1280, HEIGHT = 720;
    
    public static void main(String[] args) throws Exception{
        
        
        //Frame
        JFrame theFrame = new JFrame("Breakout Game");
        theFrame.setLocation(0, 0);
        
        
        
        
        //Main playing area
        GamePanel thePanel = new GamePanel();
        theFrame.add(thePanel, BorderLayout.CENTER);
        theFrame.setSize(WIDTH, HEIGHT);
        theFrame.add(thePanel);
        theFrame.setResizable(false);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setVisible(true);
        thePanel.run();
    }
}
