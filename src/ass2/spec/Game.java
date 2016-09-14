package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileNotFoundException;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr, Brandon Sandoval
 */
public class Game extends JFrame implements GLEventListener, KeyListener {

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
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        // Rotate the camera around axis
        gl.glRotated (angleX, 1, 0, 0);
        gl.glRotated (angleY, 0, 1, 0);
        gl.glRotated (angleZ, 0, 0, 1);
        
        // 4 points of a square (from x,z to x+1,z+1 with altitude y)
        double p0[] = {0, 0, 0};
        double p1[] = {0, 0, 0};
        double p2[] = {0, 0, 0};
        double p3[] = {0, 0, 0};
        
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
                gl.glBegin(GL2.GL_TRIANGLES);{
                  
                    // Green triangle
                    gl.glColor3f(0f,0f,0.7f);
                    gl.glVertex3dv(p0,0);
                    gl.glVertex3dv(p1,0);
                    gl.glVertex3dv(p2,0);
                    // Dark-green triangle
                    gl.glColor3f(0f,0f,0.6f);
                    gl.glVertex3dv(p2,0);
                    gl.glVertex3dv(p1,0);
                    gl.glVertex3dv(p3,0);
                    
                }gl.glEnd();
            }
        }
    
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // TODO Auto-generated method stub
    
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        
        // Set up gl with depth test
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
        int height) {
        
        GL2 gl = drawable.getGL().getGL2();
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        // Using an orthographic camera
        // L R B T N F
        gl.glOrtho(-12, 12, -12, 12, -20, 20);
        
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
