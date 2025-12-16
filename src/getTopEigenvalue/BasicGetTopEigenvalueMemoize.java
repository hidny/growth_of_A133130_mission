package getTopEigenvalue;

public class BasicGetTopEigenvalueMemoize {

	
	public static void main(String[] args) {
		
		initializePow2();
		
		int NUM_IT = 10;
		int PERIOD_DEBUG_PER_LOOP = 1;
		
		
		
		for(int n=1; n<19; n++) {
			
			System.out.println("Memorizing the answers to convert Bool:");
			setupCovertToBoolAnswers(n);
			System.out.println("Done Memorizing the answers to convert Bool:");
			
			
			System.out.println();
			System.out.println("N = " + n + ":");
			
			double vector[] = new double[(int)Math.pow(2, n)];
			for(int i=0; i<vector.length; i++) {
				vector[i] = 1.0;
			}
			
			
			
			//double prevEignevalue = 0.0;
			double curEigenvalue = 0.0;
			for(int i=0; i<NUM_IT; i++) {
				vector = multCurrentVection(vector, n);
				curEigenvalue = vector[0];
				vector = normalizeVector(vector);
				
				if(i % PERIOD_DEBUG_PER_LOOP == 0) {
					
					System.out.println("Current eigenvalue: " + curEigenvalue);
				}
			}
			
			System.out.println("Final eigenvalue: " + curEigenvalue);
			
			System.out.println("Estimated growth rate: " + Math.pow(curEigenvalue, 1.0/(1.0 * n)));
			
		}
	}


	public static void initializePow2() {
		pow2 = new int[30];
		
		pow2[0] = 1;
		
		for(int i=1; i<pow2.length; i++ ) {
			pow2[i] = 2 * pow2[i-1];
		}
	}
	public static int pow2[] = null;
	
	
	
	
	public static double[] multCurrentVection(double vector[], int n) {
		
		double newVector[] = new double[vector.length];
		
		for(int i=0; i<vector.length; i++) {
			
			boolean bitsI[] = covertToBoolAnswers[i];
			
			for(int j=0; j<vector.length; ) {
				
				boolean bitsJ[] = covertToBoolAnswers[j];
				
				int badIndex = 0;
				boolean foundBad2x2Subspace = false;
				for(int k=0; k<bitsI.length - 1; k++) {
					if(bitsI[k] != bitsI[k+1] && bitsI[k] == bitsJ[k+1] && bitsI[k+1] == bitsJ[k]) {
						foundBad2x2Subspace = true;
						badIndex = bitsI.length - 2 - k;
						break;
					}
				}
				
				if( ! foundBad2x2Subspace) {
					newVector[i] += vector[j];
				}

				j += pow2[badIndex];
				
				
			}
		}
		
		
		return newVector;
	}
	
	
	public static double[] normalizeVector(double vector[]) {
		
		double newVector[] = new double[vector.length];
		
		for(int i=0; i<vector.length; i++) {
			newVector[i] = vector[i] / vector[0];
		}
		
		return newVector;
	}

	public static boolean[][] covertToBoolAnswers;
	
	public static void setupCovertToBoolAnswers(int numBinaryDigits) {
		covertToBoolAnswers = new boolean[(int)Math.pow(2, numBinaryDigits)][];
		
		for(int i=0; i<covertToBoolAnswers.length; i++) {
			covertToBoolAnswers[i] = convertToBoolSlow(i, numBinaryDigits);
		}
		
	}

