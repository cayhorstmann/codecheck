public class Numbers {
    /**
        Counts the number of digits with value {=digitNumber} in a given number
        @param n any non-negative number
        @return the number of digits with value {=digitNumber} in the decimal representation of n
    */
    //CALL {=codecheck.randomDigitString(7, 0, 3)}
    //CALL {=codecheck.randomDigitString(9, 0, 5)}
    //CALL {=codecheck.randomDigitString(3, 0, 0)}
    //CALL {=codecheck.randomDigitString(5, 0, 1)}
    //CALL {=codecheck.randomDigitString(8, 0, 7)}
    public int count{=digitString}(int n) {
        // your work here
        char d = (char) ('0' + {=digitNumber}); 
        
        int result = 0;
        String s = n + "";
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == d)
                result++;
                
        return result;
    }
}

