package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.Genotype;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.IntStream;

import static com.codefork.rosalind.util.Math.fact;

public class IndependentAlleles extends Problem {

    public static int OFFSPRING_PER_GENERATION = 2;

    /**
     * Math.pow() but for floats
     */
    public static float pow(float f, int power) {
        if(power == 0) {
            return 1;
        }
        var result = f;
        for(var i=0; i < power-1; i++) {
            result *= f;
        }
        return result;
    }

    /**
     * probability mass function for binomial distribution, for calculating exactly k successes out of n trials
     * https://en.wikipedia.org/wiki/Binomial_distribution#Probability_mass_function
     *
     * @param p probability of success
     * @param k number of successes
     * @param n number of trials
     * @return
     */
    public static float pmf(float p, int k, int n) {
        //System.out.println("p = " + p + " k = " + k + " n = " +n);
        var nChooseK = fact(n).divide(fact(k).multiply(fact(n-k)));
        var nChooseKFloat = Float.parseFloat(nChooseK.toString());
        return nChooseKFloat * (float) pow(p, k) * pow(1.0f-p, n-k);
    }

    // an organism has an ordered list of genotypes (factors)
    public record Organism(List<Genotype> genotypes) {

        // returns Punnett Square of potential offspring (has duplicates)
        public Organism[] mate(Organism other) {
            var results = mateRecursive(other, 0, new Stack<Genotype>(), new ArrayList<Organism>());
            return results.toArray(new Organism[0]);
        }

        // pair every possible offspring for an allele with every possible offspring for every other allele
        private List<Organism> mateRecursive(Organism other, int i, Stack<Genotype> prefix, List<Organism> results) {
            var allele = genotypes().get(i);
            var correspondingAllele = other.genotypes().get(i);
            var offspring = allele.possibleOffspring(correspondingAllele);
            for (var child : offspring) {
                if (i + 1 == genotypes().size()) {
                    var allAlleles = new ArrayList<>(prefix);
                    allAlleles.add(child);
                    results.add(new Organism(allAlleles));
                } else {
                    prefix.push(child);
                    mateRecursive(other, i + 1, prefix, results);
                    prefix.pop();
                }
            }
            return results;
        }

        /**
         * renders this organism's alleles as AaBBCc, etc
         */
        @Override
        public String toString() {
            char letter = 'a';
            StringBuffer buf = new StringBuffer();
            for(var gt : genotypes()) {
                buf.append(gt.toString(letter));
                letter += 1;
            }
            return buf.toString();
        }
    }

    // represent a child organism with a given probability
    public static class Child {
        private final Organism organism;
        private float probability = 1;

        public Child(Organism organism, float probability) {
            this.organism = organism;
            this.probability = probability;
        }

        public Organism getOrganism() {
            return organism;
        }

        public float getProbability() {
            return probability;
        }

        @Override
        public String toString() {
            return organism.toString() + " " + getProbability();
        }
    }

    // consolidate list of Organisms containing duplicates to Child objects having probabilities for each genotype within the set
    public List<Child> orgsToChildren(List<Organism> organisms) {
        Map<Organism, Integer> counts = new HashMap<>();
        for (var org : organisms) {
            counts.put(org, counts.getOrDefault(org, 0) + 1);
        }
        List<Child> results = new ArrayList<>();
        for (var org : counts.keySet()) {
            Child child = new Child(org, (counts.get(org) / (float) organisms.size()));
            results.add(child);
        }
        return results;
    }

    @Override
    public String getID() {
        return "LIA";
    }

    /**
     * Thanks to Rob Abrazado for helping me understand the finer points of probability.
     * https://github.com/robabrazado
     *
     * This was tricky for me because there's really two kinds of probability involved here:
     * 1) P(a child in a generation is an AaBb child). we can treat multiple alleles as random
     *   independent variables (the main point of this exercise) and calculate the probabilities that way.
     *
     * 2) P(at least N AaBb children in the k-th generation). This is, in my opinion, the more
     *   complicated one to figure out. Since we're looking for AaBb children as "successes", we can treat
     *   a generation's children as a binomial distribution and sum the P of every
     *   possible successful outcome from N .. total number of children in the generation,
     *   in order to calculate "at least N."
     */
    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        var parts = input.split(" ");
        var k = Integer.valueOf(parts[0]);
        var n = Integer.valueOf(parts[1]);

        var AaBb = new Organism(Arrays.asList(Genotype.Aa, Genotype.Aa));

        // "Tom"
        var root = new Child(AaBb, 1);

        // given the parameters of this problem, the probability for every child in the generation is the same.
        // I'm not sure I FULLY comprehend why this is, but it's definitely the case.
        // this means we don't need to iterate over k generations to calculate the probabilities,
        // we can just do it once.

        // generate set of possible children and their probabilities
        var children = orgsToChildren(Arrays.asList(root.getOrganism().mate(AaBb)));

        //for (PNode node : children) {
        //    System.out.println(node.getOrganism() + " " + node.getProbability());
        //}

        // probability of child being AaBb
        var p = children.stream().filter(node -> node.getOrganism().equals(AaBb)).findFirst().get().getProbability();

        var genSize = (int) Math.pow(OFFSPRING_PER_GENERATION, k);

        // "at least n" = summation of all the exact possibilities
        var atLeastN = IntStream.range(n, genSize+1).mapToObj(i -> pmf(p, i, genSize)).reduce(0f, (acc, f) -> acc + f);

        return String.format("%f", atLeastN);
    }

    @Override
    public String getSampleDataset() {
        return "2 1";
    }

    public static void main(String[] args) {
        new IndependentAlleles().run();
    }
}
