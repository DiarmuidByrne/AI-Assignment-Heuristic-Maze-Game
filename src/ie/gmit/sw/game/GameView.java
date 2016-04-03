package ie.gmit.sw.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
	private static final int IMAGE_COUNT = 21;
	private static final int MAZE_DIMENSION = 60;
	private MazeGenerator m = new MazeGenerator(MAZE_DIMENSION, MAZE_DIMENSION);
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	private BufferedImage[] images;
	private int enemy_state = 5, playerState = 5;
	private int currentRow;
	private int currentCol;
	private Timer timer;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private int spriteIndex = 10;
	private List<Enemy> enemies = new ArrayList<Enemy>();
	private Map<Node, Item> items = new HashMap<Node, Item>();
	private Player p;
	private List<Node> path = new ArrayList<Node>();
	private Node goalNode;
	private boolean hintActive = false;
	private HeadsUpDisplay hud = new HeadsUpDisplay(DEFAULT_VIEW_SIZE);


	
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

	public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        cellspan = zoomOut ? maze.length : 5;         
        int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, DEFAULT_VIEW_SIZE, DEFAULT_VIEW_SIZE);

        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		NodeType ch = NodeType.wall;
       		
        		if (zoomOut){
        			size = DEFAULT_VIEW_SIZE/cellspan;
        			Node n = maze[row][col];
        			ch = n.getNodeType();
        			hud.showMap(g2, n, size, x1, y1, p);

        		}else{
        			ch = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getNodeType();
	
	        		if (ch == NodeType.wall){        			
	        			imageIndex = 0;;
	        		}else if (ch == NodeType.weapon){
	        			imageIndex = 1;;
	        		}else if (ch == NodeType.hint){
	        			imageIndex = 2;;
	        		}else if (ch == NodeType.bomb){
	        			imageIndex = 3;;
	        		}else if (ch == NodeType.radar){
	        			imageIndex = 4;;
	        		}else if (ch == NodeType.enemy){
	        			imageIndex = enemy_state;;       			
	        		}else if (ch == NodeType.player) {
	        			imageIndex = spriteIndex;
	        		} else if (ch == NodeType.path) {
	        			imageIndex = 7;        		
	        		}else if (ch == NodeType.goal) {
	        			imageIndex = 8;
	        		}else if (ch == NodeType.key){
	        			imageIndex = 19;
	        		} else if (ch == NodeType.ash) {
	        			imageIndex = 20;
	        		} else {
	        			imageIndex = -1;
	        		}
	        		
	        		if (imageIndex >= 0){
	        			g2.drawImage(images[imageIndex], x1, y1, null);
	        		}
        		}
        	}
        }
        if (!zoomOut) hud.showHealth(p, g2);

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
					n.setEnemy(e);
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
		
		if (p.isFacingRight()) {
			if (playerState == 9) playerState = 10;
			else if (playerState == 10) playerState = 11;
			else playerState = 9;
		} else {
			if (playerState == 14) playerState = 15;
			else if (playerState == 15) playerState = 16;
			else playerState = 14;
		}
		spriteIndex = p.getPlayerState(maze, playerState);

		for (Enemy enemy : enemies) {
			enemy.setMaze(maze);
		}
		if (p.getStepCount() > 50) {
			hintActive = false;
			hidePath();
		}
		repaint();
	}
	
	public void setFacingRight(boolean facingRight) {
		p.setFacingRight(facingRight);
	}
	
	// Retrieves a list of Nodes from the Hints traverser class and displays a path to the goal
	public void showPath(List<Node> path) {
		this.path = path;
		if (hintActive) p.setStepCount(0); 
		else hud.showPath(p, path);
	}
	
	public void hidePath() {
		hud.hidePath(path);
	}
	
	public void initializeFight(Node enemyNode) {
		Enemy toFight = null;
		for(Enemy e : enemies) {
			if (e.getCurrentNode() == enemyNode) toFight = e;
		}
		int damage = new Fight().evaluateFight(toFight, p.getWeaponDurability());
		p.setWeaponDurability(p.getWeaponDurability()-5);
		p.setHealth(p.getHealth() - damage);
	}
	
	public void activateItem(Node n) {
		if (n.getNodeType() == NodeType.weapon) p.addWeapon();
		Item i = items.get(n);
	    i.activateItem(maze, p.getCurrentNode(), goalNode, this);
	}
	
	private void init() throws Exception{
		images = new BufferedImage[IMAGE_COUNT];
		images[0] = ImageIO.read(new java.io.File("Resources/hedge.png"));
		images[1] = ImageIO.read(new java.io.File("Resources/sword.png"));		
		images[2] = ImageIO.read(new java.io.File("Resources/help.png"));
		images[3] = ImageIO.read(new java.io.File("Resources/bomb.png"));
		images[4] = ImageIO.read(new java.io.File("Resources/radar.png"));
		images[5] = ImageIO.read(new java.io.File("Resources/spider_down.png"));
		images[6] = ImageIO.read(new java.io.File("Resources/spider_up.png"));
		images[7] = ImageIO.read(new java.io.File("Resources/hint.png"));
		images[8] = ImageIO.read(new java.io.File("Resources/Goal.png"));
		images[9] = ImageIO.read(new java.io.File("Resources/Player/Idle_1.png"));
		images[10] = ImageIO.read(new java.io.File("Resources/Player/Idle_1.png"));
		images[11] = ImageIO.read(new java.io.File("Resources/Player/Idle_2.png"));
		images[12] = ImageIO.read(new java.io.File("Resources/Player/Sword_Idle.png"));
		images[13] = ImageIO.read(new java.io.File("Resources/Player/Scared.png"));
		images[14] = ImageIO.read(new java.io.File("Resources/Player/L_Idle_1.png"));
		images[15] = ImageIO.read(new java.io.File("Resources/Player/L_Idle_1.png"));
		images[16] = ImageIO.read(new java.io.File("Resources/Player/L_Idle_2.png"));
		images[17] = ImageIO.read(new java.io.File("Resources/Player/L_Sword_Idle.png"));
		images[18] = ImageIO.read(new java.io.File("Resources/Player/L_Scared.png"));
		images[19] = ImageIO.read(new java.io.File("Resources/key.png"));
		images[20] = ImageIO.read(new java.io.File("Resources/ash_pile.png"));
	}

	
	public MazeGenerator getMazeGenerator() {
		return m;
	}
	
	public Node[][] getMaze(){
		return m.getMaze();
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public void setMaze(Node[][] maze) {
		m.setMaze(maze);
	}
	
	public int getMazeDimension() {
		return MAZE_DIMENSION;
	}
}
