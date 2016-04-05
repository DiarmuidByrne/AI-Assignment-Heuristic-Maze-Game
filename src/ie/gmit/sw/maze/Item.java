package ie.gmit.sw.maze;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.game.*;

public class Item {
	private Node node;
	private static final int GRENADE_DEPTH_LIMIT = 10;
	private static final int RADAR_DEPTH_LIMIT = 8;

	public Item(Node node) {
		this.node = node;
	}
	
	/*
	 * Activates the items that were picked up by the player
	 * Items include a bomb that uses a depth-limited DFS to kill enemies within a radius
	 * a radar shows all enemies on the map within a limited radius using a depth-limited BFS
	 * The key object allows the player to exit the map when it reaches the final goal node
	 */
	
	public void activateItem(Node[][] maze, Node startNode, Node goal, GameView g) {
		if (node.getNodeType() == NodeType.hint) {
			new AStarSearch(maze, startNode, goal, g);
			
		} else if(node.getNodeType() == NodeType.bomb) {
			new DepthLimitedDFS(maze, startNode, GRENADE_DEPTH_LIMIT, g);
		}
		else if (node.getNodeType() == NodeType.radar) {
			new DepthLimitedBFS(maze, startNode, RADAR_DEPTH_LIMIT, g);
		} else if (node.getNodeType() == NodeType.key) {
			g.getPlayer().giveKey();
		}
	}
}