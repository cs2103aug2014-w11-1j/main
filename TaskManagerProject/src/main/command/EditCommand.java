package main.command;

import java.util.Scanner;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.EditManager;
import manager.datamanager.SearchManager;
import manager.result.Result;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditCommand implements Command {
    private final EditManager editManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final TaskInfo taskToEdit;
    private final TaskId taskId;

    public EditCommand(String args, ManagerHolder managerHolder) {
        editManager = managerHolder.getEditManager();
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
        
        Scanner sc = new Scanner(args);
        taskId = parseTaskId(sc.next());
        taskToEdit = parseEditParams(sc.nextLine());
        sc.close();
    }

    private TaskId parseTaskId(String args) {
        try {
            int relativeTaskId = Integer.parseInt(args);
            return searchManager.getAbsoluteIndex(relativeTaskId);
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            return TaskId.makeTaskId(absoluteTaskId);
        }
    }

    private TaskInfo parseEditParams(String args) {
        Scanner sc = new Scanner(args);
        String editType = sc.next();
        String editParam = sc.nextLine();
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
                String changeType = sc.next();
                if (changeType.toLowerCase().equals("add")) {
                    // do something
                } else {
                    // do something else
                }
                break;
            case "priority" :
                editTask.priority = CommandParser.parsePriority("+" + editParam);
                break;
            default :
                // something
        }

        sc.close();

        return editTask;
    }

    @Override
    public Response execute() {
        if (stateManager.canEdit()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result;
            if (taskToEdit == null) {
                result = editManager.startEditMode(taskId);
            } else {
                result = editManager.editTask(taskToEdit, taskId);
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
