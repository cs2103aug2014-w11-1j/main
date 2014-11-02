package main.command.alias;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiFunction;

import main.command.AddCommand;
import main.command.AliasCommand;
import main.command.AliasDeleteCommand;
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
    // Variable string: \$
    public static final String VARIABLE_STRING_REGEX = "\\\\\\$";
    public static final String VARIABLE_STRING = "\\$";

    private HashSet<String> unoverridableStringSet;
    private HashMap<String, BiFunction<String, ManagerHolder, Command>> defaultMap;
    private HashMap<String, String> customMap;
    
    public AliasStorage() {
        defaultMap = new HashMap<>();
        customMap = new HashMap<>();
        unoverridableStringSet = new HashSet<>();
        
        initialiseUnoverriableStrings();
        initialiseDefaultCommands();
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
    
    /**
     * @param alias from this keyword (must be one word)
     * @param replacement to this string
     * @return the string the alias is binded to.
     */
    public String createCustomCommand(String alias, String replacement) {
        assert canOverride(alias);
        
        String[] keywords = replacement.split(" ", 2);
        String value;
        if (keywords.length == 1) {
            value = keywords[0] + " " + VARIABLE_STRING;
        } else {
            value = replacement;
        }
        
        if (put(alias, value)) {
            return value;
        } else {
            return null;
        }
    }
    
    /**
     * @param alias alias string to test.
     * @return true iff this alias is overridable. (e.g. custom is not
     * overridable)
     */
    public boolean canOverride(String alias) {
        return !unoverridableStringSet.contains(alias);
    }
    
    /**
     * @param alias delete the custom command that uses this alias
     * @return the value the alias is binded to. Returns null iff alias did
     * not originally exist
     */
    public String deleteCustomCommand(String alias) {
        return customMap.remove(alias);
    }
    
    private BiFunction<String, ManagerHolder, Command> defaultMakeCommand(
            String input) {
        
        return (args, managerHolder) ->
                new ArgumentCommand(input + " " + args, managerHolder);
    }
    
    private void initialiseDefaultCommands() {

        defineDefaultCommands(
                (args, managerHolder) -> new AddCommand(args, managerHolder),
                "add", "create");
        
        defineDefaultCommands(
                (args, managerHolder) -> new SearchCommand(args, managerHolder),
                "show", "search", "ls");

        defineDefaultCommands(
                (args, managerHolder) -> new EditCommand(args, managerHolder),
                "edit", "set", "modify", "change");

        defineDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.MARK),
                "mark");

        defineDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.UNMARK),
                "unmark");
        
        defineDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.STATUS),
                "status");

        defineDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.RESCHEDULE),
                "reschedule");

        defineDefaultCommands(
                (args, managerHolder) ->
                new EditCommand(args, managerHolder, ParseType.RENAME),
                "rename");

        defineDefaultCommands(
                (args, managerHolder) -> new UndoCommand(managerHolder),
                "undo");
        
        defineDefaultCommands(
                (args, managerHolder) -> new RedoCommand(managerHolder),
                "redo");

        defineDefaultCommands(
                (args, managerHolder) -> new ReportCommand(managerHolder),
                "report");

        defineDefaultCommands(
                (args, managerHolder) ->
                new FreeDaySearchCommand(args, managerHolder),
                "freeday");

        defineDefaultCommands(
                (args, managerHolder) -> new DeleteCommand(args, managerHolder),
                "delete", "del", "remove", "rm");

        defineDefaultCommands(
                (args, managerHolder) -> new DetailsCommand(args, managerHolder),
                "detail", "details");

        defineDefaultCommands(
                (args, managerHolder) -> new BackCommand(managerHolder),
                "back", "return");

        defineDefaultCommands(
                (args, managerHolder) -> new ExitCommand(managerHolder),
                "back", "exit");

        defineDefaultCommands(
                (args, managerHolder) -> new AliasCommand(args, managerHolder),
                "alias", "custom");

        defineDefaultCommands(
                (args, managerHolder) -> new AliasDeleteCommand(args, managerHolder),
                "unalias");
        
    }
    
    private void initialiseUnoverriableStrings() {
        unoverridableStringSet.add("custom");
        unoverridableStringSet.add("alias");
    }
    
    
    private void defineDefaultCommands(
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
