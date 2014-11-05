package manager.datamanager.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@author A0065475X
@RunWith(Suite.class)
@Suite.SuiteClasses({
    UndoManagerTest.class,
    FreeDayManagerTest.class,
    FreeTimeManagerTest.class,
    SearchManagerTest.class
})
public class DataManagerTestSuite {
}
