import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.text.NumberFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// My Defined Graph class
import DynamicGraph.*;

public class GraphAlgorithmAnimate extends JPanel {
	// DIMENSION / MATH VARIABLES
	private static final int DIM_W = 800;
	private static final int DIM_H = 700;
	private static final int DIAMETER = 40;
	private static final int W_OFF = DIM_W - DIAMETER * 2;
	private static final int H_OFF = DIM_H - DIAMETER * 3;
	private static final int X_OFF = 20;
	private static final int Y_OFF = 40 + DIAMETER/2;
	// GUI ITEMS
	private JMenuBar menuBar;
	private JMenu propertiesMenu;
	private JMenu algorithmsMenu;
	// Algorithms:
	// BFS
	// DFS
	// Dijkstras
	// Kruskals
	// Prims
	private JCheckBoxMenuItem connectedMenuItem;
	private JCheckBoxMenuItem directedMenuItem; 
	private JCheckBoxMenuItem weightedMenuItem; 
	private JCheckBoxMenuItem negativeMenuItem; 
	private JCheckBoxMenuItem dagMenuItem;	
	private JButton startButton;
	private JButton genGraphButton;
	private JButton resetButton;
	private JFormattedTextField numVertText;
	// TIMER
	private Timer timer = null;
	// GRAPH ITEMS
	private Graph graph;
	private Vertex[] verticies;
	private Edge[] edges;
	// DRAWING HELP
	private boolean ExistingGraph;
	Map<Integer,Color> COLORS = Map.of( 0,Color.BLACK, 1,Color.GRAY, 2,Color.YELLOW, 3,Color.MAGENTA );

	public GraphAlgorithmAnimate() {
		initializeGUIItems();
	}

	private void initializeGUIItems(){
		timer = new Timer(300, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				((Timer) e.getSource()).stop();
//				startButton.setEnabled(false);
				repaint();
			}
		});
		menuBar = new JMenuBar(); // Menu Bar

		// Properties menu and items
		propertiesMenu = new JMenu("Properties");
		menuBar.add(propertiesMenu); // Add properties menu to menu bar
		connectedMenuItem = new JCheckBoxMenuItem("Connected");
		directedMenuItem = new JCheckBoxMenuItem("Directed");
		weightedMenuItem = new JCheckBoxMenuItem("Weighted");
		negativeMenuItem = new JCheckBoxMenuItem("Negative");
		dagMenuItem = new JCheckBoxMenuItem("DAG");
		propertiesMenu.add(connectedMenuItem);
		propertiesMenu.add(directedMenuItem);
		propertiesMenu.add(weightedMenuItem);
		propertiesMenu.add(negativeMenuItem);
		propertiesMenu.add(dagMenuItem);

		// Algorithms menu and items
		algorithmsMenu = new JMenu("Algorithm");
		menuBar.add(algorithmsMenu);
		// Create text field only allowing entries of 1 - 26
		NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1); // Restrict entry to min of 1
		formatter.setMaximum(26); // Restrict to max 26
		formatter.setAllowsInvalid(true); // Do not allow invalid changes
		formatter.setCommitsOnValidEdit(true); // Set value on valid edits
		numVertText = new JFormattedTextField(formatter); 
		numVertText.setValue(6); // Initial start value of 6
		numVertText.setColumns(2); // Set width to 2

		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				timer.start();
			}
		});

		genGraphButton = new JButton("Generate Graph");
		genGraphButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
//				timer.stop();
				generateNewGraph();
			}
		});
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				repaint();
//				startButton.setEnabled(true);
			}
		});
		add(menuBar);
		add(numVertText);
		add(genGraphButton);
		add(startButton);
		add(resetButton);
	}

	private void generateNewGraph(){

		//TODO: generate a new simple graph with 'n' verticies 
		graph = new Graph( (int)numVertText.getValue() );
		verticies = graph.get_verticies();
		edges = graph.get_edges();

		//TODO: generate a new graph based on the buttons selected

		ExistingGraph = true; // Alert the view that the graph now exists
		repaint();
	}

	public void drawVertex(Graphics2D g, Vertex v) {
		int off = DIAMETER / 2;
		int x = (int)(v.Location.x * W_OFF) + X_OFF;
		int y = (int)(v.Location.y * H_OFF) + Y_OFF;
		g.setColor(COLORS.get(v.Color));
		g.drawOval(x, y, DIAMETER, DIAMETER);
		g.drawString(v.Name, x + off - 6, y + off + 6);
	}

	public void drawEdge(Graphics2D g, Edge e) {
		int rad = DIAMETER / 2;
		int x1 = (int)(e.Begin.x * W_OFF) + rad + X_OFF;
		int y1 = (int)(e.Begin.y * H_OFF) + rad + Y_OFF;
		int x2 = (int)(e.End.x * W_OFF) + rad + X_OFF;
		int y2 = (int)(e.End.y * H_OFF) + rad + Y_OFF;
		Point v = new Point(x2 - x1, y2 - y1);
		double mag_v = Math.sqrt(v.x * v.x + v.y * v.y);
		v.x /= mag_v; // Scale v to be a normal vector
		v.y /= mag_v;
		v.x *= rad; // Scale v to offset distance, radius of the circles
		v.y *= rad;
		// Calculate new point p1
		Point p1 = new Point(x1 + v.x, y1 + v.y);
		// Calculate new point p2
		Point p2 = new Point(x2 - v.x, y2 - v.y);
		// TODO: Incorporate coloring into edges for MST and DFS
		if(e.Directed) Arrow.drawArrow(g, (int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		else g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
//		if(e.Weighted) //TODO: Print weights of the edges
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintComponent((Graphics2D) g);
	}

	protected void paintComponent(Graphics2D g){
		g.setFont(new Font("Courier New", Font.BOLD, 24));
		if(ExistingGraph){ // Only paint the graph if one exists
			for(int i = 0; i < edges.length; i++){
				drawEdge(g, edges[i]); 
			}
			g.setStroke(new BasicStroke(4));
			for(int i = 0; i < verticies.length; i++){
				drawVertex(g, verticies[i]);
			}
		}
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(DIM_W, DIM_H);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Graph Algorithms");
				frame.add(new GraphAlgorithmAnimate());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
