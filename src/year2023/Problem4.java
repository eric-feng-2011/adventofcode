package year2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Problem4 {

    private static class CardSplitObject {
        public List<Integer> scratchNumbers;
        public Set<Integer> winningNumbers;

        public CardSplitObject(List<Integer> scratchNumbers, Set<Integer> winningNumbers) {
            this.scratchNumbers = scratchNumbers;
            this.winningNumbers = winningNumbers;
        }
    }

    private static Map<Integer, Integer> numMatchingFromLineData = new HashMap<>();

    /*
     * Part 1
     */
    public static int solve(String[] cards) {
        int sum = 0;
        for (String card: cards) {
            sum += determineCardValue(card);
        }
        return sum;
    }

    private static int determineCardValue(String card) {
        CardSplitObject cardSplitObject = splitCard(card);
        int power = determineNumMatching(cardSplitObject);
        return (int) Math.pow(2, power - 1);
    }

    /*
     * Part 2
     */
    public static int solvePart2(String[] cards) {
        // Preprocessing to make the rest of it go faster
        List<CardSplitObject> allCardSplitObjects = new ArrayList<>();
        for (String card: cards) {
            CardSplitObject cardSplitObject = splitCard(card);
            allCardSplitObjects.add(cardSplitObject);
        }

        int totalCardsWon = 0;
        for (int i = allCardSplitObjects.size() - 1; i >= 0; i--) {
            totalCardsWon += solvePart2Recursion(allCardSplitObjects, i);
        }

        return allCardSplitObjects.size() + totalCardsWon;
    }

    private static int solvePart2Recursion(List<CardSplitObject> allCardSplitObjects, int startIndex) {
        int numMatchingFromLine = 0;
        if (numMatchingFromLineData.containsKey(startIndex)) {
            numMatchingFromLine = numMatchingFromLineData.get(startIndex);
        } else {
            int numMatchingInLine = determineNumMatching(allCardSplitObjects.get(startIndex));
            for (int j = 1; j <= numMatchingInLine; j++) {
                numMatchingFromLine += solvePart2Recursion(allCardSplitObjects, startIndex + j);
            }
            numMatchingFromLine += numMatchingInLine;
            numMatchingFromLineData.put(startIndex, numMatchingFromLine);
        }
        return numMatchingFromLine;
    }

    private static int determineNumMatching(CardSplitObject cardSplitObject) {
        int number = 0;
        for (Integer scratchNumber: cardSplitObject.scratchNumbers) {
            if (cardSplitObject.winningNumbers.contains(scratchNumber)) {
                number++;
            }
        }
        return number;
    }

    private static CardSplitObject splitCard(String card) {
        String[] cardSplit = card.split("[:/|]");
        List<Integer> scratchNumbers = Arrays.stream(cardSplit[1].split(" "))
                .filter(str -> !str.isEmpty())
                .map(Integer::parseInt).toList();
        Set<Integer> winningNumbers = Arrays.stream(cardSplit[2].split(" "))
                .filter(str -> !str.isEmpty())
                .map(Integer::parseInt).collect(Collectors.toSet());
        return new CardSplitObject(scratchNumbers, winningNumbers);
    }
}
