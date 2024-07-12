package com.codefork.rosalind.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Iterator for FASTA files
 */
public class FastaIterator implements Iterator<FastaRecord> {
    private final BufferedReader reader;
    private String line;

    public FastaIterator(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
        try {
            this.line = this.reader.readLine();
        } catch (IOException e) {
            System.out.println(String.format("error reading from file", e));
        }
    }

    @Override
    public boolean hasNext() {
        return line != null;
    }

    @Override
    public FastaRecord next() {
        FastaRecord record = null;
        boolean keepGoing = true;
        while (keepGoing) {
            if (record == null) {
                if (this.line.startsWith(">")) {
                    record = new FastaRecord(line, null);
                } else {
                    throw new RuntimeException(String.format("Expected to find a description line, got this instead: %s", this.line));
                }
            } else {
                var seq = record.seq();
                if (seq == null) {
                    seq = "";
                }
                record = new FastaRecord(record.desc(), seq + this.line);
            }

            try {
                this.line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException("Error reading line");
            }
            // keep going until we hit EOF or start of next record
            keepGoing = this.line != null && !this.line.startsWith(">");
        }
        return record;
    }
}

