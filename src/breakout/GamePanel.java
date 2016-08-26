
package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;



public class GamePanel extends JPanel implements Runnable{
    
    //Value
    private boolean running, start = false;
    private BufferedImage image;
    private Graphics2D g;
    private MyMouseMotionListener theMouseListener;
    private MyMouseListener theListener;
    private int mousex;
    
    //Entities
    private Ball theBall;
    private Paddle thePaddle;
    private Map theMap;
    private HUD theHud;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<BrickExplosion> brickExplosions;
    boolean started = false;
    
    //The game
    private Thread game;
    
    //Data structures to handle high scores
    private AtomicBoolean isPaused;
    
    //contructor
    public GamePanel() throws Exception{
        init();
    }
    
    private void init() throws Exception{
        isPaused = new AtomicBoolean(true);
        
        theMap = new Map(6, 10);        
        theHud = new HUD();       
        theMouseListener = new MyMouseMotionListener();
        theListener = new MyMouseListener();
        powerUps = new ArrayList<>();         
        brickExplosions = new ArrayList<>();        
        addMouseMotionListener(theMouseListener);
        addMouseListener(theListener);
        
        theBall = new Ball();        
        thePaddle = new Paddle(100, 20);       
        running = true;        
        image = new BufferedImage(BrickBreaker.WIDTH, BrickBreaker.HEIGHT, BufferedImage.TYPE_INT_RGB);       
        g = (Graphics2D) image.getGraphics();        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        game = new Thread(this);
	game.start();
	stop();
	isPaused.set(true);
    }
    
