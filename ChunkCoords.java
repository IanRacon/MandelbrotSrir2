import java.io.*;

public class ChunkCoords implements Serializable
{
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

