package com.codefork.rosalind.util;

import java.math.BigInteger;

public class Math {

    // factorials on inputs will overflow ints, so use BigInteger
    public static BigInteger fact(int x) {
        var one = new BigInteger("1");
        if(x == 0 || x == 1) {
            return one;
        }
        var result = new BigInteger(Integer.toString(x));
        for(var i=x; i > 0; i--) {
            if(i == 1) {
                break;
            } else {
                result = result.multiply(new BigInteger(Integer.toString(i)).subtract(one));
            }
        }
        return result;
    }

}
