public class Numbers {
    /**
        Counts the number of digits with value {=digitNumber} in a given number
        @param n any non-negative number
        @return the number of digits with value {=digitNumber} in the decimal representation of n
    */
    //CALL {=codecheck.randomDigitString(7, digitNumber, 3)}
    //CALL {=codecheck.randomDigitString(9, digitNumber, codecheck.randomInt(10))}
    //CALL {=codecheck.randomDigitString(3, digitNumber, 0)}
    //CALL {=codecheck.randomDigitString(5, digitNumber, codecheck.randomInt(5))}
    //CALL {=codecheck.randomDigitString(8, digitNumber, 8)}
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

