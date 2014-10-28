package main.command.alias;

import java.util.Scanner;

public class AliasController {
    private static final String SYMBOL_DELIM = " ";

    private AliasStorage aliasStorage;

    public AliasController(AliasStorage aliasStorage) {
        this.aliasStorage = aliasStorage;
    }

    public CommandString replaceAlias(String cmd) {
        cmd = cleanCmdString(cmd);
        beforeAliasCheck();

        StringBuilder sB = new StringBuilder();

        Scanner sc = new Scanner(cmd);
        while (sc.hasNext()) {
            String original = sc.next();
            String replacement = getCustomAlias(original);
            if (replacement != null) {
                sB.append(replacement);
            } else {
                sB.append(original);
            }
            sB.append(" ");
        }
        sc.close();

        return new CommandString(this, sB.toString().trim());
    }


    private void beforeAliasCheck() {
        aliasStorage.read();
    }

    public CommandType getReservedCommand(String cmdString) {
        return aliasStorage.getReservedCommand(cmdString.toLowerCase());
    }

    public String getCustomAlias(String possibleAlias) {
        return aliasStorage.getCustomCommand(possibleAlias.toLowerCase());
    }

    private static String cleanCmdString(String cmdString) {
        return stripExtraDelims(cmdString).trim();
    }

    private static String stripExtraDelims(String s) {
        String doubleDelim = SYMBOL_DELIM + SYMBOL_DELIM;
        while (s.contains(doubleDelim)) {
            s = s.replace(doubleDelim, SYMBOL_DELIM);
        }
        return s;
    }
}
