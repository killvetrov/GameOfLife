package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	private class PaintPanel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			lifeWorld.paint(g);
		}
	}
	
	private class Ticker extends Thread {
		public Ticker() {
			setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			while (true) {
				try {
					sleep(67);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (tickerToggle) {
					lifeWorld.nextGeneration();
					repaint();
				}
			}
		}
	}

	private PaintPanel contentPane;
	
	private World lifeWorld = new World(80, 80);
	
	private boolean tickerToggle = false;
	
	private Cell lastPokedCell = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setTitle("Игра \"Жизнь\" Джона Конвея");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 35 + 80 * 10, 80 + 80 * 10);
		contentPane = new PaintPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setFont(new Font("Consolas", Font.PLAIN, 11));
		contentPane.setBackground(Color.BLACK);
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				super.componentResized(e);				
				System.out.println("resized "+ getContentPane().getBounds().width + " " + getContentPane().getBounds().height);
			}
		});		
		
		addKeyListener(new KeyAdapter() {			
			@Override
			public void keyReleased(KeyEvent ke) {
				if ( ke.getKeyCode() == KeyEvent.VK_SPACE ) {
					tickerToggle = !tickerToggle;
				}					
			}
		});
		
		contentPane.addMouseMotionListener(new MouseMotionListener() {			
			@Override
			public void mouseMoved(MouseEvent me) {}

			@Override
			public void mouseDragged(MouseEvent me) {					
				Cell myCell = lifeWorld.getCellByXY(me.getX(), me.getY());
				if (myCell == lastPokedCell) return;
				if (myCell != null) {
					lifeWorld.pokeCell(myCell);
					lastPokedCell = myCell;
					repaint();
				}						
			}

		});
		
		contentPane.addMouseListener(new MouseAdapter() {			
			@Override
			public void mouseReleased(MouseEvent me) {				
				switch (me.getButton()) {
				case MouseEvent.BUTTON3:
					tickerToggle = false;
					lifeWorld.clear();
					repaint();
					break;

				case MouseEvent.BUTTON2:
					tickerToggle = false;
					lifeWorld.randomize();
					repaint();
					break;
				}						
			}
			
			@Override
			public void mousePressed(MouseEvent me) {
				switch (me.getButton()) {
				case MouseEvent.BUTTON1:
					Cell myCell = lifeWorld.getCellByXY(me.getX(), me.getY());
					if (myCell != null) {
						lifeWorld.pokeCell(myCell);
						lastPokedCell = myCell;
						repaint();
					}	
					break;
				}				
			}
			
		} );
		
		setContentPane(contentPane);
		
		new Ticker();
	}
	
}
