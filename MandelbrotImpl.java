import java.util.Arrays;

/**
 * Test purpose class.
 */
public class MandelbrotImpl {
    
    private final double minx;
    private final double maxx;
    private final double miny;
    private final double maxy;
    private final double step;
    private final static int maxiters = 999;
    
    static {
        System.loadLibrary("MandelbrotImpl");
    }
    
    public MandelbrotImpl(double minx, double maxx, double miny, double maxy, double step) {
        this.minx = minx;
        this.maxx = maxx;
        this.miny = miny;
        this.maxy = maxy;
        this.step = step;
    }
    
    public native int[][] compute();
    
    public static void main(String[] args) {
        MandelbrotImpl man = new MandelbrotImpl(-2, 1, -1, 1, 0.1);
        int[][] result = man.compute();
        System.out.println(Arrays.deepToString(result));
    }
    
}
