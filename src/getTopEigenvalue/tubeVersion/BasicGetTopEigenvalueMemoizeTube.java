package getTopEigenvalue.tubeVersion;

//Oh wow! This seems to converge much much faster!
//I'm now confident g = 1.8003313 +/- 0.0000005

//I already have a home-grown theorem called the expansion tolerance theorem that could prove that gTube = gNormal

//Wolfram alpha is suggesting that it's approximately: 3(a_0) + 7/8,
// but I don't know!


public class BasicGetTopEigenvalueMemoizeTube {

	
	public static void main(String[] args) {
		
		initializePow2();
		
		int NUM_IT = 30;
		int PERIOD_DEBUG_PER_LOOP = Math.max(1, NUM_IT/10);
		
		
		
		for(int n=21; n<=21; n++) {
			
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
					//Tube check:
					if(bitsI[0] != bitsI[bitsI.length - 1] && bitsI[bitsI.length - 1] == bitsJ[0] &&   bitsI[0] == bitsJ[bitsI.length - 1]) {
						//pass!
					} else {
						newVector[i] += vector[j];
					}
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
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:


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
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 2:
Current eigenvalue: 4.0
Current eigenvalue: 3.56
Current eigenvalue: 3.561558901682905
Current eigenvalue: 3.5615527889422607
Current eigenvalue: 3.5615528129023803
Current eigenvalue: 3.5615528128084635
Current eigenvalue: 3.5615528128088316
Current eigenvalue: 3.5615528128088303
Current eigenvalue: 3.5615528128088303
Current eigenvalue: 3.5615528128088303
Final eigenvalue: 3.5615528128088303
Estimated growth rate: 1.8872076761206835
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 3:
Current eigenvalue: 8.0
Current eigenvalue: 5.992805755395683
Current eigenvalue: 6.0000333411129265
Current eigenvalue: 5.9999998456437345
Current eigenvalue: 6.000000000714612
Current eigenvalue: 5.999999999996691
Current eigenvalue: 6.000000000000014
Current eigenvalue: 6.0
Current eigenvalue: 6.0
Current eigenvalue: 6.0
Final eigenvalue: 6.0
Estimated growth rate: 1.8171205928321397
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 4:
Current eigenvalue: 16.0
Current eigenvalue: 10.619377162629759
Current eigenvalue: 10.62994117064741
Current eigenvalue: 10.629912328314395
Current eigenvalue: 10.629912415138248
Current eigenvalue: 10.629912414927668
Current eigenvalue: 10.629912414928466
Current eigenvalue: 10.629912414928466
Current eigenvalue: 10.629912414928466
Current eigenvalue: 10.629912414928466
Final eigenvalue: 10.629912414928464
Estimated growth rate: 1.805645215864932
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 5:
Current eigenvalue: 32.0
Current eigenvalue: 18.98032428493351
Current eigenvalue: 18.99707210022995
Current eigenvalue: 18.99704259340446
Current eigenvalue: 18.997042650616542
Current eigenvalue: 18.99704265051366
Current eigenvalue: 18.99704265051386
Current eigenvalue: 18.99704265051386
Current eigenvalue: 18.99704265051386
Current eigenvalue: 18.99704265051386
Final eigenvalue: 18.99704265051386
Estimated growth rate: 1.8019270280992905
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 6:
Current eigenvalue: 64.0
Current eigenvalue: 34.080950072709655
Current eigenvalue: 34.10973297469577
Current eigenvalue: 34.10969647226467
Current eigenvalue: 34.10969654501554
Current eigenvalue: 34.10969654503097
Current eigenvalue: 34.109696545031625
Current eigenvalue: 34.10969654503162
Current eigenvalue: 34.10969654503162
Current eigenvalue: 34.10969654503162
Final eigenvalue: 34.10969654503162
Estimated growth rate: 1.800858717951592
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 7:
Current eigenvalue: 128.0
Current eigenvalue: 61.28884211275815
Current eigenvalue: 61.34354832671177
Current eigenvalue: 61.343489905278695
Current eigenvalue: 61.343490008206395
Current eigenvalue: 61.34349000818872
Current eigenvalue: 61.34349000818906
Current eigenvalue: 61.34349000818905
Current eigenvalue: 61.34349000818905
Current eigenvalue: 61.34349000818905
Final eigenvalue: 61.34349000818905
Estimated growth rate: 1.8005098318996733
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 8:
Current eigenvalue: 256.0
Current eigenvalue: 110.28395541362858
Current eigenvalue: 110.39271697124285
Current eigenvalue: 110.39260824004012
Current eigenvalue: 110.39260843403397
Current eigenvalue: 110.39260843411947
Current eigenvalue: 110.39260843412114
Current eigenvalue: 110.39260843412112
Current eigenvalue: 110.39260843412112
Current eigenvalue: 110.39260843412112
Final eigenvalue: 110.39260843412112
Estimated growth rate: 1.800393761516551
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 9:
Current eigenvalue: 512.0
Current eigenvalue: 198.49232110276026
Current eigenvalue: 198.71049271794575
Current eigenvalue: 198.71027893681028
Current eigenvalue: 198.71027931758894
Current eigenvalue: 198.71027931772156
Current eigenvalue: 198.71027931772474
Current eigenvalue: 198.71027931772466
Current eigenvalue: 198.71027931772466
Current eigenvalue: 198.71027931772466
Final eigenvalue: 198.71027931772466
Estimated growth rate: 1.8003536141201562
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 10:
Current eigenvalue: 1024.0
Current eigenvalue: 357.2854887640084
Current eigenvalue: 357.7210019687093
Current eigenvalue: 357.720576514397
Current eigenvalue: 357.72057727123683
Current eigenvalue: 357.7205772715197
Current eigenvalue: 357.7205772715244
Current eigenvalue: 357.7205772715244
Current eigenvalue: 357.7205772715244
Current eigenvalue: 357.7205772715244
Final eigenvalue: 357.7205772715244
Estimated growth rate: 1.800339425996185
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 11:
Current eigenvalue: 2048.0
Current eigenvalue: 643.1368821237875
Current eigenvalue: 643.9991732903243
Current eigenvalue: 643.9983297681302
Current eigenvalue: 643.9983312688381
Current eigenvalue: 643.9983312693922
Current eigenvalue: 643.9983312693996
Current eigenvalue: 643.9983312693994
Current eigenvalue: 643.9983312693995
Current eigenvalue: 643.9983312693993
Final eigenvalue: 643.9983312693994
Estimated growth rate: 1.8003343108385468
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 12:
Current eigenvalue: 4096.0
Current eigenvalue: 1157.705769735629
Current eigenvalue: 1159.399478533962
Current eigenvalue: 1159.3978193043827
Current eigenvalue: 1159.3978222570508
Current eigenvalue: 1159.397822258122
Current eigenvalue: 1159.3978222581418
Current eigenvalue: 1159.3978222581409
Current eigenvalue: 1159.3978222581409
Current eigenvalue: 1159.3978222581395
Final eigenvalue: 1159.3978222581393
Estimated growth rate: 1.8003324384564048
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 13:
Current eigenvalue: 8192.0
Current eigenvalue: 2083.990692356157
Current eigenvalue: 2087.2942772970905
Current eigenvalue: 2087.2910384167776
Current eigenvalue: 2087.2910441809786
Current eigenvalue: 2087.2910441830168
Current eigenvalue: 2087.2910441831023
Current eigenvalue: 2087.2910441831013
Current eigenvalue: 2087.2910441831004
Current eigenvalue: 2087.291044183101
Final eigenvalue: 2087.291044183094
Estimated growth rate: 1.800331744175025
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 14:
Current eigenvalue: 16384.0
Current eigenvalue: 3751.4098521020155
Current eigenvalue: 3757.8149927110003
Current eigenvalue: 3757.808710921516
Current eigenvalue: 3757.8087221014666
Current eigenvalue: 3757.8087221053524
Current eigenvalue: 3757.808722105418
Current eigenvalue: 3757.8087221054197
Current eigenvalue: 3757.808722105419
Current eigenvalue: 3757.8087221054197
Final eigenvalue: 3757.8087221054197
Estimated growth rate: 1.8003314839581552
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:


N = 15:
Current eigenvalue: 32768.0
Current eigenvalue: 6752.953169023184
Current eigenvalue: 6765.3079018099015
Current eigenvalue: 6765.295783401677
Current eigenvalue: 6765.29580496969
Current eigenvalue: 6765.295804976944
Current eigenvalue: 6765.295804977414
Current eigenvalue: 6765.295804977404
Current eigenvalue: 6765.295804977394
Current eigenvalue: 6765.295804977408
Final eigenvalue: 6765.295804977407
Estimated growth rate: 1.8003313855298055
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

N = 16:
Current eigenvalue: 65536.0
Current eigenvalue: 12156.069160009032
Current eigenvalue: 12179.793538768663
Current eigenvalue: 12179.770266667903
Current eigenvalue: 12179.770308086052
Current eigenvalue: 12179.770308100935
Current eigenvalue: 12179.770308101026
Current eigenvalue: 12179.770308100964
Current eigenvalue: 12179.770308101028
Current eigenvalue: 12179.770308100964
Final eigenvalue: 12179.770308100966
Estimated growth rate: 1.800331348003789

N = 17:
Current eigenvalue: 131072.0
Current eigenvalue: 21882.284994002774
Current eigenvalue: 21927.663751118664
Current eigenvalue: 21927.61923514889
Current eigenvalue: 21927.619314371823
Current eigenvalue: 21927.619314401465
Current eigenvalue: 21927.619314401396
Current eigenvalue: 21927.61931440141
Current eigenvalue: 21927.619314401403
Current eigenvalue: 21927.61931440141
Final eigenvalue: 21927.619314401407
Estimated growth rate: 1.8003313335982305
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:

Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:


N = 18:
Current eigenvalue: 262144.0
Current eigenvalue: 39390.565182437356
Current eigenvalue: 39477.06263332835
Current eigenvalue: 39476.97777594986
Current eigenvalue: 39476.977926972824
Current eigenvalue: 39476.97792702902
Current eigenvalue: 39476.97792703004
Current eigenvalue: 39476.97792703004
Current eigenvalue: 39476.97792703004
Current eigenvalue: 39476.97792703004
Final eigenvalue: 39476.97792703005
Estimated growth rate: 1.8003313280347228
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:


N = 19:
Current eigenvalue: 524288.0
Current eigenvalue: 70907.43387712556
Current eigenvalue: 71071.79944895496
Current eigenvalue: 71071.63819081962
Current eigenvalue: 71071.63847779515
Current eigenvalue: 71071.63847790452
Current eigenvalue: 71071.63847790394
Current eigenvalue: 71071.638477904
Current eigenvalue: 71071.63847790395
Current eigenvalue: 71071.63847790397
Final eigenvalue: 71071.63847790397
Estimated growth rate: 1.8003313258745517
Memorizing the answers to convert Bool:
Done Memorizing the answers to convert Bool:


N = 20:
Current eigenvalue: 1048576.0
Current eigenvalue: 127641.33298602815
Current eigenvalue: 127952.8009884469
Current eigenvalue: 127952.49539100162
Current eigenvalue: 127952.49593492241
Current eigenvalue: 127952.49593509594
Current eigenvalue: 127952.4959351034
Current eigenvalue: 127952.49593510336
Current eigenvalue: 127952.49593510333
Current eigenvalue: 127952.49593510333
Final eigenvalue: 127952.49593510333
Estimated growth rate: 1.8003313250318118


N = 21:
Current eigenvalue: 2097152.0
Current eigenvalue: 229768.71515436203
Current eigenvalue: 230357.4623172757

estimate:
1.80033132503
tends to
1.8003313248
*/