package ass2.spec;

import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.common.nio.Buffers;
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

    // *** Texturing
    private String textureFileName = "img/grass01.jpg";
    private String textureExt = "jpg";
    private MyTexture myTexture = null;

    // *** VBOs
    // Hold points, normals and texture coords for VBO
    private float positions[];
    private float normals[];
    private float textures[];
    private FloatBuffer posData;
    private FloatBuffer normData;
    private FloatBuffer texData;
    // Number of points for position, normals and texture coords
    private int numPoints;
    private int numNormals;
    private int numTextures;
    // VBO ID
    private int[] bufferID = new int[1];
    // Store whether VBO has already been initialized
    private boolean runOnceSetupVBO = false;
    
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
     * Initial setup for texturing
     * @param gl
     * @param mipmaps
     */
    public void loadTerrain(GL2 gl, boolean mipmaps) {
        if(myTexture == null) {
            myTexture = new MyTexture(gl, textureFileName, textureExt, mipmaps);
        }
    }

    // Load position data into array and setup VBO
    // This must be run before running drawVBOTerrain();
    public void setupVBO (GL2 gl) {
    	// Do not run the code if we had already ran it
    	if(runOnceSetupVBO){
    		return;
    	}
    	runOnceSetupVBO = true;
    	
    	// Size of 2d grid + 
        // x2 (2 triangles per grid)
        // x3 (3 points per triangle)
        numPoints =  mySize.width*mySize.height *2 *3;
        numNormals = mySize.width*mySize.height *2 *3;
        numTextures =mySize.width*mySize.height *2 *3;
        // x3 (3 dimensions per point/normal)
        // but 2 dimensions on texture images
        positions = new float[numPoints *3];
        normals = new float[numNormals *3];
        textures = new float[numNormals *2];
        // 4 points of a square (from x,z to x+1,z+1 with altitude y)
        // and 2 texture cord maps triangles A and B
        // see diagram below
        float p0[] = {0, 0, 0};
        float p1[] = {0, 0, 0};
        float p2[] = {0, 0, 0};
        float p3[] = {0, 0, 0};
        float texcoordA[] = {0, 0,
        				     0, 1,
        				     1, 0};
        float texcoordB[] = {1, 1,
        					 1, 0,
        					 0, 1};
        // holds result of normal calculations
        float[] n;
        // index for positions
        int iP = 0;
        // index for normals
        int iN = 0;
        // index for texture coords
        int iT = 0;
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
                // p0
                p0[0] = x;
                p0[1] = (float)getGridAltitude(x, z);
                p0[2] = z;
                // p1
                p1[0] = x;
                p1[1] = (float)getGridAltitude(x, z+1);
                p1[2] = z+1;
                // p2
                p2[0] = x+1;
                p2[1] = (float)getGridAltitude(x+1, z);
                p2[2] = z;
                // p3
                p3[0] = x+1;
                p3[1] = (float)getGridAltitude(x+1, z+1);
                p3[2] = z+1;
                
                // Store both these triangles points into array positions
                // Triangle A -> positions
                System.arraycopy(p0, 0, positions, iP      , 3);
                System.arraycopy(p1, 0, positions, iP+(1*3), 3);
                System.arraycopy(p2, 0, positions, iP+(2*3), 3);
                // Triangle B -> positions
                System.arraycopy(p3, 0, positions, iP+(3*3), 3);
                System.arraycopy(p2, 0, positions, iP+(4*3), 3);
                System.arraycopy(p1, 0, positions, iP+(5*3), 3);
                // position index jumps by 6 points * 3 dimensions
                iP += 6*3;

                // Vertex Normals
                // Triangle A -> normals
                n = MathUtil.normal(p0, p1, p2);
                System.arraycopy(n, 0, normals, iN      , 3);
                System.arraycopy(n, 0, normals, iN+(1*3), 3);
                System.arraycopy(n, 0, normals, iN+(2*3), 3);
                // Triangle A -> normals
                n = MathUtil.normal(p3, p2, p1);
                System.arraycopy(n, 0, normals, iN+(3*3), 3);
                System.arraycopy(n, 0, normals, iN+(4*3), 3);
                System.arraycopy(n, 0, normals, iN+(5*3), 3);
                // normal index jumps by 6 normals * 3 dimensions
                iN += 6*3;
                
                // Texture coords
                System.arraycopy(texcoordA, 0, textures, iT  , 6);
                System.arraycopy(texcoordB, 0, textures, iT+6, 6);
                iT += 12;
            }
        }
        // Set up VBO
        gl.glGenBuffers(1, bufferID, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferID[0]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, 
				numPoints*3*Float.BYTES +
				numNormals*3*Float.BYTES +
				numTextures*2*Float.BYTES, 
				null, 
				GL2.GL_STATIC_DRAW);
        posData = Buffers.newDirectFloatBuffer(positions);
        positions = null;
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
				0,
				numPoints*3*Float.BYTES,
				posData);
        normData = Buffers.newDirectFloatBuffer(normals);
        normals = null;
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
        		numPoints*3*Float.BYTES,
				numNormals*3*Float.BYTES,
				normData);
        texData = Buffers.newDirectFloatBuffer(textures);
        textures = null;
        gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 
        		numPoints*3*Float.BYTES +
				numNormals*3*Float.BYTES,
				numTextures*2*Float.BYTES,
				texData);
    }
    
    /**
     * Draws the terrain using VBO 
     * must call setupVBO() first
     * @param gl
     */
    public void drawVBOTerrain(GL2 gl) {
        MaterialLightProp.terrainLightProp(gl);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId());
    	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, bufferID[0]);
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
    	gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    	gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
    	gl.glNormalPointer(GL.GL_FLOAT, 0, numPoints*3*Float.BYTES);
    	gl.glTexCoordPointer(2, GL.GL_FLOAT, 0, numPoints*3*Float.BYTES + numNormals*3*Float.BYTES);
    	gl.glDrawArrays(GL2.GL_TRIANGLES, 0, numPoints);
    	//gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    	//gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
    	//gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
    	//gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Draws the terrain (using gl immediate mode)
     * @param gl
     */
    public void drawTerrain(GL2 gl) {
        // 4 points of a square (from x,z to x+1,z+1 with altitude y)
        double p0[] = {0, 0, 0};
        double p1[] = {0, 0, 0};
        double p2[] = {0, 0, 0};
        double p3[] = {0, 0, 0};

        // Set lighting and texturing
        MaterialLightProp.terrainLightProp(gl);
        gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId());
        
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