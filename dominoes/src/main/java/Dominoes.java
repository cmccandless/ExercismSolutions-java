import java.util.*;
import java.util.stream.*;
import java.lang.*;

public class Dominoes {
    public Dominoes() { }

    private static boolean isChain(List<Domino> chain) {
        if (chain.isEmpty()) return true;
        Domino prev = chain.get(chain.size() - 1);
        for (Domino d : chain) {
            if (prev.getRight() != d.getLeft()) {
                return false;
            }
            prev = d;
        }
        return true;
    }

    public List<Domino> formChain(List<Domino> dominoesList) throws ChainNotFoundException {
        if (dominoesList.isEmpty()) return dominoesList;
        PermutationGenerator gen = new PermutationGenerator(dominoesList);
        while (true) {
            List<Domino> chain = gen.next();
            if (isChain(chain)) {
                return chain;
            }
        }
    }

    class PermutationGenerator {;
        private List<Domino> baseChain, subChain;
        private int stop, index = 0, base = 0;
        private PermutationGenerator subGenerator = null;
        private Queue<Integer> uniqueBases = new LinkedList<>(Arrays.asList(0));
        private boolean flipped = false;

        public PermutationGenerator(List<Domino> baseChain) {
            this.baseChain = baseChain.stream()
                .map(d -> d.sorted())
                .sorted()
                .collect(Collectors.toList());
            stop = IntStream.range(1, baseChain.size() + 1).reduce(1, (a, b) -> a * b);
            for (int i = 1; i < baseChain.size(); i++) {
                Domino d = baseChain.get(i);
                if (uniqueBases.stream().allMatch(j -> d != baseChain.get(j))) {
                    uniqueBases.add(i);
                }
            }
            createSubGenerator();
        }

        private void createSubGenerator() {
            base = uniqueBases.poll();
            if (stop > 1) {
                List<Domino> subList = IntStream.range(0, baseChain.size())
                    .filter(i -> i != base)
                    .mapToObj(i -> baseChain.get(i))
                    .collect(Collectors.toList());
                subGenerator = new PermutationGenerator(subList);
            }
        }

        public List<Domino> next() throws ChainNotFoundException {
            if (index == stop) {
                throw new ChainNotFoundException("No domino chain found.");
            }
            List<Domino> perm = new ArrayList<>();
            if (baseChain.size() > 1) {
                try {
                    if (!flipped) {
                        subChain = subGenerator.next();
                    }
                    perm.addAll(subChain);
                } catch (ChainNotFoundException sp) {
                    createSubGenerator();
                    return next();
                }
            }
            perm.add(0, baseChain.get(base));
            if (flipped) {
                perm.set(0, perm.get(0).flip());
                index++;
            }
            flipped = !flipped;
            return perm;
        }
    }
}