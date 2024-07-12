package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.FastaRecord;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Map.entry;

public class ConsensusAndProfile extends Problem {

    private Map<Character, Integer> baseIndices = Map.ofEntries(
            entry('A', 0),
            entry('C', 1),
            entry('G', 2),
            entry('T', 3)
    );

    private Character[] orderedBases = null;

    private Character[] getOrderedBases() {
        if(orderedBases == null) {
            orderedBases = baseIndices.keySet().toArray(new Character[0]);
            Arrays.sort(orderedBases);
        }
        return orderedBases;
    }

    @Override
    public String getID() {
        return "CONS";
    }

    public String createConcensusString(int seqLength, int[][] profileMatrix) {
        var orderedBases = getOrderedBases();
        var concensus = IntStream.range(0, seqLength).mapToObj(i -> {
            var baseIdx = 0;
            Character mostCommonBase = 'Z';
            var highestCount = 0;
            for(Character base : orderedBases) {
                if(profileMatrix[baseIdx][i] >= highestCount) {
                    mostCommonBase = base;
                    highestCount = profileMatrix[baseIdx][i];
                }
                baseIdx++;
            }
            return mostCommonBase.toString();
        }).collect(Collectors.joining());
        return concensus;
    }

    @Override
    public String solve(InputStream is) {
        Iterator<FastaRecord> iter = new FastaIterator(is);
        Iterable<FastaRecord> iterable = () -> iter;

        int[][] profileMatrix = null;
        int seqLength = 0;

        for(FastaRecord record : iterable) {
            var seq = record.seq();
            if(profileMatrix == null) {
                seqLength = seq.length();
                profileMatrix = new int[4][seqLength];
            }
            for(var i=0; i < seqLength; i++) {
                var base = seq.charAt(i);
                var baseIdx = baseIndices.get(base);
                profileMatrix[baseIdx][i] = profileMatrix[baseIdx][i] + 1;
            }
        }

        var result = new StringBuilder();
        result.append(createConcensusString(seqLength, profileMatrix) + "\n");
        var baseIdx = 0;
        for(Character base : getOrderedBases()) {
            result.append(base + ":");
            for(var i=0; i < seqLength; i++) {
                result.append(" " + profileMatrix[baseIdx][i]);
            }
            result.append("\n");
            baseIdx++;
        }
        return result.toString();
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_1
                ATCCAGCT
                >Rosalind_2
                GGGCAACT
                >Rosalind_3
                ATGGATCT
                >Rosalind_4
                AAGCAACC
                >Rosalind_5
                TTGGAACT
                >Rosalind_6
                ATGCCATT
                >Rosalind_7
                ATGGCACT
                """;
    }

    public static void main(String[] args) {
        new ConsensusAndProfile().run();
    }
}
