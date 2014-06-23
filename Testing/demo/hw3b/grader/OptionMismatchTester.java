import java.util.InputMismatchException;

public class OptionMismatchTester
{
   public static void test(String filename)
   {
      try
      {
         Homework3B.main(new String[] { filename });
         System.out.println("No exception");
      }
      catch (InputMismatchException ex)
      {
         System.out.println("Mismatch detected");
      }
      catch (Throwable ex)
      {
         System.out.println("Some other exception");
      }

      System.out.println("Expected: Mismatch detected");
   }
   
   public static void main(String[] args)
   {
      test("badoptions.txt");
      test("badoptions2.txt");
      test("badoptions3.txt");
      test("badoptions4.txt");
   }
}
