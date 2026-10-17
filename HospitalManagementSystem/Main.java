import java.util.Scanner;

/**
 * Main.java
 * ---------------------------------------------------------------------
 * Entry point of the application. Compile and run with:
 *
 *      javac *.java
 *      java Main
 *
 * Flow:
 *   1. Show the Authorization/Login screen (Authentication module).
 *   2. If access is granted, launch the Hospital Management System's
 *      main menu (Singleton instance).
 *   3. If the user fails 3 login attempts, terminate the program.
 * ---------------------------------------------------------------------
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean accessGranted = Authentication.authenticate(scanner);

        if (accessGranted) {
            HospitalManagementSystem hms = HospitalManagementSystem.getInstance();
            hms.run(scanner);
        }

        scanner.close();
    }
}
