package main.command;

import java.util.NoSuchElementException;
import java.util.Scanner;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
import manager.datamanager.EditManager;
import manager.result.Result;
import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.TaskInfo;


//@author A0065475X
public class EditCommand extends TargetedCommand {
    private static final String ARGUMENT_CLEAR = "clear";
    private static final String ARGUMENT_DESCRIPTION = "details";
    private static final String ARGUMENT_DESCRIPTION_2 = "description";
    private static final String ARGUMENT_DATE = "date";
    private static final String ARGUMENT_TIME = "time";
    private static final String ARGUMENT_TAG = "tag";
    private static final String ARGUMENT_TAG_2 = "tags";
    private static final String ARGUMENT_PRIORITY = "priority";
    private static final String ARGUMENT_NAME = "name";
    private static final String ARGUMENT_DATETIME = "datetime";
    private static final String ARGUMENT_STATUS = "status";
    
    private final EditManager editManager;
    private TaskInfo taskToEdit;
    private Operation specialOperation = Operation.NONE;
    private Info infoToClear = null;
    private final ParseType parseType;
    
    private enum Operation {
        NONE,
        EDIT_MODE,
        TAG_ADD,
        TAG_DELETE,
        CLEAR_INFO
    }
    
    public enum Info {
        DATE,
        TIME,
        DATETIME,
        PRIORITY,
        STATUS,
        DESCRIPTION,
        TAGS
    }
    
    public enum ParseType {
        NORMAL,
        MARK,
        UNMARK,
        STATUS,
        RESCHEDULE,
        RENAME
    }

    public EditCommand(String args, ManagerHolder managerHolder)
            throws NoSuchElementException {
        super(managerHolder);
        editManager = managerHolder.getEditManager();

        this.parseType = ParseType.NORMAL;
        parse(args);
    }

    
    public EditCommand(String args, ManagerHolder managerHolder, ParseType parseType) {
        super(managerHolder);
        editManager = managerHolder.getEditManager();

        this.parseType = parseType;
        parse(args);
    }

    private void parse(String args) {
        if (stateManager.inEditMode()) {
            // edit mode
            targetTaskIdSet = editManager.getEditingTasks();
            taskToEdit = parseCommandParams(args);
        } else {
            // not edit mode
            args = tryParseIdsIntoSet(args);
            if (targetTaskIdSet == null) {
                taskToEdit = parseKeywordAndEditParams(args);
            } else {
                taskToEdit = parseCommandParams(args);
            }
        }
    }

    private TaskInfo parseKeywordAndEditParams(String args) {
        StringBuilder keywords = new StringBuilder();

        TaskInfo taskInfo = parseCommandParams(args);
        while (!args.isEmpty() && (taskInfo == null || keywords.length() == 0)) {
            String[] split = args.split(" ", 2);
            keywords.append(split[0]).append(" ");
            
            if (split.length > 1) {
                args = split[1];
                taskInfo = parseCommandParams(args);
            } else {
                args = "";
            }
        }

        taskInfo = parseCommandParams(args);
        parseAsSearchString(keywords.toString());
        return taskInfo;
    }


    private TaskInfo parseCommandParams(String args) {
        assert args != null : "There should not be a null passed in.";

        switch (parseType) {
            case NORMAL :
                return parseEditParams(args);
            case MARK :
                return parseMarkParams(args);
            case UNMARK :
                return parseUnmarkParams(args);
            case STATUS :
                return parseStatusParams(args);
            case RESCHEDULE :
                return parseRescheduleParams(args);
            case RENAME :
                return parseRenameParams(args);
            default :
                throw new UnsupportedOperationException("Unknown Parse Type: " +
                        parseType.name());
        }
    }
    
    private TaskInfo parseMarkParams(String args) {
        TaskInfo taskInfo = TaskInfo.createEmpty();
        
        args = args.trim();
        if (args.length() == 0) {
            taskInfo.status = Status.DONE;
            return taskInfo;
        }

        return parseEditParams(ARGUMENT_STATUS + " " + args);
    }
    
