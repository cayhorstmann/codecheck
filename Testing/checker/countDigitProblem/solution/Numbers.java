public class Numbers {
    /**
        Counts the number of digits with value {=digitNumber} in a given number
        @param n any non-negative number
        @return the number of digits with value {=digitNumber} in the decimal representation of n
    */
    //CALL {=randStringDigit(8)}
    //CALL {=randStringDigit(4)}
    //CALL {=randStringDigit(3)}
    //CALL {=randStringDigit(5)}
    //CALL {=randStringDigit(1)}
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

