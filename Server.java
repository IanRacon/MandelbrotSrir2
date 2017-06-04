import java.rmi.*;
import java.rmi.server.*;
import java.net.*;

public class Server extends UnicastRemoteObject implements IMandelbrotResolver
{
	/* constructor */
	public Server() throws RemoteException
	{ 
		/* call parent class constructor,it will create server skeleton */
		super(); 							   	
	}

	/* declare external C function as this class'es method (native) */

/*** Step 1:
   * Provide Java side declaration of JNI native (C implemented) method 
   * (named i.e. compute) performing actual calculations on 
   * vector[], Power, Mean[] arguments and returning dummy errorCode.
   */


    public MandelbrotTest(double minx, double maxx, double miny, double maxy, double step) {
        this.minx = minx;
        this.maxx = maxx;
        this.miny = miny;
        this.maxy = maxy;
        this.step = step;
	}

   public native int[][] compute();
   //public native int[][] compute(double x1, double x2, double y1, double y2, double step);

	/* implement method declared in IMandelbrotResolver interface */

/*** Step 2:
   * Implement RMI method (server side) declared in the RMI interface
   * throwing RemoteException class exceptions. This method calls
   * JNI native method (compute - declared in step 1) 
   * to do the calculations. Parameters called by reference may change
   * its values.
   */
    public ChunkData mandelbrot(ChunkCoords chunkParams) throws RemoteException
    {
		MandelbrotTest man = new MandelbrotTest(chunkParams.x1, chunkParams.y1, chunkParams.x2, chunkParams.y2, chunkParams.step); 
		ChunkData chunkData = new ChunkData();
        chunkData.mandelbrotValues = compute();
        return chunkData;
    }
    

	/* load dynamic system library containing native function */

/*** Step 3:
   * Load dynamically linked library (with native C function implementation).
   */
    static
    {   
        System.loadLibrary("MandelbrotTest");
    }


	/* register server in RMI registry */

	public static void main( String args[] ) throws Exception 
	{
		String hostName = InetAddress.getLocalHost().getHostName();
		
		/* start security manager */
		if( System.getSecurityManager() == null )
			System.setSecurityManager( new SecurityManager() );

        /* bind server implemented object to URL (with optional port)
		   in RMI registry on local hostName:port */

/*** Step 4:
   * Register this class object creation with specific URL in RMI registry
   * by rebinding //server_host_name/path/name with new object of this class.
   */
    Naming.rebind("//"+hostName+(args.length>0?":"+args[0]:"")+"/mandelbrot", new Server());

	}
}
