package year2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    public static int solve(String[] list) {
        String currentNode = "";
        for (int i = 2; i < list.length; i++) {
            String parsedKey = parseLineAndSetNodes(list[i]);
            if (i == 2) {
                currentNode = parsedKey;
            }
        }

        int numSteps = 0;
        char[] LRInstructions = list[0].toCharArray();
        while (true) {
            if (currentNode.equals("ZZZ")) {
                return numSteps;
            }
            char currentInstruction = LRInstructions[numSteps % (LRInstructions.length)];
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
}
