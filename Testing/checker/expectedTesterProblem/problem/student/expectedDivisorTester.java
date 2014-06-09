public class expectedDivisorTester {
    
    public static void main(String[] args) {
        Divisor divisor = new Divisor();
        System.out.println(divisor.numberOfDivisors({=codecheck.randomInt(1000)}));
        System.out.println("Expected: ");
        System.out.println(divisor.numberOfDivisors({=codecheck.randomInt(10)}));
        System.out.println("Expected: ");
    }
}
