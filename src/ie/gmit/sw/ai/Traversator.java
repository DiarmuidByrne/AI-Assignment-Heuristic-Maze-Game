package ie.gmit.sw.ai;

import ie.gmit.sw.*;
public interface Traversator {
	// All searching algorithms will implement this interface
	public void search(Node[][] maze, int startRow, int startCol);
	
	public void setNewRow(int r); public int getNewRow();
	public void setNewCol(int c); public int getNewCol();
	
	public void setRow(int r); public int getRow();
	public void setCol(int c); public int getCol();

}
