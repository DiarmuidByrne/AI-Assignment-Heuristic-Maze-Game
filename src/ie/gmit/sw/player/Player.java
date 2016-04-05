package ie.gmit.sw.player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import ie.gmit.sw.maze.*;

public class Player {
	private Node currentNode;
	private Weapon weapon;
	private int health = 100;
	// Will hide any shown pathways if it reaches a certain value
	private int stepCount = 0;
	private boolean facingRight = true;
	private boolean hasKey = false;
	
	public Player() {
		weapon = new Weapon(0);
	}
	
	public int getStepCount() {
		return stepCount;
	}

	public void setStepCount(int stepCount) {
		this.stepCount = stepCount;
	}

	public Player(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(Node currentNode) {
		if(this.currentNode != currentNode)	stepCount++;
		this.currentNode = currentNode;
	}
	
	// Only add new weapon to inventory if 
	// No weapon is in inventory
	// Or current weapon is in worse condition
	public void addWeapon() {
		int durability = ThreadLocalRandom.current().nextInt(3, 8 + 1);
		if (weapon == null || weapon.getDurability() < durability)	weapon = new Weapon(durability);
	}
	
	public int getWeaponDurability() {
		if (weapon!= null)	return weapon.getDurability();
		else return 0;
	}
	
	public void setWeaponDurability(int durability) {
		if (weapon != null) weapon.setDurability(durability);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getPlayerState(Node[][] maze, int playerState) {
		int imageIndex = 0;
		
		List<Node> nodes = currentNode.getAdjacentNodes(maze);
		for (Node n : nodes) {

			if (facingRight) {
				if (n.getNodeType() == NodeType.enemy && (weapon== null || weapon.getDurability() == 0)) imageIndex = 13;
				else if (weapon != null && weapon.getDurability() > 0) imageIndex = 12;
				else imageIndex = playerState;
			} else {
				if (n.getNodeType() == NodeType.enemy && (weapon== null || weapon.getDurability() == 0)) imageIndex = 18;
				else if (weapon != null && weapon.getDurability() > 0) imageIndex = 17;
				else imageIndex = playerState;
			}
		} 
		return imageIndex;
	}
	
	public void setFacingRight(boolean facingRight) {
		this.facingRight = facingRight;
	}
	
	public boolean isFacingRight() { 
		return facingRight;
	}

	public boolean hasKey() {
		return hasKey;
	}

	public void giveKey() {
		hasKey = true;
	}
}
