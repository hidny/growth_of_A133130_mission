package getTopTriangleEigenvector;

import java.util.Hashtable;

public class GetTrigTopEigenTubeSymBitFlipTrial {
	
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
		
		return tmpCheckProb & NON_POWER_4_BITS;
	}

	public static int NON_POWER_4_BITS = 0;
	static {
		NON_POWER_4_BITS = 0;
		for(int i=0; i<14; i++) {
			NON_POWER_4_BITS = 4*NON_POWER_4_BITS + 2;
		}
		
	}
	
	//TODO: BUG: I reversed top and bottom!
	//TOOD: does that make a diff?
	
	public static double[] multCurrentVection(double vector[], int numBits, Hashtable <Integer, Integer> mappingNumToIndex, Hashtable <Integer, Integer> mappingIndexToNum) {
		
		int RELEVANT_TILES = (int)Math.pow(2, numBits) - 1;
		int RELEVANT_TILES_NOT_LEFTMOST = (int)Math.pow(2, numBits - 1) - 1;
		
		double newVector[] = new double[vector.length];

		long debugNumIter = 0;
		
		for(int i=0; i<vector.length; i++) {

			int belowLayer = mappingIndexToNum.get(i);
			
			int extendedBottom = belowLayer + (belowLayer << numBits);

			int bottomLeftValues = (extendedBottom >> 1) & NON_POWER_4_BITS;
			int bottomMidValues = extendedBottom & NON_POWER_4_BITS;
			int bottomRightValues = (extendedBottom << 1) & NON_POWER_4_BITS;
			
			
			int debugJSkip = 0;
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				debugNumIter++;
				
				int extendedTop = j + (j << numBits);
		
				int topLeftValues = (extendedTop >> 2) & NON_POWER_4_BITS;
				int topMidValues = (extendedTop >> 1) & NON_POWER_4_BITS;
				int topRightValues = extendedTop & NON_POWER_4_BITS;
				
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
					
					int answerisLeftMostTile = (answer >> (numBits-1)) & 1;
					int answerNotLeftMost = (answer  & RELEVANT_TILES_NOT_LEFTMOST) >> 1;
					
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

				    
				    
				    //Final eigenvalue for triangle version: 106.5817459006501
				    //Estimated growth rate for triangle version: 1.5950278960753408
				    //Final eigenvalue for triangle version: 6.458813769776312
				    //Estimated growth rate for triangle version: 1.5941830624788615
					
					//j++;

				    //Final eigenvalue for triangle version: 6.458813769776312
				    //Estimated growth rate for triangle version: 1.5941830624788615
				    //Final eigenvalue for triangle version: 106.5817459006501
					//Estimated growth rate for triangle version: 1.5950278960753408					
					
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

//Shot in the dark that are not correct:
//(2^(1/3) e^(2/3) log^(5/6)(2))/(log^(4/3)(3)) approx 1.595027838271
//or:
//root of 4 x^4 - 41 x^3 + 51 x^2 + 13 x - 10 near x = 1.59503 approx 1.595027839819

//N=24:
//Final eigenvalue for triangle version: 73525.58048237748
//Estimated growth rate for triangle version: 1.5950278455365183

//N=22
//Final eigenvalue for triangle version: 28900.271663531104
//Estimated growth rate for triangle version: 1.5950278458233007

//N=20
//Final eigenvalue for triangle version: 11359.661278052241
//Estimated growth rate for triangle version: 1.5950278449572721

//N=18:
//Final eigenvalue for triangle version: 4465.075619053574
//Estimated growth rate for triangle version: 1.5950278398038673

//N=16:
//Final eigenvalue for triangle version: 1755.0624537400415
//Estimated growth rate for triangle version: 1.59502790692782

//N = 14:
//Final eigenvalue for triangle version: 689.8540504307512
//Estimated growth rate for triangle version: 1.595028225687307


//Debug 2nd one because it's tending to something as numbits increase!
//1 -> 0.5122395166311167

//N=24:

//Current eigenvalue: 73525.58048211866
//Current eigenvalue: 73525.58048238617
//Current eigenvalue: 73525.58048237412
//Current eigenvalue: 73525.58048237495
//Current eigenvalue: 73525.58048237521
//Current eigenvalue: 73525.58048237753
//Current eigenvalue: 73525.58048237702
//Current eigenvalue: 73525.58048237542
//Current eigenvalue: 73525.58048237761
//Current eigenvalue: 73525.58048237748
//Current eigenvalue: 73525.58048237543

//Current eigenvalue: 73525.58048237761
//