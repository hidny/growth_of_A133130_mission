package penAndPaperHelp;

public class SteamPuzzle3 {

	public static void main(String[] args) {

		solve();
	}

	
	public static int historyBoard[][][] = new int[50][5][5];
	
	
	public static int EMPTY = 0;
	public static int BLUE = 1;
	
	
	public static void solve() {
		
		for(int i=0; i<historyBoard[0].length; i++) {
			for(int j=0; j<historyBoard[1].length; j++) {
				if(i == 0 || j == 0 || i == historyBoard[0].length -1 || j == historyBoard[0][0].length - 1) {
					
					if(i == 4 && j==0) {
						solveWithStart(i, j);
					}
				}
			}
		}
		
		System.out.println("End program");
	}
	
	public static void clearBoard() {
		for(int i=0; i<historyBoard.length; i++) {
			for(int j=0; j<historyBoard[0].length; j++) {
				for(int k=0; k<historyBoard[0][0].length; k++) {
					historyBoard[i][j][k] = EMPTY;
				}
			}
		}
	}
	
	public static int[][] getNeighbours(int board[][], int i, int j) {
		
		int numRet = 0;
		for(int i2=i-1; i2<=i+1; i2++) {
			for(int j2=j-1; j2<=j+1; j2++) {
				if(i2 == i && j2 == j) {
					continue;
				}
				if(i2 != i && j2 != j) {
					continue;
				}
				
				if(i2 >=0 && j2>=0 && i2 <board.length && j2 < board[0].length) {
					numRet++;
				}
			}
		}
		
		int ret[][] = new int[numRet][2];
		
		int curIndex = 0;
		for(int i2=i-1; i2<=i+1; i2++) {
			for(int j2=j-1; j2<=j+1; j2++) {
				if(i2 == i && j2 == j) {
					continue;
				}
				if(i2 != i && j2 != j) {
					continue;
				}
				
				if(i2 >=0 && j2>=0 && i2 <board.length && j2 < board.length) {
					ret[curIndex][0] = i2;
					ret[curIndex][1] = j2;
					curIndex++;
				}
			}
		}
		
		return ret;
	}
	
	public static void solveWithStart(int i, int j) {
		clearBoard();
		historyBoard[0][i][j] = BLUE;
		solve(i, j, 1, "");
	}
	
	public static void setupFromPrevBoard(int depth) {
		for(int i=0; i<historyBoard[0].length; i++) {
			for(int j=0; j<historyBoard[0].length; j++) {
				historyBoard[depth][i][j] = historyBoard[depth-1][i][j];
			}
		}
	}
	
	public static int numSolutions = 0;
	
	public static int MAX_DEPTH = 20;
	
	public static void solve(int i, int j, int depth, String trail) {
		
		if(depth >= MAX_DEPTH) {
			return;
		}
		
		setupFromPrevBoard(depth);
		
		if(trail.equals("")) {
			trail += "( " + i + ", " + j + ")";
		} else {
			trail += ", ( " + i + ", " + j + ")";
		}
		if(trail.equals("( 0, 0)")) {
			System.out.println("yes debug");
		}
		if(depth < 5) {
			System.out.println(trail);
		}
		int board[][] = historyBoard[depth];
		
		
		int choices[][] = getNeighbours(board, i, j);
		
		
		for(int k=0; k<choices.length; k++) {
			if(board[choices[k][0]][choices[k][1]] != BLUE) {
				
				
				board[choices[k][0]][choices[k][1]] = BLUE;
				
				if(boardStillValid(board)) {
					
					if(boardIsDone(board)) {
						System.out.println("Done with max depth " + depth + "!");
						System.out.println("Trail:");
						String trailTmp = "";
						trailTmp = trail + ", ( " + choices[k][0] + ", " + choices[k][1] + ")";
						
						
						System.out.println(trailTmp);
						System.out.println();
						System.out.println();
						
						if(trail.equals("( 4, 4), ( 3, 4), ( 2, 4), ( 2, 3), ( 2, 2), ( 3, 2), ( 4, 2), ( 4, 1), ( 4, 0), ( 3, 0), ( 2, 0), ( 1, 0)")) {
							
							System.out.println("boardIsDone:");
							boardIsDone(board);
						}
						numSolutions++;
					} else {
						solve(choices[k][0], choices[k][1], depth + 1, trail);
					}
					
				}
				

				
				board[choices[k][0]][choices[k][1]] = historyBoard[depth-1][choices[k][0]][choices[k][1]];
			}
		}
		
	}
	
	
	
	
	public static boolean boardStillValid(int board[][]) {
		
		for(int i=0; i<5; i++) {
			if( ! validCheck(board, i, 4, true) || ! validCheck(board, i, 4, false)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean validCheck(int board[][], int index, int target, boolean isVert) {
		

		int count = 0;
		for(int i=0; i<board.length; i++) {
			if(isVert) {
				if(board[i][index] == BLUE) {
					count++;
				}
			} else {
				if(board[index][i] == BLUE) {
					count++;
				}
				
			}
		}
		
		return count <= target;
		
	}
	public static boolean boardIsDone(int board[][]) {
		
		//return quadIsDone(board, 0, 1) && quadIsDone(board, 1, 2) && quadIsDone(board, 2, 3) && quadIsDone(board, 3, 4);
		
		for(int i=0; i<5; i++) {
			if( ! sumCheck(board, i, 4, true) || ! sumCheck(board, i, 4, false)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean sumCheck(int board[][], int index, int target, boolean isVert) {
		
		int count = 0;
		for(int i=0; i<board.length; i++) {
			if(isVert) {
				if(board[i][index] == BLUE) {
					count++;
				}
			} else {
				if(board[index][i] == BLUE) {
					count++;
				}
				
			}
		}
		
		return target == count;
		
		
	}
}
