package main.command;

import java.util.Scanner;

import manager.ManagerHolder;

/**
 * Handles the parsing of commands.
 * Commands are returned to MainController for execution.
 *
 * @author You Jun
 */
public class CommandController {
    private ManagerHolder managerHolder;

    public CommandController(ManagerHolder managerHolder) {
        this.managerHolder = managerHolder;
    }

    public Command getCommand(String cmdTxt) {
        Scanner sc = new Scanner(cmdTxt);

        String cmdType = sc.next();
        String cmdArgs = sc.nextLine().trim();

        sc.close();

        return getCommand(cmdType, cmdArgs);
    }

    private Command getCommand(String cmdType, String cmdArgs) {
        switch (cmdType) {
            case "add":
                return new AddCommand(cmdArgs, managerHolder);
            case "show":
                //return new SearchCommand(cmdArgs);
            case "edit":
                //return new EditCommand(cmdArgs);
            case "del":
                //return new DeleteCommand(cmdArgs);
            case "undo":
                return new UndoCommand(managerHolder);
        }
        return null;
    }
}
