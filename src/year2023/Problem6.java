package year2023;

import java.util.Arrays;
import java.util.List;

public class Problem6 {
    private static int determineDistance(int holdDownTime, int raceTime) {
        return (holdDownTime) * (raceTime - holdDownTime);
    }

    public static int solve(String[] lines) {
        // Part 1
//        List<Integer> raceTimes = parseLineForNumbers(lines[0].substring(5));
//        List<Integer> recordTimes = parseLineForNumbers(lines[1].substring(9));

        // Part 2
        List<Long> raceTimes = parseLineForSingleNumber(lines[0].substring(5));
        List<Long> recordTimes = parseLineForSingleNumber(lines[1].substring(9));

        assert(raceTimes.size() == recordTimes.size());

        int totalValidTimesMultiplied = 1;
        for (int i = 0; i < raceTimes.size(); i++) {
            int[] rangeOfHoldTimes = solveForValidHoldTimes(1, raceTimes.get(i), recordTimes.get(i));

            assert(rangeOfHoldTimes.length == 2);

            int rangeValue = rangeOfHoldTimes[1] - rangeOfHoldTimes[0] + 1;
            totalValidTimesMultiplied *= (rangeValue);
        }
        return totalValidTimesMultiplied;
    }

    private static List<Integer> parseLineForNumbers(String line) {
        return Arrays.stream(line
                        .split(" "))
                .filter(str -> !str.isEmpty())
                .map(Integer::parseInt).toList();
    }

    private static int[] solveForValidHoldTimes(int a, long raceTime, long recordTime) {
        // Format of quadratic equation is 0 = x^2 - (raceTime) * x + recordTime
        // a = 1, b = -raceTime, c = recordTime

        // sqrt(b^2 - 4ac)
        double discriminateSqrt = Math.sqrt(Math.pow(raceTime, 2) - (4 * a * recordTime));

        // (-b +- {above}) / 2a
        double quadraticSolutionTopOfRange = Math.floor((raceTime + discriminateSqrt) / (2 * a));
        double quadraticSolutionBottomOfRange = Math.ceil((raceTime - discriminateSqrt) / (2 * a));

        // Need to make sure we beat the time, so we can't have an exact match
        if ((raceTime + discriminateSqrt) / (2 * a) == quadraticSolutionTopOfRange) {
            quadraticSolutionTopOfRange--;
        }
        if ((raceTime - discriminateSqrt) / (2 * a) == quadraticSolutionBottomOfRange) {
            quadraticSolutionBottomOfRange++;
        }

        return new int[]{(int) quadraticSolutionBottomOfRange, (int) quadraticSolutionTopOfRange};
    }

    // Part 2
    private static List<Long> parseLineForSingleNumber(String line) {
        return List.of(Long.parseLong(line
                        .replaceAll(" ", "")));
    }
}
