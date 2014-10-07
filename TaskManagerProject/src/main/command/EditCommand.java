package main.command;

import java.util.NoSuchElementException;
import java.util.Scanner;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.EditManager;
import manager.datamanager.SearchManager;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditCommand implements Command {
    private static final int TAG_ADD = 1;
    private static final int TAG_DEL = -1;

    private final EditManager editManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final TaskId taskId;
    private final TaskInfo taskToEdit;
    private int tagOperation = 0;

    public EditCommand(String args, ManagerHolder managerHolder)
            throws NoSuchElementException {
        editManager = managerHolder.getEditManager();
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();

        // check if in edit mode
        if (stateManager.inEditMode()) {
            // edit mode
            taskId = editManager.getEditingTask();
            taskToEdit = parseEditParams(args);
        } else {
            // not edit mode
            Scanner sc = new Scanner(args);
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
            sc.close();
        }
    }

    private TaskId parseTaskId(String args) {
        // should return null if in edit mode
        assert args != null : "There should not be a null passed in.";
        if (args.isEmpty()) {
            return null;
        }

        try {
            int relativeTaskId = Integer.parseInt(args);
            if (stateManager.inSearchMode()) {
                return searchManager.getAbsoluteIndex(relativeTaskId);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            return TaskId.makeTaskId(absoluteTaskId);
        } catch (IndexOutOfBoundsException e) {
            return null;
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
        String editParam = sc.nextLine().trim();

        TaskInfo editTask = TaskInfo.createEmpty();

        switch (editType.toLowerCase()) {
            case "name" :
                editTask.name = CommandParser.parseName(editParam);
                break;
            case "date" :
                // TODO modify date somehow
                break;
            case "time" :
                // TODO modify time somehow
                break;
            case "tag" :
                editTask.tags = CommandParser.parseTags("#" + editParam);
                if (sc.hasNext()) {
                    String changeType = sc.next();
                    if (changeType.toLowerCase().equals("add")) {
                        tagOperation = TAG_ADD;
                    }
                    if (changeType.toLowerCase().equals("del")){
                        tagOperation = TAG_DEL;
                    }
                }
                break;
            case "priority" :
                editTask.priority = CommandParser.parsePriority("+" + editParam);
                break;
            default :
                // invalid edit type, throw invalid edit field exception?
        }

        sc.close();

        return editTask;
    }

    @Override
    public Response execute() {
        if (stateManager.canEdit()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result;
            if (taskId != null) {
                if (taskToEdit == null) {
                    result = editManager.startEditMode(taskId);
                } else {
                    if (tagOperation == TAG_ADD) {
                        result = editManager.addTaskTag(taskToEdit.tags[0], taskId);
                    } else if (tagOperation == TAG_DEL) {
                        result = editManager.deleteTaskTag(taskToEdit.tags[0], taskId);
                    } else {
                        result = editManager.editTask(taskToEdit, taskId);
                    }
                }
            } else {
                result = new SimpleResult(Result.Type.INVALID_ARGUMENT);
            }
            Response response = stateManager.update(result);
            return response;
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }

    public TaskId convertStringtoTaskId(String stringId){
    	return TaskId.makeTaskId(stringId);
    }

}
