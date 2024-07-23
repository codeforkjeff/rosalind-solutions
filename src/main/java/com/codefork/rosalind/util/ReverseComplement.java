package com.codefork.rosalind.util;

import java.util.function.Function;

public class ReverseComplement implements Function<String, String> {

    public String apply(String input) {
        StringBuilder buf = new StringBuilder();
        input.chars().forEach(ch -> {
            buf.append(switch(ch) {
                case 'A' -> 'T';
                case 'C' -> 'G';
                case 'G' -> 'C';
                case 'T' -> 'A';
                default -> throw new RuntimeException(String.format("Unrecognized base: %s", ch));
            });
        });
        return buf.reverse().toString();
    }

}
