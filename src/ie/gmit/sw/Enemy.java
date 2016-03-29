package ie.gmit.sw;

public interface Enemy {
	public void search(Node[][] maze, int row, int col);
	
	public void setNewRow(int r); public int getNewRow();
	public void setNewCol(int c); public int getNewCol();
	
	public void setRow(int r); public int getRow();
	public void setCol(int c); public int getCol();
}
