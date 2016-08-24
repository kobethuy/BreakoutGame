
package breakout;

import java.awt.BorderLayout;
import javax.swing.JFrame;


public class BrickBreaker {
    
    public static final int WIDTH = 640, HEIGHT = 480;
    
    public static void main(String[] args){
        
        
        //Frame
        JFrame theFrame = new JFrame("Breakout Game");
        theFrame.setLocation(500, 200);
         
        //Main playing area
        GamePanel thePanel = new GamePanel();
        theFrame.add(thePanel, BorderLayout.CENTER);
          
       
        theFrame.setResizable(false);
        theFrame.setSize(WIDTH, HEIGHT);
        theFrame.add(thePanel);
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        theFrame.setVisible(true);
        
        
        thePanel.run();
    }
}