	public static boolean[] convertToBoolSlow(int num, int numBinaryDigits) {
		
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

/*
 * 
N = 1:
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Current eigenvalue: 2.0
Final eigenvalue: 2.0
Estimated growth rate: 2.0

N = 2:
Current eigenvalue: 4.0
Current eigenvalue: 3.561797752808989
Current eigenvalue: 3.561552964178479
Current eigenvalue: 3.5615528129023803
Current eigenvalue: 3.561552812808888
Current eigenvalue: 3.5615528128088303
Current eigenvalue: 3.5615528128088303
Current eigenvalue: 3.5615528128088303
Current eigenvalue: 3.5615528128088303
Current eigenvalue: 3.5615528128088303
Final eigenvalue: 3.5615528128088303
Estimated growth rate: 1.8872076761206835

N = 3:
Current eigenvalue: 8.0
Current eigenvalue: 6.41916747337851
Current eigenvalue: 6.418832759199776
Current eigenvalue: 6.4188326759906955
Current eigenvalue: 6.418832675970047
Current eigenvalue: 6.418832675970042
Current eigenvalue: 6.418832675970042
Current eigenvalue: 6.418832675970042
Current eigenvalue: 6.418832675970042
Current eigenvalue: 6.418832675970042
Final eigenvalue: 6.418832675970042
Estimated growth rate: 1.8584548654699737

N = 4:
Current eigenvalue: 16.0
Current eigenvalue: 11.555453097493505
Current eigenvalue: 11.554619059296712
Current eigenvalue: 11.554618773794221
Current eigenvalue: 11.554618773663975
Current eigenvalue: 11.554618773663908
Current eigenvalue: 11.554618773663908
Current eigenvalue: 11.554618773663908
Current eigenvalue: 11.554618773663908
Current eigenvalue: 11.554618773663908
Final eigenvalue: 11.554618773663911
Estimated growth rate: 1.843694267244297

N = 5:
Current eigenvalue: 32.0
Current eigenvalue: 20.80408429758061
Current eigenvalue: 20.802382227072105
Current eigenvalue: 20.802381759689716
Current eigenvalue: 20.80238175952346
Current eigenvalue: 20.802381759523396
Current eigenvalue: 20.802381759523403
Current eigenvalue: 20.802381759523396
Current eigenvalue: 20.802381759523403
Current eigenvalue: 20.802381759523396
Final eigenvalue: 20.802381759523396
Estimated growth rate: 1.8349431453466731

N = 6:
Current eigenvalue: 64.0
Current eigenvalue: 37.45464048856965
Current eigenvalue: 37.451128752886156
Current eigenvalue: 37.45112783512576
Current eigenvalue: 37.451127834791365
Current eigenvalue: 37.45112783479121
Current eigenvalue: 37.45112783479121
Current eigenvalue: 37.45112783479121
Current eigenvalue: 37.45112783479121
Current eigenvalue: 37.45112783479121
Final eigenvalue: 37.45112783479121
Estimated growth rate: 1.8291282200785635

N = 7:
Current eigenvalue: 128.0
Current eigenvalue: 67.43154124099634
Current eigenvalue: 67.42444832727642
Current eigenvalue: 67.42444661626912
Current eigenvalue: 67.42444661569374
Current eigenvalue: 67.42444661569348
Current eigenvalue: 67.4244466156935
Current eigenvalue: 67.4244466156935
Current eigenvalue: 67.4244466156935
Current eigenvalue: 67.4244466156935
Final eigenvalue: 67.4244466156935
Estimated growth rate: 1.8249863783409301

N = 8:
Current eigenvalue: 256.0
Current eigenvalue: 121.40051455081371
Current eigenvalue: 121.38634451486388
Current eigenvalue: 121.3863412929786
Current eigenvalue: 121.38634129194136
Current eigenvalue: 121.38634129194101
Current eigenvalue: 121.38634129194098
Current eigenvalue: 121.38634129194098
Current eigenvalue: 121.386341291941
Current eigenvalue: 121.38634129194101
Final eigenvalue: 121.386341291941
Estimated growth rate: 1.821886122100499

N = 9:
Current eigenvalue: 512.0
Current eigenvalue: 218.56366646872115
Current eigenvalue: 218.53563887723024
Current eigenvalue: 218.53563283708382
Current eigenvalue: 218.53563283523846
Current eigenvalue: 218.53563283523775
Current eigenvalue: 218.5356328352377
Current eigenvalue: 218.53563283523772
Current eigenvalue: 218.53563283523772
Current eigenvalue: 218.53563283523772
Final eigenvalue: 218.53563283523775
Estimated growth rate: 1.819478456832375

N = 10:
Current eigenvalue: 1024.0
Current eigenvalue: 393.4915482239425
Current eigenvalue: 393.4365565414748
Current eigenvalue: 393.4365452294762
Current eigenvalue: 393.43654522618203
Current eigenvalue: 393.43654522618067
Current eigenvalue: 393.43654522618067
Current eigenvalue: 393.43654522618067
Current eigenvalue: 393.43654522618067
Current eigenvalue: 393.43654522618067
Final eigenvalue: 393.43654522618067
Estimated growth rate: 1.8175546154654834

N = 11:
Current eigenvalue: 2048.0
Current eigenvalue: 708.4233209950635
Current eigenvalue: 708.3161577302218
Current eigenvalue: 708.3161365792257
Current eigenvalue: 708.3161365733479
Current eigenvalue: 708.3161365733455
Current eigenvalue: 708.316136573346
Current eigenvalue: 708.3161365733457
Current eigenvalue: 708.3161365733461
Current eigenvalue: 708.3161365733456
Final eigenvalue: 708.3161365733454
Estimated growth rate: 1.8159820766054082

N = 12:
Current eigenvalue: 4096.0
Current eigenvalue: 1275.411388258734
Current eigenvalue: 1275.2037678129802
Current eigenvalue: 1275.2037283188286
Current eigenvalue: 1275.2037283083453
Current eigenvalue: 1275.2037283083448
Current eigenvalue: 1275.2037283083444
Current eigenvalue: 1275.2037283083444
Current eigenvalue: 1275.2037283083444
Current eigenvalue: 1275.2037283083444
Final eigenvalue: 1275.2037283083441
Estimated growth rate: 1.8146726669147375

N = 13:
Current eigenvalue: 8192.0
Current eigenvalue: 2296.189525472479
Current eigenvalue: 2295.789290844725
Current eigenvalue: 2295.7892171943954
Current eigenvalue: 2295.7892171757235
Current eigenvalue: 2295.789217175704
Current eigenvalue: 2295.789217175705
Current eigenvalue: 2295.789217175705
Current eigenvalue: 2295.789217175705
Current eigenvalue: 2295.789217175705
Final eigenvalue: 2295.789217175705
Estimated growth rate: 1.8135654423366352

N = 14:
Current eigenvalue: 16384.0
Current eigenvalue: 4133.9495518444655
Current eigenvalue: 4133.1813793154715
Current eigenvalue: 4133.181242133625
Current eigenvalue: 4133.181242100236
Current eigenvalue: 4133.181242100231
Current eigenvalue: 4133.1812421002305
Current eigenvalue: 4133.1812421002305
Current eigenvalue: 4133.1812421002305
Current eigenvalue: 4133.1812421002305
Final eigenvalue: 4133.1812421002305
Estimated growth rate: 1.8126169304159052

N = 15:
Current eigenvalue: 32768.0
Current eigenvalue: 7442.564608715633
Current eigenvalue: 7441.095915224615
Current eigenvalue: 7441.095659993796
Current eigenvalue: 7441.095659934349
Current eigenvalue: 7441.095659934319
Current eigenvalue: 7441.095659934319
Current eigenvalue: 7441.095659934319
Current eigenvalue: 7441.095659934319
Current eigenvalue: 7441.095659934319
Final eigenvalue: 7441.095659934319
Estimated growth rate: 1.8117952880347965

N = 16:
Current eigenvalue: 65536.0
Current eigenvalue: 13399.236555800777
Current eigenvalue: 13396.438079555517
Current eigenvalue: 13396.437605188554
Current eigenvalue: 13396.437605082583
Current eigenvalue: 13396.437605082625
Estimated growth rate: 1.81107665647661747


N = 17:
Current eigenvalue: 131072.0
Current eigenvalue: 20148.741653442383
Current eigenvalue: 24565.25587610428
Current eigenvalue: 24066.09358249103
Current eigenvalue: 24123.343191143886
Current eigenvalue: 24117.36839220228
Current eigenvalue: 24118.09267983785
Current eigenvalue: 24118.01715202838
Current eigenvalue: 24118.027137937424
Current eigenvalue: 24118.026115302382
Final eigenvalue: 24118.026115302382
Estimated growth rate: 1.8104428058959234

*/
/*
 * 
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:
N = 18:
Current eigenvalue: 262144.0
Current eigenvalue: 35880.40375518799
Current eigenvalue: 44272.8987603416
Current eigenvalue: 43321.667778946874
Current eigenvalue: 43430.510708147995
Current eigenvalue: 43419.19387579724
Current eigenvalue: 43420.56291435467
Current eigenvalue: 43420.42105273426
Current eigenvalue: 43420.43978943387
Current eigenvalue: 43420.437890940004
Final eigenvalue: 43420.437890940004
Estimated growth rate: 1.8098795699639625
*/
