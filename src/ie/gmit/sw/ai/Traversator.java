package ie.gmit.sw.ai;

import ie.gmit.sw.maze.Node;

public interface Traversator {
	
	// All searching algorithms will implement this interface
	public void search(Node startNode);
	
	public void setMaze(Node[][] maze); 
	
	public void setCurrentNode(Node currentNode); public Node getCurrentNode();
	
	public void kill(boolean kill);
}
