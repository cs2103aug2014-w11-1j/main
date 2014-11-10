package test.crashtest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import taskline.debug.Taskline;
import main.MainController;

//@author A0065475X
/**
 * Carries either one or two instances of taskline. If it is created in
 * multi-instance mode, it randomly chooses between the two tasklines based
 * on the probability ratio (0<ratio<1) given.
 */
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
    
    /**
     * @param fileName file name of task list
     * @param aliasFileName file name of alias list
     * @return a TasklineInstanceContainer containing only one instance of
     * taskline.
     */
    public static TasklineInstanceContainer createMonoInstance(String fileName,
            String aliasFileName) {
        return new TasklineInstanceContainer(fileName, aliasFileName,
                0, false, 0);
    }
    
    /**
     * @param fileName file name of task list
     * @param aliasFileName file name of alias list
     * @param seed random seed used for getNextInstance()
     * @param ratio getNextInstance() randomly chooses between the two tasklines
     * based on the probability ratio (0<ratio<1) given.
     * @return a TasklineInstanceContainer containing two instances of taskline.
     */
    public static TasklineInstanceContainer createMultiInstance(String fileName,
            String aliasFileName, int seed, float ratio) {
        return new TasklineInstanceContainer(fileName, aliasFileName,
                seed, true, ratio);
    }
    
    /**
     * Retrieves a random instance of Taskline.<br>
     * If in mono-instance mode, it always returns the same instance.<br>
     * If in multi-instance mode, it randomly chooses between the two instances
     * to return, based on the ratio configured.<br>
     * @return an instance of taskline in the form of a MainController.
     */
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
    
    /**
     * @return the index of the last instance retrieved. The tasklines are
     * labelled "1" and "2" respectively.
     */
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
