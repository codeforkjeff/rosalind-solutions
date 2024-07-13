package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MortalRabbits extends Problem {

    // a population of rabbits in a given month
    public static class Population {
        private ArrayList<Integer> ages = new ArrayList<>();

        public void nextGeneration(Population previousPopulation, int lifetime) {
            var iter = previousPopulation.getAges().iterator();
            while (iter.hasNext()) {
                var pairAge = iter.next();
                // a pair will reproduce if it's old enough
                if (pairAge > 1) {
                    addPair(1);
                }
                // kill off pair if appropriate
                var newAge = pairAge + 1;
                if (newAge <= lifetime) {
                    addPair(newAge);
                }
            }
        }

        public void addPair(int age) {
            ages.add(age);
        }

        public ArrayList<Integer> getAges() {
            return ages;
        }

        public int getSize() {
            return ages.size();
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            return ages.stream().map(i -> Integer.valueOf(i).toString()).collect(Collectors.joining(" "));

        }
    }

    /**
     * This only works for small values of n (gets ridiculously slow for n >= 35) and uses a ton of memory.
     * BUT since this is correct for small n, we can use it to look for a pattern and develop a "dynamic programming"
     * solution for arbitrarily large n.
     *
     * @param n number of months
     * @param m lifetime of rabbit pairs in months
     */
    public String naiveSolution(int n, int m) {
        Population prevMonth = null;
        for (var i = 0; i < n; i++) {
            var pop = new Population();
            if (i == 0) {
                // a single pair to start
                pop.getAges().add(1);
            } else {
                pop.nextGeneration(prevMonth, m);
            }
            System.out.println("month " + (i + 1) + " = " + pop.getSize());
            prevMonth = pop;
        }
        return String.valueOf(prevMonth.getAges().size());
    }

    @Override
    public String getID() {
        return "FIBD";
    }

    /**
     * pattern is Fn = Fn−1 + Fn−2 - (Fn-m, sort of)
     * use BigInteger or we get overflows
     *
     * @param n number of months
     * @param m lifetime of rabbit pairs in months
     */
    public BigInteger calculatePopSize(int n, int m) {
        ArrayList<BigInteger> popHistory = new ArrayList<>();
        for (var i = 0; i < n; i++) {
            BigInteger pop;
            if (i == 0 || i == 1) {
                pop = new BigInteger("1");
            } else {
                // decrease the population by the pop at m+1 months ago
                var idxDecrease = i - (m + 1);
                // at the first generation where pairs start dying, we have to decrease by 1 (number of starting pairs)
                var decreaseAmount = (i == m ? 1 : 0);
                var decrease = (idxDecrease >= 0) ? popHistory.get(idxDecrease) : decreaseAmount;
                pop = popHistory.get(i - 1).add(popHistory.get(i - 2)).subtract(new BigInteger(String.valueOf(decrease)));
            }
            popHistory.add(pop);
            // System.out.println("month " + (i+1) + " = " + pop);
        }
        return popHistory.getLast();
    }

    @Override
    public String solve(InputStream is) {
        int n, m;
        try (var f = new BufferedReader(new InputStreamReader(is))) {
            var line = f.readLine();
            var parts = line.split(" ");
            n = Integer.parseInt(parts[0]);
            m = Integer.parseInt(parts[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return calculatePopSize(n, m).toString();
    }

    @Override
    public String getSampleDataset() {
        return "6 3";
    }

    public static void main(String[] args) {
        new MortalRabbits().run();
    }
}
