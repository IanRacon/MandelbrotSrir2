import java.rmi.*;

public interface IMandelbrotResolver extends Remote
{
   /* declare RMI method than can be invoked from the client */
   public ChunkData mandelbrot( ChunkCoords coords ) throws RemoteException;
}
