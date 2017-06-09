import java.rmi.*;			
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.*;


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
			IMandelbrotResolver remoteObj1 = (IMandelbrotResolver) Naming.lookup("//"+hostName+(arg.length > 6?":"
			 + arg[6]:"")+"/mandelbrot");

			IMandelbrotResolver remoteObj2 = (IMandelbrotResolver) Naming.lookup("//"+hostName+(arg.length > 7?":"
			 + arg[7]:"")+"/mandelbrot");

				/* prepare "parameters" RMI argument object */

			/*** Step 2:
			   * Prepare ChunkCoords "parameters".
			   */
			ChunkCoords chunkParams1 = new ChunkCoords();
			chunkParams1.x1 = x1;
			chunkParams1.y1 = y1;
			chunkParams1.x2 = x2;
			chunkParams1.y2 = (y1+y2)/2;
			chunkParams1.step = step;

			ChunkCoords chunkParams2 = new ChunkCoords();
			chunkParams2.x1 = x1;
			chunkParams2.y1 = (y1+y2)/2 + step;
			chunkParams2.x2 = x2;
			chunkParams2.y2 = y2;
			chunkParams2.step = step;

			/* execute RMI call */
			/*** Step 3:
			   * Call remote method of obj object. 
			   */
			ChunkData result1;
			ChunkData result2;
			result1 = remoteObj1.mandelbrot(chunkParams1);
			result2 = remoteObj2.mandelbrot(chunkParams2);

			System.out.println("Data from server 1: rows: " + result1.mandelbrotValues.length + ", cols: " + result1.mandelbrotValues[0].length);
			System.out.println("Data from server 2: rows: " + result2.mandelbrotValues.length + ", cols: " + result2.mandelbrotValues[0].length);

			int[][] result = append(result1.mandelbrotValues, result2.mandelbrotValues);
			/* print results from returned parameters object */
			/*** Step 4:
			   * Print results obtained by RMI.
			   */

			//System.out.println(Arrays.deepToString(result1.mandelbrotValues));
			//System.out.println(Arrays.deepToString(result2.mandelbrotValues));
			exportBoardToFile(result, x1, x2, y1, y2, step, rows, cols); 
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return;
		}
	}
	private static void exportBoardToFile(int[][] result, double minx, 
			double maxx, double miny, double maxy, double step,
			int rows, int cols)
	{
		try
		{
			PrintWriter out = new PrintWriter("data.txt");	
			out.println("# Mandelbrot set for x = [" + minx + ", " 
			+ maxx + "], y = [" + miny + ", " + maxy 
			+ "], step = " + step);

			double x_mult = (maxx - minx) / (cols - 1);
			double y_mult = (maxy - miny) / (rows - 1);
			System.out.println("Rows: " + rows + ", cols: " + cols);
			for (int row = 0; row < rows; ++row) 
			{
				double y = y_mult * row + miny;
				for (int col = 0; col < cols; ++col) 
				{
					double x = x_mult * col + minx;
					out.println( x + "\t" + y + "\t" + result[row][col]);
				}
				out.println();
			}
			out.close();
		}catch(FileNotFoundException e)
		{      
			System.out.println(e);
		}
	}
	private static int[][] append(int[][] a, int[][] b) 
	{
		int[][] result = new int[a.length + b.length][];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
    }	
}
