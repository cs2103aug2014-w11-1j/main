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
        
        String[] split = commandString.split(" ", 2);
        String commandType = extractFirstToken(split);
        String arguments = extractSecondToken(split);

        BiFunction<String, ManagerHolder, Command> makeCommand;
        makeCommand = aliasController.getReservedCommand(commandType);
        
        Command command = makeCommand.apply(arguments, managerHolder);
        
        return command;
    }

    protected String extractSecondToken(String[] split) {
        String arguments = "";
        if (split.length >= 2) {
            assert split.length == 2;
            arguments = split[1];
        }
        return arguments;
    }

    protected String extractFirstToken(String[] split) {
        String commandType = "";
        if (split.length >= 1) {
            commandType = split[0];
        }
        return commandType;
    }
}
