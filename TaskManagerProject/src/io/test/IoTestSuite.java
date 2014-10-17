package io.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    FileInputOutputTest.class,
    JsonReaderWriterTest.class
})
public class IoTestSuite {

}
