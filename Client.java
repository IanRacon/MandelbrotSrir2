import java.rmi.*;			
import java.util.Arrays;

public class Client
{
	public static void main( String arg[] ) throws Exception
	{
		if( arg.length < 6 )
		{					
			System.out.println("Please specify hostname and x1, x2, y1, y2, step of the mandelbrotSet");
			return;
		}
		
		String	hostName = arg[ 0 ];	/* Server */
		double x1 = Float.parseFloat(arg[1]);
		double x2 = Float.parseFloat(arg[2]);
		double y1 = Float.parseFloat(arg[3]);
		double y2 = Float.parseFloat(arg[4]);
		double step = Float.parseFloat(arg[5]);

		int cols = (int)Math.floor((x2-x1)/step) + 1;
		int rows = (int)Math.floor((y2-y1)/step) + 1;
		
		try
		{
				/* start security manager */
				if( System.getSecurityManager() == null )
					System.setSecurityManager( new SecurityManager() );

				/* obtain reference to remote method interface */
			/*** Step 1:
			   * Get reference to the stub object (obj) representing RMI interface
			   * by calling lookup method of Naming class and providing server's URL.
			   */
			IMandelbrotResolver remoteObj = (IMandelbrotResolver) Naming.lookup("//"+hostName+(arg.length > 6?":"
			 + arg[6]:"")+"/mandelbrot");

				/* prepare "parameters" RMI argument object */

			/*** Step 2:
			   * Prepare ChunkCoords "parameters".
			   */
			ChunkCoords chunkParams = new ChunkCoords();
			chunkParams.x1 = x1;
			chunkParams.y1 = y1;
			chunkParams.x2 = x2;
			chunkParams.y2 = y2;
			chunkParams.step = step;

			/* execute RMI call */
			/*** Step 3:
			   * Call remote method of obj object. 
			   */
			ChunkData result;
			result = remoteObj.mandelbrot(chunkParams);

			/* print results from returned parameters object */
			/*** Step 4:
			   * Print results obtained by RMI.
			   */

			System.out.println(Arrays.deepToString(result.mandelbrotValues));
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return;
		}
	}
}
