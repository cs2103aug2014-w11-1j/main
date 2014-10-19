package main.command;

import java.util.NoSuchElementException;
import java.util.Scanner;

import main.command.parser.CommandParser;
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

            /*Scanner sc = new Scanner(args);
            if (sc.hasNext()) {
                taskId = parseTaskId(sc.next());
                if (sc.hasNext()) {
                    taskToEdit = parseEditParams(sc.nextLine());
                } else {
                    taskToEdit = null;
                }
            } else {
                taskId = null;
                taskToEdit = null;
            }
            sc.close();*/
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
                editParam = sc.nextLine().trim();
                CommandParser.parseDateTime(editParam, editTask);
                // TODO modify date somehow
                break;
            case "time" :
                editParam = sc.nextLine().trim();
                CommandParser.parseDateTime(editParam, editTask);
                // TODO modify time somehow
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
                result = editManager.addTaskTag(taskToEdit.tags[0], targetTaskIdSet);
            } else if (tagOperation == TAG_DEL) {
                result = editManager.deleteTaskTag(taskToEdit.tags[0], targetTaskIdSet);
            } else {
                result = editManager.editTask(taskToEdit, targetTaskIdSet);
            }
        }
        return result;
    }

}
