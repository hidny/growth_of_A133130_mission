package getTopEigenvalue;

import determinantStudy.Permutation;
import number.IsNumber;

// TODO:
// How to improve on this:
// Next level: https://math.stackexchange.com/questions/2269267/determinant-of-large-matrices-it-must-exist-a-faster-way
public class BruteForceMaxEigenvalue {

	public static void main(String[] args) {
		
		//TODO: n=6 means not using longs for the limits...
		// Also, it's out of reach.
		
		
		for(int n=1; n<=3; n++) {

			System.out.println();
			System.out.println();
			System.out.println("n = " + n);
			boolean matrix[][] = createMatrix(n);
			
			int result[] = new int[((int)Math.pow(2, n)) + 1];
			
			String permIndexes[] = new String[matrix.length];
			for(int j=0; j<matrix.length; j++) {
				
				permIndexes[j] = "" + j;
			}
			
			
			long max = Permutation.getSmallFactorial(permIndexes.length);
			
			long itDebug = 0;
			
			for(long j=0; j<max; ) {
				String permToUse[] = Permutation.generatePermutation(permIndexes, j);
				
				if(itDebug % 1000000 ==0 ) {
					for(int k=0; k<permToUse.length; k++) {
						System.out.print(permToUse[k] + "  ");
					}
					System.out.println();
				}
				
				boolean stillGood = true;
				int numIterationFactorial = permToUse.length - 1;
				int numMainDiag = 0;
				
				for(int k=0; k<permToUse.length; k++) {
					
					int colNumberToUse = pint(permToUse[k]);
					
					if(matrix[k][colNumberToUse]) {
						numIterationFactorial--;
						
						if(k == colNumberToUse) {
							numMainDiag++;
						}
						
					} else {
						stillGood = false;
						break;
					}
				}
				
				if(stillGood) {
					
					//TODO: we have to figure out the sign!
					
					boolean shouldBePositive = true;
					//TODO: don't initialize every time...
					boolean used[] = new boolean[permToUse.length];
					
					
					for(int k=0; k<permToUse.length; k++) {
						
						int colNumberToUse = pint(permToUse[k]);

						used[colNumberToUse] = true;
						
						for(int m=0; m<colNumberToUse; m++) {
							if(used[m] == false) {
								shouldBePositive = !shouldBePositive;
							}
						}
					}
					
					if(shouldBePositive) {
						result[result.length - 1 - numMainDiag]++;
					} else {
						result[result.length - 1 - numMainDiag]--;
					}
				}
				
				//Both should get same answer, but the uncommented one should be faster:
				//j++;
				j += Permutation.getSmallFactorial(numIterationFactorial);
				
				itDebug++;
			}
			
			
			System.out.println("Result:");
			
			for(int k=0; k<result.length; k++) {
				
				if(k < result.length - 1 && result[k] != 0) {
					System.out.print("(" + result[k] + ")" + "(1-y)^" + (result.length-1-k) + " + ");
				
				} else if(k == result.length - 1){
					System.out.print("(" + result[k] + ")");
				}
			}
			System.out.println("= 0");
			
		}
		
	}
	

	public static int pint(String s) {
		if (IsNumber.isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			System.out.println("Error: (" + s + ") is not a number");
			return -1;
		}
	}
	

	
	//TODO: what if we order it by the # of times the numbers switch from 0 to 1? Will the picture look clearer?
		//Right now, it has a fractal shape.
		public static boolean[][] createMatrix(int numBits) {
			
			boolean ret[][] = new boolean[(int)Math.pow(2, numBits)][(int)Math.pow(2, numBits)];
			
			for(int i=0; i<Math.pow(2, numBits); i++) {
				
				boolean bitsI[] = convertToBool(i, numBits);
				
				for(int j=0; j<Math.pow(2, numBits); j++) {
					boolean bitsJ[] = convertToBool(j, numBits);
					
					boolean foundProb = false;
					for(int k=0; k<bitsI.length - 1; k++) {
						if(bitsI[k] != bitsI[k+1] && bitsI[k] == bitsJ[k+1] && bitsI[k+1] == bitsJ[k]) {
							foundProb = true;
							break;
						}
					}
					if(foundProb) {
						System.out.print("_");
						ret[i][j] = false;
					} else {
						System.out.print("#");
						ret[i][j] = true;
					}
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
			
			return ret;
		}
		
		public static boolean[] convertToBool(int num, int numBinaryDigits) {
			
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
