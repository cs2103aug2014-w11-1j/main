package main.command.alias;

public class CommandString {
    private CommandType type;
    private String alias;

    public CommandString(AliasController aliasController, String cmdString) {
        int delimIdx = cmdString.indexOf(' ');
        if (delimIdx == -1) {
            type = aliasController.getReservedCommand(cmdString);
            if (type == null) {
                alias = cmdString;
            } else {
                alias = "";
            }
        } else {
            type = aliasController.getReservedCommand(cmdString.substring(0, delimIdx));
            if (type == null) {
                alias = cmdString;
            } else {
                alias = cmdString.substring(delimIdx + 1);
            }
        }
    }

    public CommandType getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }
}
