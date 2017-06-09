import java.rmi.*;			
import java.util.Arrays;
import java.io.PrintWriter;
import java.io.*;

class RemoteMandelbrot extends Thread
{
	private String threadName;
	ChunkCoords chunkParams;
	IMandelbrotResolver remoteMethod;
	ChunkData chunkData;

	RemoteMandelbrot(String name, ChunkCoords p_chunkParams, IMandelbrotResolver p_remoteMethod)
	{
		threadName = name;
		chunkParams = p_chunkParams;
		remoteMethod = p_remoteMethod;
	}
	public void run()
	{
		try
		{
			System.out.println("Starting remote method");
			chunkData = remoteMethod.mandelbrot(chunkParams);
			System.out.println("Remote method execution ends");
		}catch(RemoteException e)
		{
			System.out.println("Thread: " + threadName + " interrupted by exception: " + e);
		}
	}
	public ChunkData getResults()
	{
		return chunkData;
	}
}

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
			ChunkCoords chunkParams1 = new ChunkCoords(x1, x2, y1, (y1+y2)/2, step);
			ChunkCoords chunkParams2 = new ChunkCoords(x1, x2, (y1+y2)/2 + step, y2, step);
			RemoteMandelbrot remote1 = new RemoteMandelbrot("Remote-1", chunkParams1, remoteObj1);
			RemoteMandelbrot remote2 = new RemoteMandelbrot("Remote-2", chunkParams2, remoteObj2);
			/* execute RMI call */
			/*** Step 3:
			   * Call remote method of obj object. 
			   */
			remote1.start();
			remote2.start();

			try
			{
				remote1.join();
				remote2.join();
			}catch(Exception e)
			{
				System.out.println(e);
			}

			int[][] results1 = remote1.getResults().mandelbrotValues;
			int[][] results2 = remote2.getResults().mandelbrotValues;
			System.out.println("Data from server 1: rows: " + results1.length + ", cols: " + results1[0].length);
			System.out.println("Data from server 2: rows: " + results2.length + ", cols: " + results2[0].length);

			int[][] aggregatedResult = append(results1, results2);

			/* print results from returned parameters object */
			/*** Step 4:
			   * Print results obtained by RMI.
			   */
			exportBoardToFile(aggregatedResult, x1, x2, y1, y2, step, rows, cols); 
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
