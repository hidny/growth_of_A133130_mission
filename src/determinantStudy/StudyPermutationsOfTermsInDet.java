package determinantStudy;

import java.util.ArrayList;

import number.IsNumber;

public class StudyPermutationsOfTermsInDet {


	public static int N = 6;
	
	public static void main(String[] args) {

		//TODO automate n! better
		long numPerm = Permutation.getSmallFactorial(N);
		
		String list[] = new String[N];
		
		for(int i=0; i<N; i++) {
			list[i] = "" + (i);
		}
		
		ArrayList <DeterminantDetails> detDetails = new ArrayList <DeterminantDetails>();
		
		for(int i=0; i<numPerm; i++) {
			
			
			System.out.println();
			String tmp[] = Permutation.generatePermutation(list, i);
			for(int j=0; j<tmp.length; j++) {
				System.out.print(tmp[j] + " ");
			}
			System.out.println();
			

			//Details I want to collect:
			// Is det pos
			// num main diag (1 cycles)
			// num 2 cycles
			// num 3 cycles
			// num 4 cycles
			// num 5 cycles
			// num 6 cycles
			// num 7 cycles
			//TOOD: record a profile object and then test theories about how commen it is.
			
			int cycles[] = new int[N + 1];
			boolean used[] = new boolean[N];
			for(int j=0; j<cycles.length; j++) {
				cycles[j] = 0;
			}
			for(int j=0; j<used.length; j++ ) {

				used[j] = false;
			}
			
			
			boolean detSignPos = true;
			
			boolean signNextPos = true; 
			
			
			for(int j=0; j<N; j++) {
				for(int k=0; k<N; k++) {
					
					if(pint(tmp[j]) == k) {
						
						used[k] = true;
						if(signNextPos) {
							//pass
						} else {
							detSignPos = ! detSignPos;
						}
						
						signNextPos = true;
						
						break;
					} else if(used[k] == false) {
						signNextPos = !signNextPos;
					}
				}
			}
			
			if(detSignPos) {
				System.out.println("Det +");
			} else {
				System.out.println("Det -");
			}
			
			//used = new boolean[N];
			//for(int j=0; j<cycles.length; j++) {
			//	used[j] = false;
			//}
			
			for(int j=0; j<N; j++) {
				
				boolean foundNewCycle = true;
				int cycleLength = 1;
				
				int oldk = -1;
				int k = j;
				while(pint(tmp[k]) != j) {
					
					oldk = k;
					k = pint(tmp[k]);
					cycleLength++;
					
					if(k < j) {
						foundNewCycle =false;
						break;
					}
					
				}
				if(foundNewCycle) {
					cycles[cycleLength]++;
				}
				
			
			}
			
			int sumCycles = 0;
			for(int j=0; j<cycles.length; j++) {
				
				if(cycles[j] > 0) {
					System.out.println("Num cycles length " + j + ": " + cycles[j]);
				}
				sumCycles += j * cycles[j];
			}
			
			if(sumCycles != N) {
				System.out.println("Doh!");
				System.exit(1);
			}
			
			
			detDetails.add(new DeterminantDetails(cycles, detSignPos));
		}
		
		
		// N=6 tests:
		System.out.println();
		System.out.println("N = 6 tests:");
		System.out.println("Basic test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, N, 0, 0, 0, 0, 0}, true));


		System.out.println("Basic 2 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 4, 1, 0, 0, 0, 0}, false));

		System.out.println("Double 2 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 2, 2, 0, 0, 0, 0}, true));
		
		System.out.println("Triple 2 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 0, 3, 0, 0, 0, 0}, false));
		

		System.out.println("Basic 3 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 3, 0, 1, 0, 0, 0}, true));
		
		System.out.println("Double 3 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 0, 0, 2, 0, 0, 0}, true));
		
		System.out.println("3 2 1 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 1, 1, 1, 0, 0, 0}, false));
		

		System.out.println("Basic 4 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 2, 0, 0, 1, 0, 0}, false));
		
		System.out.println("Other 4 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 0, 1, 0, 1, 0, 0}, true));
		

		System.out.println("Basic 5 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 1, 0, 0, 0, 1, 0}, true));
		

		System.out.println("Basic 6 test: " + DeterminantDetails.validateAllSum(detDetails, new int[] {0, 0, 0, 0, 0, 0, 1}, false));
		
	}
	
	
	public static int pint(String s) {
		if (IsNumber.isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			System.out.println("Error: (" + s + " is not a number");
			return -1;
		}
	}


}
