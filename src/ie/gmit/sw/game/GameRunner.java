package ie.gmit.sw.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import ie.gmit.sw.maze.*;

 /*
  * Contains the Main method to run the program
  * Also controls player movement using the Keylistener interface
  */

public class GameRunner implements KeyListener{

	private Node[][] model; 
	private GameView view;
	private int currentRow;
	private int currentCol;
	private int mazeDimension;
	
	public GameRunner () throws Exception {
 		view = new GameView();
		model = view.getMaze();
    	mazeDimension = view.getMazeDimension();
    	getInitialPosition();
    	// Generate a place for the player to spawn
    	updateView(); 		
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE-20, GameView.DEFAULT_VIEW_SIZE-20);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development)");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setBackground(Color.BLACK);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
	}
			
	private void updateView(){
		view.setCurrentPosition(currentRow, currentCol);		
	}

    public void keyPressed(KeyEvent e) {
    	if (view.isGameOver()) {
    		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        		System.exit(0);
    		}
    	} 
    	else {
    		if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < mazeDimension - 1) {
            	view.setFacingRight(true);
            	if (isValidMove(currentRow, currentCol + 1)) currentCol++;   		
            }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
            	view.setFacingRight(false);
            	if (isValidMove(currentRow, currentCol - 1)) currentCol--;	
            }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
            	if (isValidMove(currentRow - 1, currentCol)) currentRow--;
            }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < mazeDimension - 1) {
            	if (isValidMove(currentRow + 1, currentCol)) {
            		currentRow++;
            	}
            }else if (e.getKeyCode() == KeyEvent.VK_Z){
            	view.toggleZoom();
            }
            else{
            	return;
            }
            
            updateView();
    	}
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore

    
	private boolean isValidMove(int r, int c){
		if (r <= model.length - 1 && c <= model[r].length - 1 
				&& (model[r][c].getNodeType() == NodeType.floor
				|| model[r][c].getNodeType() == NodeType.path
				|| model[r][c].getNodeType() == NodeType.enemy
				|| model[r][c].getNodeType() == NodeType.ash)){
			
			// If enemy and player meet, initialize a fight
			if (model[r][c].getNodeType() == NodeType.enemy) view.initializeFight(model[r][c]);			
			model[currentRow][currentCol].setNodeType(NodeType.floor);
			model[r][c].setNodeType(NodeType.player);
			view.repaint();
			return true;
		}else {
			// Check if the wall contains items/prisoners
			checkForItems(r, c);
			return false; //Can't move
		}
	}
	
	public void getInitialPosition() {
		for(int row=0; row<model.length; row++) {
			for (int col=0; col<model[0].length; col++) {
				if(model[row][col].getNodeType() == NodeType.player) {
					currentRow = row; currentCol = col;
				}
			}
		}
	}
	
	private void checkForItems(int r, int c) {
		Node n = model[r][c];
		
		if(n.containsItem()) {
			 view.activateItem(n);
			n.setNodeType(NodeType.wall);
		} else if (n.isGoalNode()) view.finishGame();
	}
	
	public static void main(String[] args) throws Exception {
		new GameRunner();
	}
}
