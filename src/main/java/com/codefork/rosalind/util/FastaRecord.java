package com.codefork.rosalind.util;

public record FastaRecord(String desc, String seq) {
    public String getID() {
        return desc().substring(1);
    }
}

