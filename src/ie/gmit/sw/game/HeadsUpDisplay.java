package ie.gmit.sw.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import java.util.*;
import ie.gmit.sw.maze.Node;
import ie.gmit.sw.maze.NodeType;
import ie.gmit.sw.player.Player;

/*
 * This class controls the display of the map and additional
 * information for the game, such as the player's current health
 * and the player's weapon condition
 * Also displays a path when a hint is activated 
 * and a message when the game is over
 */

public class HeadsUpDisplay {
	private boolean hintActive;
	private int DEFAULT_VIEW_SIZE;
	private boolean messageShown = false, timerStarted = false;
	private boolean won = false, gameOver = false;
	
	public HeadsUpDisplay(int DEFAULT_VIEW_SIZE) {
		this.DEFAULT_VIEW_SIZE = DEFAULT_VIEW_SIZE;
	}
	
	public void showMap(Graphics2D g2, Node n, int size, int x1, int y1, Player p) {
		// Display character on map
		if (n == p.getCurrentNode()){
			g2.setColor(Color.WHITE);
			g2.fillRect(x1, y1, size, size);
		} else if (n.getNodeType() == NodeType.path ){
			g2.setColor(Color.WHITE);
			g2.fillRect(x1, y1, size, size);
		} else if(n.getNodeType() == NodeType.wall 
				|| n.getNodeType() == NodeType.goal
				|| n.containsItem()) {
			g2.setColor(Color.decode("#336600"));
			g2.fillRect(x1, y1, size, size);
		} else if (n.getNodeType() == NodeType.enemy) {
			// Only display enemies if ther are nearby and a radar has been picked up
			if (n.getEnemy()== null || n.getEnemy().isVisible() == false) return;
			g2.setColor(Color.RED);
			g2.fillRect(x1, y1, size, size);
		} 
	}
	
	public void showHealth(Player p, Graphics2D g2) {
		// Implement a health bar based on player's current health from 0-100
		Font font = new Font(Font.DIALOG_INPUT, Font.BOLD, 20);
		g2.setFont(font);
		FontMetrics f = g2.getFontMetrics(font);
        g2.setColor(Color.black);
        g2.fillRect(DEFAULT_VIEW_SIZE/2-100, 39, 101*2,12);
        String str = "Health";
        g2.drawString(str, DEFAULT_VIEW_SIZE/2-f.stringWidth(str)/2, 30);
        if(p.getHealth() > 30)  g2.setColor(Color.blue);
        else g2.setColor(Color.red);
        g2.fillRect(DEFAULT_VIEW_SIZE/2-100, 40, p.getHealth()*2,10);
        
        showWeaponDurability(p, g2);
        if (messageShown) showMessage(g2);
        if (gameOver) showGameOverMenu(won, g2);
	}
	
	public void showWeaponDurability(Player p, Graphics2D g2) {
		if (p.getWeaponDurability() > 0) {		
			Font font = new Font(Font.DIALOG_INPUT, Font.BOLD, 20);
			g2.setFont(font);
			FontMetrics f = g2.getFontMetrics(font);
	        g2.setColor(Color.black);
	        g2.fillRect(DEFAULT_VIEW_SIZE/4-125, 39, 50*2,12);
	        String str = "Wpn Condition";
	        g2.drawString(str, DEFAULT_VIEW_SIZE/4-f.stringWidth(str), 30);
	        if(p.getWeaponDurability() > 3)  g2.setColor(Color.blue);
	        else g2.setColor(Color.red);
	        g2.fillRect(DEFAULT_VIEW_SIZE/4-125, 40, p.getWeaponDurability()*10,10);
		}
	}
	
	// Displays when the player dies or when the player finishes the game
	private void showGameOverMenu(boolean won, Graphics2D g2) {
		Font font = new Font(Font.DIALOG_INPUT, Font.BOLD, 35);
		g2.setColor(Color.black); 
		g2.fillRect(0, 0, DEFAULT_VIEW_SIZE, DEFAULT_VIEW_SIZE);
		g2.setFont(font);
		FontMetrics f = g2.getFontMetrics(font);

		// Show game winning message
		if (won) {
			g2.setColor(Color.white);
			String message = "YOU WON! \nPress Enter to Quit!";
	        g2.drawString(message, DEFAULT_VIEW_SIZE/2-f.stringWidth(message)/2, DEFAULT_VIEW_SIZE/2);
		}
		// Show game over message
		else {
			g2.setColor(Color.RED);
			String message = "YOU DIED! \nPress Enter to Quit!";
	        g2.drawString(message, DEFAULT_VIEW_SIZE/2-f.stringWidth(message)/2, DEFAULT_VIEW_SIZE/2);
		}
	}
	
	public void showPath(Player p, List<Node> path) {
		p.setStepCount(0);
		for(Node node : path) {
				if (node.getNodeType() != NodeType.player &&
						node.getNodeType() != NodeType.enemy && 
						node.getNodeType()!= NodeType.goal &&
						node.getNodeType() != NodeType.key) 
					node.setNodeType(NodeType.path);
			}
		hintActive = true;
	}
	
	public void showMessage(Graphics2D g2) {
		// Show message
		Font font = new Font(Font.DIALOG_INPUT, Font.BOLD, 20);
		FontMetrics f = g2.getFontMetrics(font);
		g2.setFont(font);
		String message = "You need a key before you can exit the maze!"; 
		g2.setColor(Color.RED);
        g2.drawString(message, DEFAULT_VIEW_SIZE/2-(f.stringWidth(message)/2), 80);
		if((!timerStarted)) {
			timerStarted = true;
			new Thread(new Runnable() {
				public void run() {
					startTimer();
				}
			}).start();
		}
	}
	
	public void hidePath(List<Node> path) {
		if (hintActive) {
			hintActive = false;
			for(Node p : path) {
				if (p.getNodeType() == NodeType.path) p.setNodeType(NodeType.floor);
			}
		}
	}
	
	public void setMessageShown(boolean messageShown) {
		this.messageShown = messageShown;
	}
	
	private void startTimer() {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageShown = false;
	}
	
	public void setGameOver(boolean gameOver, boolean won) {
		this.gameOver = gameOver;
		this.won = won;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
}
