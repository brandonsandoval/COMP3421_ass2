package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {

    // Do not change this value, ASCII[0-255]
    private static final int ASCII_SIZE = 256;
    
    private boolean[] input;
    
    public KeyboardController(){
        this.input = new boolean[ASCII_SIZE];
    }
    
    public void keyPressed(KeyEvent e) {

        double[] pos = Camera.getPos();
        double[] angle = Camera.getAngle();
        input[e.getKeyCode()] = true;

        if(!Camera.getThirdPerson()) {
            // WSAD [MOVEMENT]
            if(input[KeyEvent.VK_W]) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Y] += Math.sin(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(input[KeyEvent.VK_S]) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Y] -= Math.sin(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(input[KeyEvent.VK_A]) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
            if(input[KeyEvent.VK_D]) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
        } else {
            if(input[KeyEvent.VK_W]) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Y] += Math.sin(Math.toRadians(angle[Game.Z]+45));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(input[KeyEvent.VK_S]) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y])) * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Y] -= Math.sin(Math.toRadians(angle[Game.Z]+45));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]));
            }
            if(input[KeyEvent.VK_A]) {
                pos[Game.X] += Math.cos(Math.toRadians(angle[Game.Y]+90));
                pos[Game.Z] -= Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
            if(input[KeyEvent.VK_D]) {
                pos[Game.X] -= Math.cos(Math.toRadians(angle[Game.Y]+90));// * Math.cos(Math.toRadians(angle[Game.Z]));
                pos[Game.Z] += Math.sin(Math.toRadians(angle[Game.Y]+90));
            }
        }
        // QE [ALTITUDE]
        if(input[KeyEvent.VK_SPACE]) {
            pos[Game.Y] += 1;
        }
        if(input[KeyEvent.VK_SHIFT]) {
            pos[Game.Y] -= 1;
        }

        // ARROW KEYS [ROTATE] - only used for testing
        if(input[KeyEvent.VK_UP]) {
            angle[Game.Z] += 1;
        }
        if(input[KeyEvent.VK_DOWN]) {
            angle[Game.Z] -= 1;
        }
        if(input[KeyEvent.VK_LEFT]) {
            angle[Game.Y] += 1;
        }
        if(input[KeyEvent.VK_RIGHT]) {
            angle[Game.Y] -= 1;
        }

        // OTHER
        if(input[KeyEvent.VK_T]) {
            Camera.setMouseLock(!Camera.getMouseLock());
        }
        if(input[KeyEvent.VK_V]) {
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
        if(input[KeyEvent.VK_G]) {
            Camera.setGravity(!Camera.getGravity());
            System.out.println("GRAVITY:" + Camera.getGravity());
        }
        if(input[KeyEvent.VK_C]) {
            Camera.setCollision(!Camera.getCollision());
            System.out.println("COLLISION:" + Camera.getCollision());
        }

    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        // Something goes wrong here with error message but nothing actually happens..
        input[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

}
