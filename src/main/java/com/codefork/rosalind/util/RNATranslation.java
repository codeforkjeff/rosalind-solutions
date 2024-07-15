package com.codefork.rosalind.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * Static methods and variables to support operations involving RNA translation
 * (mRNA -> amino acids)
 */
public class RNATranslation {

    public static final String STOP = "Stop";

    public static Map<String, String> CODON_TABLE = Map.ofEntries(
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

    public static Map<String, List<String>> REVERSE = CODON_TABLE.keySet().stream().reduce(new HashMap<String, List<String>>(),
            (acc, codon) -> {
                var aa = CODON_TABLE.get(codon);
                var list = acc.getOrDefault(aa, new ArrayList<String>());
                list.add(codon);
                acc.put(aa, list);
                return acc;
            }, (r1, r2) -> {
                r1.putAll(r2);
                return r1;
            });

}
