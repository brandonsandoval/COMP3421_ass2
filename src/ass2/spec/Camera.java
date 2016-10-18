/************************************
 *            PACKAGE               *
 ***********************************/
package ass2.spec;


/************************************
 *            IMPORTS               *
 ***********************************/
import java.awt.Point;
import java.awt.Robot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

// TODO: CLEAN CODE (will do later)

// W S A D
// Shift / Space (Altitude)
// Arrow Keys (Can use to rotate but should use mouse)
// T (Toggle Mouse Lock)
// V (Toggle Change View)
// C (Toggle Collision)
// G (Toggle Gravity)


/**************************************************
 *                  CAMERA                
 **************************************************/
public class Camera implements KeyListener, MouseListener, MouseMotionListener {
    
    /************************************
     *            FIELDS                *
     ***********************************/
    // Static Fields
    //private static final double[] START_POS = {0, 0, 0};
    private static final double[] START_POS = {128, 140, 128};
    private static final double[] START_ANGLE = {0,0,0};
    private static final double[] START_LOOK = {0,0,0};
    private static final double[] START_ORIEN = {0,0,0};
    private static final double START_FOV = 80;
    private static final double START_ZNEAR = 0.01;
    private static final double START_ZFAR = 300;

    // Do not change this value, ASCII[0-255]
    private static final int ASCII_SIZE = 256;
    private static final double MOUSE_SENSITIVITY = 0.1;

    // Camera Settings
    private double[] cameraPos;
    private double[] cameraAngle;
    private double[] cameraLook;
    private double[] cameraOrien;
    private double fov;
    private double zNear;
    private double zFar;

    // Input Settings
    private boolean[] input;
    private Robot mouseRobot;
    private double m_sensitivity;
    private boolean mouseLock;
    private double view_distance;
    
    // Mode Settings
    static public boolean thirdPerson;
    private boolean collision;
    private boolean gravity;
    
    private Terrain myTerrain;
    private int height;
    private int width;


    // Constructor
    public Camera(Terrain myTerrain) {
        
        System.out.println("Setting up Camera");

        // Camera
        this.cameraPos = START_POS;
        this.cameraAngle = START_ANGLE;
        this.cameraLook = START_LOOK;
        this.cameraOrien = START_ORIEN;
        this.fov = START_FOV;
        this.zNear = START_ZNEAR;
        this.zFar = START_ZFAR;

        // Mouse
        this.m_sensitivity = MOUSE_SENSITIVITY;
        
        this.input = new boolean[ASCII_SIZE];
        this.mouseLock = false;
        this.thirdPerson = false;
        this.view_distance = 3;
        this.myTerrain = myTerrain;
        this.gravity = true;
        this.collision = true;
        
        try {
            this.mouseRobot = new Robot();
            mouseRobot.setAutoDelay(16);
        } catch (Exception e) {}

    }
    
    public void setView(GL2 gl) {
        GLU glu = new GLU();
        
        if(thirdPerson) {
            glu.gluLookAt(cameraPos[Game.X] - Math.cos(Math.toRadians(cameraAngle[Game.Y])) * Math.cos(Math.toRadians(cameraAngle[Game.Z]))*view_distance,
                    cameraPos[Game.Y] - Math.sin(Math.toRadians(cameraAngle[Game.Z]))*view_distance,
                    cameraPos[Game.Z] + Math.sin(Math.toRadians(cameraAngle[Game.Y]))*view_distance,
                    
                    cameraPos[Game.X],
                    cameraPos[Game.Y],
                    cameraPos[Game.Z],
                    
                    0, 1, 0);
        } else {
            gl.glRotated(-cameraAngle[Game.Z], 0,0,1);
            gl.glRotated(-cameraAngle[Game.Y], 0,1,0);
            gl.glTranslated(-cameraPos[Game.X], -cameraPos[Game.Y], -cameraPos[Game.Z]);
        }
    }
    
    public void update() {
        if(collision) {
            double altitude = myTerrain.altitude(cameraPos[Game.X], cameraPos[Game.Z]);
            if(cameraPos[Game.Y] <= altitude) {
                cameraPos[Game.Y] = altitude+0.5;
            }
        }
        
        if(gravity) {
            cameraPos[Game.Y] = myTerrain.altitude(cameraPos[Game.X], cameraPos[Game.Z]) + 2;
        }
    
    }

