import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Member currentMember = null;
        Admin currentAdmin = null;

        while (true) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Register");
            System.out.println("2. Login as Member");
            System.out.println("3. Login as Admin");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    currentMember = new Member("new");
                    break;
                case 2:
                    currentMember = new Member();
                    currentMember.login();
                    if (currentMember.getTaskManager() != null) {
                        taskMenu(currentMember);
                    }
                    break;
                case 3:
                    currentAdmin = new Admin();
                    if (currentAdmin.login()) {
                        adminMenu(currentAdmin);
                    }
                    break;
                case 4:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void taskMenu(Member member) {
        Scanner scanner = new Scanner(System.in);
        TaskManager taskManager = member.getTaskManager();

        while (true) {
            System.out.println("\n===== TASK MENU =====");
            System.out.println("1. Add Task");
            System.out.println("2. List Tasks");
            System.out.println("3. Find Task");
            System.out.println("4. Update Task Status");
            System.out.println("5. Change Username");
            System.out.println("6. Change Email");
            System.out.println("7. Change Password");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    taskManager.addTask();
                    break;
                case 2:
                    taskManager.listTasks();
                    break;
                case 3:
                    taskManager.findTask();
                    break;
                case 4:
                    taskManager.setTaskStatus();
                    break;
                case 5:
                    member.changeUserName();
                    break;
                case 6:
                    member.changeEmail();
                    break;
                case 7:
                    member.changePassword();
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void adminMenu(Admin admin) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Tasks");
            System.out.println("2. Delete Task");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    admin.viewAllTasks();
                    break;
                case 2:
                    admin.deleteTask();
                    break;
                case 3:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
