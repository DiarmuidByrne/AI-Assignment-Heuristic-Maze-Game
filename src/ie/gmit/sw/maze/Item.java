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
	
	public void activateItem(Node[][] maze, Node startNode, Node goal, GameView g) {
		boolean isBomb;
		if (node.getNodeType() == NodeType.hint) {
//			new GreedyBestFirstSearch(maze, startNode, goal, g);
			new AStarSearch(maze, startNode, goal, g);
		} else if(node.getNodeType() == NodeType.bomb) {
			isBomb = true;
			new DepthLimitedDFS(maze, isBomb, startNode, GRENADE_DEPTH_LIMIT, g);
		}
		else if (node.getNodeType() == NodeType.radar) {
			isBomb = false;
			new DepthLimitedBFS(maze, isBomb, startNode, RADAR_DEPTH_LIMIT, g);
		}
	}
}