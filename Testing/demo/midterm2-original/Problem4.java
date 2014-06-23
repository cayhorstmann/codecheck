/*

Consider these Problem4 and Problem4Demo classes. You do not need to
understand the code. This question tests whether you can use the debugger.

a) Using the BlueJ debugger, set a breakpoint on the first line inside the
〈enigma∣puzzle∣doSomething∣helper∣whatchamacallit∣cantThinkOfAName〉 method of Problem4.java. Run Problem4Demo.
What is the value of a and b?

b) Now step through the method. 

What values does j assume as you reach the line // Inspect j

c) What value does the method return?

d) Remove the breakpoint at the beginning of the method and add 
a breakpoint where it says // Set second breakpoint here

Run the program until the second breakpoint is reached. Take a screenshot of the source window with the breakpoint and the debugger window 
that shows the values of ch, count, max, and result. (You can just take a screen shot of the entire screen, e.g. scrot problem4.png in Lubuntu.)

e) What is the value of max and result when the mystery method returns?

Write the answers to a) b) c) e) in this file and also submit a file problem4.png containing your screen capture.

[12 points]

*/

public class Problem4
{
   public String mystery(String str)
   {
      int max = 0;
      String result = "";
      while (str.length() > 0)
      {
         String ch = str.substring(0, 1);

         int count = 〈enigma∣puzzle∣doSomething∣helper∣whatchamacallit∣cantThinkOfAName〉(str, ch);
         if (count == max) 
         {
            result = result + ch;
         }
         else if (count > max)
         {
            result = ch; // Set second breakpoint here
            max = count; 
         }
         str = str.replace(ch, "");
      }
      return result;
   }

   public int 〈enigma∣puzzle∣doSomething∣helper∣whatchamacallit∣cantThinkOfAName〉(String a, String b)
   {      
      int count = 1;
      int j = 1;
      while (true)
      {
         j = a.indexOf(b, j + 1); 
         if (j == -1) // Inspect j
         {
            return count;
         }
         else
         {
            count++;
         }
      }
   }
}
