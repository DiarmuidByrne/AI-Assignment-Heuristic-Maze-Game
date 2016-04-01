package ie.gmit.sw.ai;

import java.util.*;

import ie.gmit.sw.game.*;
import ie.gmit.sw.maze.Node;
import ie.gmit.sw.maze.NodeType;

public class RecursiveBacktracker  implements Generator{
	private Random r = new Random();
	private Node[][] maze;
	private Set<Node> startingCells = new HashSet<Node>();
	/*
	 * Implementation of a recursive backtracker algorithm
	 * Unlike Eller's algorithm, this produces a much windier solution,
	 * With a high river percentage and low dead-end percentage
	 */
	public RecursiveBacktracker(Node[][] maze) {
		super();
		this.maze = maze;
		buildMaze();
	}
	
	// Initialize each Node
	// Starting off the maze in a grid pattern
	private void init(){
		int rowCounter = 0; int colCounter =0;
		int row = 0; int col = 0;
		for (row = 0; row < maze.length; row++){
			rowCounter++;
			for (col = 0; col < maze[row].length; col++){
				maze[row][col] = new Node();
				maze[row][col].setRow(row); maze[row][col].setCol(col);
				colCounter++;
				if(row < 1 || col < 1 
						|| row > maze.length-1 
						|| col > maze[0].length-1) maze[row][col].setNodeType(NodeType.wall);
				else if(colCounter % 2 == 0 && rowCounter % 2 == 0)  {
					maze[row][col].setNodeType(NodeType.floor);
					maze[row][col].setStartingCell(true);
					startingCells.add(maze[row][col]);
					colCounter = 0;
				} else {
					maze[row][col].setNodeType(NodeType.wall);
				}
			}
		}
	}
	
	public void buildMaze() {
		init();
		
		Stack<Node> nodeStack = new Stack<Node>();
		List<Node> unvisitedNeighbours = new ArrayList<Node>();
		int randIndex = r.nextInt(startingCells.size());
		int i = 0;
		// Initial Node
		Node startingNode = new Node();
		
		for(Node cell : startingCells) {
			if (i == randIndex) startingNode = cell;
			else i++;
		}
		
		Node currentNode = startingNode;
		do {
			currentNode.setVisited(true);
			
			List<Node> adjacentNodes = currentNode.getAdjacentCells(maze);
			// Get a list of each unvisited neighbouring cell
			for (Node node : adjacentNodes) {
				if(!node.isVisited()) {
					unvisitedNeighbours.add(node);
				}
			}
			if (unvisitedNeighbours.size() > 0) {
				int nextCellIndex = r.nextInt(unvisitedNeighbours.size());
				Node nextNode = unvisitedNeighbours.get(nextCellIndex);
				nodeStack.push(currentNode);
				
				Node wallNode = getWall(currentNode, nextNode);
				wallNode.setNodeType(NodeType.floor);
				currentNode = nextNode;
				unvisitedNeighbours.clear();
			} else if (!nodeStack.isEmpty()) {
				currentNode = nodeStack.pop();
			}
		}
		while (currentNode != startingNode && !nodeStack.isEmpty());
	}
	
	private Node getWall(Node n1, Node n2) {
		Node wall;
		
		if (n1.getRow() == n2.getRow()) {
			if (n1.getCol() < n2.getCol())
				wall = maze[n2.getRow()][n2.getCol()-1];
			else 
				wall = maze[n2.getRow()][n2.getCol()+1];
			
		} else {
			if (n1.getRow() < n2.getRow()) 
				wall = maze[n2.getRow()-1][n2.getCol()];
			else
				wall = maze[n2.getRow()+1][n2.getCol()];
		}
		return wall;
	}
 }
