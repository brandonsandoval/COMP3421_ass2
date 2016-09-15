package ass2.spec;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Sets up the Ambient, Diffuse, Specular and Global ambient as well as the
 * position and angle for the main directional light of the game.
 * @author BrandonSandoval
 *
 */
public class LightProp {
    
    // Ambient, Diffuse, Specular and Global ambient properties of light
    private float a, d, s, g;
    // Position of light
    private float x, y, z;
    // Local viewpoint?
    private int localViewer = 0;

    public void setProperties(float a, float d, float s, float g) {
        this.a = a;
        this.d = d;
        this.s = s;
        this.g = g;
    }
    
    public void setPositionAngle(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setup(GL2 gl) {
        setLighting(gl);
        positionLight(gl);
    }
    
    public void setLighting(GL2 gl) {
        
        gl.glEnable(GL2.GL_LIGHT0);
        
        // Light property vectors.
        float lightAmb[] = { a, a, a, 1.0f };
        float lightDif0[] = { d, d, d, 1.0f };
        float lightSpec0[] = { s, s, s, 1.0f };
        float globAmb[] = { g, g, g, 1.0f };

        // Light0 properties.
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDif0,0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec0,0);

        // Global light properties
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globAmb,0); // Global ambient light.
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewer); // Enable local viewpoint
        positionLight(gl);
    }

    private void positionLight(GL2 gl){
        // Directional light
        float lightPos0[] = {x, y, z, 0};

        //Transformations to move lights
        gl.glPushMatrix();{
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos0, 0);
        }gl.glPopMatrix();
    }
    
}
