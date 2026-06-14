package indepMatrixDiffTrial;

import getTopTriangleEigenvector.DrawOnGui;

public class GetMatrixDiff {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static boolean[][] diffMatrxi2(int numBits) {
		
		boolean diff1[][] = diffMatrix(numBits);
		
		boolean indepMatrix2[][] = indepMatrix2(numBits);
		
		
		boolean ret[][] = new boolean[diff1.length][diff1.length];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				if(indepMatrix2[i][j] && ! diff1[i][j]) {
					ret[i][j] = true;
				}
				
				if(diff1[i][j] && ! indepMatrix2[i][j]) {
					System.out.println("oops!");
					System.exit(1);
				}
			}
		}
		
		return ret;
		
	}
	public static boolean[][] indepMatrix2(int numBits) {
		
		boolean diff1[][] = diffMatrix(numBits);
		
		int length = (int)Math.pow(2, numBits);
		boolean diff2[][] = new boolean[length][length];
		
		boolean origFractal[][] = new boolean[8][8];
		
		origFractal[2][4] = true;
		origFractal[2][5] = false;
		origFractal[3][4] = true;
		origFractal[3][5] = true;
		

		origFractal[4][2] = true;
		origFractal[4][3] = true;
		origFractal[5][2] = false;
		origFractal[5][3] = true;

		int numMultFour = 1;
		int tmpLength = origFractal.length;
		
		int maxLengthFractal = origFractal.length;
		while(tmpLength <= diff2.length) {
			numMultFour++;
			tmpLength *=4;
			maxLengthFractal*= 4;
		}
		maxLengthFractal /=4;
		
		for(int lengthFractal=maxLengthFractal; lengthFractal>=origFractal.length; lengthFractal/=4) {
			
			boolean fractalToUse[][] = origFractal;
			
			while(fractalToUse.length < lengthFractal) {
				fractalToUse = multFour(fractalToUse);
			}
			
			for(int i=0; i<diff2.length; i+=fractalToUse.length) {
				for(int j=0; j<diff2[0].length; j+=fractalToUse.length) {

					boolean cancelIt = false;
					for(int i2=0; i2<fractalToUse.length; i2++) {
						for(int j2=0; j2<fractalToUse[0].length; j2++) {
							if(diff2[i + i2][j + j2]) {
								cancelIt = true;
							}
						}
					}
					
					if( ! cancelIt) {
						for(int i2=0; i2<fractalToUse.length; i2++) {
							for(int j2=0; j2<fractalToUse[0].length; j2++) {
								
								if(fractalToUse[i2][j2]) {
									diff2[i + i2][j + j2] = true;
								} else {
									//diff2[i + i2][j + j2] = false;
								}
							}
						}
					}
				}
			}
			
		}
		
	
		
		
		
		return diff2;
		
	}
	
	public static boolean[][] multFour(boolean init[][]) {
		boolean ret[][] = new boolean[4 * init.length][4 * init[0].length];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				
				if(init[i/4][j/4]) {
					ret[i][j] = true;
				} else {
					ret[i][j] = false;
				}
			}
		}
		
		return ret;
	}
	
	public static boolean[][] diffMatrix(int numBits) {
		
		boolean arrayIndep[][] = getTableSquareLatticeEveryOtherConstraint(numBits);
		boolean arrayGoal[][] = DrawOnGui.getTableSquareLattice(numBits);
		
		boolean arrayDiff[][] = new boolean[arrayGoal.length][arrayGoal[0].length];
		

		for(int i=0; i<arrayDiff.length; i++) {
			for(int j=0; j<arrayDiff[0].length; j++) {
				arrayDiff[i][j] = arrayIndep[i][j] && (!arrayGoal[i][j]);
			}
		}
		
		for(int i=0; i<arrayDiff.length; i++) {
			for(int j=0; j<arrayDiff[0].length; j++) {
				if(arrayDiff[i][j]) {
					System.out.print("#");
				} else {
					System.out.print("_");
				}
			}
			System.out.println();
		}
		
		return arrayDiff;
	}
	
	
	public static int getEveryOtherConstraint(int numBits) {
		

		int RELEVANT_TILES = (int)Math.pow(2, numBits - 1) - 1;
		
		int cur = 1;
		while(cur < RELEVANT_TILES) {
			//TODO: 2 or 1????
			cur = 4*cur + 1;
		}
		
		return cur;
	}
	
	public static boolean[][] getTableSquareLatticeEveryOtherConstraint(int numBits) { 
		int EVERY_OTHER_CONSTRAINT = getEveryOtherConstraint(numBits);
		return getTableSquareLatticeLimitedConstraint(numBits, EVERY_OTHER_CONSTRAINT);
	}
	
	public static boolean[][] getTableSquareLatticeLimitedConstraint(int numBits, int CONSTRAINT_FLAGS) { 
		int length = (int)Math.pow(2, numBits);

		int RELEVANT_TILES = (int)Math.pow(2, numBits - 1) - 1;
		
		
		System.out.println("CONSTRAINT_FLAGS: " + CONSTRAINT_FLAGS);
		
		boolean ret[][] = new boolean[length][length];
		
		for(int i=0; i<ret.length; i++) {

			int rhsToCheck = ((i >> 1) ^ i) & RELEVANT_TILES;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				
				int tmpCheckProb = ((i ^ j) & (~(i ^ (j >> 1))) & rhsToCheck) & CONSTRAINT_FLAGS;
				
				
				if( tmpCheckProb != 0) {
				    j++;

				} else {

					//No Collision:
					ret[i][j] = true;
					j++;
				}
				
				
				
			}
			
		}
		
		return ret;
	}

}
