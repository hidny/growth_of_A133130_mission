package getTopTriangleEigenvector;

public class DrawTheTrigMatrix {

	public static final int NUMBER_THAT_GETS_CHECKED = 2;
	
	public static void main(String args[]) {
	
		
		int NUM_BITS = 8;
		
		if(args.length >= 1) {
			NUM_BITS = Integer.parseInt(args[0]);
		}
		
		if(NUM_BITS % 2 != 0) {
			System.out.println("ERROR: NUM_BITS must be set as an even number");
			System.exit(1);
		}
		
		
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
					
					if(bitsJ[getIndex(bitsI, k-2)] ) {
						topLeftValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsJ[getIndex(bitsI, k-1)] ) {
						topMidValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsJ[getIndex(bitsI, k)] ) {
						topRightValues = NUMBER_THAT_GETS_CHECKED;
					}
					
					int bottomLeftValues = 0;
					int bottomMidValues = 0;
					int bottomRightValues = 0;
					
					if(bitsI[getIndex(bitsI, k-1)] ) {
						bottomLeftValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsI[getIndex(bitsI, k)] ) {
						bottomMidValues = NUMBER_THAT_GETS_CHECKED;
					}
					if(bitsI[getIndex(bitsI, k+1)] ) {
						bottomRightValues = NUMBER_THAT_GETS_CHECKED;
					}
					
					int tmpCheckProb = GetTrigTopEigenTubeSymBitFlipTrial.checkProb(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
					
					boolean probSlow = checkProbSlow(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
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
		
		System.out.println("Vs");
		
		if(args.length == 0) {
			DrawTheTrigMatrix2.main(new String[] {(NUM_BITS + "")});
		}
	}
	
	public static boolean checkProbSlow(int topLeftValues, int topMidValues, int topRightValues, int bottomRightValues, int bottomMidValues, int bottomLeftValues) {
		
		boolean array[] = new boolean[6];
		array[0] = topLeftValues == NUMBER_THAT_GETS_CHECKED;
		array[1] = topMidValues == NUMBER_THAT_GETS_CHECKED;
		array[2] = topRightValues == NUMBER_THAT_GETS_CHECKED;
		array[3] = bottomRightValues == NUMBER_THAT_GETS_CHECKED;
		array[4] = bottomMidValues == NUMBER_THAT_GETS_CHECKED;
		array[5] = bottomLeftValues == NUMBER_THAT_GETS_CHECKED;
		
		int numSwitches = 0;
		
		for(int i=1; i<6; i++) {
			if(array[i] != array[i-1]) {
				numSwitches++;
			}
		}
		
		return numSwitches > 2;
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
