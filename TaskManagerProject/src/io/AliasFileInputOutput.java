package io;

import java.util.logging.Logger;

import main.command.alias.IAliasStorage;
import taskline.TasklineLogger;

public class AliasFileInputOutput implements IFileInputOutput {
    private static final Logger log = TasklineLogger.getLogger();
    
    private IAliasStorage aliasStorage;
    private final String fileName;
    private String fileHash = "";

    public AliasFileInputOutput(IAliasStorage aliasStorage, String fileName) {
        this.fileName = fileName;
    }

    /* (non-Javadoc)
     * @see io.IFileInputOutput#read()
     */
    public boolean read() {
        if (fileUnchanged()) {
            return false;
        }
        
        

        return true;
    }

    /* (non-Javadoc)
     * @see io.IFileInputOutput#write()
     */
    @Override
    public boolean write() {
        // TODO Auto-generated method stub
        return false;
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
