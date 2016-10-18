package ass2.spec;

import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseController implements MouseListener, MouseMotionListener {

    private static final double MOUSE_SENSITIVITY = 0.1;
    private static double m_sensitivity;
    private static Robot mouseRobot;
    private static int width;
    private static int height;
    
    public MouseController(){

        // Mouse
        m_sensitivity = MOUSE_SENSITIVITY;
        try {
            mouseRobot = new Robot();
            mouseRobot.setAutoDelay(16);
        } catch (Exception e) {}
        width = Game.getWIN_WIDTH();
        height = Game.getWIN_HEIGHT();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        
        Point mousePos = e.getPoint();

        if(((mousePos.x == width/2) && (mousePos.y == height/2))
                || (Camera.getMouseLock() == false)) {
            
        } else {
            mouseRobot.mouseMove(width/2 + (int)e.getComponent().getLocationOnScreen().getX(), 
                height/2 + (int)e.getComponent().getLocationOnScreen().getY());
            
            Camera.getAngle()[Game.Y] += (width/2 - mousePos.x) * m_sensitivity;
            
            // Prevent camera 'bobbing' past 90 deg
            if((Camera.getAngle()[Game.Z] + (height/2 - mousePos.y) * m_sensitivity) >= -90 && 
               (Camera.getAngle()[Game.Z] + (height/2 - mousePos.y) * m_sensitivity) <= 90) {
                Camera.getAngle()[Game.Z] += (height/2 - mousePos.y) * m_sensitivity;
            } else if(Camera.getAngle()[Game.Z] > 90) {
                Camera.getAngle()[Game.Z] = 89.9;
            } else if(Camera.getAngle()[Game.Z] < -90) {
                Camera.getAngle()[Game.Z] = -89.9;
            }
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

    // Sets
    public static void setSize(int height, int width) {
        MouseController.height = height;
        MouseController.width = width;
    }
}
