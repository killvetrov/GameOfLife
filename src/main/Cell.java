package main;

import java.util.Random;

public class Cell {
	
	private static Random rnd = new Random();
	
	private boolean alive;
	private int color;
	
	public Cell() {
		alive = false;
		pickNewColor();
	}
	
	public Cell(boolean alive) {
		this.alive = alive;
		pickNewColor();
	}
	
	public Cell(Cell c) {
		this.alive = c.alive;
		this.color = c.color;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void live() {
		if (!alive) {
			alive = true;
			pickNewColor();
		}
	}
	
	public void die() {
		if (alive) {
			alive = false;
		}
	}
	
	public int getColor() {
		return color;
	}
	
	private void pickNewColor() {
		color = (225 - rnd.nextInt(128));
	}

}
