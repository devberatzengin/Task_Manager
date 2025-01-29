import java.io.*;
import java.util.Scanner;

public class Admin extends AUser {

    private int adminID;
    private String adminUserName;
    private String adminPassword;
    private String adminEmail;

    Admin(String text) {
        if (text.equalsIgnoreCase("new")) {
            register();
            this.taskManager = new TaskManager();
        }
    }
    Admin() {
        this.taskManager = new TaskManager();
    }

    @Override
    public void register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPlease enter your Email: ");
        adminEmail = scanner.nextLine().trim();

        System.out.print("\nPlease enter your Username: ");
        adminUserName = scanner.nextLine().trim();

        System.out.print("\nPlease enter your Password: ");
        adminPassword = scanner.nextLine().trim();

        this.adminID = getNextAdminID(); //getNextAdminID() metotodu mevcut id ler arasından en buyuk olanun bir fazlasını döndürü o yüzden yeni register olacak olan kişinin id si budur


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("admin.txt", true))) {
            writer.write(this.adminID + ";" + this.adminEmail + ";" + this.adminUserName + ";" + this.adminPassword);
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("❌ Error saving data: " + exception.getMessage());
        }

        System.out.println("✅ Registration successful! Your Admin ID: " + this.adminID);
    }

    private int getNextAdminID() {
        int lastID = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0) {
                    try {
                        lastID = Math.max(lastID, Integer.parseInt(parts[0]));
                    } catch (NumberFormatException ignored) {}
                }
            }
        } catch (IOException ignored) {}
        return lastID + 1; //Yeni kullanıcının id bu olacaktır
    }

    @Override
    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPlease enter your Username: ");
        String loginUsername = scanner.nextLine().trim();
        System.out.print("\nPlease enter your Password: ");
        String loginPassword = scanner.nextLine().trim();

        try (BufferedReader reader = new BufferedReader(new FileReader("admin.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";"); //; ile ayırarak 4 ana bilgiye ulaşıyoruz
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]); // int a çeviriyoruz
                    String email = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    if (username.equals(loginUsername) && password.equals(loginPassword)) {
                        this.adminID = id;
                        this.adminEmail = email;
                        this.adminUserName = username;
                        this.adminPassword = password;
                        System.out.println("✅ Admin Login Successful! Your Admin ID: " + this.adminID);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading the file: " + e.getMessage());
        }
        System.out.println("❌ Admin Login Failed. Incorrect username or password.");
    }

    @Override
    public void changePassword() {
        System.out.print("\nPlease enter your New Password: ");
        Scanner scanner = new Scanner(System.in);
        String newPassword = scanner.nextLine().trim();
        updateAdminData(adminID, 3, newPassword);
    }

    @Override
    public void changeEmail() {
        System.out.print("\nPlease enter your New Email: ");
        Scanner scanner = new Scanner(System.in);
        String newEmail = scanner.nextLine().trim();
        updateAdminData(adminID, 1, newEmail);
    }

    @Override
    public void changeUserName() {
        System.out.print("\nPlease enter your New Username: ");
        Scanner scanner = new Scanner(System.in);
        String newUsername = scanner.nextLine().trim();
        updateAdminData(adminID, 2, newUsername);
    }

    private void updateAdminData(int id, int fieldIndex, String newValue) {
        File tempFile = new File("temp.txt");
        File originalFile = new File("admin.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    try {
                        int adminFileID = Integer.parseInt(parts[0]);
                        if (adminFileID == id) {                // id kısmı tuttugu taktirde belitilen indexteki değeri günceller
                            parts[fieldIndex] = newValue;
                            updated = true;
                        }
                    } catch (NumberFormatException ignored) {}
                }
                writer.write(String.join(";", parts));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("❌ Error updating file: " + e.getMessage());
            return;
        }

        if (updated) {
            originalFile.delete();
            tempFile.renameTo(originalFile);
            System.out.println("✅ Information updated successfully!");
        } else {
            tempFile.delete();
            System.out.println("❌ Admin ID not found.");
        }
    }
}
