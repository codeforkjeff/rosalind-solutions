package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.DNACodonTable;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.FastaRecord;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RNASplicing extends Problem {

    public static String removeIntrons(String seq, List<FastaRecord> introns) {
        StringBuilder intronsRemoved = new StringBuilder();
        var i = 0;
        while(i < seq.length()) {
            FastaRecord intronFound = null;
            for(var intron: introns) {
                var end = i + intron.seq().length();
                if(end <= seq.length() && seq.substring(i, end).equals(intron.seq())) {
                    intronFound = intron;
                    break;
                }
            }
            if(intronFound != null) {
                i += intronFound.seq().length();
            } else {
                intronsRemoved.append(seq.substring(i, i+1));
                i += 1;
            }
        }
        return intronsRemoved.toString();
    }

    public static String exonsToProteinString(String exons) {
        StringBuilder buf = new StringBuilder();
        var i = 0;
        while(i < exons.length()) {
            var codon = exons.substring(i, i+3);
            if (DNACodonTable.STOP_CODONS.contains(codon)) {
                break;
            }
            var protein = DNACodonTable.TABLE.get(codon);
            buf.append(protein);
            i += 3;
        }
        return buf.toString();
    }

    @Override
    public String getID() {
        return "SPLC";
    }

    @Override
    public String solve(InputStream is) {
        var iter = new FastaIterator(is);

        var s = iter.next().seq();

        var introns = new ArrayList<FastaRecord>();
        while(iter.hasNext()) {
            introns.add(iter.next());
        }

        var intronsRemoved = removeIntrons(s, introns);

        return exonsToProteinString(intronsRemoved);
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_10
                ATGGTCTACATAGCTGACAAACAGCACGTAGCAATCGGTCGAATCTCGAGAGGCATATGGTCACATGATCGGTCGAGCGTGTTTCAAAGTTTGCGCCTAG
                >Rosalind_12
                ATCGGTCGAA
                >Rosalind_15
                ATCGGTCGAGCGTGT
                """;
    }

    public static void main(String[] args) {
        new RNASplicing().run();
    }
}
