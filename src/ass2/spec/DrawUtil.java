package ass2.spec;

import com.jogamp.opengl.GL2;

public class DrawUtil {
    /**
     * Draws a cube with dimensions w.h.d
     */
    public static void drawCube(GL2 gl, double w, double h, double d, 
        double[] texFront, double[] texBack, double[] texLeft,
        double[] texRight, double[] texTop, double[] texBottom) {
        
        //double[] texCoords = {0,0,1,1};
        
        gl.glBegin(GL2.GL_QUADS); {
            
            // FRONT FACE
            drawSquare(gl, w, h, texFront);
            // LEFT FACE
            gl.glPushMatrix(); {
                gl.glTranslated(0, 0, -d);
                gl.glRotated(-90, 0, 1, 0);
                drawSquare(gl, d, h, texLeft);
            } gl.glPopMatrix();
            // BACK FACE
            gl.glPushMatrix(); {
                gl.glTranslated(w, 0, -d);
                gl.glRotated(180, 0, 1, 0);
                drawSquare(gl, w, h, texBack);
            } gl.glPopMatrix();
            // RIGHT FACE
            gl.glPushMatrix(); {
                gl.glTranslated(w, 0, 0);
                gl.glRotated(90, 0, 1, 0);
                drawSquare(gl, d, h, texRight);
            } gl.glPopMatrix();
            // BOTTOM FACE
            gl.glPushMatrix(); {
                gl.glTranslated(0, 0, -d);
                gl.glRotated(90, 1, 0, 0);
                drawSquare(gl, w, d, texBottom);
            } gl.glPopMatrix();
            // TOP FACE
            gl.glPushMatrix(); {
                gl.glTranslated(0, h, 0);
                gl.glRotated(-90, 1, 0, 0);
                drawSquare(gl, w, d, texTop);
            } gl.glPopMatrix();
        }gl.glEnd();
        
    }
    /**
     * Draws a square with dimensions w.h
     * with usual perpendicular normals to the square
     * 
     * @param texCoords in the format {Ax, Ay, Bx, By}
     * where A is the start texture coord
     * and B is the end texture coord
     */
    public static void drawSquare(GL2 gl, double w, double h, double[] texCoords) {
        
        // Array indexes for texCoords param
        final int TEX_START_X = 0;
        final int TEX_START_Y = 1;
        final int TEX_END_X = 2;
        final int TEX_END_Y = 3;
        
        // Store normal and 4 points for face
        double n[];
        double p0[] = new double[3];
        double p1[] = new double[3];
        double p2[] = new double[3];
        double p3[] = new double[3];
        
        gl.glBegin(GL2.GL_QUADS); {
            p0[Game.X] = 0;
            p0[Game.Y] = 0;
            p0[Game.Z] = 0;
            
            p1[Game.X] = w;
            p1[Game.Y] = 0;
            p1[Game.Z] = 0;
            
            p2[Game.X] = w;
            p2[Game.Y] = h;
            p2[Game.Z] = 0;
            
            p3[Game.X] = 0;
            p3[Game.Y] = h;
            p3[Game.Z] = 0;
            
            n = MathUtil.normal(p0, p1, p2);
            gl.glNormal3dv(n, 0);
            gl.glTexCoord2d(texCoords[TEX_START_X], texCoords[TEX_START_Y]);
            gl.glVertex3dv(p0, 0);
            gl.glTexCoord2d(texCoords[TEX_END_X], texCoords[TEX_START_Y]);
            gl.glVertex3dv(p1, 0);
            gl.glTexCoord2d(texCoords[TEX_END_X], texCoords[TEX_END_Y]);
            gl.glVertex3dv(p2, 0);
            gl.glTexCoord2d(texCoords[TEX_START_X], texCoords[TEX_END_Y]);
            gl.glVertex3dv(p3, 0);
        }gl.glEnd();
    }
    
}
