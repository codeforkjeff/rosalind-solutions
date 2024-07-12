package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;
import java.util.ArrayList;

public class RecurrenceRelations extends Problem {

    /**
     * the pattern is that at n months, the number of pairs = number of pairs at n-1 months,
     * plus number of pairs at n-2 months * k. regular fibonacci sequence doesn't have the multiplier.
     * Must use longs or we overflow.
     */
    public long fn(long n, long k) {
        ArrayList<Long> history = new ArrayList<>();
        for(int i=0; i<n; i++) {
            if(i==0 || i==1) {
                history.add(1L);
            } else {
                history.add(history.get(i - 1) + (history.get(i - 2) * k));
            }
        }
        return history.getLast();
    }

    @Override
    public String getID() {
        return "FIB";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        var parts = input.split(" ");
        var n = Integer.valueOf(parts[0]);
        var k = Integer.valueOf(parts[1]);
        return String.valueOf(fn(n, k));
    }

    @Override
    public String getSampleDataset() {
        return "5 3";
    }

    public static void main(String[] args) {
        new RecurrenceRelations().run();
    }
}
