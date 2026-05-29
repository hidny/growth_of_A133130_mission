package getTopTriangleEigenvector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//From youtube Java GUI Tutorial - Make a GUI in 13 Minutes #99
//From https://introcs.cs.princeton.edu/java/15inout/GUI.java.html
public class DrawOnGui implements ActionListener {

	private JFrame frame;
	private JLabel label;
	private JPanel contentPane;
	private JPanel panelDrawing;
	private JButton button;
	private JButton button2;
	private int count = 0;
	
	//TODO: make this shiftable:
	private int N = 5;
	
	public static final int DIM = 512;
	
	public DrawOnGui() {
		frame = new JFrame();
		frame.setSize(600, 800);
		
		button = new JButton("Increase N");
		button.addActionListener(this);
		button2 = new JButton("Decrease N");
		button2.addActionListener(this);
		
		label = new JLabel("Number of clicks: " + count);
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(5, 5));
		contentPane.add(button);
		contentPane.add(button2);
		contentPane.add(label);
		
		panelDrawing = new GPanel();
		panelDrawing.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		//panel.setLayout(new GridLayout(0, 1));
		panelDrawing.setLayout(null);
		//panel.add(button);
		//panel.add(label);
		
		//TODO: please paint grid or whatever.

		frame.add(contentPane, BorderLayout.NORTH);
		frame.add(panelDrawing, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Visualization on Matrix");
		//frame.pack();
		frame.setVisible(true);
		
	}
	
	public static boolean[][] getTableTubeSquareLattice(int numBits) { 
		
		int length = (int)Math.pow(2, numBits);

		int RELEVANT_TILES = (int)Math.pow(2, numBits) - 1;
		int RELEVANT_TILES_NOT_LEFTMOST = (int)Math.pow(2, numBits - 1) - 1;
		
		boolean ret[][] = new boolean[length][length];
		
		for(int i=0; i<ret.length; i++) {

			int wrappedBottom =  (i << numBits) + i;
			
			int rhsToCheck = ((wrappedBottom >> 1) ^ wrappedBottom) & RELEVANT_TILES;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				int wrappedTop = ((j << numBits) + j);
				
				int tmpCheckProb = (wrappedBottom ^ wrappedTop) & (~(wrappedBottom ^ (wrappedTop >> 1))) & rhsToCheck;
				
				if( tmpCheckProb != 0) {
					//Collision:
					
					/* From random thread poster called VArtem: (codeforces)
					 * There is Integer.highestOneBit(int i) method in Java that returns int with leftmost bit set in x. It is implemented as follows:

					public static int highestOneBit(int i) {
					    i |= (i >>  1);
					    i |= (i >>  2);
					    i |= (i >>  4);
					    i |= (i >>  8);
					    i |= (i >> 16);
					    return i - (i >>> 1);
					}
					*/
					//find the index of the leftmost bit:
					int getHighestBit = tmpCheckProb;
					//Copied algo that's O(log(size)) instead of just O(size):
					getHighestBit |= (getHighestBit >>  1);
					getHighestBit |= (getHighestBit >>  2);
					getHighestBit |= (getHighestBit >>  4);
					getHighestBit |= (getHighestBit >>  8);
					getHighestBit |= (getHighestBit >> 16);
					
					
					int answer = getHighestBit - (getHighestBit >>> 1);
					
					//The two variables below are made to get rid of one if condition about how to handle case
					//where the leftmost bit interferes with rightmost tile.
					int answerisLeftMostTile = (answer >> (numBits-1)) & 1;
					int answerNotLeftMost = answer & RELEVANT_TILES_NOT_LEFTMOST;
					
					
				    j += answerisLeftMostTile + answerNotLeftMost;

				} else {

					//No Collision:
					ret[i][j] = true;
					j++;
				}
				
				
				
			}
			
		}
		
		return ret;
	}
	
	private class GPanel extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			
			int startX= 10;
			int startY = 10;
			
			int numDivs = (int) Math.pow(2, N);
			int gap = DIM / numDivs;
			System.out.println("Gap: " + gap);
			System.out.println("numDivs: " + numDivs);
			
			int DIV_BETWEEN_SQUARES = 1;
			
			if(N >= 8) {
				DIV_BETWEEN_SQUARES = 0;
			}
			
			boolean ret[][] = getTableTubeSquareLattice(N);
			
			for(int i=0; i<numDivs; i++) {
				for(int j=0; j<numDivs; j++) {
					/*if((i + j) % 2 == 0) {
						g.setColor(Color.BLUE);
					} else {
						g.setColor(Color.YELLOW);
						
					}*/
					if(ret[i][j]) {
						g.setColor(Color.BLUE);
					} else {
						g.setColor(Color.YELLOW);
					}
					
					g.fillRect(startY + j*gap, startX + i*gap, gap - DIV_BETWEEN_SQUARES, gap - DIV_BETWEEN_SQUARES);
				}
			}
			
			//g.drawRect(10,10, 190, 190);
			g.drawRect(startX, startY, DIM -1, DIM -1);
			g.setColor(Color.BLUE);
			//g.fillOval(50, 50, 100, 100);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("?Hello world");
		
		new DrawOnGui();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		count++;
		label.setText("Number of clicks: " + count);
		
		String buttonText = event.getActionCommand();
		
		System.out.println("Button Text: " + buttonText);
		if(buttonText.equals("Increase N")) {
			N++;
			
			if(N >= 10) {
				N = 9;
			}
			
		} else if(buttonText.equals("Decrease N")) {
			N--;
			
			if(N <= 0) {
				N = 1;
			}
			
		}
		System.out.println("N: " + N);
		panelDrawing.repaint();
		
	}

}
