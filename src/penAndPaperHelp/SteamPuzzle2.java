package penAndPaperHelp;

public class SteamPuzzle2 {

	public static void main(String[] args) {

		solve();
	}

	
	public static int historyBoard[][][] = new int[50][5][5];
	
	
	public static int EMPTY = 0;
	public static int BLUE = 1;
	public static int RED = 2;
	
	
	public static void solve() {
		
		for(int i=0; i<historyBoard[0].length; i++) {
			for(int j=0; j<historyBoard[1].length; j++) {
				if(i == 0 || j == 0 || i == historyBoard[0].length -1 || j == historyBoard[0][0].length - 1) {
					solveWithStart(i, j);
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
		if(trail.equals("( 0, 0), ( 0, 1), ( 0, 2)")) {
			System.out.println("yes debug");
		}
		if(depth < 5) {
			System.out.println(trail);
		}
		int board[][] = historyBoard[depth];
		
		
		int choices[][] = getNeighbours(board, i, j);
		boolean turnRed[] = new boolean[choices.length];
		
		for(int k=0; k<choices.length; k++) {
			if(board[choices[k][0]][choices[k][1]] == EMPTY) {
				turnRed[k] = true;
			}
		}
		
		for(int k=0; k<choices.length; k++) {
			if(board[choices[k][0]][choices[k][1]] != RED) {
				
				//setup for next recursion:
				for(int k2=0; k2<choices.length; k2++) {
					if(turnRed[k2] && k2 != k) {
						board[choices[k2][0]][choices[k2][1]] = RED;
						
						
					}
				}
				
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
				

				//destroy for next recursion:
				for(int k2=0; k2<choices.length; k2++) {
					if(turnRed[k2] && k2 != k) {
						
						if( historyBoard[depth-1][choices[k2][0]][choices[k2][1]] != EMPTY) {
							System.out.println("Doh1");
							System.exit(1);
						}
						
						board[choices[k2][0]][choices[k2][1]] = EMPTY;
						
						
					}
				}
				board[choices[k][0]][choices[k][1]] = historyBoard[depth-1][choices[k][0]][choices[k][1]];
			}
		}
		
	}
	
	
	
	/*public static boolean boardStillValid(int board[][]) {
		
		//return valid(board, 0, 1) && valid(board, 1, 2) && valid(board, 2, 3) && valid(board, 3, 4);
	}
	
	public static boolean valid(int board[][], int quadIndex, int target) {
		
		/*int numBlue = 0;
		int numRed = 0;
		
		int startI = 2*(quadIndex / 2);
		int startJ = 2*(quadIndex % 2);
		
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				
				if(i ==1 && j == 1) {
					continue;
				}
				
				if(i!=1 && j !=1) {
					continue;
				}
				
				int iCoord = i + startI;
				int jCoord = j + startJ;
				
				if(board[iCoord][jCoord] == BLUE) {
					numBlue++;
				} else if(board[iCoord][jCoord] == RED) {
					numRed++;
				}
				
			}
		}
		
		if(4 - numRed >= target && numBlue <= target) {
			return true;
		} else {
			return false;
		}
		
	}*/
	
	public static boolean boardStillValid(int board[][]) {
		
		return validCheck(board, 1, 2, true) && validCheck(board, 3, 2, true) && validCheck(board, 4, 3, true)&& validCheck(board, 0, 1, false) && validCheck(board, 2, 2, false)&& validCheck(board, 4, 4, false);
	}
	
	public static boolean validCheck(int board[][], int index, int target, boolean isVert) {
		
		int countBlue = 0;
		int countNotBlue = 0;
		for(int i=0; i<board.length; i++) {
			int colour = -1;
			if(isVert) {
				colour = board[i][index];
			} else {
				colour = board[index][i];
				
			}
			if(colour == BLUE) {
				countBlue++;
			}
			if(colour != BLUE) {
				countNotBlue++;
			}
		}
		
		return target <= 5 - countBlue && countNotBlue >= target;
	}
	public static boolean boardIsDone(int board[][]) {
		
		//return quadIsDone(board, 0, 1) && quadIsDone(board, 1, 2) && quadIsDone(board, 2, 3) && quadIsDone(board, 3, 4);
		
		return sumCheck(board, 1, 2, true) && sumCheck(board, 3, 2, true) && sumCheck(board, 4, 3, true)&& sumCheck(board, 0, 1, false) && sumCheck(board, 2, 2, false)&& sumCheck(board, 4, 4, false);
	}
	
	public static boolean sumCheck(int board[][], int index, int target, boolean isVert) {
		
		int count = 0;
		for(int i=0; i<board.length; i++) {
			if(isVert) {
				if(board[i][index] != BLUE) {
					count++;
				}
			} else {
				if(board[index][i] != BLUE) {
					count++;
				}
				
			}
		}
		
		return target == count;
		/*
		int numBlue = 0;

		int startI = 2*(quadIndex / 2);
		int startJ = 2*(quadIndex % 2);
		
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				
				if(i ==1 && j == 1) {
					continue;
				}
				
				if(i!=1 && j !=1) {
					continue;
				}
				
				int iCoord = i + startI;
				int jCoord = j + startJ;
				
				if(board[iCoord][jCoord] == BLUE) {
					numBlue++;
				}
				
			}
		}
		
		if(numBlue == target) {
			return true;
		} else {
			return false;
		}*/
		
	}
}