    private TaskInfo parseUnmarkParams(String args) {
        TaskInfo taskInfo = TaskInfo.createEmpty();
        
        args = args.trim();
        if (args.length() == 0) {
            taskInfo.status = Status.UNDONE;
            return taskInfo;
        }
        return null;
    }
    
    private TaskInfo parseStatusParams(String args) {
        return parseEditParams(ARGUMENT_STATUS + " " + args);
    }
    
    private TaskInfo parseRescheduleParams(String args) {
        return parseEditParams(ARGUMENT_DATETIME + " " + args);
    }

    private TaskInfo parseRenameParams(String args) {
        return parseEditParams(ARGUMENT_NAME + " " + args);
    }

    //@author A0111862M
    private TaskInfo parseEditParams(String args) {
        Scanner sc = new Scanner(args);
        if (!sc.hasNext()) {
            tryChangeToStartEditModeCommand();
            sc.close();
            return null;
        }
        String editType = sc.next();
        if (!sc.hasNext()) {
            sc.close();
            return null;
        }
        String editParam = "";

        TaskInfo editTask = TaskInfo.createEmpty();

        switch (editType.toLowerCase()) {
            case ARGUMENT_NAME :
                editParam = CommandParser.parseName(sc.nextLine());
                editTask.name = editParam;
                break;
            case ARGUMENT_DESCRIPTION :
            case ARGUMENT_DESCRIPTION_2 :
                editParam = sc.nextLine().trim();
                editTask.details = CommandParser.parseName(editParam);
                break;
            case ARGUMENT_DATE :
            case ARGUMENT_TIME :
            case ARGUMENT_DATETIME :
                editParam = sc.nextLine().trim();
                parseDateTimes(editParam, editTask);
                break;
            case ARGUMENT_TAG :
            case ARGUMENT_TAG_2 :
                editParam = sc.nextLine().trim();
                parseTags(editParam, editTask);
                break;
            case ARGUMENT_PRIORITY :
                editParam = sc.nextLine().trim();
                Priority p = CommandParser.parsePriority("+" + editParam);
                if (p != null) {
                    editTask.priority = p;
                } else {
                    editTask = null;
                }
                break;
            case ARGUMENT_STATUS :
                editParam = sc.nextLine().trim();
                Status s = CommandParser.parseStatus(editParam);
                if (s != null) {
                    editTask.status = s;
                } else {
                    editTask = null;
                }
                break;
            case ARGUMENT_CLEAR :
                editParam = sc.nextLine().trim();
                if (clearInfo(editParam)) {
                    setSpecialOperation(Operation.CLEAR_INFO);
                    editTask = TaskInfo.createEmpty();
                } else {
                    editTask = null;
                }
                break;
            default :
                editTask = null;
        }

        sc.close();

        return editTask;
    }


    //@author A0065475X
    protected void tryChangeToStartEditModeCommand() {
        if (parseType == ParseType.NORMAL && taskToEdit == null) {
            setSpecialOperation(Operation.EDIT_MODE);
        }
    }

    //@author A0111862M
    private void parseDateTimes(String editParam, TaskInfo editTask) {
        DateTimePair dtPair = CommandParser.parseDateTimes(editParam);
        if (dtPair.isEmpty()) {
            return;
        }

        // either a single date and/or time
        if (!dtPair.hasSecondDate() && !dtPair.hasSecondTime()) {
            if (dtPair.hasFirstDate()) {
                editTask.endDate = dtPair.getFirstDate();
            }
            if (dtPair.hasFirstTime()) {
                editTask.endTime = dtPair.getFirstTime();
            }
        } else {
            // set everything first
            editTask.startDate = dtPair.getFirstDate();
            editTask.startTime = dtPair.getFirstTime();
            editTask.endDate = dtPair.getSecondDate();
            editTask.endTime = dtPair.getSecondTime();

            // handle the case where one value has to be filled in
            if (!dtPair.hasSecondDate()) {
                editTask.endDate = editTask.startDate;
            }
            if (!dtPair.hasSecondTime()) {
                editTask.endTime = editTask.startTime;
            }
        }
    }

