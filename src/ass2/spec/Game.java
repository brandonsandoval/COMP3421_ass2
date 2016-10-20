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
import java.awt.event.KeyEvent;
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
    private static final int WIN_WIDTH = 640;
    private static final int FPS = 60;
    
    // Terrain
    private Terrain myTerrain;
    
    // Camera
    private Camera myCamera;
    
    // Mouse and Keyboard listeners
    private MouseController myMouse;
    private KeyboardController myKeyboard;
    
    // For FPS counter
    private long timing = 0;
    private int count = 0;
    
    // An invisible cursor
    BufferedImage cursorImg;
    static Cursor blankCursor;
    
    
    /************************************
     *           CONSTRUCTOR            *
     ***********************************/
     public Game(Terrain terrain) {
        super("Assignment 2");
        
        this.myTerrain = terrain;
        this.myCamera = new Camera(terrain);
        
        this.myMouse = new MouseController();
        this.myKeyboard = new KeyboardController();
        
        this.timing = 0;
        this.count = 0;
        
        this.cursorImg = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg
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
        panel.addKeyListener(myKeyboard);
        panel.addMouseListener(myMouse);
        panel.addMouseMotionListener(myMouse);
        panel.setFocusable(true);
        
        // Add an animator to call 'display' at 60fps
        FPSAnimator animator = new FPSAnimator(FPS);
        animator.add(panel);
        animator.start();
        
        // Add Panel to this Frame
        getContentPane().add(panel);
        
        // Hide cursor
        getContentPane().setCursor(blankCursor);
        
        // Frame Settings
        setSize(WIN_WIDTH, WIN_HEIGHT);
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
            System.out.println("Pos: " + Camera.getPos()[X] + " " + Camera.getPos()[Y] + " " + Camera.getPos()[Z]);
            System.out.println("Angle: " + Camera.getAngle()[X] + " " + Camera.getAngle()[Y] + " " + Camera.getAngle()[Z]);
            System.out.println("Orie: " + Camera.getOrien()[X] + " " + Camera.getOrien()[Y] + " " + Camera.getOrien()[Z]);
            System.out.println("Fov: " + Camera.getFov() + " Near: " + Camera.getZNear() + " Far: " + Camera.getZFar());
            System.out.println();
            count = 0;
        }
        count++;
        
        double[] pos = Camera.getPos();
        double[] angle = Camera.getAngle();

        //if(!Camera.getThirdPerson()) {
            // WSAD [MOVEMENT]
            if(KeyboardController.key(Keys.W)) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                if(Camera.getThirdPerson()){
                    pos[Game.Y] += Math.sin(Math.toRadians(angle[Game.Z]));
                }else{
                    pos[Game.Y] += Math.sin(Math.toRadians(angle[Game.Z]+45));
                }
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(KeyboardController.key(Keys.S)) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                if(Camera.getThirdPerson()){
                    pos[Game.Y] -= Math.sin(Math.toRadians(angle[Game.Z]));
                }else{
                    pos[Game.Y] -= Math.sin(Math.toRadians(angle[Game.Z]+45));
                }
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(KeyboardController.key(Keys.A)) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
            if(KeyboardController.key(Keys.D)) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]+90));
            }/*
        } else {
            if(KeyboardController.key(Keys.W)) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Y] += Math.sin(Math.toRadians(angle[Game.Z]+45));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(KeyboardController.key(Keys.S)) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Y] -= Math.sin(Math.toRadians(angle[Game.Z]+45));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(KeyboardController.key(Keys.A)) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y]+90));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
            if(KeyboardController.key(Keys.D)) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
        }*/
        // QE [ALTITUDE]
        if(KeyboardController.key(Keys.SPACE)) {
            pos[Game.Y] += 100;
        }
        if(KeyboardController.key(Keys.SHIFT)) {
            pos[Game.Y] -= 1;
        }

        // ARROW KEYS [ROTATE] - only used for testing
        if(KeyboardController.key(Keys.UP)) {
            angle[Game.Z] += 1;
        }
        if(KeyboardController.key(Keys.DOWN)) {
            angle[Game.Z] -= 1;
        }
        if(KeyboardController.key(Keys.LEFT)) {
            angle[Game.Y] += 1;
        }
        if(KeyboardController.key(Keys.RIGHT)) {
            angle[Game.Y] -= 1;
        }

        // OTHER
        if(KeyboardController.keyToggle(Keys.T)) {
            Camera.setMouseLock(!Camera.getMouseLock());
        }
        if(KeyboardController.keyToggle(Keys.V)) {
            Camera.setThirdPerson(!Camera.getThirdPerson());
            if(Camera.getThirdPerson()) {
                angle[Game.X] = 0;
                angle[Game.Y] = 0;
                angle[Game.Z] = -45;
            } else {
                angle[Game.X] = 0;
                angle[Game.Y] = 0;
                angle[Game.Z] = 0;
            }
        }
        if(KeyboardController.keyToggle(Keys.G)) {
            Camera.setGravity(!Camera.getGravity());
            System.out.println("GRAVITY:" + Camera.getGravity());
        }
        if(KeyboardController.keyToggle(Keys.C)) {
            Camera.setCollision(!Camera.getCollision());
            System.out.println("COLLISION:" + Camera.getCollision());
        }
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
        if(!Camera.getThirdPerson()) {
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
        
        // Draws X, Y, Z but only without Lights
        drawCoor(gl, 128, 140, 128);
        
        // Wireframe Mode
        //gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        //gl.glColor3d(1,1,0);
        //gl.glLineWidth(1);
        myTerrain.drawVBOTerrain(gl);
        //myTerrain.drawTerrain(gl);
        
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
    
    // Drawing X, Y, Z
    public void drawCoor(GL2 gl, double x, double y, double z) {
    	double length = 12;
        //gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINES);
        gl.glLineWidth(10);
        gl.glBegin(GL2.GL_LINES); {
            // X (RED)
        	MaterialLightProp.redLightProp(gl);
            gl.glVertex3d(x+length, y, z);
            gl.glVertex3d(x, y, z);
        
        } gl.glEnd();
        
        gl.glBegin(GL2.GL_LINES); {
            // Y (GREEN)
        	MaterialLightProp.greenLightProp(gl);
            gl.glVertex3d(x, y+length, z);
            gl.glVertex3d(x, y, z);
        
        } gl.glEnd();
        
        gl.glBegin(GL2.GL_LINES); {
            // Z (BLUE)
        	MaterialLightProp.blueLightProp(gl);
            gl.glVertex3d(x, y, z+length);
            gl.glVertex3d(x, y, z);
        
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
        myTerrain.loadTerrain(gl, true);
        List<Tree> trees = myTerrain.trees();
        for(Tree tree : trees) {
            tree.loadTexture(gl, true);
        }
        
        // Setup terrain VBO
        myTerrain.setupVBO(gl);
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
        
        // Probably should change this later..
        MouseController.setSize(height, width); // Need to give mouseController window size

        glu.gluPerspective(Camera.getFov(), (float)width/(float)height, Camera.getZNear(), Camera.getZFar());

    }
    
    @Override
    public void dispose(GLAutoDrawable drawable) {}

    
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

    // Gets
    public static int getWIN_WIDTH(){ return WIN_WIDTH; }
    public static int getWIN_HEIGHT(){ return WIN_HEIGHT; }

}