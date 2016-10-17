/************************************
 *            PACKAGE               *
 ***********************************/
package ass2.spec;


/************************************
 *            IMPORTS               *
 ***********************************/
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;


/**************************************************
 *                  GAME                
 * 
 * @author malcolmr, BrandonSandoval, James Shin
 **************************************************/
public class Game extends JFrame implements GLEventListener {

    
    /************************************
     *             FIELDS               *
     ***********************************/
    private static final long serialVersionUID = 1L;

    // Misc
    public static final int X = 0;
    public static final int Y = 1;
    public static final int Z = 2;
    
    // JFrame / Panel Settings
    private static final int WIN_HEIGHT = 480;
    private static final int WIN_WIDTH = 480;
    private static final int FPS = 60;
    
    // Terrain
    private Terrain myTerrain;
    
    // Camera
    private Camera myCamera;
    
    // For FPS counter
    private long timing = 0;
    private int count = 0;
    
    // An invisible cursor
    BufferedImage cursorImg;
    Cursor blankCursor;
    
    
    /************************************
     *           CONSTRUCTOR            *
     ***********************************/
     public Game(Terrain terrain) {
        super("Assignment 2");
        
        this.myTerrain = terrain;
        this.myCamera = new Camera(terrain);
        
        this.timing = 0;
        this.count = 0;
        
        this.cursorImg = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        this.blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg
        		, new Point(0,0), "blank cursor");
        
     }
     
     
     /************************************
      *         METHOD (RUN)             *
      ***********************************/
    public void run() {
        // Init Profile
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create Panel and Add Listeners
        GLJPanel panel = new GLJPanel(caps);
        panel.addGLEventListener(this);
        panel.addKeyListener(myCamera);
        panel.addMouseMotionListener(myCamera);
        panel.setFocusable(true);
        
        // Add an animator to call 'display' at 60fps
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
        
        // Add Panel to this Frame
        getContentPane().add(panel);
        
        // Hide cursor
        hideCursor(true);
        
        // Frame Settings
        setSize(WIN_HEIGHT, WIN_WIDTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);     
        
    }
    
     /************************************
      *         METHOD (UPDATE)          *
      ***********************************/
    private void update(GL2 gl) {
        
        // To properly calculate FPS
        long time = System.currentTimeMillis();
        double fps = 1 / ((time - timing) / 1000.0);
        timing = time;
        
        // Prints every second
        if(count > fps) {
            System.out.println("FPS: " + fps);
            System.out.println("Pos: " + myCamera.getPos()[X] + " " + myCamera.getPos()[Y] + " " + myCamera.getPos()[Z]);
            System.out.println("Angle: " + myCamera.getAngle()[X] + " " + myCamera.getAngle()[Y] + " " + myCamera.getAngle()[Z]);
            System.out.println("Orie: " + myCamera.getOrien()[X] + " " + myCamera.getOrien()[Y] + " " + myCamera.getOrien()[Z]);
            System.out.println("Fov: " + myCamera.getFov() + " Near: " + myCamera.getZNear() + " Far: " + myCamera.getZFar());
            System.out.println();
            count = 0;
        }
        count++;
        
    }
    
    
     /************************************
      *         METHOD (RENDER)          *
      ***********************************/
    private void render(GL2 gl) {
        
        // Load ModelView Matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // Clear Screen and Buffer
        gl.glClearColor(0.5f, 0.75f, 1f, 1); // light-blue sky/background
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Setup Camera
        if(!Camera.thirdPerson) {
            gl.glRotated(90, 0, 1, 0);
            myCamera.setView(gl);
            myCamera.update();
        } else {
        	myCamera.setView(gl);
        	myCamera.draw(gl);
        	myCamera.update();
        }

        // Light Setting
        LightProp lp = new LightProp();
        float[] s = myTerrain.getSunlight();
        lp.setProperties(0.1f, 0.5f, 0.1f, 0.2f);
        lp.setPositionAngle(s[0], s[1], s[2]);
        lp.setup(gl);
        
        //	Draws X, Y, Z but only without Lights
        //drawCoor(gl);
        
        //	Wireframe Mode
        //gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        //gl.glColor3d(1,1,0);
        //gl.glLineWidth(1);
        myTerrain.drawTerrain(gl);
        
        // Draw all trees
        List<Tree> trees = myTerrain.trees();
        for(Tree tree : trees) {
            tree.drawTree(gl);
        }
        
        // Draw all roads
        List<Road> roads = myTerrain.roads();
        for(Road road : roads) {
            road.drawRoad(gl);
        }
        
        // Needed for Debugging
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        
    }
    
    //	Drawing X, Y, Z (COLORS ONLY works with NO LIGHTING)
    public void drawCoor(GL2 gl) {
    	//gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINES);
    	gl.glLineWidth(10);
    	gl.glBegin(GL2.GL_LINES); {
    		//	X (RED)
    		gl.glColor3d(1, 0, 0);
    		gl.glVertex3d(140,140,128);
    		gl.glVertex3d(128,140,128);
    	
    	} gl.glEnd();
    	
    	gl.glBegin(GL2.GL_LINES); {
    		//	Y (GREEN)
    		gl.glColor3d(0, 1, 0);
    		gl.glVertex3d(128,152,128);
    		gl.glVertex3d(128,140,128);
    	
    	} gl.glEnd();
    	
    	gl.glBegin(GL2.GL_LINES); {
    		//	Z (BLUE)
    		gl.glColor3d(0, 0, 1);
    		gl.glVertex3d(128,140,140);
    		gl.glVertex3d(128,140,128);
    	
    	} gl.glEnd();
    }

     
     /************************************
      *     METHOD (GLEVENTLISTENER)     *
      ***********************************/
    @Override
    public void display(GLAutoDrawable drawable) {
        
        GL2 gl = drawable.getGL().getGL2();
        
        // Update Game State
        update(gl);
        
        // Render Game
        render(gl);
        
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        
        GL2 gl = drawable.getGL().getGL2();
        
        // Set up gl with depth test
        gl.glEnable(GL2.GL_DEPTH_TEST);
        
        // Set up gl with lighting
        gl.glEnable(GL2.GL_LIGHTING);
        
        // Normalize vectors1
        gl.glEnable(GL2.GL_NORMALIZE);
        
        // Cull back faces
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        
        // Turn on texturing
        gl.glEnable(GL2.GL_TEXTURE_2D); 
        myTerrain.loadTerrain(gl);
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        
        GL2 gl = drawable.getGL().getGL2();
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        // Using an orthographic camera
        // L R B T N F
        // gl.glOrtho(-12, 12, -12, 12, -20, 20);
        
        // Using a perspective camera
        GLU glu = new GLU();
        
        //	Probably should change this later..
        myCamera.setSize(height, width);    // Need to give camera window size as argument

        glu.gluPerspective(myCamera.getFov(), (float)width/(float)height, myCamera.getZNear(), myCamera.getZFar());

    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {}

    
    public void hideCursor(boolean value) {
        if(value) {
            getContentPane().setCursor(blankCursor);
        }else {
            // Restore default cursor
            getContentPane().setCursor(null);
        }
    }
    
    /************************************
     *              MAIN        
     *
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     ***********************************/
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

}