import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class Homework3B 
{
   
   /**
      Converts a string to CSV.
      @param str a string
      @return the string surrounded by quotation marks, and
      with " replaced by ""
   */
   public static String toCSV(String str)
   {
      return "\"" + str.replace("\"", "\"\"") + "\"";
   }
    

   /**
      Checks if the options are valid.
      @param options the option lines of the problem
   */     
   public static void checkOptions(ArrayList<String> options) throws InputMismatchException
   {
      if (options.size() != 4) throw new InputMismatchException("Expected four options.");

      boolean haveStar = false;
      for (int i = 0; i < options.size(); i++)
      {
         String option = options.get(i);
         if (option.startsWith("*")) 
         {
            if (haveStar) throw new InputMismatchException("More than one *");
            haveStar = true;
            option = option.substring(1);
         }
         String prefix = (char)('A' + i) + ")";
         if (!option.startsWith(prefix)) throw new InputMismatchException(prefix + " expected");
      }      
      if (!haveStar) throw new InputMismatchException("No *");
   }
    
   public static void main(String[] args) throws FileNotFoundException, InputMismatchException 
   {        
      if (args.length == 0) throw new FileNotFoundException("No file specified");
      Scanner in = new Scanner(new File(args[0]), "UTF-8");         
      if (!in.hasNextLine())
         throw new InputMismatchException("No title"); 
      String title = in.nextLine();

      ArrayList<String> options = new ArrayList<String>();
      String questionText = "";
       
      while (options.size() == 0)
      {
         if (!in.hasNextLine()) 
            throw new InputMismatchException("No options"); 
         String line = in.nextLine();
         if (line.startsWith("*A)") || line.startsWith("A)"))
            options.add(line);
         else if (questionText.length() == 0)
            questionText = line;
         else
            questionText = questionText + "\n" + line;
      }
      while (in.hasNextLine())
      {
         options.add(in.nextLine());
      }
       
      checkOptions(options);

      // Printing the data in d2l format
      System.out.println("NewQuestion,MC");
      System.out.println("Title," + toCSV(title));
      System.out.println("QuestionText," + toCSV(questionText));
      System.out.println("Points,1\nDifficulty,1");
      for (int i = 0; i < options.size(); i++)
      {
         System.out.print("Option,");
         String option = options.get(i);
         if (option.startsWith("*"))
            System.out.println("100," + toCSV(option.substring(3).trim()));
         else
            System.out.println("0," + toCSV(option.substring(2).trim()));
      }            
   }
}
