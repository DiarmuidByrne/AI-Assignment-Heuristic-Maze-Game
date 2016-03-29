package ie.gmit.sw.ai;

import java.util.*;

import ie.gmit.sw.*;

public class RandomWalk implements Traversator{
	private int newRow, newCol;
	private int row, col;
	
	public RandomWalk(Node[][] maze, int row, int col) {
		search(maze, row, col);
	}
	
	public void search(Node[][] maze, int startRow, int startCol) {
		long time = System.currentTimeMillis();
    	int visitCount = 0;
    	Node node = maze[startRow][startCol];
		boolean complete = false;
		while(!complete){		
			node.setVisited(true);	
			visitCount++;
			
			if (node.isGoalNode()){
				// Fight
		        time = System.currentTimeMillis() - time; //Stop the clock
		        complete = true;
				break;
			}
			
			try { // Move once per second
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Pick a random adjacent node
        	List<Node> children = node.getAdjacentNodes(maze);
        	int nextNode = 0;
        	// Pick random nearby Node until an available position is found
        	while (children.get(nextNode).getNodeType() != NodeType.floor
        			|| children.get(nextNode).getNodeType() != NodeType.player) {
        		nextNode = (int)(children.size() * Math.random());
        	}
        	// Move enemy in new direction
        	node = children.get((int)(children.size() * Math.random()));
        	
		}
	}
	
	
	public void setNewRow(int r) {
		newRow = r;		
	}

	public int getNewRow() {
		return newRow;
	}

	public void setNewCol(int c) {
		newCol = c;
	}

	public int getNewCol() {
		return newCol;
	}

	public void setRow(int r) {
		row = r;
	}

	public int getRow() {
		return row;
	}

	public void setCol(int c) {
		col = c;		
	}

	public int getCol() {
		return col;
	}
	
}
