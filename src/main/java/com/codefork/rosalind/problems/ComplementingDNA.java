package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;

public class ComplementingDNA extends Problem {
    @Override
    public String getID() {
        return "REVC";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        StringBuilder buf = new StringBuilder();
        input.chars().forEach(ch -> {
            buf.insert(0, switch(ch) {
                case 'A' -> 'T';
                case 'C' -> 'G';
                case 'G' -> 'C';
                case 'T' -> 'A';
                default -> throw new RuntimeException(String.format("Unrecognized base: %s", ch));
            });
        });
        return buf.toString();
    }

    @Override
    public String getSampleDataset() {
        return "AAAACCCGGT";
    }

    public static void main(String[] args) {
        new ComplementingDNA().run();
    }
}
