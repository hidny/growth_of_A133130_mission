package getTopEigenvalue.tubeVersion;

import java.util.Hashtable;

//Ideas to make it go faster:
// 1 ) save mapping results!
// 2) Figure out where to start when comparing two layers (not always at index 0)
// 2.1) Save carry 1 info in byte array
// 3) make decent guess at eigenvector to being with. maybe 0.6^#pairs of switches


public class GetTopEigenvalueMemoizeTubeSym {

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
	
	public static void main(String[] args) {
		
		initializePow2();
		
		int NUM_BITS_TO_USE = 4;
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
		
		for(int numBits=NUM_BITS_TO_USE; numBits<=NUM_BITS_TO_USE; numBits++) {
			
			TWO_POW_N_MINUS_1 = (int)Math.pow(2, numBits - 1);
			MASK_FOR_COMPLIMENT = (int)Math.pow(2, numBits) - 1;
			
			System.out.println("Memorizing the answers to convert Bool:");
			setupCovertToBoolAnswers(numBits);
			System.out.println("Done Memorizing the answers to convert Bool:");
			
			
			System.out.println();
			System.out.println("N = " + numBits + ":");
			
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
			//255   -> 0.6523609648053742
			//
			//15 bits: 0.6523519681590982
			//16 bits: 0.6523519548560871
			//17 bits: 0.652351949818637
			//18 bits: 0.6523519478962301
			//19 bits: 0.6523519471574133
			//20 bits: 0.6523519468719053 Estimated growth rate: N=20 1.8003313250318116
			//
			//22 bits: 0.6523519467176729 Estimated growth rate: N=22 1.8003313245717636
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

//Notes:
// Num bits=10 and 11
// mersenne nums are about 0.6 of the blank index for some reason
//

//TODO: Chart this! I think there's a quantum like orbitals...
//TODO: figure out a model for predicting the ball-park of these values.
//hint: definitely look at the binary sequaence.
/*
 * 0 -> 1.0
1 -> 0.6523530799300028
3 -> 0.6032853194810823
5 -> 0.4382664734570725
7 -> 0.6005871745237986
9 -> 0.4259007610541303
11 -> 0.4122422617171067
15 -> 0.599978277644776
17 -> 0.4256423105411273
19 -> 0.3942966375859586
21 -> 0.2957874230142158
23 -> 0.41102777333167134
27 -> 0.3929374028380487
31 -> 0.5998597810423094
33 -> 0.42557543835805944
35 -> 0.393713282069051
37 -> 0.286214939879606
39 -> 0.3925755975261789
43 -> 0.2792522490842242
45 -> 0.2818882860483179
47 -> 0.4107588291672537
51 -> 0.3657857148338451
55 -> 0.3922776106173019
67 -> 0.39360182925541465
69 -> 0.2859830205955336
71 -> 0.3919806232367851
73 -> 0.27806877129009044
75 -> 0.26929612083807236
77 -> 0.26945636215862456
79 -> 0.3922064967183102
83 -> 0.26515230544553337
85 -> 0.19984812673076222
87 -> 0.27855484784520085
91 -> 0.268874767507979
93 -> 0.28136731664890413
99 -> 0.36444076273176024
103 -> 0.3643291143217508
107 -> 0.2645000371080668
119 -> 0.39172506094123355
137 -> 0.2779412206681381
139 -> 0.269114117895138
147 -> 0.2575398846734057
149 -> 0.19323046444146985
151 -> 0.26860672707929417
153 -> 0.2577575345154298
155 -> 0.2569464783973418
157 -> 0.2687520299521409
163 -> 0.2647141462044592
165 -> 0.19243974597998886
167 -> 0.26414574337963653
171 -> 0.18894839827907345
173 -> 0.19106847497902715
179 -> 0.25016727097935854
199 -> 0.36317573150768956
203 -> 0.24984696715928306
219 -> 0.25683292648513545
293 -> 0.18707308413504817
299 -> 0.18275380990528706
301 -> 0.18438510562570432
307 -> 0.23954996132966547
331 -> 0.1815280528267774
339 -> 0.17949718635972842
341 -> 0.13529382095650117

 */

/*
// TODO: untested custom hash: (It might be useful, but I'm not sure)
public static boolean customHash[] = null;

public static int getMaxNumBitsOrbit(int numOrbits) {
	int curLength = 0;
	
	int cur = 1;
	int MULT = 2;
	
	while(numOrbits >= cur) {
		curLength++;
		cur *= MULT;
	}
	
	return curLength;
}

public static void setupCustomHash(int maxBitsNumOrbits, int numBits) {
	
	customHash = new boolean[maxBitsNumOrbits * ((int)Math.pow(2, numBits))];
	
	for(int i=0; i<(int)Math.pow(2, numBits); i++) {
		
		int min = getMinNumberConsideringNumBits(i, numBits);
		
		boolean table[] = convertToBoolSlow(min, maxBitsNumOrbits);
		
		for(int j=0; j<maxBitsNumOrbits; j++) {
			customHash[maxBitsNumOrbits * i + j] = table[j];
		}
	}
}

public static int getHash(int num, int maxBitsNumOrbits) {
	int ret = 0;
	
	for(int i=maxBitsNumOrbits*num; i<maxBitsNumOrbits*(num+1); i++) {
		
		if(customHash[i]) {
			ret = 2*ret + 1;
		} else {
			ret *= 2;
		}
	}
	return ret;
}*/

