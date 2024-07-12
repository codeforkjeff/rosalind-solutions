package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class TranslatingRNA extends Problem {

    private static final String STOP = "Stop";

    private static Map<String, String> codonTable = Map.ofEntries(
            entry("UUU", "F"),
            entry("UUC", "F"),
            entry("UUA", "L"),
            entry("UUG", "L"),
            entry("UCU", "S"),
            entry("UCC", "S"),
            entry("UCA", "S"),
            entry("UCG", "S"),
            entry("UAU", "Y"),
            entry("UAC", "Y"),
            entry("UAA", STOP),
            entry("UAG", STOP),
            entry("UGU", "C"),
            entry("UGC", "C"),
            entry("UGA", STOP),
            entry("UGG", "W"),
            entry("CUU", "L"),
            entry("CUC", "L"),
            entry("CUA", "L"),
            entry("CUG", "L"),
            entry("CCU", "P"),
            entry("CCC", "P"),
            entry("CCA", "P"),
            entry("CCG", "P"),
            entry("CAU", "H"),
            entry("CAC", "H"),
            entry("CAA", "Q"),
            entry("CAG", "Q"),
            entry("CGU", "R"),
            entry("CGC", "R"),
            entry("CGA", "R"),
            entry("CGG", "R"),
            entry("AUU", "I"),
            entry("AUC", "I"),
            entry("AUA", "I"),
            entry("AUG", "M"),
            entry("ACU", "T"),
            entry("ACC", "T"),
            entry("ACA", "T"),
            entry("ACG", "T"),
            entry("AAU", "N"),
            entry("AAC", "N"),
            entry("AAA", "K"),
            entry("AAG", "K"),
            entry("AGU", "S"),
            entry("AGC", "S"),
            entry("AGA", "R"),
            entry("AGG", "R"),
            entry("GUU", "V"),
            entry("GUC", "V"),
            entry("GUA", "V"),
            entry("GUG", "V"),
            entry("GCU", "A"),
            entry("GCC", "A"),
            entry("GCA", "A"),
            entry("GCG", "A"),
            entry("GAU", "D"),
            entry("GAC", "D"),
            entry("GAA", "E"),
            entry("GAG", "E"),
            entry("GGU", "G"),
            entry("GGC", "G"),
            entry("GGA", "G"),
            entry("GGG", "G")
    );

    @Override
    public String getID() {
        return "PROT";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        var codons = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for(int i=0; i < input.length(); i++) {
            buf.append(input.charAt(i));
            if(buf.length() == 3) {
                codons.add(buf.toString());
                buf = new StringBuilder();
            }
        }
        var proteinString = codons.stream()
                .map(codon -> {
                    var prot = codonTable.get(codon);
                    if(prot == null) {
                        throw new RuntimeException("No entry in codon table for " + codon);
                    }
                    return prot;
                })
                .map(prot -> STOP.equals(prot) ? "" : prot)
                .collect(Collectors.joining());
        return proteinString;
    }

    @Override
    public String getSampleDataset() {
        return "AUGGCCAUGGCGCCCAGAACUGAGAUCAAUAGUACCCGUAUUAACGGGUGA";
    }

    public static void main(String[] args) {
        new TranslatingRNA().run();
    }
}
