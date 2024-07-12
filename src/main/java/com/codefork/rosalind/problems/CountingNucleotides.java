package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;
import java.util.HashMap;

public class CountingNucleotides extends Problem {

    public String count(String seq) {
        var counts = seq.chars().mapToObj(ch -> (char) ch).
                reduce(new HashMap<Character, Integer>(),
                        (acc, base) -> {
                            acc.put(base, acc.getOrDefault(base, 0) + 1);
                            return acc;
                        }, (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        });

        var keys = counts.keySet().stream().sorted().toList();

        return keys.stream().reduce("",
                (acc, base) -> {
                    var sep = acc.length() > 0 ? " " : "";
                    return acc + sep + counts.get(base).toString();
                },
                (s1, s2) -> s1 + s2
        );
    }

    @Override
    public String getID() {
        return "DNA";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        return count(input);
    }

    @Override
    public String getSampleDataset() {
        return "AGCTTTTCATTCTGACTGCAACGGGCAATATGTCTCTGTGTGGATTAAAAAAAGAGTGTCTGATAGCAGC";
    }

    public static void main(String[] args) {
        new CountingNucleotides().run();
    }
}
