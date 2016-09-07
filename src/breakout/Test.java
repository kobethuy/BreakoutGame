package breakout;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseMotionListener;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Test extends Thread {
    private boolean isRunning = true;
    private final Canvas canvas;
    private BufferStrategy strategy;
    private final BufferedImage background;
    private Graphics2D backgroundGraphics;
    private Graphics2D graphics;
    private final JFrame frame;
    private final int width = 1281, height = 720, scale = 1;
    private Map map;
    private Paddle paddle;
    private int mousex;
    private MyMouseMotionListener listener;
    private final GraphicsConfiguration config =
    		GraphicsEnvironment.getLocalGraphicsEnvironment()
    			.getDefaultScreenDevice()
    			.getDefaultConfiguration();

    // create a hardware accelerated image
    public final BufferedImage create(final int width, final int height,
    		final boolean alpha) {
    	return config.createCompatibleImage(width, height, alpha
    			? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    // Setup
    public Test() {
    	// JFrame
    	frame = new JFrame();
    	frame.addWindowListener(new FrameClose());
    	frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    	frame.setSize(width * scale, height * scale);
    	frame.setVisible(true);

    	// Canvas
    	canvas = new Canvas(config);
    	canvas.setSize(width * scale, height * scale);
    	frame.add(canvas, 0);

    	// Background & Buffer
    	background = create(width, height, false);
    	canvas.createBufferStrategy(2);
    	do {
    		strategy = canvas.getBufferStrategy();
    	} while (strategy == null);
        
        try {
            map = new Map(6, 10);
            paddle = new Paddle(100, 20);
            listener = new MyMouseMotionListener();
            addMouseMotionListener(listener);
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    	start();
    }

    private class FrameClose extends WindowAdapter {
    	@Override
    	public void windowClosing(final WindowEvent e) {
    		isRunning = false;
    	}
    }

    // Screen and buffer stuff
    private Graphics2D getBuffer() {
    	if (graphics == null) {
    		try {
    			graphics = (Graphics2D) strategy.getDrawGraphics();
    		} catch (IllegalStateException e) {
    			return null;
    		}
    	}
    	return graphics;
    }

    private boolean updateScreen() {
    	graphics.dispose();
    	graphics = null;
    	try {
    		strategy.show();
    		Toolkit.getDefaultToolkit().sync();
    		return (!strategy.contentsLost());

    	} catch (NullPointerException | IllegalStateException e) {
    		return true;
    	}
    }

    @Override
    public void run() {
    	backgroundGraphics = (Graphics2D) background.getGraphics();
    	long fpsWait = (long) (1.0 / 30 * 1000);
    	main: while (isRunning) {
    		long renderStart = System.nanoTime();
    		updateGame();

    		// Update Graphics
    		do {
    			Graphics2D bg = getBuffer();
    			if (!isRunning) {
    				break main;
    			}
    			renderGame(backgroundGraphics); // this calls your draw method
    			// thingy
    			if (scale != 1) {
    				bg.drawImage(background, 0, 0, width * scale, height
    						* scale, 0, 0, width, height, null);
    			} else {
    				bg.drawImage(background, 0, 0, null);
    			}
    			bg.dispose();
    		} while (!updateScreen());
                /*
    		// Better do some FPS limiting here
    		long renderTime = (System.nanoTime() - renderStart) / 1000000;
    		try {
    			Thread.sleep(Math.max(0, fpsWait - renderTime));
    		} catch (InterruptedException e) {
    			Thread.interrupted();
    			break;
    		}
    		renderTime = (System.nanoTime() - renderStart) / 1000000;
                */
    	}
    	frame.dispose();
    }

    public void updateGame() {
    	paddle.update();
    }

    public void renderGame(Graphics2D g) {
    	//map.draw(g);
        paddle.draw(g);
    }
    
    private class MyMouseMotionListener extends MouseAdapter{

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            /*
            if (!Paddle.reverseMouse) {
                mousex = e.getX();
            } else {
                if (e.getX() < BrickBreaker.WIDTH) {
                    mousex = BrickBreaker.WIDTH - e.getX();
                } else {
                    mousex = e.getX() - BrickBreaker.WIDTH;
                }
            }*/
            mousex = e.getX();
            paddle.mouseMoved(mousex);
        }      
    }
}