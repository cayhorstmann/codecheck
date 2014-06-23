public class NumbersTester1
{
   public static void main(String[] args)
   {
      Numbers nums = new Numbers();
      System.out.println(nums.countSevens(2476657));
      System.out.println("Expected: 2");
      System.out.println(nums.countSevens(7777));
      System.out.println("Expected: 4");
   }
}
