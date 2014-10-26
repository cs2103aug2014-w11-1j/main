package main.command.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TargetedCommandTest.class,
    AddCommandTest.class,
    SearchCommandTest.class,
    EditCommandTest.class
})
public class CommandTestSuite {
}
