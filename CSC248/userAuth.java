import java.io.*;
import java.util.Scanner;

public class userAuth {

    private static final String FILE_NAME = "user.txt";

    /* ================= SIGN UP ================= */
    public static void signUp(Scanner in) {
        System.out.print("Enter username: ");
        String username = in.nextLine();

        if (userExists(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Enter phone number: ");
        String phone = in.nextLine();

        System.out.print("Enter age: ");
        String age = in.nextLine();

        System.out.print("Enter password: ");
        String password = in.nextLine();

        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            pw.println(username + "," + phone + "," + age + "," + password);
            System.out.println("Account created successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to user.txt");
        }
    }

    /* ================= LOGIN ================= */
    public static boolean login(Scanner in) {
        System.out.print("Username: ");
        String username = in.nextLine();

        System.out.print("Password: ");
        String password = in.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4 &&
                    data[0].equals(username) &&
                    data[3].equals(password)) {

                    System.out.println("Login successful. Welcome, " + username + "!");
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading user.txt");
        }

        System.out.println("Invalid username or password.");
        return false;
    }

    /* ================= CHECK USER ================= */
    private static boolean userExists(String username) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException ignored) {}

        return false;
    }
}