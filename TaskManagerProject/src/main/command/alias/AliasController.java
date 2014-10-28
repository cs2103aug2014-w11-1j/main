package main.command.alias;

import java.util.Scanner;

import manager.ManagerHolder;

public class AliasController {
    private static final String SYMBOL_DELIM = " ";

    AliasManager aliasManager;

    public AliasController(ManagerHolder managerHolder) {
        aliasManager = managerHolder.getAliasManager();
    }

    public CommandString replaceAlias(String cmd) {
        cmd = cleanCmdString(cmd);
        aliasManager.beforeAliasCheck();

        StringBuilder sB = new StringBuilder();

        Scanner sc = new Scanner(cmd);
        while (sc.hasNext()) {
            String original = sc.next();
            String replacement = aliasManager.getCustomAlias(original);
            if (replacement != null) {
                sB.append(replacement);
            } else {
                sB.append(original);
            }
            sB.append(" ");
        }
        sc.close();

        return new CommandString(aliasManager, sB.toString().trim());
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
