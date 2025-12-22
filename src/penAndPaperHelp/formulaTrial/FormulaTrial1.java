package penAndPaperHelp.formulaTrial;

public class FormulaTrial1 {

	
	public static double K = (0.5 + 2.0/Math.sqrt(17));
	public static double P = 1.0/(Math.sqrt(17) -3.0);
	
	//TODO: solve via dynamic programming.
	// My feeling is that the formula is wrong somehow and I'll be nonsense answers.
	
	
	public static void main(String[] args) {
		setupPerm();
		
		double sum = 0.0;
		for(int b=0; b<100; b++) {
			double coef = getEstimateCoefficientsForB_2CyclesForY(10, K, P, b);
			
			sum += coef;
			System.out.println("Coef when there's " + b + " 2-cycles: " + coef);
		}
		System.out.println("Sum: " + sum);

		System.out.println();
		System.out.println();
		System.out.println();
		sum = 0.0;
		int CYCLE = 3;
		for(int b=0; b<100; b++) {
			double coef = getEstimateCoefficientsForD_CCyclesForY(10, CYCLE, b);

			sum += coef;
			System.out.println("Coef when there's " + b + " " + CYCLE + "-cycles: " + coef);
		}
		System.out.println("Sum: " + sum);

		System.out.println();
		System.out.println();
		System.out.println();
		sum = 0.0;
		CYCLE = 10;
		for(int b=0; b<100; b++) {
			double coef = getEstimateCoefficientsForD_CCyclesForY(10, CYCLE, b);

			sum += coef;
			System.out.println("Coef when there's " + b + " " + CYCLE + "-cycles: " + coef);
		}
		System.out.println("Sum: " + sum);
		

		System.out.println();
		System.out.println();
		System.out.println();
		sum = 0.0;
		CYCLE = 20;
		for(int b=0; b<100; b++) {
			double coef = getEstimateCoefficientsForD_CCyclesForY(10, CYCLE, b);

			sum += coef;
			System.out.println("Coef when there's " + b + " " + CYCLE + "-cycles: " + coef);
		}
		System.out.println("Sum: " + sum);
		
		
		System.out.println();
		System.out.println();
		System.out.println();
		sum = 0.0;
		CYCLE = 100;
		for(int b=0; b<100; b++) {
			double coef = getEstimateCoefficientsForD_CCyclesForY(50, CYCLE, b);

			sum += coef;
			System.out.println("Coef when there's " + b + " " + CYCLE + "-cycles: " + coef);
		}
		System.out.println("Sum: " + sum);
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		System.out.println("How goes: k^(n-1)");
		
		double cur = 1.0;
		for(int n=1; n<1000; n++) {
			System.out.println("n = " + n + ": " + cur);
			cur *=K;
		}
		System.out.println("VS: " + Math.pow(K, 998));
		System.out.println("The fact that it tends to 0 means that the terms with lower coefficients might matter when they seem not to...");
	}
	
	
	
	public static double[] getEstimateCoefficients(int n, double k, double p) {
		
		
		return null;
	}
	
	// sum (b=1 to inf) (-1)^(b)(M^2*p^n*k/(2x^2))^b*(1/b!)
	//Idea: y = x/M*(p^n*k)
	// denom has p^n*k
	//= sum (b=1 to inf) (-1)^(b)(1/(p^n*k*c*y^c))^b*(1/b!)
	
	public static double getEstimateCoefficientsForB_2CyclesForY(int n, double k, double p, int b) {

		double c = 2.0;
		
		double posNeg = Math.pow(-1, b);
		
		// c = 2.0 is the special case because the Matrix is symmetric
		double absTerm = Math.pow(1.0/(c*Math.pow(p, n)*k), b) / permB[b];
		
		return posNeg * absTerm;
		
	}
	
	//pre: c >= 3
	// sum (b=1 to inf) (-1)^(b*(c-1))(M^c*(p^n*k)^c/(c*x^c))^b*(1/b!)
	//Idea: y = x/M*(p^n*k)
	// normally x approx (2pk)^n = M*(p^n*k^n)
	// Therefore y approx = M*(p^n*k^n)/M*(p^n*k) = k^(n-1) which tends towards 0 as n->inf...
	
	//= sum (b=1 to inf) (-1)^(b*(c-1)) (1/(c*y^c))^b*(1/b!)
	public static double getEstimateCoefficientsForD_CCyclesForY(int n, int c, int b) {


		double posNeg = Math.pow(-1, b*(c-1));
		
		double absTerm = Math.pow(1.0/c, b) / permB[b];
		
		return posNeg * absTerm;
	}
	
	
	public static double permB[] = new double[100];
	
	public static void setupPerm() {
		permB = new double[100];
		permB[0] = 1;
		for(int i=1; i<permB.length; i++) {
			permB[i] = i * permB[i-1];
			System.out.println(i + "! : " +  permB[i]);
		}
		
	}
}
