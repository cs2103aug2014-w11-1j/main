package main.formatting.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@author A0065475X
@RunWith(Suite.class)
@Suite.SuiteClasses({
    AddSuccessfulFormatterTest.class,
    DeleteSuccessfulFormatterTest.class,
    EditSuccessfulFormatterTest.class,
    ReportFormatterTest.class,
    SearchModeFormatterTest.class,
    WaitingModeFormatterTest.class
})
public class FormatterTestSuite {
}
