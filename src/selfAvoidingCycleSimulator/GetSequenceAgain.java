package selfAvoidingCycleSimulator;

import java.math.BigInteger;

public class GetSequenceAgain {

	// I matched A133130, but it's not clear how...
	//June 6th, 2026
	//I feel like I did this before, and I probably did it more efficiently too.
	//AHA: I did it in misselaneous.growthRateMultLoops.GetNumWaysToGridWithMultLoops.java
	
	public static void main(String[] args) {

		solve(3);
	}

	
	public static void solve(int n) {
		
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
		
		for(int i=1; i<n; i++) {
			
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
		for(int i=0; i<curList.length; i++) {
			sum = sum.add(curList[i]);
		}
		
		System.out.println("Sum for n = " + n + ": " + sum);
		
	}
}
