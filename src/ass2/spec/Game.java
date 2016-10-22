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

    // X Y Z
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
    // How high and far should the camera be
    private double CAMERA_HEIGHT = 3;
    private double CAMERA_DISTANCE = 4;
    
    // Movement speed: How fast the avatar/camera moves
    private double moveSpeed = 0.3;
    
    // Avatar
    private Avatar avatar;
    
    // Portal
    private Portal portal_in;
    private Portal portal_out;
    
    // Mouse and Keyboard listeners
    private MouseController myMouse;
    private KeyboardController myKeyboard;
    
    // An invisible cursor
    private BufferedImage cursorImg;
    private static Cursor blankCursor;
    
    // For FPS counter
    private long timing = 0;
    private int count = 0;
    
    // Other
    boolean wireframeMode;
    boolean drawCoord;

    
    /************************************
     *           CONSTRUCTOR            *
     ***********************************/
     public Game(Terrain terrain) {
         
        super("Assignment 2");
        
        this.myTerrain = terrain;
        this.myCamera = new Camera(terrain, CAMERA_HEIGHT, CAMERA_DISTANCE);
        
        this.myMouse = new MouseController();
        this.myKeyboard = new KeyboardController();
        
        // Setting Portal IDs
        if(this.myTerrain.hasPortals()) {
	        this.portal_in = this.myTerrain.getPortals()[0];
	        this.portal_out = this.myTerrain.getPortals()[1];
        }
        
        
        this.timing = 0;
        this.count = 0;
        
        // Set Cursor Invisible
        this.cursorImg = new BufferedImage(1,1, BufferedImage.TYPE_INT_ARGB);
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg
                , new Point(0,0), "blank cursor");
        
        // Other
        this.wireframeMode = false;
        this.drawCoord = false;
        
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

        // Camera Updates
        double[] pos = Camera.getPos();
        double[] angle = Camera.getAngle();

        // Camera uses EULER ANGLES
        // WSAD [MOVEMENT]
        if(KeyboardController.key(Keys.W)) {
            pos[Game.X] += moveSpeed * Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
            if(Camera.getThirdPerson()){
                pos[Game.Y] += moveSpeed * Math.sin(Math.toRadians(angle[Game.Z]));
            }else{
                pos[Game.Y] += moveSpeed * Math.sin(Math.toRadians(angle[Game.Z]+45));
            }
            pos[Game.Z] -= moveSpeed * Math.sin(Math.toRadians(angle[Game.Y]));
        }
        if(KeyboardController.key(Keys.S)) {
            pos[Game.X] -= moveSpeed * Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
            if(Camera.getThirdPerson()){
                pos[Game.Y] -= moveSpeed * Math.sin(Math.toRadians(angle[Game.Z]));
            }else{
                pos[Game.Y] -= moveSpeed * Math.sin(Math.toRadians(angle[Game.Z]+45));
            }
            pos[Game.Z] += moveSpeed * Math.sin(Math.toRadians(angle[Game.Y]));
        }
        if(KeyboardController.key(Keys.A)) {
            pos[Game.X] += moveSpeed * Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
            pos[Game.Z] -= moveSpeed * Math.sin(Math.toRadians(angle[Game.Y]+90));
        }
        if(KeyboardController.key(Keys.D)) {
            pos[Game.X] -= moveSpeed * Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
            pos[Game.Z] += moveSpeed * Math.sin(Math.toRadians(angle[Game.Y]+90));
        }
            
        // SPACE/CONTROL [ALTITUDE]
        if(KeyboardController.key(Keys.SPACE)) {
            pos[Game.Y] += 2;
        }
        if(KeyboardController.key(Keys.CONTROL)) {
            pos[Game.Y] -= 1;
        }

        // ARROW KEYS [ROTATE] - only used for testing
        if(KeyboardController.key(Keys.UP)) {
            angle[Game.Z] += 10;
        }
        if(KeyboardController.key(Keys.DOWN)) {
            angle[Game.Z] -= 10;
        }
        if(KeyboardController.key(Keys.LEFT)) {
            angle[Game.Y] += 10;
        }
        if(KeyboardController.key(Keys.RIGHT)) {
            angle[Game.Y] -= 10;
        }

        // OTHER TOGGLES
        // Mouse lock
        if(KeyboardController.keyToggle(Keys.T)) {
            Camera.setMouseLock(!Camera.getMouseLock());
        }
        // Toggle 1st/3rd person
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
        // Gravity and collision toggle
        if(KeyboardController.keyToggle(Keys.G)) {
            Camera.setGravity(!Camera.getGravity());
            System.out.println("GRAVITY:" + Camera.getGravity());
        }
        if(KeyboardController.keyToggle(Keys.C)) {
            Camera.setCollision(!Camera.getCollision());
            System.out.println("COLLISION:" + Camera.getCollision());
        }
        // Camera rotation lock toggle (in 3rd person)
        if(KeyboardController.keyToggle(Keys.R)) {
            avatar.setRotationLock(!avatar.getRotationLock());
        }
        // Speed boost toggle
        if(KeyboardController.keyToggle(Keys.X)) {
            if((moveSpeed == 0.3) || (moveSpeed == 0.1)) {
                moveSpeed = 1;
            }else if(moveSpeed == 1) {
                moveSpeed = 0.3;
            }
        }
        
        // COLLISION WITH END OF MAP
        if(pos[Game.X] < 0) {
            pos[Game.X] = 0;
        } else if(pos[Game.X] > myTerrain.size().getHeight()-1.01) {
            pos[Game.X] = myTerrain.size().getHeight()-1.01;
        }
        if(pos[Game.Z] < 0) {
            pos[Game.Z] = 0;
        } else if(pos[Game.Z] > myTerrain.size().getWidth()-1.01) {
            pos[Game.Z] = myTerrain.size().getWidth()-1.01;
        }
        //System.out.println("SIZE: " + myTerrain.size().getHeight());
        //System.out.println("X: " + pos[Game.X]);
        //System.out.println("Z: " + pos[Game.Z]);
        
        
        
        // Portal Update
        if(this.myTerrain.hasPortals()) {
	        double[] portalPosA = portal_in.getPos();
	        double[] portalPosB = portal_out.getPos();
	        
	        System.out.println("PORTAL A: " + portalPosA[Game.X] + " " + portalPosA[Game.Y] + " " + portalPosA[Game.Z]);
	        System.out.println("PORTAL B: " + portalPosB[Game.X] + " " + portalPosB[Game.Y] + " " + portalPosB[Game.Z]);
	        System.out.println("POS: " + pos[Game.X] + " " + pos[Game.Y] + " " + pos[Game.Z]);
	        
	        // If player enters portal A
	        if((portalPosA[Game.X] == Math.round(pos[Game.X]))
	                && (portalPosA[Game.Y] == Math.round(pos[Game.Y])
	                        ||    portalPosA[Game.Y] == Math.round(pos[Game.Y]+1))
	                && (portalPosA[Game.Z] == Math.round(pos[Game.Z]))) {
	            
	             double[] temp = portalPosB.clone();
	             temp[Game.X] += 1;
	             
	             Camera.setCameraPos(temp);
	             System.out.println("PORTAL A TO B");
	            
	        // If player enters portal B
	        } else if((portalPosB[Game.X] == Math.round(pos[Game.X]))
	                && (portalPosB[Game.Y] == Math.round(pos[Game.Y])
	                        ||    portalPosB[Game.Y] == Math.round(pos[Game.Y]+1))
	                && (portalPosB[Game.Z] == Math.round(pos[Game.Z]))) {
	            
	                double[] temp = portalPosA.clone();
	                temp[Game.X] += 1;
	                
	             Camera.setCameraPos(temp);
	             System.out.println("PORTAL B TO A");
	        }
	        
	        // Portal Animation
	        if(count % 5 == 0) {
	            portal_in.nextImg();
	            portal_out.nextImg();
	        }
        }
        count ++;
        
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
            avatar.drawAvatar(gl);
            myCamera.update();
        }

        // Light Setting
        LightProp lp = new LightProp();
        float[] s = myTerrain.getSunlight();
        lp.setProperties(0.1f, 0.5f, 0.1f, 0.2f);
        lp.setPositionAngle(s[0], s[1], s[2]);
        lp.setup(gl);
        
        // Draws X, Y, Z but only without Lights
        if(drawCoord) {
            drawCoor(gl, 128, 140, 128);
        }
        
        // Wireframe Mode
        if(wireframeMode) {
            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_LINE);
        }
        
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
        
        // Draw all enemies
        List<Enemy> enemies = myTerrain.enemies();
        for(Enemy enemy : enemies) {
            enemy.drawEnemy(gl);
        }
        
        // Draw Portals
        if(this.myTerrain.hasPortals()) {
	        portal_in.drawPortal(gl);
	        portal_out.drawPortal(gl);
        }
        // Needed for Debugging
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        
    }
    
    // Drawing X, Y, Z
    public void drawCoor(GL2 gl, double x, double y, double z) {
        double length = 12;
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
        gl.glLineWidth(2);
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
       
        // Load Tree Texture
        List<Tree> trees = myTerrain.trees();
        for(Tree tree : trees) {
            tree.loadTexture(gl, true);
        }
        
        // Load Road Texture
        List<Road> roads = myTerrain.roads();
        for(Road road : roads) {
            road.loadTexture(gl, true);
        }
        
        // Load Enemy Texture
        List<Enemy> enemies = myTerrain.enemies();
        for(Enemy enemy : enemies) {
            System.out.println(enemy.getPosition()[0]+" "+enemy.getPosition()[2]);
            enemy.loadTexture(gl, true);
        }

        // Setup Avatar
        avatar = new Avatar(CAMERA_HEIGHT);
        avatar.loadTexture(gl, true);
        
        // Load Portal Textures
        if(this.myTerrain.hasPortals()) {
	        portal_in.loadTexture(gl, true);
	        portal_out.loadTexture(gl, true);
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