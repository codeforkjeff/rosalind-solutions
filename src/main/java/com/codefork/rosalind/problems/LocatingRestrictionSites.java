package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.ReverseComplement;

import java.io.InputStream;

public class LocatingRestrictionSites extends Problem {

    @Override
    public String getID() {
        return "REVP";
    }

    @Override
    public String solve(InputStream is) {
        var iter = new FastaIterator(is);
        var record = iter.next();
        var seq = record.seq();
        var seqLen = seq.length();

        // calculate the reverse complement once for entire sequence
        // and use it to look up reverse complements for portions of the sequence.
        // this is faster than generating the reverse complement each iteration
        // in the inner loop (2ms vs 5ms for the real dataset)
        var completeRc = new ReverseComplement().apply(seq);
        var completeRcLen = completeRc.length();

        StringBuffer results = new StringBuffer();

        for(var length = 4; length <= 12; length++) {
            for(var i  = 0; i + length <= seqLen; i++) {
                var candidate = seq.substring(i, i+length);
                var rc = completeRc.substring(completeRcLen - (i + length), completeRcLen - i);
                if(candidate.equals(rc)) {
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
