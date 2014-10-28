package main.command.alias;

public class AliasManager {
    AliasData aliasData;
    AliasInputOutput aliasIO;

    public AliasManager(AliasData aliasData) {
        this.aliasData = aliasData;
        aliasIO = new AliasInputOutput(aliasData, "");
    }

    public void beforeAliasCheck() {
        aliasIO.read();
    }

    public AliasData.CommandType getReservedCommand(String cmdString) {
        return aliasData.getReservedCommand(cmdString.toLowerCase());
    }

    public String getCustomAlias(String possibleAlias) {
        return aliasData.getCustomCommand(possibleAlias.toLowerCase());
    }

}
