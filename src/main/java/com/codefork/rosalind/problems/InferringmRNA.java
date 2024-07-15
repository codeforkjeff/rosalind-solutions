package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.RNATranslation;

import java.io.InputStream;
import java.math.BigInteger;

public class InferringmRNA extends Problem {
    @Override
    public String getID() {
        return "MRNA";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        // every possible string will have one of 3 stop codons
        var numStopCodons = RNATranslation.REVERSE.get(RNATranslation.STOP).size();
        var possible = new BigInteger(String.valueOf(numStopCodons));
        for(var i=0; i < input.length(); i++) {
            String aa = input.substring(i, i+1);
            var codons = RNATranslation.REVERSE.get(aa);
            possible = possible.multiply(new BigInteger(String.valueOf(codons.size())));
        }
        var modded = possible.mod(new BigInteger("1000000"));
        return modded.toString();
    }

    @Override
    public String getSampleDataset() {
        return "MA";
    }

    public static void main(String[] args) {
        new InferringmRNA().run();
    }

}
