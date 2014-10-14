package io;

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

import taskline.debug.Taskline;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class FileInputOutput {
    private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);
    
    private final String fileName;
    private String fileHash = "";
    
    private final TaskData taskData;
    
    public FileInputOutput(TaskData taskData, String fileName) {
        this.fileName = fileName;
        this.taskData = taskData;
    }
    
    /**
     * @return true iff there is a change in the file.
     */
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

    /**
     * @return true for success.
     */
    public boolean write() {
        if (taskData.hasUnsavedChanges()) {
            log.log(Level.FINER, "Write to file: TaskData has unsaved changes. Writing...");
            boolean result = writeTasksToFile();
            
            if (result == true) {
                log.log(Level.FINER, "Writing to file successful. Recomputing hash.");
                fileHash = computeHash(fileName);
                taskData.saveSuccessful();
            } 
            
            return result;
            
        } else {
            log.log(Level.FINER, "Write to file: TaskData has no unsaved changes. Do nothing.");
            return true;
        }
    }
    
    
    public static String computeHash(String nameOfFile) {
        try {
            MessageDigest md5er = generateMessageDigest(nameOfFile);

            if (md5er == null) {
                return null;
            }
            
            byte[] digest = md5er.digest();
            
            if (digest == null) {
                return null;
            }
            
            StringBuilder result = new StringBuilder();
            
            for (int i = 0; i < digest.length; i++) {
                result.append(byteToHex(digest[i]));
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return null;
        }
    }

    private static String byteToHex(byte b) {
        return Integer.toString((b & 0xff) + 0x100, 16).substring(1);
    }

    private static MessageDigest generateMessageDigest(String nameOfFile) {
        MessageDigest md5er;
        
        try {
            InputStream fin = new FileInputStream(nameOfFile);
            md5er = MessageDigest.getInstance("MD5");
            
            byte[] buffer = new byte[1024];
            
            int read = 0;
            while (read != -1) {
                read = fin.read(buffer);
                if (read > 0) {
                    md5er.update(buffer, 0, read);
                }
            }
            
            fin.close();
            
        } catch (IOException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return md5er;
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
            taskInfos = JsonReaderWriter.readTasksFromJson(fileReader);
            
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
            result = JsonReaderWriter.writeTasksToJson(fileWriter, taskInfos);
            
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
        String currentHash = computeHash(fileName);
        return fileHash.equals(currentHash);
    }
}
