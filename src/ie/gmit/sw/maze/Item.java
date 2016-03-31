package ie.gmit.sw.maze;

import ie.gmit.sw.ai.AStarSearch;
import ie.gmit.sw.game.GameView;

public class Item {
	private Node node;

	public Item(Node node) {
		this.node = node;
	}
	
	public void activateItem(Node[][] maze, Node startNode, Node goal, GameView g) {
		if (node.getNodeType() == NodeType.hint) {
			new AStarSearch(maze, startNode, goal, g);
		}
	}
}