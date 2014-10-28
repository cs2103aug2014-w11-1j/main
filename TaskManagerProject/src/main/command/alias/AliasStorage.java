package main.command.alias;

import java.util.HashMap;

public class AliasStorage {
    private HashMap<String, CommandType> reservedMap;
    private HashMap<String, String> customMap;
    private final String fileName;
    private String fileHash = "";

    public AliasStorage(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return true iff there is a change in the file.
     */
    public boolean read() {
        if (fileUnchanged()) {
            return false;
        }

        HashMap<String, String> reservedAliases = readReservedAliasesFromFile();
        HashMap<String, String> customAliases = readCustomAliasesFromFile();

        updateReservedMap(reservedAliases);
        updateCustomMap(customAliases);

        fileHash = "tempLock";

        return true;
    }

    private HashMap<String, String> readCustomAliasesFromFile() {
        HashMap<String, String> customAliases = new HashMap<String, String>();

        customAliases.put("search", "show");
        customAliases.put("detail", "details");
        customAliases.put("quit", "exit");
        customAliases.put("d", "delete 1");

        return customAliases;
    }

    private HashMap<String, String> readReservedAliasesFromFile() {
        HashMap<String, String> reservedAliases = new HashMap<String, String>();

        String[] aliases = { "add", "show", "edit", "mark", "unmark", "status",
                "reschedule", "rename", "delete", "details", "undo", "redo",
                "back", "freeday", "exit", "report" };
        for (String s : aliases) {
            reservedAliases.put(s, s);
        }
        return reservedAliases;
    }

    private boolean fileUnchanged() {
        String currentHash = "tempLock";
        return fileHash.equals(currentHash);
    }

    public void updateCustomMap(HashMap<String, String> customCommands) {
        customMap = customCommands;
    }

    public void updateReservedMap(HashMap<String, String> reservedCommands) {
        assert reservedCommands != null;
        assert CommandType.values().length == reservedCommands.size();

        reservedMap = new HashMap<String, CommandType>();

        for (CommandType type : CommandType.values()) {
            String alias = reservedCommands.get(type.toString().toLowerCase());
            assert alias != null;
            reservedMap.put(alias, type);
        }
    }

    public CommandType getReservedCommand(String cmdString) {
        return reservedMap.get(cmdString);
    }

    public String getCustomCommand(String cmdString) {
        return customMap.get(cmdString);
    }
}
