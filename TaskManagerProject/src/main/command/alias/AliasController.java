package main.command.alias;

import java.util.function.BiFunction;

import main.command.Command;
import manager.ManagerHolder;


public class AliasController {
    private static final String SYMBOL_DELIM = " ";

    private AliasStorage aliasStorage;

    public AliasController(AliasStorage aliasStorage) {
        this.aliasStorage = aliasStorage;
    }

    public String replaceAlias(String commandString) {
        commandString = cleanCmdString(commandString);
        beforeAliasCheck();

        if (commandString.length() == 0) {
            return commandString;
        }
        
        commandString = tryReplaceWithCustom(commandString);
        return commandString;
    }


    private void beforeAliasCheck() {
        //aliasFileInputOutput.read();
    }
    
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
        
        replacement = replacement.replaceAll(AliasStorage.VARIABLE_STRING_REGEX,
                argument);
        return replacement;
    }

    public BiFunction<String, ManagerHolder, Command> getReservedCommand(
            String cmdString) {
        return aliasStorage.getDefaultCommand(cmdString.toLowerCase());
    }

    public String getCustomAlias(String possibleAlias) {
        return aliasStorage.getCustomCommand(possibleAlias.toLowerCase());
    }

    private static String cleanCmdString(String cmdString) {
        return stripExtraDelims(cmdString).trim();
    }

    private static String stripExtraDelims(String s) {
        String doubleDelim = SYMBOL_DELIM + SYMBOL_DELIM;
        while (s.contains(doubleDelim)) {
            s = s.replace(doubleDelim, SYMBOL_DELIM);
        }
        return s;
    }
}
