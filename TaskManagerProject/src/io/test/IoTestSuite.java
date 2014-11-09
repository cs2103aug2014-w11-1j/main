package io.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

//@author A0065475X
@RunWith(Suite.class)
@Suite.SuiteClasses({
    FileInputOutputTest.class,
    JsonReaderWriterTest.class,
    JsonItemParserTest.class
})
public class IoTestSuite {

}
