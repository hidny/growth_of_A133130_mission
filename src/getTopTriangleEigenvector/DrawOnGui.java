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
	
	//TODO: make slow version of these table creation algos, so you can sanity check them.
	

	public static boolean[][] getTableSquareLattice(int numBits) { 
		
		int length = (int)Math.pow(2, numBits);

		int RELEVANT_TILES = (int)Math.pow(2, numBits - 1) - 1;
		
		boolean ret[][] = new boolean[length][length];
		
		for(int i=0; i<ret.length; i++) {

			int rhsToCheck = ((i >> 1) ^ i) & RELEVANT_TILES;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				
				int tmpCheckProb = (i ^ j) & (~(i ^ (j >> 1))) & rhsToCheck;
				
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
					
					
					
				    j += answer;

				} else {

					//No Collision:
					ret[i][j] = true;
					j++;
				}
				
				
				
			}
			
		}
		
		return ret;
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
	
	public static int RIGHT_SIDE_UP_TRIANGLES = 0;
	static {
		RIGHT_SIDE_UP_TRIANGLES = 0;
		for(int i=0; i<14; i++) {
			RIGHT_SIDE_UP_TRIANGLES = 4*RIGHT_SIDE_UP_TRIANGLES + 2;
		}
		
	}
	
	public static int checkTriangleProb(int topLeftValues, int topMidValues, int topRightValues, int bottomRightValues, int bottomMidValues, int bottomLeftValues) {
		int tmpCheckProb = 0;
		
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		
		tmpCheckProb |= ~topLeftValues & topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & ~topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  ~topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  ~bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  bottomRightValues & ~bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & ~bottomLeftValues;
		
		
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & ~topMidValues &  ~topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  ~topRightValues &  ~bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  ~bottomRightValues & ~bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & ~bottomLeftValues;
		
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & topMidValues &  topRightValues &  bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  topRightValues &  bottomRightValues & bottomMidValues & ~bottomLeftValues;
		
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & topMidValues &  topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  topRightValues &  bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  bottomRightValues & bottomMidValues & ~bottomLeftValues;
		
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & bottomLeftValues;
		tmpCheckProb |= topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  bottomRightValues & ~bottomMidValues & ~bottomLeftValues;
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & bottomMidValues & ~bottomLeftValues;
		
		//System.out.println("2nd last: " + tmpCheckProb);
		//System.out.println("2nd last 2: " + tmpCheckProb);
		tmpCheckProb |= ~topLeftValues & ~topMidValues &  ~topRightValues &  ~bottomRightValues & ~bottomMidValues & ~bottomLeftValues;

		//System.out.println("Last: " + tmpCheckProb);
		tmpCheckProb = (~tmpCheckProb);

		//System.out.println("Ret: " + tmpCheckProb);
		
		return tmpCheckProb & RIGHT_SIDE_UP_TRIANGLES;
	}
	

	public static boolean[][] getTableTriangleLattice(int numBits) { 
		
		int length = (int)Math.pow(2, numBits);

		int RELEVANT_TILES = (int)Math.pow(2, numBits) - 1;
		int RELEVANT_TILES_NOT_LEFTMOST = (int)Math.pow(2, numBits - 1) - 1;
		
		boolean ret[][] = new boolean[length][length];
		
		for(int i=0; i<ret.length; i++) {

			int extendedTop = (i << numBits) + i;
			
			int topLeftValues = (extendedTop >> 2) & RIGHT_SIDE_UP_TRIANGLES;
			int topMidValues = (extendedTop >> 1) & RIGHT_SIDE_UP_TRIANGLES;
			int topRightValues = extendedTop & RIGHT_SIDE_UP_TRIANGLES;

			int debugJSkip = 0;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				int extendedBottom = j + (j << numBits);

				int bottomLeftValues = (extendedBottom >> 1) & RIGHT_SIDE_UP_TRIANGLES;
				int bottomMidValues = extendedBottom & RIGHT_SIDE_UP_TRIANGLES;
				int bottomRightValues = (extendedBottom << 1) & RIGHT_SIDE_UP_TRIANGLES;
				//1st idea: over all 32 combos:
				
				int tmpCheckProb = checkTriangleProb(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
				
				tmpCheckProb = tmpCheckProb  & RELEVANT_TILES_NOT_LEFTMOST;
				
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
					int getHighestBit = tmpCheckProb & RELEVANT_TILES;
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
					int answerNotLeftMost = (answer  & RELEVANT_TILES_NOT_LEFTMOST) >> 1;
					
					if(answerisLeftMostTile + answerNotLeftMost == 0) {
						System.out.println(tmpCheckProb);
					}
					if(!((answerNotLeftMost > 0) ^ (answerisLeftMostTile > 0))) {
						System.out.println("i: " + i);
						System.out.println("j: " + j);
						System.out.println("Doh1!: " + tmpCheckProb);
						System.out.println("Doh2!: " + answerNotLeftMost);
						System.out.println("Doh3!: " + answerisLeftMostTile);
						System.exit(1);
					}
					
					if(answerisLeftMostTile > 1) {
						System.out.println("Ah!");
						System.out.println("Doh!: " + answerisLeftMostTile);
						System.exit(1);
					}
					//System.out.println("answerisLeftMostTile: " + answerisLeftMostTile);
					//System.out.println("answerNotLeftMost: " + answerNotLeftMost);
					//System.out.println(belowLayer + " and " + j + " collision. Add " + (answerisLeftMostTile + answerNotLeftMost));
					
					if(debugJSkip <= j) {
						debugJSkip = j + answerisLeftMostTile + answerNotLeftMost;
					}
					
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
	
	public static boolean[][] getTableTubeTriangleLattice(int numBits) { 
		
		int length = (int)Math.pow(2, numBits);

		int RELEVANT_TILES = (int)Math.pow(2, numBits) - 1;
		int RELEVANT_TILES_NOT_LEFTMOST = (int)Math.pow(2, numBits - 1) - 1;
		
		boolean ret[][] = new boolean[length][length];
		
		for(int i=0; i<ret.length; i++) {

			int extendedTop = (i << numBits) + i;
			
			int topLeftValues = (extendedTop >> 2) & RIGHT_SIDE_UP_TRIANGLES;
			int topMidValues = (extendedTop >> 1) & RIGHT_SIDE_UP_TRIANGLES;
			int topRightValues = extendedTop & RIGHT_SIDE_UP_TRIANGLES;

			int debugJSkip = 0;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				int extendedBottom = j + (j << numBits);

				int bottomLeftValues = (extendedBottom >> 1) & RIGHT_SIDE_UP_TRIANGLES;
				int bottomMidValues = extendedBottom & RIGHT_SIDE_UP_TRIANGLES;
				int bottomRightValues = (extendedBottom << 1) & RIGHT_SIDE_UP_TRIANGLES;
				//1st idea: over all 32 combos:
				
				if(i == 15 && j == 9) {
					System.out.println("Question!");
				}
				int tmpCheckProb = checkTriangleProb(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
				
				tmpCheckProb = tmpCheckProb  & RELEVANT_TILES;
				
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
					int getHighestBit = tmpCheckProb & RELEVANT_TILES;
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
					int answerNotLeftMost = (answer  & RELEVANT_TILES_NOT_LEFTMOST) >> 1;
					
					if(answerisLeftMostTile + answerNotLeftMost == 0) {
						System.out.println(tmpCheckProb);
					}
					if(!((answerNotLeftMost > 0) ^ (answerisLeftMostTile > 0))) {
						System.out.println("i: " + i);
						System.out.println("j: " + j);
						System.out.println("Doh1!: " + tmpCheckProb);
						System.out.println("Doh2!: " + answerNotLeftMost);
						System.out.println("Doh3!: " + answerisLeftMostTile);
						System.exit(1);
					}
					
					if(answerisLeftMostTile > 1) {
						System.out.println("Ah!");
						System.out.println("Doh!: " + answerisLeftMostTile);
						System.exit(1);
					}
					//System.out.println("answerisLeftMostTile: " + answerisLeftMostTile);
					//System.out.println("answerNotLeftMost: " + answerNotLeftMost);
					//System.out.println(belowLayer + " and " + j + " collision. Add " + (answerisLeftMostTile + answerNotLeftMost));
					
					if(debugJSkip <= j) {
						debugJSkip = j + answerisLeftMostTile + answerNotLeftMost;
					}
					
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

			int N_TO_USE = -1;

			boolean IS_TRIANGLE = true;
			boolean ret[][] = null;

			if(IS_TRIANGLE) {
				
				int EVEN_NUM_N = N - N%2;
				System.out.println("EVEN_NUM_N = " + EVEN_NUM_N);
				//ret = getTableTubeTriangleLattice(EVEN_NUM_N);
				ret = getTableTriangleLattice(EVEN_NUM_N);
				N_TO_USE = EVEN_NUM_N;
			} else {

				//boolean ret[][] = getTableTubeSquareLattice(N);
				
				ret = getTableSquareLattice(N);
				N_TO_USE = N;
			}
			
			int numDivs = (int) Math.pow(2, N_TO_USE);
			int gap = DIM / numDivs;
			System.out.println("Gap: " + gap);
			System.out.println("numDivs: " + numDivs);

			
			int DIV_BETWEEN_SQUARES = 1;
			
			if(N >= 8) {
				DIV_BETWEEN_SQUARES = 0;
			}
			
			
			
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
