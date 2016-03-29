package ie.gmit.sw;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class GameRunner implements KeyListener{
	private Node[][] model;
	private GameView view;
	private int currentRow;
	private int currentCol;
	private int mazeDimension;
	private List<Enemy> enemies = new ArrayList<Enemy>();
	
	public GameRunner () throws Exception {
		view = new GameView();
		model = view.getMaze();
    	mazeDimension = view.getMazeDimension();
    	
    	// Generate a place for the player to spawn
    	placePlayer();
    	//placeEnemies();
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE+20, GameView.DEFAULT_VIEW_SIZE+20);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
	
	private void placePlayer(){  
		boolean placed = false;
		
		while(!placed) {
	    	currentRow = (int) (mazeDimension * Math.random());
	    	currentCol = (int) (mazeDimension * Math.random());
	    	
	    	if (model[currentRow][currentCol].getNodeType() == NodeType.floor) placed = true;
		}
    	model[currentRow][currentCol].setNodeType(NodeType.player);
    	updateView(); 		
	}
	
	
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

    public void keyPressed(KeyEvent e) {
    	
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < mazeDimension - 1) {
        	if (isValidMove(currentRow, currentCol + 1)) currentCol++;   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < mazeDimension - 1) {
        	if (isValidMove(currentRow + 1, currentCol)) {
        		currentRow++;
        	}
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom();
        }else{
        	return;
        }
        
        updateView();  
        //updateEnemyPositions();
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore

    
	private boolean isValidMove(int r, int c){
		if (r <= model.length - 1 && c <= model[r].length - 1 
				&& model[r][c].getNodeType() == NodeType.floor){
			
			model[currentRow][currentCol].setNodeType(NodeType.floor);
			model[r][c].setNodeType(NodeType.player);
			return true;
		}else {
			// Check if the wall contains items/prisoners
			checkForItems(r, c);
			return false; //Can't move
		}
	}
	
	private void checkForItems(int r, int c) {
		if(model[r][c].getNodeType() == NodeType.weapon) {
			//view.getMaze().setFeature(r, c, 'X');
		}
	}
	
	public static void main(String[] args) throws Exception {
		new GameRunner();
	}
}
