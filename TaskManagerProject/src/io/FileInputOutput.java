package io;

import io.json.JsonReaderWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import taskline.TasklineLogger;
import data.ITaskDataFileInputOutput;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class FileInputOutput implements IFileInputOutput {
    private static final Logger log = TasklineLogger.getLogger();
    
    private final String fileName;
    private String fileHash = "";
    
    private final ITaskDataFileInputOutput taskData;
    
    public FileInputOutput(TaskData taskData, String fileName) {
        this.fileName = fileName;
        this.taskData = taskData;
    }
    
    /* (non-Javadoc)
     * @see io.IFileInputOutput#read()
     */
    @Override
    public boolean read() {
        
        if (fileUnchanged()) {
            log.log(Level.FINER, "Read from file - Hash unchanged. Will not change TaskData");
            return false;
        }
        log.log(Level.FINER, "Read from file - Hash mismatch.");
        
        TaskInfo[] taskInfos = readTasksFromFile();
        
        if (taskInfos == null) {
            log.log(Level.FINER, "Unable to read tasks from file. Will not change TaskData");
            return false;
        } else {
            log.log(Level.FINER, "New task read from file successfully - Updating TaskData...");
            taskData.updateTaskList(taskInfos);
            return true;
        }
    }

    /* (non-Javadoc)
     * @see io.IFileInputOutput#write()
     */
    @Override
    public boolean write() {
        if (taskData.hasUnsavedChanges()) {
            log.log(Level.FINER, "Write to file: TaskData has unsaved changes. Writing...");
            boolean result = writeTasksToFile();
            
            if (result == true) {
                log.log(Level.FINER, "Writing to file successful. Recomputing hash.");
                fileHash = IFileInputOutput.computeHash(fileName);
                taskData.saveSuccessful();
            } 
            
            return result;
            
        } else {
            log.log(Level.FINER, "Write to file: TaskData has no unsaved changes. Do nothing.");
            return true;
        }
    }
    


    /**
     * @return tasks as read from file, in a TaskInfo[] array.
     * Returns null if there is an error reading.
     */
    private TaskInfo[] readTasksFromFile() {
        
        TaskInfo[] taskInfos = null;
        
        File file = new File(fileName);
        try {
            FileReader fileReader = new FileReader(file);
            IReaderWriter readerWriter = new JsonReaderWriter();
            taskInfos = readerWriter.readTasksFromJson(fileReader);
            
            fileReader.close();
            
        } catch (IOException e) {
            return null;
        }
        
        return taskInfos;
    }
    
    /**
     * @return true iff successful.
     */
    private boolean writeTasksToFile() {
        
        TaskInfo[] taskInfos = getTaskInfoArrayFromTaskData();
        
        File file = new File(fileName);
        boolean result = false;
        
        try {
            FileWriter fileWriter = new FileWriter(file);
            IReaderWriter readerWriter = new JsonReaderWriter();
            result = readerWriter.writeTasksToJson(fileWriter, taskInfos);
            
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        
        return result;
    }

    private TaskInfo[] getTaskInfoArrayFromTaskData() {
        int size = taskData.getSize();
        TaskInfo[] taskInfos = new TaskInfo[size];
        
        int index = 0;
        TaskId current = taskData.getFirst();
        
        while (current.isValid()) {
            taskInfos[index] = taskData.getTaskInfo(current);
            current = taskData.getNext(current);
            index++;
        }
        return taskInfos;
    }

    /**
     * The file is unchanged iff the file's hash matches the old hash.
     * @return true iff the file has not changed since the last save.
     */
    private boolean fileUnchanged() {
        String currentHash = IFileInputOutput.computeHash(fileName);
        return fileHash.equals(currentHash);
    }
}
