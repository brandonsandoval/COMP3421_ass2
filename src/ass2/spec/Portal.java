package ass2.spec;

import com.jogamp.opengl.GL2;

public class Portal {
    
    double[] pos;
    boolean in;
    int portalID;
    
    private MyTexture myPortalTextureA = null;
    private MyTexture myPortalTextureB = null;
    private String texturePortal = "img/portal01.jpg";
    private String textureExt = "jpg";
    
    double row;
    double col;
    double inc;
    double n;
    double m;
    
    public Portal(int portalID) {
        this.pos = new double[3];
        this.in = false;
        this.portalID = portalID;
        
        this.row = 4;
        this.col = 4;

        this.n = 1;
        this.m = 1;
    }
    
    public void setPos(double[] pos) {
        this.pos[Game.X] = pos[Game.X];
        this.pos[Game.Y] = pos[Game.Y];
        this.pos[Game.Z] = pos[Game.Z];
    }
    
    public double[] getPos() {
        return this.pos;
    }
    
    public void drawPortal(GL2 gl) {
        
        gl.glPushMatrix();
        gl.glDisable(GL2.GL_CULL_FACE);
        gl.glTranslated(pos[Game.X], pos[Game.Y], pos[Game.Z]);
        gl.glRotated(Camera.getAngle()[Game.Y]-90, 0, 1, 0);
        
        MaterialLightProp.portalLightPropA(gl);
        
        if(portalID == 0) {
            MaterialLightProp.portalLightPropA(gl);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, myPortalTextureA.getTextureId());
        } else {
            MaterialLightProp.portalLightPropB(gl);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, myPortalTextureB.getTextureId());
        }
        
        
        gl.glBegin(GL2.GL_TRIANGLE_FAN); {
            gl.glTexCoord2d(0.5,0.5);
            gl.glVertex3d(0,0,0);
            
            int inc = 360/30;
            int angle = 0;
            for(int i = 0; i < 31; i++) {
                gl.glTexCoord2d((1/col)* (n - 0.5) + Math.cos(Math.toRadians(angle))/8
                        , ((1/row)* (m - 0.5) + Math.sin(Math.toRadians(angle))/8));
                
                gl.glVertex3d(Math.cos(Math.toRadians(angle)), (Math.sin(Math.toRadians(angle)))*2, 0);
                angle += inc;
            }
        }gl.glEnd();
        
        gl.glEnable(GL2.GL_CULL_FACE);
        
        gl.glPopMatrix();
    }
    
    public void loadTexture(GL2 gl, boolean mipmaps){
        if(myPortalTextureA == null) {
            myPortalTextureA = new MyTexture(gl, texturePortal, textureExt, mipmaps);
        }
        if(myPortalTextureB == null) {
            myPortalTextureB = new MyTexture(gl, texturePortal, textureExt, mipmaps);
        }
    }
    
    public void nextImg() {
        System.out.println("n: " + n + " m: " + m); 
        if(n > col) {
            n = 1;
            m++;
        } else {
            n++;
        }
        if(m > row) {
            m = 1;
            n = 1;
        }
    }
    
}
