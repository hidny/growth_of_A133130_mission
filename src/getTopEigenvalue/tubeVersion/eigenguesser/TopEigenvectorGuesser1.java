package getTopEigenvalue.tubeVersion.eigenguesser;

import java.util.Hashtable;

public class TopEigenvectorGuesser1 {

	//TODO: for numBits <12, try num_it =300
	// What happens to the top eigenvector?
	// I think there's group of similar values, but I don't get it yet.
	
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
		
		if(num == 5461) {
			System.out.println("Debug!");
		}
		int numSwitch = 1;
		boolean cur = false;
		for(int i=0; i<list.length; i++) {
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
		
		//int mask = (int)Math.pow(2, numBits) - 1;

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
	
	public static double getPredictionOfEigenvectorValueForState(int num, int numBits, Hashtable <Integer, Integer> mappingNumToIndex) {
		double ret = 0.0;
		if(num == 0) {
			ret = 1.0;
		} else {
			ret = Math.pow(0.65, getNumGroups0(num, numBits));
		}
		
		return ret;
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
				
				vector[doubleIndex] = getPredictionOfEigenvectorValueForState(num, numBits, mappingNumToIndex);
				
				System.out.println("Prediction of eigenvector value: " + num + " -> " + vector[doubleIndex]);
				
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
		
		initializePow2();
		
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
		System.out.println("State Number (convert binary),eigenvector value, predicted eigenvector");
		if(numBits <= 17) {
			for(int i=0; i<countOrbits; i++) {
				System.out.println(mappingIndexToNum.get(i) + ", " + vector[i] + ", " + getPredictionOfEigenvectorValueForState(mappingIndexToNum.get(i), numBits, mappingNumToIndex));
			
			}
		}
		System.out.println("Debug 2nd one because it's tending to something as numbits increase!");
		System.out.println(mappingIndexToNum.get(1) + "-> " + vector[1]);
		//255   -> 0.6523609648053742
		//
		//15 bits: 0.6523519681590982
		//16 bits: 0.6523519548560871
		//17 bits: 0.652351949818637
		//18 bits: 0.6523519478962301
		//19 bits: 0.6523519471574133
		//20 bits: 0.6523519468719053 Estimated growth rate: N=20 1.8003313250318116
	}


	public static void initializePow2() {
		pow2 = new int[30];
		
		pow2[0] = 1;
		
		for(int i=1; i<pow2.length; i++ ) {
			pow2[i] = 2 * pow2[i-1];
		}
	}
	public static int pow2[] = null;
	
	
	
	
	public static double[] multCurrentVection(double vector[], int numBits, Hashtable <Integer, Integer> mappingNumToIndex, Hashtable <Integer, Integer> mappingIndexToNum) {
		
		double newVector[] = new double[vector.length];
		
		for(int i=0; i<vector.length; i++) {
			
			boolean bitsI[] = covertToBoolAnswers[mappingIndexToNum.get(i)];
			
			for(int j=0; j<Math.pow(2, numBits); ) {
				
				boolean bitsJ[] = covertToBoolAnswers[j];
				
				int badIndex = 0;
				boolean foundBad2x2Subspace = false;
				for(int k=0; k<bitsI.length - 1; k++) {
					if(bitsI[k] != bitsI[k+1] && bitsI[k] == bitsJ[k+1] && bitsI[k+1] == bitsJ[k]) {
						foundBad2x2Subspace = true;
						badIndex = bitsI.length - 2 - k;
						break;
					}
				}
				
				
				if( ! foundBad2x2Subspace) {
					//Tube check:
					if(bitsI[0] != bitsI[bitsI.length - 1] && bitsI[bitsI.length - 1] == bitsJ[0] &&   bitsI[0] == bitsJ[bitsI.length - 1]) {
						//pass!
					} else {
						newVector[i] += vector[mappingNumToIndex.get(NUM_TO_MIN_NUMBER_MAPPING[j])];
					}
				}

				j += pow2[badIndex];
				
				
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

//Notes:
// Num bits=10 and 11
// mersenne nums are about 0.6 of the blank index for some reason
//