    //@author A0111862M
    private void parseTags(String editParam, TaskInfo editTask) {
        if (editParam.isEmpty()) {
            return;
        }

        Scanner sc = new Scanner(editParam);
        String changeType = sc.next();

        // ensure it still has a tag to add / delete
        if (sc.hasNext()) {
            String changeTypeLower = changeType.toLowerCase();
            
            if (changeTypeLower.equals("add")) {
                setSpecialOperation(Operation.TAG_ADD);
            }
            if (changeTypeLower.equals("del") ||
                    changeTypeLower.equals("delete")){
                setSpecialOperation(Operation.TAG_DELETE);
            }
        }

        // ensure it is adding / deleting tags
        if (specialOperation == Operation.TAG_ADD ||
                specialOperation == Operation.TAG_DELETE) {
            StringBuilder tags = new StringBuilder();
            while (sc.hasNext()) {
                String tag = sc.next();
                tags.append("#").append(tag).append(" ");
            }
            editTask.tags = CommandParser.parseTags(tags.toString());
        }

        sc.close();
    }
    
    //@author A0065475X
    private boolean clearInfo(String info) {
        info = info.trim();
        switch(info) {
            case ARGUMENT_DESCRIPTION :
            case ARGUMENT_DESCRIPTION_2 :
                infoToClear = Info.DESCRIPTION;
                break;
            case ARGUMENT_DATE :
                infoToClear = Info.DATE;
                break;
            case ARGUMENT_TIME :
                infoToClear = Info.TIME;
                break;
            case ARGUMENT_DATETIME :
                infoToClear = Info.DATETIME;
                break;
            case ARGUMENT_TAG :
            case ARGUMENT_TAG_2 :
                infoToClear = Info.TAGS;
                break;
            case ARGUMENT_PRIORITY :
                infoToClear = Info.PRIORITY;
                break;
            case ARGUMENT_STATUS :
                infoToClear = Info.STATUS;
                break;
        }
        
        return (infoToClear != null);
    }
    
    private void setSpecialOperation(Operation operation) {
        specialOperation = operation;
    }

    public TaskId convertStringtoTaskId(String stringId){
    	return TaskId.makeTaskId(stringId);
    }

    @Override
    protected boolean isValidArguments() {
        if (specialOperation != Operation.EDIT_MODE &&
                specialOperation != Operation.CLEAR_INFO) {
            
            if (taskToEdit == null || taskToEdit.isEmpty()) {
                return false;
            }
        }
        return targetTaskIdSet != null;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canEdit();
    }

    @Override
    protected Result executeAction() {
        Result result;
        
        switch (specialOperation) {
            case EDIT_MODE :
                assert taskToEdit == null;
                result = editManager.startEditMode(targetTaskIdSet);
                break;
            case CLEAR_INFO :
                assert taskToEdit.isEmpty();
                assert infoToClear != null;
                result = editManager.clearInfo(targetTaskIdSet, infoToClear);
                break;
            case NONE :
                assert taskToEdit != null;
                result = editManager.editTask(taskToEdit, targetTaskIdSet);
                break;
            case TAG_ADD :
                assert taskToEdit != null;
                result = editManager.addTaskTags(taskToEdit.tags, targetTaskIdSet);
                break;
            case TAG_DELETE :
                assert taskToEdit != null;
                result = editManager.deleteTaskTags(taskToEdit.tags, targetTaskIdSet);
                break;
            default :
                throw new UnsupportedOperationException("Invalid operation: " +
                        specialOperation.name());
        }
        
        return result;
    }

}
