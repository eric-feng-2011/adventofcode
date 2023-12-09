package year2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Problem8 {

    private static Map<String, Node> stringNodeMap = new HashMap<>();

    private static class Node {

        String value;
        String leftNodeValue;
        String rightNodeValue;

        public Node(String value, String leftNode, String rightNode) {
            this.value = value;
            this.leftNodeValue = leftNode;
            this.rightNodeValue = rightNode;
        }
    }

    public static long solve(String[] list) {
        for (int i = 2; i < list.length; i++) {
            parseLineAndSetNodes(list[i]);
        }

        char[] LRInstructions = list[0].toCharArray();

        // Part 1
//        int numSteps = determineNumSteps("AAA", "ZZZ", LRInstructions);

        // Part 2
        long numSteps = 1;
        for (String key: stringNodeMap.keySet()) {
            if (key.charAt(2) == 'A') {
                numSteps = lcm(numSteps, determineNumSteps(key, null, LRInstructions));
            }
        }

        return numSteps;
    }

    private static String parseLineAndSetNodes(String line) {
        String[] nodeInfo = line.split("=");
        assert(nodeInfo.length == 2);

        String[] nodeDirections = nodeInfo[1]
                .replaceAll("[()]", "")
                .split(",");
        assert(nodeDirections.length == 2);

        String nodeValue = nodeInfo[0].trim();
        stringNodeMap.putIfAbsent(nodeValue,
                new Node(nodeValue, nodeDirections[0].trim(), nodeDirections[1].trim()));
        return nodeValue;
    }

    private static int determineNumSteps(String currentNode, String endingNode, char[] LRInstructions) {
        int numSteps = 0;

        while (true) {
            if ((currentNode.equals(endingNode))
                || (Objects.isNull(endingNode) && currentNode.charAt(2) == 'Z')) {
                return numSteps;
            }
            for (char currentInstruction: LRInstructions) {
                switch (currentInstruction) {
                    case 'L':
                        currentNode = stringNodeMap.get(currentNode).leftNodeValue;
                        break;
                    case 'R':
                        currentNode = stringNodeMap.get(currentNode).rightNodeValue;
                        break;
                    default:
                        throw new RuntimeException(String.format("Not a valid direction instruction: %c", currentInstruction));
                }
                numSteps += 1;
            }
        }
    }

    // From baeldung - LCM function
    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}