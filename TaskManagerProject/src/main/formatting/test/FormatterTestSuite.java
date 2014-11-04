package main.formatting.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    EditSuccessfulFormatterTest.class,
    SearchModeFormatterTest.class,
    AddSuccessfulFormatterTest.class,
    DeleteSuccessfulFormatterTest.class,
    EditSuccessfulFormatterTest.class,
    ReportFormatterTest.class,
})
public class FormatterTestSuite {
}
