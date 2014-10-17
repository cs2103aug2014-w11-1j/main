package data.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
   EuclideanAlgorithmTest.class,
   TaskDataTest.class,
   TaskIdTest.class,
   TaskInfoTest.class
})
public class DataTestSuite {
}
