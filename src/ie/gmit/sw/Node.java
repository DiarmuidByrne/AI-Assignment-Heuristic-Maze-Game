package ie.gmit.sw;

import java.util.*;

public class Node {
	// Each block in the maze is represented as a node.
	// The node can hold every component of the game, from
	// npc's to the player to weapons
	private NodeType nodeType;
	private int row, col;
	private Enemy e;
	private boolean visited = false;
	private boolean isGoalNode = false;
	private Set<Node> nodeSet;
	private boolean startingCell = false;
	
	public Node() {
		nodeSet = new HashSet<Node>();
		nodeSet.add(this);
	}

	
	public boolean isStartingCell() {
		return startingCell;
	}
	
	public void setStartingCell(boolean startingCell) {
		this.startingCell = startingCell;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public boolean isGoalNode() {
		return isGoalNode;
	}
	public void setGoalNode(boolean isGoalNode) {
		this.isGoalNode = isGoalNode;
	}
	
	
	// Returns true if the Node isn't a border Node
	public boolean isInBoundaries(Node[][] maze) {
		return row > 0 && col > 0 
				&& row < maze.length-1 && col < maze[0].length-1;
	}
	
	public List<Node> getAdjacentNodes(Node[][] maze) {
		List<Node> adjacentNodes = new ArrayList<Node>();
		
		if(row-1 > 0) adjacentNodes.add(maze[row-1][col]); // Node Above
		if(row+1 < maze.length-1) adjacentNodes.add(maze[row+1][col]); // Node Below
		if(col-1 > 0) adjacentNodes.add(maze[row][col-1]); // Node to left
		if(col+1 < maze[0].length-1) adjacentNodes.add(maze[row][col+1]); // Node to right
		return adjacentNodes; 
	}
	
	public boolean isInBorder(Node[][] maze) {
		// Returns true if Node is inside the outer border
		return row > 0 && row < maze.length-1 &&
				col > 0 && col < maze[0].length-1;
	}
	
	public void addNodeToSet(Node n) {
		nodeSet.add(n);
	}
	
	public NodeType getNodeType() {
		return nodeType;
	}
	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public Set<Node> getNodeSet() {
		return nodeSet;
	}
	public void setNodeSet(Set<Node> nodeSet) {
		this.nodeSet = nodeSet;
	}	
	
}