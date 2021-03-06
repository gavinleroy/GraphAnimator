/*
 * GRAPH ANIMATOR
 * 	This project animates popular Graph algorithms for educational purposes
 * Copyright (c) Gavin Gray May, 2020
 * */

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

import java.util.*;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DynamicGraph.*;
import DynamicGraph.Point;
import DynamicGraph.Algorithm;

public class GraphAlgorithmAnimate extends JPanel {
	// DIMENSION / MATH VARIABLES
	private static int DIM_W = 1200;
	private static int DIM_H = 700;
	private static int DIAMETER = 40;
	private static int W_OFF;// = DIM_W - DIAMETER * 2;
	private static int H_OFF;// = DIM_H - DIAMETER * 3;
	private static int X_OFF = 20;
	private static int Y_OFF = 40 + DIAMETER/2;
	private static final int TIMER_MAX = 1000;
	// GUI ITEMS
	private ButtonState buttonstate;
	private JSlider timerSpeed;
	private JLabel sourceLabel;
	private JMenuBar menuBar;
	// Algorithms:
	private Algorithm algorithm;
	private String[] algorithmList = { Algorithm.BFS, 
					   Algorithm.DFS, 
					   Algorithm.DIJKSTRAS, 
					   Algorithm.KRUSKALS, 
					   Algorithm.PRIMS };
	private JComboBox algorithmMenu;

	private JMenu propertiesMenu; // Property Menu Items
	private JCheckBoxMenuItem completeMenuItem;
	private JCheckBoxMenuItem directedMenuItem; 
	private JCheckBoxMenuItem weightedMenuItem; 
	private JCheckBoxMenuItem negativeMenuItem; 
	private JCheckBoxMenuItem acyclicMenuItem;	
	private JButton startButton; // Utility Buttons
	private JButton genGraphButton;
	private JButton pauseButton;
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

		sourceLabel = new JLabel("A");
		
		menuBar = new JMenuBar(); // Menu Bar
		g_props = new HashSet<Graph.Property>();

		// Property menu and items START ---> 
		propertiesMenu = new JMenu("Properties");
		menuBar.add(propertiesMenu); 

