
public class Divisor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int n = 28;
		System.out.println(numberOfDivisors(n));
	}

	public static int numberOfDivisors(int n) {
		int count = 0;		
		int m = (int) Math.sqrt(n);
		
		for (int i = 1; i <= m; i++)
			if (n % i == 0) count += 2;
		
		if (m * m == n) count--;
		
		return count;
	}
}
