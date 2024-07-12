package com.codefork.rosalind;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public abstract class Problem {

    /**
     * Returns problem ID
     */
    public abstract String getID();

    /**
     * Solve this problem given the data in the input stream
     */
    public abstract String solve(InputStream is);

    /**
     * Returns the sample dataset in the problem statement
     */
    public abstract String getSampleDataset();

    public Path getDatasetPath() {
        return FileSystems.getDefault().getPath("datasets/rosalind_" + getID().toLowerCase() + ".txt");
    }

    /**
     * Convenience method to read a single line of input, which is the case for
     * a lot of (but not all) problems.
     */
    public String readSingleLine(InputStream is) {
        try (var f = new BufferedReader(new InputStreamReader(is))) {
            return f.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream createInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    /**
     * run the solution for both the sample dataset and the real dataset,
     * if the file for the latter is available. Real datasets
     * are stored at this path: datasets/rosalind_[PROBLEM_ID].txt
     */
    public void run() {
        System.out.println(String.format("Problem: %s", getID()));
        System.out.println("Sample Output: ");
        System.out.println(solve(createInputStream(getSampleDataset())));
        var path = getDatasetPath().toFile();
        if(path.exists()) {
            try (var is = new FileInputStream(path)) {
                System.out.println("Dataset Output: ");
                System.out.println(solve(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(String.format("Dataset file %s does not exist, skipping", path));
        }
    }

}