    @Override
    public void run(){
        //drawStart();
        //Loop
        
        while(running){
            //update
            update();
            //draw
            draw();
            //display
            repaint();
            
            try{
                Thread.sleep(15);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    //collosion between ball and paddle
    //ball and bricks
    public void checkCollisions(){
        Rectangle ballRect = theBall.getRect();
        Rectangle paddleRect = thePaddle.getRect();
        
        for (PowerUp powerUp : powerUps) {
            Rectangle puRect = powerUp.getRect();
            if (paddleRect.intersects(puRect)) {
                if (powerUp.getType() == PowerUp.WIDEPADDLE && !powerUp.getWasUsed()) {
                    thePaddle.setWidth(thePaddle.getWidth() * 2);
                    powerUp.setWasUsed(true);
                }
            }
        }
        
        if(ballRect.intersects(paddleRect) && theBall.getDY() > 0) {
            theBall.setDY(-theBall.getDY());
            playSound("../Resources/click.wav", 0);
            
            if(theBall.getX() < mousex + thePaddle.getWidth() / 4){
                theBall.setDX(theBall.getDX() - .5);
            }
            if(theBall.getX() < mousex + thePaddle.getWidth() && theBall.getX() > mousex + thePaddle.getWidth() / 4){
                theBall.setDX(theBall.getDX() + .5);
            }
        }
        
        
        A: for(int row = 0; row < theMap.getMapArray().length; row++){
            for(int col = 0; col < theMap.getMapArray()[0].length; col++){
                int brick = theMap.getMapArray()[row][col];
                
                if(brick > 0){
                    
                    int brickx = col * theMap.getBrickWidth() + theMap.HOR_PAD;
                    int bricky = row * theMap.getBrickHeight() + theMap.VERT_PAD;
                    int brickWidth = theMap.getBrickWidth();
                    int brickHeight = theMap.getBrickHeight();

                    Rectangle brickRect = new Rectangle(brickx, bricky, brickWidth, brickHeight); 

                    if(ballRect.intersects(brickRect)){
                        playSound("../Resources/glass.wav", 0);
                        
                        if(theMap.getMapArray()[row][col] == 1){
                            brickExplosions.add(new BrickExplosion(brickx, bricky, theMap));
                            
                        }
                        
                        if(theMap.getMapArray()[row][col] > 3){
                            powerUps.add(new PowerUp(brickx, bricky, theMap.getMapArray()[row][col], brickWidth, brickHeight));
                            theMap.setBrick(row, col, 3);
                        }else{
                            theMap.hitBrick(row, col);
                        }
                        theMap.hitBrick(row, col);
                        theBall.setDY(-theBall.getDY());
                        theHud.addScore(50);
                        
                        break A;
                    }
                }               
            }
        }
    }
    
   
    public void reset() {
        requestFocusInWindow();
    }
    
    //starts the thread
    public void start() {
	game.resume();
        isPaused.set(false);
    }

    //stops the thread
    public void stop() {
        game.suspend();
    }

    //ends the thread
    public void destroy() {
	game.resume();
	isPaused.set(false);
	game.stop();
	isPaused.set(true);
    }
    public void checkIfOut(){
        theBall.reset();		
	stop();
	isPaused.set(true);
    }
    public void update(){
        
        if (!start) {
            
            thePaddle.update();
            theBall.setX(thePaddle.getX() + ((thePaddle.getWidth() / 2) - theBall.getRect().getWidth() / 2));
            theBall.setY(BrickBreaker.HEIGHT - 70);
            
        } else {
        
            //checkIfOut();
            checkCollisions();
            thePaddle.update();
            theBall.update();
            //System.out.println(theBall.getDY());
            for(PowerUp pu : powerUps){
                pu.update();
            }
            for(int i = 0; i < brickExplosions.size(); i++){
                brickExplosions.get(i).update();
                if(!brickExplosions.get(i).getIsActive()){
                    brickExplosions.remove(i);
                }
            }
        }
    }
    public void draw(){
        //draw background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, BrickBreaker.WIDTH, BrickBreaker.HEIGHT);
        
        theBall.draw(g);
        thePaddle.draw(g);
        theMap.draw(g);
        theHud.draw(g);
        drawPowerUps();
        
        if(theMap.isThereAWin() == true){
            drawWin();
            running = false;
        }
        
        if(theBall.youLose()){
            drawLoser();
            running = false;
        }
        
        for(BrickExplosion bs : brickExplosions){
            bs.draw(g);
        }
         
        
    }
    public void drawWin(){
        int option = JOptionPane.showOptionDialog(this, "You won the game! Would you like to start playing from the beginning with the same score?", "A winner is you!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
        if (option == 0 || option == JOptionPane.CLOSED_OPTION) {
        } else if (option == 1) {
            System.exit(0);
        }
    }
    
    public void drawPowerUps(){
        for(PowerUp pu : powerUps){
            pu.draw(g);
        }
    }
    
    public void drawLoser(){
        int option = JOptionPane.showOptionDialog(this, "You lost the game! Would you like to continue?", "Lost the game!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
        if (option == 0) {
            reset();
        } else if (option == 1) {
            System.exit(0);
        } else if (option == JOptionPane.CLOSED_OPTION) {
            reset();
        }
    }
    //Start game screen
    public void drawStart(){
        for(int i = 100; i > 0; i--){
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, BrickBreaker.WIDTH, BrickBreaker.HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Courier New", Font.BOLD, i));
            g.drawString("Game Start", BrickBreaker.WIDTH / 2 - i * 3, BrickBreaker.HEIGHT / 2);
            repaint();
            try{
                Thread.sleep(30);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawImage(image, 0, 0, BrickBreaker.WIDTH, BrickBreaker.HEIGHT, null);
        
        g2.dispose();
    }
    //make the sound
    public void playSound(String soundFile, int times){
        try{
            URL soundLocation = getClass().getResource(soundFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundLocation);
            Clip clip = AudioSystem.getClip( );
            clip.open(audioInputStream);
            clip.loop(times);
            clip.start( );
        
        }catch(Exception ioe){
            System.out.println(ioe);        
        } 
    }

   
    //click left mouse to accelerate to start
    private class MyMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!start) {
                start = true;
                theBall.setDY(-5);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            
        }
    }
    //Control the paddle by mouse
    private class MyMouseMotionListener extends MouseAdapter{

        @Override
        public void mouseDragged(MouseEvent e) {
            int mouse = e.getButton();
            if(mouse == MouseEvent.MOUSE_DRAGGED){
                if (isPaused.get() == false) {
                    stop();
                    isPaused.set(true);
		}
		else {
                    start();
		}
                
            }
            if (mouse == MouseEvent.MOUSE_DRAGGED) {
		thePaddle.setX((int) (thePaddle.getX() - 50));
                
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mousex = e.getX();
            thePaddle.mouseMoved(e.getX());
        }      
    }
}
