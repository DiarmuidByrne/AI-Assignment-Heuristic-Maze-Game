package ie.gmit.sw.ai;

import java.util.Comparator;

import ie.gmit.sw.maze.Node;

public class HeuristicComparator implements Comparator<Node> {
	private Node goal;
	
	public HeuristicComparator(Node goal) {
		super();
		this.goal = goal;
	}
	
	public int compare(Node node1, Node node2) {
		if (node1.getHeuristic(goal) > node2.getHeuristic(goal)) {
			return -1;
		}
		else if (node1.getHeuristic(goal) < node2.getHeuristic(goal)){ 
			return 1;
		} else{
			return 0;
		}
	}
}