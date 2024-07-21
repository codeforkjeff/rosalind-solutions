package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.MonoisotopicMassTable;

import java.io.InputStream;

public class CalculatingMassProtein extends Problem {

    @Override
    public String getID() {
        return "PRTM";
    }

    @Override
    public String solve(InputStream is) {
        var input = readSingleLine(is);
        var totalWeight = input.chars().mapToObj(ch ->
            MonoisotopicMassTable.TABLE.get(Character.toString(ch))
        ).reduce(0d, Double::sum);
        return String.format("%.3f", totalWeight);
    }

    @Override
    public String getSampleDataset() {
        return "SKADYEK";
    }

    public static void main(String[] args) {
        new CalculatingMassProtein().run();
    }

}
