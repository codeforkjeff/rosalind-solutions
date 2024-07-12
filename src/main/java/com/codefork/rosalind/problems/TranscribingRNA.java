package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;
import java.util.stream.Collectors;

public class TranscribingRNA extends Problem {
    @Override
    public String getID() {
        return "RNA";
    }

    @Override
    public String solve(InputStream is) {
        String input = readSingleLine(is);
        return input.chars().mapToObj(ch -> String.valueOf((char) ch)).map((ch) -> {
            switch (ch) {
                case "T": { return "U"; }
                default: { return ch; }
            }
        }).collect(Collectors.joining());
    }

    @Override
    public String getSampleDataset() {
        return "GATGGAACTTGACTACGTAAATT";
    }

    public static void main(String[] args) {
        new TranscribingRNA().run();
    }
}
