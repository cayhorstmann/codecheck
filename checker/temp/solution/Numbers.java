public class Numbers {
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
}

