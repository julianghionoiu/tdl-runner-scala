package befaster.runner;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

public class CredentialsConfigFile {

    public static String readFromConfigFile(String key) throws ConfigNotFoundException {
        Properties properties = new Properties();
        Path config = Paths.get("config", "credentials.config");
        try {
            properties.load(new FileInputStream(config.toFile()));
        } catch (IOException e) {
            throw new ConfigNotFoundException("The \"credentials.config\" has not been found. " +
                    "Please download from challenge page. ( Reason: "+e.getMessage()+" )");
        }

        return Optional.ofNullable(properties.getProperty(key)).orElseThrow(() -> new ConfigNotFoundException(
                "The \"credentials.config\" file does not contain the username ( key="+key+" )"));
    }
}
