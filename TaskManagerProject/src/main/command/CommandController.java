package main.command;

import java.util.function.BiFunction;

import main.command.alias.AliasController;
import main.command.alias.AliasStorage;
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

    public Command getCommand(String commandString) {

        commandString = aliasController.replaceAlias(commandString);

        BiFunction<String, ManagerHolder, Command> makeCommand;
        
        String[] split = commandString.split(" ", 2);
        
        String commandType = "";
        if (split.length >= 1) {
            commandType = split[0];
        }
        makeCommand = aliasController.getReservedCommand(commandType);
        
        String arguments = "";
        if (split.length >= 2) {
            assert split.length == 2;
            arguments = split[1];
        }

        Command command = makeCommand.apply(arguments, managerHolder);
        return command;
    }
}
