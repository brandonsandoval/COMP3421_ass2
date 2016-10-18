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
    
    // Trunk/Cylinder of tree
    private double Xangle = -90;
    private double Yangle = 0;
    private double Zangle = 0;
    private static final int SLICES = 32;
    private String textureTree = "img/tree01.jpg";
    private String textureTreeExt = "jpg";
    private MyTexture myTreeTexture = null;
    
    // Head/Sphere of tree todo
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }

    /**
     * Initial setup for texturing
     * @param gl
     * @param mipmaps 
     */
    public void loadTexture(GL2 gl, boolean mipmaps){
        if(myTreeTexture == null) {
            myTreeTexture = new MyTexture(gl, textureTree, textureTreeExt, mipmaps);
        }
    }
    
    public void drawTree(GL2 gl) {

        GLUT glut = new GLUT();
        double[] t = getPosition();

        // DRAW THE HEAD OF TREE
        MaterialLightProp.treeHeadLightProp(gl);
        gl.glPushMatrix();
        gl.glTranslated(t[0], t[1]+treeHeight, t[2]);
        glut.glutSolidSphere(1, 16, 16);
        gl.glPopMatrix();
        
        // DRAW THE TRUNK OF TREE
        // Part of this code comes from week 8 in TexCylinder.java examples
        double angleIncrement = (Math.PI * 2.0) / SLICES;
        double top = t[1]+treeHeight;
        double bottom = t[1];
        double thickness = 0.3;
        
        MaterialLightProp.treeTrunkLightProp(gl);

        gl.glPushMatrix();
        {
            gl.glTranslated(t[0], 0, t[2]);
            gl.glRotated(Zangle, 0.0, 0.0, 1.0);
            gl.glRotated(Yangle, 0.0, 1.0, 0.0);
            gl.glRotated(Xangle, 1.0, 0.0, 0.0);
           //Draw the top of the trunk
            gl.glBegin(GL2.GL_POLYGON);{
           
            for(int i = 0; i < SLICES; i++)
            {
                double angle0 = i*angleIncrement;
            
                gl.glNormal3d(0.0, 0.0, 1);
                gl.glVertex3d(Math.cos(angle0)*thickness, Math.sin(angle0)*thickness, top);
            }
            }gl.glEnd();
          
            gl.glBindTexture(GL2.GL_TEXTURE_2D, myTreeTexture.getTextureId());
            gl.glBegin(GL2.GL_QUAD_STRIP);{      
                for(int i=0; i<= SLICES; i++){
                    double angle0 = i*angleIncrement;
                    double angle1 = (i+1)*angleIncrement;
                    double xPos0 = Math.cos(angle0)*thickness;
                    double yPos0 = Math.sin(angle0)*thickness;
                    double sCoord = 1.0/SLICES * i;
                    
                    gl.glNormal3d(xPos0, yPos0, 0);
                    gl.glTexCoord2d(sCoord,1);
                    gl.glVertex3d(xPos0,yPos0,top);
                    gl.glTexCoord2d(sCoord,0);
                    gl.glVertex3d(xPos0,yPos0,bottom);               
                    
                }
            }gl.glEnd();    
            
            //Draw the bottom of Trunk
            gl.glBegin(GL2.GL_POLYGON);{
               
                for(int i = 0; i < SLICES; i++)
                {
                    double angle0 = -i*angleIncrement;
                    gl.glNormal3d(0.0, 0.0, -1);
                    gl.glVertex3d(Math.cos(angle0)*thickness, Math.sin(angle0)*thickness,bottom);
                }
            }gl.glEnd();
        }gl.glPopMatrix();
        
    }
    
}