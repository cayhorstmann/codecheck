public class expectedDivisorTester {
    
    public static void main(String[] args) {
        Divisor divisor = new Divisor();
        System.out.println(divisor.numberOfDivisors(194));
        System.out.println("Expected: 4");
        System.out.println(divisor.numberOfDivisors(3));
        System.out.println("Expected: 2");
    }
}
