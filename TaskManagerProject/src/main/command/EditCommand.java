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
import data.taskinfo.TaskInfo;

public class EditCommand implements Command {
    private final EditManager editManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final TaskInfo taskToEdit;
    private int taskId;

    public EditCommand(String args, ManagerHolder managerHolder) {
        taskToEdit = parse(args);
        editManager = managerHolder.getEditManager();
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
    }

    private TaskInfo parse(String args) {
        TaskInfo editTask = new TaskInfo();
        Scanner sc = new Scanner(args);

        taskId = sc.nextInt();
        String paramType = sc.next();
        String paramNew = sc.nextLine();
        switch (paramType.toLowerCase()) {
            case "name" :
                CommandParser.parseName(paramNew, editTask);
                break;
            case "date" :
                // TODO modify date somehow
                break;
            case "time" :
                // TODO modify time somehow
                break;
            case "tag" :
                CommandParser.parseTags("#" + paramNew, editTask);
                String changeType = sc.next();
                if (changeType.toLowerCase().equals("add")) {
                    // do something
                } else {
                    // do something else
                }
                break;
            case "priority" :
                CommandParser.parsePriority("+" + paramNew, editTask);
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

            Result result = editManager.editTask(taskToEdit, null);
            Response response = stateManager.update(result);
            return response;
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }

}
