package selfAvoidingCycleSimulator;

import java.math.BigInteger;
import java.util.Random;

public class BasicSquareSimulator {

	//TODO:
	// Test that transpose looks similar to result.
	//TODO: transpose is statistically different! AHH!!!
	
	
	//TODO:
	
	//Paint regions diff coluus...
	//TODO:
	//rewrite to use num possibilities to gen random result?
	
	// No 42 equals close enough to random.
	public static Random random = new Random(30);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int N = 400;
		int PADDING = 100;
		
		//int PRINT_START = 0;
		int NUM_LINES = N;
		int NUM_COLUMNS = N;
		
		boolean start[] = getStart(NUM_COLUMNS);
		boolean reformat[] = reformatArrayForNo2x2SameOrViceVersa(start, 0);
		
		basicPrint(start);
		basicPrint(reformat);
		
		for(int i=0; i<fib.length; i++) {
			System.out.println(fibBig[i]);
		}
		
		BigInteger numSolutions = getNumSolutions(start);
		
		System.out.println("Num Solutions: " + numSolutions);
		
		//TODO: take a random num from 0 to numSolutions - 1, then get the random continuation.
		
		//TODO: make sure the numbers from  the function in this class is alligned with what's found in the
		// matrices in other files.

		boolean prev[];
		boolean cur[] = start;
		
		boolean mapThatNeedsPaddingRemoved[][] = new boolean[NUM_LINES][start.length];
		for(int i=0; i<NUM_LINES; i++) {

			mapThatNeedsPaddingRemoved[i] = reformatArrayForNo2x2SameOrViceVersa(cur, i);
			//System.out.println();
			
			//basicPrint(reformatArrayForNo2x2SameOrViceVersa(cur, i));
			//basicPrint(cur);
			//System.out.println();
			
			prev = cur;
			cur = createNextLayerNo2x2Match(cur);
			
			for(int j=1; j<cur.length; j++) {
				if(prev[j] == prev[j-1] && prev[j] == cur[j] && cur[j] == cur[j-1]) {
					System.out.println("Doh! Found 2x2 substring");
					System.exit(1);
				}
			}
		}
		
		boolean insideMap[][] =  new boolean[NUM_LINES - 2*PADDING][NUM_COLUMNS - 2*PADDING];
	
		for(int i=0; i<insideMap.length; i++) {
			for(int j=0; j<insideMap[0].length; j++) {
				insideMap[i][j] = mapThatNeedsPaddingRemoved[i + PADDING][j + PADDING];
			}
		}

		System.out.println();
		System.out.println();
		System.out.println("Printing inside map:");
		for(int i=0; i<insideMap.length; i++) {
			basicPrint(insideMap[i]);
		}
		
		int regionIndex=0;
		
		int numMap[][] = new int[insideMap.length][insideMap[0].length];
		for(int i=0; i<insideMap.length; i++) {
			for(int j=0; j<insideMap[0].length; j++) {
				numMap[i][j] = -1;
			}
		}
		
		for(int i=0; i<insideMap.length; i++) {
			for(int j=0; j<insideMap[0].length; j++) {
				
				if(insideMap[i][j] && numMap[i][j] == -1) {
					numMap[i][j] = regionIndex;
					depthFirstSearchMark(insideMap, numMap, i, j, regionIndex);
					regionIndex++;
				}
			}
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		for(int i=0; i<insideMap.length; i++) {
			String ret = "";
			for(int j=0; j<insideMap[0].length; j++) {
				
				if(insideMap[i][j]) {
					ret += getLabelFromIndex(numMap[i][j]);
				} else {
					ret += "_";
				}
			}
			System.out.println(ret);
		}

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		int numHoriCross = 0;
		int numVertCross = 0;
		
		boolean explored[][] = new boolean[insideMap.length][insideMap[0].length];
		
		for(int i=0; i<insideMap.length; i++) {
			
			depthFirstSearchCheckEdges(insideMap, explored, i, 0);
			
			for(int i2=0; i2<insideMap.length; i2++) {
				if(explored[i2][insideMap[0].length - 1]) {
					System.out.println("Horizontal cross with label: " + getLabelFromIndex(numMap[i2][insideMap[0].length - 1]));
					numHoriCross++;
					break;
				}
			}
			for(int i2=0; i2<insideMap.length; i2++) {
				explored[i2][insideMap[0].length - 1] = false;
			}
			
		}
		
		explored = new boolean[insideMap.length][insideMap[0].length];
		
		for(int j=0; j<insideMap[0].length; j++) {
			
			depthFirstSearchCheckEdges(insideMap, explored, 0, j);
			
			for(int j2=0; j2<insideMap[0].length; j2++) {
				if(explored[insideMap.length - 1][j2]) {
					System.out.println("Vertical cross with label: " + getLabelFromIndex(numMap[insideMap.length - 1][j2]));
					
					numVertCross++;
					break;
				}
			}
			for(int j2=0; j2<insideMap.length; j2++) {
				explored[insideMap.length - 1][j2] = false;
			}
			
		}
		

		System.out.println("Number of vertical cross regions: " + numVertCross);
		System.out.println("Number of Horizontal cross regions: " + numHoriCross);
	}
	
