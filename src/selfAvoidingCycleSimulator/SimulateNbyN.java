package selfAvoidingCycleSimulator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Set;

public class SimulateNbyN {

	// I matched A133130, but it's not clear how...
	//June 6th, 2026
	//I feel like I did this before, and I probably did it more efficiently too.
	//AHA: I did it in misselaneous.growthRateMultLoops.GetNumWaysToGridWithMultLoops.java

	/*
	 * Finally generated random sample:
	 * Drawing random solutions:
Key:
N=15
##__##_#_####__
____##___#__###
____#__#_####__
###____###__##_
###____####____
#_#_##____###_#
#_#_##__#______
#___#######_##_
###_#_#####____
#_____####___##
###_######___##
##__#___###_###
____#___#___#_#
_#_###__#_#_###
#######_###_#__
 
 N=17
###_##___#___##_#
_##__#_#_#_#____#
_###___#_#_##_###
__#______#__#_#__
_###_##_____#____
_##__###_####_##_
_#__####___####__
##___#________##_
#____#_###__#_###
#_######________#
########___#___##
##_#_#_#_#______#
____________#__##
##_#_#______#___#
_#############___
##_#__####___#__#
_######_##_____##
	 */
/*
 * N=18:
#######__#________
###__##_###_##__##
#######___####____
____________##____
_#___####____#___#
_#________#_##_###
####_##___#______#
___###______###__#
#___###_#___###_##
#_#_#_#_###_____##
____###_#_#_######
#_#___#_#_#_#_____
#_#####_____#___##
#_#####____######_
#_#___#_#__##_#___
____#####_###_____
##______#_#####_##
_#####_##__##_#___
 */
	/* N=20:
	 * 
__###_####_#######__
#__#__###____#______
#_###__##___##_##___
#_##____##__#######_
#__####______#####__
#_________##_____#__
___#___##_#####__#__
_###___####_###_####
##_#___##_______##_#
#__######__##_#__#__
________##__#_#_____
###__##__##___#__##_
_##___#___##_____#__
#####_###_##_#_____#
##__#_#___#####_####
____#####___#_____##
__###___#_####____##
#__##______####_#__#
#_##########_#__##__
#####_##__##___####_
	 */
/* N=21:
#_____##__###____###_
___#_####_#_#_#___###
_#______#_#_###_#__##
___###__######____##_
##___########__#___#_
_#########_###_#_####
__#__###_______####_#
_##__#_____###_#____#
_##_##___#####___#_##
##########_#_________
______########___###_
_##_#__#_____#_#__##_
###_#_##_###_####__##
_##____###___#_##__##
#####__####_________#
_#__#__#######_######
_#__########___##__##
_#_##____####___####_
___###_#_##_#__###___
#__#___#______##_#__#
#_####_##_##_##______
 */
	
	public static Hashtable<String, Integer> debugCounter = new Hashtable<String, Integer>();
	
