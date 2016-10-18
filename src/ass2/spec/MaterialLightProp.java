package ass2.spec;

import com.jogamp.opengl.GL2;

/**
 * Static functions to set the material properties for various objects
 * @author BrandonSandoval
 *
 */
public class MaterialLightProp {
    private static float matAmbAndDif[] = null;
    private static float matSpec[] = null;
    private static float matShine[] = null;
    private static float emm[] = null;

    public static void terrainLightProp(GL2 gl) {
        matAmbAndDif = new float[]{0.00f, 0.60f, 0.00f, 1.0f};
        matSpec = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        matShine = new float[]{50.0f};
        emm = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
        applyMaterialLightProp(gl);
    }
    
    public static void treeHeadLightProp(GL2 gl) {
        matAmbAndDif = new float[]{0.50f, 1.00f, 0.50f, 1.0f};
        matSpec = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        matShine = new float[]{50.0f};
        emm = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
        applyMaterialLightProp(gl);
    }
    
    public static void treeTrunkLightProp(GL2 gl) {
        matAmbAndDif = new float[]{0.50f, 0.33f, 0.22f, 1.0f};
        matSpec = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
        matShine = new float[]{50.0f};
        emm = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
        applyMaterialLightProp(gl);
    }
    
    public static void applyMaterialLightProp(GL2 gl) {
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE, matAmbAndDif,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, matSpec,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SHININESS, matShine,0);
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, emm,0);
    }
}
