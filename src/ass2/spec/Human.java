package ass2.spec;

import com.jogamp.opengl.GL2;

// Contains the human body parts. Both Avatar and Enemy will extend this
public class Human {

    // Dimensions of body parts
    final static double LEG_WIDTH = 4;
    final static double LEG_HEIGHT = 12;
    final static double LEG_DEPTH = 4;
    final static double ARM_WIDTH = 4;
    final static double ARM_HEIGHT = 12;
    final static double ARM_DEPTH = 4;
    final static double BODY_WIDTH = 8;
    final static double BODY_HEIGHT = 12;
    final static double BODY_DEPTH = 4;
    final static double HEAD_WIDTH = 8;
    final static double HEAD_HEIGHT = 8;
    final static double HEAD_DEPTH = 8;
    
    // texture file is composed of a 16x8 grid (Width by Height)
    final static double TEX_W = 16;
    final static double TEX_H = 8;
    
    // The starting and ending texture coords (x,y) for each surface on avatar
    // Format: {startX, startY, endX, endY}
    final static double[] TEX_LEG_FRONT_L = {2/TEX_W, 0/TEX_H, 1/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_FRONT_R = {1/TEX_W, 0/TEX_H, 2/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_BACK_L =  {4/TEX_W, 0/TEX_H, 3/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_BACK_R =  {3/TEX_W, 0/TEX_H, 4/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_INSIDE_L ={3/TEX_W, 0/TEX_H, 2/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_INSIDE_R ={2/TEX_W, 0/TEX_H, 3/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_OUTSIDE_L={1/TEX_W, 0/TEX_H, 0/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_OUTSIDE_R={0/TEX_W, 0/TEX_H, 1/TEX_W, 3/TEX_H};
    final static double[] TEX_LEG_TOP_L =   {2/TEX_W, 3/TEX_H, 1/TEX_W, 4/TEX_H};
    final static double[] TEX_LEG_TOP_R =   {1/TEX_W, 3/TEX_H, 2/TEX_W, 4/TEX_H};
    final static double[] TEX_LEG_BOTTOM_L ={2/TEX_W, 3/TEX_H, 3/TEX_W, 4/TEX_H};
    final static double[] TEX_LEG_BOTTOM_R ={3/TEX_W, 3/TEX_H, 2/TEX_W, 4/TEX_H};
    
    final static double[] TEX_ARM_FRONT_L = {12/TEX_W, 0/TEX_H, 11/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_FRONT_R = {11/TEX_W, 0/TEX_H, 12/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_BACK_L =  {14/TEX_W, 0/TEX_H, 13/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_BACK_R =  {13/TEX_W, 0/TEX_H, 14/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_INSIDE_L ={13/TEX_W, 0/TEX_H, 12/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_INSIDE_R ={12/TEX_W, 0/TEX_H, 13/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_OUTSIDE_L={11/TEX_W, 0/TEX_H, 10/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_OUTSIDE_R={10/TEX_W, 0/TEX_H, 11/TEX_W, 3/TEX_H};
    final static double[] TEX_ARM_TOP_L =   {12/TEX_W, 3/TEX_H, 11/TEX_W, 4/TEX_H};
    final static double[] TEX_ARM_TOP_R =   {11/TEX_W, 3/TEX_H, 12/TEX_W, 4/TEX_H};
    final static double[] TEX_ARM_BOTTOM_L ={12/TEX_W, 3/TEX_H, 13/TEX_W, 4/TEX_H};
    final static double[] TEX_ARM_BOTTOM_R ={13/TEX_W, 3/TEX_H, 12/TEX_W, 4/TEX_H};

    final static double[] TEX_BODY_FRONT ={ 5/TEX_W, 0/TEX_H,  7/TEX_W, 3/TEX_H};
    final static double[] TEX_BODY_BACK = { 8/TEX_W, 0/TEX_H, 10/TEX_W, 3/TEX_H};
    final static double[] TEX_BODY_LEFT = { 7/TEX_W, 0/TEX_H,  8/TEX_W, 3/TEX_H};
    final static double[] TEX_BODY_RIGHT ={ 4/TEX_W, 0/TEX_H,  5/TEX_W, 3/TEX_H};
    final static double[] TEX_BODY_TOP =  { 5/TEX_W, 4/TEX_H,  7/TEX_W, 3/TEX_H};
    final static double[] TEX_BODY_BOTTOM={ 7/TEX_W, 3/TEX_H,  9/TEX_W, 4/TEX_H};
    
    final static double[] TEX_HEAD_FRONT ={ 2/TEX_W, 4/TEX_H,  4/TEX_W, 6/TEX_H};
    final static double[] TEX_HEAD_BACK = { 6/TEX_W, 4/TEX_H,  8/TEX_W, 6/TEX_H};
    final static double[] TEX_HEAD_LEFT = { 4/TEX_W, 4/TEX_H,  6/TEX_W, 6/TEX_H};
    final static double[] TEX_HEAD_RIGHT ={ 0/TEX_W, 4/TEX_H,  2/TEX_W, 6/TEX_H};
    final static double[] TEX_HEAD_TOP =  { 2/TEX_W, 6/TEX_H,  4/TEX_W, 8/TEX_H};
    final static double[] TEX_HEAD_BOTTOM={ 4/TEX_W, 6/TEX_H,  6/TEX_W, 8/TEX_H};
    
    // Size and misc
    final static double SCALE_FACTOR = 0.1;

    // Texturing
    String texture;
    String textureExt;
    MyTexture myTexture = null;
    
    /**
     * Initial setup for texturing
     * @param gl
     * @param mipmaps 
     */
    public void loadTexture(GL2 gl, boolean mipmaps){
        if(myTexture == null) {
            myTexture = new MyTexture(gl, texture, textureExt, mipmaps);
        }
    }
    
    public void leftLeg(GL2 gl) {
        gl.glPushMatrix(); {
            // Rotate leg 10 deg around correct pivot
            gl.glTranslated(0, LEG_HEIGHT, 0);
            gl.glRotated(-10, 0, 0, 1);
            gl.glTranslated(0, -LEG_HEIGHT, 0);
            DrawUtil.drawCube(gl, LEG_WIDTH, LEG_HEIGHT, LEG_DEPTH, 
                TEX_LEG_BACK_L, TEX_LEG_FRONT_L, TEX_LEG_OUTSIDE_L,
                TEX_LEG_INSIDE_L, TEX_LEG_TOP_L, TEX_LEG_BOTTOM_L);
        }gl.glPopMatrix();
    }
    
    public void rightLeg(GL2 gl) {
        gl.glPushMatrix(); {
            // Move to correct position
            gl.glTranslated(LEG_WIDTH, 0, 0);
            // Rotate Leg 10 deg around correct pivot
            gl.glTranslated(0, LEG_HEIGHT, 0);
            gl.glTranslated(LEG_WIDTH, 0, 0);
            gl.glRotated(10, 0, 0, 1);
            gl.glTranslated(-LEG_WIDTH, 0, 0);
            gl.glTranslated(0, -LEG_HEIGHT, 0);
            DrawUtil.drawCube(gl, LEG_WIDTH, LEG_HEIGHT, LEG_DEPTH, 
                TEX_LEG_BACK_R, TEX_LEG_FRONT_R, TEX_LEG_INSIDE_R,
                TEX_LEG_OUTSIDE_R, TEX_LEG_TOP_R, TEX_LEG_BOTTOM_R);
        }gl.glPopMatrix();
    }
    
    public void body(GL2 gl) {
        gl.glPushMatrix(); {
            // Move to correct position
            gl.glTranslated(0, LEG_HEIGHT, 0);
            DrawUtil.drawCube(gl, BODY_WIDTH, BODY_HEIGHT, BODY_DEPTH, 
                TEX_BODY_BACK, TEX_BODY_FRONT, TEX_BODY_LEFT,
                TEX_BODY_RIGHT, TEX_BODY_TOP, TEX_BODY_BOTTOM);
        }gl.glPopMatrix();
    }
    
    public void leftArm(GL2 gl) {
        gl.glPushMatrix(); {
            // Move to correct position
            gl.glTranslated(-ARM_WIDTH, LEG_HEIGHT+(BODY_HEIGHT-ARM_HEIGHT), 0);
            // Move Arm 10 deg open
            gl.glTranslated(0, ARM_HEIGHT, 0);
            gl.glRotated(-10, 0, 0, 1);
            gl.glTranslated(0, -ARM_HEIGHT, 0);
            DrawUtil.drawCube(gl, ARM_WIDTH, ARM_HEIGHT, ARM_DEPTH, 
                TEX_ARM_BACK_L, TEX_ARM_FRONT_L, TEX_ARM_OUTSIDE_L,
                TEX_ARM_INSIDE_L, TEX_ARM_TOP_L, TEX_ARM_BOTTOM_L);
        }gl.glPopMatrix();
    }
    
    public void rightArm(GL2 gl) {
        gl.glPushMatrix(); {
            // Move to correct position
            gl.glTranslated(BODY_WIDTH, LEG_HEIGHT+(BODY_HEIGHT-ARM_HEIGHT), 0);
            // Move Arm 10 deg open
            gl.glTranslated(0, ARM_HEIGHT, 0);
            gl.glRotated(10, 0, 0, 1);
            gl.glTranslated(0, -ARM_HEIGHT, 0);
            DrawUtil.drawCube(gl, ARM_WIDTH, ARM_HEIGHT, ARM_DEPTH, 
                TEX_ARM_BACK_R, TEX_ARM_FRONT_R, TEX_ARM_INSIDE_R,
                TEX_ARM_OUTSIDE_R, TEX_ARM_TOP_R, TEX_ARM_BOTTOM_R);
        }gl.glPopMatrix();
    }

    public void head(GL2 gl) {
        gl.glPushMatrix(); {
            // Move to correct position
            gl.glTranslated(0, LEG_HEIGHT+BODY_HEIGHT, HEAD_DEPTH/4);
            DrawUtil.drawCube(gl, HEAD_WIDTH, HEAD_HEIGHT, HEAD_DEPTH, 
                TEX_HEAD_BACK, TEX_HEAD_FRONT, TEX_HEAD_LEFT,
                TEX_HEAD_RIGHT, TEX_HEAD_TOP, TEX_HEAD_BOTTOM);
        }gl.glPopMatrix();
    }
    // Special head function that moves in up/down direction of angle
    public void head(GL2 gl, double angle) {
        gl.glPushMatrix(); {
            // Move to correct position
            gl.glTranslated(0, LEG_HEIGHT+BODY_HEIGHT, HEAD_DEPTH/4);
            // Allow the head to rotate up/down direction where you are looking
            gl.glTranslated(0, 0, -HEAD_DEPTH/2);
            gl.glRotated(angle, 1, 0, 0);
            gl.glTranslated(0, 0, +HEAD_DEPTH/2);
            DrawUtil.drawCube(gl, HEAD_WIDTH, HEAD_HEIGHT, HEAD_DEPTH, 
                TEX_HEAD_BACK, TEX_HEAD_FRONT, TEX_HEAD_LEFT,
                TEX_HEAD_RIGHT, TEX_HEAD_TOP, TEX_HEAD_BOTTOM);
        }gl.glPopMatrix();
    }
}
