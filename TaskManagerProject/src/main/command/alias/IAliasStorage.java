package main.command.alias;

import java.util.function.BiFunction;

import main.command.Command;
import manager.ManagerHolder;

//@author A0065475X
public interface IAliasStorage {

    public abstract BiFunction<String, ManagerHolder, Command> getDefaultCommand(
            String commandString);

    public abstract String getCustomCommand(String cmdString);

    /**
     * @param alias from this keyword (must be one word)
     * @param replacement to this string
     * @return the string the alias is binded to.
     */
    public abstract String createCustomCommand(String alias, String replacement);

    /**
     * @param alias alias string to test.
     * @return true iff this alias is overridable. (e.g. custom is not
     * overridable)
     */
    public abstract boolean canOverride(String alias);

    /**
     * @param alias delete the custom command that uses this alias
     * @return the value the alias is binded to. Returns null iff alias did
     * not originally exist
     */
    public abstract String deleteCustomCommand(String alias);

    /**
     * @param alias check this alias (key).
     * @return true iff the alias is already binded to something
     */
    public abstract boolean isAlreadyBinded(String alias);

    /**
     * @return Get the entire list of custom aliases.
     */
    public abstract AliasValuePair[] getAllCustomAliases();
}