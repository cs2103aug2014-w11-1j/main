package manager.datamanager.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    UndoManagerTest.class,
    FreeDayManagerTest.class,
    FreeTimeManagerTest.class
})
public class DataManagerTestSuite {
}
