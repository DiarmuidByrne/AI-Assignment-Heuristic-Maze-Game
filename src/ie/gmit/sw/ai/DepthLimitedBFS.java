package ie.gmit.sw.ai;

import java.util.*;
import ie.gmit.sw.game.GameView;
import ie.gmit.sw.maze.Node;
import ie.gmit.sw.maze.NodeType;

public class DepthLimitedBFS {
	private int limit;
	private Node[][] maze;
	private LinkedList<Node> queue = new LinkedList<Node>();
	private List<Node> nodesChecked = new ArrayList<Node>();
	
	public DepthLimitedBFS(Node[][] maze, Node startNode, int limit, GameView g) {
		this.limit = limit;
		this.maze = maze;
		search(startNode);
		setNodesAsUnvisited();
	}
	
	/*
	 * This search is used for bombs
	 * When a bomb is picked up, any enemy in the limit area will be killed
	 */
	
	public void search(Node startNode){
		int depth = 0;
		queue.addLast(startNode);
		while(depth < limit){
			Node currentNode = queue.getFirst();

			if (currentNode.getNodeType() == NodeType.enemy){
				if (currentNode.getEnemy()!=null)
					currentNode.getEnemy().setVisible(true);
			}
			
			queue.removeFirst();
			if (depth <= limit && queue.isEmpty()) {
				getNearbyCellsByLayer(startNode, depth);
				depth++;
				continue;
			} 
		}
	}
	
	// Gets every node surrounding a given node from a specified layer
	private void getNearbyCellsByLayer(Node n, int layer) {
		int minRow = n.getRow()-layer, maxRow = n.getRow()+layer;
		int minCol = n.getCol()-layer, maxCol = n.getCol()+layer;
		
		// Prevent ArrayIndexOutOfBounds exceptions if start node is near borders
		if (minRow < 1) minRow = 1;
		if (minCol < 1) minCol = 1;
		if (maxRow > maze.length-1) maxRow = maze.length-1;
		if (maxCol > maze[0].length-1) maxCol = maze.length-1;
		
		for(int row=minRow; row<=maxRow; row++) {
			for(int col=minCol; col<=maxCol; col++) {
				// Add all unvisited nodes from rows
				if(row == minRow || row == maxRow && !maze[row][col].isVisited()) {
					queue.addLast(maze[row][col]);
					nodesChecked.add(maze[row][col]);
					maze[row][col].setVisited(true);
				}
				if(col == minCol || col == maxCol && !maze[row][col].isVisited()) {
					queue.addLast(maze[row][col]);
					nodesChecked.add(maze[row][col]);
					maze[row][col].setVisited(true);
				}
			}
		}
	}
	
	private void setNodesAsUnvisited() {
		for (Node n : nodesChecked) n.setVisited(false);
	}	
}
