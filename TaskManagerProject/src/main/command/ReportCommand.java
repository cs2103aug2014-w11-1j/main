package main.command;

import manager.ManagerHolder;
import manager.datamanager.ReportManager;
import manager.result.Result;

//@author A0065475X
public class ReportCommand extends Command {
    private final ReportManager reportManager;

    public ReportCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        reportManager = managerHolder.getReportManager();
    }

    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canGetReport();
    }

    @Override
    protected Result executeAction() {
        Result result = reportManager.report();
        return result;
    }

}
