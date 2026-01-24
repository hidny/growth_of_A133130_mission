package penAndPaperHelp;

public class ProductMMinusI_i_0_to_n {

	// Index term 1:
	// n choose 2 (MT: n ^2)
	
	//Index term 2:
	//A000914 "Stirling numbers of the first kind: s(n+2, n)."
	//a(n) = binomial(n+2, 3)*(3*n+5)/4 = (n+1)*n*(n+2)*(3*n+5)/24  (MT: n ^4)
	
	//Index term 3:
	//https://oeis.org/A001303 Stirling numbers of first kind, s(n+3, n), negated.
	// a(n) = binomial(n+3, 4)*binomial(n+3, 2)  (MT: n ^6)
	
	//Index term 4:
	//https://oeis.org/A000915
	//Stirling numbers of first kind s(n+4, n).
	//a(n) = binomial(n+4, 5)*(15*n^3 + 150*n^2 + 485*n + 502)/48 (MT: n ^ 11)
	
	//INDEX_TERM = 5;
	//https://oeis.org/A053567
	//	a(n) = (-1)^n*binomial(n+5, 6)*binomial(n+5, 2)*(3*n^2 + 23*n + 38)/8
	
	//INDEX_TERM = 6;
	///https://oeis.org/A112002
	
	//Index term 7:
	//https://oeis.org/A191685
	
	//Index term 8 does not exist!
	// Finally!
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int INDEX_TERM = 8;
		
		for(int i=0; i<20; i++) {
			long coefficient = getTerm(INDEX_TERM, i);
			System.out.println("n = " + i + ": " + coefficient);
		}
	}

	
	// Get expanded terms of M(M-1)(M-2)(M-3)...(M-n)
	public static long getTerm(int indexTerm, int n) {
		
		if(indexTerm > n) {
			return 0;
		}
		
		boolean combo[] = new boolean[n + 1];
		for(int i=0; i<indexTerm; i++) {
			combo[i] = true;
		}
		
		long ret = 0;
		
		while(combo != null) {
			
			long nextTerm = 1L;
			
			for(int j=0; j<combo.length; j++) {
				if(combo[j]) {
					nextTerm *= (-j);
				}
			}
			
			ret += nextTerm;
			combo = Combination.getNextCombination(combo);
		}
		
		
		return ret;
		
	}
}
