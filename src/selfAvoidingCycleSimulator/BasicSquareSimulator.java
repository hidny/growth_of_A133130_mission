package selfAvoidingCycleSimulator;

import java.math.BigInteger;
import java.util.Random;

public class BasicSquareSimulator {

	// No 42 equals close enough to random.
	public static Random random = new Random(42);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		boolean start[] = getStart(200);
		boolean reformat[] = reformatArrayForNo2x2Same(start);
		
		basicPrint(start);
		basicPrint(reformat);
		
		for(int i=0; i<fib.length; i++) {
			System.out.println(fibBig[i]);
		}
		
		BigInteger numSolutions = getNumSolutions(start);
		
		System.out.println("Num Solutions: " + numSolutions);
		
		//TODO: take a random num from 0 to numSolutions - 1, then get the random continuation.
		
		//TODO: make sure the numbers from  the function in this class is alligned with what's found in the
		// matrices in other files.
	}

	
	public static boolean[] getStart(int n) {
		boolean array[] = new boolean[n];
		
		
		int SIXTY_FOUR_BITS = 64;
		
		for(int i=0; i<n;) {
			
			long randomNum = random.nextLong();
			for(int j=0; j + i < n && j<SIXTY_FOUR_BITS; j++) {
				array[i + j] = randomNum % 2 == 1;
				randomNum = randomNum >>> 1;
			}
			i+= SIXTY_FOUR_BITS;
		}
		
		return array;
	}
	
	public static void basicPrint(boolean array[]) {
		String tmp = "";
		for(int i=0; i<array.length; i++) {
			if(array[i]) {
				tmp += "#";
			} else {
				tmp += "_";
			}
		}
		System.out.println(tmp);
	}
	
	public static boolean[] reformatArrayForNo2x2Same(boolean array[]) {
		boolean ret[] = new boolean[array.length];
		
		
		for(int i=0; i<array.length; i++) {
			ret[i] = array[i] ^ (i % 2 == 0);
		}
		
		return ret;
	}

	public static BigInteger fibBig[] = new BigInteger[100];
	public static long fib[] = new long[100];
	
	static {
		fib[0] = 1;
		fib[1] = 1;
		
		for(int i=2; i<fib.length; i++) {
			fib[i] = fib[i-1] + fib[i-2];
		}
		
		for(int i=0; i<fibBig.length; i++) {
			fibBig[i] = new BigInteger("" + fib[i]);
		}
	}
	
	
	public static BigInteger getNumSolutions(boolean array[]) {
		
		boolean reformat[] = reformatArrayForNo2x2Same(array);
		
		BigInteger ret = BigInteger.ONE;
		
		for(int i=0; i<reformat.length; ) {
			
			boolean start = reformat[i];
			int curLength = 0;
			
			do {
				curLength++;
				i++;
			} while(i < array.length && start == reformat[i]);
			
			ret = ret.multiply(fibBig[1 + curLength]);
			
			System.out.println("curLength: " + curLength);
		}
		
		//##___#_######_####_#_#_#___##_#______##____#__##__#_#___####_####_#####_#_#_#__#____#___#__##_#___#_##_#_###_###_#__##_###_#####_####____###_###_######___#__#_#_##_##_##__#_##_#_###_#__#_##______###__

		
		return ret;
	}
}
