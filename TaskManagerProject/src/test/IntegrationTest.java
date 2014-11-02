package test;

import static org.junit.Assert.assertEquals;
import io.FileInputOutput;
import io.IFileInputOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.MainController;
import main.command.alias.AliasStorage;
import main.command.alias.IAliasStorage;
import manager.ManagerHolder;

import org.junit.After;
import org.junit.Test;

import data.TaskData;

public class IntegrationTest {
    private static final String NEWL = "\r\n";
    private static final String TEST_FILENAME = "testTasks.txt";

    @After
    public void after() {
        deleteTestFile();
    }
    
    @Test
    public void initialiseTest() {

        String fileName = TEST_FILENAME;
        deleteTestFile();

        String aliasFileName = "testAlias.txt";
        IAliasStorage aliasStorage = new AliasStorage();

        TaskData taskData = new TaskData();
        IFileInputOutput fileInputOutput = new FileInputOutput(taskData, fileName);
        ManagerHolder managerHolder = new ManagerHolder(taskData, fileInputOutput, aliasStorage);
        MainController mainController = new MainController(managerHolder, aliasStorage);
        
        test(mainController);
    }

    private void deleteTestFile() {
        try {
            Path path = Paths.get(TEST_FILENAME);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void test(MainController mainController) {
        String input;
        String output;
        String expected = "a";
        
        input = "add orange +high";
        expected = "Task orange added sucessfully." + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);

        input = "add purple 2pm 14 Oct 2014";
        expected = "Task purple added sucessfully." + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);
        
        input = "add green";
        expected = "Task green added sucessfully." + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);
        
        input = "add violet 2pm 14 Oct 2014 4pm 14 Oct 2014";
        expected = "Task violet added sucessfully." + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);
        
        input = "show";
        expected = "Tue, 14 Oct 2014 ---" + NEWL +
                    "1) [   14:00   ] purple                                                 - [PO9]" + NEWL +
                    "2) [14:00-16:00] violet                                                 - [1JL]" + NEWL +
                    "Floating Tasks ---" + NEWL +
                    "3)               green                                                  - [Z5M]" + NEWL +
                    "4)               orange                                                 - [4GQ]" + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);
        
        input = "delete green";
        expected = "Task green deleted." + NEWL +
                "Tue, 14 Oct 2014 ---" + NEWL +
                "1) [   14:00   ] purple                                                 - [PO9]" + NEWL +
                "2) [14:00-16:00] violet                                                 - [1JL]" + NEWL +
                "Floating Tasks ---" + NEWL +
                "3)               orange                                                 - [4GQ]" + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);

        input = "report";
        expected = 
                "You have 1 tasks today, and 1 tasks tomorrow." + NEWL +
                "Below are the high-priority tasks." + NEWL +
                "Floating Tasks ---" + NEWL +
                "1)               orange" + NEWL;
        output = mainController.runCommand(input);
        assertSame(expected, output);
    }
    
    private void assertSame(String expected, String output) {
        assertEquals(expected.trim(), output.trim());
    }
}
