package year2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Problem5 {
    private static final int NUMBER_OF_MAPS = 7;
    private static List<List<Node>> transitionMaps = new ArrayList<>();;
    private static class Node {
        public long destinationStart;
        public long sourceStart;
        public long range;

        public Node(long destinationStart, long sourceStart, long range) {
            this.destinationStart = destinationStart;
            this.sourceStart = sourceStart;
            this.range = range;
        }

        public boolean inNode(long number) {
            return ((sourceStart <= number) && (number <= sourceStart + range - 1));
        }

        public long getRelevantChildNumber(long number) {
            return destinationStart + (number - sourceStart);
        }

        public String toString() {
            return String.format("DestinationStart: %d, SourceStart: %d, Range: %d",
                    this.destinationStart, this.sourceStart, this.range);
        }
    }

    public static long solve(String[] lines) {
        getTransitionMaps(lines);
        List<Long> seedNumbers = getSeeds(lines[0]);

        // Part 1
//        Optional<Long> minLocationNumber = seedNumbers.stream()
//                .map(Problem5::getLocationFromSeed)
//                .min(Long::compareTo);
//        return minLocationNumber.orElseGet(() -> -1L);

        // Part 2
        assert(seedNumbers.size() % 2 == 0);
        long minimum = Long.MAX_VALUE;
        for (int i = 0; i < seedNumbers.size(); i += 2) {
            for (long j = seedNumbers.get(i); j < seedNumbers.get(i) + seedNumbers.get(i + 1); j++) {
                long locationNumber = getLocationFromSeed(j);
                if (locationNumber < minimum) {
                    minimum = locationNumber;
                }
            }
        }
        return minimum;
    }

    private static void getTransitionMaps(String[] lines) {
        for (int i = 0; i < NUMBER_OF_MAPS; i++) {
            transitionMaps.add(new ArrayList<>());
        }

        int mapToUpdate = -1; // Start at -1
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].contains("map")) {
                mapToUpdate++;
            } else {
                List<Long> transitionEntry = parseLineForNumbers(lines[i]);
                if (!transitionEntry.isEmpty()) {
                    assert(transitionEntry.size() == 3);
                    transitionMaps.get(mapToUpdate).add(new Node(
                            transitionEntry.get(0),
                            transitionEntry.get(1),
                            transitionEntry.get(2)
                    ));
                }
            }
        }
    }

    private static List<Long> getSeeds(String seedLine) {
        assert(seedLine.contains("seeds"));
        return parseLineForNumbers(seedLine.substring(6));
    }

    private static long getLocationFromSeed(long seedNumber) {
        long transitionNumber = seedNumber;
        for (int i = 0; i < NUMBER_OF_MAPS; i++) {
            List<Node> transitionMap = transitionMaps.get(i);
            for (Node node: transitionMap) {
                if (node.inNode(transitionNumber)) {
                    transitionNumber = node.getRelevantChildNumber(transitionNumber);
                    break;
                }
            }
        }
        return transitionNumber;
    }

    private static List<Long> parseLineForNumbers(String line) {
        return Arrays.stream(line
                        .split(" "))
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong).toList();
    }
}
