package main.command.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@author A0065475X
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TargetedCommandTest.class,
    AddCommandTest.class,
    SearchCommandTest.class,
    EditCommandTest.class
})
public class CommandTestSuite {
}
