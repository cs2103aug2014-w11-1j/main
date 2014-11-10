package io;

import java.io.Reader;
import java.io.Writer;

import main.command.alias.AliasValuePair;
import data.taskinfo.TaskInfo;

//@author A0065475X
/**
 * A ReaderWriter class interface that is used by File IO to parse data from
 * a (file) Reader into objects, and to convert objects into a String format in
 * a (file) Writer to output to a file.<br>
 * <br>
 * Can parse TaskInfo objects and AliasValuePAir objects.
 */
public interface IReaderWriter {

    /**
     * @param writer Outputs the JSON string to this writer.
     * @param taskInfos an Array of AliasValuePair to be converted into JSON
     * format
     * @return true iff successful.
     */
    public abstract boolean writeAliasesToJson(Writer writer,
            AliasValuePair[] taskInfos);

    /**
     * @param reader A Reader to read a JSON string from.
     * @return an array of AliasValuePairs extracted from parsing the JSON data
     * in the reader.
     */
    public abstract AliasValuePair[] readAliasesFromJson(Reader reader);

    /**
     * @param taskInfos an Array of TaskInfo to be converted into a string.
     * @return a String containing all the data from the taskInfos array,
     * in JSON format, pretty printed.
     */
    public abstract String tasksToJsonString(TaskInfo[] taskInfos);

    /**
     * @param jsonString A string formatted in Json storing the TaskInfos
     * @return an array of TaskInfos extracted from jsonString
     * @throws InvalidFileFormatException
     * thrown when an error occurred during the parsing of the string.
     */
    public abstract TaskInfo[] jsonStringToTasks(String jsonString)
            throws InvalidFileFormatException;

    /**
     * @param writer Outputs the JSON string to this writer.
     * @param taskInfos an Array of TaskInfo to be converted into JSON format
     * @return true iff successful.
     */
    public abstract boolean writeTasksToJson(Writer writer, TaskInfo[] taskInfos);

    /**
     * @param reader A Reader to read a JSON string from.
     * @return an array of TaskInfos extracted from parsing the JSON data in
     * the reader.
     */
    public abstract TaskInfo[] readTasksFromJson(Reader reader);

}