package ie.gmit.sw.ai;

import java.util.*;


import ie.gmit.sw.game.*;
import ie.gmit.sw.maze.Node;

public class AStarSearch {
	private volatile GameView g;
	private volatile Node[][] maze;
	private volatile Node goal;
	
	private HeuristicComparator heuristicComparer;
	private List<Node> closed = new ArrayList<Node>();
	private PriorityQueue<Node> open;
	private boolean complete = false;
	
	/*
	 * This search is used whenever a hint is activated to show the closest path to the goal
	 * The goal node varies depending on the player's progress
	 * The path will point to a key, if the key hasn't been found
	 * Else it will point to the exit
	 * The heuristic creates an optimal path based on wall node positions and
	 * the danger of each node
	 */
	
	public AStarSearch(Node[][] maze, Node start, Node goal, GameView g) {
		this.maze = maze;
		this.goal = goal;
		this.g= g;
		heuristicComparer = new HeuristicComparator();
		open  = new PriorityQueue<Node>(40, heuristicComparer);
						
		new Thread(new Runnable() {
			public void run () {
				resetNodes();
				search(start);
				resetNodes();
			}
		}).start();
	}
	
	public void search(Node start) {

		open.add(start);
		start.setDistanceTravelled(0);
		while(!complete) {
			Node next = open.poll();

			if (next == goal) {
				
				complete = true;
				List<Node> path = new ArrayList<Node>();

				while (next.getParent() != null) {
					path.add(next);
					next = next.getParent();
				}
				
				// return with List of Nodes and repaint the scene 
				// to show path to goal
				g.showPath(path);
				return;
			}
			pushSuccessors(next);
			closed.add(next);
		}
	}
	
	public void pushSuccessors(Node current) {
		List<Node> children = current.getAdjacentNodes(maze);
		
		for (Node child : children) {

			int score = current.getDistanceTravelled()+1 + child.getHeuristic(goal);
			int childScore = child.getDistanceTravelled() + child.getHeuristic(goal);

			if(!child.isTraversable() && child != goal)continue;

			if((open.contains(child) || closed.contains(child)) && childScore < score) continue; 
			
			open.remove(current);
			closed.remove(child);
			child.setDistanceTravelled(current.getDistanceTravelled() + 1);
			child.setParent(current);
			open.add(child);
		}
	}
	
	private void resetNodes() {
		for(int r=0; r<maze.length;r++) {
			for(int c=0; c<maze[0].length;c++) {
				maze[r][c].setVisited(false);
				maze[r][c].setParent(null);
			}
		}
	}
}
