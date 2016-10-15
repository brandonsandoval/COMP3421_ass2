package ass2.spec;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr, BrandonSandoval
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    private String textureFileName = "img/grass01.jpg";
    private String textureExt = "jpg";
    private MyTexture myTexture;
    
    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        
        double[] target = {x, 0, z};
        int xF = (int) Math.floor(x);
        int zF = (int) Math.floor(z);
        double sum = (x - xF) + (z - zF);
        double p0[] = {xF, getGridAltitude(xF, zF), zF};
        double p1[] = {xF, getGridAltitude(xF, zF+1), zF+1};
        double p2[] = {xF+1, getGridAltitude(xF+1, zF), zF};
        double p3[] = {xF+1, getGridAltitude(xF+1, zF+1), zF+1};

        /*
         *  p0 ---- p2
         *  |     / |
         *  | A  /  |
         *  |   / B |
         *  |  /    |
         *  p1 ---- p3
         *  
         *  if sum >= 0 && < 1  we are in triangle A
         *  if sum > 1  && <= 2 we are in triangle B
         *  if sum == 1 we are neither triangle A nor B, we are on the line p1-p2
         *  so now we know which points to interpolate
         */

        assert(sum >= 0);
        assert(sum <= 2);
        if(sum == 1) {
            // we are on the line p1-p2 we need to interpolate this line and return the altitude
            return MathUtil.linearInterpolate(target, MathUtil.Z_AXIS, MathUtil.Y_AXIS, p1, p2);
        }else if(sum <= 1) {
            // We are in triangle A, do bilinear interpolating
            return MathUtil.bilinearInterpolate(target, p0, p1, p2);
        } else {
            // We are in triangle B, do bilinear interpolating
            return MathUtil.bilinearInterpolate(target, p1, p2, p3);
        }
    }

    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


    /**
     * Draws the terrain
     * @param gl
     */
    public void drawTerrain(GL2 gl) {
        // 4 points of a square (from x,z to x+1,z+1 with altitude y)
        double p0[] = {0, 0, 0};
        double p1[] = {0, 0, 0};
        double p2[] = {0, 0, 0};
        double p3[] = {0, 0, 0};

        MaterialLightProp.terrainLightProp(gl);
        myTexture = new MyTexture(gl, textureFileName, textureExt, true);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        
        // loop over the altitude grid in myTerrain
        for(int z = 0; z < size().height -1; z++) {
            for(int x = 0; x < size().width -1; x++) {
            
                /*
                 *  p0 ---- p2
                 *  |     / |
                 *  | A  /  |
                 *  |   / B |
                 *  |  /    |
                 *  p1 ---- p3
                 *  
                 *  p0 -> p1 -> p2  == A
                 *  p3 -> p2 -> p1  == B
                 */
                
                p0[0] = x;
                p0[1] = getGridAltitude(x, z);
                p0[2] = z;
                
                p1[0] = x;
                p1[1] = getGridAltitude(x, z+1);
                p1[2] = z+1;
                
                p2[0] = x+1;
                p2[1] = getGridAltitude(x+1, z);
                p2[2] = z;
                
                p3[0] = x+1;
                p3[1] = getGridAltitude(x+1, z+1);
                p3[2] = z+1;
              
                // The two triangles represents 1 grid square
                gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId());
                double[] n;
                gl.glBegin(GL2.GL_TRIANGLES);{
                    n = MathUtil.normal(p0, p1, p2);
                    gl.glNormal3d(n[0], n[1], n[2]);
                    gl.glTexCoord2d(0, 0);
                    gl.glVertex3dv(p0,0);
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3dv(p1,0);
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3dv(p2,0);

                    n = MathUtil.normal(p3, p2, p1);
                    gl.glNormal3d(n[0], n[1], n[2]);
                    gl.glTexCoord2d(1, 1);
                    gl.glVertex3dv(p3,0);
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3dv(p2,0);
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3dv(p1,0);
                    
                }gl.glEnd();
            }
        }
    }
}
