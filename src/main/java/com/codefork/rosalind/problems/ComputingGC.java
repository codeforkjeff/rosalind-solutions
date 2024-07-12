package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaRecord;
import com.codefork.rosalind.util.FastaIterator;

import java.io.InputStream;
import java.util.stream.StreamSupport;

public class ComputingGC extends Problem {

    /** Calculates GC content */
    public static class GCContent {
        private final FastaRecord record;
        private final float gcContent;

        public GCContent(FastaRecord record) {
            this.record = record;
            this.gcContent = calculate();
        }

        public FastaRecord getRecord() {
            return record;
        }

        public float getGcContent() {
            return gcContent;
        }

        private float calculate() {
            var count = 0;
            var seq = this.record.seq();
            var len = seq.length();
            for (var i = 0; i < len; i++) {
                char ch = seq.charAt(i);
                if (ch == 'G' || ch == 'C') {
                    count++;
                }
            }
            return (float) count / (float) len;
        }
    }

    @Override
    public String getID() {
        return "GC";
    }

    @Override
    public String solve(InputStream is) {
        Iterable<FastaRecord> iterable = () -> new FastaIterator(is);
        var stream = StreamSupport.stream(iterable.spliterator(), false);
        var highestGc = stream
                .map(rec -> new GCContent(rec))
                .max((r1, r2) -> Float.compare(r1.getGcContent(), r2.getGcContent()));
        return highestGc.map(gcContent ->
                gcContent.getRecord().getID() + "\n" + String.format("%.5f", gcContent.getGcContent() * 100.0)
        ).orElse("");
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_6404
                CCTGCGGAAGATCGGCACTAGAATAGCCAGAACCGTTTCTCTGAGGCTTCCGGCCTTCCC
                TCCCACTAATAATTCTGAGG
                >Rosalind_5959
                CCATCGGTAGCGCATCCTTAGTCCAATTAAGTCCCTATCCAGGCGCTCCGCCGAAGGTCT
                ATATCCATTTGTCAGCAGACACGC
                >Rosalind_0808
                CCACCCTCGTGGTATGGCTAGGCATTCAGGAACCGGAGAACGCTTCAGACCAGCCCGGAC
                TGGGAACCTGCGGGCAGTAGGTGGAAT
                """;
    }

    public static void main(String[] args) {
        new ComputingGC().run();
    }

}
