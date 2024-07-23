package com.codefork.rosalind.util;

import java.util.Map;

import static java.util.Map.entry;

public class DNACodonTable {

    public static final String STOP = "Stop";

    public static Map<String, String> TABLE = Map.ofEntries(
            entry("TTT", "F"),
            entry("TTC", "F"),
            entry("TTA", "L"),
            entry("TTG", "L"),
            entry("TCT", "S"),
            entry("TCC", "S"),
            entry("TCA", "S"),
            entry("TCG", "S"),
            entry("TAT", "Y"),
            entry("TAC", "Y"),
            entry("TAA", STOP),
            entry("TAG", STOP),
            entry("TGT", "C"),
            entry("TGC", "C"),
            entry("TGA", STOP),
            entry("TGG", "W"),
            entry("CTT", "L"),
            entry("CTC", "L"),
            entry("CTA", "L"),
            entry("CTG", "L"),
            entry("CCT", "P"),
            entry("CCC", "P"),
            entry("CCA", "P"),
            entry("CCG", "P"),
            entry("CAT", "H"),
            entry("CAC", "H"),
            entry("CAA", "Q"),
            entry("CAG", "Q"),
            entry("CGT", "R"),
            entry("CGC", "R"),
            entry("CGA", "R"),
            entry("CGG", "R"),
            entry("ATT", "I"),
            entry("ATC", "I"),
            entry("ATA", "I"),
            entry("ATG", "M"),
            entry("ACT", "T"),
            entry("ACC", "T"),
            entry("ACA", "T"),
            entry("ACG", "T"),
            entry("AAT", "N"),
            entry("AAC", "N"),
            entry("AAA", "K"),
            entry("AAG", "K"),
            entry("AGT", "S"),
            entry("AGC", "S"),
            entry("AGA", "R"),
            entry("AGG", "R"),
            entry("GTT", "V"),
            entry("GTC", "V"),
            entry("GTA", "V"),
            entry("GTG", "V"),
            entry("GCT", "A"),
            entry("GCC", "A"),
            entry("GCA", "A"),
            entry("GCG", "A"),
            entry("GAT", "D"),
            entry("GAC", "D"),
            entry("GAA", "E"),
            entry("GAG", "E"),
            entry("GGT", "G"),
            entry("GGC", "G"),
            entry("GGA", "G"),
            entry("GGG", "G")
    );

    public static final String[] STOP_CODONS = TABLE.entrySet().stream()
            .filter(entry ->entry.getValue().equals(DNACodonTable.STOP))
            .map(Map.Entry::getKey)
            .toArray(String[]::new);

}
