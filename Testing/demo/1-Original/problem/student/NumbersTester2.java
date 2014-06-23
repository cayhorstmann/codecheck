public class NumbersTester2
{
   public static void main(String[] args)
   {
      Numbers nums = new Numbers();
      System.out.println(nums.countSevens(7));
      System.out.println("Expected: 1");
      System.out.println(nums.countSevens(32424));
      System.out.println("Expected: 0");
   }
}