    public void keyPressed(KeyEvent e) {
        
        input[e.getKeyCode()] = true;

        if(!thirdPerson) {
            // WSAD [MOVEMENT]
            if(input[KeyEvent.VK_W]) {
                cameraPos[Game.X] += Math.cos(Math.toRadians(cameraAngle[Game.Y])) * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Y] += Math.sin(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Z] -= Math.sin(Math.toRadians(cameraAngle[Game.Y]));
            }
            if(input[KeyEvent.VK_S]) {
                cameraPos[Game.X] -= Math.cos(Math.toRadians(cameraAngle[Game.Y])) * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Y] -= Math.sin(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Z] += Math.sin(Math.toRadians(cameraAngle[Game.Y]));
            }
            if(input[KeyEvent.VK_A]) {
                cameraPos[Game.X] += Math.cos(Math.toRadians(cameraAngle[Game.Y]+90));// * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Z] -= Math.sin(Math.toRadians(cameraAngle[Game.Y]+90));
            }
            if(input[KeyEvent.VK_D]) {
                cameraPos[Game.X] -= Math.cos(Math.toRadians(cameraAngle[Game.Y]+90));// * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Z] += Math.sin(Math.toRadians(cameraAngle[Game.Y]+90));
            }
        } else {
            if(input[KeyEvent.VK_W]) {
                cameraPos[Game.X] += Math.cos(Math.toRadians(cameraAngle[Game.Y])) * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Y] += Math.sin(Math.toRadians(cameraAngle[Game.Z]+45));
                cameraPos[Game.Z] -= Math.sin(Math.toRadians(cameraAngle[Game.Y]));
            }
            if(input[KeyEvent.VK_S]) {
                cameraPos[Game.X] -= Math.cos(Math.toRadians(cameraAngle[Game.Y])) * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Y] -= Math.sin(Math.toRadians(cameraAngle[Game.Z]+45));
                cameraPos[Game.Z] += Math.sin(Math.toRadians(cameraAngle[Game.Y]));
            }
            if(input[KeyEvent.VK_A]) {
                cameraPos[Game.X] += Math.cos(Math.toRadians(cameraAngle[Game.Y]+90));
                cameraPos[Game.Z] -= Math.sin(Math.toRadians(cameraAngle[Game.Y]+90));
            }
            if(input[KeyEvent.VK_D]) {
                cameraPos[Game.X] -= Math.cos(Math.toRadians(cameraAngle[Game.Y]+90));// * Math.cos(Math.toRadians(cameraAngle[Game.Z]));
                cameraPos[Game.Z] += Math.sin(Math.toRadians(cameraAngle[Game.Y]+90));
            }
        }
        // QE [ALTITUDE]
        if(input[KeyEvent.VK_SPACE]) {
            cameraPos[Game.Y] += 1;
        }
        if(input[KeyEvent.VK_SHIFT]) {
            cameraPos[Game.Y] -= 1;
        }

        // ARROW KEYS [ROTATE] - only used for testing
        if(input[KeyEvent.VK_UP]) {
            cameraAngle[Game.Z] += 1;
        }
        if(input[KeyEvent.VK_DOWN]) {
            cameraAngle[Game.Z] -= 1;
        }
        if(input[KeyEvent.VK_LEFT]) {
            cameraAngle[Game.Y] += 1;
        }
        if(input[KeyEvent.VK_RIGHT]) {
            cameraAngle[Game.Y] -= 1;
        }

        // OTHER
        if(input[KeyEvent.VK_T]) {
            mouseLock = !mouseLock;
        }
        if(input[KeyEvent.VK_V]) {
            thirdPerson = !thirdPerson;
            if(thirdPerson) {
                cameraAngle[Game.X] = 0;
                cameraAngle[Game.Y] = 0;
                cameraAngle[Game.Z] = -45;
            } else {
                cameraAngle[Game.X] = 0;
                cameraAngle[Game.Y] = 0;
                cameraAngle[Game.Z] = 0;
            }
        }
        if(input[KeyEvent.VK_G]) {
            gravity = !gravity;
            System.out.println("GRAVITY:" + gravity);
        }
        if(input[KeyEvent.VK_C]) {
            collision = !collision;
            System.out.println("COLLISION:" + collision);
        }

    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Something goes wrong here with error message but nothing actually happens..
        input[e.getKeyCode()] = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        Point mousePos = e.getPoint();

        if(((mousePos.x == width/2) && (mousePos.y == height/2))
                || (mouseLock == false)) {
            
        } else {
            mouseRobot.mouseMove(width/2 + (int)e.getComponent().getLocationOnScreen().getX(), 
                height/2 + (int)e.getComponent().getLocationOnScreen().getY());
            
            cameraAngle[Game.Y] += (width/2 - mousePos.x) * m_sensitivity;
            if(cameraAngle[Game.Z] > -90 && cameraAngle[Game.Z] < 90) {
                cameraAngle[Game.Z] += (height/2 - mousePos.y) * m_sensitivity;
            } else if(cameraAngle[Game.Z] > 90) {
                cameraAngle[Game.Z] = 89.9;
            } else if(cameraAngle[Game.Z] < -90) {
                cameraAngle[Game.Z] = -89.9;
            }
        }
        
    }

    public void setSize(int height, int width) {
        this.height = height;
        this.width = width;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    
    // Gets
    public double[] getPos() {return this.cameraPos;}
    public double[] getAngle() {return this.cameraAngle;}
    public double[] getLook() {return this.cameraLook;}
    public double[] getOrien() {return this.cameraOrien;}
    public double getFov() {return this.fov;}
    public double getZNear() {return this.zNear;}
    public double getZFar() {return this.zFar;}

    
    // Draw Cube
    public void draw(GL2 gl) {
        System.out.println("DRAWING...");
        
          gl.glBegin(GL2.GL_QUADS); {
            // front   
            gl.glColor3f(1, 0, 0);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]-1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]-1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]+1, cameraPos[Game.Z]+1);
            
            // back 
            gl.glColor3f(0, 0, 1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]-1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]+1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]-1, cameraPos[Game.Z]-1);
                    
            // top
            gl.glColor3f(0, 1, 0);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]+1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]+1, cameraPos[Game.Z]-1);

            // bottom
            gl.glColor3f(1, 1, 0);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]-1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]-1, cameraPos[Game.Z]+1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]-1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]-1, cameraPos[Game.Z]-1);
            
            //left
            gl.glColor3f(0, 1, 1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]-1, cameraPos[Game.Z]+11);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]-1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]+1, cameraPos[Game.Z]);
            gl.glVertex3d(cameraPos[Game.X]-1, cameraPos[Game.Y]+1, cameraPos[Game.Z]);
            
            //right
            gl.glColor3f(1, 0, 1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]-1);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]);
            gl.glVertex3d(cameraPos[Game.X]+1, cameraPos[Game.Y]+1, cameraPos[Game.Z]);
          } gl.glEnd();
    }
}