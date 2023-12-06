package year2023;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Problem1 {
    private static final Map<String, String> WORD_TO_NUMBER_MAP = new HashMap<>() {{
        put("one", "1");
        put("two", "2");
        put("three", "3");
        put("four", "4");
        put("five", "5");
        put("six", "6");
        put("seven", "7");
        put("eight", "8");
        put("nine", "9");
    }};
    public static int solve(final String[] lines) {
        // Read document and parse lines
        return Arrays.stream(lines)
                .map(Problem1::replaceWrittenNumbers)
                .map(Problem1::parseLine)
                .reduce(0, Integer::sum);
    }

    private static String replaceWrittenNumbers(String line) {
        final Set<String> wordKeys = WORD_TO_NUMBER_MAP.keySet();
        String newLine = "";
        int startIndex = 0;
        int startIndexEnd = 0;
        for (int i = 0; i <= line.length(); i++) {
            String subString = line.substring(startIndex, i);
            for (String word: wordKeys) {
                if (subString.contains(word)) {
                    startIndex = i - 1;
                    startIndexEnd = i;
                    newLine = newLine.concat(subString.replace(word, WORD_TO_NUMBER_MAP.get(word)));
                }
            }
        }
        newLine = newLine.concat(line.substring(startIndexEnd));
        return newLine;
    }

    private static int parseLine(final String line) {
        final List<String> onlyNums = Arrays.stream(line.split("[a-zA-Z\\r\\n]*"))
                .filter(str -> !str.isEmpty())
                .toList();
        if (onlyNums.isEmpty()) {
            throw new RuntimeException(String.format(
                    "There should be at least one number in the given line %s", line));
        }
        final String result = onlyNums.get(0).concat(onlyNums.get(onlyNums.size()-1));
        return Integer.parseInt(result);
    }
}
