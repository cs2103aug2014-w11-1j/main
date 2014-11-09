package hotkey;

import java.awt.*;

import static java.awt.event.KeyEvent.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Delayed;

 public class WindowsHotkeyRegister {


     public static void main( String[] args ) throws IOException {

    	 File f = new File("HotKeyRegister.txt");
    	 if(!f.exists())
    	 {
    	 try {
    		 
    		 String text = "HotKey Registered: CTRL + SHIFT + ALT + T";
    	        try {
    	          File file = new File("HotKeyRegister.txt");
    	          BufferedWriter output = new BufferedWriter(new FileWriter(file));
    	          output.write(text);
    	          output.close();
    	        } catch ( IOException e ) {
    	           e.printStackTrace();
    	        }
             Robot robot = new Robot();

             robot.keyPress( VK_ALT );
             robot.keyPress( VK_TAB );
             robot.delay(20);
             robot.keyRelease( VK_ALT );
             robot.keyRelease( VK_TAB );
             robot.delay(100);
             robot.keyPress(VK_UP);
             robot.delay(20);
             robot.keyRelease(VK_UP);
             robot.keyPress( VK_T );
             robot.delay(20);
             robot.keyRelease(VK_T);
             robot.keyPress(VK_SHIFT);
             robot.keyPress(VK_F10);
             robot.delay(20);
             robot.keyRelease(VK_SHIFT);
             robot.keyRelease(VK_F10);
             
             robot.keyPress(VK_S);
             robot.delay(20);
             robot.keyRelease(VK_S);
             robot.delay(200);
             
             robot.delay( 100 );
             robot.keyPress(VK_SHIFT);
             robot.keyPress(VK_F10);
             robot.delay(20);
             robot.keyRelease(VK_SHIFT);
             robot.keyRelease(VK_F10);
             
             robot.keyPress(VK_R);
             robot.delay(20);
             robot.keyRelease(VK_R);
             robot.delay(100);
             
             robot.keyPress(VK_TAB);
             robot.delay(20);
             robot.keyRelease(VK_TAB);
             robot.delay(100);
             robot.keyPress(VK_TAB);
             robot.delay(20);
             robot.keyRelease(VK_TAB);
             
             robot.delay(100);
             robot.keyPress( VK_ALT );
             robot.keyPress( VK_CONTROL );
             robot.keyPress( VK_SHIFT );
             robot.keyPress( VK_T );
             robot.delay(20);
             robot.keyRelease( VK_ALT );
             robot.keyRelease( VK_CONTROL );
             robot.keyRelease( VK_SHIFT );
             robot.keyRelease( VK_T );
             
             robot.keyPress(VK_ENTER);
             robot.delay(100);
             robot.keyPress( VK_ALT );
             robot.keyPress( VK_TAB );
             robot.delay(20);
             robot.keyRelease( VK_ALT );
             robot.keyRelease( VK_TAB );

         } catch( AWTException e ) {
             e.getMessage();
         }
    	 } 
     }

 }