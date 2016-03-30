package ie.gmit.sw;

public interface Enemy {
	
	public void search(Node[][] maze, Node startNode, GameView g);
	
	public void setMaze(Node[][] maze); 
	
	public void setCurrentNode(Node currentNode);	public Node getCurrentNode();
	public void setNextNode(Node nextNode); public Node getNextNode();
}
