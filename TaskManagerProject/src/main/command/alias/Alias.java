package main.command.alias;

import java.util.HashMap;

public class Alias {
    public enum CommandType {
        ADD("add"),
        SHOW("show"),
        EDIT("edit"),
        MARK("mark"),
        UNMARK("unmark"),
        STATUS("status"),
        RESCHEDULE("reschedule"),
        DELETE("delete"),
        DETAILS("details"),
        UNDO("undo"),
        REDO("redo"),
        BACK("back"),
        FREEDAY("freeday"),
        EXIT("exit");

        String type;

        CommandType(String type) {
            this.type = type;
        }
    }

    HashMap<String, CommandType> reservedMap;
    HashMap<String, String> customMap;

    public Alias(String name, String command, boolean isCustom) {
        assert name.equals(name.trim());
    }

    public void refresh() {
        // TODO get data from aliasManager if needed
        buildReservedMap();
        buildCustomMap();
    }

    private void buildCustomMap() {
        customMap = new HashMap<String, String>();
        // TODO Auto-generated method stub

    }

    private void buildReservedMap() {
        reservedMap = new HashMap<String, CommandType>();
        HashMap<String, String> defaultCommandsMap = new HashMap<String, String>();
        //reservedMapCommandType.valueOf(arg0)
        // TODO Auto-generated method stub
        for (CommandType type : CommandType.values()) {
            //reservedMap.put(type.type), type);
        }
    }

    public String parse(String next) {
        if (customMap.containsKey(next)) {
            return customMap.get(next);
        }
        return next;
    }
}
