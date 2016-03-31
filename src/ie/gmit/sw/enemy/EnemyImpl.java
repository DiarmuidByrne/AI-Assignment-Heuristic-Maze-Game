package ie.gmit.sw.enemy;

import java.util.Random;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.game.GameView;
import ie.gmit.sw.maze.Node;

public class EnemyImpl implements Enemy {
	
	/* Enemy Object Class
	 * Every Node that contains an enemy type will have
	 * an independantly threaded Enemy Object 
	 * 
	 * All encapsulated methods here delegate 
	 * to the appropriate Traversator implementation
	 */
	
	private Traversator t;
	private Random r = new Random(); 
	
	public EnemyImpl(Node[][] maze, Node startNode, GameView g) throws Exception  {
		search(maze, startNode, g);
	}
	
	public void search(Node[][] maze, Node startNode, GameView g) {
		int i = r.nextInt(1);
		
		if(i == 0) t = new RandomWalk(maze, startNode, g); 
	}
	
	public void setMaze(Node[][] maze) {
		t.setMaze(maze);
	}
	
	public Node getCurrentNode() {
		return t.getCurrentNode();
	}
	
	public void setCurrentNode(Node currentNode) {
		t.setCurrentNode(currentNode);
	}

	public void kill(boolean kill) {
		t.kill(kill);
	}
}
