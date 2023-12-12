package year2023;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Problem10 {

    private enum Directions {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private static Pipe[][] pipeGrid;

    private static class Coordinate {
        int x;
        int y;
        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Pipe {
        public Set<Directions> pipeEndsSet;
        public boolean isGround = false;
        public boolean isStart = false;
        public Coordinate coordinate;

        public char pipeShape;

        public Pipe(char pipeShape, int x, int y) {
            coordinate = new Coordinate(x, y);

            this.pipeShape = pipeShape;
            switch(pipeShape) {
                case '|':
                    pipeEndsSet = ImmutableSet.of(Directions.NORTH, Directions.SOUTH);
                    break;
                case '-':
                    pipeEndsSet = ImmutableSet.of(Directions.WEST, Directions.EAST);
                    break;
                case 'L':
                    pipeEndsSet = ImmutableSet.of(Directions.NORTH, Directions.EAST);
                    break;
                case 'J':
                    pipeEndsSet = ImmutableSet.of(Directions.NORTH, Directions.WEST);
                    break;
                case '7':
                    pipeEndsSet = ImmutableSet.of(Directions.SOUTH, Directions.WEST);
                    break;
                case 'F':
                    pipeEndsSet = ImmutableSet.of(Directions.SOUTH, Directions.EAST);
                    break;
                case '.':
                    isGround = true;
                    break;
                default: // Only case left is starting point 'S'
                    isStart = true;
                    pipeEndsSet = ImmutableSet.of(Directions.SOUTH, Directions.EAST, Directions.WEST, Directions.NORTH);
                    break;
            }
        }

        public String toString() {
            return String.valueOf(pipeShape);
        }
    }

    public static int solve(String[] lines) {
        int[] startCoordinates = getPipeGridAndStart(lines);
        assert(startCoordinates.length == 2);

        // Part 1
        List<Pipe> path = determinePathFromStart(startCoordinates[0], startCoordinates[1]);
        assert(path != null);
        System.out.println("Solution one: 7093 - " + Math.ceilDiv(path.size(), 2));

        // Part 2
        // 1. Go through path again and traverse using "Labyrinth" method
        // - Create a pointer representing a character's direction
        // - Walk along the pipe and mark each pipe as being on the character's right or left side
        // - Continue this until the character reaches the starting point again
        // 2. At this point, we have created two categories of points. Those that are inside the loop and those that are outside
        // - i.e. depending on whether we start inside or outside the maze, we specify inside or outside direction of loop
        //   in respect to pipe object P
        // 3. We don't know if we started inside or outside, so we print out the size of both categories. One of those answers will
        // be the size of the inside of the loop. The other will be the size outside. The loop components itself can be included
        // in one of those values based on the implementation

        return 0;
    }

    private static int[] getPipeGridAndStart(String[] lines) {
        int pipeGridHeight = lines.length;
        int pipeGridWidth = lines[0].length();

        pipeGrid = new Pipe[pipeGridHeight][pipeGridWidth];
        int startIndexY = -1;
        int startIndexX = -1;
        for (int y = 0; y < lines.length; y++) {
            assert(pipeGridWidth == lines[y].length());
            for (int x = 0; x < lines[y].length(); x++) {
                pipeGrid[y][x] = new Pipe(lines[y].charAt(x), x, y);
                if (pipeGrid[y][x].isStart) {
                    startIndexY = y;
                    startIndexX = x;
                }
            }
        }
        return new int[]{startIndexX, startIndexY};
    }

    private static List<Pipe> determinePathFromStart(int startIndexX, int startIndexY) {
        Pipe start = pipeGrid[startIndexY][startIndexX];
        List<Pipe> path = new ArrayList<>();

        for (Directions direction : Directions.values()) {
            Pipe target = returnNextPipeOrNull(start, Directions.SOUTH);
            if (Objects.nonNull(target) && createsPathIfPossible(start, target, path)) {
                return path;
            } else {
                path.clear();
            }
        }
        return null;
    }

    /*
     * Returns true if the loop completes / finishes on start
     */
    private static boolean createsPathIfPossible(Pipe start, Pipe target, List<Pipe> path) {
        while (!Objects.isNull(target) && !target.isStart) {
            Directions nextDirection = ifTraversableReturnNextDirectionOrNull(start, target);
            if (Objects.nonNull(nextDirection)) {
                path.add(target);
            } else {
                return false;
            }
            start = target;
            target = returnNextPipeOrNull(target, nextDirection);
        }
        return true;
    }

    private static Directions ifTraversableReturnNextDirectionOrNull(Pipe start, Pipe target) {
        if (Objects.isNull(start.pipeEndsSet) || Objects.isNull(target.pipeEndsSet)) {
            return null;
        }

        // subtract start location by target location
        int xDiff = start.coordinate.x - target.coordinate.x;
        int yDiff = start.coordinate.y - target.coordinate.y;

        // One coordinate difference must have absolute value of 1. The other must be 0
        if ((Math.abs(xDiff) == 1 && yDiff == 0)
            || (xDiff == 0 && Math.abs(yDiff) == 1)) {

            if (xDiff != 0) {         // Connection is East <-> West
                switch(xDiff) {
                    case -1: // start -> EAST (right) -> target
                        return (start.pipeEndsSet.contains(Directions.EAST) && target.pipeEndsSet.contains(Directions.WEST)) ?
                                returnDirectionTwo(target, Directions.WEST) : null;
                    case 1: // start -> WEST (left) -> target
                        return (start.pipeEndsSet.contains(Directions.WEST) && target.pipeEndsSet.contains(Directions.EAST)) ?
                                returnDirectionTwo(target, Directions.EAST) : null;
                }
            } else { // Connection is North <-> South
                switch(yDiff) {
                    case -1: // start -> SOUTH (down) -> target
                        return (start.pipeEndsSet.contains(Directions.SOUTH) && target.pipeEndsSet.contains(Directions.NORTH)) ?
                                returnDirectionTwo(target, Directions.NORTH) : null;
                    case 1: // start -> NORTH (up) -> target
                        return (start.pipeEndsSet.contains(Directions.NORTH) && target.pipeEndsSet.contains(Directions.SOUTH)) ?
                                returnDirectionTwo(target, Directions.SOUTH) : null;
                }
            }
        }
        return null;
    }

    private static Pipe returnNextPipeOrNull(Pipe pipe, Directions direction) {
        if (Objects.isNull(direction)) {
            return null;
        }

        try {
            return switch (direction) {
                case NORTH -> pipeGrid[pipe.coordinate.y - 1][pipe.coordinate.x];
                case SOUTH -> pipeGrid[pipe.coordinate.y + 1][pipe.coordinate.x];
                case EAST -> pipeGrid[pipe.coordinate.y][pipe.coordinate.x + 1];
                case WEST -> pipeGrid[pipe.coordinate.y][pipe.coordinate.x - 1];
            };
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    private static Directions returnDirectionTwo(Pipe pipe, Directions directionOne) {
        // This if statement should never be used but just in case
        if (Objects.isNull(pipe.pipeEndsSet)) {
            return null;
        }

        for (Directions direction: pipe.pipeEndsSet) {
            if (!direction.equals(directionOne)) {
                return direction;
            }
        }
        throw new RuntimeException("There should be one direction in the pipeEndsSet that is not the same as directionOne");
    }
}
