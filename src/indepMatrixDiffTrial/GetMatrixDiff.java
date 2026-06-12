package indepMatrixDiffTrial;

import getTopTriangleEigenvector.DrawOnGui;

public class GetMatrixDiff {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
			cur = 4*cur + 2;
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
