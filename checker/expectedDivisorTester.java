public class expectedDivisorTester {
    
    public static void main(String[] args) {
        Divisor divisor = new Divisor();
        System.out.println(divisor.numberOfDivisors(479));
        System.out.println("Expected: 2");
        System.out.println(divisor.numberOfDivisors(4));
        System.out.println("Expected: 3");
    }
}