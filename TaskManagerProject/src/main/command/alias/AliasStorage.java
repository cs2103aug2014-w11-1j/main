package main.command.alias;

import java.util.HashMap;
import java.util.function.BiFunction;

import main.command.AddCommand;
import main.command.ArgumentCommand;
import main.command.BackCommand;
import main.command.Command;
import main.command.DeleteCommand;
import main.command.DetailsCommand;
import main.command.EditCommand;
import main.command.EditCommand.ParseType;
import main.command.ExitCommand;
import main.command.FreeDaySearchCommand;
import main.command.RedoCommand;
import main.command.ReportCommand;
import main.command.SearchCommand;
import main.command.UndoCommand;
import manager.ManagerHolder;


public class AliasStorage {
    public static final String VARIABLE_STRING = "\\$";

    private HashMap<String, BiFunction<String, ManagerHolder, Command>> defaultMap;
    private HashMap<String, String> customMap;
    
    public AliasStorage() {
        defaultMap = new HashMap<>();
        customMap = new HashMap<>();
        
        setupDefaultCommands();
    }

    public BiFunction<String, ManagerHolder, Command> getDefaultCommand(
            String commandString) {
        
        BiFunction<String, ManagerHolder, Command> makeCommandFunction;
        
        makeCommandFunction = defaultMap.get(commandString);
        if (makeCommandFunction == null) {
            return defaultMakeCommand(commandString);
        } else {
            return defaultMap.get(commandString);
        }
    }

    public String getCustomCommand(String cmdString) {
        return customMap.get(cmdString);
    }
    
    public boolean putCustomCommand(String alias, String replacement) {
        String[] keywords = replacement.split(" ", 2);
        if (keywords.length == 1) {
            return put(alias, keywords[0] + " " + VARIABLE_STRING);
        } else {
            return put(alias, replacement);
        }
    }
    
    private BiFunction<String, ManagerHolder, Command> defaultMakeCommand(
            String input) {
        
        return (args, managerHolder) ->
                new ArgumentCommand(input + " " + args, managerHolder);
    }
    
    private void setupDefaultCommands() {

        setDefaultCommands(
                (args, managerHolder) -> new AddCommand(args, managerHolder),
                "add", "create");
        
        setDefaultCommands(
                (args, managerHolder) -> new SearchCommand(args, managerHolder),
                "show", "search", "ls");

        setDefaultCommands(
                (args, managerHolder) -> new EditCommand(args, managerHolder),
                "edit", "set", "modify", "change");

        setDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.MARK),
                "mark");

        setDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.UNMARK),
                "unmark");
        
        setDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.STATUS),
                "status");

        setDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.RESCHEDULE),
                "reschedule");

        setDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.RENAME),
                "rename");

        setDefaultCommands(
                (args, managerHolder) -> new UndoCommand(managerHolder),
                "undo");
        
        setDefaultCommands(
                (args, managerHolder) -> new RedoCommand(managerHolder),
                "redo");

        setDefaultCommands(
                (args, managerHolder) -> new ReportCommand(managerHolder),
                "report");

        setDefaultCommands(
                (args, managerHolder) ->
                new FreeDaySearchCommand(args, managerHolder),
                "freeday");

        setDefaultCommands(
                (args, managerHolder) -> new DeleteCommand(args, managerHolder),
                "delete", "del", "remove", "rm");

        setDefaultCommands(
                (args, managerHolder) -> new DetailsCommand(args, managerHolder),
                "detail", "details");

        setDefaultCommands(
                (args, managerHolder) -> new BackCommand(managerHolder),
                "back", "return");

        setDefaultCommands(
                (args, managerHolder) -> new ExitCommand(managerHolder),
                "back", "exit");
        
    }
    
    private void setDefaultCommands(
            BiFunction<String, ManagerHolder, Command> commandFunction,
            String...commandStrings) {
        
        for (String commandString : commandStrings) {
            defaultMap.put(commandString, commandFunction);
        }
    }
    
    private boolean put(String alias, String value) {
        boolean alreadyHas = customMap.containsKey(alias);
        customMap.put(alias, value);
        
        return alreadyHas;
    }
}
