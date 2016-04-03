package ie.gmit.sw.enemy;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
	private int strength;
	private Traversator t;
	private Random r = new Random(); 
	private boolean visible = false;
	private static final int MIN_STRENGTH = 3, MAX_STRENGTH = 8;
	
	public EnemyImpl(Node[][] maze, Node startNode, GameView g) throws Exception  {
		search(maze, startNode, g);
		strength = ThreadLocalRandom.current().nextInt(MIN_STRENGTH, MAX_STRENGTH + 1);
	}
	
	public void search(Node[][] maze, Node startNode, GameView g) {
		// Randomize search algorithm
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

	public void kill() {
		t.setFinished(true);
	}
	
	public int getStrength() {
		return strength;
	}

	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
