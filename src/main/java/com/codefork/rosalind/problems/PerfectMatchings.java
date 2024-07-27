package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;

import java.io.InputStream;

import static com.codefork.rosalind.util.Math.fact;

public class PerfectMatchings extends Problem {

    @Override
    public String getID() {
        return "PMCH";
    }

    /**
     * The formula given in the problem statement applies to joining a node to
     * every other node, so we can't use it directly, but it gives us a clue about
     * how to approach a solution. Since we only care about the number of perfect matchings
     * and not the contents of those matchings, we can figure out the recurrence relation
     * and avoid building a graph and calculating each possibility.
     *
     * An A base can only form an edge with U, and C with G, so we can count A and C
     * to get number of possible edges containing each letter, to start.
     *
     * For n number of A bases, we can form n possible edges * number of possible edges
     * for n-1 A bases. This is a factorial. We can do the same for C bases.
     *
     * Once we have the factorials for each, we can multiply them to get the total number
     * of combinations.
     */
    @Override
    public String solve(InputStream is) {
        var iter = new FastaIterator(is);
        var record = iter.next();
        var seq = record.seq();

        var numNodes = seq.length();
        // should always be even
        assert numNodes % 2 == 0;

        var numA = seq.chars().filter(ch -> ((char) ch) == 'A').count();
        var numC = seq.chars().filter(ch -> ((char) ch) == 'C').count();

        var numPerfect = fact((int) numA).multiply(fact((int) numC));

        return numPerfect.toString();
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_23
                AGCUAGUCAU
                """;
    }

    public static void main(String[] args) {
        new PerfectMatchings().run();
    }
}
