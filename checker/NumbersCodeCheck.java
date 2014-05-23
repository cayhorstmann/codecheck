public class NumbersCodeCheck {
    /**
        Counts the number of digits with value 0 in a given number
        @param n any non-negative number
        @return the number of digits with value 0 in the decimal representation of n
    */
    //CALL 6071900
    //CALL 400000380
    //CALL 791
    //CALL 76550
    //CALL 90000000
    public int countZeros(int n) {
        // your work here
        char d = (char) ('0' + 0); 
        
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
            Object expected = obj2.countZeros(6071900);
            System.out.println(expected);
            Object actual = obj1.countZeros(6071900);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("2"))
        {
            Object expected = obj2.countZeros(400000380);
            System.out.println(expected);
            Object actual = obj1.countZeros(400000380);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("3"))
        {
            Object expected = obj2.countZeros(791);
            System.out.println(expected);
            Object actual = obj1.countZeros(791);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("4"))
        {
            Object expected = obj2.countZeros(76550);
            System.out.println(expected);
            Object actual = obj1.countZeros(76550);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
        if (args[0].equals("5"))
        {
            Object expected = obj2.countZeros(90000000);
            System.out.println(expected);
            Object actual = obj1.countZeros(90000000);
            System.out.println(actual);
            System.out.println(java.util.Objects.deepEquals(actual, expected));
        }
    }
}

