package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CountingPointMutations extends Problem {
    @Override
    public String getID() {
        return "HAMM";
    }

    @Override
    public String solve(InputStream is) {
        String seq1, seq2;
        try (var f = new BufferedReader(new InputStreamReader(is))) {
            seq1 = f.readLine();
            seq2 = f.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assert seq1.length() == seq2.length();

        var count = 0;
        for(var i = 0; i < seq1.length(); i++) {
            count += seq1.charAt(i) != seq2.charAt(i) ? 1 : 0;
        }
        return String.valueOf(count);
    }

    @Override
    public String getSampleDataset() {
        return """
                GAGCCTACTAACGGGAT
                CATCGTAATGACGGCCT
                """;
    }

    public static void main(String[] args) {
        new CountingPointMutations().run();
    }

}
