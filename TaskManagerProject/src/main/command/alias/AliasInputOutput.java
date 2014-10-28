package main.command.alias;

import java.util.HashMap;

public class AliasInputOutput {

    private final String fileName;
    private String fileHash = "";

    private final AliasData aliasData;

    public AliasInputOutput(AliasData aliasData, String fileName) {
        this.fileName = fileName;
        this.aliasData = aliasData;
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

        aliasData.updateReservedMap(reservedAliases);
        aliasData.updateCustomMap(customAliases);

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
}
