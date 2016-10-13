/************************************
 *            PACKAGE               *
 ***********************************/
package ass2.spec;


/************************************
 *            IMPORTS               *
 ***********************************/
import java.io.File;
import java.io.FileNotFoundException;

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
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    
    // JFrame / Panel Settings
    private static final int WIN_HEIGHT = 480;
    private static final int WIN_WIDTH = 480;
    private static final int FPS = 60;
    
    // Terrain
    private Terrain myTerrain;
    
    // Camera
    private Camera myCamera;

    // Graphics
    private boolean wireframeMode;
    private int count;
    
    /************************************
     *           CONSTRUCTOR            *
     ***********************************/
     public Game(Terrain terrain) {
        super("Assignment 2");
        
        this.myTerrain = terrain;
        this.myCamera = new Camera();
        this.wireframeMode = true;
        this.count = 0;             // just needed temporaily
     }
     
     
     /************************************
      *         METHOD (RUN)             *
      ***********************************/
    public void run() {
        
        // Init Profile
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        
        // Create Panel and Add Listeners
        GLJPanel panel = new GLJPanel();
        panel.addGLEventListener(this);
        panel.addKeyListener(myCamera);
        panel.addMouseMotionListener(myCamera);
        //panel.addMouseListener(MouseController);
        //panel.addMouseMotionListener(MouseMotionController);
        //panel.addMouseWheelListener(MouseWheelController);
        panel.setFocusable(true);
        
        // Add an animator to call 'display' at 60fps
        FPSAnimator animator = new FPSAnimator(FPS);
        animator.add(panel);
        animator.start();
        
        // Add Panel to this Frame
        getContentPane().add(panel);
        
        // Frame Settings
        setSize(WIN_HEIGHT, WIN_WIDTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);     
        
    }
    
     /************************************
      *         METHOD (UPDATE)             *
      ***********************************/
    private void update(GL2 gl) {
        
        // Prints every second
        if(count % 60 == 0) {
            System.out.println("Pos: " + myCamera.getPos()[X] + " " + myCamera.getPos()[Y] + " " + myCamera.getPos()[Z]);
            System.out.println("Angle: " + myCamera.getAngle()[X] + " " + myCamera.getAngle()[Y] + " " + myCamera.getAngle()[Z]);
            System.out.println("Orie: " + myCamera.getOrien()[X] + " " + myCamera.getOrien()[Y] + " " + myCamera.getOrien()[Z]);
            System.out.println("Fov: " + myCamera.getFov() + " Near: " + myCamera.getZNear() + " Far: " + myCamera.getZFar());
            System.out.println();
        }
        
        myCamera.update();
        
        
        count++;
    }
    
    
     /************************************
      *         METHOD (RENDER)             *
      ***********************************/
    private void render(GL2 gl) {
        
        // Load ModelView Matrix
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        // Clear Screen and Buffer
        gl.glClearColor(0, 0, 0, 1);        //    Black
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        




        // Manually Set Camera
        /*GLU glu = new GLU();
        // Position the camera for viewing.
        glu.gluLookAt(1, 20, 0, 0.0, 0, 0.0, 0.0, 1.0, 0.0);*/

        // Setup Camera
        myCamera.setView(gl);
        
        // Light Setting
        /*
        LightProp lp = new LightProp();
        float[] s = myTerrain.getSunlight();
        lp.setProperties(0.1f, 0.5f, 0.1f, 0.2f);
        lp.setPositionAngle(s[0], s[1], s[2]);
        lp.setup(gl);
        */


        if(wireframeMode) {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
            gl.glColor4f(1, 0, 0, 1);
        }
        
        // Draw Terrain
        myTerrain.drawTerrain(gl);
        
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glColor4f(0, 0, 0, 1);
        myTerrain.drawTerrain(gl);
        
        /*        
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
        */
    
        // Needed for Debugging
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
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
        //gl.glEnable(GL2.GL_LIGHTING);
        
        // Normalize vectors1
        //gl.glEnable(GL2.GL_NORMALIZE);
        
        // Cull back faces
        gl.glEnable(GL2.GL_CULL_FACE);
        //gl.glCullFace(GL2.GL_BACK);
        
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
        myCamera.setSize(height, width);    // Need to give camera window size as argument


        glu.gluPerspective(myCamera.getFov(), (float)width/(float)height, myCamera.getZNear(), myCamera.getZFar());

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

}