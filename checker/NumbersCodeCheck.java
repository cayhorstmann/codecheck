public class NumbersCodeCheck {
    /**
        Counts the number of digits with value 7 in a given number
        @param n any non-negative number
        @return the number of digits with value 7 in the decimal representation of n
    */
    //CALL 50065199
    //CALL 3704
    //CALL 147
    //CALL 40
    //CALL 7
    //CALL 9240587
    //CALL 7
    //CALL 0
    //CALL 7777777
    //CALL 1797
    public int countSevens(int n) {
        // your work here
        char d = (char) ('0' + 7); 
        
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
            Object expected = obj2.countSevens(50065199);
            System.out.println(expected);
            Object actual = obj1.countSevens(50065199);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("2"))
        {
            Object expected = obj2.countSevens(3704);
            System.out.println(expected);
            Object actual = obj1.countSevens(3704);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("3"))
        {
            Object expected = obj2.countSevens(147);
            System.out.println(expected);
            Object actual = obj1.countSevens(147);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("4"))
        {
            Object expected = obj2.countSevens(40);
            System.out.println(expected);
            Object actual = obj1.countSevens(40);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("5"))
        {
            Object expected = obj2.countSevens(7);
            System.out.println(expected);
            Object actual = obj1.countSevens(7);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("6"))
        {
            Object expected = obj2.countSevens(9240587);
            System.out.println(expected);
            Object actual = obj1.countSevens(9240587);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("7"))
        {
            Object expected = obj2.countSevens(7);
            System.out.println(expected);
            Object actual = obj1.countSevens(7);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("8"))
        {
            Object expected = obj2.countSevens(0);
            System.out.println(expected);
            Object actual = obj1.countSevens(0);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("9"))
        {
            Object expected = obj2.countSevens(7777777);
            System.out.println(expected);
            Object actual = obj1.countSevens(7777777);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("10"))
        {
            Object expected = obj2.countSevens(1797);
            System.out.println(expected);
            Object actual = obj1.countSevens(1797);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
    }
}

