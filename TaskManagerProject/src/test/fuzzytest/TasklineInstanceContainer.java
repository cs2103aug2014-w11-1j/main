package test.fuzzytest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import taskline.debug.Taskline;
import main.MainController;

public class TasklineInstanceContainer {
    private final String fileName;
    private final String aliasFileName;

    private final float ratio;
    private final MainController[] mainControllers;
    private final boolean multiple;
    private final Random rand;

    private int indexOfLastInstance;

    private TasklineInstanceContainer(String fileName, String aliasFileName,
            int seed, boolean multiple, float ratio) {
        this.fileName = fileName;
        this.aliasFileName = aliasFileName;
        deleteTestFiles();
        
        this.ratio = ratio;
        this.multiple = multiple;
        rand = new Random(seed);
        
        if (multiple) {
            mainControllers = new MainController[2];
            mainControllers[0] = Taskline.setupTaskLine(fileName, aliasFileName);
            mainControllers[1] = Taskline.setupTaskLine(fileName, aliasFileName);
        } else {
            mainControllers = new MainController[1];
            mainControllers[0] = Taskline.setupTaskLine(fileName, aliasFileName);
        }
    }
    
    public static TasklineInstanceContainer createMonoInstance(String fileName,
            String aliasFileName) {
        return new TasklineInstanceContainer(fileName, aliasFileName,
                0, false, 0);
    }
    
    public static TasklineInstanceContainer createMultiInstance(String fileName,
            String aliasFileName, int seed, float ratio) {
        return new TasklineInstanceContainer(fileName, aliasFileName,
                seed, true, ratio);
    }
    
    public MainController getNextInstance() {
        if (multiple) {
            if (rand.nextDouble() < ratio) {
                indexOfLastInstance = 1;
                return mainControllers[0];
            } else {
                indexOfLastInstance = 2;
                return mainControllers[1];
            }
        } else {
            indexOfLastInstance = 1;
            return mainControllers[0];
        }
    }
    
    public int indexOfLastInstance() {
        return indexOfLastInstance;
    }

    public void close() {
        deleteTestFiles();
    }

    private void deleteTestFiles() {
        try {
            Path path = Paths.get(fileName);
            Files.deleteIfExists(path);
            path = Paths.get(aliasFileName);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
