package misselaneous.growthRateMultLoops;

public class DeriveGrowthRate1 {

	
	//pen and paper:
	
	// let k = (1/2 + 2/sqrt(17))
	// let p = 1/(Math.sqrt(17) -3.0)
	// let N be the length of the n x n square
	// let M = 2^N ( or the length of the adjency matrix)
	
	// Approx number of 1s in a 2^N by 2^N matrix based on A133130: k*p^(N-1)
	// Number of 0/1 colorings of an n X n square for which no 2 by 2 subsquare is monochromatic.
	
	// lambda = top eigenvalue of the matrix.
	// let x = lambda + 1
	// let y = k*p^(N-1)
	
	// goal: Find the growth rate by finding: limit n-> inf, (top eigenvalue of the matrix of matrix)^(1/n)
	
	// By school:
	// Solve for det(M-(lambda)I) = 0
	
	// If we focus on the highest powers of lambda first, we get:
	
	//F(x) = (no displacement) - (2 displacements) - (3 displacements) - ...
	
	
	//  (-x)^(M-2) : picking the diag.
	// M ^ 2 /2 (M choose 2) non diag entries
	// y^2: odds all the non-diag entries are 1.
	
	// Note that if there's an even num non-diag entries, the det is neg
	// and if there's an odd num non-diag entries, the det is pos
	// Also not that when odd num non-diag, (-x)^(M-odd) is neg, that's why all the entries in the sum are negative.
	
	//Note that I might be able to justify the probability trick by arguing that the det get all possible combinations,
	// and it has to average out, but I'll deal with that later.
	
	//F(x) = (-x)^M - (-x)^(M-2) * (My)^2/2 +  (-x)^(M-3) * (My)^3/3 -  (-x)^(M-4) * (My)^4/4 + ...
	
	//F(x) = x^M - x^(M-2) *(My)^2/2 - x^(M-3) *(My)^3/3 - x^(M-4) *(My)^4/4 - ...
	
	//F(x) = x^M - (sum(k = 2 to M) of x^(M-k) *(My)^k/k ) = 0
	
	//  x^M = (sum(k = 2 to M) of x^(M-k) *(My)^k/k )
	// M log(x) = 
	
	//Guess: We only needs the first 100 ish terms of the sum to get something good.
	
	//I did wolfram with k going to infinity and got a simple closed equation I don't 100% trust.
	// I did wolfram with k going to M  and got a complicated equation I think might simplify to the previous equation.
	
	//The plan is to deal with the infinite sum first.
	
	
	// By pen and paper, we want the 
	// F(x) = x

	
	
	
	public static final double k = (1.0/2.0 + 2.0/Math.sqrt(17));
	public static final double p = 1.0/(Math.sqrt(17) -3.0);
	
	

	// Estimate based on wolfram alpha formalu
	public static double estimateGrowthAlt(int n) {

		double m = Math.pow(2, n);
		boolean firstIt = true;
		
		double bestAttemptAtZero = 0.0;
		double bestEstGrowth = -1;
		boolean isNeg = false;
		
		for(double estGrowth= 1.0001; estGrowth<1.01; estGrowth+=0.00001) {
			

			double xEst = estGrowth *m;
			double y = k*Math.pow(p, n-1);
			double my = m*y;
			
			
			double attemptAtZeroPosNeg = xEst * Math.log(1 - my/xEst) + my - xEst;
			double attemptAtZero = Math.abs(xEst * Math.log(1 - my/xEst) + my - xEst);
			
			if(firstIt || attemptAtZero < bestAttemptAtZero) {
				
				firstIt = false;
				
				bestAttemptAtZero = attemptAtZero;
				bestEstGrowth = estGrowth;
				
				if(attemptAtZeroPosNeg < 0) {
					isNeg = true;
				} else {
					isNeg = false;
				}
				System.out.println("my: " + my);
				System.out.println("xEst: " + xEst);
				System.out.println("What is the neg number: " + Math.log(1 - my/xEst));
			}
			
		}
		if(isNeg) {
			System.out.println("Negative!");
		}
		System.out.println("bestAttemptAtZero: " + bestAttemptAtZero);
		return bestEstGrowth;
		
	}
	public static double estimateGrowth(int n, int numTerms) {
		
		
		//First guess at x:
		double m = Math.pow(2, n);
		
		double best= 0.0;
		double bestDiff = 0.0;
		
		double bestRatio = 100.0;
		//I don't know...
		for(double estGrowth= 1.01; estGrowth<1.81; estGrowth+=0.001) {

			double xEst = estGrowth *m;
			
			double y = k*Math.pow(p, n-1);
			
			
			//TODO
			double diff = 0.0;

			//F(x) = x^M - x^(M-2) *(My)^2/2 - x^(M-3) *(My)^3/3 - x^(M-4) *(My)^4/4 - ...
			
			// x^M
			double allDiag = Math.pow(xEst, numTerms + 1);
			
			//Let's do 10 terms...)
			
			double negativeTerms = 0.0;
			for(int k=numTerms - 1, k2=2; k>=0; k--, k2++) {
				
				double term = Math.pow(xEst, k) * Math.pow(m*y, k2)/k2;
				
				negativeTerms += term;
				
			}
			
			double ratio = allDiag / negativeTerms;
			
			if(ratio < bestRatio) {
				bestRatio = ratio;
			}
			
			diff = allDiag - negativeTerms;
			
			if(best == 0.0 || Math.abs(diff) < Math.abs(bestDiff) ) {
				best = estGrowth;
				bestDiff = Math.abs(diff);
			}
		}
		
		System.out.println("Best Ratio: " + bestRatio);
		
		
		return best;
	}
	
	public static void main(String[] args) {
		
		double cur = 0.0;
		double prev = 0.0;
		for(int n=2; n<20; n++) {
			
			System.out.println("Estimate growth rate with N = " + n);
			prev = cur;
			cur = estimateGrowthAlt(n);
			
			System.out.println(cur);
		}

	}

}
