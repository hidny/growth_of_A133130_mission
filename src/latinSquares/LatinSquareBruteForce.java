package latinSquares;

public class LatinSquareBruteForce {

	
	public static void main(String[] args) {
		
		long numSolutions = solve(4);
		
		System.out.println("Number of solutions found: " + numSolutions);

	}
	
	public static int[][] initTalbe(int n) {

		int solution[][] = new int[n][n];
		
		
		for(int i=0; i<n; i++) {
			solution[0][i] = i + 1;
			solution[i][0] = i + 1;
		}
		return solution;
	}
	
	public static long solve(int n) { 
		
		if(n<2) {
			return 1;
		}
		
		int solution[][] = initTalbe(n);
		
		System.out.println(tableToString(solution));
		
		return addDigit(1, solution, 2);
		
	}
	
	public static long addDigit(int rowNumber, int solution[][], int digit) {
		
		if(digit > solution.length) {
			
			boolean legitSolutionSoFar = true;
			
			//Handle the 1 digit
			boolean used[] = new boolean[solution.length];
			
			ADDING_ONE:
			for(int i=1; i<solution.length; i++) {
				for(int j=1; j<solution.length; j++) {
					
					if(solution[i][j] == 0) {
						if(used[j] == false) {
							used[j] = true;
							break;
						} else {
							legitSolutionSoFar = false;
							break ADDING_ONE;
						}
					}
					
				}
			}
			
			if(legitSolutionSoFar) {
				
				numSolutionsFoundGlobal++;
				
				if(numSolutionsFoundGlobal % 1000000 == 0 || solution.length < 6) {
					//PRINT
					String solutionString = tableToString(solution);
					solutionString = solutionString.replace("0", "1");
					
					System.out.println(numSolutionsFoundGlobal + " solutions found:");
					System.out.println(solutionString);
					//END PRINT
				}
				
				return 1L;
			} else {
				return 0L;
			}
		} else if(rowNumber >= solution.length) {
			return addDigit(1, solution, digit + 1);
			
		} else if(rowNumber + 1 == digit) {
			return addDigit(rowNumber + 1, solution, digit);
		}
		
		long ret = 0L;
		
		for(int j=1; j<solution.length; j++) {
			
			if(solution[rowNumber][j] != 0) {
				continue;
			}
			
			boolean couldAddSoFar = true;
			for(int i=0; i<rowNumber; i++) {
				if(solution[i][j] == digit) {
					couldAddSoFar = false;
					break;
				}
			}
			
			if(couldAddSoFar) {
				solution[rowNumber][j] = digit;
				
				ret += addDigit(rowNumber + 1, solution, digit);

				solution[rowNumber][j] = 0;
			}

		}
		
		return ret;
		
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
