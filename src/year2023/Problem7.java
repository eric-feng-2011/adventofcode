package year2023;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Problem7 {
    private static final Map<Character, Integer> ORDER_OF_CARDS = new HashMap<>() {{
        put('2', 2);
        put('3', 3);
        put('4', 4);
        put('5', 5);
        put('6', 6);
        put('7', 7);
        put('8', 8);
        put('9', 9);
        put('T', 10);
        put('J', 1); // Changed to 1 for part 2
        put('Q', 12);
        put('K', 13);
        put('A', 14);
    }};
    private static final int HAND_LENGTH = 5;

    private static class Hand implements Comparable {
        public String handString;
        public int bid;

        /*
         * 0 - High Card
         * 1 - Two of a kind
         * 2 - Two Pair
         * 3 - Three of a kind
         * 4 - Full house
         * 5 - Four of a kind
         * 6 - Five of a kind
         */
        public int handRank;

        public Hand(String handString, int bid) {
            assert(handString.length() == HAND_LENGTH);

            this.handString = handString;
            this.bid = bid;

            determineHandRank(this.handString);
        }

        private void determineHandRank(String handString) {
            Map<Character, Integer> cardCounts = new HashMap<>();
            for (int i = 0; i < handString.length(); i++) {
                int currentCount = cardCounts.getOrDefault(handString.charAt(i), 0);
                cardCounts.put(handString.charAt(i), currentCount + 1);
            }
            Collection<Integer> valuesOnly = cardCounts.values();
            Integer numJ = cardCounts.remove('J');
            if (Objects.isNull(numJ)) {
                determineHandRankWithoutJ(valuesOnly);
            } else {
                determineHandRankWithJ(valuesOnly, numJ);
            }
        }

        private void determineHandRankWithJ(Collection<Integer> valuesOnly, int numJ) {
            if (numJ == 5) {
                this.handRank = 6;
            } else {
                Integer maxValue = Collections.max(valuesOnly);
                switch(maxValue + numJ) {
                    case 5:
                        this.handRank = 6;
                        break;
                    case 4:
                        this.handRank = 5;
                        break;
                    case 3:
                        if (numJ == 2) {
                            this.handRank = 3;
                        } else if (numJ == 1) {
                            if (valuesOnly.size() == 3) {
                                this.handRank = 3;
                            } else {
                                this.handRank = 4;
                            }
                        } else {
                            throw new RuntimeException("We should never have max combination of 3 without numJ being 1 or 2");
                        }
                        break;
                    case 2:
                        this.handRank = 1;
                        break;
                    default:
                        throw new RuntimeException("We should never get 'maxValue + numJ' == " + (maxValue + numJ));
                }
            }
        }

        private void determineHandRankWithoutJ(Collection<Integer> valuesOnly) {
            switch (valuesOnly.size()) {
                case 5:
                    this.handRank = 0;
                    break;
                case 4:
                    this.handRank = 1;
                    break;
                case 3:
                    if (valuesOnly.contains(3)) {
                        this.handRank = 3;
                    } else {
                        this.handRank = 2;
                    }
                    break;
                case 2:
                    if (valuesOnly.contains(4)) {
                        this.handRank = 5;
                    } else {
                        this.handRank = 4;
                    }
                    break;
                case 1:
                    this.handRank = 6;
                    break;
                default:
                    throw new RuntimeException("Error determining hand rank");
            }
        }

        @Override
        public int compareTo(Object otherHandObject) {
            if (!otherHandObject.getClass().equals(Hand.class)) {
                throw new RuntimeException("Objects are not comparable");
            }
            Hand otherHand = (Hand) otherHandObject;
            if (this.handRank < otherHand.handRank) {
                return -1;
            } else if (this.handRank > otherHand.handRank) {
                return 1;
            } else {
                for (int i = 0; i < HAND_LENGTH; i++) {
                    int thisHandCardRank = ORDER_OF_CARDS.get(this.handString.charAt(i));
                    int otherHandCardRank = ORDER_OF_CARDS.get(otherHand.handString.charAt(i));
                    if (thisHandCardRank < otherHandCardRank) {
                        return -1;
                    } else if (thisHandCardRank > otherHandCardRank) {
                        return 1;
                    }
                }
            }
            return 0;
        }
    }

    public static int solve(String[] list) {
        List<Hand> handsList = Arrays.asList(list).stream()
                .map(str -> str.split(" "))
                .map(strSplit -> new Hand(strSplit[0], Integer.parseInt(strSplit[1])))
                .sorted()
                .toList();
        int total = 0;
        for (int i = 0; i < handsList.size(); i++) {
            total += handsList.get(i).bid * (i + 1);
        }
        return total;
    }
}