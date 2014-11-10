package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@author A0065475X
/**
 * Runs both the main test suite and the crash tester.<br>
 * I usually run both separately because the crash tester can take a while.<br>
 * But this is good for seeing total test coverage.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    MainTestSuite.class,
    SingleInstanceCrashTester.class
})
public class MainTestSuiteAndCrashTest {
}
