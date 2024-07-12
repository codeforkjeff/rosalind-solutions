package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FindingMotif extends Problem {
    @Override
    public String getID() {
        return "SUBS";
    }

    @Override
    public String solve(InputStream is) {
        String seq, motif;
        try (var f = new BufferedReader(new InputStreamReader(is))) {
            seq = f.readLine();
            motif = f.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        var results = new ArrayList<Integer>();
        for(var i = 0; i < seq.length() - motif.length() + 1; i++) {
            var match = true;
            for(var j = 0; j < motif.length(); j++) {
                if(seq.charAt(i+j) != motif.charAt(j)) {
                    match = false;
                    break;
                }
            }
            if(match) {
                // 1-indexed
                results.add(i+1);
            }
        }

        var resultStr = results.stream().map(i -> i.toString()).collect(Collectors.joining(" "));
        return resultStr;
    }

    @Override
    public String getSampleDataset() {
        return """
                GATATATGCATATACTT
                ATAT
                """;
    }

    public static void main(String[] args) {
        new FindingMotif().run();
    }
}
