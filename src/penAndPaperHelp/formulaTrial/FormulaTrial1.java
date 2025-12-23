package penAndPaperHelp.formulaTrial;

public class FormulaTrial1 {

	
	public static double K = (0.5 + 2.0/Math.sqrt(17));
	public static double P = 1.0/(Math.sqrt(17) -3.0);
	
	//TODO: solve via dynamic programming.
	// My feeling is that the formula is wrong somehow and I'll be nonsense answers.

	
	public static void main(String[] args) {
		//setupPerm();
		
		
		//[1]
		//[1, 2]
		//[1, 2, 3]
		//[1, 2, 3, 4]
		
		getEquation(30, 20);
		
		
	}

	public static double[] getEquation(int numCoef, int n) {

		double firstNCoef[][] = new double[numCoef][numCoef];
		
		for(int j=0; j<firstNCoef[0].length; j++) {
			firstNCoef[0][j] = 1.0;
		}
		
		for(int i=1; i<firstNCoef.length; i++) {
			for(int c=0; c<firstNCoef[0].length; c++) {
				
				firstNCoef[i][c] = 0.0;
				
				if(c > 0) {
					firstNCoef[i][c] += firstNCoef[i][c-1];
				}

				if(c >= 2) {
					//TODO: You could make this faster:
					for(int b=1; c*b<=i; b++) {
						
						if(c == 2) {
							firstNCoef[i][c] += firstNCoef[i-c*b][c-1] * getEstimateCoefficientsForB_2CyclesForY(n, K, P, b);
						} else {
							firstNCoef[i][c] += firstNCoef[i-c*b][c-1] * getEstimateCoefficientsForD_CCyclesForY(n, c, b);
							
						}
					}
				}
				
				System.out.print(firstNCoef[i][c] + ", ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();

		double ret[] = new double[firstNCoef.length];
		
		for(int i=0; i<ret.length; i++) {
			ret[i] = firstNCoef[i][firstNCoef[0].length - 1];
			
			System.out.print(ret[i] + ", ");
		}
		

		for(int i=0; i<ret.length; i++) {
			ret[i] = firstNCoef[i][firstNCoef[0].length - 1];
			
			System.out.print(ret[i] + ", ");
		}

		System.out.println();
		System.out.println();

		for(int i=0; i<ret.length; i++) {
			
			System.out.print(ret[i] + "*y^" + (numCoef - i - 1));
			
			if(i<ret.length - 1) {
				System.out.print(" + ");
			}
		}
		System.out.print(" = 0 ");
		System.out.println();
		
		return ret;
	}
	// wolfram: getEquation(10, 20);
	// y = 0.197496
	// x = M*(p^n*k) * y
	// x  = (n/p)^n *k * y = ( 2.0/(sqrt(17) -3.0))^20*(0.5 + 2.0/sqrt(17))* 0.197496
	//  =20008.5
	// g approx x^(1/20)
	// g = 1.640819
	// Dph!
	
	//TODO: solve polynomial with binary search
	//TODO: automate the g calc test
	// TODO: Does the assumption that cycles >=3 are random with no bias even hold?
	// For example P(s1 connects s3) == P( s1 connects s3 given s1 connects to s2 and s2 connects to s3) ???
	// I assumed it did, but that's starting to seem foolish.
	//TODO: Test the assumption by doing random sampling for both situations and comparing.
	
	
	
	public static void display() {
		
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
	
	
	public static double permB[] = setupPerm();
	
	public static double[] setupPerm() {
		double ret[] = new double[100];
		ret[0] = 1;
		for(int i=1; i<ret.length; i++) {
			ret[i] = i * ret[i-1];
			System.out.println(i + "! : " +  ret[i]);
		}
		return ret;
	}
}
