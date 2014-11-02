package io.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import io.IFileInputOutput;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import taskline.TasklineLogger;

public class FileInputOutputTest {
    private static final Logger log = TasklineLogger.getLogger();

    @Test
    public void test() {
        testFileHash();
    }
    
    private void testFileHash() {
        String message = "Hello World! This is a test file!";
        
        try {
            FileWriter writer = new FileWriter("testFile.txt");
            writer.write(message);
            writer.flush();
            writer.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter("fileTest.txt");
            writer.write("THIS IS A WRONG TEST FILE!");
            writer.flush();
            writer.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        String correctHash = "6bd6955dce300739cdea07fa4fc574aa";
        
        String hash1 = IFileInputOutput.computeHash("testFile.txt");
        String hash2 = IFileInputOutput.computeHash("fileTest.txt");
        
        assertEquals(correctHash, hash1);
        assertEquals(correctHash.length(), hash2.length());
        assertFalse(correctHash.equals(hash2));
    }

}
