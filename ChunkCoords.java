import java.io.*;

/**
 * Carry mandelbrot space chunk parameters.
 */
public class ChunkCoords implements Serializable
{
	/**
	 * @param x1 Left chunk coordinate
	 * @param x2 Right chunk coordinate
	 * @param y1 Upper chunk coordinate
	 * @param y2 Lower chunk coordinate
	 * @param step Distance between two points 
	 */
	ChunkCoords(double x1, double x2, double y1, double y2, double step)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.step = step;
	}
	double x1;
	double y1;
	double x2;
	double y2;
	double step;
}

