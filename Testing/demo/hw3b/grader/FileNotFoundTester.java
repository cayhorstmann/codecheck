import java.io.FileNotFoundException;

public class FileNotFoundTester
{
    public static void main(String[] args)
    {
     try
     {
        Homework3B.main(new String[] { "nosuchfile.txt" });
        System.out.println("No exception");
     }
     catch (FileNotFoundException ex)
     {
        System.out.println("No file specified.");
     }
     catch (Throwable ex)
     {
        System.out.println("Some other exception");
     }

     System.out.println("Expected: No file specified.");
    }
}