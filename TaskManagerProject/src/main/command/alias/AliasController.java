package main.command.alias;

import io.IFileInputOutput;

import java.util.function.BiFunction;

import main.command.Command;
import manager.ManagerHolder;


//@author A0111862M
public class AliasController {
    private static final String SYMBOL_DELIM = " ";

    private final IAliasStorage aliasStorage;
    private final IFileInputOutput aliasFileInputOutput;

    public AliasController(IAliasStorage aliasStorage,
            IFileInputOutput aliasFileInputOutput) {
        this.aliasStorage = aliasStorage;
        this.aliasFileInputOutput = aliasFileInputOutput;
    }

    //@author A0111862M
    public String replaceAlias(String commandString) {
        commandString = cleanCmdString(commandString);
        beforeAliasCheck();

        if (commandString.isEmpty()) {
            return commandString;
        }

        commandString = tryReplaceWithCustom(commandString);
        return commandString;
    }

    //@author A0111862M
    private void beforeAliasCheck() {
        aliasFileInputOutput.read();
    }

    //@author A0065475X
    private String tryReplaceWithCustom(String command) {
        String[] split = command.split(" ", 2);
        assert split.length >= 1;

        String replacement = getCustomAlias(split[0]);
        if (replacement == null) {
            return command;
        }

        String argument;

        if (split.length <= 1) {
            argument = "";
        } else {
            assert split.length == 2;
            argument = split[1];
        }

        replacement = replacement.replace(AliasStorage.VARIABLE_STRING,
                argument);
        return replacement;
    }

    //@author A0065475X
    public BiFunction<String, ManagerHolder, Command> getReservedCommand(
            String cmdString) {
        return aliasStorage.getDefaultCommand(cmdString.toLowerCase());
    }

    //@author A0065475X
    public String getCustomAlias(String possibleAlias) {
        return aliasStorage.getCustomCommand(possibleAlias.toLowerCase());
    }

    //@author A0111862M
    private static String cleanCmdString(String cmdString) {
        return stripExtraDelims(cmdString).trim();
    }

    //@author A0111862M
    private static String stripExtraDelims(String s) {
        String doubleDelim = SYMBOL_DELIM + SYMBOL_DELIM;
        while (s.contains(doubleDelim)) {
            s = s.replace(doubleDelim, SYMBOL_DELIM);
        }
        return s;
    }
}
