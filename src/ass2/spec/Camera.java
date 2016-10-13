package ass2.spec;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera implements KeyListener, MouseListener, MouseMotionListener {
    
    // X Y Z
    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;
    
    private static final double[] START_POS = { 128, 150, 128 };
    private static final double[] START_ANGLE = { 0, 0, 0 };
    private static final double[] START_LOOK = { 0, 0, 0, };
    private static final double[] START_ORIEN = { 0, 0, 0, };
    
    private static final double FOV = 80;
    private static final double ZNEAR = 1;
    private static final double ZFAR = 300;
    
    private double[] cameraPos = START_POS;
    private double[] cameraAngle = START_ANGLE;
    private double[] cameraLookat = START_LOOK;
    private double[] cameraOrientation = START_ORIEN;
    
    private double[] d_cameraPos;
    private double[] d_cameraAngle;
    
    private double fov = FOV;
    private double zNear = ZNEAR;
    private double zFar = ZFAR;
    
    private double d_fov = 0;
    private double d_zNear = 0;
    private double d_zFar = 0;
    private boolean mouseLock = true;
    
    // Mouse settings
    private double m_sensitivity = 2.25;
    private double m_yaw = 0.022;
    private double m_pitch = 0.022;
    
    private int height;
    private int width;
    private Robot robot;
    
    public Camera() {
        System.out.println("Setting up Camera...");
        
        this.d_cameraPos = new double[3];
        this.d_cameraAngle = new double[3];
        
        System.out.println("Pos: " + cameraPos[X] + " " + cameraPos[Y] + " " + cameraPos[Z]);
        System.out.println("Angle: " + cameraAngle[X] + " " + cameraAngle[Y] + " " + cameraAngle[Z]);
        System.out.println("Orientation: " + cameraOrientation[X] + " " + cameraOrientation[Y] + " " + cameraOrientation[Z]);
        System.out.println("FOV: " + fov);
        System.out.println("zNear: " + zNear + " zFar: " + zFar);
        
        try {
            this.robot = new Robot();
        } catch (Exception e) {}
        
    }
    
    public void setView(GL2 gl) {
        
        GLU glu = new GLU();
        
        // Position the camera for viewing - manually
/*        System.out.println(Math.atan(cameraAngle[X]));
        System.out.println(Math.atan(cameraAngle[Y]));
        glu.gluLookAt(cameraPos[X], cameraPos[Y], cameraPos[Z], 
                        0, 0, 0,
                        0, 0, 0);*/
        
        //gl.glScaled(1 / (myScale * myAspect), 1 / myScale, 1);
        
        gl.glRotated (-cameraAngle[X], 1, 0, 0);
        gl.glRotated (-cameraAngle[Y], 0, 1, 0);
        gl.glTranslated(-cameraPos[X], -cameraPos[Y], -cameraPos[Z]);
    }
    
    public void update() {
        
        cameraPos[X] += d_cameraPos[X];
        cameraPos[Y] += d_cameraPos[Y];
        cameraPos[Z] += d_cameraPos[Z];
        
        if(cameraAngle[X] < -90) {
            cameraAngle[X] = -90;
        } else if (cameraAngle[X] > 90){
            cameraAngle[X] = 90;
        } else {
            cameraAngle[X] += d_cameraAngle[X];
        }
        
        cameraAngle[Y] += d_cameraAngle[Y];
        
        //cameraAngle[Z] += d_cameraAngle[Z];
        
        fov += d_fov;
        zNear += d_zNear;
        zFar += d_zFar;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {

            // Camera Movement
            case KeyEvent.VK_A:
                d_cameraPos[X] = 0.1;
                break;
            case KeyEvent.VK_D:
                d_cameraPos[X] = -0.1;
                break;
            case KeyEvent.VK_W:
                d_cameraPos[Z] = 0.1;
                break;
            case KeyEvent.VK_S:
                d_cameraPos[Z] = -0.1;
                break;
            case KeyEvent.VK_Q:
                d_cameraPos[Y] = 0.1;
                break;
            case KeyEvent.VK_E:
                d_cameraPos[Y] = -0.1;
                break;
            
            // Camera Angle
            case KeyEvent.VK_UP:
                d_cameraAngle[X] = 1;
                break;
            case KeyEvent.VK_DOWN:
                d_cameraAngle[X] = -1;
                break;
            case KeyEvent.VK_LEFT:
                if (e.isShiftDown()) {
                    d_cameraAngle[Z] = -1;
                } else {
                    d_cameraAngle[Y] = -1;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (e.isShiftDown()) {
                    d_cameraAngle[Z] = 1;
                } else {
                    d_cameraAngle[Y] = 1;
                }
                break;
                
            // Perspective 
            case KeyEvent.VK_F:
                if (e.isShiftDown()) {
                    d_fov = -0.1;
                } else {
                    d_fov = 0.1;
                }
                break;
            case KeyEvent.VK_R:
                if (e.isShiftDown()) {
                    d_zFar = 0.1;
                } else {
                    d_zFar = -0.1;
                }
                break;
            case KeyEvent.VK_T:
                if (e.isShiftDown()) {
                    d_zNear = -0.1;
                } else {
                    d_zNear = 0.1;
                }
                break;
            
            // Camera Orientation (alternate 0 or 1)
            case KeyEvent.VK_NUMPAD1:
                cameraOrientation[X] = 1 - cameraOrientation[X];
                break;
            case KeyEvent.VK_NUMPAD2:
                cameraOrientation[Y] = 1- cameraOrientation[Y];
                break;
            case KeyEvent.VK_NUMPAD3:
                cameraOrientation[Z] = 1- cameraOrientation[Z];
                break;
            case KeyEvent.VK_SPACE:
                mouseLock = !mouseLock;
            break;
            
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {

            // Camera Movement
            case KeyEvent.VK_A:
            case KeyEvent.VK_D:
                d_cameraPos[X] = 0;
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_S:
                d_cameraPos[Z] = 0;
                break;
            case KeyEvent.VK_Q:
            case KeyEvent.VK_E:
                d_cameraPos[Y] = 0;
                break;
            
            // Camera Angle
            case KeyEvent.VK_UP:
                d_cameraAngle[X] = 0;
                break;
            case KeyEvent.VK_DOWN:
                d_cameraAngle[X] = 0;
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
                d_cameraAngle[Z] = 0;
                d_cameraAngle[Y] = 0;
                break;
         
            // Perspective 
            case KeyEvent.VK_F:
                d_fov = 0;
                break;
            case KeyEvent.VK_R:
                d_zFar = 0;
                break;
            case KeyEvent.VK_T:
                d_zNear = 0;
                break;
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
        Point mousePos = e.getPoint();
        /*
        System.out.println("W: " + width + " H: " + height);
        System.out.println("W-cent: " + width/2 + " H: " + height/2);
        System.out.println("W-mouse: " + mousePos.x + " H2: " + mousePos.y);
        */
        if(((mousePos.x == width/2) && (mousePos.y == height/2)) || mouseLock == true) {
            
            //System.out.println("CAMERA LOCKED");
            d_cameraAngle[X] = 0;
            d_cameraAngle[Y] = 0;
            d_cameraAngle[Z] = 0;
            
        } else {
            robot.mouseMove(
                width/2 + (int)e.getComponent().getLocationOnScreen().getX(), 
                height/2 + (int)e.getComponent().getLocationOnScreen().getY());
            d_cameraAngle[Y] = (width/2 - mousePos.x) * m_yaw * m_sensitivity;
            d_cameraAngle[X] = (height/2 - mousePos.y) * m_pitch * m_sensitivity;
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    public double[] getPos() { return cameraPos; }
    public double[] getAngle() { return cameraAngle; }
    public double[] getOrien() { return cameraOrientation; }
    
    public double getFov() { return fov; }
    public double getZNear() { return zNear; }
    public double getZFar() { return zFar; }

    public void setSize(int height, int width) {
        System.out.println("YAY" + height + " " + width);
        this.height = height;
        this.width = width;
    }

}
