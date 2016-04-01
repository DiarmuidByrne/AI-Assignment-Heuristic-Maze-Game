package ie.gmit.sw.enemy;

import ie.gmit.sw.game.GameView;
import ie.gmit.sw.maze.Node;

public interface Enemy {
	
	public void search(Node[][] maze, Node startNode, GameView g);
	
	public void setMaze(Node[][] maze); 
	
	public void setCurrentNode(Node currentNode);	public Node getCurrentNode();
	
	public void kill(boolean dead);
	
	public int getStrength();
}
