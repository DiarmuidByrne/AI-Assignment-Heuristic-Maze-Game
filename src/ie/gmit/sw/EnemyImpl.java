package ie.gmit.sw;

import java.util.Random;

import ie.gmit.sw.ai.*;

public class EnemyImpl implements Enemy {
	
	/* Enemy Object Class
	 * Every Node that is a type enemy will have
	 * an independantly threaded Enemy Object 
	 */
	private Traversator t;
	private Random r = new Random(); 
	
	public EnemyImpl(Node[][] maze, int row, int col) {
		super();
		new Thread(new Runnable() {
			public void run() {
				search(maze, row, col);
			}
		}).start();
	}
	
	public void search(Node[][] maze, int row, int col) {
		int i = r.nextInt(1);
		
		if(i == 0) t = new RandomWalk(maze, row, col); 
	}
	
	public int getRow() {
		return t.getRow();
	}

	public void setRow(int row) {
		t.setRow(row);
	}

	public int getCol() {
		return t.getRow();
	}

	public void setCol(int col) {
		t.setCol(col);
	}

	public void setNewRow(int r) {
		t.setNewRow(r);
	}

	public int getNewRow() {
		return t.getNewRow();
	}

	public void setNewCol(int c) {
		t.setNewCol(c);
	}

	public int getNewCol() {
		return t.getNewCol();
	}
}
