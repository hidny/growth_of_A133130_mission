package fibonacciStyle;

public class BiggridFiller {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		setupFibonnaci();
		
		boolean listTest[] = new boolean[10000];
		
		for(int i=0; i<listTest.length; i++) {
			listTest[i] = (i % 2 == 0);
		}
		
		System.out.println("Should be about 2: " + getLogMultiplicity(listTest));
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
	
	public static double getOddsMatchAtStartRun(int lengthOfRun) {
		if(lengthOfRun + 1 < NUM_FIB) {
			return fib[lengthOfRun - 1] / fib[lengthOfRun + 1];
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
