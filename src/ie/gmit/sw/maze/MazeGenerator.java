package ie.gmit.sw.maze;

import java.util.*;
import ie.gmit.sw.ai.*;

public class MazeGenerator {
	private Node[][] maze;
	private Generator g;
	
	
	public MazeGenerator(int rows, int cols) {
		super();
		maze = new Node[rows][cols];
		
//		g = new EllersMaze(maze);
		g = new RecursiveBacktracker(maze);
		initializeFeatures();
		setNodesAsUnvisited();
	}
	
	private void setNodesAsUnvisited() {
		for(int r=0; r<maze.length;r++) {
			for(int c=0; c<maze[0].length;c++)
				maze[r][c].setVisited(false);
		}
	}
	
	
	private void initializeFeatures() {
		
		/*
		 * Initialize Game Features
		 * - 1 Enemies
		 * - 2 Weapons and Bombs 
		 * - 3 Hints
		 * - 4 Goal
		 * - 5 Key
		 */
		
		addFeature(NodeType.enemy, NodeType.floor, 80);
		addFeature(NodeType.weapon, NodeType.wall, 70);
		addFeature(NodeType.hint, NodeType.wall, 50);
		addFeature(NodeType.bomb, NodeType.wall, 60); //15
		addFeature(NodeType.radar, NodeType.wall, 40); //15
		addFeature(NodeType.goal, NodeType.wall, 1);
		addFeature(NodeType.key, NodeType.wall, 1);
	}
	
	private void addFeature(NodeType feature, NodeType replace, int number){
		int counter = 0;
		while (counter < number){
			int row = (int) (maze.length * Math.random());
			int col = (int) (maze[0].length * Math.random());
			
			// Ensures that the node with an item isn't surrounded by wall nodes
			int surrounded = 0;
			List<Node> adjacentNodes = maze[row][col].getAdjacentNodes(maze);
			for (Node neighbour : adjacentNodes) {
				if(neighbour.getNodeType() == NodeType.wall) surrounded++;
			}
			
			if (maze[row][col].getNodeType() == replace && checkBorders(row, col)
					&& surrounded < 4){
				maze[row][col].setNodeType(feature);
				counter++;
			}
		}
	}
	
	private boolean checkBorders(int row, int col) {
		return row > 0 && col > 0 
				&& row < maze.length - 1
				&& col < maze[row].length - 1;
	}
	
	public Node[][] getMaze() {
		return maze;
	}

	public void setMaze(Node[][] maze) {
		this.maze = maze;
	}
}
