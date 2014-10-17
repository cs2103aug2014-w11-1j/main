package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    data.test.DataTestSuite.class,
    io.test.IoTestSuite.class,
    main.command.test.CommandTestSuite.class,
    main.formatting.test.FormatterTestSuite.class,
    manager.datamanager.searchfilter.test.FilterTestSuite.class,
    manager.datamanager.suggestion.test.SuggestionTestSuite.class,
    manager.datamanager.test.DataManagerTestSuite.class,
    manager.test.ManagerTestSuite.class
})
public class MainTestSuite {
}
