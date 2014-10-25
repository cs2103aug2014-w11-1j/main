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

public class EditCommand extends TargetedCommand {
    private static final String ARGUMENT_DATETIME = "datetime";
    private static final String ARGUMENT_STATUS = "status";
    
    private static final int TAG_ADD = 1;
    private static final int TAG_DEL = -1;

    private final EditManager editManager;
    private TaskInfo taskToEdit;
    private int tagOperation = 0;
    
    private final ParseType parseType;
    
    public enum ParseType {
        NORMAL,
        MARK,
        UNMARK,
        STATUS,
        RESCHEDULE
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
    
    /*protected TaskInfo readStatusIntoTaskInfo(String args, TaskInfo taskInfo) {
        try {
            taskInfo.status = Status.valueOf(args.toUpperCase());
        } catch (IllegalArgumentException e) {
            taskInfo = null;
        }
        return taskInfo;
    }*/

    private TaskInfo parseStatusParams(String args) {
        return parseEditParams(ARGUMENT_STATUS + " " + args);
    }
    
    private TaskInfo parseRescheduleParams(String args) {
        return parseEditParams(ARGUMENT_DATETIME + " " + args);
    }

    private TaskInfo parseEditParams(String args) {
        Scanner sc = new Scanner(args);
        if (!sc.hasNext()) {
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
            case "name" :
                editParam = sc.nextLine().trim();
                editTask.name = editParam;
                break;
            case "details" :
            case "description" :
                editParam = sc.nextLine().trim();
                editTask.details = CommandParser.parseName(editParam);
                break;
            case "date" :
            case "time" :
            case ARGUMENT_DATETIME :
                editParam = sc.nextLine().trim();
                parseDateTimes(editParam, editTask);
                break;
            case "tag" :
                editParam = sc.nextLine().trim();
                parseTags(editParam, editTask);
                break;
            case "priority" :
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
            default :
                editTask = null;
        }

        sc.close();

        return editTask;
    }

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
            editTask.endTime = dtPair.getFirstTime();

            // handle the case where one value has to be filled in
            if (!dtPair.hasSecondDate()) {
                editTask.endDate = editTask.startDate;
            }
            if (!dtPair.hasSecondTime()) {
                editTask.endTime = editTask.startTime;
            }
        }
    }

    private void parseTags(String editParam, TaskInfo editTask) {
        if (editParam.isEmpty()) {
            return;
        }

        Scanner sc = new Scanner(editParam);
        String changeType = sc.next();

        // ensure it still has a tag to add / delete
        if (sc.hasNext()) {
            if (changeType.toLowerCase().equals("add")) {
                tagOperation = TAG_ADD;
            }
            if (changeType.toLowerCase().equals("del")){
                tagOperation = TAG_DEL;
            }
        }

        // ensure it is adding / deleting tags
        if (tagOperation == TAG_ADD || tagOperation == TAG_DEL) {
            StringBuilder tags = new StringBuilder();
            while (sc.hasNext()) {
                String tag = sc.next();
                tags.append("#").append(tag).append(" ");
            }
            editTask.tags = CommandParser.parseTags(tags.toString());
        }

        sc.close();
    }

    public TaskId convertStringtoTaskId(String stringId){
    	return TaskId.makeTaskId(stringId);
    }

    @Override
    protected boolean isValidArguments() {
        if (taskToEdit == null && parseType != ParseType.NORMAL) {
            return false;
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
        if (taskToEdit == null) {
            result = editManager.startEditMode(targetTaskIdSet);
        } else {
            if (tagOperation == TAG_ADD) {
                result = editManager.addTaskTags(taskToEdit.tags, targetTaskIdSet);
            } else if (tagOperation == TAG_DEL) {
                result = editManager.deleteTaskTags(taskToEdit.tags, targetTaskIdSet);
            } else {
                result = editManager.editTask(taskToEdit, targetTaskIdSet);
            }
        }
        return result;
    }

}
