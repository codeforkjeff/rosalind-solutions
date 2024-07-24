package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.ReverseComplement;

import java.io.InputStream;

public class LocatingRestrictionSites extends Problem {

    public static boolean isReversePalindrome(String seq) {
        return seq.equals(new ReverseComplement().apply(seq));
    }

    @Override
    public String getID() {
        return "REVP";
    }

    @Override
    public String solve(InputStream is) {
        var iter = new FastaIterator(is);
        var record = iter.next();
        var seq = record.seq();

        StringBuffer results = new StringBuffer();

        for(var length = 4; length <= 12; length++) {
            for(var i  = 0; i + length <= seq.length(); i++) {
                var candidate = seq.substring(i, i+length);
                if(isReversePalindrome(candidate)) {
                    results.append((i+1) + " " + length + "\n");
                }
            }
        }

        return results.toString();
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_24
                TCAATGCATGCGGGTCTATATGCAT
                """;
    }

    public static void main(String[] args) {
        new LocatingRestrictionSites().run();
    }
}
