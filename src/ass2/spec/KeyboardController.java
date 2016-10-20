package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {
    
    private static boolean[] keys = new boolean[Keys.values().length];
    private static boolean[] check = new boolean[Keys.values().length];
    
    public KeyboardController() {
        for(int i = 0; i < Keys.values().length; i++) {
            keys[i] = false;
        }
        for(int i = 0; i < Keys.values().length; i++) {
            check[i] = false;
        }
    }

    // True = key pressed, False = key released
    public static boolean key(Keys k) {
        return keys[k.ordinal()];
    }

    // True = key pressed then set any future calls to keyToggle to false
    // until key is released
    public static boolean keyToggle(Keys k) {
        if(check[k.ordinal()] == false && keys[k.ordinal()] == true){
            check[k.ordinal()] = true;
            keys[k.ordinal()] = false;
            return true;
        }else{
            return false;
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        keys[convertKeyEventtoKeys(e)] = true;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        keys[convertKeyEventtoKeys(e)] = false;
        check[convertKeyEventtoKeys(e)] = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static int convertKeyEventtoKeys(KeyEvent ke){
        if(ke.getKeyCode() == KeyEvent.VK_SPACE)
            return Keys.SPACE.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_SHIFT)
            return Keys.SHIFT.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_LEFT)
            return Keys.LEFT.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
            return Keys.RIGHT.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_UP)
            return Keys.UP.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_DOWN)
            return Keys.DOWN.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_W)
            return Keys.W.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_A)
            return Keys.A.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_S)
            return Keys.S.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_D)
            return Keys.D.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_T)
            return Keys.T.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_C)
            return Keys.C.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_G)
            return Keys.G.ordinal();
        if(ke.getKeyCode() == KeyEvent.VK_V)
            return Keys.V.ordinal();
        return 0;
    }
}