		completeMenuItem = new JCheckBoxMenuItem("Complete");
		completeMenuItem.addActionListener(new ActionListener() {         
			public void actionPerformed(ActionEvent e) {
				propertiesMenu.doClick();
				if(completeMenuItem.isSelected()) g_props.add(Graph.Property.CONNECTED);
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
		propertiesMenu.add(completeMenuItem);
		// Property menu and items END <--- 


		// Algorithms menu and items START --->
		algorithmMenu = new JComboBox(algorithmList);
		algorithmMenu.setSelectedIndex(0);
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


		// TIMER START --->
		timer = new Timer(TIMER_MAX / 2, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(algorithm.isDone()){
					buttonstate.init();
				}else{
					algorithm.step();
					repaint();
				}
			}
		});
		// TIMER END <---

		// SPEED SLIDER START --->
		timerSpeed = new JSlider(0, TIMER_MAX - 10);
		timerSpeed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int offset = ((JSlider)e.getSource()).getValue();
				timer.setDelay(TIMER_MAX - offset);
			}
		});
		// SPEED SLIDER END <---

		// BUTTONS START --->
		buttonstate = new ButtonState();

		startButton = new JButton();
		startButton.addActionListener(buttonstate); 		

		pauseButton = new JButton();
		pauseButton.addActionListener(buttonstate);

		genGraphButton = new JButton("Generate Graph");
		genGraphButton.addActionListener(buttonstate);
		// BUTTONS END <---

		// MOUSE EVENTS START --->
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				if(ExistingGraph){
					int closest = findClosestVertex(e.getX(), e.getY());
					if(closest >= 0){
						String newLabel = Character.toString((char)(closest+'A'));
						sourceLabel.setText(newLabel);
					}
				}
			}	
		});
		// MOUSE EVENTS END <---

		// DIMENSION RESIZING --->
		addComponentListener(new ComponentAdapter(){
			@Override
			public void componentResized(ComponentEvent e){
				setDimension(e.getComponent().getWidth(), e.getComponent().getHeight());
			}
		});
		// DIMENSION RESIZING END <---
		
		buttonstate.init();
		// Add all items to Frame --->
		add(menuBar);
		add(algorithmMenu);
		add(new JLabel(" V:"));
		add(numVertText);
		add(genGraphButton);
		add(startButton);
		add(pauseButton);
		add(timerSpeed);
		add(new JLabel("Starting Vertex: "));
		add(sourceLabel);
	}

	/*
	 * Method to find the Vertex which is within RADIUS distance from the given x,y
	 * */
	private int findClosestVertex(int x1, int y1){
		for(int i = 0; i < verticies.length; i++){
			int x2 = (int)(verticies[i].Location.x * W_OFF) + X_OFF + DIAMETER / 2;
			int y2 = (int)(verticies[i].Location.y * H_OFF) + Y_OFF + DIAMETER / 2;
			if(Math.sqrt((Math.pow(x1-x2, 2)) + (Math.pow(y1-y2,2))) <= DIAMETER / 2){
				return i;
			}
		}
		return -1;
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
		g.setColor(Colors.GetColor(v.Color));
		g.drawOval(x, y, DIAMETER, DIAMETER);
		g.drawString( Character.toString(((char)(v.ID + 'A'))), x + off - 8, y + off + 8);
		if((String)(algorithmMenu).getSelectedItem() == Algorithm.DIJKSTRAS || 
		(String)(algorithmMenu).getSelectedItem() == Algorithm.BFS ){ 
			String d = (v.Distance == Integer.MAX_VALUE) ? "INF" : Integer.toString(v.Distance);
			g.drawString(d, x, y - 2);
		}
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
	 * P3 = P{0|1} +- (c * u) where c is distance away from point
	 * */
	public void drawEdge(Graphics2D g, Edge e) {
		int rad = DIAMETER / 2;
		int x1 = (int)(verticies[e.Begin].Location.x * W_OFF) + rad + X_OFF;
		int y1 = (int)(verticies[e.Begin].Location.y * H_OFF) + rad + Y_OFF;
		int x2 = (int)(verticies[e.End].Location.x * W_OFF) + rad + X_OFF;
		int y2 = (int)(verticies[e.End].Location.y * H_OFF) + rad + Y_OFF;
		Point v = new Point(x2 - x1, y2 - y1);
		double mag_v = Math.sqrt(v.x * v.x + v.y * v.y);
		v.x /= mag_v; // Scale v to be a normal vector
		v.y /= mag_v;
		// Calculate new point p1
		Point p1 = new Point(x1 + rad * v.x, y1 + rad * v.y);
		// Calculate new point p2
		Point p2 = new Point(x2 - rad * v.x, y2 - rad * v.y);

		g.setColor(Colors.GetColor(e.Color)); // Color could change based on algorithm
		// If graph is directed draw an Arrow instead
		if(e.isDirected) Arrow.drawArrow(g, (int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		else g.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
		// If graph is weighted then we need to print the weight as well
		if(e.isWeighted){
			mag_v = Math.abs(mag_v);
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
			g.setStroke(new BasicStroke(2));
			for(int i = 0; i < edges.length; i++){
				if(edges[i].Color != Colors.TREEEDGE) drawEdge(g, edges[i]); 
			}
			for(int i = 0; i < edges.length; i++){
				if(edges[i].Color == Colors.TREEEDGE) drawEdge(g, edges[i]); 
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
		// Create new graph with # of edges as specified in text field,
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

	private void setDimension(int width, int height){
		DIM_W = width;
		DIM_H = height;
		W_OFF = DIM_W - DIAMETER * 2;
		H_OFF = DIM_H - DIAMETER * 3;
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

	/*
	 * MAIN
	 * */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Graph Animator");
				frame.add(new GraphAlgorithmAnimate());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	/*
	 * Object to handle the state of the two buttons available on the GUI
	 * this allows the reduction of several buttons into two and cleanly represent
	 * the values as states.
	 * The three main states are:
	 * 0) Before algorithm is started, giving the options to 
	 * 	start the algorithm or reset the graph
	 * 1) While the algorithm is running on a timer, the only
	 * 	option is to pause the animation.
	 * 2) While the animation is paused, the options are to
	 * 	resume the running timer or take a single manual step
	 * */
	protected class ButtonState implements ActionListener{
		private int state;
		public ButtonState(){ }
		public void init(){
			setState0();
		}
		public void actionPerformed(ActionEvent e){
			if((JButton)e.getSource() == genGraphButton){ 
				generateNewGraph();
				setState0();
			}else if(ExistingGraph){
				pressed((JButton)e.getSource());
			}
		}
		public void pressed(JButton b){
			switch(state){
				case 0:
					if(b==startButton){ 
						String algostring = (String)(algorithmMenu).getSelectedItem();
						int sourceVal = (int)sourceLabel.getText().charAt(0) - (int)'A';
						algorithm = graph.createAlgorithm(algostring, sourceVal);
						setState1();
					}else if(b==pauseButton){ 
						graph.reset();
						repaint();
					}
					break;
				case 1:
					if(b==pauseButton) setState2();
					break;
				case 2:
					if(b==startButton) setState1();
					else if(b==pauseButton){ 
						algorithm.step();
						repaint();
						if(algorithm.isDone()) setState0();
					}
					break;
			}
		}
		private void setState0(){
			state = 0;
			timer.stop();
			startButton.setText("Start");
			startButton.setEnabled(true);
			pauseButton.setText("Reset");
			pauseButton.setEnabled(true);
		}
		private void setState1(){
			state = 1;
			timer.start();
			startButton.setText("Resume");
			startButton.setEnabled(false);
			pauseButton.setText("Pause");
			pauseButton.setEnabled(true);
		}
		private void setState2(){
			state = 2;
			timer.stop();
			startButton.setText("Resume");
			startButton.setEnabled(true);
			pauseButton.setText("Step");
			pauseButton.setEnabled(true);
		}
	}
}
