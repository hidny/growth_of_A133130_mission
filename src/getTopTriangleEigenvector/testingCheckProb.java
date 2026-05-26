package getTopTriangleEigenvector;

public class testingCheckProb {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//32 (good)
		//basicTest(2);

		//32 good
		//basicTest(10);

		//64 (good)
		//basicTest(1);
		
		//32 (good)
		basicTest(12);
	}
	
	public static void basicTest(int numToUse) {
		int SIDES_HEX = 6;
		
		int numSolutions = 0;
		
		System.out.println("Solutions that don't cause intersection when using number: " + numToUse);
		for(int i=0; i<64; i++) {
			
			boolean array[] = GetTrigTopEigenTubeSymBitFlipTrial.convertToBoolSlow(i, SIDES_HEX);
			
			int topLeftValues = 0;
			int topMidValues = 0;
			int topRightValues = 0;
			int bottomRightValues = 0;
			int bottomMidValues = 0;
			int bottomLeftValues = 0;
			
			if(array[0]) {
				topLeftValues = numToUse;
			}

			if(array[1]) {
				topMidValues = numToUse;
			}
			if(array[2]) {
				topRightValues = numToUse;
			}
			if(array[3]) {
				bottomRightValues = numToUse;
			}
			if(array[4]) {
				bottomMidValues = numToUse;
			}
			if(array[5]) {
				bottomLeftValues = numToUse;
			}
			
			int result = GetTrigTopEigenTubeSymBitFlipTrial.checkProb(topLeftValues, topMidValues, topRightValues, bottomRightValues, bottomMidValues, bottomLeftValues);
			
			if(result == 0) {
				printArray(array);
				numSolutions++;
			}
		}
		
		System.out.println("Number of solutions for number " + numToUse + ":");
		System.out.println(numSolutions);
	}
	
	public static void printArray(boolean array[]) {
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
}
