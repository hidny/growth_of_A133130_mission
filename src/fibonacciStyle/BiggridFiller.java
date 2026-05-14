package fibonacciStyle;

public class BiggridFiller {

	public static void main(String[] args) {
		setupFibonnaci();
		
		//sanityTests();
		
		double constantGuess = testForConstant(1000, 1000);
	
		//Guess at the constant: 1.7951362393174088 (too low)
		System.out.println("Guess at the constant: " + constantGuess);
	}
	
	public static double testForConstant(int size, int iteration) {
		

		boolean prevList[] = new boolean[size];
		for(int i=0; i<prevList.length; i++) {
			prevList[i] = ((int)(Math.floor(2*Math.random())) == 1);
		}
		
		for(int it=0; it<iteration; it++) {

			boolean curList[] = new boolean[size];
			
			int runLeft= 0;
			
			for(int j=0; j<curList.length; j++) {
				
				if(runLeft == 0) {
					runLeft = getRun(prevList, j);
				}

				//Sanity:
				if(getRun(prevList, j) != runLeft) {
					System.out.println("DOH 1!");
					System.exit(1);
				}
				//End sanity
				
				double probMatch = getProbMatchAtIndex(runLeft);
				//System.out.println("Prob Match: " + probMatch);
				
				if(Math.random() < probMatch) {
					curList[j] = prevList[j];
					
					runLeft--;
					if(runLeft > 0) {

						j++;
						
						//Sanity:
						if(getRun(prevList, j) != runLeft) {
							System.out.println("DOH 2!");
							System.exit(1);
						}
						//End sanity
						
						curList[j] = ! prevList[j];
						
						runLeft--;
					}
					
				} else {
					curList[j] = ! prevList[j];
					runLeft--;
				}
				
				
			}
			//printList(prevList);
			sanityCheckNo2x2(prevList, curList);
			prevList = curList;
		}

		//printList(prevList);
		
		return getLogMultiplicity(prevList);
		
	}
	
	public static void sanityCheckNo2x2(boolean prevList[], boolean curList[]) {
		
		for(int i=0; i<prevList.length -1; i++) {
			
			if(prevList[i] == prevList[i+1] && curList[i] == curList[i+1] && curList[i] == prevList[i]) {
				System.out.println("DOH 3!");
				System.exit(1);
			}
		}
		
	}
	
	public static void printList(boolean list[]) {
		
		String ret = "";
		for(int i=0; i<list.length; i++) {
			if(list[i]) {
				ret += "1";
			} else {
				ret += "0";
			}
		}
		System.out.println(ret);
	}
	
	public static void sanityTests() {
		boolean listTest[] = new boolean[1000];
		
		for(int i=0; i<listTest.length; i++) {
			listTest[i] = (i % 2 == 0);
		}
		
		System.out.println("Should be about 2: " + getLogMultiplicity(listTest));

		//boolean listTest2[] = new boolean[1000000000];
		boolean listTest2[] = new boolean[10000000];
		int numOne = 0;
		int numTotal = 0;
		for(int i=0; i<listTest2.length; i++) {
			listTest2[i] = ((int)(Math.floor(2*Math.random())) == 1);
			numTotal++;
			if(listTest2[i]) {
				numOne++;
			}
		}
		
		//TODO: At least figure out what this is supposed to be
		//1.7706656394277887
		//1.770669958614007
		System.out.println("Should be less than 2. but I don't know: " + getLogMultiplicity(listTest2));
		System.out.println("Num ones: " + numOne + " num total: " + numTotal);
		
		

		boolean listTest3[] = new boolean[1000];
		
		for(int i=0; i<listTest3.length; i++) {
			listTest3[i] = (i % 4 >= 2);
		}
		
		System.out.println("Should be less than 2 (somehow got sqrt(3)): " + getLogMultiplicity(listTest3));
		

		boolean listTest4[] = new boolean[10000];
		
		for(int i=0; i<listTest4.length; i++) {
			listTest4[i] = (i % 6 >= 3);
		}
		
		System.out.println("Should be less than 2 (should be cube root 5): " + getLogMultiplicity(listTest4));
		

		boolean listTest5[] = new boolean[10000];
		
		for(int i=0; i<listTest5.length; i++) {
			listTest5[i] = (i % 8 >= 4);
		}
		
		System.out.println("Should be less than 2 (should be 8^(1/4)): " + getLogMultiplicity(listTest5));
		

		boolean listTest6[] = new boolean[10000];
		
		for(int i=0; i<listTest6.length; i++) {
			listTest6[i] = (i % 10 >= 5);
		}
		
		System.out.println("Should be less than 2 (should be 13^(1/5)): " + getLogMultiplicity(listTest6));
	}
	
	public static final int NUM_FIB = 100;
	
	public static double fib[] = new double[NUM_FIB];
	
	public static void setupFibonnaci() {
		fib[0] = 1.0;
		fib[1] = 1.0;
		
		for(int i=0; i+2<NUM_FIB; i++) {
			fib[i+2] = fib[i+1] + fib[i];
		}
	}
	
	public static double phi = 1.6180339887498948482;
	
	public static double getProbMatchAtIndex(int lengthOfRunLeft) {
		if(lengthOfRunLeft + 1 < NUM_FIB) {
			return fib[lengthOfRunLeft - 1] / fib[lengthOfRunLeft + 1];
		} else {
			return 1.0 / (phi*phi);
		}
	}
	
	public static double getLogNumPossibleForRun(int lengthOfRun) {
		if(lengthOfRun + 1 < NUM_FIB) {
			return Math.log(fib[lengthOfRun + 1]);
		} else {
			return Math.log(Math.pow(phi, lengthOfRun + 1));
		}
	}
	
	public static boolean [] fillGrid(int size) {
		
		return null;
		
	}
	
	public static double getLogMultiplicity(boolean list[]) {
		
		double ret = 0.0;
		
		for(int curIndex=0; curIndex<list.length;) {
			
			int run = getRun(list, curIndex);
			
			ret += getLogNumPossibleForRun(run);
			
			curIndex += run;
		}
		
		double logAvg =  ret / ((1.0)*(list.length));
		
		return Math.pow(Math.E, logAvg);
		
	}
	
	public static int getRun(boolean list[], int startIndex) {
		int ret = 1;
		boolean startValue = list[startIndex];
		
		for(int i=startIndex+1; i<list.length && list[i] == startValue; i++) {
			ret++;
		}
		
		return ret;
	}

}