	public static char getLabelFromIndex(int index) {
		char retChar = (char)( 'a' + (index % 26));
		if(index % 52 >= 26) {
			retChar = (char)(retChar + 'A' - 'a');
		}
		return retChar;
	}
	
	
	public static boolean[][] depthFirstSearchCheckEdges(boolean map[][], boolean explored[][], int curi, int curj) {
		
		for(int i2=curi-1; i2<=curi+1; i2++) {
			for(int j2=curj-1; j2<=curj+1; j2++) {
				
				if(!(i2 == curi ^ j2 == curj)) {
					continue;
				}
				
				if(i2 >=0 && j2 >=0 && i2 <map.length && j2 < map[0].length) {
					
					if(!explored[i2][j2] && map[i2][j2]) {
						explored[i2][j2] = true;
						explored = depthFirstSearchCheckEdges(map, explored, i2, j2);
					}
				}
			}
		}
		
		return explored;
	}
	
	
	public static int[][] depthFirstSearchMark(boolean map[][], int numMap[][], int curi, int curj, int index) {
		
		
		for(int i2=curi-1; i2<=curi+1; i2++) {
			for(int j2=curj-1; j2<=curj+1; j2++) {
				
				if(!(i2 == curi ^ j2 == curj)) {
					continue;
				}
				
				if(i2 >=0 && j2 >=0 && i2 <map.length && j2 < map[0].length) {
					
					if(numMap[i2][j2] == -1 && map[i2][j2]) {
						numMap[i2][j2] = index;
						numMap = depthFirstSearchMark(map, numMap, i2, j2, index);
					}
				}
			}
		}
		
		return numMap;
	}
	

	public static boolean[] createNextLayer(boolean curLayerIntersectAvoid[], int curAdjustment) {
		//BigInteger numSolutions = getNumSolutions(curLayer);
		
		//System.out.println("Num Solutions: " + numSolutions);
		
		boolean reformatedCur[] = createNextLayerNo2x2Match(reformatArrayForNo2x2SameOrViceVersa(curLayerIntersectAvoid, curAdjustment));
		
		//System.out.println("Hello");
		return reformatArrayForNo2x2SameOrViceVersa(reformatedCur, curAdjustment+1);
	}
	
	public static boolean[] createNextLayerNo2x2Match(boolean curLayerNo2x2Match[]) {

		
		boolean nextLayer[] = new boolean[curLayerNo2x2Match.length];
		
		for(int i=0; i<nextLayer.length;) {
			
			boolean prevMatch = false;
			
			int lengthRun = getLengthOfRun(curLayerNo2x2Match, i);
			
			for(int j=0; j < lengthRun; j++) {
				
				if(prevMatch) {
					nextLayer[i + j] = ! curLayerNo2x2Match[i + j];
					prevMatch = false;
					continue;
				}
			
				long numPos = fib[(lengthRun-j) + 1];
				long numPosMatchAbove = fib[(lengthRun-j) - 1];
				
				boolean shouldMatch = (numPos * Math.random()) < numPosMatchAbove;
				
				
				nextLayer[i + j] = !(shouldMatch ^ curLayerNo2x2Match[i + j]);
				prevMatch = shouldMatch;
				
			}
			
			i += lengthRun;
			
		}
		
		return nextLayer;
	}
	
	public static int getLengthOfRun(boolean arrayNo2x2[], int index) {
		int ret = 1;
		
		//basicPrint(arrayNo2x2);
		
		for(int i=index+1; i<arrayNo2x2.length && arrayNo2x2[i] == arrayNo2x2[index]; i++) {
			ret++;
		}
		//System.out.println(ret);
		
		return ret;
	}
	
	public static boolean[] getStart(int n) {
		boolean array[] = new boolean[n];
		
		
		int SIXTY_FOUR_BITS = 64;
		
		for(int i=0; i<n;) {
			
			long randomNum = random.nextLong();
			for(int j=0; j + i < n && j<SIXTY_FOUR_BITS; j++) {
				array[i + j] = randomNum % 2 == 1;
				randomNum = randomNum >>> 1;
			}
			i+= SIXTY_FOUR_BITS;
		}
		
		return array;
	}
	
	public static void basicPrint(boolean array[]) {
		String tmp = "";
		for(int i=0; i<array.length; i++) {
			if(array[i]) {
				tmp += "#";
			} else {
				tmp += "_";
			}
		}
		System.out.println(tmp);
	}
	
	public static boolean[] reformatArrayForNo2x2SameOrViceVersa(boolean array[], int adjustment) {
		boolean ret[] = new boolean[array.length];
		
		
		for(int i=0; i<array.length; i++) {
			ret[i] = array[i] ^ ((adjustment + i) % 2 == 0);
		}
		
		return ret;
	}

	public static BigInteger fibBig[] = new BigInteger[100];
	public static long fib[] = new long[100];
	
	static {
		fib[0] = 1;
		fib[1] = 1;
		
		for(int i=2; i<fib.length; i++) {
			fib[i] = fib[i-1] + fib[i-2];
		}
		
		for(int i=0; i<fibBig.length; i++) {
			fibBig[i] = new BigInteger("" + fib[i]);
		}
	}
	
	
	public static BigInteger getNumSolutions(boolean array[]) {
		
		boolean reformat[] = reformatArrayForNo2x2SameOrViceVersa(array, 0);
		
		BigInteger ret = BigInteger.ONE;
		
		for(int i=0; i<reformat.length; ) {
			
			boolean start = reformat[i];
			int curLength = 0;
			
			do {
				curLength++;
				i++;
			} while(i < array.length && start == reformat[i]);
			
			ret = ret.multiply(fibBig[1 + curLength]);
			
			//System.out.println("curLength: " + curLength);
		}
		
		//##___#_######_####_#_#_#___##_#______##____#__##__#_#___####_####_#####_#_#_#__#____#___#__##_#___#_##_#_###_###_#__##_###_#####_####____###_###_######___#__#_#_##_##_##__#_##_#_###_#__#_##______###__

		
		return ret;
	}
}
