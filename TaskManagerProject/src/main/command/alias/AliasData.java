package main.command.alias;

import java.util.HashMap;

public class AliasData {
    public enum CommandType {
        ADD,
        SHOW,
        EDIT,
        MARK,
        UNMARK,
        STATUS,
        RESCHEDULE,
        RENAME,
        DELETE,
        DETAILS,
        UNDO,
        REDO,
        BACK,
        FREEDAY,
        REPORT,
        EXIT;
    }

    HashMap<String, CommandType> reservedMap;
    HashMap<String, String> customMap;

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
