package main.command;

import java.util.NoSuchElementException;
import java.util.Scanner;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.EditManager;
import manager.result.Result;
import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.TaskInfo;

public class EditCommand extends TargetedCommand {
    private static final int TAG_ADD = 1;
    private static final int TAG_DEL = -1;

    private final EditManager editManager;
    private final StateManager stateManager;
    //private final TaskId taskId;
    private final TaskInfo taskToEdit;
    private int tagOperation = 0;

    public EditCommand(String args, ManagerHolder managerHolder)
            throws NoSuchElementException {
        super(managerHolder);
        editManager = managerHolder.getEditManager();
        stateManager = managerHolder.getStateManager();

        // check if in edit mode
        if (stateManager.inEditMode()) {
            // edit mode
            targetTaskIdSet = editManager.getEditingTasks();
            taskToEdit = parseEditParams(args);
        } else {
            // not edit mode
            args = tryParseIdsIntoSet(args);
            taskToEdit = parseEditParams(args);
        }
    }

    private TaskInfo parseEditParams(String args) {
        assert args != null : "There should not be a null passed in.";

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
            case "datetime" :
                editParam = sc.nextLine().trim();
                parseDateTimes(editParam, editTask);
                break;
            case "tag" :
                String changeType = sc.next();
                if (sc.hasNext()) {
                    if (changeType.toLowerCase().equals("add")) {
                        tagOperation = TAG_ADD;
                    }
                    if (changeType.toLowerCase().equals("del")){
                        tagOperation = TAG_DEL;
                    }
                    if (tagOperation == TAG_ADD || tagOperation == TAG_DEL) {
                        editParam = sc.next();
                        editTask.tags = CommandParser.parseTags("#" + editParam);
                    }
                }
                break;
            case "priority" :
                editParam = sc.nextLine().trim();
                Priority p = CommandParser.parsePriority("+" + editParam);
                if (p != null) {
                    editTask.priority = p;
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

    public TaskId convertStringtoTaskId(String stringId){
    	return TaskId.makeTaskId(stringId);
    }

    @Override
    protected boolean isValidArguments() {
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
