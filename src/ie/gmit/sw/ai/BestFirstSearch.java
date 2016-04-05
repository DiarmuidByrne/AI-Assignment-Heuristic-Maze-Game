package ie.gmit.sw.ai;

import java.util.*;

import ie.gmit.sw.enemy.Enemy;
import ie.gmit.sw.game.*;
import ie.gmit.sw.maze.Node;
import ie.gmit.sw.maze.NodeType;
import ie.gmit.sw.player.Player;

public class BestFirstSearch implements Traversator{
	private final int MAX_DELAY = 1500, MIN_DELAY = 1000;
	private Random r = new Random();
	private Node[][] maze;
	private Player p;
	private Node currentNode;
	private NodeType currentNodeType = NodeType.floor, nextNodeType;
	private LinkedList<Node> queue = new LinkedList<Node>();;
	private Enemy e;
	private GameView g;
	private boolean complete = false;
	public BestFirstSearch(Node[][] maze, Node start, GameView g, Enemy e) {
		this.maze = maze;
		this.p = g.getPlayer();
		this.e = e;
		this.g = g;
		new Thread(new Runnable() {
			public void run () {
				search(start);
				
			}
		}).start();
		setNodesAsUnvisited();
	}
	
	public void search(Node start) {
		queue.add(start);
		start.setEnemy(e);
		boolean firstMove = true;
		currentNode = start;
		while(!queue.isEmpty() && !complete) {
			Node next = queue.poll();
			
			try {
				int ms = (int)r.nextInt(MAX_DELAY-MIN_DELAY) + MIN_DELAY;
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (complete) return;
			if (!firstMove) {
				if (next.getNodeType() == NodeType.enemy) continue;
				nextNodeType = next.getNodeType();
	        	next.setEnemy(currentNode.getEnemy());

	        	currentNode.setEnemy(null);
	        	currentNodeType = nextNodeType;
				g.updateEnemyPositions(currentNode, currentNodeType, next);
				currentNode = next;
			}
			firstMove = false;
			next.setEnemy(e);
			if (next == p.getCurrentNode()) {
				// Found player
				g.initializeFight(currentNode);
				return;
			}
			
			queue.clear();

			pushSuccessors(next);
			Collections.sort(queue,(Node current, Node nextNode) -> current.getHeuristic(p.getCurrentNode()) - nextNode.getHeuristic(p.getCurrentNode()));		
		}
	}
	
	public void pushSuccessors(Node current) {
		List<Node> children = current.getAdjacentNodes(maze);
		for (Node child : children) {

			if(!child.isTraversable() || queue.contains(child)) continue;
			
			else {
				nextNodeType = child.getNodeType();
				child.setParent(current);
				queue.add(child);
			}
		}
	}
	
	private void setNodesAsUnvisited() {
		for(int r=0; r<maze.length;r++) {
			for(int c=0; c<maze[0].length;c++)
				maze[r][c].setVisited(false);
		}
	}

	public void setMaze(Node[][] maze) {
		this.maze = maze;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setFinished(boolean finished) {
		complete = finished;
	}
}
