import java.rmi.*;

/**
 * Remote mandelbrot method interface
 */
public interface IMandelbrotResolver extends Remote
{
   /**
	* RMI method than can be invoked from the client.
	* @param coords Coordinates of requested mandelbrot space.
	* @return Data (Iterations) of requested mandelbrot space.
	* */
   public ChunkData mandelbrot( ChunkCoords coords ) throws RemoteException;
}
