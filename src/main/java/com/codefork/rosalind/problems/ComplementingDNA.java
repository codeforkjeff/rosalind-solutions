package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.ReverseComplement;

import java.io.InputStream;

public class ComplementingDNA extends Problem {
    @Override
    public String getID() {
        return "REVC";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        return new ReverseComplement().apply(input);
    }

    @Override
    public String getSampleDataset() {
        return "AAAACCCGGT";
    }

    public static void main(String[] args) {
        new ComplementingDNA().run();
    }
}
