package ie.gmit.sw.player;

import java.util.concurrent.ThreadLocalRandom;

public class Weapon {

	private int durability;
	// Randomize condition of found weapon
	public Weapon() {
		int d = ThreadLocalRandom.current().nextInt(3, 8 + 1);
		this.durability = d;

	}
	
	public int getDurability() {
		return durability;
	}
	
	public void setDurability(int d) {
		this.durability = d;
	}
}
