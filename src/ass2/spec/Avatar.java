package ass2.spec;

import com.jogamp.opengl.GL2;

public class Avatar extends Human {

    private double heightOffset;
    private boolean rotationLock;
    
    public Avatar(double heightOffset) {
        this.heightOffset = heightOffset-0.1;
        this.rotationLock = true;

        texture = "img/skin03.png";
        textureExt = "png";
    }
    
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
    
    public void drawAvatar(GL2 gl){
        double[] pos = Camera.getPos();
        double[] angle = Camera.getAngle();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId());
        gl.glPushMatrix(); {
            // Move the avatar next to camera
            gl.glTranslated(pos[Game.X], pos[Game.Y]-heightOffset, pos[Game.Z]);
            gl.glRotated(-90, 0, 1, 0);
            // Rotate the avatar to the direction we are looking at
            if(rotationLock)
                gl.glRotated(angle[Game.Y], 0, 1, 0);
            gl.glScaled(SCALE_FACTOR, SCALE_FACTOR, SCALE_FACTOR);
            gl.glTranslated(-BODY_WIDTH/2, 0, 0);

            // Draw the avatar
            MaterialLightProp.avatarLightProp(gl);
            leftLeg(gl);
            rightLeg(gl);
            body(gl);
            leftArm(gl);
            rightArm(gl);
            head(gl, angle[Game.Z]);
            
        } gl.glPopMatrix();
    }


    public void setRotationLock(boolean value) {
        rotationLock = value;
    }
    public boolean getRotationLock() {
        return rotationLock;
    }
    
}
