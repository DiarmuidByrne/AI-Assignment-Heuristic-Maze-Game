package ie.gmit.sw.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

import ie.gmit.sw.enemy.*;
import ie.gmit.sw.maze.*;
import ie.gmit.sw.player.*;

public class GameView extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;	
	private static final int IMAGE_COUNT = 10;
	private static final int MAZE_DIMENSION = 60;
	private MazeGenerator m = new MazeGenerator(MAZE_DIMENSION, MAZE_DIMENSION);
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	private BufferedImage[] images;
	private int enemy_state = 5;
	private int currentRow;
	private int currentCol;
	private Timer timer;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private Map<Node, Item> items = new HashMap<Node, Item>();
	private Player p;
	private Node goalNode;
	private boolean hintActive = false;
	private Timer hintTimer;


	
	public GameView () throws Exception {
		maze = m.getMaze();
		placePlayer();
    	initializeEntities();
		init();
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	private void placePlayer(){  
		boolean placed = false;
		while(!placed) {
	    	currentRow = (int) (MAZE_DIMENSION * Math.random());
	    	currentCol = (int) (MAZE_DIMENSION * Math.random());
	    	
	    	if (maze[currentRow][currentCol].getNodeType() == NodeType.floor) placed = true;
		}
    	maze[currentRow][currentCol].setNodeType(NodeType.player);
	}
	
	public void setCurrentPosition(int row, int col) {
		
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze[currentRow].length - 1) - cellpadding){
			currentCol = (maze[currentRow].length - 1) - cellpadding;
		}else{
			currentCol = col;
		}
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.length - 1) - cellpadding){
			currentRow = (maze.length - 1) - cellpadding;
		}else{
			currentRow = row;
		}
		p.setCurrentNode(maze[row][col]);
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
              
        cellspan = zoomOut ? maze.length : 5;         
        int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		NodeType ch = NodeType.wall;
       		
        		if (zoomOut){
        			size = DEFAULT_VIEW_SIZE/cellspan;
        			ch = maze[row][col].getNodeType();
        			// Display character on map
        			if (row == currentRow && col == currentCol){
        				g2.setColor(Color.WHITE);
        				g2.fillRect(x1, y1, size, size);
        				continue;
        			} else if (maze[row][col].getNodeType() == NodeType.enemy) {
        				g2.setColor(Color.RED);
        				g2.fillRect(x1, y1, size, size);
        			} else if (maze[row][col].getNodeType() == NodeType.goal) {
        				g2.setColor(Color.YELLOW);
        				g2.fillRect(x1, y1, size, size);
        			} else if (maze[row][col].getNodeType() == NodeType.hint) {
        				g2.setColor(Color.BLUE);
        				g2.fillRect(x1, y1, size, size);

        			}
        		}else{
        			ch = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getNodeType();
        		}
        		
        		
        		if (ch == NodeType.wall){        			
        			imageIndex = 0;;
        		}else if (ch == NodeType.weapon){
        			imageIndex = 1;;
        		}else if (ch == NodeType.hint){
        			imageIndex = 2;;
        		}else if (ch == NodeType.bomb){
        			imageIndex = 3;;
        		}else if (ch == NodeType.hBomb){
        			imageIndex = 4;;
        		}else if (ch == NodeType.enemy){
        			imageIndex = enemy_state;;       			
        		}else if (ch == NodeType.player) {
        			imageIndex = 7;
        		} else if (ch == NodeType.path) {
        			imageIndex = 8;        		
        		}else if (ch == NodeType.goal) {
        			imageIndex = 9;
        		}else{
        			imageIndex = -1;
        		}
        		
        		if (imageIndex >= 0){
        			g2.drawImage(images[imageIndex], x1, y1, null);
        		}
        		 else{
        			g2.setColor(Color.LIGHT_GRAY);
        			g2.fillRect(x1, y1, size, size);
        		}      		
        	}
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;	
	}
	
	private void initializeEntities() throws Exception {
		for(int row=0; row<maze.length; row++) {
			for(int col=0; col<maze[0].length; col++) {
				// Initialize enemy objects
				Node n = maze[row][col];
				if (n.getNodeType() == NodeType.enemy) {
					Enemy e = new EnemyImpl(maze, n, this);
					e.setCurrentNode(n);
					enemies.add(e);
				}
				// Initialize Player Object
				else if (n.getNodeType() == NodeType.player) {
			    	p = new Player(n);
				}
				else if (n.getNodeType() == NodeType.goal) {
					goalNode = n;
					goalNode.setGoalNode(true);
				}
				// Initialize item objects
				else if (n.containsItem()) {
					Item i = new Item(n);
					items.put(n, i);
				}
			}
		}
	}
	
	public void updateEnemyPositions(Node current, Node next) {
		current.setNodeType(NodeType.floor);
		next.setNodeType(NodeType.enemy);
		
	}

	public void actionPerformed(ActionEvent e) {
		
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}else{
			enemy_state = 5;
		}
		
		for (Enemy enemy : enemies) {
			enemy.setMaze(maze);
		}
		repaint();
	}
	
	// Retrieves a list of Nodes from the Hints traverser class and displays a path to the goal
	public void showPath(List<Node> path) {
		
//		if (!hintTimer.isRunning()) {
			for(Node node : path) {
				if (node.getNodeType() != NodeType.player &&
						node.getNodeType() != NodeType.enemy && node.getNodeType()!= NodeType.goal) 
					node.setNodeType(NodeType.path);
			}
//			new TimerTask() {	
//				public void run() {
					hidePath();
//				}
//			};
//		}
		hintActive = false;
	}
	
	public void hidePath() {
		
	}
	
	public void initializeFight(Node enemyNode) {
		Enemy toFight = null;
		for(Enemy e : enemies) {
			if (e.getCurrentNode() == enemyNode) toFight = e;
		}
		int damage = new Fight().evaluateFight(toFight, p.getWeaponDurability());
		p.setWeaponDurability(p.getWeaponDurability()-5);
		p.setHealth(p.getHealth() - damage);
		System.out.println("Player health: " + p.getHealth());
	}
	
	public void activateItem(Node n) {
		if (n.getNodeType() == NodeType.weapon) p.addWeapon();
		Item i = items.get(n);
	    i.activateItem(maze, p.getCurrentNode(), goalNode, this);
	}
	
	private void init() throws Exception{
		images = new BufferedImage[IMAGE_COUNT];
		images[0] = ImageIO.read(new java.io.File("src/Resources/hedge.png"));
//		images[0] = ImageIO.read(new java.io.File("src/Resources/Eoghan.png"));
		images[1] = ImageIO.read(new java.io.File("src/Resources/sword.png"));		
		images[2] = ImageIO.read(new java.io.File("src/Resources/help.png"));
		images[3] = ImageIO.read(new java.io.File("src/Resources/bomb.png"));
		images[4] = ImageIO.read(new java.io.File("src/Resources/h_bomb.png"));
		images[5] = ImageIO.read(new java.io.File("src/Resources/spider_down.png"));
		images[6] = ImageIO.read(new java.io.File("src/Resources/spider_up.png"));
		images[7] = ImageIO.read(new java.io.File("src/Resources/player.png"));
		images[8] = ImageIO.read(new java.io.File("src/Resources/hint.png"));
		images[9] = ImageIO.read(new java.io.File("src/Resources/Goal.png"));
	}
	
	public MazeGenerator getMazeGenerator() {
		return m;
	}
	
	public Node[][] getMaze(){
		return m.getMaze();
	}
	
	public void setMaze(Node[][] maze) {
		m.setMaze(maze);
	}
	
	public void setHintActive(boolean active) {
		hintActive = active;
	}
	
	public int getMazeDimension() {
		return MAZE_DIMENSION;
	}
}
