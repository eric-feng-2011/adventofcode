import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import year2023.Problem5;

public class Main {
    private static final String PATH_TO_PROBLEM_DOCUMENT = "src/year2023/resources/Problem5.txt";

    public static void main(String[] args) {
        // Read document and parse lines
        try {
            final String documentString = Files.readString(Path.of(PATH_TO_PROBLEM_DOCUMENT));
            final String[] lines = documentString.split("\r\n");
            System.out.println(Problem5.solve(lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}