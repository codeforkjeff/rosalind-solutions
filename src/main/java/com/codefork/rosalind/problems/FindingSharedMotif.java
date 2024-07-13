package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.FastaRecord;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class FindingSharedMotif extends Problem {
    @Override
    public String getID() {
        return "LCSM";
    }

    @Override
    public String solve(InputStream is) {
        var iter = new FastaIterator(is);

        var records = new ArrayList<FastaRecord>();
        while(iter.hasNext()) {
            records.add(iter.next());
        }

        // sort by seq size, and find shortest
        records.sort(Comparator.comparingInt(r -> r.seq().length()));
        var shortest = records.getFirst();
        var others = records.subList(1, records.size());

        var sharedMotifs = new ArrayList<String>();

        // find all substrings in the shortest string, and see if they exist in every other string
        var seq = shortest.seq();
        for(var start=0; start < seq.length(); start++) {
            for(var end=start+1; end <= seq.length(); end++) {
                String candidate = seq.substring(start, end);
                if(others.stream().allMatch(record -> record.seq().contains(candidate))) {
                    sharedMotifs.add(candidate);
                }
            }
        }

        // find longest shared motif
        sharedMotifs.sort(Comparator.comparingInt(String::length));
        return sharedMotifs.getLast();
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_1
                GATTACA
                >Rosalind_2
                TAGACCA
                >Rosalind_3
                ATACA
                """;
    }

    public static void main(String[] args) {
        new FindingSharedMotif().run();
    }

}
