public class Numbers {
    /**
        Counts the number of digits with value 7 in a given number
        @param n any non-negative number
        @return the number of digits with value 7 in the decimal representation of n
    */
    //CALL {=randStringOfDigit(8)}
    //CALL {=randStringOfDigit(4)}
    //CALL {=randStringOfDigit(3)}
    //CALL {=randStringOfDigit(2)}
    //CALL {=randStringOfDigit(1)}
    //CALL {=randStringOfDigit(7)}
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
}

