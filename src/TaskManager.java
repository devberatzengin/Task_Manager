import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private static final String TASKS_FILE = "tasks.txt";
    private ArrayList<Task> tasks = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    /*
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
*/

    public void listTasks() {
        tasks.clear(); // Önceki görevleri temizle, tekrar eklenmesini önle

        boolean taskFound = false; // En az bir görev olup olmadığını kontrol etmek için

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    int taskId = Integer.parseInt(parts[0]);
                    String taskName = parts[1];
                    String description = parts[2];
                    int status = Integer.parseInt(parts[3].replace("%", ""));
                    String completionStatus = parts[4];

                    tasks.add(new Task(taskId, taskName, description, status)); // Sadece listeye ekle, ekrana yazdırma

                    // Görevi istenilen formatta yazdır
                    System.out.println("ID: " + taskId +
                            " | Name: " + taskName +
                            " | Description: " + description +
                            " | Status: " + status + "% " + completionStatus);
                    taskFound = true;
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error loading tasks: " + e.getMessage());
        }

        if (!taskFound) {
            System.out.println("No tasks available.");
        }
    }
    public boolean deleteTaskById(int taskId) {
        List<String> updatedTasks = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (Integer.parseInt(parts[0]) == taskId) {
                    found = true;
                    continue; // Bu satırı dosyaya yazmayarak silmiş oluyoruz
                }
                updatedTasks.add(line);
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
            return false;
        }

        if (!found) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (String task : updatedTasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + e.getMessage());
            return false;
        }

        return true;
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
    public void addTask() {
        System.out.print("\nEnter task name: ");
        String taskName = sc.nextLine().trim();
        System.out.print("Enter task description: ");
        String description = sc.nextLine().trim();
        System.out.print("Enter task status (0-100): ");
        int status = Integer.parseInt(sc.nextLine().trim());

        int taskId = getNextTaskID();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE, true))) {
            if (status<0 || status>100) {
                System.out.println("Invalid input. Please enter a number between 0 and 100.");
            }
            writer.write(taskId + ";" + taskName + ";" + description +";");
            writer.write(status+"%;");
            if (status==100){
                writer.write("Completed");
            }else{
                writer.write("Not Completed");
            }
            writer.newLine();
            System.out.println("✅ Task added successfully! Task ID: " + taskId);
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + e.getMessage());
        }
        this.saveTasksToFile(TASKS_FILE);
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
    public void findTask() {
        System.out.print("\nEnter task id to search: ");
        int taskId = sc.nextInt();
        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
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
    public void setTaskStatus() {
        System.out.print("Enter task status id for update: ");
        int taskId = sc.nextInt();
        System.out.print("Enter new task status for " + taskId + "' task: ");
        int status = sc.nextInt();

        List<String> updatedTasks = new ArrayList<>();

        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 3) {
                    int id = Integer.parseInt(parts[0]);
                    if (id == taskId) {
                        parts[3] = status + "%";
                        parts[4] = (status == 100) ? "Completed" : "Not Completed";
                        found = true;
                        System.out.println("✅ Task updated successfully! ID: " + parts[0] + ", Name: " + parts[1] + ", Description: " + parts[2] + ", Status: " + parts[3]);
                    }
                }
                updatedTasks.add(String.join(";", parts));
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("❌ Task not found. Update failed.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASKS_FILE))) {
            for (String task : updatedTasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error writing to file: " + e.getMessage());
        }
        this.saveTasksToFile(TASKS_FILE);
    }
}