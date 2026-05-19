package getTopEigenvalue.tubeVersion;

import java.util.Hashtable;

public class GetTopEigenvalueMemoizeTubeSymBitFlipTrial {
	
	public static int TWO_POW_N_MINUS_1 = -1;
	public static int MASK_FOR_COMPLIMENT = -1;
	
	public static int rotateRight(int n, int numBits) {
		
		if( n % 2 == 1 ) { 
			return TWO_POW_N_MINUS_1 + (n >> 1);
		} else {
			return n>>1;
		}
	}
	
	public static int reverseNumber(int n, int numBits) {
		int ret = 0;
		int cur = n;
		
		for(int i=0; i<numBits; i++) {
			if( cur % 2 == 1) {
				ret = 2* ret + 1;
			} else {
				ret *= 2;
			}
			cur /= 2;
		}
		return ret;
	}
	
	public static int getNumGroups0(int num, int numBits) {
		
		boolean list[] = covertToBoolAnswers[num];
		
		int numSwitch = 1;
		boolean cur = false;
		for(int i=1; i<list.length; i++) {
			if(list[i] != cur) {
				numSwitch++;
				cur = !cur;
			}
		}
		
		return numSwitch/2;
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
		
		//TODO: copy/paste code to be quick:
		cur = reverseNumber(n, numBits);
		for(int i=0; i<numBits; i++) {
			cur = rotateRight(cur, numBits);
			if(cur < smallest) {
				smallest = cur;
			}
		}
		
		cur = (reverseNumber(n, numBits)) ^ MASK_FOR_COMPLIMENT;
		for(int i=0; i<numBits; i++) {
			cur = rotateRight(cur, numBits);
			if(cur < smallest) {
				smallest = cur;
			}
		}
		//END TODO: copy/paste code to be quick:
		
		return smallest;
	}
	
	public static double[] setupBestGuessEigenvectorCrude(int numBits, int countOrbits, Hashtable <Integer, Integer> mappingNumToIndex) {
		
		int numStates = (int)Math.pow(2, numBits);
		double vector[] = new double[countOrbits];
		
		for(int i=0; i<vector.length; i++) {
			vector[i] = -1.0;
		}
		
		for(int i=0; i<numStates; i++) {
			int num = NUM_TO_MIN_NUMBER_MAPPING[i];
			
			if(num == i) {
				int doubleIndex = mappingNumToIndex.get(num);
				
				if(num == 0) {
					vector[doubleIndex] = 1.0;
				} else {
					vector[doubleIndex] = Math.pow(0.65, getNumGroups0(num, numBits));
					System.out.println("Pre: " + num + " -> " + vector[doubleIndex]);
				}
			}
		}
		
		for(int i=0; i<vector.length; i++) {
			if(vector[i] < 0 ) {
				System.out.println("DOH!");
				System.exit(1);
			}
		}
		
		return vector;
	}
	
	public static void main(String[] args) {
		
		int NUM_BITS_TO_USE = 17;
		int NUM_IT = 30;
		

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
		
		TWO_POW_N_MINUS_1 = (int)Math.pow(2, numBits - 1);
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
		
		vector = setupBestGuessEigenvectorCrude(numBits, countOrbits, mappingNumToIndex);
		
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
		
		System.out.println("Final eigenvalue: " + curEigenvalue);
		
		System.out.println("Estimated growth rate: " + Math.pow(curEigenvalue, 1.0/(1.0 * numBits)));
		
		System.out.println("Debug frequency:");
		if(numBits <= 12) {
			for(int i=0; i<countOrbits; i++) {
				System.out.println(mappingIndexToNum.get(i) + " -> " + vector[i]);
			
			}
		}
		System.out.println("Debug 2nd one because it's tending to something as numbits increase!");
		System.out.println(mappingIndexToNum.get(1) + " -> " + vector[1]);
	}

	
	public static double[] multCurrentVection(double vector[], int numBits, Hashtable <Integer, Integer> mappingNumToIndex, Hashtable <Integer, Integer> mappingIndexToNum) {
		
		int LEFT_HAND_SIDE_CELL = (int)Math.pow(2, numBits - 1);
		int RELEVANT_TILES = (int)Math.pow(2, numBits) - 1;
		
		double newVector[] = new double[vector.length];
		
		for(int i=0; i<vector.length; i++) {

			int belowLayer = mappingIndexToNum.get(i);
			
			boolean checkAround = ((belowLayer & LEFT_HAND_SIDE_CELL) ^ ((belowLayer%2) << (numBits-1))) != 0;
			
			int lhsToCheck = (belowLayer ^ (belowLayer << 1));
			
			if(checkAround) {
				for(int j=0; j<Math.pow(2, numBits); ) {
					
					int tmpCheckProb = lhsToCheck & (belowLayer ^ j) & (~(belowLayer ^ (j << 1))) & RELEVANT_TILES;
					if( tmpCheckProb != 0) {
						//Collision:
						
						/*
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
						int getHighestBit = tmpCheckProb >> 1;
						//Copied algo that's O(log(size)) instead of just O(size):
						getHighestBit |= (getHighestBit >>  1);
						getHighestBit |= (getHighestBit >>  2);
						getHighestBit |= (getHighestBit >>  4);
						getHighestBit |= (getHighestBit >>  8);
						getHighestBit |= (getHighestBit >> 16);
						
						int answer = getHighestBit - (getHighestBit >>> 1);
					    j += answer;
						if(j<0) {
							System.exit(1);
						}
						continue;
					}
					
					boolean checkAroundJ = ((j & LEFT_HAND_SIDE_CELL) ^ ((j%2) << (numBits-1))) != 0;
					
					if(checkAroundJ && (j + belowLayer) % 2 == 1) {
						//Collision when wrapping around:
						j++;
						continue;
					}
					
					//No Collision:
					newVector[i] += vector[mappingNumToIndex.get(NUM_TO_MIN_NUMBER_MAPPING[j])];
					j++;
					
				}
			} else {
				//Copy-paste code in the hopes of getting an efficiency gain:
				for(int j=0; j<Math.pow(2, numBits); ) {
					
					int tmpCheckProb = lhsToCheck & (belowLayer ^ j) & (~(belowLayer ^ (j << 1))) & RELEVANT_TILES;
					if( tmpCheckProb != 0) {
						//Collision!
						
						int getHighestBit = tmpCheckProb >> 1;
						getHighestBit |= (getHighestBit >>  1);
						getHighestBit |= (getHighestBit >>  2);
						getHighestBit |= (getHighestBit >>  4);
						getHighestBit |= (getHighestBit >>  8);
						getHighestBit |= (getHighestBit >> 16);
						
						int answer = getHighestBit - (getHighestBit >>> 1);
						
					    j += answer;
						continue;
					}
					
					//No Collision:
					newVector[i] += vector[mappingNumToIndex.get(NUM_TO_MIN_NUMBER_MAPPING[j])];
					j++;
					
				}
				//END Copy-paste code in the hopes of getting an efficiency gain:
			}
			
		}
				
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