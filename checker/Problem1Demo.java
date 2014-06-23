/*
  Look into the Problem1 class and add the four indicated methods.
  You can use this program to test your work.
*/
public class Problem1Demo
{
   public static void main(String[] args)
   {
      Problem1 gallery = new Problem1();
      gallery.add("bellini-titian.jpg");
      gallery.add("cezanne.jpg");
      gallery.add("cole.jpg");
      gallery.add("davinci.jpg");
      gallery.add("degas.jpg");
      gallery.add("fragonard.jpg");
      gallery.add("gaugin.jpg");
      gallery.add("homer.jpg");
      gallery.add("manet2.jpg");
      gallery.add("manet.jpg");
      gallery.add("monet2.jpg");
      gallery.add("monet3.jpg");
      gallery.add("monet4.jpg");
      gallery.add("monet5.jpg");
      gallery.add("monet.jpg");
      gallery.add("raphael.jpg");
      gallery.add("rembrandt.jpg");
      gallery.add("renoir2.jpg");
      gallery.add("renoir.jpg");
      gallery.add("rousseau.jpg");
      gallery.add("titian.jpg");
      gallery.add("vangogh2.jpg");
      gallery.add("vangogh.jpg");
      gallery.add("vermeer.jpg");

      System.out.println(gallery.getTallest());
      System.out.println(Arrays.toString(gallery.getPerimeters()));
      System.out.println(gallery.countShort());
      gallery.removeNarrow(128);
      System.out.println(gallery);
   }
}
