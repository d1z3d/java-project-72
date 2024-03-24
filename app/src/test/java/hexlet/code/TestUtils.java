package hexlet.code;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {
    public static String getDataFromFile(String pathToFile) throws Exception {
        Path path = Paths.get(pathToFile).toAbsolutePath().normalize();
        return Files.readString(path);
    }
    public static String getFixturePath(String directory, String file) {
        return Paths.get("src", "test", "resources", "fixtures", directory, file).toString();
    }
}
