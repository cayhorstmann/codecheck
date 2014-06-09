function randomInt(n) {
    return Math.floor(Math.random() * n);
}

function randomDigitString(length) {
    var r = String.fromCharCode(randomInt(9) + 1 + 48);
    for (var i = 1; i < length; i++)
        r += String.fromCharCode(randomInt(10) + 48);
    return r;
}
