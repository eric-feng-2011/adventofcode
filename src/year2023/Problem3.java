package year2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Problem3 {
    /*
     * Part1 of Problem 3
     */
    public static int solve (String[] lines) {
        // For each line
        // Split line by '.' and remove empty
        // For resulting array
        // Determine if value is a number
        // if number
        // check if there is a special character around it by
        // passing in indexOf and lastIndexOf of the number
        // and the 3 lines around it

        int sum = 0;
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                sum += determineSum("", lines[i], lines[i+1]);
            } else if (i == lines.length - 1) {
                sum += determineSum(lines[i-1], lines[i], "");
            } else {
                sum += determineSum(lines[i - 1], lines[i], lines[i + 1]);
            }
        }

        return sum;
    }

    private static int determineSum(String previousLine,
                                    String currentLine,
                                    String followingLine) {
        int currentLineSum = 0;
        List<String> splitList = Arrays.stream(currentLine
                        .trim()
                        .split("[^0-9]+"))
                .filter(str -> !str.isEmpty())
                .toList();
        List<int[]> offsetLocations = getOffSetLocations(currentLine, splitList);

        assert(splitList.size() == offsetLocations.size());

        for (int i = 0; i < splitList.size(); i++) {
            try {
                int valueToBeChecked = Integer.parseInt(splitList.get(i));
                if (checkLineContainsSymbolGivenOffsets(previousLine,
                        offsetLocations.get(i)[0],
                        offsetLocations.get(i)[1])
                        || checkLineContainsSymbolGivenOffsets(followingLine,
                        offsetLocations.get(i)[0],
                        offsetLocations.get(i)[1])
                        || checkLineContainsSymbolGivenOffsets(currentLine,
                        offsetLocations.get(i)[0],
                        offsetLocations.get(i)[1])
                ) {
                    currentLineSum += valueToBeChecked;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return currentLineSum;
    }

    private static List<int[]> getOffSetLocations(final String line, List<String> splitList) {
        int startIndex = 0;
        int endIndex = 0;
        List<int[]> offSetLocations = new ArrayList<>();
        for (String value: splitList) {
            startIndex = line.indexOf(value, endIndex);
            endIndex = startIndex + value.length();
            offSetLocations.add(new int[]{startIndex, endIndex});
        }
        return offSetLocations;
    }

    private static boolean checkLineContainsSymbolGivenOffsets(final String line,
                                                               final int startingIndex,
                                                               final int endingIndex) {
        if (line.isEmpty()) {
            return false;
        }
        String lineSubString = line.substring(
                Math.max(startingIndex - 1, 0),
                Math.min(endingIndex + 1, line.length()));
        boolean temp = (lineSubString.matches(".*[^0-9.\r].*"));
        return temp;
    }

    /*
     * Part 2 of Problem 3
     */
    public static int solvePart2(String[] lines) {
        int sum = 0;
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                sum += determineMultiplication("", lines[i], lines[i+1]);
            } else if (i == lines.length - 1) {
                sum += determineMultiplication(lines[i-1], lines[i], "");
            } else {
                sum += determineMultiplication(lines[i - 1], lines[i], lines[i + 1]);
            }
        }

        return sum;
    }

    private static int determineMultiplication(String previousLine,
                                    String currentLine,
                                    String followingLine) {
        if (!currentLine.contains("*")) {
            return 0;
        }
        List<String> starSplitList = Arrays.stream(currentLine
                        .trim()
                        .split("[^*]+"))
                .filter(str -> !str.isEmpty())
                .toList();
        List<int[]> starOffsetLocations = getOffSetLocations(currentLine, starSplitList);

        List<String> currentSplitList = getNumbersSplitList(currentLine);
        List<String> previousSplitList = getNumbersSplitList(previousLine);
        List<String> followingSplitList = getNumbersSplitList(followingLine);
        List<int[]> previousOffsetLocations = getOffSetLocations(previousLine, previousSplitList);
        List<int[]> currentOffsetLocations = getOffSetLocations(currentLine, currentSplitList);
        List<int[]> followingOffsetLocations = getOffSetLocations(followingLine, followingSplitList);

        assert(previousOffsetLocations.size() == previousSplitList.size());
        assert(followingOffsetLocations.size() == followingSplitList.size());
        assert(currentOffsetLocations.size() == currentSplitList.size());

        int currentLineSum = 0;
        for (int i = 0; i < starOffsetLocations.size(); i++) {
            currentLineSum += checkStarContainsTwoNumbersGivenOffsetAndReturn(
                    starOffsetLocations.get(i)[0],
                    currentSplitList,
                    currentOffsetLocations,
                    previousSplitList,
                    previousOffsetLocations,
                    followingSplitList,
                    followingOffsetLocations
            );
        }
        return currentLineSum;
    }

    private static List<String> getNumbersSplitList(String line) {
        return Arrays.stream(line
                        .trim()
                        .split("[^0-9]+"))
                .filter(str -> !str.isEmpty())
                .toList();
    }

    private static int checkStarContainsTwoNumbersGivenOffsetAndReturn(
            final int starOffset,
            final List<String> currentLineNumbers,
            final List<int[]> currentLineOffsets,
            final List<String> previousLineNumbers,
            final List<int[]> previousLineOffsets,
            final List<String> followingLineNumbers,
            final List<int[]> followingLineOffsets
            ) {
        List<Integer> gearNumbers = new ArrayList<>();
        addTouchingNumbers(starOffset, currentLineNumbers, currentLineOffsets, gearNumbers);
        addTouchingNumbers(starOffset, previousLineNumbers, previousLineOffsets, gearNumbers);
        addTouchingNumbers(starOffset, followingLineNumbers, followingLineOffsets, gearNumbers);
        if (gearNumbers.size() == 2) {
            return gearNumbers.get(0) * gearNumbers.get(1);
        } else {
            return 0;
        }
    }

    private static void addTouchingNumbers(
            int starOffset,
            List<String> lineNumbers,
            List<int[]> lineOffsets,
            List<Integer> gearNumbers) {
        for (int i = 0; i < lineNumbers.size(); i++) {
            int[] offsets = lineOffsets.get(i);
            if (offsets[0] - 1 <= starOffset && offsets[1] >= starOffset) {
                gearNumbers.add(Integer.parseInt(lineNumbers.get(i)));
            }
        }
    }
}
