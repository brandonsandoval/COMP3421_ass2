package ass2.spec;

import java.util.List;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr, BrandonSandoval
 */
public class Tree {

    private double[] myPos;
    private double treeHeight = 4;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }

    public void drawTree(GL2 gl) {

        GLUT glut = new GLUT();
        double[] t = getPosition();

        // Head of tree
        MaterialLightProp.treeHeadLightProp(gl);
        gl.glPushMatrix();
        gl.glTranslated(t[0], t[1]+treeHeight, t[2]);
        glut.glutSolidSphere(1, 16, 16);
        gl.glPopMatrix();

        // Trunk of tree
        MaterialLightProp.treeTrunkLightProp(gl);
        gl.glPushMatrix();
        gl.glTranslated(t[0], t[1], t[2]);
        gl.glRotated(-90, 1, 0, 0);
        glut.glutSolidCylinder(0.25, treeHeight, 32, 1);
        gl.glPopMatrix();
        
    }
    
}
