package getTopTriangleEigenvector;

//TODO: why is this much slower than original!!!

import java.util.Hashtable;

public class GetTrigTopEigenTubeSymBitFlipTrial2 {
	
	public static int MASK_FOR_COMPLIMENT = -1;
	
	public static int rotateRight(int n, int numBits) {
		
		return (n >> 2) + ((n << (numBits-2)) & MASK_FOR_COMPLIMENT);
		
	}
	
	
	public static int[] NUM_TO_MIN_NUMBER_MAPPING;
	
	public static void setupNumToMinNumberMapping(int numBits) {
		
		NUM_TO_MIN_NUMBER_MAPPING = new int[(int)Math.pow(2, numBits)];
		
		for(int i=0; i<NUM_TO_MIN_NUMBER_MAPPING.length; i++) {
			NUM_TO_MIN_NUMBER_MAPPING[i] = getMinNumberConsideringNumBits(i, numBits);
		}
	}
	
	public static int getMinNumberConsideringNumBits(int n, int numBits) {
		
		int cur = n;
		int smallest = n;
		for(int i=0; i<numBits; i++) {
			cur = rotateRight(cur, numBits);
			if(cur < smallest) {
				smallest = cur;
			}
		}
		
		cur = n ^ MASK_FOR_COMPLIMENT;
		for(int i=0; i<numBits; i++) {
			cur = rotateRight(cur, numBits);
			if(cur < smallest) {
				smallest = cur;
			}
		}
		
		
		return smallest;
	}
	
	
	//1.59502790692782
	//1.5950279069278188
	//Wrong now:
	//1.5916444275957222
	
	//Right now:
	//NUM iterations: 121202039 ( 4mins)
	//1.5950279069278188
	//Orig:
	//NUM iterations:  11076120
	//1.59502790692782
	
	public static void main(String[] args) {
		
		int NUM_BITS_TO_USE = 16;
		int NUM_IT = 30;
		
		if(NUM_BITS_TO_USE % 2 != 0) {
			System.out.println("ERROR: please use an even number of bits!");
		}

		int PERIOD_DEBUG_PER_LOOP = 1;
		if(NUM_BITS_TO_USE < 15) {
			int MULT_TEMP = 100;
			if(NUM_BITS_TO_USE >= 12) {
				MULT_TEMP = 10;
			}
			NUM_IT *= MULT_TEMP;
			PERIOD_DEBUG_PER_LOOP *= MULT_TEMP;
		}
		
		int numBits = NUM_BITS_TO_USE;
		
		MASK_FOR_COMPLIMENT = (int)Math.pow(2, numBits) - 1;
		
		System.out.println("Memorizing the answers to convert Bool:");
		setupNumToMinNumberMapping(NUM_BITS_TO_USE);
		setupCovertToBoolAnswers(numBits);
		System.out.println("Done Memorizing the answers to convert Bool:");
		
		
		System.out.println();
		System.out.println("N = " + numBits + ":");
		
		int countOrbits = 0;
		int numStatesPow2 = (int)Math.pow(2, numBits);
		
		for(int i=0; i<numStatesPow2; i++) {
			if( i == NUM_TO_MIN_NUMBER_MAPPING[i]) {
				countOrbits++;
			}
		}
		
		
		Hashtable <Integer, Integer> mappingNumToIndex = new Hashtable <Integer, Integer>();
		Hashtable <Integer, Integer> mappingIndexToNum = new Hashtable <Integer, Integer>();
		int indexOrbits = 0;
		
		for(int i=0; i<numStatesPow2; i++) {
			int minNum = NUM_TO_MIN_NUMBER_MAPPING[i];
			
			if(i == minNum) {
				mappingNumToIndex.put(i, indexOrbits);
				mappingIndexToNum.put(indexOrbits, i);
				
				System.out.println(i + " --> " + indexOrbits);
				System.out.println();
				
				indexOrbits++;
			}
		}

		double vector[] = new double[countOrbits];
		for(int i=0; i<numStatesPow2; i++) {
			int doubleIndex = mappingNumToIndex.get(NUM_TO_MIN_NUMBER_MAPPING[i]);

			vector[doubleIndex] += 1.0;
		}
		System.out.println("Vector:");
		for(int i=0; i<vector.length; i++) {
			System.out.println(vector[i]);
		}
		
		//Bare minimum to adjust vector to reasonable numbers:
		for(int i=0; i<vector.length; i++) {
			vector[i] = 1.0;
			
		}
		
		
		//TODO: adjust vectors to even better guess so that the doesn't need as many iterations.
		
		System.out.println("Number of orbits: " + countOrbits);
		
		
		double curEigenvalue = 0.0;
		for(int i=0; i<NUM_IT; i++) {
			vector = multCurrentVection(vector, numBits, mappingNumToIndex, mappingIndexToNum);
			curEigenvalue = vector[0];
			vector = normalizeVector(vector);
			
			if(i % PERIOD_DEBUG_PER_LOOP == 0) {
				
				System.out.println("Current eigenvalue: " + curEigenvalue);
			}
		}
		
		System.out.println("Final eigenvalue for triangle version: " + curEigenvalue);
		
		System.out.println("Estimated growth rate for triangle version: " + Math.pow(curEigenvalue, 1.0/(1.0 * numBits)));
		
		System.out.println("Debug frequency:");
		if(numBits <= 12) {
			for(int i=0; i<countOrbits; i++) {
				System.out.println(mappingIndexToNum.get(i) + " -> " + vector[i]);
			
			}
		}
		System.out.println("Debug 2nd one because it's tending to something as numbits increase!");
		System.out.println(mappingIndexToNum.get(1) + " -> " + vector[1]);
	}

	
	public static int checkProb(int topLeftValues, int topMidValues, int topRightValues, int bottomRightValues, int bottomMidValues, int bottomLeftValues) {
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
		
		return tmpCheckProb & POWER_OF_4_BITS_MINUS_1;
	}

