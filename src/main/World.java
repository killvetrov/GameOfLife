package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class World {
	
	private Random rnd = new Random();

	private int width, height;
	private Cell grid[][];
	
	private int time, population, births, deaths;
	
	public World(int w, int h) {
		width = w;
		height = h;
		grid = new Cell[height][width];
		randomize();
	}
	
	public synchronized Cell getCell(int row, int column) {
		if ( isValidRowColumn(row, column) )
			return grid[row][column];
		else {			
			if (row < 0) row = (height ) + (row % height);
			if (row >= height) row = row % height;
			if (column < 0) column = (width ) + (column % width);
			if (column >= width) column = column % width;
			return grid[row][column];
			
			//return null;			
		}	
	}
	
	public synchronized Cell getCellByXY(int x, int y) {
		final int margin = 10;
		
		int r, c;
		
		r = (y - margin) / 10;
		c = (x - margin) / 10;
		
		return getCell(r, c);
	}
	
	
	public synchronized void pokeCell(Cell c) {		
		if (c.isAlive()) {
			c.die();
			population--;
		}
		else {
			c.live();
			population++;
		}	
	}

	
	public boolean isValidRowColumn(int row, int column) {
		return (row >= 0) && (row < height) && (column >= 0) && (column < width);
	}
	
	public synchronized void nextGeneration() {
		long startTime = System.currentTimeMillis();
		
		Cell newGrid[][] = new Cell[height][width];
		
		births = deaths = 0;
				
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				newGrid[i][j] = new Cell(grid[i][j]);
				
				if ( grid[i][j].isAlive() ) {
					switch ( getLivingNeighboursCount(i, j) ) {
					case 2:
					case 3:
						// клетка продолжает жить в новом поколении
						break;
					default:
						newGrid[i][j].die();
						deaths++;
					}
				} else {
					switch ( getLivingNeighboursCount(i, j) ) {					
					case 3:
						newGrid[i][j].live();
						births++;
						break;
					}
				}
			
			}
				
		grid = newGrid;
		time++;
		population += births - deaths;
		
		System.out.println( "Algorithm time: " + (System.currentTimeMillis() - startTime) );
	}
	
	private synchronized int getLivingNeighboursCount(int row, int column) {
		int count = 0;
		Cell c;
		
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++) {
				if ( (i == 0 && j == 0) || ((c = getCell(row + i, column + j)) == null) ) continue;
				if ( c.isAlive() ) count++; 
			}
		
		return count;
	}
	
	public synchronized int getPopulation() {
		int population = 0;
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if ( getCell(i, j).isAlive() )
					population++;
		return population;
	}
	
	public synchronized void randomize() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				grid[i][j] = new Cell(rnd.nextBoolean() && rnd.nextBoolean());
		
		time = births = deaths = 0; population = getPopulation();
	}
	
	public synchronized void clear() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				grid[i][j] = new Cell();
		time = deaths = population = births = 0;
	}
	
	public synchronized void paint(Graphics g) {
		long startTime = System.currentTimeMillis();
		
		final int margin = 10;
		
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, 1000, 1000);
		
		g.setColor(Color.GRAY.darker().darker().darker());
		for (int i = 0; i <= width; i++) {
			g.drawLine(margin + 10 * i, margin, margin + 10 * i, margin + 10 * width);
		}
		for (int j = 0; j <= height; j++) { 
			g.drawLine(margin, margin + 10 * j, margin + 10 * height, margin + 10 * j);
		}

		
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {			
				if ( getCell(i, j).isAlive() ) {					
					g.setColor(Color.GREEN);
					g.drawRect(margin + 10 * j + 1, margin + 10 * i + 1, 8, 8);					
					g.setColor( new Color(0, getCell(i, j).getColor(), 0) );
					g.fillRect(margin + 10 * j + 2, margin + 10 * i + 2, 7, 7);
				} //else
					//g.setColor(Color.BLACK);
				
			}
		
		g.setColor(Color.CYAN);
		g.drawString(String.format("Поколение: %3d | Население: %3d | Родилось: %3d | Умерло: %3d", 
				
				time, 
				population, 
				//(double) 100 * population / (width * height), 
				births, 
				deaths
				), 
				
				margin, margin + 10 * height + 15);
		
		System.out.println( "Paint time: " + (System.currentTimeMillis() - startTime) );
	}

}
