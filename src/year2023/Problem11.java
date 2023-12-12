package year2023;

import java.util.ArrayList;
import java.util.List;

public class Problem11 {

    // Part 1
    private static List<int[]> galaxyCoordinates = new ArrayList<>();

    public static int solve(String[] lines) {
        List<List<Character>> updatedGalaxyMap = new ArrayList<>();

        int factor = 9;

        // Update the galaxy rows
        for (int i = 0; i < lines.length; i++) {
            List<Character> galaxyRow = new ArrayList<>();
            for (int j = 0; j < lines.length; j++) {
                galaxyRow.add(lines[i].charAt(j));
            }
            if (lines[i].contains("#")) {
                updatedGalaxyMap.add(galaxyRow);
            } else {
                updatedGalaxyMap.add(galaxyRow);
                for (int a = 0; a < factor; a++) {
                    updatedGalaxyMap.add(new ArrayList(galaxyRow));
                }
            }
        }

        // Update the galaxy columns
        for (int x = 0; x < updatedGalaxyMap.get(0).size(); x++) {
            boolean columnContainsGalaxy = false;
            for (int y = 0; y < updatedGalaxyMap.size(); y++) {
                if (updatedGalaxyMap.get(y).get(x).equals('#')) {
                    columnContainsGalaxy = true;
                }
            }
            if (!columnContainsGalaxy) {
                int columnIndexToAddAt = x;
                for (int a = 0; a < factor; a++) {
                    for (int y = 0; y < updatedGalaxyMap.size(); y++) {
                        updatedGalaxyMap.get(y).add(columnIndexToAddAt, '.');
                    }
                    x++; // We need to skip the newly added column
                }
            }
        }

        for (int y = 0; y < updatedGalaxyMap.size(); y++) {
            for (int x = 0; x < updatedGalaxyMap.get(0).size(); x++) {
                if (updatedGalaxyMap.get(y).get(x).equals('#')) {
                    galaxyCoordinates.add(new int[]{x, y});
                }
            }
        }

        int sum = getAllShortedDistances(galaxyCoordinates);

        return sum;
    }

    private static int getAllShortedDistances(List<int[]> galaxyCoordinates) {
        int total = 0;
        for (int i = 0; i < galaxyCoordinates.size(); i++) {
            for (int j = i + 1; j < galaxyCoordinates.size(); j++) {
                total += (Math.abs(galaxyCoordinates.get(i)[0] - galaxyCoordinates.get(j)[0]));
                total += (Math.abs(galaxyCoordinates.get(i)[1] - galaxyCoordinates.get(j)[1]));
            }
        }
        return total;
    }


    // Part 2
    private static List<int[]> galaxyCoordinatesPart2 = new ArrayList<>();
    private static List<Integer> galaxyColumnsToExpand = new ArrayList<>();
    private static List<Integer> galaxyRowsToExpand = new ArrayList<>();

    public static long solvePart2(String[] lines) {
        List<List<Character>> galaxyMap = new ArrayList<>();

        // Update the galaxy rows
        for (int i = 0; i < lines.length; i++) {
            List<Character> galaxyRow = new ArrayList<>();
            for (int j = 0; j < lines.length; j++) {
                galaxyRow.add(lines[i].charAt(j));
            }
            galaxyMap.add(galaxyRow);
            if (!lines[i].contains("#")) {
                galaxyRowsToExpand.add(i);
            }
        }

        // Update the galaxy columns
        for (int x = 0; x < galaxyMap.get(0).size(); x++) {
            boolean columnContainsGalaxy = false;
            for (int y = 0; y < galaxyMap.size(); y++) {
                if (galaxyMap.get(y).get(x).equals('#')) {
                    columnContainsGalaxy = true;
                }
            }
            if (!columnContainsGalaxy) {
                galaxyColumnsToExpand.add(x);
            }
        }

        for (int y = 0; y < galaxyMap.size(); y++) {
            for (int x = 0; x < galaxyMap.get(0).size(); x++) {
                if (galaxyMap.get(y).get(x).equals('#')) {
                    galaxyCoordinatesPart2.add(new int[]{x, y});
                }
            }
        }

        long sum = getAllShortedDistancesPart2(galaxyMap, 2);

        return sum;
    }

    private static long getAllShortedDistancesPart2(List<List<Character>> galaxyMap, int multiplier) {
        long total = 0;
        int factor = 999999;
        for (int i = 0; i < galaxyCoordinatesPart2.size(); i++) {
            for (int j = i + 1; j < galaxyCoordinatesPart2.size(); j++) {
                int galaxyOneX = galaxyCoordinatesPart2.get(i)[0];
                int galaxyTwoX = galaxyCoordinatesPart2.get(j)[0];
                int galaxyOneY = galaxyCoordinatesPart2.get(i)[1];
                int galaxyTwoY = galaxyCoordinatesPart2.get(j)[1];
                // Determine the number of expanses between columns
                for (Integer column: galaxyColumnsToExpand) {
                    assert(galaxyOneX != column && galaxyTwoX != column);
                    if (galaxyOneX < galaxyTwoX) {
                        if (determineNumberBetweenNotInclusive(column, galaxyOneX, galaxyTwoX)) {
                            total += factor;
                        }
                    } else {
                        if (determineNumberBetweenNotInclusive(column, galaxyTwoX, galaxyOneX)) {
                            total += factor;
                        }
                    }
                }

                // Determine the number of expanses between rows
                for (Integer row: galaxyRowsToExpand) {
                    assert(galaxyOneY != row && galaxyTwoY != row);
                    if (galaxyOneY < galaxyTwoY) {
                        if (determineNumberBetweenNotInclusive(row, galaxyOneY, galaxyTwoY)) {
                            total += factor;
                        }
                    } else {
                        if (determineNumberBetweenNotInclusive(row, galaxyTwoY, galaxyOneY)) {
                            total += factor;
                        }
                    }
                }
//                618719584

                total += (Math.abs(galaxyOneX - galaxyTwoX));
                total += (Math.abs(galaxyOneY - galaxyTwoY));
            }
        }
        return total;
    }

    private static boolean determineNumberBetweenNotInclusive(int number, int smaller, int larger) {
        return (smaller < number && number < larger);
    }
}
