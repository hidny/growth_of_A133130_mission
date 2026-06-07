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

	public static Hashtable<String, Integer> debugCounter = new Hashtable<String, Integer>();
	
	public static void main(String[] args) {

		System.out.println("Drawing random solutions:");
		//for(int i=0; i<1000000; i++) {
			//drawRandom(4);
		//}
	
		for(int i=0; i<10000000; i++) {
			drawRandom(2);
		}
		
		
		Set<String> keySet = debugCounter.keySet();
		
		int numKeys = 0;
		// Loop through the keys
        for (String key : keySet) {
            System.out.println("Key:\n" + key);
            System.out.println("Values: " + debugCounter.get(key));
            numKeys++;
        }
        
        System.out.println("Number of keys: " + numKeys);
        
        
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
						break;
					}
					
				}
			}
			
			
		}
		
		if(ret == -1) {
			System.out.println("Doh! Ret == -1 ");
			System.exit(1);
		}
		
		//TODO:
		return ret;
	}
}
