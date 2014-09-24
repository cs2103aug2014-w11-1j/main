package io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class FileInputOutputTest {

    @Test
    public void test() {
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
        
        String hash1 = FileInputOutput.computeHash("testFile.txt");
        String hash2 = FileInputOutput.computeHash("fileTest.txt");
        
        assertEquals(correctHash, hash1);
        assertEquals(correctHash.length(), hash2.length());
        assertFalse(correctHash.equals(hash2));
    }

}
