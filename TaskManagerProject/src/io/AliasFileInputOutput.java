package io;

import io.json.JsonReaderWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import main.command.alias.AliasValuePair;
import main.command.alias.IAliasStorageFileInputOutput;
import taskline.TasklineLogger;
import data.AutoCompleteDictionary;

public class AliasFileInputOutput implements IFileInputOutput {
    private static final Logger log = TasklineLogger.getLogger();
    
    private final AutoCompleteDictionary autoCompleteDictionary;
    private final IAliasStorageFileInputOutput aliasStorage;
    private final String fileName;
    private String fileHash = "";

    public AliasFileInputOutput(IAliasStorageFileInputOutput aliasStorage,
            String fileName, AutoCompleteDictionary autoCompleteDictionary) {
        this.aliasStorage = aliasStorage;
        this.fileName = fileName;
        this.autoCompleteDictionary = autoCompleteDictionary;
    }

    /* (non-Javadoc)
     * @see io.IFileInputOutput#read()
     */
    public boolean read() {
        if (fileUnchanged()) {
            return false;
        }
        AliasValuePair[] aliases = readAliasesFromFile();
        
        if (aliases == null) {
            return false;
        } else {
            aliasStorage.setAllCustomAliases(aliases);
            updateAutoCompleteDictionary();
            return true;
        }
    }

    /* (non-Javadoc)
     * @see io.IFileInputOutput#write()
     */
    @Override
    public boolean write() {
        boolean result = writeAliasesToFile();
        
        if (result == true) {
            updateAutoCompleteDictionary();
            fileHash = IFileInputOutput.computeHash(fileName);
        }
        
        return result;
    }


    /**
     * @return tasks as read from file, in a TaskInfo[] array.
     * Returns null if there is an error reading.
     */
    private AliasValuePair[] readAliasesFromFile() {
        
        AliasValuePair[] aliases = null;
        
        File file = new File(fileName);
        try {
            FileReader fileReader = new FileReader(file);
            IReaderWriter readerWriter = new JsonReaderWriter();
            aliases = readerWriter.readAliasesFromJson(fileReader);
            
            fileReader.close();
            
        } catch (IOException e) {
            return null;
        }
        
        return aliases;
    }
    
    /**
     * @return true iff successful.
     */
    private boolean writeAliasesToFile() {
        
        AliasValuePair[] aliases = getAliasArrayFromStorage();
        
        File file = new File(fileName);
        boolean result = false;
        
        try {
            FileWriter fileWriter = new FileWriter(file);
            IReaderWriter readerWriter = new JsonReaderWriter();
            result = readerWriter.writeAliasesToJson(fileWriter, aliases);
            
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        
        return result;
    }
    
    
    private AliasValuePair[] getAliasArrayFromStorage() {
        return aliasStorage.getAllCustomAliases();
    }

    /**
     * The file is unchanged iff the file's hash matches the old hash.
     * @return true iff the file has not changed since the last save.
     */
    private boolean fileUnchanged() {
        String currentHash = IFileInputOutput.computeHash(fileName);
        return fileHash.equals(currentHash);
    }
    
    private void updateAutoCompleteDictionary() {
        String[] allBindedStrings = aliasStorage.getAllBindedStrings();
        autoCompleteDictionary.refreshDictionary(allBindedStrings);
    }
    
}
