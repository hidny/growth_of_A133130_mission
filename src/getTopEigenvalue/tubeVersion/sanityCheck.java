package getTopEigenvalue.tubeVersion;

public class sanityCheck {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Sanity check the powers of 2:");
		int cur = 1;
		for(int i=0; i<30; i++) {
			int num1 = (int)Math.pow(2, i);

			if( cur != num1) {
				System.out.println("Doh!");
			}
			System.out.println(num1 + " vs " + cur);
			
			cur *= 2;
			
		}
	}

}
