package ie.gmit.sw.ai;

import java.util.*;
import ie.gmit.sw.game.GameView;
import ie.gmit.sw.maze.Node;
import ie.gmit.sw.maze.NodeType;

public class DepthLimitedDFS {
	private int limit;
	private Node[][] maze;
	private List<Node> nodesChecked = new ArrayList<Node>();
	// Prevents radar searches from killing enemies

	public DepthLimitedDFS(Node[][] maze, Node startNode, int limit, GameView g) {
		this.limit = limit;
		this.maze = maze;
		search(startNode, 1);
		setNodesAsUnvisited();
	}
	
	/*
	 * This search is used for bombs and radars
	 * When used with a bomb, any enemy in the limit area will be killed
	 * A radar will show any nearby enemy on the map for a limited time
	 */
	
	public void search(Node node, int depth) {
		if (depth > limit) return;
		// For each path, travel down until the depth limit is reached,
		// Killing all enemies encountered along the way
		nodesChecked.add(node);
		node.setVisited(true);	
		if (node.getNodeType() == NodeType.enemy) {

			if(node.getEnemy()!=null){
				node.getEnemy().kill();
				node.setNodeType(NodeType.ash);
			}
		}

		List<Node> children = node.getAdjacentNodes(maze);
		for (int i = 0; i < children.size(); i++) {
			Node child = children.get(i);
			if ((child.getNodeType() == NodeType.enemy) || !child.isVisited() 
					&& child.isTraversable()){
				child.setParent(node);

				depth++;
				search(child,depth);
			}
		}
	}
	
	private void setNodesAsUnvisited() {
		for (Node n : nodesChecked) n.setVisited(false);
	}
	

	public void setMaze(Node[][] maze) {
		this.maze = maze;
	}
}
