/* generate random number between 1 and n */
function randInt(n) { return Math.floor(n * Math.random()) + 1 }

var digitNumber = randInt(10) - 1;

function randStringOfDigit(length) {
    var count = randInt(length + 1) - 1;
    
    var digit = randInt(9); //1 --> 9
    while (digit == digitNumber && count == 0) {
        digit = randInt(9);
    }
    if (digit == digitNumber) 
        count--;    
        
    var r = digit.toString();
    for (var i = 1; i < length; i++) {
        digit = randInt(10) - 1; //0 --> 9
        while (digit == digitNumber && count == 0) {
            digit = randInt(9);
        }
        if (digit == digitNumber) 
            count--;    
        r += (randInt(10) - 1).toString();
    }
    return r;
}

var digitArray = ["Zeros", "Ones", "Twos", "Threes", "Fours", "Fives", "Sixs", "Sevens", "Eights", "Nines"];

var digitString = digitArray[digitNumber];
