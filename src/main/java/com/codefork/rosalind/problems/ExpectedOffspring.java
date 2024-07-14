package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.Genotype;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ExpectedOffspring extends Problem {

    public enum Couple {
        AA_AA(Genotype.AA, Genotype.AA),
        AA_Aa(Genotype.AA, Genotype.Aa),
        AA_aa(Genotype.AA, Genotype.aa),
        Aa_Aa(Genotype.Aa, Genotype.Aa),
        Aa_aa(Genotype.Aa, Genotype.aa),
        aa_aa(Genotype.aa, Genotype.aa);

        private final Genotype p1;
        private final Genotype p2;

        Couple(Genotype p1, Genotype p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public Genotype[] possibleOffspring() {
            return p1.possibleOffspring(p2);
        }
    }

    static final int OFFSPRING_PER_GENERATION = 2;

    @Override
    public String getID() {
        return "IEV";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        var parts = input.split(" ");
        ArrayList<Float> probs = new ArrayList<>();
        for(var i=0; i < parts.length; i++) {
            var numCouples = Long.valueOf(parts[i]);
            var c = Couple.values()[i];
            var possibleOffspring = c.possibleOffspring();
            var dominantPheno = Arrays.stream(possibleOffspring).filter(Genotype::isDominantPhenotype).count();
            // Pr(X=k), given num of offspring per gen
            var p = (dominantPheno / (float) possibleOffspring.length) * OFFSPRING_PER_GENERATION;
            // k x Pr(X=k)
            probs.add(numCouples * p);
        }

        var sum = probs.stream().reduce(0f, (acc, f) -> acc + f);
        return String.valueOf(sum);
    }

    @Override
    public String getSampleDataset() {
        return "1 0 0 1 0 1";
    }

    public static void main(String[] args) {
        new ExpectedOffspring().run();
    }
}
