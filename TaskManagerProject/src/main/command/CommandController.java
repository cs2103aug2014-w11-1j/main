package main.command;

import io.IFileInputOutput;

import java.util.function.BiFunction;

import main.command.alias.AliasController;
import main.command.alias.IAliasStorage;
import manager.ManagerHolder;

//@author A0065475X
/**
 * Handles the parsing of commands.
 * Commands are returned to MainController for execution.
 */
public class CommandController {
    private ManagerHolder managerHolder;
    private AliasController aliasController;

    public CommandController(ManagerHolder managerHolder,
            IAliasStorage aliasStorage, IFileInputOutput aliasFileInputOutput) {
        this.managerHolder = managerHolder;
        aliasController = new AliasController(aliasStorage, aliasFileInputOutput);
    }

    /**
     * Retrieves a command object given an input string.<br>
     * The default command bindings are stored in
     * AliasStorage.initialiseDefaultCommands()
     *
     * @param commandString the input string
     * @return a Command object that is ready to be executed.
     */
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
