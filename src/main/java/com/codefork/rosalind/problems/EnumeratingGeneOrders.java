package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnumeratingGeneOrders extends Problem {

    public String permStackToString(Stack<Integer> perm) {
        return perm.stream().map(i -> String.valueOf(i+1)).collect(Collectors.joining(" "));
    }

    public List<String> permutations(int n) {
        var set = IntStream.range(0, n).boxed().collect(Collectors.toSet());
        return permutations(n, 0, new Stack<>(), set, new ArrayList<>());
    }

    /**
     * recursively generate permutations
     * @param n lenth of permutation to calculate a list for
     * @param pos current position in output ordering
     * @param perm the permutation calculated so far, as a stack
     * @param remaining remaining items in the set to iterate through for remaining positions
     * @param acc accumulator for final results
     * @return
     */
    public List<String> permutations(int n, int pos, Stack<Integer> perm, Set<Integer> remaining, List<String> acc) {
        if(remaining.size() > 0) {
            for (var element : remaining) {
                perm.push(element);

                Set<Integer> newRemaining = new HashSet<>();
                newRemaining.addAll(remaining);
                newRemaining.remove(element);

                if(newRemaining.size() > 0) {
                    permutations(n, pos + 1, perm, newRemaining, acc);
                } else {
                    acc.add(permStackToString(perm));
                }

                perm.pop();
            }
        }
        return acc;
    }

    @Override
    public String getID() {
        return "PERM";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        var n = Integer.valueOf(input);
        var perms = permutations(n);
        StringBuilder buf = new StringBuilder();
        buf.append(perms.size());
        buf.append("\n");
        for(var p: perms) {
            buf.append(p);
            buf.append("\n");
        }
        return buf.toString();
    }

    @Override
    public String getSampleDataset() {
        return "3";
    }

    public static void main(String[] args) {
        new EnumeratingGeneOrders().run();
    }
}
