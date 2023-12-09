package year2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem9 {
    public static long solve(String[] lines) {
        long sum = 0;

        // Part 1
//        for (String line: lines) {
//            sum += determineNextValue(line);
//        }

        // Part 2
        for (String line: lines) {
            sum += determineNextValueBackwards(line);
        }
        return sum;
    }

    private static long determineNextValue(String line) {
        List<Long> values = Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
        return determineNextValueRecursion(values);
    }

    private static long determineNextValueBackwards(String line) {
        List<Long> values = Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
        return determineNextValueRecursionBackwards(values);
    }

    private static long determineNextValueRecursion(List<Long> values) {
        if (checkIfAllZeros(values)) {
            return 0;
        }
        List<Long> nextValues = new ArrayList<>();
        for (int i = 1; i < values.size(); i ++) {
            nextValues.add(values.get(i) - values.get(i - 1));
        }
        return values.get(values.size() - 1) + determineNextValueRecursion(nextValues);
    }

    private static long determineNextValueRecursionBackwards(List<Long> values) {
        if (checkIfAllZeros(values)) {
            return 0;
        }
        List<Long> nextValues = new ArrayList<>();
        for (int i = 1; i < values.size(); i ++) {
            nextValues.add(values.get(i) - values.get(i - 1));
        }
        return values.get(0) - determineNextValueRecursionBackwards(nextValues);
    }

    private static boolean checkIfAllZeros(List<Long> values) {
        for (Long value: values) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }
}
