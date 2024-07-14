package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;
import com.codefork.rosalind.util.Genotype;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MendelsFirstLaw extends Problem {

    // node in a probability tree
    public static class PNode {
        private final Genotype selection;
        private final HashMap<Genotype, Integer> remaining = new HashMap<>();
        private float probability = 0;
        private ArrayList<PNode> children = new ArrayList<>();

        public PNode(Genotype selection) {
            this.selection = selection;
        }

        public PNode(Genotype selection, HashMap<Genotype, Integer> remaining) {
            this(selection);
            this.remaining.putAll(remaining);
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        /**
         * Creates a node representing a selection from this node's "remaining"
         * and adds it as a child
         */
        public PNode makeNewSelection(Genotype newSelection) {
            var child = new PNode(newSelection);

            child.setProbability(remaining.get(newSelection) / (float) getSizeRemaining());

            child.getRemaining().putAll(remaining);
            child.getRemaining().put(newSelection, remaining.get(newSelection) - 1);

            this.children.add(child);

            return child;
        }

        public HashMap<Genotype, Integer> getRemaining() {
            return remaining;
        }

        public int getSizeRemaining() {
            return remaining.values().stream().mapToInt(i -> i).sum();
        }

        public Genotype getSelection() {
            return selection;
        }

        public float getProbability() {
            return probability;
        }

        public void setProbability(float probability) {
            this.probability = probability;
        }

        public ArrayList<PNode> getChildren() {
            return children;
        }

        /**
         * for debugging
         */
        public static void printTree(PNode node, int level) {
            var indent = " ".repeat(level * 2);
            var remainingStr = String.format("remaining k=%d m=%d n=%d", node.remaining.get(Genotype.AA), node.remaining.get(Genotype.Aa), node.remaining.get(Genotype.aa));
            System.out.println(indent + node.getSelection().toString() + " " + node.getProbability() + " " + remainingStr);
            for (PNode child : node.getChildren()) {
                printTree(child, level + 1);
            }
        }


    }

    /**
     * return: The probability that two randomly selected mating organisms will produce an individual possessing a
     * dominant allele (and thus displaying the dominant phenotype). Assume that any two organisms can mate.
     */
    public static class DominantAllele {
        private final PNode node;
        private final float probability;

        public DominantAllele(PNode node) {
            this.node = node;

            var probabilities = new ArrayList<Float>();
            traverse(node, new ArrayList<PNode>(), probabilities);
            this.probability = probabilities.stream().reduce(0f, (acc, f) -> acc + f);
        }

        public float getProbability() {
            return probability;
        }

        /**
         * depth-first traversal to find all the probabilities of offspring having a dominant allele.
         * path is always the full path of the node currently being visited.
         */
        private void traverse(PNode node, ArrayList<PNode> path, ArrayList<Float> probabilities) {
            if (node.isLeaf()) {
                // at this point, there should be exactly two nodes in the path
                assert path.size() == 2;
                var mate1 = path.get(0);
                var mate2 = path.get(1);
                var possibleOffspring = mate1.getSelection().possibleOffspring(mate2.getSelection());

                var matingProbability = mate1.getProbability() * mate2.getProbability();
                var dominantProbability = Arrays.stream(possibleOffspring).filter(z -> z.name().contains("A")).count() / (float) 4;
//                System.out.println(String.format("Path = %s", path));
//                System.out.println(String.format("Mates: %s %s, Potential Offspring: %s", mate1.getSelection(), mate2.getSelection(), Arrays.toString(possibleOffspring)));
//                System.out.println(String.format("matingProbability %f, dominantProbability %f", matingProbability, dominantProbability));
//                System.out.println(String.format("total prob = %f", matingProbability * dominantProbability));
                probabilities.add(matingProbability * dominantProbability);
            } else {
                for (var child : node.getChildren()) {
                    path.add(child);
                    traverse(child, path, probabilities);
                    path.removeLast();
                }
            }
        }
    }


    @Override
    public String getID() {
        return "IPRB";
    }

    @Override
    public String solve(InputStream is) {
        String input = readSingleLine(is);
        var parts = input.split(" ");
        var k = Integer.valueOf(parts[0]); // number of homozygous dominant
        var m = Integer.valueOf(parts[1]); // number of heterozygous
        var n = Integer.valueOf(parts[2]); // number of homozygous recessive

        var initial = new HashMap<Genotype, Integer>();
        initial.put(Genotype.AA, k);
        initial.put(Genotype.Aa, m);
        initial.put(Genotype.aa, n);

        // making a tree is a bit weird because it's only ever going to be two levels deep.
        // seems overly abstract, but it follows how the problem was laid out, and it IS
        // actually helpful for understanding combining probabilities

        // zygosity/probability doesn't matter here, we just need a root node
        var root = new PNode(Genotype.AA, initial);

        // make a tree 2 levels deep (2 selections)
        for (Genotype z1 : Genotype.values()) {
            var node = root.makeNewSelection(z1);
            for (Genotype z2 : Genotype.values()) {
                if (node.getRemaining().get(z2) > 0) {
                    node.makeNewSelection(z2);
                }
            }
        }

        // PNode.printTree(root, 1);

        var da = new DominantAllele(root);
        return String.format("%.5f", da.getProbability());
    }

    @Override
    public String getSampleDataset() {
        return "2 2 2";
    }

    public static void main(String[] args) {
        new MendelsFirstLaw().run();
    }
}
