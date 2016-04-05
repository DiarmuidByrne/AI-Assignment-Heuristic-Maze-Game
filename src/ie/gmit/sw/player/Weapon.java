package ie.gmit.sw.player;

/*
 * The Durability of every picked up weapon is randomized.
 * If a weapon of higher durability/condition is picked up, 
 * the weapon in the player's inventory will be replaced
 */

public class Weapon {
	
	private int durability;
	public Weapon(int d) {
		this.durability = d;
	}
	
	public int getDurability() {
		return durability;
	}
	
	public void setDurability(int d) {
		this.durability = d;
	}
}
