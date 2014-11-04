package io.json;

import io.IReaderWriter;
import io.InvalidFileFormatException;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.command.alias.AliasValuePair;
import taskline.TasklineLogger;
import data.taskinfo.TaskInfo;

//@author A0065475X
public class JsonReaderWriter implements IReaderWriter {
    private static final Logger log = TasklineLogger.getLogger();

    static final String ERROR_WRONG_EVENT = "Wrong event: ";
    static final String ERROR_UNKNOWN_ELEMENT = "Unknown element: ";
    

    /* (non-Javadoc)
     * @see io.IReaderWriter#writeAliasesToJson(java.io.Writer, main.command.alias.AliasValuePair[])
     */
    @Override
    public boolean writeAliasesToJson(Writer writer, AliasValuePair[] aliases) {
        JsonAliasParser.aliasesToJson(writer, aliases);
        return true;
    }

    /* (non-Javadoc)
     * @see io.IReaderWriter#readAliasesFromJson(java.io.Reader)
     */
    @Override
    public AliasValuePair[] readAliasesFromJson(Reader reader) {
        AliasValuePair[] aliases = null;
        try {
            aliases = JsonAliasParser.jsonToAliases(reader);
            
        } catch (InvalidFileFormatException e) {
            log.log(Level.SEVERE, "Invalid File Format: " + e.getMessage());
            return null;
        }
        return aliases;
    }
    
    /* (non-Javadoc)
     * @see io.IReaderWriter#tasksToJsonString(data.taskinfo.TaskInfo[])
     */
    @Override
    public String tasksToJsonString(TaskInfo[] taskInfos) {
        StringWriter stringWriter = new StringWriter();
        JsonTaskInfoParser.tasksToJson(stringWriter, taskInfos);
        
        return stringWriter.toString();
    }

    /* (non-Javadoc)
     * @see io.IReaderWriter#jsonStringToTasks(java.lang.String)
     */
    @Override
    public TaskInfo[] jsonStringToTasks(String jsonString)
            throws InvalidFileFormatException {
        
        StringReader stringReader = new StringReader(jsonString);
        return JsonTaskInfoParser.jsonToTasks(stringReader);
    }

    /* (non-Javadoc)
     * @see io.IReaderWriter#writeTasksToJson(java.io.Writer, data.taskinfo.TaskInfo[])
     */
    @Override
    public boolean writeTasksToJson(Writer writer, TaskInfo[] taskInfos) {
        JsonTaskInfoParser.tasksToJson(writer, taskInfos);
        return true;
    }

    /* (non-Javadoc)
     * @see io.IReaderWriter#readTasksFromJson(java.io.Reader)
     */
    @Override
    public TaskInfo[] readTasksFromJson(Reader reader) {
        TaskInfo[] taskInfos = null;
        try {
            taskInfos = JsonTaskInfoParser.jsonToTasks(reader);
            
        } catch (InvalidFileFormatException e) {
            return null;
        }
        return taskInfos;
    }
    
}
