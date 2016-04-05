package ie.gmit.sw.ai;

import java.util.*;

import ie.gmit.sw.game.*;
import ie.gmit.sw.maze.Node;
import ie.gmit.sw.maze.NodeType;

/*
 * This is an implementation of the 'RandomWalk' search algorithm.
 * The only difference is, if the enemy can see the player
 * (i.e is in an adjacent Node) 
 * it will instinctively move into the player position
 */

public class RandomWalk implements Traversator {
	private final int MAX_DELAY = 1500, MIN_DELAY = 1000;
	private Node[][] maze;
	private GameView g;
	private Node currentNode, nextNode;
	private Random r = new Random();
	private volatile boolean complete = false;
	private volatile boolean playerFound = false;
	
	public RandomWalk(Node[][] maze, Node currentNode, GameView g){
		this.maze = maze;
		this.currentNode = currentNode;
		this.g = g;
		
		new Thread(new Runnable() {
			public void run() {
				search(currentNode);
			}
		}).start();
	}
	
	public void search(Node currentNode) {
		NodeType currentNodeType = NodeType.floor,  nextNodeType = NodeType.floor;

		while(!complete){			

			try { 
				// Set delay for enemy movement
				// Delay is randomized so enemy movement patterns aren't synced
				int ms = (int)r.nextInt(MAX_DELAY-MIN_DELAY) + MIN_DELAY;
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			//Pick a random adjacent node
        	List<Node> children = currentNode.getAdjacentNodes(maze);
        	int nextNodeIndex = (int)(children.size() * Math.random());
        	if(playerFound) return;
        	
        	for(Node neighbour : children) { 
        		if (neighbour.getNodeType() == NodeType.player) {
        			nextNode = neighbour;
        			playerFound = true;
        			break;
        		}
        	}
        	if(!playerFound) {
	        	// Pick random nearby Node until an available position is found
	        	while (children.get(nextNodeIndex).getNodeType() != NodeType.floor
	        			&& children.get(nextNodeIndex).getNodeType() != NodeType.player
	        			&& children.get(nextNodeIndex).getNodeType() != NodeType.path
	        			&& children.get(nextNodeIndex).getNodeType() != NodeType.ash) {
	           		nextNodeIndex = (int)(children.size() * Math.random());
	        	} 
	        	// Move enemy in new direction
	        	nextNode = children.get(nextNodeIndex);
        	} 
        	if(complete) return;

        	nextNodeType = nextNode.getNodeType();
        	nextNode.setEnemy(currentNode.getEnemy());
        	g.updateEnemyPositions(currentNode, currentNodeType, nextNode);

        	currentNodeType = nextNodeType;
        	currentNode = nextNode;
        	this.currentNode = currentNode;
        	if(currentNode == g.getPlayer().getCurrentNode()) {
        		g.initializeFight(currentNode);
        		return;
        	}
			if (g.getPlayer().getCurrentNode() == currentNode) return; 
		}
	}
	
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public void setNextNode(Node nextNode) {
		this.nextNode = nextNode;
	}
	
	public Node getNextNode() {
		return nextNode;
	}

	public void setMaze(Node[][] maze) {
		this.maze = maze;
	}

	public void setFinished(boolean finished) {
		complete = finished;		
	}
}
