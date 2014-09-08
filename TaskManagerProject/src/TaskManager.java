import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {
        print("Welcome to Taskline.");
		print("");
		
		printInput("add lunch with Jim at \"Café 5 O’clock\" on Tuesday 12 PM");
		print("New task recorded!");
		print("  Time: 12:00 PM 17/02/15");
		print("  Task: lunch with Jim at Cafe 5 O'clock");
		print("");
		print("Type \"x\" to undo last task.");
		print("");
		
		printInput("show");
		print("1) Task: lunch with Jim at Cafe 5 O'clock");
		print("   Date/Time: 12:00 PM 17/02/15");
		print("   Tagged: taskline, lunch");
		print("   Priority: High");
		print("");

		System.out.print("> ");
		(new Scanner(System.in)).next();
		/*
		System.out.println("Hello world!");
        System.out.println("I am a task manager!");
        System.out.println("I manage tasks!");
        System.out.println("You have no tasks to do today!");*/
    }
	
	public static void print(String message) {
		System.out.println(message);
	}
	
	public static void printInput(String message) {
		print("> " + message);
	}
}
