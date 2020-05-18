package DynamicGraph;

import java.util.*;
import java.awt.Color;

/*
 * Static Class to manage the coloring of edges and verticies in the dynamic graph.
 * A class that both the view and model can access makes for easy changing of the 
 * colors if needed and allows for greater separation.
 * */
public class Colors{
	//TODO: Add more colors as needed
	public static final int TREEEDGE = 3;
	public static final int BACKEDGE = 5;
	public static final int EFOCUSED = 4;
	public static final int VDONE = 3;
	public static final int VFOCUSED = 2;
	public static final int SHADOW = 1;
	public static final int INIT = 0;
	private static Map<Integer, Color> COLORS = Map.of(
			0,Color.BLACK, 
			1,Color.GRAY,
			2,Color.ORANGE,
			3,Color.MAGENTA,
			4,Color.RED,
			5,Color.LIGHT_GRAY
			);
	public static Color GetColor(int i){
		return COLORS.get(i);
	}
}
