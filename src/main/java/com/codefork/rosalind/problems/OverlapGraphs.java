package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.FastaIterator;
import com.codefork.rosalind.util.FastaRecord;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OverlapGraphs extends Problem {

    public static class Node {
        private FastaRecord record;
        private Set<Node> targets = new HashSet<>();

        public Node(FastaRecord record) {
            this.record = record;
        }

        public FastaRecord getRecord() {
            return record;
        }

        public Set<Node> getTargets() {
            return targets;
        }

        public void addTarget(Node target) {
            targets.add(target);
        }

        @Override
        public String toString() {
            return record.getID() + ", targets=" + targets.toString();
        }

    }

    public static class OverlapGraph {
        private final int k;

        // track prefixes and suffixes of nodes, so we don't have to iterate through
        // all nodes every time we add one to the graph
        private Map<String, List<Node>> prefixes = new HashMap<>();
        private Map<String, List<Node>> suffixes = new HashMap<>();

        public OverlapGraph(int k) {
            this.k = k;
        }

        public void add(Node node) {
            var seq = node.getRecord().seq();
            var prefix = seq.substring(0, k);
            var suffix = seq.substring(seq.length() - k);

            // we don't need to check for directed loops
            // since we add our node only after connecting

            // create vertices
            List<Node> nodesWithSuffix = suffixes.get(prefix);
            if(nodesWithSuffix != null) {
                for(var connectFrom : nodesWithSuffix) {
                    connectFrom.addTarget(node);
                }
            }
            List<Node> nodesWithPrefix = prefixes.get(suffix);
            if(nodesWithPrefix != null) {
                for(var connectTo : nodesWithPrefix) {
                    node.addTarget(connectTo);
                }
            }

            // update prefixes and suffixes maps
            List<Node> suffixNodes = suffixes.get(suffix);
            if(suffixNodes == null) {
                suffixNodes = new ArrayList<>();
                suffixes.put(suffix, suffixNodes);
            }
            suffixNodes.add(node);

            List<Node> prefixNodes = prefixes.get(prefix);
            if(prefixNodes == null) {
                prefixNodes = new ArrayList<>();
                prefixes.put(prefix, prefixNodes);
            }
            prefixNodes.add(node);
        }

        public String toAdjacencyList() {
            StringBuilder buf = new StringBuilder();
            for(var nodeList : prefixes.values()) {
                for(var node : nodeList) {
                    for(var target : node.getTargets()) {
                        buf.append(node.getRecord().getID());
                        buf.append(" ");
                        buf.append(target.getRecord().getID());
                        buf.append("\n");
                    }
                }
            }
            return buf.toString();
        }
    }

    @Override
    public String getID() {
        return "GRPH";
    }

    @Override
    public String solve(InputStream is) {
        var graph = new OverlapGraph(3);

        var iter = new FastaIterator(is);
        while(iter.hasNext()) {
            graph.add(new Node(iter.next()));
        }

        return graph.toAdjacencyList();
    }

    @Override
    public String getSampleDataset() {
        return """
                >Rosalind_0498
                AAATAAA
                >Rosalind_2391
                AAATTTT
                >Rosalind_2323
                TTTTCCC
                >Rosalind_0442
                AAATCCC
                >Rosalind_5013
                GGGTGGG
                """;
    }

    public static void main(String[] args) {
        new OverlapGraphs().run();
    }
}
