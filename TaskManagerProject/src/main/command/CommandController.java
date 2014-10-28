package main.command;

import main.command.EditCommand.ParseType;
import main.command.alias.AliasController;
import main.command.alias.AliasStorage;
import main.command.alias.CommandString;
import manager.ManagerHolder;

/**
 * Handles the parsing of commands.
 * Commands are returned to MainController for execution.
 *
 * @author You Jun
 */
public class CommandController {
    private ManagerHolder managerHolder;
    private AliasController aliasController;

    public CommandController(ManagerHolder managerHolder, AliasStorage aliasStorage) {
        this.managerHolder = managerHolder;
        aliasController = new AliasController(aliasStorage);
    }

    public Command getCommand(String cmdTxt) {
        /*
        Scanner sc = new Scanner(cmdTxt);

        String cmdType = sc.next();
        String cmdArgs = "";
        if (sc.hasNextLine()) {
            cmdArgs = sc.nextLine().trim();
        }

        sc.close();
        */

        CommandString cmdString = aliasController.replaceAlias(cmdTxt);

        return getCommand(cmdString);
    }

    private Command getCommand(CommandString cmdString) {
        String cmdArgs = cmdString.getAlias();

        if (cmdString.getType() == null) {
            return new ArgumentCommand(cmdArgs, managerHolder);
        }

        switch (cmdString.getType()) {
            case ADD :
                return new AddCommand(cmdArgs, managerHolder);
            case SHOW :
                return new SearchCommand(cmdArgs, managerHolder);
            case EDIT :
                return new EditCommand(cmdArgs, managerHolder);
            case MARK :
                return new EditCommand(cmdArgs, managerHolder, ParseType.MARK);
            case UNMARK :
                return new EditCommand(cmdArgs, managerHolder, ParseType.UNMARK);
            case STATUS :
                return new EditCommand(cmdArgs, managerHolder, ParseType.STATUS);
            case RESCHEDULE :
                return new EditCommand(cmdArgs, managerHolder, ParseType.RESCHEDULE);
            case RENAME :
                return new EditCommand(cmdArgs, managerHolder, ParseType.RENAME);
            case DELETE :
                return new DeleteCommand(cmdArgs, managerHolder);
            case DETAILS :
                return new DetailsCommand(cmdArgs, managerHolder);
            case UNDO :
                return new UndoCommand(managerHolder);
            case REDO :
                return new RedoCommand(managerHolder);
            case BACK :
                return new BackCommand(managerHolder);
            case EXIT :
                return new ExitCommand(managerHolder);
            case FREEDAY :
                return new FreeDaySearchCommand(cmdArgs, managerHolder);
            case REPORT :
                return new ReportCommand(managerHolder);
            default :
                return new ArgumentCommand(cmdArgs, managerHolder);
        }

        /*
        switch (cmdType) {
            case "add" :
            case "create" :
                return new AddCommand(cmdArgs, managerHolder);
            case "show" :
            case "search" :
            case "ls" :
                return new SearchCommand(cmdArgs, managerHolder);
            case "edit" :
            case "set" :
            case "change" :
            case "modify" :
                return new EditCommand(cmdArgs, managerHolder);
            case "mark" :
                return new EditCommand(cmdArgs, managerHolder, ParseType.MARK);
            case "unmark" :
                return new EditCommand(cmdArgs, managerHolder, ParseType.UNMARK);
            case "status" :
                return new EditCommand(cmdArgs, managerHolder, ParseType.STATUS);
            case "reschedule" :
                return new EditCommand(cmdArgs, managerHolder, ParseType.RESCHEDULE);
            case "rename" :
                return new EditCommand(cmdArgs, managerHolder, ParseType.RENAME);
            case "del" :
            case "delete" :
            case "remove" :
            case "rm" :
                return new DeleteCommand(cmdArgs, managerHolder);
            case "detail" :
            case "details" :
                return new DetailsCommand(cmdArgs, managerHolder);
            case "undo" :
                return new UndoCommand(managerHolder);
            case "redo" :
                return new RedoCommand(managerHolder);
            case "back" :
            case "return" :
                return new BackCommand(managerHolder);
            case "exit" :
            case "quit" :
                return new ExitCommand(managerHolder);
            case "freeday" :
                return new FreeDaySearchCommand(cmdArgs, managerHolder);
            case "report" :
                return new ReportCommand(managerHolder);
            default :
                return new ArgumentCommand(cmdType + " " + cmdArgs, managerHolder);
        }
        */
    }
}
