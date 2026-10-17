import java.util.Scanner;

/**
 * Authentication.java
 * ---------------------------------------------------------------------
 * Handles the mandatory login/authorization screen that must be passed
 * before the Hospital Management System's main menu is shown.
 *
 * The password is hardcoded (acceptable for this project). The user is
 * given a maximum of 3 attempts before the program terminates.
 * ---------------------------------------------------------------------
 */
public class Authentication {

    // Hardcoded system password
    private static final String PASSWORD = "admin123";
    private static final int MAX_ATTEMPTS = 3;

    /**
     * Runs the login flow. Returns true if access was granted, false if
     * the user exhausted all attempts (in which case the caller should
     * terminate the program).
     */
    public static boolean authenticate(Scanner scanner) {
        printBanner();

        int attemptsLeft = MAX_ATTEMPTS;

        while (attemptsLeft > 0) {
            System.out.print("Enter Password: ");
            String enteredPassword;
            try {
                enteredPassword = scanner.nextLine();
            } catch (Exception e) {
                // Handles any unexpected input stream issues
                System.out.println("Invalid input. Please try again.");
                attemptsLeft--;
                continue;
            }

            if (enteredPassword != null && enteredPassword.equals(PASSWORD)) {
                System.out.println();
                System.out.println("Access Granted");
                System.out.println("Welcome Authorized User");
                System.out.println();
                return true;
            } else {
                attemptsLeft--;
                System.out.println();
                System.out.println("Incorrect Password.");
                System.out.println("Access Denied.");
                if (attemptsLeft > 0) {
                    System.out.println("Attempts remaining: " + attemptsLeft);
                }
                System.out.println();
            }
        }

        System.out.println("Too many failed attempts.");
        System.out.println("Program Terminated.");
        return false;
    }

    private static void printBanner() {
        System.out.println("=====================================");
        System.out.println("     HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("         AUTHORIZATION");
        System.out.println();
    }
}
