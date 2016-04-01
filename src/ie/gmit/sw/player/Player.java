package ie.gmit.sw.player;

import ie.gmit.sw.maze.*;

public class Player {
	private Node currentNode;
	private Weapon weapon;
	private int health = 100;
	
	public Player(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public Node getCurrentNode() {
		return currentNode;
	}
	
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}
	
	public void addWeapon() {
		weapon = new Weapon();
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
}
