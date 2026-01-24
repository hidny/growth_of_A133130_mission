package getTopEigenvalue.tubeVersion;

public class findingGEstimate {

	
	public static void main(String args[]) {
		double numbers[] = new double[]
			{
			2.0,
			1.8872076761206835,
			1.8171205928321397,
			1.805645215864932,
			1.8019270280992905,
			1.800858717951592,
			1.8005098318996733,
			1.800393761516551,
			1.8003536141201562,
			1.800339425996185,
			1.8003343108385468,
			1.8003324384564048,
			1.800331744175025,
			1.8003314839581552,
			1.8003313855298055,
			1.800331348003789,
			1.8003313335982305,
			1.8003313280347228,
			1.8003313258745517,
			1.8003313250318118,
			1.8003313247016295};
		
		double diffs[] = new double[numbers.length - 1];
		for(int i=0; i<numbers.length; i++) {
			if(i > 0) {
				diffs[i - 1] = numbers[i] - numbers[i - 1];
				System.out.println("Diff: " + diffs[i - 1]);
				
			}
		}
		System.out.println();
		for(int i=0; i<diffs.length; i++) {
			if(i > 0) {
				double ratio = diffs[i] / diffs[i-1];
				System.out.println("Ratio: " + ratio);
				
				if(i > 1) {
					System.out.println("Diff ratio: " + ((diffs[i] / diffs[i-1]) - (diffs[i-1] / diffs[i-2])));
				}
			}
		}
		
		//Extrapolate ration 0.4:
		//1.8003313244815078
		//Extrapolate ration 0.393:
		//1.8003313244878543
		//Extrapolate ration 0.392:
		//1.8003313244887489
		double RATIO = 0.392;
		
		double firstDiff = diffs[diffs.length - 1];
		double firstEstimate = numbers[numbers.length - 1];
		
		double curDiff = firstDiff;
		double prevEstimate = firstEstimate;
		for(int i=0; i<100; i++) {

			curDiff *= RATIO;
			double nextEstimate = prevEstimate + curDiff;
			
			System.out.println("Diff: " + (nextEstimate - prevEstimate));
			prevEstimate = nextEstimate;
			
			System.out.println(nextEstimate);
		}
	}
}
