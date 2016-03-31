package ie.gmit.sw.player;

import ie.gmit.sw.maze.*;

public class Player {
	Node currentNode;
	
	public Player(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}
}
