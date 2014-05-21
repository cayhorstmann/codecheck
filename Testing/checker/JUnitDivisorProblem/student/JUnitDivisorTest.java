import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.horstmann.codecheck.rule.Score;

import java.util.ArrayList;

public class JUnitDivisorTest {
    
    Divisor divisor;
    
    @Score(3)
	@Test(timeout = 100) 
    public void test1() {
        assertEquals("Test 1, n = 10000", 25, Divisor.numberOfDivisors(10000));        
    } 	
    
    @Score(1)
    @Test(timeout = 100) 
    public void test2() {
        assertEquals("Test 2, n = 100", 9, Divisor.numberOfDivisors(100));        
    }
    
    @Test(timeout = 100)
    public void test3() {
        assertEquals("Test 3, n = 1283", 16, Divisor.numberOfDivisors(1283));        
    }
    
    @Test(expected= IndexOutOfBoundsException.class) 
    public void test4() {
        new ArrayList<Integer>().get(0);
    }
}
