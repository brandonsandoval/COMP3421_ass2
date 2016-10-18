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
    private static final int SLICES_TREE = 32;
    private String textureTree = "img/tree01.jpg";
    private String textureTreeExt = "jpg";
    private MyTexture myTreeTexture = null;
    
    // Head/Sphere of tree
    private String textureLeaves = "img/leaves01.jpg";
    private String textureLeavesExt = "jpg";
    private MyTexture myLeavesTexture = null;
    private static final double RADIUS_LEAVES = 1.5;
    private static final int SLICES_LEAVES = 24;
    private static final int STACKS_LEAVES = 20;
    
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }

    
    public void normalize(double v[])  
    {  
        double d = Math.sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);  
        if (d != 0.0) 
        {  
           v[0]/=d; 
           v[1]/=d;  
           v[2]/=d;  
        }  
    }
    
    double r(double t){
        double x  = Math.cos(2 * Math.PI * t);
        return x;
    }
    
    double getY(double t){
        
        double y  = Math.sin(2 * Math.PI * t);
        return y;
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
        if(myLeavesTexture == null) {
            myLeavesTexture = new MyTexture(gl, textureLeaves, textureLeavesExt, mipmaps);
        }
    }
    
    public void drawTree(GL2 gl) {

        GLUT glut = new GLUT();
        double[] t = getPosition();

        // DRAW THE HEAD OF TREE
        // ! Part of this code was modified from RevSphereTex.java
        // ! from week 8 examples
        MaterialLightProp.treeHeadLightProp(gl);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myLeavesTexture.getTextureId());
        gl.glPushMatrix();
        {
            gl.glTranslated(t[0], t[1]+treeHeight, t[2]);
            double deltaT = 0.5/STACKS_LEAVES;
            int ang;  
            int delang = 360/SLICES_LEAVES;
            double x1,x2,z1,z2,y1,y2;
            for (int i = 0; i < STACKS_LEAVES; i++) { 
                double td = -0.25 + i*deltaT;
                
                gl.glBegin(GL2.GL_TRIANGLE_STRIP); 
                
                for(int j = 0; j <= SLICES_LEAVES; j++) {  
                    ang = j*delang;
                    x1 = RADIUS_LEAVES * r(td)*Math.cos((double)ang*2.0*Math.PI/360.0); 
                    x2 = RADIUS_LEAVES * r(td+deltaT)*Math.cos((double)ang*2.0*Math.PI/360.0); 
                    y1 = RADIUS_LEAVES * getY(td);
    
                    z1 = RADIUS_LEAVES * r(td)*Math.sin((double)ang*2.0*Math.PI/360.0);  
                    z2 = RADIUS_LEAVES * r(td+deltaT)*Math.sin((double)ang*2.0*Math.PI/360.0);  
                    y2 = RADIUS_LEAVES * getY(td+deltaT);
    
                    double normal[] = {x1,y1,z1};
                    normalize(normal);    
    
                    gl.glNormal3dv(normal,0);  
                    double tCoord = 1.0/STACKS_LEAVES * i; //Or * 2 to repeat label
                    double sCoord = 1.0/SLICES_LEAVES * j;
                    gl.glTexCoord2d(sCoord,tCoord);
                    gl.glVertex3d(x1,y1,z1);
                    normal[0] = x2;
                    normal[1] = y2;
                    normal[2] = z2;
    
                    normalize(normal);    
                    gl.glNormal3dv(normal,0); 
                    tCoord = 1.0/STACKS_LEAVES * (i+1); //Or * 2 to repeat label
                    gl.glTexCoord2d(sCoord,tCoord);
                    gl.glVertex3d(x2,y2,z2); 
    
                }; 
                gl.glEnd();  
            }
        }gl.glPopMatrix();
        
        // DRAW THE TRUNK OF TREE
        // ! Part of this code was modified from TexCylinder.java
        // ! from week 8 examples
        double angleIncrement = (Math.PI * 2.0) / SLICES_TREE;
        double top = t[1]+treeHeight;
        double bottom = t[1];
        double thickness = 0.3;
        
        MaterialLightProp.treeTrunkLightProp(gl);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTreeTexture.getTextureId());

        gl.glPushMatrix();
        {
            gl.glTranslated(t[0], 0, t[2]);
            gl.glRotated(Zangle, 0.0, 0.0, 1.0);
            gl.glRotated(Yangle, 0.0, 1.0, 0.0);
            gl.glRotated(Xangle, 1.0, 0.0, 0.0);
           //Draw the top of the trunk
            gl.glBegin(GL2.GL_POLYGON);{
           
            for(int i = 0; i < SLICES_TREE; i++)
            {
                double angle0 = i*angleIncrement;
            
                gl.glNormal3d(0.0, 0.0, 1);
                gl.glVertex3d(Math.cos(angle0)*thickness, Math.sin(angle0)*thickness, top);
            }
            }gl.glEnd();
          
            gl.glBegin(GL2.GL_QUAD_STRIP);{
                for(int i=0; i<= SLICES_TREE; i++){
                    double angle0 = i*angleIncrement;
                    double angle1 = (i+1)*angleIncrement;
                    double xPos0 = Math.cos(angle0)*thickness;
                    double yPos0 = Math.sin(angle0)*thickness;
                    double sCoord = 1.0/SLICES_TREE * i;
                    
                    gl.glNormal3d(xPos0, yPos0, 0);
                    gl.glTexCoord2d(sCoord,1);
                    gl.glVertex3d(xPos0,yPos0,top);
                    gl.glTexCoord2d(sCoord,0);
                    gl.glVertex3d(xPos0,yPos0,bottom);
                    
                }
            }gl.glEnd();
            
            //Draw the bottom of Trunk
            gl.glBegin(GL2.GL_POLYGON);{
               
                for(int i = 0; i < SLICES_TREE; i++)
                {
                    double angle0 = -i*angleIncrement;
                    gl.glNormal3d(0.0, 0.0, -1);
                    gl.glVertex3d(Math.cos(angle0)*thickness, Math.sin(angle0)*thickness,bottom);
                }
            }gl.glEnd();
        }gl.glPopMatrix();
        
    }
    
}