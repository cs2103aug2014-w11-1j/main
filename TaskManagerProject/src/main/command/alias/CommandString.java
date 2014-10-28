package main.command.alias;

public class CommandString {
    private AliasData.CommandType type;
    private String alias;

    public CommandString(AliasManager aliasManager, String cmdString) {
        int delimIdx = cmdString.indexOf(' ');
        if (delimIdx == -1) {
            type = aliasManager.getReservedCommand(cmdString);
            if (type == null) {
                alias = cmdString;
            } else {
                alias = "";
            }
        } else {
            type = aliasManager.getReservedCommand(cmdString.substring(0, delimIdx));
            if (type == null) {
                alias = cmdString;
            } else {
                alias = cmdString.substring(delimIdx + 1);
            }
        }
    }

    public AliasData.CommandType getType() {
        return type;
    }

    public String getAlias() {
        return alias;
    }
}
