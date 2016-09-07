
package breakout;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;



public class GamePanel extends JPanel implements Runnable{
    
    //Value
    private boolean running, start = false;
    private BufferedImage image, bg;
    private Graphics2D g;
    private MyMouseMotionListener theMouseListener;
    private MyMouseListener theListener;
    private int mousex;
    
    //Entities
    private Ball ball;
    private Paddle thePaddle;
    private Map map;
    private HUD hud;
    private ArrayList<PowerUp> powerUps;
    private ArrayList<BrickExplosion> brickExplosions;
    private ArrayList<Ball> ballList;
    private boolean started = false;
    private Random ran;
    
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
        
        map = new Map(6, 10);        
        hud = new HUD();       
        theMouseListener = new MyMouseMotionListener();
        theListener = new MyMouseListener();
        powerUps = new ArrayList<>();
        ballList = new ArrayList<>();
        brickExplosions = new ArrayList<>();        
        addMouseMotionListener(theMouseListener);
        addMouseListener(theListener);
        
        ran = new Random();
        ball = new Ball();        
        thePaddle = new Paddle(100, 20);       
        running = true;        
        image = new BufferedImage(BrickBreaker.WIDTH, BrickBreaker.HEIGHT, BufferedImage.TYPE_INT_RGB);       
        g = (Graphics2D) image.getGraphics();        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        bg = ImageIO.read(new File(getClass().getResource("../Resources/img/background.jpg").toURI()));
        
        ballList.add(ball);
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
            try{
                Thread.sleep(15);
            }catch(Exception e){
            }
            //draw
            draw();
            //display
            repaint();
        }
    }
    
    //collosion between ball and paddle
    //ball and bricks
    public void checkCollisions(){
        ArrayList<Rectangle> ballRect = new ArrayList<>();
        
        for (Ball b : ballList)
            ballRect.add(b.getRect());
        
        Rectangle paddleRect = thePaddle.getRect();
        Iterator<PowerUp> p = powerUps.iterator();
        while (p.hasNext()) {
            PowerUp pu = p.next();
            Rectangle puRect = pu.getRect();
            
            if (paddleRect.intersects(puRect)) {
                switch (pu.getType()) {
                    
                    case 0:
                        //Paddle.reverseMouse = true;
                        break;
                    case 1:
                        if (!Paddle.wide) {
                            Paddle.wide = true;
                            thePaddle.setWidth(thePaddle.getWidth() * 2);
                        }
                        break;
                    case 2:
                        if (!Paddle.half) {
                            Paddle.half = true;
                            thePaddle.setWidth(thePaddle.getWidth() / 2);
                        }
                        break;
                    case 3:
                        ballList.add(new Ball(  ballList.get(0).getX(), 
                                                ballList.get(0).getY(), 
                                                -ballList.get(0).getDX(), 
                                                ballList.get(0).getDY()));
                        
                        break;
                }
                p.remove();
            }
            if (!pu.getIsOnScreen())
                p.remove();
        }
        
        for (int a = 0; a < ballRect.size(); a++) {
            if(ballRect.get(a).intersects(paddleRect) && ballList.get(a).getDY() > 0) {
                ballList.get(a).setDY(-ballList.get(a).getDY());
                playSound("../Resources/sound/click.wav", 0);

                if(ballList.get(a).getX() < mousex + thePaddle.getWidth() / 4){
                    ballList.get(a).setDX(ballList.get(a).getDX() - .5);
                }
                if( ballList.get(a).getX() < mousex + thePaddle.getWidth() && 
                    ballList.get(a).getX() > mousex + thePaddle.getWidth() / 4) {
                    ballList.get(a).setDX(ballList.get(a).getDX() + .5);
                }
            }
        
            A: for(int row = 0; row < map.getMapArray().length; row++) {
                for(int col = 0; col < map.getMapArray()[0].length; col++) {
                    int brick = map.getMapArray()[row][col].getHealth();

                    if(brick > 0){

                        int brickx = col * map.getBrickWidth() + map.HOR_PAD;
                        int bricky = row * map.getBrickHeight() + map.VERT_PAD;
                        int brickWidth = map.getBrickWidth();
                        int brickHeight = map.getBrickHeight();

                        Rectangle brickRect = new Rectangle(brickx, bricky, brickWidth, brickHeight); 

                        if(ballRect.get(a).intersects(brickRect)){
                            playSound("../Resources/sound/glass.wav", 0);

                            if(map.getMapArray()[row][col].getHealth() == 1){
                                brickExplosions.add(new BrickExplosion(brickx, bricky, map));  
                            }

                            if ((ran.nextInt(1) == 1) && (brick == 1)) {
                                powerUps.add(new PowerUp(brickx, bricky, ran.nextInt(4), brickWidth, brickHeight));
                                //powerUps.add(new PowerUp(brickx, bricky, 3, brickWidth, brickHeight));

                            }
                            
                            map.hitBrick(row, col);
                            ballList.get(a).setDY(-ballList.get(a).getDY());
                            hud.addScore(50);

                            break A;
                        }
                    }               
                }
            }
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawImage(image, 0, 0, BrickBreaker.WIDTH, BrickBreaker.HEIGHT, null);
        
        g2.dispose();
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
        ball.reset();		
	stop();
	isPaused.set(true);
    }
    
    public void update(){
        
        if (!start) {
            
            thePaddle.update();
            ball.setX(thePaddle.getX() + ((thePaddle.getWidth() / 2) - ball.getRect().getWidth() / 2));
            ball.setY(BrickBreaker.HEIGHT - 70);
            
        } else {
        
            //checkIfOut();
            checkCollisions();
            thePaddle.update();
            for (Ball b : ballList)
                b.update();

            for(int i = 0; i < brickExplosions.size(); i++){
                brickExplosions.get(i).update();
                if(!brickExplosions.get(i).getIsActive()){
                    brickExplosions.remove(i);
                }
            }
        }
    }
    
    public void draw() {
        //draw background
        
        try { 
            g.drawImage(bg, 0, 0, null);
        } catch (Exception ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, BrickBreaker.WIDTH, BrickBreaker.HEIGHT);
        */
        
        for (Ball b : ballList)
            b.draw(g);
        
        thePaddle.draw(g);
        map.draw(g);
        hud.draw(g);
        drawPowerUps();
        
        if(map.winCheck() == true){
            drawWin();
            running = false;
        }
        Iterator<Ball> iter = ballList.iterator();
        while (iter.hasNext()) {
            Ball b = iter.next();
            if (b.isLose() && ballList.size() == 1){
                drawLoser();
                running = false;
            } else if (b.isLose() && ballList.size() > 1) {
                iter.remove();
            }
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
            pu.update();
            pu.draw(g);
        }
    }
    
    public void drawLoser(){
        int option = JOptionPane.showOptionDialog(this, "You lost the game! Would you like to continue?", "Lost the game!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
        switch (option) {
            case 0:
                reset();
                break;
            case 1:
                System.exit(0);
            case JOptionPane.CLOSED_OPTION:
                reset();
                break;
            default:
                break;
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
                ball.setDY(-5);
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
            if (!Paddle.reverseMouse) {
                mousex = e.getX();
            } else {
                if (e.getX() < BrickBreaker.WIDTH) {
                    mousex = BrickBreaker.WIDTH - e.getX();
                } else {
                    mousex = e.getX() - BrickBreaker.WIDTH;
                }
            }
            thePaddle.mouseMoved(mousex);
        }      
    }
}
