/*
 * GRAPH ANIMATOR
 * Copyright Gavin Gray 2020
 * */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import java.util.*;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.Timer;

import DynamicGraph.*;
import DynamicGraph.Point;
import DynamicGraph.Algorithm;

public class GraphAlgorithmAnimate extends JPanel {
	// DIMENSION / MATH VARIABLES
	private static final int DIM_W = 1000;
	private static final int DIM_H = 700;
	private static final int DIAMETER = 40;
	private static final int W_OFF = DIM_W - DIAMETER * 2;
	private static final int H_OFF = DIM_H - DIAMETER * 3;
	private static final int X_OFF = 20;
	private static final int Y_OFF = 40 + DIAMETER/2;
	// GUI ITEMS
	private JMenuBar menuBar;
	private JMenu algorithmsMenu; // Algorithm Menu Items
	// Algorithms:
	private Algorithm algorithm;
	private JRadioButtonMenuItem bfsMenuItem; // BFS
	// DFS
	// Dijkstras
	// Kruskals
	// Prims
	private JLabel algorithmLabel;
	private JMenu propertiesMenu; // Property Menu Items
	private JCheckBoxMenuItem connectedMenuItem;
	private JCheckBoxMenuItem directedMenuItem; 
	private JCheckBoxMenuItem weightedMenuItem; 
	private JCheckBoxMenuItem negativeMenuItem; 
	private JCheckBoxMenuItem acyclicMenuItem;	
	private JButton startButton; // Utility Buttons
	private JButton genGraphButton;
	private JButton resetButton;
	private JFormattedTextField numVertText;
	// TIMER
	private Timer timer;
	// GRAPH ITEMS
	private HashSet<Graph.Property> g_props;
	private Graph graph;
	private Vertex[] verticies;
	private Edge[] edges;
	// DRAWING HELP
	private boolean ExistingGraph;
	Map<Integer,Color> COLORS = Map.of( 
			0, Color.BLACK, 1, Color.GRAY, 2, Color.YELLOW, 3, Color.MAGENTA 
			);

	public GraphAlgorithmAnimate() {
		initializeGUIItems();
	}

	/*
	 * Helper method to initialize all GUI items, this includes:
	 * Property Menu:
	 * 	Check Box Menu items for all graph properites, when
	 * 	one is clicked the item is either removed or added
	 * 	to g_prop list which is a parameter to generate a Graph
	 * Algorithm Menu:
	 * 	Radio Menu items for all available algorithms.
	 * 	Radio buttons are used because only one algorithm may be 
	 * 	run at a time.
	 * Text Box:
	 * 	Accepts integers which will set the number of nodes in the graph
	 * Option Buttons: 
	 * 	Generate Graph: generates new graph with selected properties
	 * 	Start: starts the selected algorithm on the available graph.
	 * */
	private void initializeGUIItems(){
		// TIMER START --->
		timer = new Timer(300, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				((Timer) e.getSource()).stop();
//				startButton.setEnabled(false);
				repaint();
			}
		});
		// TIMER END <---
		
		menuBar = new JMenuBar(); // Menu Bar
		g_props = new HashSet<Graph.Property>();

		// Property menu and items START ---> 
		propertiesMenu = new JMenu("Properties");
		menuBar.add(propertiesMenu); 

		connectedMenuItem = new JCheckBoxMenuItem("Connected");
		connectedMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				propertiesMenu.doClick();
				if(connectedMenuItem.isSelected()) g_props.add(Graph.Property.CONNECTED);
				else g_props.remove(Graph.Property.CONNECTED);
			}
		});
		directedMenuItem = new JCheckBoxMenuItem("Directed");
		directedMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				propertiesMenu.doClick();
				if(directedMenuItem.isSelected()) g_props.add(Graph.Property.DIRECTED);
				else g_props.remove(Graph.Property.DIRECTED);
			}
		});
		weightedMenuItem = new JCheckBoxMenuItem("Weighted");
		weightedMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				propertiesMenu.doClick();
				if(weightedMenuItem.isSelected()) g_props.add(Graph.Property.WEIGHTED);
				else g_props.remove(Graph.Property.WEIGHTED);
			}
		});
		negativeMenuItem = new JCheckBoxMenuItem("Negative");
		negativeMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				propertiesMenu.doClick();
				if(negativeMenuItem.isSelected()) g_props.add(Graph.Property.NEGATIVE);
				else g_props.remove(Graph.Property.NEGATIVE);
			}
		});
		acyclicMenuItem = new JCheckBoxMenuItem("Acyclic");
		acyclicMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				propertiesMenu.doClick();
				if(acyclicMenuItem.isSelected()) g_props.add(Graph.Property.ACYCLIC);
				else g_props.remove(Graph.Property.ACYCLIC);
			}
		}); // ADD PROPERTIES TO MENU
		propertiesMenu.add(acyclicMenuItem);
		propertiesMenu.add(directedMenuItem);
		propertiesMenu.add(weightedMenuItem);
		propertiesMenu.add(negativeMenuItem);
		propertiesMenu.add(connectedMenuItem);
		// Property menu and items END <--- 


		// Algorithms menu and items START --->
		algorithmsMenu = new JMenu("Algorithm");
		ButtonGroup algoGroup = new ButtonGroup();
		algorithmLabel = new JLabel("BREADTH FIRST SEARCH");
		bfsMenuItem = new JRadioButtonMenuItem("BFS");
		bfsMenuItem.setSelected(true);
		bfsMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				algorithmLabel.setText("BREADTH FIRST SEARCH");
			}
		});
		menuBar.add(algorithmsMenu);
		// Add radio buttons to group
		algoGroup.add(bfsMenuItem);
		// Add radio buttons to menu
		algorithmsMenu.add(bfsMenuItem);
		// Algorithms menu and items END <---



		// TEXT FIELD START --->
		NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1); // Restrict entry to min of 1
		formatter.setMaximum(26); // Restrict to max 26
		formatter.setAllowsInvalid(true); // Allow invalid changes
		formatter.setCommitsOnValidEdit(true); // Set value on valid edits
		numVertText = new JFormattedTextField(formatter); 
		numVertText.setValue(6); // Initial start value of 6
		numVertText.setColumns(2); // Set width to 2
		// TEXT FIELD END <---



		// BUTTONS START --->
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: implement starting of algorithm
				algorithm = graph.createAlgorithm();
				repaint();
			}
		});
		genGraphButton = new JButton("Generate Graph");
		genGraphButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				generateNewGraph();
			}
		});
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: possibly change to a pause button
				// or reset the algorithm
