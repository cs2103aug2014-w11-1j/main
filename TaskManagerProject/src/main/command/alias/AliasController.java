package main.command.alias;

import java.util.Scanner;

import manager.ManagerHolder;

public class AliasController {
    Alias alias;

    public AliasController(ManagerHolder managerHolder) {
        //alias = new Alias(managerHolder.getAliasManager());
    }

    public CommandString replaceAlias(String cmd) {
        alias.refresh();
        // clean cmd

        StringBuilder sB = new StringBuilder();

        Scanner sc = new Scanner(cmd);
        while (sc.hasNext()) {
            sB.append(alias.parse(sc.next()));
        }
        sc.close();

        return new CommandString(sB.toString().trim());
    }
}
