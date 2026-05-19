package latinSquares;

import java.math.BigInteger;

//TODO: it's still skeleton code!
public class LatinSquare2by2Efficiency {

	public static void main(String[] args) {
		
		BigInteger numSolutions = solve(4);
		
		System.out.println("Number of solutions found: " + numSolutions);

	}
	
	public static final int UNDETERMINED = -1;
	
	public static int[][] initTable(int n) {

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
	
	public static int[][] initTalbePrevDigitMap(int n) {
		int digitIFromRowIOutputJ[][] = new int[n][n];
		
		for(int j=0; j<n; j++) {
			digitIFromRowIOutputJ[j][0] = j;
		}
		
		for(int i=0; i<n; i++) {
			digitIFromRowIOutputJ[i][i] = 0;
		}
		
		
		return digitIFromRowIOutputJ;
	}
	
	public static BigInteger solve(int n) { 
		
		if(n<2) {
			return BigInteger.ONE;
		}
		
		int solution[][] = initTable(n);
		
		System.out.print("Start cofig:");
		System.out.println(tableToString(solution));
		
		boolean used2x2[][] = new boolean[solution.length][solution.length];
		int digitIFromRowIOutputTopLeftI[][] = new int[solution.length][solution.length];
		int digitIFromRowIOutputTopLeftJ[][] = new int[solution.length][solution.length];
		boolean isBottomRight2x2[][] = new boolean[solution.length][solution.length];;
		int curNum2x2Boxes = 0;
		
		int digitIFromRowIOutputJ[][] = initTalbePrevDigitMap(n);
		
		
		
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

			//System.out.println("New loop " + i + ", " + j);
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
				boolean twoByTwoDenied = false;
				for(int prevI=1; prevI<i; prevI++) {
					
					int prevJ = digitIFromRowIOutputJ[k][prevI];
					
					/*if(k== 0) {
						System.out.println("TEST k = 0 (2)");

						int printedSolution[][] = addOneToTable(solution);
						System.out.println(tableToString(printedSolution));
						System.out.println("END TEST k = 0 (2)");
					}*/
					
					if(prevJ > 0 && prevJ < j && solution[i][prevJ] == solution[prevI][j] ) {
						/*if(k== 0) {
							System.out.println("TEST k = 0");
							int printedSolution[][] = addOneToTable(solution);
							System.out.println(tableToString(printedSolution));
							System.out.println("END TEST k = 0");
						}*/
						
						if(k == solution[prevI][prevJ] && ! used2x2[prevI][prevJ] && ! used2x2[i][prevJ] && ! used2x2[prevI][j]) {
							
							if(solution[i][prevJ] < k) {
							
								//TODO: BUG! you have to swap it and check if the swapped square would have a 2x2 block exactly 
								// where this 2x2 block is.
								
								System.out.println("DENIAL TEST for k = " + (k+1));
								int printedSolution[][] = addOneToTable(solution);
								System.out.println(tableToString(printedSolution));
								System.out.println(prevJ + ", " +prevI + " to " + i + ", " + j);
								System.out.println("END DENIAL TEST");
								twoByTwoDenied = true;
								break;
								
							} else {
								System.out.println("???");

								if(used2x2[i][j]) {
									System.out.println("oops 1!");
									System.exit(1);
								}
								
								used2x2[prevI][prevJ] = true;
								used2x2[i][prevJ] = true;
								used2x2[prevI][j] = true;
								used2x2[i][j] = true;

								isBottomRight2x2[i][j] = true;
								curNum2x2Boxes++;
								
								//int indexused2x2PrevI[][] = new int[solution.length][solution.length];
								//int indexused2x2PrevJ[][] = new int[solution.length][solution.length];
								//int curNum2x2Boxes = 0;
								digitIFromRowIOutputTopLeftI[k][i] = prevI;
								digitIFromRowIOutputTopLeftJ[k][i] = prevJ;
								
								System.out.println("----------");
								System.out.println("TEST 2x2:");
								System.out.println("Made box: " + prevI + ", " + prevJ + " to " + i + ", " + j);
								int printedSolution[][] = addOneToTable(solution);
								System.out.println(tableToString(printedSolution));
								System.out.println("New k: " + (k+1));
								System.out.println("END TEST 2x2:");
								System.out.println("----------");
								
							}
						}
					}
				}
				if(twoByTwoDenied) {
					System.out.println("DENIAL TEST2");
					continue;
				}
				
				digitIFromRowIOutputJ[k][i] = j;
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
							
							System.out.println("Found solution with weight: " + TWO.pow(curNum2x2Boxes));
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

				if(isBottomRight2x2[i][j]) {
					isBottomRight2x2[i][j] = false;
					curNum2x2Boxes--;
					
					int oldI = digitIFromRowIOutputTopLeftI[solution[i][j]][i];
					int oldJ = digitIFromRowIOutputTopLeftJ[solution[i][j]][i];
					used2x2[i][j] = false;
					used2x2[oldI][j] = false;
					used2x2[i][oldJ] = false;
					used2x2[oldI][oldJ] = false;
					System.out.println("Remove 2x2 box 1 for (i, j) = " + i + ", " + j + " (solution[i][j]: " + solution[i][j] + ")");
					System.out.println("(oldi, oldj) = " + oldI + ", " + oldJ);
					
				}
				if(used2x2[i][j]) {
					System.out.println("Oops2! (i, j) = " + i + ", " + j);
					System.exit(1);
				}
				
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
				

				if(isBottomRight2x2[i][j]) {
					isBottomRight2x2[i][j] = false;
					curNum2x2Boxes--;

					int oldI = digitIFromRowIOutputTopLeftI[solution[i][j]][i];
					int oldJ = digitIFromRowIOutputTopLeftJ[solution[i][j]][i];
					used2x2[i][j] = false;
					used2x2[oldI][j] = false;
					used2x2[i][oldJ] = false;
					used2x2[oldI][oldJ] = false;
					System.out.println("Remove 2x2 box 2 for (i, j) = " + i + ", " + j);
					System.out.println("(oldi, oldj) = " + oldI + ", " + oldJ);
					
				}
				if(used2x2[i][j]) {
					System.out.println("Oops3! (i, j) = " + i + ", " + j);
					System.exit(1);
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
