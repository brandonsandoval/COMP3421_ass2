package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr, BrandonSandoval
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private Terrain myTerrain;
    
    private static int angleX = 0;
    private static int angleY = 0;
    private static int angleZ = 0;
    
    public Game(Terrain terrain) {
        super("Assignment 2");
            myTerrain = terrain;
   
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLJPanel panel = new GLJPanel();
        panel.addGLEventListener(this);
        panel.addKeyListener(this);
        
        // Add an animator to call 'display' at 60fps
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
  
        getContentPane().add(panel);
        setSize(800, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
          
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glClearColor(0.9f, 0.9f, 1.0f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        GLU glu = new GLU();
        // Position the camera for viewing.
        glu.gluLookAt(-10.0, 5, -10.0, 0.0, 2.0, 0.0, 0.0, 1.0, 0.0);
        
        // Rotate the camera around axis
        gl.glRotated (angleX, 1, 0, 0);
        gl.glRotated (angleY, 0, 1, 0);
        gl.glRotated (angleZ, 0, 0, 1);
        
        // Lighting
        LightProp lp = new LightProp();
        float[] s = myTerrain.getSunlight();
        lp.setProperties(0.1f, 0.5f, 0.1f, 0.2f);
        lp.setPositionAngle(s[0], s[1], s[2]);
        lp.setup(gl);
        
        drawTerrain(gl);
        drawTrees(gl);
        drawRoads(gl);
    
    }
    
    /**
     * Draws the terrain
     * @param gl
     */
    public void drawTerrain(GL2 gl) {
        // 4 points of a square (from x,z to x+1,z+1 with altitude y)
        double p0[] = {0, 0, 0};
        double p1[] = {0, 0, 0};
        double p2[] = {0, 0, 0};
        double p3[] = {0, 0, 0};

        MaterialLightProp.terrainLightProp(gl);
        // loop over the altitude grid in myTerrain
        for(int z = 0; z < myTerrain.size().height -1; z++) {
            for(int x = 0; x < myTerrain.size().width -1; x++) {
            
                p0[0] = x;
                p0[1] = myTerrain.getGridAltitude(x  , z  );
                p0[2] = z;
                
                p1[0] = x;
                p1[1] = myTerrain.getGridAltitude(x  , z+1);
                p1[2] = z+1;
                
                p2[0] = x+1;
                p2[1] = myTerrain.getGridAltitude(x+1, z  );
                p2[2] = z;
                
                p3[0] = x+1;
                p3[1] = myTerrain.getGridAltitude(x+1, z+1);
                p3[2] = z+1;
              
                // The two triangles represents 1 grid square
                double[] n;
                gl.glBegin(GL2.GL_TRIANGLES);{
                    n = MathUtil.normal(p0, p1, p2);
                    gl.glNormal3d(n[0], n[1], n[2]);
                    gl.glVertex3dv(p0,0);
                    gl.glVertex3dv(p1,0);
                    gl.glVertex3dv(p2,0);

                    n = MathUtil.normal(p2, p1, p3);
                    gl.glNormal3d(n[0], n[1], n[2]);
                    gl.glVertex3dv(p2,0);
                    gl.glVertex3dv(p1,0);
                    gl.glVertex3dv(p3,0);
                    
                }gl.glEnd();
            }
        }
    }
    
    /**
     * Tree drawn as a rectangle at the moment
     * TODO We need to fix Terrain.altitude() to draw them at there correct heights
     * @param gl
     */
    public void drawTrees(GL2 gl) {

        GLUT glut = new GLUT();
        double[] t = {0,0,0};
        double height = 5;
        List<Tree> trees = myTerrain.trees();
        
        for(Tree tree : trees) {
            t = tree.getPosition();

            // Head of tree
            MaterialLightProp.treeHeadLightProp(gl);
            gl.glPushMatrix();
            gl.glTranslated(t[0], t[1]+height, t[2]);
            glut.glutSolidSphere(1, 16, 16);
            gl.glPopMatrix();

            // Trunk of tree
            MaterialLightProp.treeTrunkLightProp(gl);
            gl.glPushMatrix();
            gl.glTranslated(t[0], t[1], t[2]);
            gl.glRotated(-90, 1, 0, 0);
            glut.glutSolidCylinder(0.25, height, 32, 1);
            gl.glPopMatrix();
        }
    }
    
    /**
     * 
     * @param gl
     */
    public void drawRoads(GL2 gl) {
        // TODO draw Roads
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
    
    }
    
    @Override
    public void init(GLAutoDrawable drawable) {
        
        GL2 gl = drawable.getGL().getGL2();
        // Set up gl with depth test
        gl.glEnable(GL2.GL_DEPTH_TEST);
        // Set up gl with lighting
        gl.glEnable(GL2.GL_LIGHTING);
        // Normalize vectors
        gl.glEnable(GL2.GL_NORMALIZE);
        // Cull back faces
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glCullFace(GL2.GL_BACK);
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
        int height) {
        
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        // Using an orthographic camera
        // L R B T N F
        // gl.glOrtho(-12, 12, -12, 12, -20, 20);
        
        // Using a perspective camera
        GLU glu = new GLU();
        glu.gluPerspective(60.0, (float)width/(float)height, 1.0, 50.0);
        
    }

    // Debugging, use keyboard numbers 1-6 to rotate around the axis
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
            case KeyEvent.VK_NUMPAD1:
                angleX = (angleX + 10) % 360;
                break;
            case KeyEvent.VK_2:
            case KeyEvent.VK_NUMPAD2:
                angleX = (angleX - 10) % 360;
                break;
                
            case KeyEvent.VK_3:
            case KeyEvent.VK_NUMPAD3:
                angleY = (angleY + 10) % 360;
                break;
            case KeyEvent.VK_4:
            case KeyEvent.VK_NUMPAD4:
                angleY = (angleY - 10) % 360;
                break;
                
            case KeyEvent.VK_5:
            case KeyEvent.VK_NUMPAD5:
                angleZ = (angleZ + 10) % 360;
                break;
            case KeyEvent.VK_6:
            case KeyEvent.VK_NUMPAD6:
                angleZ = (angleZ - 10) % 360;
                break;
                
            default:
              break;
        }
        System.out.println("Rotation: X="+angleX+", Y="+angleY+", Z="+angleZ);
    }
    
    @Override
    public void keyReleased(KeyEvent arg0) {
        // TODO Auto-generated method stub
    
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
    
    }
}
