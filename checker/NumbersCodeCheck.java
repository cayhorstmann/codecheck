public class NumbersCodeCheck {
    /**
        Counts the number of digits with value 6 in a given number
        @param n any non-negative number
        @return the number of digits with value 6 in the decimal representation of n
    */
    //CALL 2000083
    //CALL 481010000
    //CALL 403
    //CALL 41910
    //CALL 50000000
    public int countSixs(int n) {
        // your work here
        char d = (char) ('0' + 6); 
        
        int result = 0;
        String s = n + "";
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == d)
                result++;
                
        return result;
    }
    public static void main(String[] args) 
    {
        Numbers obj1 = new Numbers();
        NumbersCodeCheck obj2 = new NumbersCodeCheck();
        if (args[0].equals("1"))
        {
            Object expected = obj2.countSixs(2000083);
            System.out.println(expected);
            Object actual = obj1.countSixs(2000083);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("2"))
        {
            Object expected = obj2.countSixs(481010000);
            System.out.println(expected);
            Object actual = obj1.countSixs(481010000);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("3"))
        {
            Object expected = obj2.countSixs(403);
            System.out.println(expected);
            Object actual = obj1.countSixs(403);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("4"))
        {
            Object expected = obj2.countSixs(41910);
            System.out.println(expected);
            Object actual = obj1.countSixs(41910);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("5"))
        {
            Object expected = obj2.countSixs(50000000);
            System.out.println(expected);
            Object actual = obj1.countSixs(50000000);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
    }
}

