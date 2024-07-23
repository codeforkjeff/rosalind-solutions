package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FindingProteinMotif extends Problem {

    public static InputStream getUniprotInputStream(String uniprotId) throws IOException {
        var url = new URL("https://www.uniprot.org/uniprot/" + uniprotId + ".fasta");
        var conn = url.openConnection();
        return conn.getInputStream();
    }

    public static final Pattern N_glycosylation_motif = Pattern.compile("N[^P][ST][^P]");

    public static final Integer[] findMotif(String seq) {
        var matchIndices = new ArrayList<Integer>();
        var matcher = N_glycosylation_motif.matcher(seq);
        while(matcher.find()) {
            var start = matcher.start();
            // in order to pick up overlapping matches, we need to reset the matcher each time
            // with the remainder of the input string, and adjust our indices
            var pre = matchIndices.size() > 0 ? matchIndices.getLast() : 0;
            // this becomes 1-indexed
            var adjusted = pre + start + 1;
            matchIndices.add(adjusted);
            matcher = N_glycosylation_motif.matcher(seq.substring(adjusted));
        }
        return matchIndices.toArray(Integer[]::new);
    }

    @Override
    public String getID() {
        return "MPRT";
    }

    /**
     * The datasets from Rosalind are broken for this problem:
     * UniProt returns http 400 response code errors for bad accessions
     */
    @Override
    public String solve(InputStream is) {
        //var s = "NNSST";
        //var result = findMotif(s);
        //System.out.println(Arrays.toString(result));
        StringBuilder buf = new StringBuilder();
        try (var f = new BufferedReader(new InputStreamReader(is))) {
            var uniprotId = f.readLine();
            while(uniprotId != null) {
                // skip over bad accessions
                try {
                    var stream = getUniprotInputStream(uniprotId);
                    var iter = new FastaIterator(stream);
                    while (iter.hasNext()) {
                        var record = iter.next();
                        var indices = findMotif(record.seq());
                        if (indices.length > 0) {
                            buf.append(uniprotId);
                            buf.append("\n");
                            var indicesStr = Arrays.stream(indices).map(String::valueOf).collect(Collectors.joining(" "));
                            buf.append(indicesStr);
                            buf.append("\n");
                        }
                    }
                } catch(IOException e) {
                    System.out.println("Error with uniprotId " + uniprotId + ", skipping it. Error msg: " + e.getMessage());
                }
                uniprotId = f.readLine();
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buf.toString();
    }

    @Override
    public String getSampleDataset() {
        return """
                A2Z669
                B5ZC00
                P07204_TRBM_HUMAN
                P20840_SAG1_YEAST
                """;
    }

    public static void main(String[] args) {
        new FindingProteinMotif().run();
    }

}
