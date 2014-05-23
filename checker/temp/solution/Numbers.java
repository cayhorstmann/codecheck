public class Numbers {
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
}