	public static int POWER_OF_4_BITS_MINUS_1 = 0;
	//public static int UP_SIDE_DOWN_TRIANGLES = 0;
	static {
		POWER_OF_4_BITS_MINUS_1 = 0;
		for(int i=0; i<14; i++) {
			POWER_OF_4_BITS_MINUS_1 = 4*POWER_OF_4_BITS_MINUS_1 + 1;
		}
		//UP_SIDE_DOWN_TRIANGLES = RIGHT_SIDE_UP_TRIANGLES >> 1;
		POWER_OF_4_BITS_MINUS_1 = POWER_OF_4_BITS_MINUS_1 - 1;
	}
	
	
	public static long debugNumIter = 0;
	//TODO: BUG: I reversed top and bottom!
	//TOOD: does that make a diff?
	
	public static double[] multCurrentVection(double vector[], int numBits, Hashtable <Integer, Integer> mappingNumToIndex, Hashtable <Integer, Integer> mappingIndexToNum) {
		
		int RELEVANT_TILES = (int)Math.pow(2, numBits+2) - 1;
		int RELEVANT_TILES_NOT_LEFTMOST = (int)Math.pow(2, numBits) - 1;
		
		double newVector[] = new double[vector.length];
		
		debugNumIter = 0;
		for(int i=0; i<vector.length; i++) {

			int aboveLayer = mappingIndexToNum.get(i);

			if(aboveLayer == 5) {
				System.out.println("DEBUG");
			}
			
			//TODO: I reversed top/bottom. I didn't think this through...
			//FOR EXAMPLE: the j skip might be wrong now...
			//TODO: something seems off!
			int extendedTop = aboveLayer + (aboveLayer << numBits);
	
			int topLeftValues = (extendedTop >> 1) & POWER_OF_4_BITS_MINUS_1;
			int topMidValues = (extendedTop ) & POWER_OF_4_BITS_MINUS_1;
			int topRightValues = (extendedTop << 1) & POWER_OF_4_BITS_MINUS_1;
			
			
			int debugJSkip = 0;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				debugNumIter++;
				int extendedBottom = j + (j << numBits);

				int bottomLeftValues = (extendedBottom) & POWER_OF_4_BITS_MINUS_1;
				int bottomMidValues = (extendedBottom << 1) & POWER_OF_4_BITS_MINUS_1;
				int bottomRightValues = (extendedBottom << 2) & POWER_OF_4_BITS_MINUS_1;
				//1st idea: over all 32 combos:
				
				int tmpCheckProb = checkProb(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
				
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
					//find the index to the right of the leftmost bit:
					int getHighestBit = tmpCheckProb & RELEVANT_TILES;
					//Copied algo that's O(log(size)) instead of just O(size):
					getHighestBit |= (getHighestBit >>  1);
					getHighestBit |= (getHighestBit >>  2);
					getHighestBit |= (getHighestBit >>  4);
					getHighestBit |= (getHighestBit >>  8);
					getHighestBit |= (getHighestBit >> 16);
					
					//Estimated growth rate for triangle version: 1.6028038616652438
					
					int answer = getHighestBit - (getHighestBit >>> 1);
					
					//The two variables below are made to get rid of one if condition about how to handle case
					//where the leftmost bit interferes with rightmost tile.
					
					int answerisLeftMostTile = (answer >> (numBits)) & 3;
					int answerNotLeftMost = (answer  & RELEVANT_TILES_NOT_LEFTMOST) >> 2;
					
					if(answerisLeftMostTile + answerNotLeftMost == 0) {
						System.out.println(tmpCheckProb);
					}
					if(!(answerNotLeftMost > 0 ^ answerisLeftMostTile > 0)) {
						System.out.println("Doh!: " + tmpCheckProb);
						System.out.println("Doh!: " + answerNotLeftMost);
						System.out.println("Doh!: " + answerisLeftMostTile);
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
					//System.out.println(belowLayer + " and " + j + " no collision");
					if(j < debugJSkip) {
						System.out.println("DOH debug J skip!");
						System.exit(1);
					}
					//No Collision:
					newVector[i] += vector[mappingNumToIndex.get(NUM_TO_MIN_NUMBER_MAPPING[j])];
					j++;
					
				}
				
			}
			
		}
		
		System.out.println("NUM iterations: " + debugNumIter);
		System.out.println("RIGHT_SIDE_UP_TRIANGLES: " + POWER_OF_4_BITS_MINUS_1);
		return newVector;
	}
	
	
	public static double[] normalizeVector(double vector[]) {
		
		double newVector[] = new double[vector.length];
		
		for(int i=0; i<vector.length; i++) {
			newVector[i] = vector[i] / vector[0];
		}
		
		return newVector;
	}

	public static boolean[][] covertToBoolAnswers;
	
	public static void setupCovertToBoolAnswers(int numBinaryDigits) {
		covertToBoolAnswers = new boolean[(int)Math.pow(2, numBinaryDigits)][];
		
		for(int i=0; i<covertToBoolAnswers.length; i++) {
			covertToBoolAnswers[i] = convertToBoolSlow(i, numBinaryDigits);
		}
		
	}

	public static boolean[] convertToBoolSlow(int num, int numBinaryDigits) {
		
		boolean ret[] = new boolean[numBinaryDigits];
		
		int curIndex = numBinaryDigits -1;
		while(curIndex >= 0) {
			
			if(num % 2 == 0) {
				ret[curIndex] = false;
			} else {
				ret[curIndex] = true;
			}
			num /= 2;
			curIndex--;
			
		}
		
		return ret;
		
	}
}
