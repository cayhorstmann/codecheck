/*

In this problem, you draw 〈nshapes∣nShapes∣numShapes∣numshapes∣shapeCount〉 〈squares∣rectangles that are twice as tall as they are wide∣rectangles that are twice as wide as they are tall∣circles〉. 

Your code must work for any value of 〈nshapes∣nShapes∣numShapes∣numshapes∣shapeCount〉.

The first shape has width 〈60∣70∣75∣80〉, and the width of each subsequent shape 〈increases by 10∣increases by 6∣decreases by 4〉 pixels. 

The shapes should be aligned 〈horizontally∣vertically〉. 

The first shape has (x, y) = 〈(10, 20)∣(10, 30)∣(0, 30)〉. 

There should be a gap of 〈2∣4∣5∣6∣10〉 pixels between the shapes. 

Color the first, third, fifth, ... shape in 〈red∣green∣blue∣yellow〉 and the second, fourth, sixth, ...
shape in 〈orange∣pink∣light gray∣dark gray∣cyan∣magenta〉.

Leave the code that prints your ID in the top left corner.

Submit a screen shot problem2.png of your program run (even if 
you didn't produce the correct output). 

[16 points]

*/

public class Problem2
{
   public static void main(String[] args)
   {
      int 〈nshapes∣nShapes∣numShapes∣numshapes∣shapeCount〉 = 〈〉 % 11;
      Text text = new Text(0, 0, "〈〉");
      text.draw();
      

      // Your code here
      double x = ...; // initialize here and update in loop
      double y = ...; // initialize here and update in loop
      double width = ...; // initialize here and update in loop
      double height = ...; // initialize here and update in loop

      // your loop here


   }
}
