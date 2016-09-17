package ass2.spec;

/**
 * Static math functions for vectors
 * @author BrandonSandoval
 *
 */
public class MathUtil {

    // Constants used for array indexes
    private final static int X = 0, Y = 1, Z = 2;
    // Constant to define an axis, used in interpolation
    final static int X_AXIS = 0, Y_AXIS = 1, Z_AXIS = 2;
    
    /**
     * Subtract 2 vectors a and b such that r = (b - a)
     * @param a vector
     * @param b vector
     * @return r vector
     */
    public static double[] vectorMinus(double[] a, double[] b) {
        double[] r = new double[a.length];
        for(int i = 0; i < a.length; i++) {
            r[i] = b[i] - a[i];
        }
        return r;
    }
    
    /**
     * Calculate the normal vector given 3 points (counter-clockwise)
     * @param p0 point
     * @param p1 point
     * @param p2 point
     * @return n normal vector
     */
    public static double[] normal(double[] p0, double[] p1, double[] p2) {
        // Vectors
        double[] u = vectorMinus(p0, p1);
        double[] v = vectorMinus(p0, p2);
        // Normal calculation
        double[] n = {
            u[Y]*v[Z] - u[Z]*v[Y],
            u[Z]*v[X] - u[X]*v[Z],
            u[X]*v[Y] - u[Y]*v[X]
        };
        return n;
    }
    /**
     * Linear Interpolation formula
     * 
     * @param target point we are interpolating (between p0 - p1)
     * @param axis which axis are we interpolating for i.e. X or Z interpolation
     * @param targetAxis which axis are we deriving our answer from, i.e. depth on Y-axis
     * @param p0 first point
     * @param p1 second point
     * @return result 
     */
    public static double linearInterpolate(double[] target, int axis, int targetAxis, double[] p0, double[] p1) {
        return (target[axis] - p0[axis]) / (p1[axis] - p0[axis]) * p1[targetAxis] +
               (p1[axis] - target[axis]) / (p1[axis] - p0[axis]) * p0[targetAxis];
    }
    /**
     * Bilinear Interpolation formula to find altitude (y-axis) of target given 3 points 
     * 
     * @param target point
     * @param p0 point
     * @param p1 point
     * @param p2 point 
     * @return altitude
     */
    public static double bilinearInterpolate(double[] target, double[] p0, double[] p1, double[] p2) {

        double depthAy = MathUtil.linearInterpolate(target, MathUtil.Z_AXIS, MathUtil.Y_AXIS, p0, p1);
        double depthBy = MathUtil.linearInterpolate(target, MathUtil.Z_AXIS, MathUtil.Y_AXIS, p1, p2);
        double depthAx = MathUtil.linearInterpolate(target, MathUtil.Z_AXIS, MathUtil.X_AXIS, p0, p1);
        double depthBx = MathUtil.linearInterpolate(target, MathUtil.Z_AXIS, MathUtil.X_AXIS, p1, p2);
        // Now we have the 2 points, we can linear interpolate A and B
        double[] A = {depthAx, depthAy, target[2]};
        double[] B = {depthBx, depthBy, target[2]};
        return MathUtil.linearInterpolate(target, MathUtil.X_AXIS, MathUtil.Y_AXIS, A, B);
        
    }
    
}
