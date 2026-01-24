package penAndPaperHelp.formulaTrial;

public class equationTest {


	public static double K = (0.5 + 2.0/Math.sqrt(17));
	public static double P = 1.0/(Math.sqrt(17) -3.0);
	
	public static void main(String[] args) {
		
		int N = 30;
		// N=50
		//sum at i = 100: -1.8003658636837363E64
		System.out.println("c = 2: " + getEstimateCoefficientsForB_2CyclesForY(N, K, P, 1));

		for(int c=3; c<100; c++) {

			System.out.println("c = " + c + ": " + getEstimateCoefficientsForD_CCyclesForY(N, c, 1));
		}
		
		System.out.println();

		double sum = 0.0;
		
		for(int i=0; i< 1000; i++) {
			//sum += getEstimateCoefficientsForB_2CyclesForYAccurate(N, K, P, i);
			sum += getEstimateCoefficientsForD_CCyclesForY(N, 100, i);

			System.out.println("sum at i = " + i +": " + sum);
		}
		System.out.println();
		System.out.println("sum: " + sum);
		//Sum of 2 cycles nose dives though...
	}
	
	//Wait... it's harmonic!
	
	// K(1/3 - 1/4 + 1/5 - 1/6 ...)
	// sum of  cycle coef: ln(2) - 1 + 1/2 = 0.19...
	
	

	// sum (b=1 to inf) (-1)^(b)(M^2*p^n*k/(2x^2))^b*(1/b!)
	//Idea: y = x/M*(p^n*k)
	// denom has p^n*k
	//= sum (b=1 to inf) (-1)^(b)(1/(p^n*k*c*y^c))^b*(1/b!)
	
	public static double getEstimateCoefficientsForB_2CyclesForY(int n, double k, double p, int b) {

		double c = 2.0;
		
		double posNeg = Math.pow(-1, b);
		
		// c = 2.0 is the special case because the Matrix is symmetric
		if( b >= permB.length) {
			return 0.0;
		} else {
			double absTerm = Math.pow(1.0/(c*Math.pow(p, n)*k), b) / permB[b];
		
			return posNeg * absTerm;
		}
		
	}
	

	public static double getEstimateCoefficientsForB_2CyclesForYAccurate(int n, double k, double p, int b) {

		double c = 2.0;
		
		double posNeg = Math.pow(-1, b);
		
		// c = 2.0 is the special case because the Matrix is symmetric
		if( b >= permB.length) {
			return 0.0;
		} else {
			double temp = (Math.pow(2, n) - 2.0*(b-2))/(Math.pow(2, n));
			double temp2 = (Math.pow(2, n) - 2.0*(b-1))/(Math.pow(2, n));
			double numerator = temp *temp2;
			
			//double numerator = 1.0;
			
			//TODO: use perm and pow and try to keep it all balanced, so you could reach 300!
			double absTerm = Math.pow(numerator/(c*Math.pow(p, n)*k), b) / permB[b];
			
			//TODO: wait a minute! this is wrong! It's supposed to be to the power of b...
			return posNeg * absTerm;
		}
		
	}
	
	//pre: c >= 3
	// sum (b=1 to inf) (-1)^(b*(c-1))(M^c*(p^n*k)^c/(c*x^c))^b*(1/b!)
	//Idea: y = x/M*(p^n*k)
	// normally x approx > (2p)^n = M*(p^n)
	// Therefore y approx = M*(p^n)/M*(p^n*k) = 1/k which is close to 1 as n->inf...
	
	//= sum (b=1 to inf) (-1)^(b*(c-1)) (1/(c*y^c))^b*(1/b!)
	public static double getEstimateCoefficientsForD_CCyclesForY(int n, int c, int b) {


		double posNeg = Math.pow(-1, b*(c-1));
		
		double absTerm = Math.pow(1.0/c, b) / permB[b];
		
		return posNeg * absTerm;
	}
	
	

	
	public static double permB[] = setupPerm();
	
	public static double[] setupPerm() {
		double ret[] = new double[200];
		ret[0] = 1;
		for(int i=1; i<ret.length; i++) {
			ret[i] = i * ret[i-1];
			//System.out.println(i + "! : " +  ret[i]);
		}
		return ret;
	}
}
