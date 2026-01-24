package latinSquares;

import java.math.BigInteger;

//TODO: it's still skeleton code!
public class LatinSquare2by2EfficiencyPre {

	public static void main(String[] args) {
		
		BigInteger numSolutions = solve(7);
		
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
		
		boolean used2x2[][] = new boolean[solution.length][solution.length];
		
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
			
			
			
			if(keepGoing == false) {
				//TODO: Go Back! (Copy/paste code)
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
				//END GO BACK
			} else {

				//System.out.println("DEBUG: (" + i + ", " + j + ")");
				//System.out.println(tableToString(addOneToTable(solution)));
				
				j++;
				if(j >= n) {
					j = 1;
					i++;
					
					if(i == n) {
						
						debugIter++;
						if(n <= 6 || debugIter % 100000 == 0) {
							int printedSolution[][] = addOneToTable(solution);
							
							System.out.println("Found solution:");
							System.out.println(tableToString(printedSolution));
							
						}
						
						numSolutions = numSolutions.add(TWO.pow(0));
						i--;
						j = n-1;
						//TODO: Go Back! (Copy/paste code)
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
						//END GO BACK
					}
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
