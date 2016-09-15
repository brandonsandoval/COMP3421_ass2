package ass2.spec;
/**
 * Static math functions for vectors
 * @author BrandonSandoval
 *
 */
public class MathUtil {

    /**
     * Subtract 2 vectors r = (b - a)
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
        // Indexes
        final int x = 0, y = 1, z = 2;
        // Vectors
        double[] n = new double[3];
        double[] u = vectorMinus(p0, p1);
        double[] v = vectorMinus(p0, p2);
        // Normal calculation
        n[x] = u[y]*v[z] - u[z]*v[y];
        n[y] = u[z]*v[x] - u[x]*v[z];
        n[z] = u[x]*v[y] - u[y]*v[x];
        return n;
    }
    
}
