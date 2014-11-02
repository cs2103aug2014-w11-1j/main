package taskline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TasklineLogger {
    private static final String LOGGER_FILENAME = "taskline.log";
    private static String LOGGER_NAME = "Taskline";

    private static FileHandler loggerFileHandler;

    public static void setupLogger() {
        Logger log = Logger.getLogger(LOGGER_NAME);
        log.setUseParentHandlers(false);

        try {
            Path path = Paths.get(LOGGER_FILENAME + ".lck");
            Files.deleteIfExists(path);

            loggerFileHandler = new FileHandler(LOGGER_FILENAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            loggerFileHandler.setFormatter(formatter);

            log.addHandler(loggerFileHandler);
            log.setLevel(Level.FINEST);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.log(Level.INFO, "Taskline initialised.");
    }

    public static void closeLoggerFileHandler() {
        if (loggerFileHandler != null) {
            loggerFileHandler.close();
        }
    }
    
    public static Logger getLogger() {
        return Logger.getLogger(LOGGER_NAME);
    }
}
