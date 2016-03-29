package ie.gmit.sw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;	
	private static final int IMAGE_COUNT = 8;
	private static final int MAZE_DIMENSION = 40;
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
	
	public GameView () throws Exception {
		maze = m.getMaze();
		init();

		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze[currentRow].length - 1) - cellpadding){
			currentCol = (maze[currentRow].length - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.length - 1) - cellpadding){
			currentRow = (maze.length - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
              
        cellspan = zoomOut ? maze.length : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		NodeType ch = NodeType.wall;
       		
        		if (zoomOut){
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
        		// Change enemy sprites to allow animation
        		}else if (ch == NodeType.enemy){
        			imageIndex = enemy_state;;       			
        		}else if (ch == NodeType.player) {
        			imageIndex = 7;
        		} else{
        			imageIndex = -1;
        		}
        		
        		if (imageIndex >= 0){
        			g2.drawImage(images[imageIndex], x1, y1, null);
        		}else if (maze[row][col].getNodeType() == NodeType.goal) {
        			g2.setColor(Color.YELLOW);
        			g2.fillRect(x1, y1, size, size);

        		} else{
        			g2.setColor(Color.LIGHT_GRAY);
        			g2.fillRect(x1, y1, size, size);
        		}      		
        	}
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;	
	}

	public void actionPerformed(ActionEvent e) {	
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}else{
			enemy_state = 5;
		}
		this.repaint();
	}
	
	private void init() throws Exception{
		images = new BufferedImage[IMAGE_COUNT];
		images[0] = ImageIO.read(new java.io.File("src/Resources/hedge.png"));
		images[1] = ImageIO.read(new java.io.File("src/Resources/sword.png"));		
		images[2] = ImageIO.read(new java.io.File("src/Resources/help.png"));
		images[3] = ImageIO.read(new java.io.File("src/Resources/bomb.png"));
		images[4] = ImageIO.read(new java.io.File("src/Resources/h_bomb.png"));
		images[5] = ImageIO.read(new java.io.File("src/Resources/spider_down.png"));
		images[6] = ImageIO.read(new java.io.File("src/Resources/spider_up.png"));
		images[7] = ImageIO.read(new java.io.File("src/Resources/player.png"));
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
	
	public int getMazeDimension() {
		return MAZE_DIMENSION;
	}

}
