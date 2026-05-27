package getTopTriangleEigenvector;

public class DrawTheTrigMatrix2 {

	//TODO: Reversed I and J because it feels better...
	
	public static void main(String args[]) {
	
		int NUM_BITS = 4;

		if(args.length >= 1) {
			NUM_BITS = Integer.parseInt(args[0]);
		}
		
		if(NUM_BITS % 2 != 0) {
			System.out.println("ERROR: NUM_BITS must be set as an even number");
			System.exit(1);
		}
		
		int NUMBER_THAT_GETS_CHECKED = 2;
		
		for(int i=0; i<Math.pow(2, NUM_BITS); i++) {
			
			boolean bitsI[] = convertToBool(i, NUM_BITS);
			
			String tmp = "";
			
			for(int j=0; j<Math.pow(2, NUM_BITS); j++) {
				boolean bitsJ[] = convertToBool(j, NUM_BITS);
				
				boolean foundProb = false;
				for(int k=0; k<bitsI.length; k+=2) {
					int topLeftValues = 0;
					int topMidValues = 0;
					int topRightValues = 0;
					
					if(bitsI[getIndex(bitsJ, k-2)] ) {
						topLeftValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsI[getIndex(bitsJ, k-1)] ) {
						topMidValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsI[getIndex(bitsJ, k)] ) {
						topRightValues = NUMBER_THAT_GETS_CHECKED;
					}
					
					int bottomLeftValues = 0;
					int bottomMidValues = 0;
					int bottomRightValues = 0;
					
					if(bitsJ[getIndex(bitsI, k-1)] ) {
						bottomLeftValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsJ[getIndex(bitsI, k)] ) {
						bottomMidValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsJ[getIndex(bitsI, k+1)] ) {
						bottomRightValues = NUMBER_THAT_GETS_CHECKED;
					}
					
					int tmpCheckProb = GetTrigTopEigenTubeSymBitFlipTrial.checkProb(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
					
					boolean probSlow = DrawTheTrigMatrix.checkProbSlow(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
					if(probSlow ^ tmpCheckProb!=0) {
						System.out.println("PROBLEM CHECKER IS BROKEN!");
						System.exit(1);
					}
					
					if(tmpCheckProb != 0) {
						foundProb = true;
						break;
					}
					
				}
				if(i== j) {
					if(foundProb) {
						tmp += " ";
					} else {
						tmp += "D";
					}
				} else if(foundProb) {
					tmp += "_";
				} else {
					tmp += "#";
				}
			}
			System.out.println(tmp);
		}
		System.out.println();
		System.out.println();
	}
	
	public static int getIndex(boolean table[], int index) {
		int tmp = index % table.length;
		return (tmp + table.length) % table.length;
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