//				startButton.setEnabled(true);
				algorithm.step();
				repaint();
			}
		});
		// BUTTONS END <---
		
		
		// Add all items to Frame --->
		add(algorithmLabel);
		add(new JLabel("          "));
		add(menuBar);
		add(new JLabel(" V:"));
		add(numVertText);
		add(genGraphButton);
		add(startButton);
		add(resetButton);
	}

	/*
	 * @param g: Graphics2D object for drawing items to frame
	 * @param v: Vertex to be drawn to frame
	 *
	 * Method to draw a Vertex to the frame. The ID
	 * of the vertex is used to determine the US English
	 * capital letter that is associated with the vertex.
	 * Each vertex is drawn in its designated color.
	 * */
	public void drawVertex(Graphics2D g, Vertex v) {
		int off = DIAMETER / 2;
		int x = (int)(v.Location.x * W_OFF) + X_OFF;
		int y = (int)(v.Location.y * H_OFF) + Y_OFF;
		g.setColor(COLORS.get(v.Color));
		g.drawOval(x, y, DIAMETER, DIAMETER);
		g.drawString( Character.toString(((char)(v.ID + 'A'))), x + off - 6, y + off + 6);
	}

	/*
	 * @param g: Graphics2D component from drawing on the frame
	 * @param e: Edge to be drawn
	 *
	 * Method takes edge to be drawn and scales such that the 
	 * line (whether plain or arrow) only touches the outside
	 * of the vertex circle. This is done by simple vector math
	 * with says: 
	 * v = P1 - P0 
	 * u = v / ||v|| 
	 * P3 = P1/0 +- (c * u) where c is distance away from point
	 * */
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
		// Calculate new point p1
		Point p1 = new Point(x1 + rad * v.x, y1 + rad * v.y);
		// Calculate new point p2
		Point p2 = new Point(x2 - rad * v.x, y2 - rad * v.y);

		g.setColor(COLORS.get(e.Color)); // Color could change based on algorithm
		if(e.isDirected) Arrow.drawArrow(g, (int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		else g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		//TODO: Print weights of the edges
		if(e.isWeighted){
			mag_v = Math.abs(mag_v);
			g.setColor(Color.BLACK); // Make color black again for printing edge weight
			g.drawString(Integer.toString(e.Weight), 
					(int)(x1 + mag_v / 3.5 * v.x), (int)(y1 + mag_v / 3.5 * v.y));
		}
	}

	/*
	 * Overriden paintComponent to use Graphics2D rather than plain Graphics object
	 * */
	protected void paintComponent(Graphics2D g){
		// Set better font with variable font size based on circle radius
		g.setFont(new Font("Courier New", Font.BOLD, 24)); 
		if(ExistingGraph){ // Only paint the graph if one exists
			// Draw each edge in set
			for(int i = 0; i < edges.length; i++){
				drawEdge(g, edges[i]); 
			}
			g.setStroke(new BasicStroke(4)); // Set larger stroke for verticies
			// Draw each vertex in set
			for(int i = 0; i < verticies.length; i++){
				drawVertex(g, verticies[i]);
			}
		}
	}

	/*
	 * Generate Graph with currently selected specifications
	 * */
	private void generateNewGraph(){
		//TODO: generate a new graph based on the buttons selected
		// Create new graph with #of edges as specified in text field,
		// and properties as selected in drop down.
		graph = new Graph( (int)numVertText.getValue(), g_props);
		// Get List of Verticies from graph
		verticies = graph.get_verticies();
		// Get List of Edges from Graph
		edges = graph.get_edges();

		ExistingGraph = true; // Alert the view that the graph now exists
		// Repaint the view with the newly created graph
		repaint();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintComponent((Graphics2D) g);
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
