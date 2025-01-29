import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TaskManager {
    private static final String TASKS_FILE = "tasks.txt";
    private ArrayList<Task> tasks = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public void create() {
        System.out.print("\nEnter task name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter task description: ");
        String description = sc.nextLine().trim();

        int status;
        while (true) {
            System.out.print("Enter task status (0-100): ");
            try {
                status = Integer.parseInt(sc.nextLine().trim());
                if (status >= 0 && status <= 100) break;
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid input. Please enter a number between 0 and 100.");
        }

        Task newTask = new Task(name, description, status);
        tasks.add(newTask);
        System.out.println("\n✅ Task created successfully!");
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void saveTasksToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            for (Task task : tasks) {
                writer.write(task.toString() + "\n");
            }
            System.out.println("\n✅ Tasks saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void addTask(String taskName, String description) {
        int taskId = getNextTaskID();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {
            writer.write(taskId + ";" + taskName + ";" + description);
            writer.newLine();
            System.out.println("✅ Task added successfully! Task ID: " + taskId);
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + e.getMessage());
        }
    }

    private int getNextTaskID() {
        int lastID = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0) {
                    try {
                        lastID = Math.max(lastID, Integer.parseInt(parts[0]));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException ignored) {
        }
        return lastID + 1;
    }

    public void findTask(int taskId) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    if (id == taskId) {
                        System.out.println("✅ Task Found: ID: " + parts[0] + ", Name: " + parts[1] + ", Description: " + parts[2]);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
        }
        System.out.println("❌ Task not found.");
    }
}