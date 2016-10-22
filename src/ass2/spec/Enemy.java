package ass2.spec;

import com.jogamp.opengl.GL2;

public class Enemy extends Human {

    private double[] myPos;
    private double angle;
    
    public Enemy(double x, double y, double z, double angle) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
        this.angle = angle;

        texture = "img/skin01.png";
        textureExt = "png";
    }
    
    public double[] getPosition() {
        return myPos;
    }

    public double getAngle() {
        return angle;
    }
    
    public void drawEnemy(GL2 gl){

        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId());
        gl.glPushMatrix(); {
            gl.glTranslated(myPos[0], myPos[1], myPos[2]);
            gl.glRotated(angle, 0, 1, 0);
            gl.glScaled(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);

            // Draw the avatar
            
            MaterialLightProp.enemyLightProp(gl);
            leftLeg(gl);
            rightLeg(gl);
            body(gl);
            leftArm(gl);
            rightArm(gl);
            MaterialLightProp.enemyHeadLightProp(gl);
            head(gl);
            
        } gl.glPopMatrix();
    }

}
