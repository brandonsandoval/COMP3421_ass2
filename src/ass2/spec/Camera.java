/************************************
 *            PACKAGE               *
 ***********************************/
package ass2.spec;


/************************************
 *            IMPORTS               *
 ***********************************/

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

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
public class Camera {
    
    /************************************
     *            FIELDS                *
     ***********************************/
    // Static Fields
    private static final double[] START_ANGLE = {0,0,0};
    private static final double[] START_LOOK = {0,0,0};
    private static final double[] START_ORIEN = {0,0,0};
    private static final double START_FOV = 80;
    private static final double START_ZNEAR = 0.01;
    private static final double START_ZFAR = 300;

    // Camera Settings
    private static double[] cameraPos;
    private static double[] cameraAngle;
    private static double[] cameraLook;
    private static double[] cameraOrien;
    private static double fov;
    private static double zNear;
    private static double zFar;

    // Input Settings
    private static boolean mouseLock;
    private static double view_distance;
    
    // Mode Settings
    private static boolean thirdPerson;
    private static boolean collision;
    private static boolean gravity;
    
    private static Terrain myTerrain;

    // Constructor
    public Camera(Terrain myTerrain) {
        Camera.myTerrain = myTerrain;
        
        System.out.println("Setting up Camera");
        // Camera
        System.out.println(myTerrain.size().width+","+myTerrain.size().height);
        cameraPos = new double[3];
        cameraPos[Game.X] = myTerrain.size().width / 2;
        cameraPos[Game.Y] = 0;
        cameraPos[Game.Z] = myTerrain.size().height / 2;
        cameraAngle = START_ANGLE;
        cameraLook = START_LOOK;
        cameraOrien = START_ORIEN;
        fov = START_FOV;
        zNear = START_ZNEAR;
        zFar = START_ZFAR;

        mouseLock = false;
        view_distance = 3;
        gravity = true;
        collision = true;
        
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

    // Draw Cube
    public void draw(GL2 gl) {
        
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
    
    // Gets
    public static double[] getPos() {return cameraPos;}
    public static double[] getAngle() {return cameraAngle;}
    public static double[] getLook() {return cameraLook;}
    public static double[] getOrien() {return cameraOrien;}
    public static double getFov() {return fov;}
    public static double getZNear() {return zNear;}
    public static double getZFar() {return zFar;}
    public static boolean getThirdPerson() {return thirdPerson;}
    public static boolean getMouseLock() {return mouseLock;}
    public static boolean getGravity() {return gravity;}
    public static boolean getCollision() {return collision;}
    
    // Sets
    public static void setThirdPerson(boolean v) {thirdPerson = v;}
    public static void setMouseLock(boolean v) {mouseLock = v;}
    public static void setGravity(boolean v) {gravity = v;}
    public static void setCollision(boolean v) {collision = v;}
    
}