package befaster.runner;

import com.google.common.io.Files;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

class RoundManagement {
    private static final Path CHALLENGES_FOLDER = Paths.get("challenges");
    private static final Path LAST_FETCHED_ROUND_PATH = CHALLENGES_FOLDER.resolve("XR.txt");

    static String displayAndSaveDescription(String label, String description) {
        System.out.println("Starting round: " + label);
        System.out.println(description);

        //Save description
        Path descriptionPath = CHALLENGES_FOLDER.resolve(label + ".txt");
        try {
            Files.write(description.getBytes(), descriptionPath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Challenge description saved to file: " + descriptionPath + ".");

        //Save round label
        try {
            Files.write(label.getBytes(), LAST_FETCHED_ROUND_PATH.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "OK";
    }

    static String getLastFetchedRound() {
        try {
            return Files.readFirstLine(LAST_FETCHED_ROUND_PATH.toFile(), Charset.defaultCharset());
        } catch (IOException e) {
            return "noRound";
        }
    }
}
