package penAndPaperHelp;

public class failed_trying_to_guess_constant {

	public static final double constant = 1.80033132449;

	//Best guess:
	//log(550328) / log(1544) = 1.8003313244882626
	
	//Nope:
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		long num = 2;
		long denom = 2;
		
		double bestDiff = Double.MAX_VALUE;
		long bestNum = -1;
		long bestDenom = -1;
		
		for(long i=0; i<100000000L; i++) {
			
			double cur = /*Math.PI*/Math.log(num/*Math.PI*/)/Math.log(denom);
			
			double diff = cur - constant;
			
			//System.out.println(cur);

			if(Math.abs(diff) < Math.abs(bestDiff)) {
				bestDiff = diff;
				bestNum = num;
				bestDenom = denom;
				System.out.println("log("+num+") / log("+denom+") = " + cur);
			}
			
			if(diff > 0) {
				denom++;
			} else {
				num++;
			}
			
		}
		
		System.out.println("Final:");
		System.out.println("log("+bestNum+") / log("+bestDenom+") - constant = " + bestDiff);
	}

}
