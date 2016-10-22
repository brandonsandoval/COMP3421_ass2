package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr, BrandonSandoval, James Shin
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    
    private MyTexture myRoadTexture = null;
    private String textureRoad = "img/road01.jpg";
    private String textureExt = "jpg";
    private Terrain myTerrain;
    
    private static final int ALTITUDE = 0;
    
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);
    }

    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine, Terrain myTerrain) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
        this.myTerrain = myTerrain;
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    public void drawRoad(GL2 gl) {
        //gl.glLineWidth(30);
        
        //gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINES);
        
        MaterialLightProp.roadLightProp(gl);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, myRoadTexture.getTextureId());
        
        gl.glBegin(GL2.GL_QUAD_STRIP); {
            

/*            // First Points
            double[] pt1 = point(0.1);
            double[] pt2 = point(0.2);
            double[][] normals;
            
            normals = normal(pt1, pt2);
            normals[0] = normalize(normals[0]);
            normals[1] = normalize(normals[1]);
            
            double[] pointA = getRoadPoint(normals[0], pt1);
            double[] pointB = getRoadPoint(normals[1], pt1);
            
            gl.glVertex3d(pointA[0], 134, pointA[1]);
            gl.glVertex3d(pointB[0], 134, pointB[1]);
            gl.glVertex3d(pointA[0], 134, pointA[1]);
            gl.glVertex3d(pointB[0], 134, pointB[1]);*/
            
            
            
            
            
            double[] prev_pt = point(0);
            double[] pt;
            int count = 0;
            
            for(double i = 0.01; i < 1; i += 0.01) {
                
                pt = point(i);
                double[][] normals = normal(prev_pt, pt);
                
                normals[0] = normalize(normals[0]);
                normals[1] = normalize(normals[1]);
        
                double[] roadPointA = getRoadPoint(normals[0], pt);
                double[] roadPointB = getRoadPoint(normals[1], pt);
                
                gl.glTexCoord2d(i, 0);
                gl.glVertex3d(roadPointB[0], myTerrain.getGridAltitude((int)Math.round(clamp(roadPointB[0], 0, myTerrain.getMySize().width-1)), 
                                                                       (int)Math.round(clamp(roadPointB[1], 0, myTerrain.getMySize().height-1)))+0.1, 
                                                                       roadPointB[1]);
                
                gl.glTexCoord2d(i, 1);
                gl.glVertex3d(roadPointA[0], myTerrain.getGridAltitude((int)Math.round(clamp(roadPointA[0], 0, myTerrain.getMySize().width-1)), 
                                                                       (int)Math.round(clamp(roadPointA[1], 0, myTerrain.getMySize().height-1)))+0.1, 
                                                                       roadPointA[1]);
                
                //gl.glNormal3d(0, 1, 0);
                //gl.glVertex3d(roadPointB[0], 134, roadPointB[1]);
                        
                count++;
                prev_pt = pt;
            }
            
        } gl.glEnd();
        
    }
    
    public double[][] normal(double[] pt1, double[] pt2) {
        
        double[][] normals = new double[2][2];
        double dx = pt2[0] - pt1[0];
        double dz = pt2[1] - pt1[1];
        
        normals[0][0] = -dz;
        normals[0][1] = dx;
        normals[1][0] = dz;
        normals[1][1] = -dx;
        
        return normals;
    }
    
    public double[] normalize(double[] vector) {
        
        double magnitude = Math.sqrt(Math.pow(vector[0], 2) + Math.pow(vector[1], 2));
        
        double[] normalized = new double[2];
        
        normalized[0] = vector[0] / magnitude;
        normalized[1] = vector[1] / magnitude;
        
        return normalized;
    }
    
    // Get Points of road width
    public double[] getRoadPoint(double[] vector, double[] pt) {
        
        return new double[] {pt[0] + vector[0]*myWidth, pt[1] + vector[1]*myWidth};
    }
    
    public void loadTexture(GL2 gl, boolean mipmaps){
        if(myRoadTexture == null) {
            myRoadTexture = new MyTexture(gl, textureRoad, textureExt, mipmaps);
        }
    }
    
    public double clamp(double value, double lower, double upper) {
        if(value <= lower) {
            return lower;
        }else if(value >= upper) {
            return upper;
        }
        return value;
    }

}
