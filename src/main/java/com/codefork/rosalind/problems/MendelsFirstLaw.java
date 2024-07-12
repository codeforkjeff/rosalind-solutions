package com.codefork.rosalind.problems;

import com.codefork.rosalind.Problem;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MendelsFirstLaw extends Problem {

    public enum Zygosity {
        AA, // homozygous dominant
        Aa, // heterozygous
        aa; // homozygous recessive

        public Zygosity[] possibleOffspring(Zygosity mate) {
            /* this accomplishes the same thing more abstractly using streams but is way less readable */
            // var offspring = IntStream.range(0, this.name().length()).mapToObj(i -> this.name().substring(i, i+1)).flatMap(ch -> {
            //     return IntStream.range(0, mate.name().length()).mapToObj(i -> ch + mate.name().substring(i, i+1));
            // }).map(zygStr -> normalize(zygStr)).toList();
            // Zygosity[] result = new Zygosity[offspring.size()];
            // return offspring.toArray(result);

            return new Zygosity[] {
                    normalize(this.name().substring(0,1) + mate.name().substring(0, 1)),
                    normalize(this.name().substring(0,1) + mate.name().substring(1, 2)),
                    normalize(this.name().substring(1,2) + mate.name().substring(0, 1)),
                    normalize(this.name().substring(1,2) + mate.name().substring(1, 2)),
            };
        }

        /**
         * normalizes strings like "aA" to "Aa"
         */
        public static Zygosity normalize(String s) {
            var a = s.toCharArray();
            Arrays.sort(a);
            return Zygosity.valueOf(String.valueOf(a));
        }
    }

    // node in a probability tree
    public static class PNode {
        private final Zygosity selection;
        private final HashMap<Zygosity, Integer> remaining = new HashMap<>();
        private float probability = 0;
        private ArrayList<PNode> children = new ArrayList<>();

        public PNode(Zygosity selection) {
            this.selection = selection;
        }

        public PNode(Zygosity selection, HashMap<Zygosity, Integer> remaining) {
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
        public PNode makeNewSelection(Zygosity newSelection) {
            var child = new PNode(newSelection);

            child.setProbability(remaining.get(newSelection) / (float) getSizeRemaining());

            child.getRemaining().putAll(remaining);
            child.getRemaining().put(newSelection, remaining.get(newSelection) - 1);

            this.children.add(child);

            return child;
        }

        public HashMap<Zygosity, Integer> getRemaining() {
            return remaining;
        }

        public int getSizeRemaining() {
            return remaining.values().stream().mapToInt(i -> i).sum();
        }

        public Zygosity getSelection() {
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
            var remainingStr = String.format("remaining k=%d m=%d n=%d", node.remaining.get(Zygosity.AA), node.remaining.get(Zygosity.Aa), node.remaining.get(Zygosity.aa));
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

        var initial = new HashMap<Zygosity, Integer>();
        initial.put(Zygosity.AA, k);
        initial.put(Zygosity.Aa, m);
        initial.put(Zygosity.aa, n);

        // making a tree is a bit weird because it's only ever going to be two levels deep.
        // seems overly abstract, but it follows how the problem was laid out, and it IS
        // actually helpful for understanding combining probabilities

        // zygosity/probability doesn't matter here, we just need a root node
        var root = new PNode(Zygosity.AA, initial);

        // make a tree 2 levels deep (2 selections)
        for (Zygosity z1 : Zygosity.values()) {
            var node = root.makeNewSelection(z1);
            for (Zygosity z2 : Zygosity.values()) {
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
