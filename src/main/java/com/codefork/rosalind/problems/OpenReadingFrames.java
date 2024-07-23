package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.DNACodonTable;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.ReverseComplement;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OpenReadingFrames extends Problem {

    public static final Pattern getORFPattern() {
        var stopCodonsGroup = "(" +
                DNACodonTable.STOP_CODONS.stream().collect(Collectors.joining("|")) + ")";
        return Pattern.compile("ATG(.{3})*?" + stopCodonsGroup);
    }

    public static final Pattern pat = getORFPattern();

    public static final Set<String> getCandidateProteinStrings(String seq) {
        var results = new HashSet<String>();

        var remainder = seq;
        var matcher = pat.matcher(remainder);

        while(matcher.find()) {
            var match = remainder.substring(matcher.start(), matcher.end());
            var protein = new StringBuilder();
            var i = 0;
            while(true) {
                var codon = match.substring(i, i+3);
                if(!DNACodonTable.STOP_CODONS.contains(codon)) {
                    protein.append(DNACodonTable.TABLE.get(codon));
                    i += 3;
                } else {
                    break;
                }
            }
            results.add(protein.toString());

            // set remainder to everything after start codon: this ensures detecting any start codons
            // WITHIN last match found
            remainder = remainder.substring(3);
            matcher = pat.matcher(remainder);
        }
        return results;
    }

    @Override
    public String getID() {
        return "ORF";
    }

    @Override
    public String solve(InputStream is) {
        var iter = new FastaIterator(is);
        var seq = iter.next().seq();

        var proteins = getCandidateProteinStrings(seq);
        proteins.addAll(getCandidateProteinStrings(new ReverseComplement().apply(seq)));

        var output = proteins.stream().collect(Collectors.joining("\n"));

        return output;
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_99
                AGCCATGTAGCTAACTCAGGTTACATGGGGATGACCCCGCGACTTGGATTAGAGTCTCTTTTGGAATAAGCCTGAATGATCCGAGTAGCATCTCAG
                """;
    }

    public static void main(String[] args) {
        new OpenReadingFrames().run();
    }
}
