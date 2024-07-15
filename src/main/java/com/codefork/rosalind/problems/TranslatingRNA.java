package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.RNATranslation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class TranslatingRNA extends Problem {

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
                    var prot = RNATranslation.CODON_TABLE.get(codon);
                    if(prot == null) {
                        throw new RuntimeException("No entry in codon table for " + codon);
                    }
                    return prot;
                })
                .map(prot -> RNATranslation.STOP.equals(prot) ? "" : prot)
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
