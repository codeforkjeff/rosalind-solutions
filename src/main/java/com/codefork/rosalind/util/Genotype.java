package com.codefork.rosalind.util;

import java.util.Arrays;

public enum Genotype {
    AA, // homozygous dominant
    Aa, // heterozygous
    aa; // homozygous recessive

    public Genotype[] possibleOffspring(Genotype mate) {
        return new Genotype[] {
                normalize(this.name().substring(0,1) + mate.name().substring(0, 1)),
                normalize(this.name().substring(0,1) + mate.name().substring(1, 2)),
                normalize(this.name().substring(1,2) + mate.name().substring(0, 1)),
                normalize(this.name().substring(1,2) + mate.name().substring(1, 2)),
        };
    }

    public boolean isDominantPhenotype() {
        return this.equals(AA) || this.equals(Aa);
    }

    /**
     * normalizes strings like "aA" to "Aa"
     */
    public static Genotype normalize(String s) {
        var a = s.toCharArray();
        Arrays.sort(a);
        return Genotype.valueOf(String.valueOf(a));
    }
}