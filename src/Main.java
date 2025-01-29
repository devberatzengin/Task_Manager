import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Admin admin = new Admin();
        admin.login();

        admin.getTaskManager().create();

        
        admin.getTaskManager().listTasks();
        admin.getTaskManager().saveTasksToFile("tasks.txt");


    }
}