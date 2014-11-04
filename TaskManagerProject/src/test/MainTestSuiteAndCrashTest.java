package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs both the main test suite and the crash tester.<br>
 * I usually run both separately because the crash tester can take a while.<br>
 * But this is good for seeing total test coverage.
 */
//@author A0065475X
@RunWith(Suite.class)
@Suite.SuiteClasses({
    MainTestSuite.class,
    CrashTester.class
})
public class MainTestSuiteAndCrashTest {
}
