package taskline;

import io.AliasFileInputOutput;
import io.FileInputOutput;
import io.IFileInputOutput;

import java.io.IOException;

import jline.ArgumentCompletor;
import jline.Completor;
import jline.NullCompletor;
import jline.SimpleCompletor;
import main.MainController;
import main.command.alias.AliasStorage;
import manager.ManagerHolder;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.ansi;
import org.fusesource.jansi.Ansi.Color;

import ui.UIDisplay;
import data.AutoCompleteDictionary;
import data.TaskData;

/**
 * Does the main initialization of the program structure.
 *
 */
//@author A0065475X
public class Taskline {

    public static void main(String[] args) throws IOException {
        AnsiConsole.systemInstall();
        
        System.out.println(ansi().fg(Color.RED).a("The quick brown fox jumps over the lazy dog."));
        System.out.println();
        System.out.println(ansi().fg(Color.GREEN).a("The quick brown fox jumps over the lazy dog."));
        System.out.println();
        System.out.println(ansi().fg(Color.CYAN).a("The quick brown fox jumps over the lazy dog."));
        System.out.println();
        System.out.println(ansi().fg(Color.YELLOW).a("The quick brown fox jumps over the lazy dog."));
        System.out.println();
        System.out.println(ansi().fg(Color.BLUE).a("The quick brown fox jumps over the lazy dog."));
        System.out.println();
        System.out.println(ansi().fg(Color.MAGENTA).a("The quick brown fox jumps over the lazy dog."));
        System.out.println();
        System.out.println(ansi().reset());
/*        TasklineLogger.setupLogger();

        String fileName = "tasks.txt";
        String aliasFileName = "alias.txt";

        SimpleCompletor simpleCompletor = new SimpleCompletor(new String[]{});
        AutoCompleteDictionary autoCompleteDictionary =
                new AutoCompleteDictionary(simpleCompletor);
        
        ArgumentCompletor argumentCompletor = new ArgumentCompletor(
                new Completor[]{
                        simpleCompletor, 
                        new NullCompletor()});
        
        AliasStorage aliasStorage = new AliasStorage();
        IFileInputOutput aliasFileInputOutput = new AliasFileInputOutput(
                aliasStorage, aliasFileName, autoCompleteDictionary);

        TaskData taskData = new TaskData();
        IFileInputOutput fileInputOutput =
                new FileInputOutput(taskData, fileName);
        
        ManagerHolder managerHolder = new ManagerHolder(taskData,
                fileInputOutput, aliasStorage, aliasFileInputOutput);
        MainController mainController = new MainController(managerHolder,
                aliasStorage, aliasFileInputOutput);
        
        UIDisplay uiDisplay = new UIDisplay(mainController, argumentCompletor);

        startCommandLoop(uiDisplay);
        TasklineLogger.closeLoggerFileHandler();*/
    }

    private static void startCommandLoop(UIDisplay uiDisplay) {
        while (!uiDisplay.isReadyToExit()) {
            uiDisplay.commandLoopIteration();
        }
    }
}
