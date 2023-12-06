package year2023;

import com.google.common.base.CharMatcher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Problem2 {

    private static final int NUM_RED = 12;
    private static final int NUM_GREEN = 13;
    private static final int NUM_BLUE = 14;

    private static final String BLUE_COLOR_KEY = "blue";
    private static final String RED_COLOR_KEY = "red";
    private static final String GREEN_COLOR_KEY = "green";

    private static final Map<String, Integer> COLOR_TO_NUMBER = new HashMap<>() {{
        put(BLUE_COLOR_KEY, NUM_BLUE);
        put(RED_COLOR_KEY, NUM_RED);
        put(GREEN_COLOR_KEY, NUM_GREEN);
    }};

    public static int solve(String[] lines) {
        return Arrays.stream(lines)
                .map(Problem2::determineMinimum)
                .reduce(0, Integer::sum);
    }

    /*
     * Part 1 of Problem 2
     */
    private static int determineValidity(String line) {
        String[] splitResults = line
                .trim()
                .split("[:;,]");
        if (determineColorValid(splitResults, BLUE_COLOR_KEY)
                && determineColorValid(splitResults, GREEN_COLOR_KEY)
                && determineColorValid(splitResults, RED_COLOR_KEY)) {
            return Integer.parseInt(
                    CharMatcher.inRange('0', '9').retainFrom(splitResults[0]));
        }
        return 0;
    }

    private static boolean determineColorValid(String[] line, String color) {
        for (String gameDraw: line) {
            if (gameDraw.contains(color)) {
                Integer numOfColor = Integer.parseInt(
                        CharMatcher.inRange('0', '9').retainFrom(gameDraw));
                if (numOfColor > COLOR_TO_NUMBER.get(color)) {
                    System.out.println(String.format("Invalid game. Had a most %d %s" +
                            "balls, but pulled %d.", COLOR_TO_NUMBER.get(color), color, numOfColor));
                    return false;
                }
            }
        }
        return true;
    }

    /*
     * Part 2 of Problem 2
     */
    private static int determineMinimum(String line) {
        String[] splitResults = line
                .trim()
                .split("[:;,]");
        return determineMinimumColorNumber(splitResults, BLUE_COLOR_KEY)
                * determineMinimumColorNumber(splitResults, GREEN_COLOR_KEY)
                * determineMinimumColorNumber(splitResults, RED_COLOR_KEY);
    }

    private static int determineMinimumColorNumber(String[] line, String color) {
        int minNumber = 0;
        for (String gameDraw: line) {
            if (gameDraw.contains(color)) {
                int numOfColor = Integer.parseInt(
                        CharMatcher.inRange('0', '9').retainFrom(gameDraw));
                if (numOfColor > minNumber) {
                    minNumber = numOfColor;
                }
            }
        }
        return minNumber;
    }
}
