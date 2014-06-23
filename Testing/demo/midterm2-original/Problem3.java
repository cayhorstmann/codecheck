/*

On a sheet of paper, make a trace of the mystery method, with
parameter "〈bandanna∣cassavas∣defended∣egresses∣embedded∣levelled∣nineteen∣seedless∣peppered∣essences〉". 

Show the values of ALL SIX variables of this method. 

Whenever a value changes, ON A NEW LINE (and not the 
same line) write down the new value. I want you to show me in which
order the values change. 

When a variable goes out of scope (is no longer valid), place the 
letter X into the column. When multiple variables go out of scope 
at the same time, place the letters X on the same line.

Finally, also on paper, write a useful javadoc comment explaining
what this method does. 

This code requires the indexOf method which you have used in homework
assignments. str.indexOf(ch, fromIndex) gives the first position of
ch in str that is >= fromIndex. For example, 
"Banana".indexOf("a", 2) is 3.

Also note that the replace method replaces ALL instances: 
"Banana".replace("a", "") is "Bnn".

[24 points]

*/

public class Problem3
{
   public String mystery(String str)
   {
      int max = 0;
      String result = "";
      while (str.length() > 0)
      {
         String ch = str.substring(0, 1);
         int count = 1;
         for (int j = str.indexOf(ch, 1); j != -1; j = str.indexOf(ch, j + 1))
         {
            count++;
         }
         if (count == max) 
         {
            result = result + ch;
         }
         else if (count > max)
         {
            result = ch;
            max = count;
         }
         str = str.replace(ch, "");
      }
      return result;
   }
}
