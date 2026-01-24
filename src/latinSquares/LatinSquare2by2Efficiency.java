package latinSquares;

import java.math.BigInteger;

//TODO: it's still skeleton code!
public class LatinSquare2by2Efficiency {

	public static void main(String[] args) {
		
		BigInteger numSolutions = solve(8);
		
		System.out.println("Number of solutions found: " + numSolutions);

	}
	
	public static final int UNDETERMINED = -1;
	
	public static int[][] initTalbe(int n) {

		int solution[][] = new int[n][n];
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				solution[i][j] = UNDETERMINED;
			}
		}
		
		for(int i=0; i<n; i++) {
			solution[0][i] = i;
			solution[i][0] = i;
		}
		return solution;
	}
	
	public static BigInteger solve(int n) { 
		
		if(n<2) {
			return BigInteger.ONE;
		}
		
		//TODO: Maybe start at -1?
		int solution[][] = initTalbe(n);
		
		System.out.print("Start cofig:");
		System.out.println(tableToString(solution));
		
		//TODO: do it the simple way 1st:
		boolean used2x2[][] = new boolean[solution.length][solution.length];
		int indexused2x2PrevI[][] = new int[solution.length][solution.length];
		int indexused2x2PrevJ[][] = new int[solution.length][solution.length];
		int curNum2x2Boxes = 0;
		
		int digitIIndexrowJ[][] = new int[solution.length][solution.length];
		
		//END TODO
		
		
		int usedOrder[][] = new int[solution.length][solution.length];
		boolean used[][] = new boolean[solution.length][solution.length];
		
		BigInteger numSolutions = BigInteger.ZERO;
		BigInteger TWO = new BigInteger("2");
		
		long debugIter = 0L;
		//numSolutions = numSolutions.add(TWO.pow(5));

		//Guess: just do a for loop
		// Just hold 0 to n-1
		// + 1 to print
		
		//for i, j:
		//boolean used2x2[][]
		//usedRow[][]
		//
		
		SEARCH:
		for(int i=1, j=1; true; ) {
			
			if(i >= n) {
				System.out.println("ERROR: i>=n. This should not happen!");
				System.exit(1);
			}

			if(j >= n) {
				System.out.println("ERROR: j>=n. This should not happen!");
				System.exit(1);
			}

			boolean keepGoing = false;

			//Digit trial
			for(int k=solution[i][j] + 1; k<n; k++) {
				if(used[i][k] || k == i) {
					continue;
				}
				
				boolean foundAbove = false;
				for(int aboveIndex=0; aboveIndex<i; aboveIndex++) {
					if(k == solution[aboveIndex][j]) {
						foundAbove = true;
						break;
					}
				}
				if(foundAbove) {
					continue;
				}
				
				solution[i][j] = k;
				
				usedOrder[i][j] = k;
				used[i][k] = true;
				
				keepGoing = true;
				break;
			}
			
			boolean goBack = !keepGoing;
			
			if(keepGoing) {

				//System.out.println("DEBUG: (" + i + ", " + j + ")");
				//System.out.println(tableToString(addOneToTable(solution)));
				
				j++;
				if(j >= n) {
					j = 1;
					i++;
					
					if(i == n) {
						
						debugIter++;
						if(n <= 6 || debugIter % 1000000 == 0) {
							int printedSolution[][] = addOneToTable(solution);
							
							System.out.println("Found solution:");
							System.out.println(tableToString(printedSolution));
							
						}
						
						numSolutions = numSolutions.add(TWO.pow(curNum2x2Boxes));
						i--;
						j = n-1;
						goBack = true;
					}
				}
			}
			
			if(goBack) {

				solution[i][j] = UNDETERMINED;
				if(usedOrder[i][j] >= 0) {
					used[i][usedOrder[i][j]] = false;
				}
				usedOrder[i][j] = UNDETERMINED;
				
				j--;
				if(j < 1) {
					i--;
					j=n-1;
					
					if(i < 1) {
						break SEARCH;
					}
				}
				if(usedOrder[i][j] >= 0) {
					used[i][usedOrder[i][j]] = false;
				}
				usedOrder[i][j] = UNDETERMINED;
				
				if(used2x2[i][j]) {
					used2x2[i][j] = false;
					int oldI = indexused2x2PrevI[i][j];
					int oldJ = indexused2x2PrevJ[i][j];
					used2x2[oldI][j] = false;
					used2x2[i][oldJ] = false;
					used2x2[oldI][oldJ] = false;
					
					curNum2x2Boxes--;
					//TODO: maybe there's more to it?
				}
			}
			
			
		}
		
		
		return numSolutions;
		
	}
	
	public static int[][] addOneToTable(int curTable[][]) {
		
		int newTable[][] = new int[curTable.length][curTable[0].length];
		for(int i=0; i<curTable.length; i++) {
			for(int j=0; j<curTable[0].length; j++) {
				newTable[i][j] = curTable[i][j] + 1;
			}
		}
		return newTable;
	}
	
	public static String tableToString(int cur[][]) {
		String ret = "";
		for(int i=0; i<cur.length; i++) {
			for(int j=0; j<cur[0].length; j++) {
				if(j < cur[0].length - 1) {
					ret += cur[i][j] + ", ";
				} else {
					ret += cur[i][j];
				}
			}
			ret += "\n";
		}
		
		return ret;
	}
	
	public static long numSolutionsFoundGlobal = 0;

}
