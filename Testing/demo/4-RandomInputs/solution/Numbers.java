public class Numbers {
    /**
        Counts the number of digits with value 7 in a given number
        @param n any non-negative number
        @return the number of digits with value 7 in the decimal representation of n
    */
    //CALL {=randomDigitString(5)}
    //CALL {=randomDigitString(1)}
    //CALL {=randomDigitString(8)}
    //CALL {=randomDigitString(randomInt(8) + 1)}
    //CALL {=randomDigitString(randomInt(6) + 1)}
    public int countSevens(int n) {
        char d = (char) ('0' + 7); 
        
        int result = 0;
        String s = n + "";
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == d)
                result++;
                
        return result;
    }
}