	public static void main(String[] args) {

		System.out.println("Drawing random solutions:");
		//for(int i=0; i<1000000; i++) {
			//drawRandom(4);
		//}
	
		for(int i=0; i<1000; i++) {
			System.out.println("i: " + i);
			drawRandom(10);
		}
		
		//drawRandom(18);
		
		Set<String> keySet = debugCounter.keySet();
		
		int debugHoriSame =0;
		int debugVertSame =0;
		long numHash = 0;
		long numUnderScores = 0;

		
		
		int numKeys = 0;
		// Loop through the keys
        for (String key : keySet) {
            System.out.println("Key:\n" + key);
            System.out.println("Values: " + debugCounter.get(key));
            numKeys++;
            
            int horiCharLength = (int)(Math.sqrt(key.length())) + 1;
            
            if(horiCharLength * (horiCharLength-1) != key.length()) {
            	System.out.println("OOPS!");
            }
            
            
            for(int i=0; i<key.length(); i++) {
            	if(key.charAt(i) != '#' && key.charAt(i) != '_') {
            		continue;
            	}
            	if(i > 0 && key.charAt(i) == key.charAt(i-1)) {
            		debugHoriSame++;
            	}

            	if(i >= horiCharLength && key.charAt(i) == key.charAt(i-horiCharLength)) {
            		debugVertSame++;
            	}
            	
            	if(key.charAt(i) == '#') {
            		numHash++;
            	} else if(key.charAt(i) == '_') {
            		numUnderScores++;
            	}
            }
        }
        
        System.out.println("Number of keys: " + numKeys);
        
        System.out.println("Debug counts:");
		System.out.println("debugHoriSame: " + debugHoriSame);
		System.out.println("debugVertSame: " + debugVertSame);
		System.out.println("numHash: " + numHash);
		System.out.println("numUnderScores: " + numUnderScores);
        
	}
	
		
	public static void drawRandom(int length) {
		
		String ret = "";
		int lastLayerBinary = -1;
		for(int rowIndex=length-1; rowIndex>=0; rowIndex--) {
			
			lastLayerBinary = solve(length, rowIndex, lastLayerBinary);
			
			//Just print the last row 1st. It shouldn't really matter:
			ret = getLine(lastLayerBinary, length) + ret;
			
		}
		//System.out.println(ret);
		
		if(debugCounter.containsKey(ret)) {
			int numInc = debugCounter.get(ret) + 1;
			debugCounter.remove(ret);
			debugCounter.put(ret, numInc);
			
		} else {
			debugCounter.put(ret, 1);
		}
		
	}
	
	public static String getLine(int rowBinary, int numBits) {
		
		int cur = rowBinary;
		String ret = "";
		for(int i=0; i<numBits; i++) {
			
			if(cur % 2 == 0) {
				ret += "#";
			} else {
				ret += "_";
			}
			cur /= 2;
		}
		return ret + "\n";
		
	}

	public static SecureRandom randomSource = new SecureRandom();
	
	public static BigInteger sanityPredictedNextSum = BigInteger.ZERO;
	
