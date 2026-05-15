package getTopEigenvalue.tubeVersion;

import java.util.Hashtable;

public class GetTopEigenvalueMemoizeTubeSym {

	
	
	public static int rotateRight(int n, int numBits) {
		
		if( n % 2 == 1 ) { 
			return ((int)Math.pow(2, numBits-1)) + (n >> 1);
		} else {
			return n>>1;
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
		
		//TODO: LATER: Do complement and then do reflection.
		
		return smallest;
	}
	
	public static void main(String[] args) {
		
		initializePow2();
		
		int NUM_IT = 30;
		//int PERIOD_DEBUG_PER_LOOP = Math.max(1, NUM_IT/10);
		int PERIOD_DEBUG_PER_LOOP = 1;
		
		for(int numBits=15; numBits<=15; numBits++) {
			
			System.out.println("Memorizing the answers to convert Bool:");
			setupCovertToBoolAnswers(numBits);
			System.out.println("Done Memorizing the answers to convert Bool:");
			
			
			System.out.println();
			System.out.println("N = " + numBits + ":");
			
			//boolean used[] = new boolean[(int)Math.pow(2, n)];
			
			//double vector[] = new double[(int)Math.pow(2, n)];
			//for(int i=0; i<vector.length; i++) {
			//	vector[i] = 1.0;
			//}
			//for(int i=0; i<used.length; i++) {
			//	used[i] = false;
			//}
			
			int countOrbits = 0;
			int numStatesPow2 = (int)Math.pow(2, numBits);
			
			for(int i=0; i<numStatesPow2; i++) {
				if( i == getMinNumberConsideringNumBits(i, numBits)) {
					countOrbits++;
				}
			}
			
			
			Hashtable <Integer, Integer> mappingNumToIndex = new Hashtable <Integer, Integer>();
			Hashtable <Integer, Integer> mappingIndexToNum = new Hashtable <Integer, Integer>();
			int indexOrbits = 0;
			
			for(int i=0; i<numStatesPow2; i++) {
				int minNum = getMinNumberConsideringNumBits(i, numBits);
				
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
				int doubleIndex = mappingNumToIndex.get(getMinNumberConsideringNumBits(i, numBits));

				vector[doubleIndex] += 1.0;
			}
			System.out.println("Vector:");
			for(int i=0; i<vector.length; i++) {
				System.out.println(vector[i]);
			}
			//TODO i to min# to index.
			
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
			
		}
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
						newVector[i] += vector[mappingNumToIndex.get(getMinNumberConsideringNumBits(j, numBits))];
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
