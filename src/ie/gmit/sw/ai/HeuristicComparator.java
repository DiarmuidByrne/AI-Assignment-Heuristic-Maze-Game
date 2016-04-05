package ie.gmit.sw.ai;

import java.util.Comparator;
import ie.gmit.sw.maze.Node;

/*
 * This comparator is used for the priority queue 
 * in the A* traverser
 */

public class HeuristicComparator implements Comparator<Node> {
	public int compare(Node node1, Node node2) {
		if (node1.getScore() > node2.getScore()) {
			return -1;
		} else if (node1.getScore() < node2.getScore()) { 
			return 1;
		} else {
			return 0;
		}
	}
}