	public static int solve(int n, int rowIndex, int nextLayer) {
		
		BigInteger curList[] = new BigInteger[(int)Math.pow(2, n + 1)];
		
		if(curList.length % 2 !=0 ) {
			System.out.println("oops!");
			System.exit(1);
		}
		
		for(int i=0; i < curList.length; i++) {
			if( i < curList.length / 2 ) {
				curList[i] = BigInteger.ONE;
			} else {
				curList[i] = BigInteger.ZERO;
			}
		}
		
		
		BigInteger prevList[];
		
		for(int i=0; i<rowIndex; i++) {
			
			for(int j=0; j<n; j++) {
				prevList = curList;
				curList = new BigInteger[(int)Math.pow(2, n + 1)];
				
				for(int k=0; k<curList.length; k++) {
					curList[k] = BigInteger.ZERO;
				}

				//System.out.println();
				//System.out.println("j = " + j);
				
				if(j == 0) {
					
					for(int k=0; k<prevList.length; k++) {
						
						int nextVar0 = k;
						if(k % Math.pow(2, j+1) >= Math.pow(2, j)) {
							nextVar0 |= ((int)Math.pow(2, n));
						} else {
							nextVar0 &= ~((int)Math.pow(2, n));
						}
						int nextVar1 = nextVar0 ^ ((int)Math.pow(2, j));
						
						curList[nextVar0] = curList[nextVar0].add(prevList[k]);
						curList[nextVar1] = curList[nextVar1].add(prevList[k]);
					}
					
					
				} else if(j < n){

					for(int k=0; k<prevList.length; k++) {
						
						int nextVar0 = k;
						if(k % Math.pow(2, j+1) >= Math.pow(2, j)) {
							nextVar0 |= ((int)Math.pow(2, n));
						} else {
							nextVar0 &= ~((int)Math.pow(2, n));
						}
						int nextVar1 = nextVar0 ^ (int)Math.pow(2, j);
						
						//int nextRight0 = (nextVar0 >> j) & 1; 
						int nextRight1 = (nextVar1 >> j) & 1; 
						
						int nextLeft =  (nextVar1 >> (j-1)) & 1;
				
						int prevLeft = (k >> n) & 1;
						int prevRight = (k >> j) & 1;
						
						//if(!(prevRight == nextLeft && prevLeft == nextRight0 && prevLeft != prevRight)) {
						curList[nextVar0] = curList[nextVar0].add(prevList[k]);
						//}
						
						if(!(prevRight == nextLeft && prevLeft == nextRight1 && prevLeft != prevRight)) {
							curList[nextVar1] = curList[nextVar1].add(prevList[k]);
						
						}
					}
					
				}// else if(j == n -1) {
					//TODO: special wrap around condition.
					
				//}
				
				
			}
		}
		

		BigInteger sum = BigInteger.ZERO;
		if(rowIndex + 1 >= n) {
			for(int i=0; i<curList.length; i++) {
				sum = sum.add(curList[i]);
			}
			
			//System.out.println("Sum for n = " + n + " and row index " + rowIndex + ": " + sum);
		
		} else {
			
			int nextLayerInv = ~nextLayer;
			
			int maskToCheck = (int)Math.pow(2, n-1) - 1;
			
			for(int i=0; i<curList.length; i++) {
				

				int prevLayerCheck = (i ^ (i >> 1)) & maskToCheck;
				
				int nextLayerCheck = (nextLayerInv ^ (nextLayerInv >> 1)) & maskToCheck;
				
				if((prevLayerCheck & nextLayerCheck & (i ^ nextLayer)) == 0) {
					sum = sum.add(curList[i]);
				}
			}
			
			//System.out.println("Sum for n = " + n + " and row index " + rowIndex + ": " + sum);
		
			if(sum.compareTo(sanityPredictedNextSum) != 0) {
				System.out.println("rowIndex: " + rowIndex);
				System.out.println("Sanity check failed! (sanityPredictedNextSum)");
				System.out.println("sanityPredictedNextSum:\n" + sanityPredictedNextSum);
				System.out.println("sum:\n" + sum);
				System.exit(1);
			}
			
			
		}
		
		int numBitsRandom = sum.bitLength() + 15;
		
		BigInteger randomNumber;
		
		BigInteger twoPowerN = BigInteger.ONE.shiftLeft(numBitsRandom);
		
		BigInteger rejectMin = twoPowerN.divideAndRemainder(sum)[1];
		
		BigInteger indexAnswer = null;
		
		do {
		    randomNumber = new BigInteger(numBitsRandom, randomSource);
		    
		    indexAnswer = randomNumber.divideAndRemainder(sum)[1];
		    
		} while (randomNumber.compareTo(rejectMin) < 0);
		
		//System.out.println("Random index:\n" + indexAnswer);
		//System.out.println("Sum:\n" + sum);
		
		
		
		BigInteger curSum = BigInteger.ZERO;
		int ret = -1;
		
		if(rowIndex + 1 >= n) {
			for(int i=0; i<curList.length; i++) {
				curSum = curSum.add(curList[i]);
				
				if(curSum.compareTo(indexAnswer) > 0) {
					ret = i;
					sanityPredictedNextSum = curList[i].add(curList[i ^ ((int)Math.pow(2, n))]);
					break;
				}
			}
			
		
		} else {
			
			int nextLayerInv = ~nextLayer;
			
			int maskToCheck = (int)Math.pow(2, n-1) - 1;
			
			for(int i=0; i<curList.length; i++) {

				int prevLayerCheck = (i ^ (i >> 1)) & maskToCheck;
				
				int nextLayerCheck = (nextLayerInv ^ (nextLayerInv >> 1)) & maskToCheck;
				
				if((prevLayerCheck & nextLayerCheck & (i ^ nextLayer)) == 0) {
					curSum = curSum.add(curList[i]);
					
					if(curSum.compareTo(indexAnswer) > 0) {
						ret = i;
						sanityPredictedNextSum = curList[i].add(curList[i ^ ((int)Math.pow(2, n))]);
						break;
					}
					
				}
			}
			
			
		}
		
		if(ret == -1) {
			System.out.println("Doh! Ret == -1 ");
			System.exit(1);
		}
		
		
		return ret;
	}
}
