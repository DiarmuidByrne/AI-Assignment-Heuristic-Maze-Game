package ie.gmit.sw.player;

public class Weapon {

	private int durability;
	// Randomize condition of found weapon
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
