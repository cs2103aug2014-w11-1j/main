package main.command.alias;

/**
 * The interface used by File IO to synchronise the aliases in memory with an
 * external file.
 */
//@author A0065475X
public interface IAliasStorageFileInputOutput {

    /**
     * @return Get the entire list of custom aliases.
     */
    public abstract AliasValuePair[] getAllCustomAliases();

    /**
     * @return Get the entire list of default and custom aliases.
     */
    public abstract String[] getAllBindedStrings();

    /**
     * Registers a custom command, alias is assumed to be overridable.
     *
     * @param aliases Overwrites the original set of custom aliases with these.
     * @return true if successful. If unsuccessful, no change will be made to
     * AliasStorage.
     */
    public abstract boolean setAllCustomAliases(AliasValuePair[] aliases);

}