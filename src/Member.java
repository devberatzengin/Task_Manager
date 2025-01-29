import java.io.*;
import java.util.Scanner;

public class Member extends AUser {
    private int memberID;
    private String memberUserName;
    private String memberPassword;
    private String memberEmail;

    Member(String text) {
        if (text.equalsIgnoreCase("new")) {
            register();
            this.taskManager = new TaskManager();
        }
    }

    Member() {
        this.taskManager = new TaskManager();
    }

    @Override
    public void register() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPlease enter your Email: ");
        memberEmail = scanner.nextLine().trim();

        System.out.print("\nPlease enter your Username: ");
        memberUserName = scanner.nextLine().trim();

        System.out.print("\nPlease enter your Password: ");
        memberPassword = scanner.nextLine().trim();

        this.memberID = getNextMemberID();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("member.txt", true))) {
            writer.write(this.memberID + ";" + this.memberEmail + ";" + this.memberUserName + ";" + this.memberPassword);
            writer.newLine();
        } catch (IOException exception) {
            System.out.println("❌ Error saving data: " + exception.getMessage());
        }

        System.out.println("✅ Registration successful! Your Member ID: " + this.memberID);
    }

    private int getNextMemberID() {
        int lastID = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("member.txt"))) {
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
        return lastID + 1;
    }

    @Override
    public void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPlease enter your Username: ");
        String loginUsername = scanner.nextLine().trim();
        System.out.print("\nPlease enter your Password: ");
        String loginPassword = scanner.nextLine().trim();

        try (BufferedReader reader = new BufferedReader(new FileReader("member.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String email = parts[1];
                    String username = parts[2];
                    String password = parts[3];
                    if (username.equals(loginUsername) && password.equals(loginPassword)) {
                        this.memberID = id;
                        this.memberEmail = email;
                        this.memberUserName = username;
                        this.memberPassword = password;
                        System.out.println("✅ Member Login Successful! Your Member ID: " + this.memberID);
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error reading the file: " + e.getMessage());
        }
        System.out.println("❌ Member Login Failed. Incorrect username or password.");
    }

    @Override
    public void changePassword() {
        System.out.print("\nPlease enter your New Password: ");
        Scanner scanner = new Scanner(System.in);
        String newPassword = scanner.nextLine().trim();
        updateMemberData(memberID, 3, newPassword);
    }

    @Override
    public void changeEmail() {
        System.out.print("\nPlease enter your New Email: ");
        Scanner scanner = new Scanner(System.in);
        String newEmail = scanner.nextLine().trim();
        updateMemberData(memberID, 1, newEmail);
    }

    @Override
    public void changeUserName() {
        System.out.print("\nPlease enter your New Username: ");
        Scanner scanner = new Scanner(System.in);
        String newUsername = scanner.nextLine().trim();
        updateMemberData(memberID, 2, newUsername);
    }

    private void updateMemberData(int id, int fieldIndex, String newValue) {
        File tempFile = new File("temp.txt");
        File originalFile = new File("member.txt");
        boolean updated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    try {
                        int memberFileID = Integer.parseInt(parts[0]);
                        if (memberFileID == id) {
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
            System.out.println("❌ Member ID not found.");
        }
    }
}
